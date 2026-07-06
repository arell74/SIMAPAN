package Views.instruktur; // Sesuaikan dengan nama package milikmu!

import DataStore.DataStore;
import Model.Pengguna;
import Model.Instruktur;
// Asumsi DataStore ada di package controller atau model, pastikan di-import jika beda package
// import Controller.DataStore; 

public class FormProfilInstruktur extends javax.swing.JDialog {

    private Instruktur instrukturAktif;

    // Komponen UI dideklarasikan di tingkat kelas agar bisa diakses event listener
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtUser;
    private javax.swing.JTextField txtNoHp;
    private javax.swing.JTextField txtRole;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JCheckBox chkShowPass;
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnSimpan;

    // Constructor: Menangkap Frame Induk dan ID Pengguna
   public FormProfilInstruktur(java.awt.Frame parent, boolean modal, String usernameLogin) {
        super(parent, modal);
        
        // Cari Data Instruktur dari DataStore berdasarkan USERNAME
        for (Pengguna p : DataStore.daftarPengguna) {
            // Gunakan getUsername() di sini!
            if (p.getUsername().equals(usernameLogin) && p instanceof Instruktur) {
                this.instrukturAktif = (Instruktur) p;
                break;
            }
        }

        if (this.instrukturAktif == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Data Instruktur tidak ditemukan!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            this.dispose(); 
            return;
        }

        // 2. Inisialisasi Tampilan Murni
        setupUI();
        
        // 3. Pasang Event Listener (Telinga)
        setupListeners();
    }

    private void setupUI() {
        this.setTitle("Profil Instruktur");
        this.setSize(380, 300);
        this.setLocationRelativeTo(this.getParent());
        this.setResizable(false);

        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setLayout(new java.awt.GridLayout(6, 2, 10, 15));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Membuat dan Mengisi Data ke Komponen
        javax.swing.JLabel lblNama = new javax.swing.JLabel("Nama Lengkap:");
        txtNama = new javax.swing.JTextField(instrukturAktif.getNamaLengkap());

        javax.swing.JLabel lblUser = new javax.swing.JLabel("Username:");
        txtUser = new javax.swing.JTextField(instrukturAktif.getUsername());

        javax.swing.JLabel lblNoHp = new javax.swing.JLabel("No. Telp / WhatsApp:");
        txtNoHp = new javax.swing.JTextField(instrukturAktif.getNoTelp());

        javax.swing.JLabel lblRole = new javax.swing.JLabel("Role Pengguna:");
        txtRole = new javax.swing.JTextField(instrukturAktif.getRole());
        txtRole.setEditable(false);
        txtRole.setBackground(new java.awt.Color(240, 240, 240));

        javax.swing.JLabel lblPass = new javax.swing.JLabel("Password:");
        javax.swing.JPanel panelPass = new javax.swing.JPanel(new java.awt.BorderLayout());
        txtPass = new javax.swing.JPasswordField(instrukturAktif.getPassword());
        chkShowPass = new javax.swing.JCheckBox("👁");
        chkShowPass.setFocusPainted(false);
        
        panelPass.add(txtPass, java.awt.BorderLayout.CENTER);
        panelPass.add(chkShowPass, java.awt.BorderLayout.EAST);

        btnBatal = new javax.swing.JButton("Batal");
        btnSimpan = new javax.swing.JButton("Simpan");
        btnSimpan.setBackground(new java.awt.Color(122, 0, 0));
        btnSimpan.setForeground(java.awt.Color.WHITE);

        // Susun ke dalam panel
        panel.add(lblNama); panel.add(txtNama);
        panel.add(lblUser); panel.add(txtUser);
        panel.add(lblNoHp); panel.add(txtNoHp);
        panel.add(lblRole); panel.add(txtRole);
        panel.add(lblPass); panel.add(panelPass);
        panel.add(btnBatal); panel.add(btnSimpan);

        this.add(panel);
    }

    private void setupListeners() {
        chkShowPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (chkShowPass.isSelected()) {
                    txtPass.setEchoChar((char) 0);
                } else {
                    txtPass.setEchoChar('•');
                }
            }
        });

        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FormProfilInstruktur.this.dispose();
            }
        });

        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String namaBaru = txtNama.getText().trim();
                String userBaru = txtUser.getText().trim();
                String noHpBaru = txtNoHp.getText().trim();
                String passBaru = new String(txtPass.getPassword());

                if (namaBaru.isEmpty() || userBaru.isEmpty() || noHpBaru.isEmpty() || passBaru.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(FormProfilInstruktur.this, "Semua kolom wajib diisi!", "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Update memori DataStore
                instrukturAktif.setNamaLengkap(namaBaru);
                instrukturAktif.setUsername(userBaru);
                instrukturAktif.setNoTelp(noHpBaru);
                instrukturAktif.setPassword(passBaru);

                javax.swing.JOptionPane.showMessageDialog(FormProfilInstruktur.this, "Profil berhasil diperbarui!", "Sukses", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                FormProfilInstruktur.this.dispose();
            }
        });
    }
}