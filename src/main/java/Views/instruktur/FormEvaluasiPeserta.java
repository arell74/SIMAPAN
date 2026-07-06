/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views.instruktur;

import DataStore.DataStore;
import Model.CatatanEvaluasi;
import Model.Peserta;
import Model.Seleksi;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
 * Dibuka dari baris yang dipilih di tabel PanelInputNilai. Menampilkan
 * riwayat seleksi/tes peserta, form untuk menambah nilai seleksi baru,
 * serta catatan evaluasi berupa teks bebas dari instruktur.
 *
 * CATATAN: Form ini ditulis manual (bukan hasil generate NetBeans Matisse),
 * jadi sebaiknya diedit lewat tab "Source", bukan tab "Design".
 *
 * ASUMSI: getter Model.Seleksi mengikuti pola getIdSeleksi(), getPeserta(),
 * getJenisSeleksi(), getTanggal(), getNilai(), getStatus() berdasarkan
 * pemakaian constructor di DataStore. Sesuaikan jika nama aslinya berbeda.
 *
 * @author arelssi
 */
public class FormEvaluasiPeserta extends javax.swing.JFrame {

    private static final Color MAROON = new Color(122, 0, 0);

    private final Peserta pesertaAktif;
    private final String idInstrukturAktif;

    private JTable tabelRiwayatSeleksi;
    private DefaultTableModel modelRiwayat;

    private JComboBox<String> cbJenisSeleksi;
    private JTextField txtTanggalSeleksi;
    private JTextField txtNilaiSeleksi;
    private JCheckBox chkGugurPermanen;

    private JTextArea txtCatatanBaru;
    private JTextArea txtRiwayatCatatan;

