package Views.admin;

import DataStore.DataStore;
import Database.PesertaDAO;
import Database.SeleksiDAO;
import Model.Peserta;
import Model.Seleksi;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

public class FormEditSeleksi extends javax.swing.JDialog {

    private String idTarget;
    private PanelManajemenSeleksi panelInduk;
    private Seleksi seleksiTarget;

    // --- Definisi Komponen GUI ---
    private JPanel panelHeader;
    private JLabel lblJudulHeader;
    private JLabel lblSubHeader;
    private JButton btnTutupHeader;

    private JPanel panelBody;

    // Kolom Kiri: Identitas (Read-Only)
    private JLabel lblSectionPeserta;
    private JSeparator separatorPeserta;
    private JLabel lblIdSeleksi;
    private JTextField txtIdSeleksi;
    private JLabel lblIdPeserta;
    private JTextField txtIdPeserta;
    private JLabel lblNamaPeserta;
    private JTextField txtNamaPeserta;

    // Kolom Kanan: Rincian Hasil (Editable)
    private JLabel lblSectionHasil;
    private JSeparator separatorHasil;
    private JLabel lblJenisSeleksi;
    private JComboBox<String> cbJenisSeleksi;
    private JLabel lblTanggalSeleksi;
    private JTextField txtTanggalSeleksi;
    private JLabel lblNilai;
    private JTextField txtNilai;
    private JLabel lblStatusHasil;
    private JComboBox<String> cbStatusHasil;

    private JButton btnBatal;
    private JButton btnSimpan;

    // --- Definisi Warna & Font Sesuai Konvensi SIMAPAN ---
    private final Color MAROON_UTAMA = new Color(122, 0, 0);
    private final Color MAROON_GELAP = new Color(160, 20, 20);
    private final Color BG_BODY = new Color(248, 248, 248);
    private final Color BG_READONLY = new Color(230, 230, 230);
    private final Color TXT_ABU = new Color(90, 90, 90);
    private final Color TXT_UTAMA = new Color(10, 10, 10);

    private final Font FONT_BOLD_10 = new Font("Inter", Font.BOLD, 10);
    private final Font FONT_BOLD_11 = new Font("Inter", Font.BOLD, 11);
    private final Font FONT_PLAIN_11 = new Font("Inter", Font.PLAIN, 11);
    private final Font FONT_BOLD_16 = new Font("Inter", Font.BOLD, 16);

    /**
     * Constructor Manual FormEditSeleksi
     */
    public FormEditSeleksi(java.awt.Frame parent, boolean modal, String idSeleksi, PanelManajemenSeleksi panelInduk) {
        super(parent, modal);
        this.idTarget = idSeleksi;
        this.panelInduk = panelInduk;

        // Setup Window Dialog
        this.setTitle("Edit Hasil Seleksi");
        this.setSize(620, 460);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
        this.getContentPane().setLayout(new AbsoluteLayout());
        this.getContentPane().setBackground(BG_BODY);

        initComponentsManual();
        loadData();
    }

