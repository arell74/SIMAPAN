package Views.instruktur;

import DataStore.DataStore;
import Database.CatatanEvaluasiDAO;
import Database.PesertaDAO;
import Database.SeleksiDAO;
import Model.CatatanEvaluasi;
import Model.Instruktur;
import Model.Pengguna;
import Model.Peserta;
import Model.Seleksi;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Form Evaluasi Peserta.
 * Dibuka dari baris yang dipilih di tabel PanelInputNilai.
 * Terhubung ke tabel seleksi, peserta, dan catatan_evaluasi di MySQL.
 *
 * @author arelssi
 */
public class FormEvaluasiPeserta extends javax.swing.JDialog {

    private static final Color MAROON = new Color(122, 0, 0);

    private final Peserta pesertaAktif;
    private final String idInstrukturAsli;

    private JTable tabelRiwayatSeleksi;
    private DefaultTableModel modelRiwayat;

    private JComboBox<String> cbJenisSeleksi;
    private JTextField txtTanggalSeleksi;
    private JTextField txtNilaiSeleksi;
    private JCheckBox chkGugurPermanen;
    private JButton btnSimpanNilai;

    private JTextArea txtCatatanBaru;
    private JTextArea txtRiwayatCatatan;

    public FormEvaluasiPeserta(java.awt.Frame parent, boolean modal, Peserta peserta, String idInstruktur) {
        super(parent, modal);
        this.pesertaAktif = peserta;
        this.idInstrukturAsli = resolveIdInstruktur(idInstruktur);

        initComponents();
        setTitle("Form Evaluasi Peserta");
        setSize(740, 610);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        DataStore.muatUlang();
        muatDataHeader();
        muatRiwayatSeleksi();
        muatRiwayatCatatan();
    }

    /**
     * Memastikan ID instruktur yang dipakai sesuai kolom PK tabel instruktur (mis. INS001),
     * meskipun parameter yang dikirim berupa username login.
     */
    private String resolveIdInstruktur(String idAtauUsername) {
        if (idAtauUsername == null) {
            return null;
        }
        for (Pengguna pgn : DataStore.daftarPengguna) {
            if (pgn instanceof Instruktur) {
                Instruktur ins = (Instruktur) pgn;
                if ((ins.getIdInstruktur() != null && ins.getIdInstruktur().equals(idAtauUsername))
                        || (ins.getUsername() != null && ins.getUsername().equals(idAtauUsername))) {
                    return ins.getIdInstruktur();
                }
            }
        }
        return idAtauUsername;
    }

    private void muatDataHeader() {
        if (pesertaAktif == null) {
            return;
        }
        lblNamaHeader.setText(pesertaAktif.getNamaLengkap());
        lblIdHeader.setText(pesertaAktif.getIdPeserta() + " · " + safe(pesertaAktif.getKelas()) + " · Level " + safe(pesertaAktif.getLevelBahasa()));
        lblStatusSaatIni.setText("Status Seleksi Saat Ini: " + safe(pesertaAktif.getStatusSeleksi()));
    }

    private String safe(String s) {
        return (s == null || s.isEmpty()) ? "-" : s;
    }

    private void muatRiwayatSeleksi() {
        modelRiwayat.setRowCount(0);
        if (pesertaAktif == null) {
            return;
        }
        int jumlahPercobaan = 0;
        for (Seleksi s : DataStore.daftarSeleksi) {
            if (s.getPeserta() != null && s.getPeserta().getIdPeserta().equals(pesertaAktif.getIdPeserta())) {
                jumlahPercobaan++;
                modelRiwayat.addRow(new Object[]{
                    s.getTanggalSeleksi(),
                    s.getJenisSeleksi(),
                    s.getNilai(),
                    s.getStatusHasil()
                });
            }
        }
        if (modelRiwayat.getRowCount() == 0) {
            modelRiwayat.addRow(new Object[]{"-", "Belum ada riwayat seleksi", "-", "-"});
        }

        // Kunci form input seleksi jika percobaan sudah mencapai 3x atau status sudah final
        boolean sudahFinal = "Lulus".equalsIgnoreCase(pesertaAktif.getStatusSeleksi())
                || "Gugur".equalsIgnoreCase(pesertaAktif.getStatusSeleksi())
                || "Gugur Permanen".equalsIgnoreCase(pesertaAktif.getStatusSeleksi());
        boolean terkunci = jumlahPercobaan >= 3 || sudahFinal;

        cbJenisSeleksi.setEnabled(!terkunci);
        txtTanggalSeleksi.setEnabled(!terkunci);
        txtNilaiSeleksi.setEnabled(!terkunci);
        chkGugurPermanen.setEnabled(!terkunci);
        btnSimpanNilai.setEnabled(!terkunci);
        if (terkunci) {
            btnSimpanNilai.setText(sudahFinal ? "Status Seleksi Final" : "Batas 3x Percobaan");
        }
    }

