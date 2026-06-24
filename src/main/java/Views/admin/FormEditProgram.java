package Views.admin;

import DataStore.DataStore;
import Model.Program;

import javax.swing.*;
import java.awt.*;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

public class FormEditProgram extends javax.swing.JDialog {

    private String idTarget;
    // Asumsi Anda memiliki PanelDataProgram layaknya PanelDataInstruktur
    // Ganti Object dengan PanelDataProgram jika class-nya sudah ada
    private Object panelInduk; 
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

    private JButton btnBatal;
    private JButton btnSimpan;

    // --- Definisi Warna & Font Sesuai Konvensi SIMAPAN ---
    private final Color MAROON_UTAMA = new Color(122, 0, 0);
    private final Color MAROON_GELAP = new Color(160, 20, 20);
    private final Color BG_BODY = new Color(248, 248, 248);
    private final Color TXT_ABU = new Color(90, 90, 90);
    private final Color TXT_UTAMA = new Color(10, 10, 10);
    
    private final Font FONT_BOLD_10 = new Font("Inter", Font.BOLD, 10);
    private final Font FONT_BOLD_11 = new Font("Inter", Font.BOLD, 11);
    private final Font FONT_PLAIN_11 = new Font("Inter", Font.PLAIN, 11);
    private final Font FONT_BOLD_16 = new Font("Inter", Font.BOLD, 16);

    /**
     * Constructor Manual FormEditProgram
     * Catatan: Ubah tipe data Object panelInduk menjadi PanelDataProgram jika classnya sudah Anda buat
     */
    public FormEditProgram(java.awt.Frame parent, boolean modal, String idProgram, Object panelInduk) {
        super(parent, modal);
        this.idTarget = idProgram;
        this.panelInduk = panelInduk;
        
        // Setup Window Dialog
        this.setTitle("Edit Data Program Pemagangan");
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
        
        lblJudulHeader = new JLabel("Edit Program");
        lblJudulHeader.setForeground(Color.WHITE);
        lblJudulHeader.setFont(FONT_BOLD_16);
        panelHeader.add(lblJudulHeader, new AbsoluteConstraints(20, 15, 300, 20));

        lblSubHeader = new JLabel("Ubah rincian informasi program pemagangan");
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
        
        txtIdProgram = new JTextField();
        txtIdProgram.setFont(FONT_PLAIN_11);
        txtIdProgram.setEditable(false);
        txtIdProgram.setBackground(new Color(230, 230, 230)); // Indikator read-only
        panelBody.add(txtIdProgram, new AbsoluteConstraints(20, 70, 250, 30));

        // Nama Program
        lblNamaProgram = new JLabel("Nama Program");
        lblNamaProgram.setForeground(TXT_ABU);
        lblNamaProgram.setFont(FONT_BOLD_11);
        panelBody.add(lblNamaProgram, new AbsoluteConstraints(20, 110, 250, 20));
        
        txtNamaProgram = new JTextField();
        txtNamaProgram.setFont(FONT_PLAIN_11);
        panelBody.add(txtNamaProgram, new AbsoluteConstraints(20, 130, 250, 30));

        // Bidang Usaha
        lblBidangUsaha = new JLabel("Bidang Usaha");
        lblBidangUsaha.setForeground(TXT_ABU);
        lblBidangUsaha.setFont(FONT_BOLD_11);
        panelBody.add(lblBidangUsaha, new AbsoluteConstraints(20, 170, 250, 20));
        
        txtBidangUsaha = new JTextField();
        txtBidangUsaha.setFont(FONT_PLAIN_11);
        panelBody.add(txtBidangUsaha, new AbsoluteConstraints(20, 190, 250, 30));


        // --- KOLOM KANAN (Biaya & Kuota) ---
        lblSectionBiaya = new JLabel("DETAIL BIAYA & KUOTA");
        lblSectionBiaya.setForeground(MAROON_UTAMA);
        lblSectionBiaya.setFont(FONT_BOLD_10);
        panelBody.add(lblSectionBiaya, new AbsoluteConstraints(310, 15, 200, 20));

        separatorBiaya = new JSeparator();
        panelBody.add(separatorBiaya, new AbsoluteConstraints(310, 35, 250, 10));

        // Biaya (Tanpa format Rp agar mudah diedit)
        lblBiaya = new JLabel("Biaya Pemagangan (Angka)");
        lblBiaya.setForeground(TXT_ABU);
        lblBiaya.setFont(FONT_BOLD_11);
        panelBody.add(lblBiaya, new AbsoluteConstraints(310, 50, 250, 20));
        
        txtBiaya = new JTextField();
        txtBiaya.setFont(FONT_PLAIN_11);
        panelBody.add(txtBiaya, new AbsoluteConstraints(310, 70, 250, 30));

        // Kuota
        lblKuota = new JLabel("Total Kuota Peserta (Angka)");
        lblKuota.setForeground(TXT_ABU);
        lblKuota.setFont(FONT_BOLD_11);
        panelBody.add(lblKuota, new AbsoluteConstraints(310, 110, 250, 20));
        
        txtKuota = new JTextField();
        txtKuota.setFont(FONT_PLAIN_11);
        panelBody.add(txtKuota, new AbsoluteConstraints(310, 130, 250, 30));

        this.getContentPane().add(panelBody, new AbsoluteConstraints(0, 68, 600, 260));


        // ==========================================
        // 3. PANEL FOOTER (Tombol Aksi)
        // ==========================================
        btnBatal = new JButton("Batal");
        btnBatal.setFont(FONT_BOLD_11);
        btnBatal.setBackground(Color.WHITE);
        btnBatal.setForeground(TXT_UTAMA);
        btnBatal.setFocusPainted(false);
        btnBatal.addActionListener(e -> this.dispose());
        this.getContentPane().add(btnBatal, new AbsoluteConstraints(350, 335, 100, 35));

        btnSimpan = new JButton("Simpan");
        btnSimpan.setFont(FONT_BOLD_11);
        btnSimpan.setBackground(MAROON_UTAMA);
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.addActionListener(e -> aksiSimpan());
        this.getContentPane().add(btnSimpan, new AbsoluteConstraints(460, 335, 100, 35));
    }

