package Views.admin;

import DataStore.DataStore;
import Database.PenggunaDAO;
import Model.Instruktur;

import javax.swing.*;
import java.awt.*;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

public class FormEditInstruktur extends javax.swing.JDialog {

    private String idTarget;
    private PanelDataInstruktur panelInduk;
    private Instruktur instrukturTarget;

    // --- Definisi Komponen GUI ---
    private JPanel panelHeader;
    private JLabel lblJudulHeader;
    private JLabel lblSubHeader;
    private JButton btnTutupHeader;

    private JPanel panelBody;
    private JLabel lblSectionIdentitas;
    private JSeparator separatorIdentitas;

    private JLabel lblIdInstruktur;
    private JTextField txtIdInstruktur;
    private JLabel lblNamaLengkap;
    private JTextField txtNamaLengkap;
    private JLabel lblNoTelp;
    private JTextField txtNoTelp;

    private JLabel lblSectionKeahlian;
    private JSeparator separatorKeahlian;

    private JLabel lblSpesialisasi;
    private JComboBox<String> cbSpesialisasi;
    private JLabel lblLevelJlpt;
    private JComboBox<String> cbLevelJlpt;
    private JLabel lblKelasDiampu;
    private JComboBox<String> cbKelasDiampu;

    private JButton btnSimpan;
    private JButton btnBatal;

    // --- Definisi Warna & Font Sesuai Konvensi ---
    private final Color MAROON_UTAMA = new Color(122, 0, 0);
    private final Color MAROON_GELAP = new Color(160, 20, 20);
    private final Color BG_BODY = new Color(248, 248, 248);
    private final Color TXT_ABU = new Color(90, 90, 90);
    private final Color TXT_UTAMA = new Color(10, 10, 10);
    private final Font FONT_BOLD_11 = new Font("Inter", Font.BOLD, 11);
    private final Font FONT_PLAIN_11 = new Font("Inter", Font.PLAIN, 11);
    private final Font FONT_BOLD_10 = new Font("Inter", Font.BOLD, 10);
    private final Font FONT_BOLD_16 = new Font("Inter", Font.BOLD, 16);

    /**
     * Constructor Manual FormEditInstruktur
     */
    public FormEditInstruktur(java.awt.Frame parent, boolean modal, String idInstruktur, PanelDataInstruktur panelInduk) {
        super(parent, modal);
        this.idTarget = idInstruktur;
        this.panelInduk = panelInduk;

        // Setup Window Dialog
        this.setTitle("Edit Data Instruktur");
        this.setSize(600, 480);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
        this.getContentPane().setLayout(new AbsoluteLayout());
        this.getContentPane().setBackground(BG_BODY);

        // 1. Inisialisasi dan Render Komponen UI
        initComponentsManual();

        // 2. Setup Data Dropdown & State
        initCustomData();

        // 3. Tarik Data dari DataStore (MySQL)
        loadData();
    }