    private void initComponentsManual() {
        // ==========================================
        // 1. PANEL HEADER
        // ==========================================
        panelHeader = new JPanel(new AbsoluteLayout());
        panelHeader.setBackground(MAROON_UTAMA);

        lblJudulHeader = new JLabel("Edit Hasil Seleksi");
        lblJudulHeader.setForeground(Color.WHITE);
        lblJudulHeader.setFont(FONT_BOLD_16);
        panelHeader.add(lblJudulHeader, new AbsoluteConstraints(20, 15, 300, 20));

        lblSubHeader = new JLabel("Ubah data nilai dan status seleksi peserta LPK");
        lblSubHeader.setForeground(new Color(220, 220, 220));
        lblSubHeader.setFont(new Font("Inter", Font.PLAIN, 10));
        panelHeader.add(lblSubHeader, new AbsoluteConstraints(20, 35, 350, 20));

        btnTutupHeader = new JButton("X");
        btnTutupHeader.setBackground(MAROON_GELAP);
        btnTutupHeader.setForeground(Color.WHITE);
        btnTutupHeader.setFocusPainted(false);
        btnTutupHeader.setBorderPainted(false);
        btnTutupHeader.setFont(FONT_BOLD_11);
        btnTutupHeader.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTutupHeader.addActionListener(e -> this.dispose());
        panelHeader.add(btnTutupHeader, new AbsoluteConstraints(550, 15, 45, 30));

        this.getContentPane().add(panelHeader, new AbsoluteConstraints(0, 0, 620, 68));

        // ==========================================
        // 2. PANEL BODY
        // ==========================================
        panelBody = new JPanel(new AbsoluteLayout());
        panelBody.setBackground(BG_BODY);

        // --- KOLOM KIRI (Info Dasar - READ ONLY) ---
        lblSectionPeserta = new JLabel("IDENTITAS PESERTA (TERKUNCI)");
        lblSectionPeserta.setForeground(MAROON_UTAMA);
        lblSectionPeserta.setFont(FONT_BOLD_10);
        panelBody.add(lblSectionPeserta, new AbsoluteConstraints(20, 15, 220, 20));

        separatorPeserta = new JSeparator();
        panelBody.add(separatorPeserta, new AbsoluteConstraints(20, 35, 260, 10));

        lblIdSeleksi = new JLabel("ID Seleksi");
        lblIdSeleksi.setForeground(TXT_ABU);
        lblIdSeleksi.setFont(FONT_BOLD_11);
        panelBody.add(lblIdSeleksi, new AbsoluteConstraints(20, 50, 260, 20));

        txtIdSeleksi = createReadOnlyField();
        txtIdSeleksi.setFont(FONT_BOLD_11);
        panelBody.add(txtIdSeleksi, new AbsoluteConstraints(20, 70, 260, 30));

        lblIdPeserta = new JLabel("ID Peserta");
        lblIdPeserta.setForeground(TXT_ABU);
        lblIdPeserta.setFont(FONT_BOLD_11);
        panelBody.add(lblIdPeserta, new AbsoluteConstraints(20, 110, 260, 20));

        txtIdPeserta = createReadOnlyField();
        panelBody.add(txtIdPeserta, new AbsoluteConstraints(20, 130, 260, 30));

        lblNamaPeserta = new JLabel("Nama Lengkap Peserta");
        lblNamaPeserta.setForeground(TXT_ABU);
        lblNamaPeserta.setFont(FONT_BOLD_11);
        panelBody.add(lblNamaPeserta, new AbsoluteConstraints(20, 170, 260, 20));

        txtNamaPeserta = createReadOnlyField();
        panelBody.add(txtNamaPeserta, new AbsoluteConstraints(20, 190, 260, 30));

        // --- KOLOM KANAN (Rincian Hasil - EDITABLE) ---
        lblSectionHasil = new JLabel("RINCIAN NILAI & STATUS");
        lblSectionHasil.setForeground(MAROON_UTAMA);
        lblSectionHasil.setFont(FONT_BOLD_10);
        panelBody.add(lblSectionHasil, new AbsoluteConstraints(320, 15, 200, 20));

        separatorHasil = new JSeparator();
        panelBody.add(separatorHasil, new AbsoluteConstraints(320, 35, 260, 10));

        lblJenisSeleksi = new JLabel("Bidang / Jenis Seleksi");
        lblJenisSeleksi.setForeground(TXT_ABU);
        lblJenisSeleksi.setFont(FONT_BOLD_11);
        panelBody.add(lblJenisSeleksi, new AbsoluteConstraints(320, 50, 260, 20));

        cbJenisSeleksi = new JComboBox<>(new String[]{
            "Manufaktur", "Caregiver", "Pertanian", "Pelautan", "Konstruksi", "JLPT / Bahasa Jepang"
        });
        cbJenisSeleksi.setFont(FONT_PLAIN_11);
        cbJenisSeleksi.setBackground(Color.WHITE);
        panelBody.add(cbJenisSeleksi, new AbsoluteConstraints(320, 70, 260, 30));

        lblTanggalSeleksi = new JLabel("Tanggal Seleksi (YYYY-MM-DD)");
        lblTanggalSeleksi.setForeground(TXT_ABU);
        lblTanggalSeleksi.setFont(FONT_BOLD_11);
        panelBody.add(lblTanggalSeleksi, new AbsoluteConstraints(320, 110, 260, 20));

        txtTanggalSeleksi = new JTextField();
        txtTanggalSeleksi.setFont(FONT_PLAIN_11);
        panelBody.add(txtTanggalSeleksi, new AbsoluteConstraints(320, 130, 260, 30));

        lblNilai = new JLabel("Total Nilai (0 - 100)");
        lblNilai.setForeground(TXT_ABU);
        lblNilai.setFont(FONT_BOLD_11);
        panelBody.add(lblNilai, new AbsoluteConstraints(320, 170, 260, 20));

        txtNilai = new JTextField();
        txtNilai.setFont(FONT_PLAIN_11);
        txtNilai.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                try {
                    int nilai = Integer.parseInt(txtNilai.getText().trim());
                    if (nilai >= 70) {
                        cbStatusHasil.setSelectedItem("Lulus");
                    } else if ("Lulus".equals(cbStatusHasil.getSelectedItem())) {
                        cbStatusHasil.setSelectedItem("Mengulang");
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        });
        panelBody.add(txtNilai, new AbsoluteConstraints(320, 190, 260, 30));

        lblStatusHasil = new JLabel("Status Hasil");
        lblStatusHasil.setForeground(TXT_ABU);
        lblStatusHasil.setFont(FONT_BOLD_11);
        panelBody.add(lblStatusHasil, new AbsoluteConstraints(320, 230, 260, 20));

        cbStatusHasil = new JComboBox<>(new String[]{"Lulus", "Mengulang", "Gugur Permanen"});
        cbStatusHasil.setFont(FONT_PLAIN_11);
        cbStatusHasil.setBackground(Color.WHITE);
        panelBody.add(cbStatusHasil, new AbsoluteConstraints(320, 250, 260, 30));

        this.getContentPane().add(panelBody, new AbsoluteConstraints(0, 68, 620, 300));

        // ==========================================
        // 3. PANEL FOOTER (Tombol Aksi)
        // ==========================================
        btnBatal = new JButton("Batal");
        btnBatal.setFont(FONT_BOLD_11);
        btnBatal.setBackground(Color.WHITE);
        btnBatal.setForeground(TXT_UTAMA);
        btnBatal.setFocusPainted(false);
        btnBatal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBatal.addActionListener(e -> this.dispose());
        this.getContentPane().add(btnBatal, new AbsoluteConstraints(370, 375, 100, 35));

        btnSimpan = new JButton("Simpan");
        btnSimpan.setFont(FONT_BOLD_11);
        btnSimpan.setBackground(MAROON_UTAMA);
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSimpan.addActionListener(e -> aksiSimpan());
        this.getContentPane().add(btnSimpan, new AbsoluteConstraints(480, 375, 100, 35));
    }

