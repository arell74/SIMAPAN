package Views.instruktur;

import DataStore.DataStore;
import Database.PenggunaDAO;
import Model.Instruktur;
import Model.Pengguna;

public class FormProfilInstruktur extends javax.swing.JDialog {

    private Instruktur instrukturAktif;

    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtUser;
    private javax.swing.JTextField txtNoHp;
    private javax.swing.JTextField txtRole;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JCheckBox chkShowPass;
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnSimpan;

    public FormProfilInstruktur(java.awt.Frame parent, boolean modal, String usernameLogin) {
        super(parent, modal);

        DataStore.muatUlang();

        for (Pengguna p : DataStore.daftarPengguna) {
            if (p instanceof Instruktur) {
                Instruktur ins = (Instruktur) p;
                if ((ins.getUsername() != null && ins.getUsername().equals(usernameLogin))
                        || (ins.getIdInstruktur() != null && ins.getIdInstruktur().equals(usernameLogin))) {
                    this.instrukturAktif = ins;
                    break;
                }
            }
        }

        if (this.instrukturAktif == null) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Data Instruktur tidak ditemukan!",
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            this.dispose();
            return;
        }

        setupUI();
        setupListeners();
    }

    private void setupUI() {
        this.setTitle("Profil Instruktur");
        this.setSize(390, 310);
        this.setLocationRelativeTo(this.getParent());
        this.setResizable(false);

        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setLayout(new java.awt.GridLayout(6, 2, 10, 15));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        javax.swing.JLabel lblNama = new javax.swing.JLabel("Nama Lengkap:");
        txtNama = new javax.swing.JTextField(instrukturAktif.getNamaLengkap());

        javax.swing.JLabel lblUser = new javax.swing.JLabel("Username (PK):");
        txtUser = new javax.swing.JTextField(instrukturAktif.getUsername());
        txtUser.setEditable(false);
        txtUser.setBackground(new java.awt.Color(240, 240, 240));

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
        btnBatal.setFocusPainted(false);

        btnSimpan = new javax.swing.JButton("Simpan");
        btnSimpan.setBackground(new java.awt.Color(122, 0, 0));
        btnSimpan.setForeground(java.awt.Color.WHITE);
        btnSimpan.setFocusPainted(false);

        panel.add(lblNama); panel.add(txtNama);
        panel.add(lblUser); panel.add(txtUser);
        panel.add(lblNoHp); panel.add(txtNoHp);
        panel.add(lblRole); panel.add(txtRole);
        panel.add(lblPass); panel.add(panelPass);
        panel.add(btnBatal); panel.add(btnSimpan);

        this.add(panel);
    }

    private void setupListeners() {
        chkShowPass.addActionListener(evt -> {
            if (chkShowPass.isSelected()) {
                txtPass.setEchoChar((char) 0);
            } else {
                txtPass.setEchoChar('•');
            }
        });

        btnBatal.addActionListener(evt -> FormProfilInstruktur.this.dispose());

        btnSimpan.addActionListener(evt -> aksiSimpan());
    }

    private void aksiSimpan() {
        String namaBaru = txtNama.getText().trim();
        String noHpBaru = txtNoHp.getText().trim();
        String passBaru = new String(txtPass.getPassword()).trim();

        if (namaBaru.isEmpty() || noHpBaru.isEmpty() || passBaru.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Semua kolom wajib diisi!",
                    "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        instrukturAktif.setNamaLengkap(namaBaru);
        instrukturAktif.setNoTelp(noHpBaru);
        instrukturAktif.setPassword(passBaru);

        if (PenggunaDAO.ubahInstruktur(instrukturAktif)) {
            DataStore.muatUlang();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Profil instruktur berhasil diperbarui ke database!",
                    "Sukses", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } else {
            DataStore.muatUlang();
        }
    }
}