    private void initComponentsManual() {
        // ==========================================
        // 1. PANEL HEADER
        // ==========================================
        panelHeader = new JPanel(new AbsoluteLayout());
        panelHeader.setBackground(MAROON_UTAMA);

        lblJudulHeader = new JLabel("Edit Instruktur");
        lblJudulHeader.setForeground(Color.WHITE);
        lblJudulHeader.setFont(FONT_BOLD_16);
        panelHeader.add(lblJudulHeader, new AbsoluteConstraints(20, 15, 300, 20));

        lblSubHeader = new JLabel("Ubah data identitas dan keahlian instruktur LPK");
        lblSubHeader.setForeground(new Color(220, 220, 220));
        lblSubHeader.setFont(new Font("Inter", Font.PLAIN, 10));
        panelHeader.add(lblSubHeader, new AbsoluteConstraints(20, 35, 300, 20));

        btnTutupHeader = new JButton("X");
        btnTutupHeader.setBackground(MAROON_GELAP);
        btnTutupHeader.setForeground(Color.WHITE);
        btnTutupHeader.setFocusPainted(false);
        btnTutupHeader.setBorderPainted(false);
        btnTutupHeader.setFont(FONT_BOLD_11);
        btnTutupHeader.addActionListener(e -> this.dispose());
        panelHeader.add(btnTutupHeader, new AbsoluteConstraints(530, 15, 45, 30));

        this.getContentPane().add(panelHeader, new AbsoluteConstraints(0, 0, 600, 68));

        // ==========================================
        // 2. PANEL BODY (Form Input)
        // ==========================================
        panelBody = new JPanel(new AbsoluteLayout());
        panelBody.setBackground(BG_BODY);

        // --- KOLOM KIRI (Data Identitas) ---
        lblSectionIdentitas = new JLabel("DATA IDENTITAS");
        lblSectionIdentitas.setForeground(MAROON_UTAMA);
        lblSectionIdentitas.setFont(FONT_BOLD_10);
        panelBody.add(lblSectionIdentitas, new AbsoluteConstraints(20, 15, 150, 20));

        separatorIdentitas = new JSeparator();
        panelBody.add(separatorIdentitas, new AbsoluteConstraints(20, 35, 250, 10));

        // ID Instruktur
        lblIdInstruktur = new JLabel("ID Instruktur");
        lblIdInstruktur.setForeground(TXT_ABU);
        lblIdInstruktur.setFont(FONT_BOLD_11);
        panelBody.add(lblIdInstruktur, new AbsoluteConstraints(20, 50, 250, 20));

        txtIdInstruktur = new JTextField();
        txtIdInstruktur.setFont(FONT_PLAIN_11);
        txtIdInstruktur.setEditable(false);
        txtIdInstruktur.setBackground(new Color(230, 230, 230));
        panelBody.add(txtIdInstruktur, new AbsoluteConstraints(20, 70, 250, 30));

        // Nama Lengkap
        lblNamaLengkap = new JLabel("Nama Lengkap");
        lblNamaLengkap.setForeground(TXT_ABU);
        lblNamaLengkap.setFont(FONT_BOLD_11);
        panelBody.add(lblNamaLengkap, new AbsoluteConstraints(20, 110, 250, 20));

        txtNamaLengkap = new JTextField();
        txtNamaLengkap.setFont(FONT_PLAIN_11);
        panelBody.add(txtNamaLengkap, new AbsoluteConstraints(20, 130, 250, 30));

        // No Telp
        lblNoTelp = new JLabel("No. Telepon");
        lblNoTelp.setForeground(TXT_ABU);
        lblNoTelp.setFont(FONT_BOLD_11);
        panelBody.add(lblNoTelp, new AbsoluteConstraints(20, 170, 250, 20));

        txtNoTelp = new JTextField();
        txtNoTelp.setFont(FONT_PLAIN_11);
        panelBody.add(txtNoTelp, new AbsoluteConstraints(20, 190, 250, 30));

        // --- KOLOM KANAN (Data Keahlian) ---
        lblSectionKeahlian = new JLabel("DATA KEAHLIAN & KELAS");
        lblSectionKeahlian.setForeground(MAROON_UTAMA);
        lblSectionKeahlian.setFont(FONT_BOLD_10);
        panelBody.add(lblSectionKeahlian, new AbsoluteConstraints(310, 15, 200, 20));

        separatorKeahlian = new JSeparator();
        panelBody.add(separatorKeahlian, new AbsoluteConstraints(310, 35, 250, 10));

        // Spesialisasi
        lblSpesialisasi = new JLabel("Spesialisasi");
        lblSpesialisasi.setForeground(TXT_ABU);
        lblSpesialisasi.setFont(FONT_BOLD_11);
        panelBody.add(lblSpesialisasi, new AbsoluteConstraints(310, 50, 250, 20));

        cbSpesialisasi = new JComboBox<>();
        cbSpesialisasi.setFont(FONT_PLAIN_11);
        cbSpesialisasi.setBackground(Color.WHITE);
        panelBody.add(cbSpesialisasi, new AbsoluteConstraints(310, 70, 250, 30));

        // Level JLPT
        lblLevelJlpt = new JLabel("Level JLPT");
        lblLevelJlpt.setForeground(TXT_ABU);
        lblLevelJlpt.setFont(FONT_BOLD_11);
        panelBody.add(lblLevelJlpt, new AbsoluteConstraints(310, 110, 250, 20));

        cbLevelJlpt = new JComboBox<>();
        cbLevelJlpt.setFont(FONT_PLAIN_11);
        cbLevelJlpt.setBackground(Color.WHITE);
        panelBody.add(cbLevelJlpt, new AbsoluteConstraints(310, 130, 250, 30));

        // Kelas Diampu
        lblKelasDiampu = new JLabel("Kelas Diampu");
        lblKelasDiampu.setForeground(TXT_ABU);
        lblKelasDiampu.setFont(FONT_BOLD_11);
        panelBody.add(lblKelasDiampu, new AbsoluteConstraints(310, 170, 250, 20));

        cbKelasDiampu = new JComboBox<>();
        cbKelasDiampu.setFont(FONT_PLAIN_11);
        cbKelasDiampu.setBackground(Color.WHITE);
        panelBody.add(cbKelasDiampu, new AbsoluteConstraints(310, 190, 250, 30));

        this.getContentPane().add(panelBody, new AbsoluteConstraints(0, 68, 600, 300));

        // ==========================================
        // 3. PANEL FOOTER (Tombol Aksi)
        // ==========================================
        btnBatal = new JButton("Batal");
        btnBatal.setFont(FONT_BOLD_11);
        btnBatal.setBackground(Color.WHITE);
        btnBatal.setForeground(TXT_UTAMA);
        btnBatal.setFocusPainted(false);
        btnBatal.addActionListener(e -> this.dispose());
        this.getContentPane().add(btnBatal, new AbsoluteConstraints(350, 380, 100, 35));

        btnSimpan = new JButton("Simpan");
        btnSimpan.setFont(FONT_BOLD_11);
        btnSimpan.setBackground(MAROON_UTAMA);
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.addActionListener(e -> aksiSimpan());
        this.getContentPane().add(btnSimpan, new AbsoluteConstraints(460, 380, 100, 35));
    }

