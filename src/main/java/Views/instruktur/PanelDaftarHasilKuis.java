package Views.instruktur;

import DataStore.DataStore;
import Model.HasilKuis;
import Model.Instruktur;
import Model.Kuis;
import Model.Pengguna;
import Model.Peserta;
import Util.TabelUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class PanelDaftarHasilKuis extends JPanel {

    private String idInstrukturAktif;
    private JTable tabelDaftar;
    private DefaultTableModel modelTabel;
    private JComboBox<String> cbFilterStatus;
    private JLabel lblInfoJumlah;

    public PanelDaftarHasilKuis(String idInstruktur) {
        this.idInstrukturAktif = idInstruktur;
        initComponents();
        loadData();
    }

    private String resolveIdInstruktur() {
        if (idInstrukturAktif == null) {
            return null;
        }
        for (Pengguna pgn : DataStore.daftarPengguna) {
            if (pgn instanceof Instruktur) {
                Instruktur ins = (Instruktur) pgn;
                if ((ins.getIdInstruktur() != null && ins.getIdInstruktur().equals(idInstrukturAktif))
                        || (ins.getUsername() != null && ins.getUsername().equals(idInstrukturAktif))) {
                    return ins.getIdInstruktur();
                }
            }
        }
        return idInstrukturAktif;
    }

    private boolean milikInstrukturIni(HasilKuis h, String idAsliInstruktur, Peserta peserta) {
        if (idAsliInstruktur == null || idAsliInstruktur.isEmpty()) {
            return true;
        }

        // 1. Cek berdasarkan pembuat kuis di tabel kuis
        for (Kuis k : DataStore.daftarKuis) {
            if (k.getIdKuis() != null && k.getIdKuis().equals(h.getIdKuis())) {
                return idAsliInstruktur.equals(k.getIdInstruktur());
            }
        }

        // 2. Fallback jika kuis sudah dihapus: cek instruktur pendamping peserta
        if (peserta != null && peserta.getInstrukturDamping() != null) {
            return idAsliInstruktur.equals(peserta.getInstrukturDamping().getIdInstruktur());
        }

        return false;
    }

    public void loadData() {
        DataStore.muatUlang();

        modelTabel.setRowCount(0);
        String idAsli = resolveIdInstruktur();
        String filterStatus = (cbFilterStatus != null && cbFilterStatus.getSelectedItem() != null)
                ? cbFilterStatus.getSelectedItem().toString()
                : "Semua Status";

        int no = 1;
        for (HasilKuis h : DataStore.daftarHasilKuis) {
            Peserta peserta = DataStore.cariPeserta(h.getIdPeserta());

            if (!milikInstrukturIni(h, idAsli, peserta)) {
                continue;
            }

            String statusHasil = h.getStatus();
            if (!"Semua Status".equals(filterStatus) && !filterStatus.equalsIgnoreCase(statusHasil)) {
                continue;
            }

            String namaSiswa = (peserta != null) ? peserta.getNamaLengkap() : "Tidak Diketahui (" + h.getIdPeserta() + ")";

            modelTabel.addRow(new Object[]{
                no++,
                h.getIdHasil(),
                namaSiswa,
                h.getJudulKuis(),
                h.getTanggalPengerjaan(),
                h.getNilaiAkhir(),
                statusHasil
            });
        }

        if (lblInfoJumlah != null) {
            lblInfoJumlah.setText("Total: " + modelTabel.getRowCount() + " hasil kuis");
        }
        TabelUtil.autoResizeKolom(tabelDaftar);
    }

    private void bukaPanelPenilaian() {
        int baris = tabelDaftar.getSelectedRow();
        if (baris == -1) {
            JOptionPane.showMessageDialog(this, "Pilih dahulu data siswa dari tabel!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int barisModel = tabelDaftar.convertRowIndexToModel(baris);
        String idHasilTerpilih = modelTabel.getValueAt(barisModel, 1).toString();
        HasilKuis hasilTerpilih = null;

        for (HasilKuis h : DataStore.daftarHasilKuis) {
            if (h.getIdHasil().equals(idHasilTerpilih)) {
                hasilTerpilih = h;
                break;
            }
        }

        if (hasilTerpilih == null) {
            JOptionPane.showMessageDialog(this, "Data hasil kuis tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Peserta pesertaTerpilih = DataStore.cariPeserta(hasilTerpilih.getIdPeserta());
        if (pesertaTerpilih == null) {
            JOptionPane.showMessageDialog(this, "Data peserta untuk hasil kuis ini tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Window ancestor = SwingUtilities.getWindowAncestor(this);
        if (ancestor instanceof InstrukturFrame) {
            ((InstrukturFrame) ancestor).showPanel(
                    new PanelPenilaianHasilKuis(hasilTerpilih, pesertaTerpilih, idInstrukturAktif)
            );
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(248, 248, 248));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // HEADER
        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.setOpaque(false);

        JPanel panelJudul = new JPanel(new BorderLayout(0, 3));
        panelJudul.setOpaque(false);
        JLabel lblJudul = new JLabel("Daftar Hasil Kuis Masuk");
        lblJudul.setFont(new Font("Inter", Font.BOLD, 18));
        panelJudul.add(lblJudul, BorderLayout.NORTH);

        lblInfoJumlah = new JLabel("Total: 0 hasil kuis");
        lblInfoJumlah.setFont(new Font("Inter", Font.PLAIN, 11));
        lblInfoJumlah.setForeground(new Color(110, 110, 110));
        panelJudul.add(lblInfoJumlah, BorderLayout.SOUTH);

        panelAtas.add(panelJudul, BorderLayout.WEST);

        JPanel panelFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelFilter.setOpaque(false);

        cbFilterStatus = new JComboBox<>(new String[]{
            "Semua Status",
            HasilKuis.STATUS_MENUNGGU_PENILAIAN,
            HasilKuis.STATUS_SELESAI_DINILAI
        });
        cbFilterStatus.setFont(new Font("Inter", Font.PLAIN, 12));
        cbFilterStatus.addActionListener(e -> loadData());
        panelFilter.add(cbFilterStatus);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Inter", Font.PLAIN, 12));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadData());
        panelFilter.add(btnRefresh);

        panelAtas.add(panelFilter, BorderLayout.EAST);
        add(panelAtas, BorderLayout.NORTH);

        // TABEL
        modelTabel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"No.", "ID Hasil", "Nama Peserta", "Judul Kuis", "Tgl Pengerjaan", "Skor", "Status"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelDaftar = new JTable(modelTabel);
        tabelDaftar.setRowHeight(35);
        tabelDaftar.setFont(new Font("Inter", Font.PLAIN, 12));
        tabelDaftar.setSelectionBackground(new Color(255, 230, 230));
        tabelDaftar.setSelectionForeground(new Color(122, 0, 0));

        tabelDaftar.getTableHeader().setBackground(new Color(122, 0, 0));
        tabelDaftar.getTableHeader().setForeground(Color.WHITE);
        tabelDaftar.getTableHeader().setFont(new Font("Inter", Font.BOLD, 12));
        tabelDaftar.getTableHeader().setOpaque(true);

        tabelDaftar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    bukaPanelPenilaian();
                }
            }
        });

        JScrollPane scrollTabel = new JScrollPane(tabelDaftar);
        add(scrollTabel, BorderLayout.CENTER);

        // FOOTER (Tombol)
        JPanel panelBawah = new JPanel();
        panelBawah.setOpaque(false);

        JButton btnNilai = new JButton("Beri Penilaian / Lihat Detail");
        btnNilai.setBackground(new Color(122, 0, 0));
        btnNilai.setForeground(Color.WHITE);
        btnNilai.setFont(new Font("Inter", Font.BOLD, 12));
        btnNilai.setFocusPainted(false);
        btnNilai.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNilai.addActionListener(e -> bukaPanelPenilaian());

        panelBawah.add(btnNilai);
        add(panelBawah, BorderLayout.SOUTH);
    }
}