    private void loadData() {
        boolean dataDitemukan = false;
        
        for (Program prog : DataStore.daftarProgram) {
            if (prog.getIdProgram().equals(idTarget)) {
                this.programTarget = prog;
                
                // Prefill form
                txtIdProgram.setText(prog.getIdProgram());
                txtNamaProgram.setText(prog.getNamaProgram());
                txtBidangUsaha.setText(prog.getBidangUsaha());
                
                // Menampilkan angka asli tanpa format mata uang agar user gampang mengeditnya
                // Menghilangkan desimal .0 jika angkanya bulat
                long biayaBulat = (long) prog.getBiaya();
                if (prog.getBiaya() == biayaBulat) {
                    txtBiaya.setText(String.valueOf(biayaBulat));
                } else {
                    txtBiaya.setText(String.valueOf(prog.getBiaya()));
                }
                
                txtKuota.setText(String.valueOf(prog.getKuota()));
                
                dataDitemukan = true;
                break;
            }
        }
        
        if (!dataDitemukan) {
            System.out.println("ERROR: Data Program dengan ID '" + idTarget + "' tidak ditemukan!");
            JOptionPane.showMessageDialog(this, "Data Program tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }

    private void aksiSimpan() {
        // 1. Validasi Input Kosong
        if (txtNamaProgram.getText().trim().isEmpty() || txtBidangUsaha.getText().trim().isEmpty() ||
            txtBiaya.getText().trim().isEmpty() || txtKuota.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Semua kolom harus diisi!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double biayaBaru = 0;
        int kuotaBaru = 0;

        // 2. Validasi Tipe Data (Angka)
        try {
            biayaBaru = Double.parseDouble(txtBiaya.getText().trim());
            kuotaBaru = Integer.parseInt(txtKuota.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Kolom Biaya dan Kuota harus berupa angka yang valid (tanpa titik atau koma kecuali untuk desimal)!", 
                "Error Format Angka", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Simpan Perubahan ke Referensi Objek yang Sama
        if (programTarget != null) {
            programTarget.setNamaProgram(txtNamaProgram.getText().trim());
            programTarget.setBidangUsaha(txtBidangUsaha.getText().trim());
            programTarget.setBiaya(biayaBaru);
            programTarget.setKuota(kuotaBaru);

            JOptionPane.showMessageDialog(this, "Data Program berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

            if (panelInduk != null && panelInduk instanceof PanelDataProgram) {
                ((PanelDataProgram) panelInduk).loadTableData(""); 
            }
            
            
            // 5. Tutup form
            this.dispose();
        }
    }
}