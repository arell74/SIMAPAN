package Views.instruktur;

import Model.HasilKuis;
import Model.JawabanSoal;
import Model.Peserta;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class FormPenilaianHasilKuis extends javax.swing.JFrame {

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

    public FormPenilaianHasilKuis(Frame parent, boolean modal, HasilKuis hasil, Peserta peserta, String idInstruktur) {
        this.hasilAktif = hasil;
        this.pesertaAktif = peserta;
        this.idInstrukturAktif = idInstruktur;

        initComponents();
        setTitle("Form Penilaian Hasil Kuis");
        setSize(780, 620);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        muatDataHeader();
        muatTabelJawaban();
        muatCatatan();
    }

    private void muatDataHeader() {
        if (this.hasilAktif != null && this.pesertaAktif != null) {
            String idPeserta = this.hasilAktif.getIdPeserta();
            lblIdHeader.setText("ID Peserta: " + idPeserta);
            lblNamaHeader.setText("Penilaian Kuis Peserta"); 
        } else {
            lblNamaHeader.setText("Belum Ada Data Terpilih");
            lblIdHeader.setText("Silakan pilih peserta dari tabel terlebih dahulu.");
        }
    }

    private void muatTabelJawaban() {
        modelJawaban.setRowCount(0);
        
        // PENGAMAN: Jika data kosong, hentikan proses muat tabel
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
        perbaruiPreviewNilai();
    }

    private void muatCatatan() {
        // PENGAMAN: Jika data kosong, biarkan text area kosong
        if (this.hasilAktif == null) {
            txtCatatan.setText("");
            return;
        }
        txtCatatan.setText(hasilAktif.getCatatanInstruktur() == null ? "" : hasilAktif.getCatatanInstruktur());
    }

    private void perbaruiPreviewNilai() {
        // PENGAMAN: Jika data kosong, tampilkan default
        if (this.hasilAktif == null) {
            lblNilaiPreview.setText("Total Poin: - / -     |     Perkiraan Nilai Akhir: -");
            return;
        }
        
        int totalMaks = hasilAktif.getTotalPoinMaksimal();
        int totalDapat = 0;
        for (int i = 0; i < modelJawaban.getRowCount(); i++) {
            try {
                totalDapat += Integer.parseInt(modelJawaban.getValueAt(i, KOLOM_POIN_DIPEROLEH).toString());
            } catch (NumberFormatException ignore) {
                // Kolom sedang diketik ulang oleh user, abaikan dulu sampai valid
            }
        }
        int nilai = (totalMaks == 0) ? 0 : Math.round((totalDapat * 100.0f) / totalMaks);
        lblNilaiPreview.setText("Total Poin: " + totalDapat + " / " + totalMaks + "     |     Perkiraan Nilai Akhir: " + nilai);
    }

    private void simpanPenilaian() {
        // PENGAMAN: Jangan simpan jika tidak ada data
        if (this.hasilAktif == null) {
            JOptionPane.showMessageDialog(this, 
                    "Tidak ada data peserta yang dinilai!", 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ArrayList<JawabanSoal> daftarJawabanAsli = hasilAktif.getDaftarJawaban();

        // Validasi dulu semua baris Essay sebelum benar-benar menyimpan apa pun,
        // supaya tidak ada perubahan setengah-setengah kalau ada input yang salah.
        int[] poinBaru = new int[daftarJawabanAsli.size()];
        for (int i = 0; i < daftarJawabanAsli.size(); i++) {
            JawabanSoal j = daftarJawabanAsli.get(i);
            if (j.isPilihanGanda()) {
                poinBaru[i] = j.getPoinDiperoleh(); // PG tidak diubah
                continue;
            }

            String isiPoin = modelJawaban.getValueAt(i, KOLOM_POIN_DIPEROLEH).toString().trim();
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

        // Semua valid, baru diterapkan ke objek JawabanSoal aslinya.
        for (int i = 0; i < daftarJawabanAsli.size(); i++) {
            JawabanSoal j = daftarJawabanAsli.get(i);
            if (!j.isPilihanGanda()) {
                j.setPoinDiperoleh(poinBaru[i]);
                j.setSudahDinilai(true);
            }
        }

        hasilAktif.setCatatanInstruktur(txtCatatan.getText().trim());

        muatTabelJawaban();
        JOptionPane.showMessageDialog(this,
                "Penilaian berhasil disimpan!\nStatus hasil kuis: " + hasilAktif.getStatus());
    }

    // ==================== UI (ditulis manual, bukan generated Matisse) ====================

    private void initComponents() {
        getContentPane().setLayout(new BorderLayout());

        // ---------- HEADER ----------
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(MAROON);
        panelHeader.setPreferredSize(new Dimension(780, 64));
        panelHeader.setLayout(null);

        lblNamaHeader = new JLabel("-");
        lblNamaHeader.setForeground(Color.WHITE);
        lblNamaHeader.setFont(new Font("Inter", Font.BOLD, 15));
        lblNamaHeader.setBounds(20, 10, 600, 22);
        panelHeader.add(lblNamaHeader);

        lblIdHeader = new JLabel("-");
        lblIdHeader.setForeground(Color.WHITE);
        lblIdHeader.setFont(new Font("Inter", Font.PLAIN, 11));
        lblIdHeader.setBounds(20, 32, 600, 16);
        panelHeader.add(lblIdHeader);

        JButton btnTutupHeader = new JButton("X");
        btnTutupHeader.setBackground(new Color(160, 20, 20));
        btnTutupHeader.setForeground(Color.WHITE);
        btnTutupHeader.setBorderPainted(false);
        btnTutupHeader.setFocusPainted(false);
        btnTutupHeader.setBounds(730, 17, 30, 30);
        btnTutupHeader.addActionListener(e -> dispose());
        panelHeader.add(btnTutupHeader);

        getContentPane().add(panelHeader, BorderLayout.NORTH);

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
                // Hanya kolom "Poin Didapat" yang bisa diedit, dan hanya untuk soal Essay.
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
        panelCatatan.setPreferredSize(new Dimension(780, 130));

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

        getContentPane().add(panelBody, BorderLayout.CENTER);

        // ---------- FOOTER ----------
        JPanel panelFooter = new JPanel();
        panelFooter.setBackground(new Color(248, 248, 248));

        JButton btnSimpan = new JButton("Simpan Penilaian");
        btnSimpan.setBackground(MAROON);
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.addActionListener(e -> simpanPenilaian());
        panelFooter.add(btnSimpan);

        JButton btnTutup = new JButton("Tutup");
        btnTutup.setFocusPainted(false);
        btnTutup.addActionListener(e -> dispose());
        panelFooter.add(btnTutup);

        getContentPane().add(panelFooter, BorderLayout.SOUTH);
    }
}