package Views.admin;

import DataStore.DataStore;
import Model.Seleksi;

import javax.swing.*;
import java.awt.*;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

public class FormEditSeleksi extends javax.swing.JDialog {

    private String idTarget;
    private Object panelInduk; // Ganti Object dengan PanelDataSeleksi jika sudah ada
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
    public FormEditSeleksi(java.awt.Frame parent, boolean modal, String idSeleksi, Object panelInduk) {
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
        panelBody.add(lblSectionPeserta, new AbsoluteConstraints(20, 15, 200, 20));

        separatorPeserta = new JSeparator();
        panelBody.add(separatorPeserta, new AbsoluteConstraints(20, 35, 260, 10));

        lblIdSeleksi = new JLabel("ID Seleksi");
        lblIdSeleksi.setForeground(TXT_ABU);
        lblIdSeleksi.setFont(FONT_BOLD_11);
        panelBody.add(lblIdSeleksi, new AbsoluteConstraints(20, 50, 260, 20));
        
        txtIdSeleksi = createReadOnlyField();
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

        lblJenisSeleksi = new JLabel("Jenis Seleksi");
        lblJenisSeleksi.setForeground(TXT_ABU);
        lblJenisSeleksi.setFont(FONT_BOLD_11);
        panelBody.add(lblJenisSeleksi, new AbsoluteConstraints(320, 50, 260, 20));
        
        cbJenisSeleksi = new JComboBox<>(new String[]{
            "Seleksi Bahasa Jepang", "Seleksi Fisik", "Seleksi Matematika", "Seleksi Wawancara"
        });
        cbJenisSeleksi.setFont(FONT_PLAIN_11);
        cbJenisSeleksi.setBackground(Color.WHITE);
        panelBody.add(cbJenisSeleksi, new AbsoluteConstraints(320, 70, 260, 30));

        lblTanggalSeleksi = new JLabel("Tanggal Seleksi (DD/MM/YYYY)");
        lblTanggalSeleksi.setForeground(TXT_ABU);
        lblTanggalSeleksi.setFont(FONT_BOLD_11);
        panelBody.add(lblTanggalSeleksi, new AbsoluteConstraints(320, 110, 260, 20));
        
        txtTanggalSeleksi = new JTextField();
        txtTanggalSeleksi.setFont(FONT_PLAIN_11);
        panelBody.add(txtTanggalSeleksi, new AbsoluteConstraints(320, 130, 260, 30));

        lblNilai = new JLabel("Total Nilai (Angka)");
        lblNilai.setForeground(TXT_ABU);
        lblNilai.setFont(FONT_BOLD_11);
        panelBody.add(lblNilai, new AbsoluteConstraints(320, 170, 260, 20));
        
        txtNilai = new JTextField();
        txtNilai.setFont(FONT_PLAIN_11);
        panelBody.add(txtNilai, new AbsoluteConstraints(320, 190, 260, 30));

        lblStatusHasil = new JLabel("Status Hasil");
        lblStatusHasil.setForeground(TXT_ABU);
        lblStatusHasil.setFont(FONT_BOLD_11);
        panelBody.add(lblStatusHasil, new AbsoluteConstraints(320, 230, 260, 20));
        
        cbStatusHasil = new JComboBox<>(new String[]{"Lulus", "Gagal", "Menunggu"});
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
        btnBatal.addActionListener(e -> this.dispose());
        this.getContentPane().add(btnBatal, new AbsoluteConstraints(370, 375, 100, 35));

        btnSimpan = new JButton("Simpan");
        btnSimpan.setFont(FONT_BOLD_11);
        btnSimpan.setBackground(MAROON_UTAMA);
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
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
        boolean dataDitemukan = false;
        
        for (Seleksi sel : DataStore.daftarSeleksi) {
            if (sel.getIdSeleksi().equals(idTarget)) {
                this.seleksiTarget = sel;
                
                // Prefill Data Peserta (Kiri)
                txtIdSeleksi.setText(sel.getIdSeleksi());
                if (sel.getPeserta() != null) {
                    txtIdPeserta.setText(sel.getPeserta().getIdPeserta());
                    txtNamaPeserta.setText(sel.getPeserta().getNamaLengkap());
                }
                
                // Prefill Data Hasil (Kanan)
                cbJenisSeleksi.setSelectedItem(sel.getJenisSeleksi());
                txtTanggalSeleksi.setText(sel.getTanggalSeleksi());
                txtNilai.setText(String.valueOf(sel.getNilai()));
                cbStatusHasil.setSelectedItem(sel.getStatusHasil());
                
                dataDitemukan = true;
                break;
            }
        }
        
        if (!dataDitemukan) {
            JOptionPane.showMessageDialog(this, "Data Seleksi tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }

    private void aksiSimpan() {
        // 1. Validasi Kolom Kosong
        if (txtTanggalSeleksi.getText().trim().isEmpty() || txtNilai.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Tanggal Seleksi dan Total Nilai tidak boleh kosong!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int nilaiBaru = 0;

        // 2. Validasi Angka pada Nilai
        try {
            nilaiBaru = Integer.parseInt(txtNilai.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Kolom Total Nilai harus berupa angka bulat yang valid!", 
                "Error Format Angka", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Simpan Data ke Objek Target
        if (seleksiTarget != null) {
            seleksiTarget.setJenisSeleksi(cbJenisSeleksi.getSelectedItem().toString());
            seleksiTarget.setTanggalSeleksi(txtTanggalSeleksi.getText().trim());
            seleksiTarget.setNilai(nilaiBaru);
            seleksiTarget.setStatusHasil(cbStatusHasil.getSelectedItem().toString());

            // 4. Update Status Lulus/Gagal Peserta jika diperlukan
            // (Opsional: Anda bisa menambahkan logika di sini untuk meng-update 
            // status keseluruhan Peserta berdasarkan status seleksi ini)

            JOptionPane.showMessageDialog(this, "Data Hasil Seleksi berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

            if (panelInduk != null) {
                 ((PanelManajemenSeleksi) panelInduk).loadTableData(""); 
            }
            this.dispose();
        }
    }
}