    public FormEvaluasiPeserta(java.awt.Frame parent, boolean modal, Peserta peserta, String idInstruktur) {
        this.pesertaAktif = peserta;
        this.idInstrukturAktif = idInstruktur;

        initComponents();
        setTitle("Form Evaluasi Peserta");
        setSize(720, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        muatDataHeader();
        muatRiwayatSeleksi();
        muatRiwayatCatatan();
    }

    private void muatDataHeader() {
        if (pesertaAktif == null) {
            return;
        }
        lblNamaHeader.setText(pesertaAktif.getNamaLengkap());
        lblIdHeader.setText(pesertaAktif.getIdPeserta() + " · " + pesertaAktif.getKelas() + " · Level " + pesertaAktif.getLevelBahasa());
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
        for (Seleksi s : DataStore.daftarSeleksi) {
            if (s.getPeserta() != null && s.getPeserta().getIdPeserta().equals(pesertaAktif.getIdPeserta())) {
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
    }

    private void muatRiwayatCatatan() {
        if (pesertaAktif == null) {
            txtRiwayatCatatan.setText("");
            return;
        }
        StringBuilder sb = new StringBuilder();
        // Tampilkan dari yang terbaru (asumsi entri baru ditambahkan di akhir list)
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
            sb.append("[").append(c.getTanggal()).append(" · ").append(c.getIdInstruktur()).append("]\n");
            sb.append(c.getIsiCatatan()).append("\n");
            sb.append("------------------------------------------------------\n");
        }
        txtRiwayatCatatan.setText(sb.toString());
        txtRiwayatCatatan.setCaretPosition(0);
    }

    private void simpanNilaiSeleksi() {
        if (pesertaAktif == null) {
            return;
        }

        String jenis = (String) cbJenisSeleksi.getSelectedItem();
        String tanggal = txtTanggalSeleksi.getText() == null ? "" : txtTanggalSeleksi.getText().trim();

        if (tanggal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tanggal tidak boleh kosong.", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!tanggal.matches("^\\d{2}/\\d{2}/\\d{4}$")) {
            JOptionPane.showMessageDialog(this, "Format tanggal harus dd/MM/yyyy, contoh: 01/10/2025", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int nilai;
        try {
            nilai = Integer.parseInt(txtNilaiSeleksi.getText().trim());
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Nilai harus berupa angka.", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (nilai < 0 || nilai > 100) {
            JOptionPane.showMessageDialog(this, "Nilai harus berada di rentang 0-100.", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String status;
        if (chkGugurPermanen.isSelected()) {
            status = "Gugur Permanen";
        } else if (nilai >= 70) {
            status = "Lulus";
        } else {
            status = "Tidak Lulus";
        }

        try {
            String idSeleksi = "SEL" + System.currentTimeMillis();
            Seleksi seleksiBaru = new Seleksi(idSeleksi, pesertaAktif, jenis, tanggal, nilai, status);
            DataStore.daftarSeleksi.add(seleksiBaru);

            // Sinkronkan status seleksi terbaru ke data peserta secara keseluruhan
            pesertaAktif.setStatusSeleksi(status);

            muatRiwayatSeleksi();
            muatDataHeader();
            txtNilaiSeleksi.setText("");
            chkGugurPermanen.setSelected(false);

            JOptionPane.showMessageDialog(this, "Nilai seleksi berhasil disimpan!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal menyimpan nilai seleksi: " + ex.getMessage(),
                    "Terjadi Kesalahan",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void simpanCatatanEvaluasi() {
        if (pesertaAktif == null) {
            return;
        }

        String isi = txtCatatanBaru.getText() == null ? "" : txtCatatanBaru.getText().trim();
        if (isi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Catatan evaluasi tidak boleh kosong.", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String idCatatan = "CTT" + System.currentTimeMillis();
            String tanggalHariIni = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            CatatanEvaluasi catatanBaru = new CatatanEvaluasi(idCatatan, pesertaAktif, idInstrukturAktif, tanggalHariIni, isi);
            DataStore.daftarCatatanEvaluasi.add(catatanBaru);

            muatRiwayatCatatan();
            txtCatatanBaru.setText("");

            JOptionPane.showMessageDialog(this, "Catatan evaluasi berhasil disimpan!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal menyimpan catatan: " + ex.getMessage(),
                    "Terjadi Kesalahan",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==================== UI (ditulis manual, bukan generated Matisse) ====================

    private JLabel lblNamaHeader;
    private JLabel lblIdHeader;
    private JLabel lblStatusSaatIni;

    private void initComponents() {
        getContentPane().setLayout(new BorderLayout());

        // Label status dibuat di awal karena dipakai oleh buatPanelKiri()
        // yang dipanggil sebelum bagian bawah method ini selesai.
        lblStatusSaatIni = new JLabel("Status Seleksi Saat Ini: -");
        lblStatusSaatIni.setFont(new Font("Inter", Font.PLAIN, 11));
        lblStatusSaatIni.setForeground(new Color(90, 90, 90));

        // ---------- HEADER ----------
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(MAROON);
        panelHeader.setPreferredSize(new Dimension(720, 64));
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
        btnTutupHeader.setBounds(670, 17, 30, 30);
        btnTutupHeader.addActionListener(e -> dispose());
        panelHeader.add(btnTutupHeader);

        getContentPane().add(panelHeader, BorderLayout.NORTH);

        // ---------- BODY ----------
        JPanel panelBody = new JPanel();
        panelBody.setLayout(new java.awt.GridLayout(1, 2, 10, 0));
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
        scrollRiwayat.setPreferredSize(new Dimension(320, 200));
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

        txtTanggalSeleksi = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtNilaiSeleksi = new JTextField();
        chkGugurPermanen = new JCheckBox("Tandai Gugur Permanen");

        panelForm.add(new JLabel("Jenis Seleksi:"));
        panelForm.add(cbJenisSeleksi);
        panelForm.add(new JLabel("Tanggal (dd/MM/yyyy):"));
        panelForm.add(txtTanggalSeleksi);
        panelForm.add(new JLabel("Nilai (0-100):"));
        panelForm.add(txtNilaiSeleksi);
        panelForm.add(new JLabel());
        panelForm.add(chkGugurPermanen);

        JButton btnSimpanNilai = new JButton("Simpan Nilai Seleksi");
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
        scrollCatatan.setPreferredSize(new Dimension(320, 260));
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