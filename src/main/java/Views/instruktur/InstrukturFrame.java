/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views.instruktur;

import DataStore.DataStore;
import Model.HasilKuis;
import Model.Peserta;
import Views.admin.FormLogin;
import Views.admin.FormTambahPeserta;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author arelssi
 */
public class InstrukturFrame extends javax.swing.JFrame {
    private String idInstruktur;
    private HasilKuis hasilAktif;
    private Peserta pesertaAktif;
    public void showPanel(JPanel panel) {
            mainContent.removeAll(); 

            mainContent.setLayout(new java.awt.BorderLayout());
            mainContent.add(panel, java.awt.BorderLayout.CENTER);

            mainContent.repaint();
            mainContent.revalidate();
        }

        private Color colorActive = new Color(90, 0, 0);
        private Color colorDefault = new Color(122, 0, 0);
        private Color foregroundActive = new Color(255,255,255);
        private Color foregroundDefault = new Color(255,210,210);

        private void setActiveButton(JButton activeBtn) {
        JButton[] sidebarButtons = {btnDashboard, btnPeserta, btnInstruktur, btnManajemenSeleksi, btnInputMateri};

        for (JButton btn : sidebarButtons) {
            btn.setBackground(colorDefault);
            btn.setForeground(foregroundDefault);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
        }

            activeBtn.setBackground(colorActive);
            activeBtn.setForeground(foregroundActive);
        }
        
