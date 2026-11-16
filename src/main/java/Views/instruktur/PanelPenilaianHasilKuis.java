package Views.instruktur;

import DataStore.DataStore;
import Database.HasilKuisDAO;
import Model.HasilKuis;
import Model.JawabanSoal;
import Model.Peserta;
import Util.TabelUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class PanelPenilaianHasilKuis extends javax.swing.JPanel {

    private static final Color MAROON = new Color(122, 0, 0);
    private static final int KOLOM_POIN_DIPEROLEH = 6;

    private final HasilKuis hasilAktif;
    private final Peserta pesertaAktif;
    private final String idInstrukturAktif;

    private JLabel lblNamaHeader;
    private JLabel lblIdHeader;
    private JTable tabelJawaban;
    private DefaultTableModel modelJawaban;
    private JLabel lblNilaiPreview;
    private JTextArea txtCatatan;

    public PanelPenilaianHasilKuis(HasilKuis hasil, Peserta peserta, String idInstruktur) {
        this.hasilAktif = hasil;
        this.pesertaAktif = (peserta != null)
                ? peserta
                : (hasil != null ? DataStore.cariPeserta(hasil.getIdPeserta()) : null);
        this.idInstrukturAktif = idInstruktur;

        initComponents();

        muatDataHeader();
        muatTabelJawaban();
        muatCatatan();
    }

    private void muatDataHeader() {
        if (this.hasilAktif != null) {
            String idPeserta = this.hasilAktif.getIdPeserta();
            String namaPeserta = (this.pesertaAktif != null) ? this.pesertaAktif.getNamaLengkap() : idPeserta;
            String judulKuis = (this.hasilAktif.getJudulKuis() != null) ? this.hasilAktif.getJudulKuis() : "Kuis";

            lblNamaHeader.setText(judulKuis + " — " + namaPeserta);
            lblIdHeader.setText("ID Peserta: " + idPeserta
                    + "  ·  Tanggal Pengerjaan: " + this.hasilAktif.getTanggalPengerjaan()
                    + "  ·  Status: " + this.hasilAktif.getStatus());
        } else {
            lblNamaHeader.setText("Belum Ada Data Terpilih");
            lblIdHeader.setText("Silakan kembali dan pilih peserta dari tabel terlebih dahulu.");
        }
    }

    private void muatTabelJawaban() {
        modelJawaban.setRowCount(0);

        if (this.hasilAktif == null || this.hasilAktif.getDaftarJawaban() == null) {
            return;
        }

        int no = 1;
        for (JawabanSoal j : hasilAktif.getDaftarJawaban()) {
            String jawabanSiswa = (j.getJawabanSiswa() == null || j.getJawabanSiswa().isEmpty())
                    ? "(tidak dijawab)" : j.getJawabanSiswa();

            modelJawaban.addRow(new Object[]{
                no++,
                j.getTipeSoal(),
                j.getPertanyaan(),
                jawabanSiswa,
                j.getKunciJawaban(),
                j.getPoinMaksimal(),
                j.getPoinDiperoleh(),
                j.isSudahDinilai() ? "Sudah Dinilai" : "Belum Dinilai"
            });
        }
        TabelUtil.autoResizeKolom(tabelJawaban);
        perbaruiPreviewNilai();
    }

    private void muatCatatan() {
        if (this.hasilAktif == null) {
            txtCatatan.setText("");
            return;
        }
        txtCatatan.setText(hasilAktif.getCatatanInstruktur() == null ? "" : hasilAktif.getCatatanInstruktur());
    }

    private void perbaruiPreviewNilai() {
        if (this.hasilAktif == null) {
            lblNilaiPreview.setText("Total Poin: - / -     |     Perkiraan Nilai Akhir: -");
            return;
        }

        int totalMaks = hasilAktif.getTotalPoinMaksimal();
        int totalDapat = 0;
        for (int i = 0; i < modelJawaban.getRowCount(); i++) {
            try {
                totalDapat += Integer.parseInt(modelJawaban.getValueAt(i, KOLOM_POIN_DIPEROLEH).toString().trim());
            } catch (NumberFormatException ignore) {
            }
        }
        int nilai = (totalMaks == 0) ? 0 : Math.round((totalDapat * 100.0f) / totalMaks);
        lblNilaiPreview.setText("Total Poin: " + totalDapat + " / " + totalMaks + "     |     Perkiraan Nilai Akhir: " + nilai);
    }

    private void simpanPenilaian() {
        if (this.hasilAktif == null) {
            JOptionPane.showMessageDialog(this,
                    "Tidak ada data peserta yang dinilai!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Pastikan perubahan terakhir di sel tabel langsung ter-commit sebelum dibaca
        if (tabelJawaban.isEditing() && tabelJawaban.getCellEditor() != null) {
            tabelJawaban.getCellEditor().stopCellEditing();
        }

        ArrayList<JawabanSoal> daftarJawabanAsli = hasilAktif.getDaftarJawaban();

        // 1. Validasi seluruh baris Essay terlebih dahulu
        int[] poinBaru = new int[daftarJawabanAsli.size()];
        for (int i = 0; i < daftarJawabanAsli.size(); i++) {
            JawabanSoal j = daftarJawabanAsli.get(i);
            if (j.isPilihanGanda()) {
                poinBaru[i] = j.getPoinDiperoleh();
                continue;
            }

            Object rawPoin = modelJawaban.getValueAt(i, KOLOM_POIN_DIPEROLEH);
            String isiPoin = (rawPoin != null) ? rawPoin.toString().trim() : "";
            int poin;
            try {
                poin = Integer.parseInt(isiPoin);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this,
                        "Poin soal Essay nomor " + (i + 1) + " harus berupa angka.",
                        "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (poin < 0 || poin > j.getPoinMaksimal()) {
                JOptionPane.showMessageDialog(this,
                        "Poin soal Essay nomor " + (i + 1) + " harus di antara 0 dan " + j.getPoinMaksimal() + ".",
                        "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
                return;
            }
            poinBaru[i] = poin;
        }

        // 2. Terapkan nilai ke objek JawabanSoal & catatan ke HasilKuis
        for (int i = 0; i < daftarJawabanAsli.size(); i++) {
            JawabanSoal j = daftarJawabanAsli.get(i);
            if (!j.isPilihanGanda()) {
                j.setPoinDiperoleh(poinBaru[i]);
                j.setSudahDinilai(true);
            }
        }
        hasilAktif.setCatatanInstruktur(txtCatatan.getText().trim());

        // 3. Simpan perubahan secara permanen ke MySQL
        if (HasilKuisDAO.ubah(hasilAktif)) {
            DataStore.muatUlang();
            muatDataHeader();
            muatTabelJawaban();

            JOptionPane.showMessageDialog(this,
                    "Penilaian berhasil disimpan ke database!\n"
                    + "Nilai Akhir: " + hasilAktif.getNilaiAkhir() + "\n"
                    + "Status hasil kuis: " + hasilAktif.getStatus(),
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } else {
            DataStore.muatUlang();
        }
    }

    private void kembaliKeDaftar() {
        Window ancestor = SwingUtilities.getWindowAncestor(this);
        if (ancestor instanceof InstrukturFrame) {
            ((InstrukturFrame) ancestor).showPanel(new PanelDaftarHasilKuis(idInstrukturAktif));
        }
    }

    // ==================== UI ====================

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 248, 248));

        // ---------- HEADER ----------
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(MAROON);
        panelHeader.setPreferredSize(new Dimension(100, 64));
        panelHeader.setLayout(null);

        lblNamaHeader = new JLabel("-");
        lblNamaHeader.setForeground(Color.WHITE);
        lblNamaHeader.setFont(new Font("Inter", Font.BOLD, 15));
        lblNamaHeader.setBounds(20, 10, 880, 22);
        panelHeader.add(lblNamaHeader);

        lblIdHeader = new JLabel("-");
        lblIdHeader.setForeground(Color.WHITE);
        lblIdHeader.setFont(new Font("Inter", Font.PLAIN, 11));
        lblIdHeader.setBounds(20, 32, 880, 16);
        panelHeader.add(lblIdHeader);

        add(panelHeader, BorderLayout.NORTH);

        // ---------- BODY ----------
        JPanel panelBody = new JPanel();
        panelBody.setLayout(new BorderLayout(0, 10));
        panelBody.setBackground(new Color(248, 248, 248));
        panelBody.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel judulTabel = new JLabel("DAFTAR JAWABAN PESERTA");
        judulTabel.setForeground(MAROON);
        judulTabel.setFont(new Font("Inter", Font.BOLD, 11));
        panelBody.add(judulTabel, BorderLayout.NORTH);

        modelJawaban = new DefaultTableModel(
                new Object[][]{},
                new String[]{"No.", "Tipe", "Pertanyaan", "Jawaban Peserta", "Kunci/Referensi", "Poin Maks", "Poin Didapat", "Status"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column != KOLOM_POIN_DIPEROLEH) {
                    return false;
                }
                Object tipe = getValueAt(row, 1);
                return tipe != null && tipe.toString().equalsIgnoreCase("Essay");
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 5 || columnIndex == 6) {
                    return Object.class;
                }
                return String.class;
            }
        };

        modelJawaban.addTableModelListener(e -> {
            if (e.getColumn() == KOLOM_POIN_DIPEROLEH) {
                perbaruiPreviewNilai();
            }
        });

        tabelJawaban = new JTable(modelJawaban);
        tabelJawaban.setRowHeight(48);
        tabelJawaban.setFont(new Font("Inter", Font.PLAIN, 12));
        tabelJawaban.setSelectionBackground(new Color(255, 230, 230));
        tabelJawaban.setSelectionForeground(MAROON);
        javax.swing.table.JTableHeader header = tabelJawaban.getTableHeader();
        header.setBackground(MAROON);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Inter", Font.BOLD, 12));
        header.setOpaque(true);

        JScrollPane scrollJawaban = new JScrollPane(tabelJawaban);
        panelBody.add(scrollJawaban, BorderLayout.CENTER);

        lblNilaiPreview = new JLabel("Total Poin: - / -     |     Perkiraan Nilai Akhir: -");
        lblNilaiPreview.setFont(new Font("Inter", Font.BOLD, 13));
        lblNilaiPreview.setForeground(MAROON);
        lblNilaiPreview.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));

        JPanel panelCatatan = new JPanel(new BorderLayout(0, 6));
        panelCatatan.setBorder(BorderFactory.createTitledBorder("Catatan / Feedback untuk Peserta"));
        panelCatatan.setBackground(Color.WHITE);
        panelCatatan.setPreferredSize(new Dimension(100, 130));

        txtCatatan = new JTextArea(4, 20);
        txtCatatan.setLineWrap(true);
        txtCatatan.setWrapStyleWord(true);
        JScrollPane scrollCatatan = new JScrollPane(txtCatatan);
        panelCatatan.add(scrollCatatan, BorderLayout.CENTER);

        JPanel panelBawah = new JPanel(new BorderLayout());
        panelBawah.setBackground(new Color(248, 248, 248));
        panelBawah.add(lblNilaiPreview, BorderLayout.NORTH);
        panelBawah.add(panelCatatan, BorderLayout.CENTER);

        panelBody.add(panelBawah, BorderLayout.SOUTH);

        add(panelBody, BorderLayout.CENTER);

        // ---------- FOOTER ----------
        JPanel panelFooter = new JPanel();
        panelFooter.setBackground(new Color(248, 248, 248));

        JButton btnKembali = new JButton("Kembali");
        btnKembali.setFocusPainted(false);
        btnKembali.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnKembali.addActionListener(e -> kembaliKeDaftar());
        panelFooter.add(btnKembali);

        JButton btnSimpan = new JButton("Simpan Penilaian");
        btnSimpan.setBackground(MAROON);
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSimpan.addActionListener(e -> simpanPenilaian());
        panelFooter.add(btnSimpan);

        add(panelFooter, BorderLayout.SOUTH);
    }
}