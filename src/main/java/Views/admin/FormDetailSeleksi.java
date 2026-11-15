package Views.admin;

import DataStore.DataStore;
import Model.Seleksi;

import javax.swing.*;
import java.awt.*;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

public class FormDetailSeleksi extends javax.swing.JDialog {

    private String idTarget;
    private PanelManajemenSeleksi panelInduk;
    private Seleksi seleksiTarget;

    // --- Definisi Komponen GUI ---
    private JPanel panelHeader;
    private JLabel lblJudulHeader;
    private JLabel lblSubHeader;
    private JButton btnTutupHeader;

    private JPanel panelBody;

    // Kolom Kiri: Identitas & Info Dasar
    private JLabel lblSectionPeserta;
    private JSeparator separatorPeserta;
    private JLabel lblIdSeleksi;
    private JTextField txtIdSeleksi;
    private JLabel lblIdPeserta;
    private JTextField txtIdPeserta;
    private JLabel lblNamaPeserta;
    private JTextField txtNamaPeserta;
    private JLabel lblPercobaan;
    private JTextField txtPercobaan;

    // Kolom Kanan: Rincian Hasil Seleksi
    private JLabel lblSectionHasil;
    private JSeparator separatorHasil;
    private JLabel lblJenisSeleksi;
    private JTextField txtJenisSeleksi;
    private JLabel lblTanggalSeleksi;
    private JTextField txtTanggalSeleksi;
    private JLabel lblNilai;
    private JTextField txtNilai;
    private JLabel lblStatusHasil;
    private JTextField txtStatusHasil;

    private JButton btnTutup;
    private JButton btnEdit;

    // --- Definisi Warna & Font Sesuai Konvensi SIMAPAN ---
    private final Color MAROON_UTAMA = new Color(122, 0, 0);
    private final Color MAROON_GELAP = new Color(160, 20, 20);
    private final Color BG_BODY = new Color(248, 248, 248);
    private final Color BG_READONLY = new Color(240, 240, 240);
    private final Color TXT_ABU = new Color(90, 90, 90);
    private final Color TXT_UTAMA = new Color(10, 10, 10);

    private final Font FONT_BOLD_10 = new Font("Inter", Font.BOLD, 10);
    private final Font FONT_BOLD_11 = new Font("Inter", Font.BOLD, 11);
    private final Font FONT_PLAIN_11 = new Font("Inter", Font.PLAIN, 11);
    private final Font FONT_BOLD_16 = new Font("Inter", Font.BOLD, 16);

    /**
     * Constructor Standar FormDetailSeleksi
     */
    public FormDetailSeleksi(java.awt.Frame parent, boolean modal, String idSeleksi) {
        this(parent, modal, idSeleksi, null);
    }