    public InstrukturFrame(HasilKuis hasil, Peserta peserta, String idInstruktur) {
        this.hasilAktif = hasil;
        this.pesertaAktif = peserta;
        this.idInstruktur = idInstruktur;
        
        initComponents();
        setSize(1200, 680);
            pack();
            setLocationRelativeTo(null);
            setResizable(false);
            setTitle("SIMAPAN - Role Instruktur");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            lblTanggal.setText(new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy", 
                               new java.util.Locale("id", "ID")).format(new java.util.Date()));
            showPanel(new Views.instruktur.PanelDashboardInstruktur(this.idInstruktur));
    }
    
    private void tampilkanDialogProfilInstruktur(Model.Pengguna instrukturAktif) {
        
        if (instrukturAktif == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Data Instruktur login tidak valid!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- JURUS DOWNCASTING DIMULAI DI SINI ---
        // Kita ubah (cast) wujud Pengguna menjadi Instruktur agar noTelp bisa diakses
        Model.Instruktur dataInstruktur = null;
        
        if (instrukturAktif instanceof Model.Instruktur) {
            dataInstruktur = (Model.Instruktur) instrukturAktif;
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Akses ditolak! Objek ini bukan Instruktur.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        // -----------------------------------------

        // 1. Membuat Jendela Pop-up Murni 
        javax.swing.JDialog dialog = new javax.swing.JDialog((java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), "Profil Instruktur", true);
        dialog.setSize(380, 300); 
        dialog.setLocationRelativeTo(this); 
        dialog.setResizable(false);

        // 2. Menyiapkan Grid Layout
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setLayout(new java.awt.GridLayout(6, 2, 10, 15)); 
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 3. Meracik Komponen Inputan (Sekarang pakai variabel 'dataInstruktur')
        javax.swing.JLabel lblNama = new javax.swing.JLabel("Nama Lengkap:");
        javax.swing.JTextField txtNama = new javax.swing.JTextField(dataInstruktur.getNamaLengkap());

        javax.swing.JLabel lblUser = new javax.swing.JLabel("Username:");
        javax.swing.JTextField txtUser = new javax.swing.JTextField(dataInstruktur.getUsername());

        // MENGAMBIL NO TELP DARI OBJEK INSTRUKTUR
        javax.swing.JLabel lblNoHp = new javax.swing.JLabel("No. Telp / WhatsApp:");
        javax.swing.JTextField txtNoHp = new javax.swing.JTextField(dataInstruktur.getNoTelp());

        javax.swing.JLabel lblRole = new javax.swing.JLabel("Role Pengguna:");
        javax.swing.JTextField txtRole = new javax.swing.JTextField(dataInstruktur.getRole());
        txtRole.setEditable(false); 
        txtRole.setBackground(new java.awt.Color(240, 240, 240));

        javax.swing.JLabel lblPass = new javax.swing.JLabel("Password:");
        javax.swing.JPanel panelPass = new javax.swing.JPanel(new java.awt.BorderLayout());
        javax.swing.JPasswordField txtPass = new javax.swing.JPasswordField(dataInstruktur.getPassword());
        javax.swing.JCheckBox chkShowPass = new javax.swing.JCheckBox("👁");
        chkShowPass.setFocusPainted(false);
        
        panelPass.add(txtPass, java.awt.BorderLayout.CENTER);
        panelPass.add(chkShowPass, java.awt.BorderLayout.EAST);

        chkShowPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (chkShowPass.isSelected()) {
                    txtPass.setEchoChar((char) 0); 
                } else {
                    txtPass.setEchoChar('•'); 
                }
            }
        });

        // Tombol Aksi
        javax.swing.JButton btnBatal = new javax.swing.JButton("Batal");
        javax.swing.JButton btnSimpan = new javax.swing.JButton("Simpan");
        btnSimpan.setBackground(new java.awt.Color(122, 0, 0)); 
        btnSimpan.setForeground(java.awt.Color.WHITE);

        // 4. Menyusun Komponen
        panel.add(lblNama);
        panel.add(txtNama);
        panel.add(lblUser);
        panel.add(txtUser);
        panel.add(lblNoHp); 
        panel.add(txtNoHp);
        panel.add(lblRole);
        panel.add(txtRole);
        panel.add(lblPass);
        panel.add(panelPass); 
        panel.add(btnBatal);
        panel.add(btnSimpan);

        dialog.add(panel);

        final Model.Instruktur instrukturFinal = dataInstruktur; 
        
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialog.dispose(); 
            }
        });

        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String namaBaru = txtNama.getText().trim();
                String userBaru = txtUser.getText().trim();
                String noHpBaru = txtNoHp.getText().trim();
                String passBaru = new String(txtPass.getPassword());

                if (namaBaru.isEmpty() || userBaru.isEmpty() || noHpBaru.isEmpty() || passBaru.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(dialog, "Semua kolom wajib diisi!", "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // UPDATE DATA KE OBJEK SPESIFIK INSTRUKTUR
                instrukturFinal.setNamaLengkap(namaBaru);
                instrukturFinal.setUsername(userBaru); 
                instrukturFinal.setNoTelp(noHpBaru); // Menyimpan noTelp ke model Instruktur
                instrukturFinal.setPassword(passBaru);

                javax.swing.JOptionPane.showMessageDialog(dialog, "Profil Instruktur berhasil diperbarui!", "Sukses", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }
    
    private void muatDataHeader() { // (Atau method lain tempat error itu berada)
    
    // Tambahkan pembungkus IF ini
    if (this.hasilAktif != null && this.pesertaAktif != null) {
        
        // Pindahkan kodemu yang lama ke dalam sini
        String idPeserta = this.hasilAktif.getIdPeserta();
        // txtIdPeserta.setText(idPeserta);
        // ... (kode lanjutanmu) ...
        
    } else {
        // Opsional: Beri nilai default jika data dipanggil dari tombol menu utama
        System.out.println("Form dibuka tanpa data peserta terpilih.");
        // lblNamaPeserta.setText("-"); 
    }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sidebar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        btnDashboard = new javax.swing.JButton();
        btnInstruktur = new javax.swing.JButton();
        btnPeserta = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        btnManajemenSeleksi = new javax.swing.JButton();
        btnInputMateri = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        btnLogout = new javax.swing.JButton();
        btnLihatLaporan1 = new javax.swing.JButton();
        btnInputSoal = new javax.swing.JButton();
        panelHeadr = new javax.swing.JPanel();
        lblHalamanJudul = new javax.swing.JLabel();
        lblHalamanSub = new javax.swing.JLabel();
        lblTanggal = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btnProfile = new javax.swing.JButton();
        mainContent = new javax.swing.JPanel();
        panelKartu1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        lblTotalPeserta = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        scrollPane1 = new java.awt.ScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelPeserta = new javax.swing.JTable();
        btnTambahPeserta = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        sidebar.setBackground(new java.awt.Color(122, 0, 0));
        sidebar.setName("panelSidebar"); // NOI18N
        sidebar.setPreferredSize(new java.awt.Dimension(220, 680));
        sidebar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("JetBrainsMono NF ExtraBold", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("LPK YUUKI");
        jLabel1.setPreferredSize(new java.awt.Dimension(180, 26));
        sidebar.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 24, -1, -1));

        jLabel2.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 180, 180));
        jLabel2.setText("Training Center Indonesia");
        jLabel2.setPreferredSize(new java.awt.Dimension(180, 16));
        sidebar.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 52, -1, -1));

        jSeparator1.setForeground(new java.awt.Color(160, 30, 30));
        jSeparator1.setPreferredSize(new java.awt.Dimension(220, 16));
        sidebar.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 82, 130, 20));

        jLabel3.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 160, 160));
        jLabel3.setText("MENU UTAMA");
        jLabel3.setPreferredSize(new java.awt.Dimension(160, 14));
        sidebar.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 96, -1, -1));

        btnDashboard.setBackground(new java.awt.Color(122, 0, 0));
        btnDashboard.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        btnDashboard.setForeground(new java.awt.Color(255, 210, 210));
        btnDashboard.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/home.png")); // NOI18N
        btnDashboard.setText(" Dashboard");
        btnDashboard.setBorderPainted(false);
        btnDashboard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDashboard.setFocusPainted(false);
        btnDashboard.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDashboard.setPreferredSize(new java.awt.Dimension(220, 42));
        btnDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDashboardActionPerformed(evt);
            }
        });
        sidebar.add(btnDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 116, -1, -1));

        btnInstruktur.setBackground(new java.awt.Color(122, 0, 0));
        btnInstruktur.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        btnInstruktur.setForeground(new java.awt.Color(255, 210, 210));
        btnInstruktur.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/teacher.png")); // NOI18N
        btnInstruktur.setText(" Jadwal Kelas");
        btnInstruktur.setBorderPainted(false);
        btnInstruktur.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnInstruktur.setFocusPainted(false);
        btnInstruktur.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnInstruktur.setPreferredSize(new java.awt.Dimension(220, 42));
        btnInstruktur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInstrukturActionPerformed(evt);
            }
        });
        sidebar.add(btnInstruktur, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, -1, -1));

        btnPeserta.setBackground(new java.awt.Color(122, 0, 0));
        btnPeserta.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        btnPeserta.setForeground(new java.awt.Color(255, 210, 210));
        btnPeserta.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/group (2).png")); // NOI18N
        btnPeserta.setText(" Data Peserta Kelas");
        btnPeserta.setBorderPainted(false);
        btnPeserta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPeserta.setFocusPainted(false);
        btnPeserta.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPeserta.setPreferredSize(new java.awt.Dimension(220, 42));
        btnPeserta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesertaActionPerformed(evt);
            }
        });
        sidebar.add(btnPeserta, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 158, -1, -1));

        jSeparator2.setForeground(new java.awt.Color(160, 30, 30));
        jSeparator2.setPreferredSize(new java.awt.Dimension(220, 8));
        sidebar.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 150, 20));

        jLabel4.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 160, 160));
        jLabel4.setText("AKADEMIK");
        sidebar.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, -1));

        btnManajemenSeleksi.setBackground(new java.awt.Color(122, 0, 0));
        btnManajemenSeleksi.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        btnManajemenSeleksi.setForeground(new java.awt.Color(255, 210, 210));
        btnManajemenSeleksi.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/approval.png")); // NOI18N
        btnManajemenSeleksi.setText("Lihat Nilai");
        btnManajemenSeleksi.setBorderPainted(false);
        btnManajemenSeleksi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnManajemenSeleksi.setFocusPainted(false);
        btnManajemenSeleksi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnManajemenSeleksi.setPreferredSize(new java.awt.Dimension(220, 42));
        btnManajemenSeleksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManajemenSeleksiActionPerformed(evt);
            }
        });
        sidebar.add(btnManajemenSeleksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, -1, -1));

        btnInputMateri.setBackground(new java.awt.Color(122, 0, 0));
        btnInputMateri.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        btnInputMateri.setForeground(new java.awt.Color(255, 210, 210));
        btnInputMateri.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/bar-chart (1).png")); // NOI18N
        btnInputMateri.setText("Input Materi");
        btnInputMateri.setBorderPainted(false);
        btnInputMateri.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnInputMateri.setFocusPainted(false);
        btnInputMateri.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnInputMateri.setPreferredSize(new java.awt.Dimension(220, 41));
        btnInputMateri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInputMateriActionPerformed(evt);
            }
        });
        sidebar.add(btnInputMateri, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 333, -1, -1));

        jSeparator4.setForeground(new java.awt.Color(255, 160, 160));
        jSeparator4.setPreferredSize(new java.awt.Dimension(220, 8));
        sidebar.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 620, 120, 20));

        btnLogout.setBackground(new java.awt.Color(100, 0, 0));
        btnLogout.setFont(new java.awt.Font("Inter", 0, 13)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("Logout");
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.setFocusPainted(false);
        btnLogout.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLogout.setPreferredSize(new java.awt.Dimension(220, 42));
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        sidebar.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 630, -1, -1));

        btnLihatLaporan1.setBackground(new java.awt.Color(122, 0, 0));
        btnLihatLaporan1.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        btnLihatLaporan1.setForeground(new java.awt.Color(255, 210, 210));
        btnLihatLaporan1.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/bar-chart (1).png")); // NOI18N
        btnLihatLaporan1.setText("Rekap Peserta");
        btnLihatLaporan1.setBorderPainted(false);
        btnLihatLaporan1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLihatLaporan1.setFocusPainted(false);
        btnLihatLaporan1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLihatLaporan1.setPreferredSize(new java.awt.Dimension(220, 41));
        btnLihatLaporan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatLaporan1ActionPerformed(evt);
            }
        });
        sidebar.add(btnLihatLaporan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 422, -1, -1));

        btnInputSoal.setBackground(new java.awt.Color(122, 0, 0));
        btnInputSoal.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        btnInputSoal.setForeground(new java.awt.Color(255, 210, 210));
        btnInputSoal.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/approval.png")); // NOI18N
        btnInputSoal.setText("Input Soal");
        btnInputSoal.setBorderPainted(false);
        btnInputSoal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnInputSoal.setFocusPainted(false);
        btnInputSoal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnInputSoal.setPreferredSize(new java.awt.Dimension(220, 42));
        btnInputSoal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInputSoalActionPerformed(evt);
            }
        });
        sidebar.add(btnInputSoal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 376, -1, -1));

        panelHeadr.setBackground(new java.awt.Color(255, 255, 255));
        panelHeadr.setPreferredSize(new java.awt.Dimension(980, 64));
        panelHeadr.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblHalamanJudul.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        lblHalamanJudul.setForeground(new java.awt.Color(25, 25, 25));
        lblHalamanJudul.setText("Dashboard");
        lblHalamanJudul.setPreferredSize(new java.awt.Dimension(200, 24));
        panelHeadr.add(lblHalamanJudul, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 10, -1, -1));

        lblHalamanSub.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        lblHalamanSub.setForeground(new java.awt.Color(140, 140, 140));
        lblHalamanSub.setText("Selamat datang, Instruktur");
        lblHalamanSub.setPreferredSize(new java.awt.Dimension(280, 24));
        panelHeadr.add(lblHalamanSub, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 36, -1, -1));

        lblTanggal.setFont(new java.awt.Font("Inter", 0, 11)); // NOI18N
        lblTanggal.setForeground(new java.awt.Color(140, 140, 140));
        lblTanggal.setText("Selasa, 12 Mei 2026");
        lblTanggal.setPreferredSize(new java.awt.Dimension(240, 16));
        panelHeadr.add(lblTanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 10, 180, 20));

        jLabel9.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(40, 40, 40));
        jLabel9.setText("Instruktur");
        jLabel9.setPreferredSize(new java.awt.Dimension(160, 16));
        panelHeadr.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 30, -1, -1));

        btnProfile.setBackground(new java.awt.Color(255, 220, 220));
        btnProfile.setFont(new java.awt.Font("JetBrainsMono NF ExtraBold", 0, 15)); // NOI18N
        btnProfile.setText("I");
        btnProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProfileActionPerformed(evt);
            }
        });
        panelHeadr.add(btnProfile, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 10, 50, 40));

        mainContent.setBackground(new java.awt.Color(245, 245, 245));
        mainContent.setName("mainContent"); // NOI18N
        mainContent.setPreferredSize(new java.awt.Dimension(980, 616));
        mainContent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelKartu1.setBackground(new java.awt.Color(122, 0, 0));
        panelKartu1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelKartu1.setPreferredSize(new java.awt.Dimension(210, 100));
        panelKartu1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(130, 130, 130));
        jLabel14.setText("Instruktur aktif - Bahasa Jepang Dasar");
        jLabel14.setPreferredSize(new java.awt.Dimension(140, 16));
        panelKartu1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, -1));

        lblTotalPeserta.setFont(new java.awt.Font("Inter", 1, 28)); // NOI18N
        lblTotalPeserta.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalPeserta.setText("Dian Priatna Kusuma S.P.d");
        lblTotalPeserta.setPreferredSize(new java.awt.Dimension(100, 36));
        panelKartu1.add(lblTotalPeserta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 380, -1));

        jLabel16.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(240, 170, 170));
        jLabel16.setText("INS001 - KELAS A - LEVEL JLPT N1");
        jLabel16.setPreferredSize(new java.awt.Dimension(140, 16));
        panelKartu1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 210, -1));

        jPanel2.setBackground(new java.awt.Color(240, 210, 210));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(122, 0, 0));
        jLabel7.setText("6");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 4, 54, -1));

        jLabel12.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel12.setText("Siswa ");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        panelKartu1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 15, 90, 70));

        jPanel3.setBackground(new java.awt.Color(240, 210, 210));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(122, 0, 0));
        jLabel6.setText("6");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 4, 54, -1));

        jLabel13.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel13.setText("Jadwal");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        panelKartu1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 15, -1, 70));

        jPanel4.setBackground(new java.awt.Color(240, 210, 210));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(122, 0, 0));
        jLabel8.setText("70");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 54, -1));

        jLabel11.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel11.setText("Rata-rata");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        panelKartu1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 15, 90, 70));

        mainContent.add(panelKartu1, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 24, 920, -1));

        tabelPeserta.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        tabelPeserta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "ID Peserta", "Nama Lengkap", "Level Bahasa", "Program", "Status Seleksi", "Status Bayar", "Status Keberangkatan"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tabelPeserta.setGridColor(new java.awt.Color(235, 235, 235));
        tabelPeserta.setRowHeight(34);
        tabelPeserta.setSelectionBackground(new java.awt.Color(255, 230, 230));
        tabelPeserta.setSelectionForeground(new java.awt.Color(122, 0, 0));
        tabelPeserta.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(tabelPeserta);

        scrollPane1.add(jScrollPane1);

        mainContent.add(scrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 196, -1, -1));

        btnTambahPeserta.setBackground(new java.awt.Color(122, 0, 0));
        btnTambahPeserta.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        btnTambahPeserta.setForeground(new java.awt.Color(255, 255, 255));
        btnTambahPeserta.setText("Tambah Peserta");
        btnTambahPeserta.setBorderPainted(false);
        btnTambahPeserta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTambahPeserta.setFocusPainted(false);
        btnTambahPeserta.setPreferredSize(new java.awt.Dimension(134, 32));
        btnTambahPeserta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahPesertaActionPerformed(evt);
            }
        });
        mainContent.add(btnTambahPeserta, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 148, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1200, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(sidebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelHeadr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(mainContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 680, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(sidebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(panelHeadr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(mainContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboardActionPerformed
        lblHalamanJudul.setText("Dashboard");
        lblHalamanSub.setText("Selamat datang, Admin LPK");

        showPanel(new Views.instruktur.PanelDashboardInstruktur(idInstruktur));
        setActiveButton(btnDashboard);
    }//GEN-LAST:event_btnDashboardActionPerformed

    private void btnInstrukturActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInstrukturActionPerformed
        lblHalamanJudul.setText("Data Jadwal");
        lblHalamanSub.setText("Kelola seluruh data Jadwal");

        showPanel(new Views.instruktur.PanelJadwalKelas(this.idInstruktur));
        setActiveButton(btnInstruktur);
    }//GEN-LAST:event_btnInstrukturActionPerformed

    private void btnPesertaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesertaActionPerformed
        lblHalamanJudul.setText("Data Peserta");
        lblHalamanSub.setText("Lihat data kelas peserta pemagangan");

        showPanel(new Views.instruktur.PanelDataPeserta(idInstruktur));
        setActiveButton(btnPeserta);
    }//GEN-LAST:event_btnPesertaActionPerformed

    private void btnTambahPesertaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahPesertaActionPerformed
        FormTambahPeserta form = new FormTambahPeserta(this, true);
        form.setVisible(true);
    }//GEN-LAST:event_btnTambahPesertaActionPerformed

    private void btnManajemenSeleksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManajemenSeleksiActionPerformed
        lblHalamanJudul.setText("Manajemen Nilai");
        lblHalamanSub.setText("Kelola Nilai Peserta dari Materi");

        // SEBELUMNYA:
        // showPanel(new PanelPenilaianHasilKuis(null, null, idInstrukturAktif));

        // UBAH MENJADI SEPERTI INI:
        showPanel(new PanelDaftarHasilKuis(idInstruktur));

        setActiveButton(btnManajemenSeleksi);
    }//GEN-LAST:event_btnManajemenSeleksiActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        int konfirmasi = javax.swing.JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin keluar dari sistem?", 
            "Konfirmasi Logout", 
            javax.swing.JOptionPane.YES_NO_OPTION, 
            javax.swing.JOptionPane.QUESTION_MESSAGE);

        if (konfirmasi == javax.swing.JOptionPane.YES_OPTION) {

            FormLogin formLogin = new FormLogin();
            formLogin.setVisible(true);

            this.dispose();
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnInputMateriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInputMateriActionPerformed
        lblHalamanJudul.setText("Input Materi");
        lblHalamanSub.setText("Upload dan kelola materi belajar untuk peserta");
 
        showPanel(new Views.instruktur.PanelInputMateri(idInstruktur));
        setActiveButton(btnInputMateri);
    }//GEN-LAST:event_btnInputMateriActionPerformed

    private void btnProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProfileActionPerformed
        if (DataStore.penggunaLogin != null) {
            
            String unameLogin = DataStore.penggunaLogin.getUsername();
            
            FormProfilInstruktur form = new FormProfilInstruktur(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), 
                true, 
                unameLogin 
            );
            form.setVisible(true);
            
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Anda belum login!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnProfileActionPerformed

    private void btnLihatLaporan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatLaporan1ActionPerformed
        lblHalamanJudul.setText("Lihat Laporan");
        lblHalamanSub.setText("Lihat Laporan Peserta");

        showPanel(new PanelLaporanInstruktur(idInstruktur));
        setActiveButton(btnInputMateri);
    }//GEN-LAST:event_btnLihatLaporan1ActionPerformed

    private void btnInputSoalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInputSoalActionPerformed
        lblHalamanJudul.setText("Input Kuis");
        lblHalamanSub.setText("Upload dan kelola Kuis untuk peserta");
 
        showPanel(new Views.instruktur.PanelInputKuis(idInstruktur));
        setActiveButton(btnInputMateri);
    }//GEN-LAST:event_btnInputSoalActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    // ... (kode tampilan look and feel bawaan IDE biarkan saja) ...

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            // UBAH BARIS INI: Tambahkan null, null
            new InstrukturFrame(null, null, "INS001").setVisible(true);
        }
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnInputMateri;
    private javax.swing.JButton btnInputSoal;
    private javax.swing.JButton btnInstruktur;
    private javax.swing.JButton btnLihatLaporan1;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnManajemenSeleksi;
    private javax.swing.JButton btnPeserta;
    private javax.swing.JButton btnProfile;
    private javax.swing.JButton btnTambahPeserta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel lblHalamanJudul;
    private javax.swing.JLabel lblHalamanSub;
    private javax.swing.JLabel lblTanggal;
    private javax.swing.JLabel lblTotalPeserta;
    private javax.swing.JPanel mainContent;
    private javax.swing.JPanel panelHeadr;
    private javax.swing.JPanel panelKartu1;
    private java.awt.ScrollPane scrollPane1;
    private javax.swing.JPanel sidebar;
    private javax.swing.JTable tabelPeserta;
    // End of variables declaration//GEN-END:variables
}
