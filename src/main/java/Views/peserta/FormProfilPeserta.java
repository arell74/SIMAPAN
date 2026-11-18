/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Views.peserta;

import DataStore.DataStore;
import Database.PesertaDAO;
import Model.Pengguna;
import Model.Peserta;
import Model.Program;
import java.util.Objects;
import javax.swing.JOptionPane;

/**
 * Form pop-up untuk menampilkan profil Peserta.
 * Terdapat field yang Read-Only (hanya bisa dilihat) dan 
 * Editable (bisa diperbarui seperti kontak dan password).
 */
public class FormProfilPeserta extends javax.swing.JDialog {

    private String usernameAktif;
    private Peserta pesertaAktif;

    /**
     * Creates new form FormProfilPeserta
     */
    public FormProfilPeserta(java.awt.Frame parent, boolean modal, String username) {
        super(parent, modal);
        initComponents();

        this.usernameAktif = username;
        setTitle("Profil Saya - LPK Yuuki");
        setSize(580, 640);
        setLocationRelativeTo(parent);
        setResizable(false);

        loadDataProfil();
    }

    private void loadDataProfil() {
        DataStore.muatUlang();

        for (Pengguna p : DataStore.daftarPengguna) {
            if (p.getUsername() != null && p.getUsername().equals(usernameAktif)) {
                if (p instanceof Peserta) {
                    pesertaAktif = (Peserta) p;
                    break;
                }
            }
        }

        if (pesertaAktif != null) {
            // === DATA READ-ONLY ===
            txtIdPeserta.setText(pesertaAktif.getIdPeserta());
            txtNik.setText(pesertaAktif.getNik());
            txtNama.setText(pesertaAktif.getNamaLengkap());
            txtGender.setText(pesertaAktif.getJenisKelamin());

            String namaProgram = "Belum Ditentukan";
            for (Program prg : DataStore.daftarProgram) {
                if (Objects.equals(prg.getIdProgram(), pesertaAktif.getProgram())) {
                    namaProgram = prg.getNamaProgram();
                    break;
                }
            }
            txtProgram.setText(namaProgram);
            txtKelasLevel.setText(pesertaAktif.getKelas() + " (Level " + pesertaAktif.getLevelBahasa() + ")");
            txtStatus.setText(pesertaAktif.getStatusSeleksi() + " | " + pesertaAktif.getStatusKeberangkatan());

            String namaInstruktur = (pesertaAktif.getInstrukturDamping() != null)
                    ? pesertaAktif.getInstrukturDamping().getNamaLengkap() : "Belum Ada";
            txtInstruktur.setText(namaInstruktur);

            // === DATA EDITABLE ===
            txtNoHp.setText(pesertaAktif.getNoHp());
            txtAlamat.setText(pesertaAktif.getAlamat());
            txtUsername.setText(pesertaAktif.getUsername());
            txtPassword.setText(pesertaAktif.getPassword());
        } else {
            JOptionPane.showMessageDialog(this, "Data profil tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelUtama = new javax.swing.JPanel();
        panelHeader = new javax.swing.JPanel();
        lblJudul = new javax.swing.JLabel();
        lblSubJudul = new javax.swing.JLabel();
        
        jLabel1 = new javax.swing.JLabel();
        txtIdPeserta = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNik = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtGender = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtProgram = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtKelasLevel = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtStatus = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtInstruktur = new javax.swing.JTextField();
        
        jSeparator1 = new javax.swing.JSeparator();
        lblSubEdit = new javax.swing.JLabel();
        
        jLabel9 = new javax.swing.JLabel();
        txtNoHp = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        
        chkShowPassword = new javax.swing.JCheckBox();
        
        jLabel12 = new javax.swing.JLabel();
        scrollAlamat = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextArea();
        
        btnSimpan = new javax.swing.JButton();
        btnTutup = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelUtama.setBackground(new java.awt.Color(255, 255, 255));
        panelUtama.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelHeader.setBackground(new java.awt.Color(122, 0, 0));
        panelHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblJudul.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 18));
        lblJudul.setForeground(new java.awt.Color(255, 255, 255));
        lblJudul.setText("Profil Peserta");
        panelHeader.add(lblJudul, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, -1, -1));