    private void muatRiwayatCatatan() {
        if (pesertaAktif == null) {
            txtRiwayatCatatan.setText("");
            return;
        }
        StringBuilder sb = new StringBuilder();
        ArrayList<CatatanEvaluasi> milikPeserta = new ArrayList<>();
        for (CatatanEvaluasi c : DataStore.daftarCatatanEvaluasi) {
            if (c.getPeserta() != null && c.getPeserta().getIdPeserta().equals(pesertaAktif.getIdPeserta())) {
                milikPeserta.add(c);
            }
        }
        if (milikPeserta.isEmpty()) {
            txtRiwayatCatatan.setText("Belum ada catatan evaluasi untuk peserta ini.");
            return;
        }
        for (int i = milikPeserta.size() - 1; i >= 0; i--) {
            CatatanEvaluasi c = milikPeserta.get(i);
            Instruktur ins = DataStore.cariInstruktur(c.getIdInstruktur());
            String namaIns = (ins != null) ? ins.getNamaLengkap() + " (" + c.getIdInstruktur() + ")" : c.getIdInstruktur();

            sb.append("[").append(c.getTanggal()).append(" · ").append(namaIns).append("]\n");
            sb.append(c.getIsiCatatan()).append("\n");
            sb.append("------------------------------------------------------\n");
        }
        txtRiwayatCatatan.setText(sb.toString());
        txtRiwayatCatatan.setCaretPosition(0);
    }