    private JTextField createReadOnlyField() {
        JTextField field = new JTextField();
        field.setFont(FONT_PLAIN_11);
        field.setEditable(false);
        field.setBackground(BG_READONLY);
        field.setForeground(TXT_UTAMA);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }

    private void loadData() {
        DataStore.muatUlang();
        this.seleksiTarget = null;

        for (Seleksi sel : DataStore.daftarSeleksi) {
            if (sel.getIdSeleksi().equals(idTarget)) {
                this.seleksiTarget = sel;
                break;
            }
        }

        if (seleksiTarget != null) {
            txtIdSeleksi.setText(seleksiTarget.getIdSeleksi());
            if (seleksiTarget.getPeserta() != null) {
                txtIdPeserta.setText(seleksiTarget.getPeserta().getIdPeserta());
                txtNamaPeserta.setText(seleksiTarget.getPeserta().getNamaLengkap());
            }

            // Pastikan jenis seleksi dari DB tetap terpilih meski tidak ada di daftar bawaan
            String jenisDb = seleksiTarget.getJenisSeleksi();
            if (jenisDb != null) {
                boolean adaDiList = false;
                for (int i = 0; i < cbJenisSeleksi.getItemCount(); i++) {
                    if (cbJenisSeleksi.getItemAt(i).equalsIgnoreCase(jenisDb)) {
                        cbJenisSeleksi.setSelectedIndex(i);
                        adaDiList = true;
                        break;
                    }
                }
                if (!adaDiList) {
                    cbJenisSeleksi.addItem(jenisDb);
                    cbJenisSeleksi.setSelectedItem(jenisDb);
                }
            }

            txtTanggalSeleksi.setText(seleksiTarget.getTanggalSeleksi());
            txtNilai.setText(String.valueOf(seleksiTarget.getNilai()));

            String statusDb = seleksiTarget.getStatusHasil();
            if ("Gagal".equalsIgnoreCase(statusDb) || "Gugur".equalsIgnoreCase(statusDb)) {
                cbStatusHasil.setSelectedItem("Gugur Permanen");
            } else if (statusDb != null) {
                cbStatusHasil.setSelectedItem(statusDb);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Data Seleksi tidak ditemukan di database!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }

    private void aksiSimpan() {
        String tglStr = txtTanggalSeleksi.getText().trim();
        String nilaiStr = txtNilai.getText().trim();

        // 1. Validasi Kolom Kosong
        if (tglStr.isEmpty() || nilaiStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Tanggal Seleksi dan Total Nilai tidak boleh kosong!",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Validasi Format Tanggal YYYY-MM-DD agar valid untuk MySQL
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(tglStr);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this,
                "Format Tanggal Seleksi harus YYYY-MM-DD (contoh: 2026-06-15)!",
                "Error Format Tanggal", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Validasi Angka & Rentang Nilai (0-100)
        int nilaiBaru;
        try {
            nilaiBaru = Integer.parseInt(nilaiStr);
            if (nilaiBaru < 0 || nilaiBaru > 100) {
                JOptionPane.showMessageDialog(this,
                    "Total Nilai harus berada di antara 0 sampai 100!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Kolom Total Nilai harus berupa angka bulat yang valid!",
                "Error Format Angka", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Update Objek & Simpan ke MySQL
        if (seleksiTarget != null) {
            String jenisBaru = cbJenisSeleksi.getSelectedItem().toString();
            String statusBaru = cbStatusHasil.getSelectedItem().toString();

            seleksiTarget.setJenisSeleksi(jenisBaru);
            seleksiTarget.setTanggalSeleksi(tglStr);
            seleksiTarget.setNilai(nilaiBaru);
            seleksiTarget.setStatusHasil(statusBaru);

            boolean ok = SeleksiDAO.ubah(seleksiTarget);

            if (ok) {
                // Sinkronkan status akhir Peserta jika record ini adalah seleksi terakhirnya
                Peserta peserta = seleksiTarget.getPeserta();
                if (peserta != null && isSeleksiTerakhir(peserta.getIdPeserta(), seleksiTarget.getIdSeleksi())) {
                    if ("Lulus".equalsIgnoreCase(statusBaru)) {
                        peserta.setStatusSeleksi("Lulus");
                        if ("Dibatalkan".equalsIgnoreCase(peserta.getStatusKeberangkatan())
                                || "Belum Dijadwalkan".equalsIgnoreCase(peserta.getStatusKeberangkatan())) {
                            peserta.setStatusKeberangkatan("Menunggu Dokumen");
                        }
                    } else if ("Gugur Permanen".equalsIgnoreCase(statusBaru)) {
                        peserta.setStatusSeleksi("Gugur");
                        peserta.setStatusKeberangkatan("Dibatalkan");
                    } else {
                        peserta.setStatusSeleksi("Mengulang");
                        if ("Dibatalkan".equalsIgnoreCase(peserta.getStatusKeberangkatan())) {
                            peserta.setStatusKeberangkatan("Belum Dijadwalkan");
                        }
                    }
                    PesertaDAO.ubah(peserta);
                }

                DataStore.muatUlang();

                JOptionPane.showMessageDialog(this, "Data Hasil Seleksi berhasil diperbarui!",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);

                if (panelInduk != null) {
                    panelInduk.loadStatistics();
                    panelInduk.loadTableData("");
                }
                this.dispose();
            } else {
                DataStore.muatUlang();
            }
        }
    }

    private boolean isSeleksiTerakhir(String idPeserta, String idSeleksiTarget) {
        String idTerakhir = null;
        for (Seleksi s : DataStore.daftarSeleksi) {
            if (s.getPeserta() != null && idPeserta.equals(s.getPeserta().getIdPeserta())) {
                idTerakhir = s.getIdSeleksi();
            }
        }
        return idSeleksiTarget.equals(idTerakhir);
    }
}