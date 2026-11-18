package Views.admin;

import DataStore.DataStore;
import Model.Peserta;
import Model.Program;

import javax.swing.*;
import java.awt.*;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

public class FormDetailProgram extends javax.swing.JDialog {

    private String idTarget;
    private PanelDataProgram panelInduk;
    private Program programTarget;

    // --- Definisi Komponen GUI ---
    private JPanel panelHeader;
    private JLabel lblJudulHeader;
    private JLabel lblSubHeader;
    private JButton btnTutupHeader;

    private JPanel panelBody;

    // Kolom Kiri
    private JLabel lblSectionInfo;
    private JSeparator separatorInfo;
    private JLabel lblIdProgram;
    private JTextField txtIdProgram;
    private JLabel lblNamaProgram;
    private JTextField txtNamaProgram;
    private JLabel lblBidangUsaha;
    private JTextField txtBidangUsaha;

    // Kolom Kanan
    private JLabel lblSectionBiaya;
    private JSeparator separatorBiaya;
    private JLabel lblBiaya;
    private JTextField txtBiaya;
    private JLabel lblKuota;
    private JTextField txtKuota;
    private JLabel lblStatistikPeserta;
    private JTextField txtStatistikPeserta;

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
     * Constructor Standar FormDetailProgram
     */
    public FormDetailProgram(java.awt.Frame parent, boolean modal, String idProgram) {
        this(parent, modal, idProgram, null);
    }

    /**
     * Constructor dengan referensi PanelDataProgram agar tabel induk ikut ter-refresh saat diedit
     */
    public FormDetailProgram(java.awt.Frame parent, boolean modal, String idProgram, PanelDataProgram panelInduk) {
        super(parent, modal);
        this.idTarget = idProgram;
        this.panelInduk = panelInduk;

        // Setup Window Dialog
        this.setTitle("Detail Program Pemagangan");
        this.setSize(600, 420);
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

        lblJudulHeader = new JLabel("Detail Program");
        lblJudulHeader.setForeground(Color.WHITE);
        lblJudulHeader.setFont(FONT_BOLD_16);
        panelHeader.add(lblJudulHeader, new AbsoluteConstraints(20, 15, 300, 20));

        lblSubHeader = new JLabel("Informasi lengkap program pemagangan LPK");
        lblSubHeader.setForeground(new Color(220, 220, 220));
        lblSubHeader.setFont(new Font("Inter", Font.PLAIN, 10));
        panelHeader.add(lblSubHeader, new AbsoluteConstraints(20, 35, 300, 20));

        btnTutupHeader = new JButton("X");
        btnTutupHeader.setBackground(MAROON_GELAP);
        btnTutupHeader.setForeground(Color.WHITE);
        btnTutupHeader.setFocusPainted(false);
        btnTutupHeader.setBorderPainted(false);
        btnTutupHeader.setFont(FONT_BOLD_11);
        btnTutupHeader.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTutupHeader.addActionListener(e -> this.dispose());
        panelHeader.add(btnTutupHeader, new AbsoluteConstraints(530, 15, 45, 30));

        this.getContentPane().add(panelHeader, new AbsoluteConstraints(0, 0, 600, 68));

        // ==========================================
        // 2. PANEL BODY (Data Detail)
        // ==========================================
        panelBody = new JPanel(new AbsoluteLayout());
        panelBody.setBackground(BG_BODY);

        // --- KOLOM KIRI (Informasi Program) ---
        lblSectionInfo = new JLabel("INFORMASI PROGRAM");
        lblSectionInfo.setForeground(MAROON_UTAMA);
        lblSectionInfo.setFont(FONT_BOLD_10);
        panelBody.add(lblSectionInfo, new AbsoluteConstraints(20, 15, 200, 20));

        separatorInfo = new JSeparator();
        panelBody.add(separatorInfo, new AbsoluteConstraints(20, 35, 250, 10));

        // ID Program
        lblIdProgram = new JLabel("ID Program");
        lblIdProgram.setForeground(TXT_ABU);
        lblIdProgram.setFont(FONT_BOLD_11);
        panelBody.add(lblIdProgram, new AbsoluteConstraints(20, 50, 250, 20));

        txtIdProgram = createReadOnlyField();
        txtIdProgram.setFont(FONT_BOLD_11);
        panelBody.add(txtIdProgram, new AbsoluteConstraints(20, 70, 250, 30));

        // Nama Program
        lblNamaProgram = new JLabel("Nama Program");
        lblNamaProgram.setForeground(TXT_ABU);
        lblNamaProgram.setFont(FONT_BOLD_11);
        panelBody.add(lblNamaProgram, new AbsoluteConstraints(20, 110, 250, 20));

        txtNamaProgram = createReadOnlyField();
        panelBody.add(txtNamaProgram, new AbsoluteConstraints(20, 130, 250, 30));

        // Bidang Usaha
        lblBidangUsaha = new JLabel("Bidang Usaha");
        lblBidangUsaha.setForeground(TXT_ABU);
        lblBidangUsaha.setFont(FONT_BOLD_11);
        panelBody.add(lblBidangUsaha, new AbsoluteConstraints(20, 170, 250, 20));

        txtBidangUsaha = createReadOnlyField();
        panelBody.add(txtBidangUsaha, new AbsoluteConstraints(20, 190, 250, 30));

        // --- KOLOM KANAN (Biaya, Kuota & Statistik) ---
        lblSectionBiaya = new JLabel("DETAIL BIAYA & KUOTA");
        lblSectionBiaya.setForeground(MAROON_UTAMA);
        lblSectionBiaya.setFont(FONT_BOLD_10);
        panelBody.add(lblSectionBiaya, new AbsoluteConstraints(310, 15, 200, 20));

        separatorBiaya = new JSeparator();
        panelBody.add(separatorBiaya, new AbsoluteConstraints(310, 35, 250, 10));

        // Biaya
        lblBiaya = new JLabel("Biaya Pemagangan");
        lblBiaya.setForeground(TXT_ABU);
        lblBiaya.setFont(FONT_BOLD_11);
        panelBody.add(lblBiaya, new AbsoluteConstraints(310, 50, 250, 20));

        txtBiaya = createReadOnlyField();
        txtBiaya.setFont(new Font("Inter", Font.BOLD, 12));
        txtBiaya.setForeground(new Color(0, 100, 0));
        panelBody.add(txtBiaya, new AbsoluteConstraints(310, 70, 250, 30));

        // Kuota
        lblKuota = new JLabel("Total Kuota Peserta");
        lblKuota.setForeground(TXT_ABU);
        lblKuota.setFont(FONT_BOLD_11);
        panelBody.add(lblKuota, new AbsoluteConstraints(310, 110, 250, 20));

        txtKuota = createReadOnlyField();
        panelBody.add(txtKuota, new AbsoluteConstraints(310, 130, 250, 30));

        // Peserta Terdaftar & Sisa Kuota
        lblStatistikPeserta = new JLabel("Status Pendaftar & Sisa Kuota");
        lblStatistikPeserta.setForeground(TXT_ABU);
        lblStatistikPeserta.setFont(FONT_BOLD_11);
        panelBody.add(lblStatistikPeserta, new AbsoluteConstraints(310, 170, 250, 20));

        txtStatistikPeserta = createReadOnlyField();
        txtStatistikPeserta.setFont(FONT_BOLD_11);
        panelBody.add(txtStatistikPeserta, new AbsoluteConstraints(310, 190, 250, 30));

        this.getContentPane().add(panelBody, new AbsoluteConstraints(0, 68, 600, 260));

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
        this.getContentPane().add(btnTutup, new AbsoluteConstraints(350, 335, 100, 35));

        btnEdit = new JButton("Edit Data");
        btnEdit.setFont(FONT_BOLD_11);
        btnEdit.setBackground(MAROON_UTAMA);
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEdit.addActionListener(e -> aksiEdit());
        this.getContentPane().add(btnEdit, new AbsoluteConstraints(460, 335, 100, 35));
    }