        lblSubJudul.setFont(new java.awt.Font("Inter", 0, 12));
        lblSubJudul.setForeground(new java.awt.Color(240, 200, 200));
        lblSubJudul.setText("Informasi Pribadi & Akademik Anda");
        panelHeader.add(lblSubJudul, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        panelUtama.add(panelHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 580, 75));

        java.awt.Color colorReadOnly = new java.awt.Color(245, 245, 245);
        java.awt.Font fontLabel = new java.awt.Font("Inter", java.awt.Font.BOLD, 11);
        java.awt.Font fontField = new java.awt.Font("Inter", 0, 12);

        jLabel1.setFont(fontLabel); jLabel1.setText("ID Peserta");
        panelUtama.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));
        txtIdPeserta.setEditable(false); txtIdPeserta.setBackground(colorReadOnly); txtIdPeserta.setFont(fontField);
        panelUtama.add(txtIdPeserta, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 250, 30));

        jLabel2.setFont(fontLabel); jLabel2.setText("NIK (No. Identitas)");
        panelUtama.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 90, -1, -1));
        txtNik.setEditable(false); txtNik.setBackground(colorReadOnly); txtNik.setFont(fontField);
        panelUtama.add(txtNik, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 110, 250, 30));

        jLabel3.setFont(fontLabel); jLabel3.setText("Nama Lengkap");
        panelUtama.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, -1, -1));
        txtNama.setEditable(false); txtNama.setBackground(colorReadOnly); txtNama.setFont(fontField);
        panelUtama.add(txtNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 250, 30));

        jLabel4.setFont(fontLabel); jLabel4.setText("Jenis Kelamin");
        panelUtama.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 150, -1, -1));
        txtGender.setEditable(false); txtGender.setBackground(colorReadOnly); txtGender.setFont(fontField);
        panelUtama.add(txtGender, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 170, 250, 30));

        jLabel5.setFont(fontLabel); jLabel5.setText("Program Pemagangan");
        panelUtama.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, -1));
        txtProgram.setEditable(false); txtProgram.setBackground(colorReadOnly); txtProgram.setFont(fontField);
        panelUtama.add(txtProgram, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 250, 30));

        jLabel6.setFont(fontLabel); jLabel6.setText("Kelas & Level");
        panelUtama.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 210, -1, -1));
        txtKelasLevel.setEditable(false); txtKelasLevel.setBackground(colorReadOnly); txtKelasLevel.setFont(fontField);
        panelUtama.add(txtKelasLevel, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 230, 250, 30));

        jLabel7.setFont(fontLabel); jLabel7.setText("Status Keberangkatan");
        panelUtama.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, -1));
        txtStatus.setEditable(false); txtStatus.setBackground(colorReadOnly); txtStatus.setFont(fontField);
        panelUtama.add(txtStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 250, 30));

        jLabel8.setFont(fontLabel); jLabel8.setText("Instruktur Pendamping");
        panelUtama.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 270, -1, -1));
        txtInstruktur.setEditable(false); txtInstruktur.setBackground(colorReadOnly); txtInstruktur.setFont(fontField);
        panelUtama.add(txtInstruktur, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 290, 250, 30));

        panelUtama.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 520, 10));
        
        lblSubEdit.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 13));
        lblSubEdit.setForeground(new java.awt.Color(70, 70, 70));
        lblSubEdit.setText("Informasi Kontak & Akun (Dapat Diubah)");
        panelUtama.add(lblSubEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 355, -1, -1));

        jLabel9.setFont(fontLabel); jLabel9.setText("No. HP / WhatsApp");
        panelUtama.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 385, -1, -1));
        txtNoHp.setFont(fontField);
        panelUtama.add(txtNoHp, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 405, 250, 30));

        jLabel10.setFont(fontLabel); jLabel10.setText("Username Login");
        panelUtama.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 385, -1, -1));
        txtUsername.setFont(fontField);
        panelUtama.add(txtUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 405, 250, 30));

        jLabel12.setFont(fontLabel); jLabel12.setText("Alamat Lengkap");
        panelUtama.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 445, -1, -1));
        txtAlamat.setFont(fontField);
        txtAlamat.setLineWrap(true);
        txtAlamat.setWrapStyleWord(true);
        scrollAlamat.setViewportView(txtAlamat);
        panelUtama.add(scrollAlamat, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 465, 250, 55));

        jLabel11.setFont(fontLabel); jLabel11.setText("Password Login");
        panelUtama.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 445, -1, -1));
        txtPassword.setFont(fontField);
        panelUtama.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 465, 250, 30));

        chkShowPassword.setText("Tampilkan Password");
        chkShowPassword.setBackground(new java.awt.Color(255, 255, 255));
        chkShowPassword.setFont(new java.awt.Font("Inter", 0, 11));
        chkShowPassword.setForeground(new java.awt.Color(100, 100, 100));
        chkShowPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkShowPassword.setFocusPainted(false);
        chkShowPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShowPasswordActionPerformed(evt);
            }
        });
        panelUtama.add(chkShowPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 500, 150, 20));

        btnTutup.setBackground(new java.awt.Color(230, 230, 230));
        btnTutup.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        btnTutup.setForeground(new java.awt.Color(50, 50, 50));
        btnTutup.setText("Tutup");
        btnTutup.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTutup.setFocusPainted(false);
        btnTutup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });
        panelUtama.add(btnTutup, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 545, 110, 38));

        btnSimpan.setBackground(new java.awt.Color(20, 120, 40));
        btnSimpan.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setText("Simpan Perubahan");
        btnSimpan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpan.setFocusPainted(false);
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        panelUtama.add(btnSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 545, 140, 38));

        getContentPane().add(panelUtama, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 580, 610));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // === LOGIKA SHOW/HIDE PASSWORD ===
    private void chkShowPasswordActionPerformed(java.awt.event.ActionEvent evt) {
        if (chkShowPassword.isSelected()) {
            txtPassword.setEchoChar((char) 0);
        } else {
            txtPassword.setEchoChar('\u2022');
        }
    }

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {
        if (pesertaAktif == null) {
            return;
        }

        String noHpBaru = txtNoHp.getText().trim();
        String alamatBaru = txtAlamat.getText().trim();
        String usernameBaru = txtUsername.getText().trim();
        String passwordBaru = new String(txtPassword.getPassword()).trim();

        if (usernameBaru.isEmpty() || passwordBaru.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Username dan Password tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (Pengguna p : DataStore.daftarPengguna) {
            if (p.getUsername() != null && p.getUsername().equalsIgnoreCase(usernameBaru)) {
                boolean akunSendiri = (p instanceof Peserta)
                        && Objects.equals(((Peserta) p).getIdPeserta(), pesertaAktif.getIdPeserta());
                if (!akunSendiri) {
                    JOptionPane.showMessageDialog(this,
                        "Username sudah digunakan oleh akun lain! Silakan pilih yang lain.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }

        // Simpan nilai lama untuk rollback jika update DB gagal
        String noHpLama = pesertaAktif.getNoHp();
        String alamatLama = pesertaAktif.getAlamat();
        String usernameLama = pesertaAktif.getUsername();
        String passwordLama = pesertaAktif.getPassword();

        pesertaAktif.setNoHp(noHpBaru);
        pesertaAktif.setAlamat(alamatBaru);
        pesertaAktif.setUsername(usernameBaru);
        pesertaAktif.setPassword(passwordBaru);

        if (PesertaDAO.ubah(pesertaAktif)) {
            if (DataStore.penggunaLogin != null) {
                DataStore.penggunaLogin.setUsername(usernameBaru);
                DataStore.penggunaLogin.setPassword(passwordBaru);
            }
            DataStore.muatUlang();

            JOptionPane.showMessageDialog(this,
                "Profil dan data akun berhasil disimpan ke database!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } else {
            pesertaAktif.setNoHp(noHpLama);
            pesertaAktif.setAlamat(alamatLama);
            pesertaAktif.setUsername(usernameLama);
            pesertaAktif.setPassword(passwordLama);
            DataStore.muatUlang();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTutup;
    private javax.swing.JCheckBox chkShowPassword;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblJudul;
    private javax.swing.JLabel lblSubEdit;
    private javax.swing.JLabel lblSubJudul;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelUtama;
    private javax.swing.JScrollPane scrollAlamat;
    private javax.swing.JTextArea txtAlamat;
    private javax.swing.JTextField txtGender;
    private javax.swing.JTextField txtIdPeserta;
    private javax.swing.JTextField txtInstruktur;
    private javax.swing.JTextField txtKelasLevel;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNik;
    private javax.swing.JTextField txtNoHp;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtProgram;
    private javax.swing.JTextField txtStatus;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}