    /**
     * Constructor dengan referensi PanelManajemenSeleksi
     */
    public FormDetailSeleksi(java.awt.Frame parent, boolean modal, String idSeleksi, PanelManajemenSeleksi panelInduk) {
        super(parent, modal);
        this.idTarget = idSeleksi;
        this.panelInduk = panelInduk;

        // Setup Window Dialog
        this.setTitle("Detail Hasil Seleksi");
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

        lblJudulHeader = new JLabel("Detail Hasil Seleksi");
        lblJudulHeader.setForeground(Color.WHITE);
        lblJudulHeader.setFont(FONT_BOLD_16);
        panelHeader.add(lblJudulHeader, new AbsoluteConstraints(20, 15, 300, 20));

        lblSubHeader = new JLabel("Informasi lengkap riwayat nilai dan status seleksi peserta");
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

        // --- KOLOM KIRI (Info Dasar) ---
        lblSectionPeserta = new JLabel("IDENTITAS PESERTA");
        lblSectionPeserta.setForeground(MAROON_UTAMA);
        lblSectionPeserta.setFont(FONT_BOLD_10);
        panelBody.add(lblSectionPeserta, new AbsoluteConstraints(20, 15, 200, 20));

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

        lblPercobaan = new JLabel("Riwayat Percobaan");
        lblPercobaan.setForeground(TXT_ABU);
        lblPercobaan.setFont(FONT_BOLD_11);
        panelBody.add(lblPercobaan, new AbsoluteConstraints(20, 230, 260, 20));

        txtPercobaan = createReadOnlyField();
        txtPercobaan.setFont(FONT_BOLD_11);
        panelBody.add(txtPercobaan, new AbsoluteConstraints(20, 250, 260, 30));

        // --- KOLOM KANAN (Rincian Hasil) ---
        lblSectionHasil = new JLabel("RINCIAN NILAI & STATUS");
        lblSectionHasil.setForeground(MAROON_UTAMA);
        lblSectionHasil.setFont(FONT_BOLD_10);
        panelBody.add(lblSectionHasil, new AbsoluteConstraints(320, 15, 200, 20));

        separatorHasil = new JSeparator();
        panelBody.add(separatorHasil, new AbsoluteConstraints(320, 35, 260, 10));

        lblJenisSeleksi = new JLabel("Jenis / Bidang Seleksi");
        lblJenisSeleksi.setForeground(TXT_ABU);
        lblJenisSeleksi.setFont(FONT_BOLD_11);
        panelBody.add(lblJenisSeleksi, new AbsoluteConstraints(320, 50, 260, 20));

        txtJenisSeleksi = createReadOnlyField();
        panelBody.add(txtJenisSeleksi, new AbsoluteConstraints(320, 70, 260, 30));

        lblTanggalSeleksi = new JLabel("Tanggal Seleksi");
        lblTanggalSeleksi.setForeground(TXT_ABU);
        lblTanggalSeleksi.setFont(FONT_BOLD_11);
        panelBody.add(lblTanggalSeleksi, new AbsoluteConstraints(320, 110, 260, 20));

        txtTanggalSeleksi = createReadOnlyField();
        panelBody.add(txtTanggalSeleksi, new AbsoluteConstraints(320, 130, 260, 30));

        lblNilai = new JLabel("Total Nilai");
        lblNilai.setForeground(TXT_ABU);
        lblNilai.setFont(FONT_BOLD_11);
        panelBody.add(lblNilai, new AbsoluteConstraints(320, 170, 260, 20));

        txtNilai = createReadOnlyField();
        txtNilai.setFont(new Font("Inter", Font.BOLD, 14));
        panelBody.add(txtNilai, new AbsoluteConstraints(320, 190, 260, 30));

        lblStatusHasil = new JLabel("Status Hasil");
        lblStatusHasil.setForeground(TXT_ABU);
        lblStatusHasil.setFont(FONT_BOLD_11);
        panelBody.add(lblStatusHasil, new AbsoluteConstraints(320, 230, 260, 20));

        txtStatusHasil = createReadOnlyField();
        txtStatusHasil.setFont(FONT_BOLD_11);
        panelBody.add(txtStatusHasil, new AbsoluteConstraints(320, 250, 260, 30));

        this.getContentPane().add(panelBody, new AbsoluteConstraints(0, 68, 620, 300));

        // ==========================================
        // 3. PANEL FOOTER (Tombol Aksi)
        // ==========================================
        btnTutup = new JButton("Tutup");
        btnTutup.setFont(FONT_BOLD_11);
        btnTutup.setBackground(Color.WHITE);
        btnTutup.setForeground(TXT_UTAMA);
        btnTutup.setFocusPainted(false);
        btnTutup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTutup.addActionListener(e -> this.dispose());
        this.getContentPane().add(btnTutup, new AbsoluteConstraints(370, 375, 100, 35));

        btnEdit = new JButton("Edit Data");
        btnEdit.setFont(FONT_BOLD_11);
        btnEdit.setBackground(MAROON_UTAMA);
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEdit.addActionListener(e -> aksiEdit());
        this.getContentPane().add(btnEdit, new AbsoluteConstraints(480, 375, 100, 35));
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
                String idPst = seleksiTarget.getPeserta().getIdPeserta();
                txtIdPeserta.setText(idPst);
                txtNamaPeserta.setText(seleksiTarget.getPeserta().getNamaLengkap());

                // Hitung percobaan ke berapa record seleksi ini
                int urutanKe = 0;
                int totalPercobaan = 0;
                for (Seleksi s : DataStore.daftarSeleksi) {
                    if (s.getPeserta() != null && idPst.equals(s.getPeserta().getIdPeserta())) {
                        totalPercobaan++;
                        if (s.getIdSeleksi().equals(idTarget)) {
                            urutanKe = totalPercobaan;
                        }
                    }
                }
                txtPercobaan.setText("Percobaan ke-" + urutanKe + " (Total " + totalPercobaan + " / 3)");
            } else {
                txtIdPeserta.setText("-");
                txtNamaPeserta.setText("Data Peserta Tidak Valid");
                txtPercobaan.setText("-");
            }

            txtJenisSeleksi.setText(seleksiTarget.getJenisSeleksi());
            txtTanggalSeleksi.setText(seleksiTarget.getTanggalSeleksi());
            txtNilai.setText(String.valueOf(seleksiTarget.getNilai()));

            // Reset & terapkan pewarnaan Status Hasil
            String status = seleksiTarget.getStatusHasil();
            txtStatusHasil.setText(status != null ? status : "-");
            txtStatusHasil.setBackground(BG_READONLY);
            txtStatusHasil.setForeground(TXT_UTAMA);

            if (status != null) {
                if (status.equalsIgnoreCase("Lulus")) {
                    txtStatusHasil.setForeground(new Color(0, 140, 60));
                    txtStatusHasil.setBackground(new Color(230, 255, 230));
                } else if (status.equalsIgnoreCase("Gugur Permanen")
                        || status.equalsIgnoreCase("Gugur")
                        || status.equalsIgnoreCase("Gagal")) {
                    txtStatusHasil.setForeground(new Color(200, 0, 0));
                    txtStatusHasil.setBackground(new Color(255, 230, 230));
                } else {
                    txtStatusHasil.setForeground(new Color(200, 100, 0));
                    txtStatusHasil.setBackground(new Color(255, 245, 225));
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Data Seleksi tidak ditemukan di database!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }

    private void aksiEdit() {
        java.awt.Frame parentFrame = (getOwner() instanceof java.awt.Frame)
                ? (java.awt.Frame) getOwner()
                : null;

        FormEditSeleksi formEdit = new FormEditSeleksi(
            parentFrame,
            true,
            idTarget,
            panelInduk
        );

        formEdit.setVisible(true);
        loadData();
    }
}