    private void initCustomData() {
        cbSpesialisasi.addItem("Budaya Jepang");
        cbSpesialisasi.addItem("Bahasa Jepang");
        cbSpesialisasi.addItem("JLPT");

        cbLevelJlpt.addItem("N1");
        cbLevelJlpt.addItem("N2");
        cbLevelJlpt.addItem("N3");
        cbLevelJlpt.addItem("N4");
        cbLevelJlpt.addItem("N5");

        cbKelasDiampu.addItem("Reguler");
        cbKelasDiampu.addItem("Mandiri");
        cbKelasDiampu.addItem("GTOG");
    }

    private void loadData() {
        DataStore.muatUlang();
        this.instrukturTarget = DataStore.cariInstruktur(idTarget);

        if (instrukturTarget != null) {
            txtIdInstruktur.setText(instrukturTarget.getIdInstruktur());
            txtNamaLengkap.setText(instrukturTarget.getNamaLengkap());
            txtNoTelp.setText(instrukturTarget.getNoTelp());

            cbSpesialisasi.setSelectedItem(instrukturTarget.getSpesialisasi());

            String level = instrukturTarget.getLevelJlpt();
            if (level != null && !level.startsWith("N")) {
                level = "N" + level;
            }
            cbLevelJlpt.setSelectedItem(level);

            cbKelasDiampu.setSelectedItem(instrukturTarget.getKelasDiampu());
        } else {
            JOptionPane.showMessageDialog(this, "Data instruktur tidak ditemukan di database!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }

    private void aksiSimpan() {
        // 1. Validasi Input Kosong
        if (txtNamaLengkap.getText().trim().isEmpty() || txtNoTelp.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nama Lengkap dan No. Telp tidak boleh kosong!",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Update objek & simpan ke MySQL melalui PenggunaDAO
        if (instrukturTarget != null) {
            instrukturTarget.setNamaLengkap(txtNamaLengkap.getText().trim());
            instrukturTarget.setNoTelp(txtNoTelp.getText().trim());
            instrukturTarget.setSpesialisasi(cbSpesialisasi.getSelectedItem().toString());
            instrukturTarget.setLevelJlpt(cbLevelJlpt.getSelectedItem().toString());
            instrukturTarget.setKelasDiampu(cbKelasDiampu.getSelectedItem().toString());

            boolean ok = PenggunaDAO.ubahInstruktur(instrukturTarget);
            DataStore.muatUlang(); // Sinkronkan cache (sekaligus rollback memori bila update gagal)

            if (ok) {
                JOptionPane.showMessageDialog(this, "Data instruktur berhasil diperbarui!",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);

                if (panelInduk != null) {
                    panelInduk.loadTableData("");
                }

                this.dispose();
            }
        }
    }
}