    /**
     * Helper method untuk membuat JTextField read-only dengan styling konsisten
     */
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
        this.programTarget = DataStore.cariProgram(idTarget);

        if (programTarget != null) {
            txtIdProgram.setText(programTarget.getIdProgram());
            txtNamaProgram.setText(programTarget.getNamaProgram());
            txtBidangUsaha.setText(programTarget.getBidangUsaha());

            String biayaFormatted = "Rp " + String.format("%,.0f", programTarget.getBiaya()).replace(',', '.');
            txtBiaya.setText(biayaFormatted);
            txtKuota.setText(programTarget.getKuota() + " Orang");

            // Hitung peserta yang terdaftar pada program ini dari cache MySQL
            int terdaftar = 0;
            for (Peserta p : DataStore.daftarPeserta) {
                if (idTarget.equals(p.getProgram())) {
                    terdaftar++;
                }
            }
            int sisaKuota = Math.max(0, programTarget.getKuota() - terdaftar);
            txtStatistikPeserta.setText(terdaftar + " Terdaftar · Sisa " + sisaKuota + " Kuota");
            if (sisaKuota == 0) {
                txtStatistikPeserta.setForeground(new Color(180, 0, 0));
            } else {
                txtStatistikPeserta.setForeground(new Color(0, 90, 160));
            }
        } else {
            JOptionPane.showMessageDialog(this, "Data Program tidak ditemukan di database!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }

    private void aksiEdit() {
        java.awt.Frame parentFrame = (getOwner() instanceof java.awt.Frame)
                ? (java.awt.Frame) getOwner()
                : null;

        FormEditProgram formEdit = new FormEditProgram(parentFrame, true, idTarget, panelInduk);
        formEdit.setVisible(true);

        // Refresh data di dialog detail setelah form edit ditutup
        loadData();
    }
}