    private String generateIdCatatan() {
        int max = 0;
        for (CatatanEvaluasi c : DataStore.daftarCatatanEvaluasi) {
            String id = c.getIdCatatan();
            if (id != null && id.toUpperCase().startsWith("CTT")) {
                try {
                    int angka = Integer.parseInt(id.substring(3).trim());
                    if (angka > max) {
                        max = angka;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return String.format("CTT%03d", max + 1);
    }

    private void simpanNilaiSeleksi() {
        if (pesertaAktif == null) {
            return;
        }

        Object jenisObj = cbJenisSeleksi.getSelectedItem();
        String jenis = (jenisObj != null) ? jenisObj.toString().trim() : "";
        String tanggal = (txtTanggalSeleksi.getText() == null) ? "" : txtTanggalSeleksi.getText().trim();

        if (jenis.isEmpty() || tanggal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Jenis seleksi dan tanggal tidak boleh kosong.",
                    "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate.parse(tanggal, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException dtpe) {
            JOptionPane.showMessageDialog(this, "Format tanggal harus YYYY-MM-DD, contoh: 2026-11-16",
                    "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int nilai;
        try {
            nilai = Integer.parseInt(txtNilaiSeleksi.getText().trim());
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Nilai harus berupa angka bulat.",
                    "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (nilai < 0 || nilai > 100) {
            JOptionPane.showMessageDialog(this, "Nilai harus berada di rentang 0 - 100.",
                    "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Hitung percobaan ke berapa saat ini
        int jumlahSebelumnya = 0;
        for (Seleksi s : DataStore.daftarSeleksi) {
            if (s.getPeserta() != null && s.getPeserta().getIdPeserta().equals(pesertaAktif.getIdPeserta())) {
                jumlahSebelumnya++;
            }
        }

        String statusHasil;
        if (chkGugurPermanen.isSelected()) {
            statusHasil = "Gugur Permanen";
        } else if (nilai >= 70) {
            statusHasil = "Lulus";
        } else if (jumlahSebelumnya + 1 >= 3) {
            statusHasil = "Gugur Permanen";
        } else {
            statusHasil = "Mengulang";
        }

        String idSeleksi = SeleksiDAO.idBerikutnya();
        Seleksi seleksiBaru = new Seleksi(idSeleksi, pesertaAktif, jenis, tanggal, nilai, statusHasil);

        if (SeleksiDAO.tambah(seleksiBaru)) {
            if ("Lulus".equals(statusHasil)) {
                pesertaAktif.setStatusSeleksi("Lulus");
                pesertaAktif.setStatusKeberangkatan("Menunggu Dokumen");
            } else if ("Gugur Permanen".equals(statusHasil)) {
                pesertaAktif.setStatusSeleksi("Gugur");
                pesertaAktif.setStatusKeberangkatan("Dibatalkan");
            } else {
                pesertaAktif.setStatusSeleksi("Mengulang");
            }
            PesertaDAO.ubah(pesertaAktif);
            DataStore.muatUlang();

            muatRiwayatSeleksi();
            muatDataHeader();
            txtNilaiSeleksi.setText("");
            chkGugurPermanen.setSelected(false);

            JOptionPane.showMessageDialog(this, "Nilai seleksi berhasil disimpan ke database!");
        }
    }

    private void simpanCatatanEvaluasi() {
        if (pesertaAktif == null) {
            return;
        }

        String isi = (txtCatatanBaru.getText() == null) ? "" : txtCatatanBaru.getText().trim();
        if (isi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Catatan evaluasi tidak boleh kosong.",
                    "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idCatatan = generateIdCatatan();
        String tanggalHariIni = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        CatatanEvaluasi catatanBaru = new CatatanEvaluasi(idCatatan, pesertaAktif, idInstrukturAsli, tanggalHariIni, isi);

        if (CatatanEvaluasiDAO.tambah(catatanBaru)) {
            DataStore.muatUlang();
            muatRiwayatCatatan();
            txtCatatanBaru.setText("");

            JOptionPane.showMessageDialog(this, "Catatan evaluasi berhasil disimpan ke database!");
        }
    }

    // ==================== UI (ditulis manual, bukan generated Matisse) ====================

    private JLabel lblNamaHeader;
    private JLabel lblIdHeader;
    private JLabel lblStatusSaatIni;

    private void initComponents() {
        getContentPane().setLayout(new BorderLayout());

        lblStatusSaatIni = new JLabel("Status Seleksi Saat Ini: -");
        lblStatusSaatIni.setFont(new Font("Inter", Font.PLAIN, 11));
        lblStatusSaatIni.setForeground(new Color(90, 90, 90));

        // ---------- HEADER ----------
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(MAROON);
        panelHeader.setPreferredSize(new Dimension(740, 64));
        panelHeader.setLayout(null);

        lblNamaHeader = new JLabel("-");
        lblNamaHeader.setForeground(Color.WHITE);
        lblNamaHeader.setFont(new Font("Inter", Font.BOLD, 15));
        lblNamaHeader.setBounds(20, 10, 500, 22);
        panelHeader.add(lblNamaHeader);

        lblIdHeader = new JLabel("-");
        lblIdHeader.setForeground(Color.WHITE);
        lblIdHeader.setFont(new Font("Inter", Font.PLAIN, 11));
        lblIdHeader.setBounds(20, 32, 500, 16);
        panelHeader.add(lblIdHeader);

        JButton btnTutupHeader = new JButton("X");
        btnTutupHeader.setBackground(new Color(160, 20, 20));
        btnTutupHeader.setForeground(Color.WHITE);
        btnTutupHeader.setBorderPainted(false);
        btnTutupHeader.setFocusPainted(false);
        btnTutupHeader.setBounds(690, 17, 30, 30);
        btnTutupHeader.addActionListener(e -> dispose());
        panelHeader.add(btnTutupHeader);

        getContentPane().add(panelHeader, BorderLayout.NORTH);

        // ---------- BODY ----------
        JPanel panelBody = new JPanel();
        panelBody.setLayout(new java.awt.GridLayout(1, 2, 12, 0));
        panelBody.setBackground(new Color(248, 248, 248));
        panelBody.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelBody.add(buatPanelKiri());
        panelBody.add(buatPanelKanan());

        getContentPane().add(panelBody, BorderLayout.CENTER);

        // ---------- FOOTER ----------
        JPanel panelFooter = new JPanel();
        panelFooter.setBackground(new Color(248, 248, 248));
        JButton btnTutup = new JButton("Tutup");
        btnTutup.setFocusPainted(false);
        btnTutup.addActionListener(e -> dispose());
        panelFooter.add(btnTutup);
        getContentPane().add(panelFooter, BorderLayout.SOUTH);
    }

    private JPanel buatPanelKiri() {
        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.BorderLayout(0, 10));
        panel.setBackground(new Color(248, 248, 248));

        JPanel panelAtas = new JPanel();
        panelAtas.setLayout(new java.awt.GridLayout(2, 1));
        panelAtas.setBackground(new Color(248, 248, 248));

        JLabel judul = new JLabel("RIWAYAT SELEKSI / TES");
        judul.setForeground(MAROON);
        judul.setFont(new Font("Inter", Font.BOLD, 11));
        panelAtas.add(judul);
        panelAtas.add(lblStatusSaatIni);

        panel.add(panelAtas, java.awt.BorderLayout.NORTH);

        modelRiwayat = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Tanggal", "Jenis Seleksi", "Nilai", "Status"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelRiwayatSeleksi = new JTable(modelRiwayat);
        tabelRiwayatSeleksi.setRowHeight(26);
        JScrollPane scrollRiwayat = new JScrollPane(tabelRiwayatSeleksi);
        scrollRiwayat.setPreferredSize(new Dimension(330, 200));
        panel.add(scrollRiwayat, java.awt.BorderLayout.CENTER);

        // ---- Form tambah nilai seleksi baru ----
        JPanel panelForm = new JPanel();
        panelForm.setLayout(new GridLayout(5, 2, 6, 6));
        panelForm.setBorder(BorderFactory.createTitledBorder("Tambah Nilai Seleksi Baru"));
        panelForm.setBackground(Color.WHITE);

        cbJenisSeleksi = new JComboBox<>(new String[]{
            "Seleksi Berkas & Administrasi",
            "Seleksi Bahasa Jepang",
            "Tes Fisik & Kesamaptaan",
            "Seleksi Wawancara User",
            "Seleksi Kemampuan Kerja"
        });
        cbJenisSeleksi.setEditable(true);

        txtTanggalSeleksi = new JTextField(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        txtNilaiSeleksi = new JTextField();
        chkGugurPermanen = new JCheckBox("Tandai Gugur Permanen");
        chkGugurPermanen.setBackground(Color.WHITE);

        panelForm.add(new JLabel("Jenis Seleksi:"));
        panelForm.add(cbJenisSeleksi);
        panelForm.add(new JLabel("Tanggal (YYYY-MM-DD):"));
        panelForm.add(txtTanggalSeleksi);
        panelForm.add(new JLabel("Nilai (0-100):"));
        panelForm.add(txtNilaiSeleksi);
        panelForm.add(new JLabel());
        panelForm.add(chkGugurPermanen);

        btnSimpanNilai = new JButton("Simpan Nilai Seleksi");
        btnSimpanNilai.setBackground(MAROON);
        btnSimpanNilai.setForeground(Color.WHITE);
        btnSimpanNilai.setFocusPainted(false);
        btnSimpanNilai.addActionListener(e -> simpanNilaiSeleksi());
        panelForm.add(new JLabel());
        panelForm.add(btnSimpanNilai);

        panel.add(panelForm, java.awt.BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buatPanelKanan() {
        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.BorderLayout(0, 10));
        panel.setBackground(new Color(248, 248, 248));

        JLabel judul = new JLabel("CATATAN EVALUASI INSTRUKTUR");
        judul.setForeground(MAROON);
        judul.setFont(new Font("Inter", Font.BOLD, 11));
        panel.add(judul, java.awt.BorderLayout.NORTH);

        JPanel panelTengah = new JPanel(new java.awt.BorderLayout(0, 8));
        panelTengah.setBackground(new Color(248, 248, 248));

        txtRiwayatCatatan = new JTextArea();
        txtRiwayatCatatan.setEditable(false);
        txtRiwayatCatatan.setLineWrap(true);
        txtRiwayatCatatan.setWrapStyleWord(true);
        txtRiwayatCatatan.setFont(new Font("Inter", Font.PLAIN, 12));
        JScrollPane scrollCatatan = new JScrollPane(txtRiwayatCatatan);
        scrollCatatan.setPreferredSize(new Dimension(330, 260));
        panelTengah.add(scrollCatatan, java.awt.BorderLayout.CENTER);

        panel.add(panelTengah, java.awt.BorderLayout.CENTER);

        JPanel panelInput = new JPanel(new java.awt.BorderLayout(0, 6));
        panelInput.setBorder(BorderFactory.createTitledBorder("Tulis Catatan Baru"));
        panelInput.setBackground(Color.WHITE);

        txtCatatanBaru = new JTextArea(5, 20);
        txtCatatanBaru.setLineWrap(true);
        txtCatatanBaru.setWrapStyleWord(true);
        JScrollPane scrollInput = new JScrollPane(txtCatatanBaru);
        panelInput.add(scrollInput, java.awt.BorderLayout.CENTER);

        JButton btnSimpanCatatan = new JButton("Simpan Catatan Evaluasi");
        btnSimpanCatatan.setBackground(MAROON);
        btnSimpanCatatan.setForeground(Color.WHITE);
        btnSimpanCatatan.setFocusPainted(false);
        btnSimpanCatatan.addActionListener(e -> simpanCatatanEvaluasi());
        panelInput.add(btnSimpanCatatan, java.awt.BorderLayout.SOUTH);

        panel.add(panelInput, java.awt.BorderLayout.SOUTH);

        return panel;
    }
}