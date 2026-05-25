/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views;

import DataStore.DataStore;
import Model.Peserta;
import Model.Program;
import Model.Seleksi;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.sql.ResultSet;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 *
 * @author arelssi
 */
public class FormDetailPeserta extends javax.swing.JFrame {

    /**
     * Creates new form FromEditPeserta
     */
    public FormDetailPeserta(java.awt.Frame parent, boolean modal, String idPeserta) {
        initComponents();
        setSize(790, 517);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Form Detail Peserta");
        panelRiwayatSeleksi.setLayout(new BoxLayout(panelRiwayatSeleksi, BoxLayout.Y_AXIS));
        
        loadDataIdentitas(idPeserta);
        loadRiwayatSeleksi(idPeserta);
    }
    
    private void loadDataIdentitas(String idPeserta) {
        Peserta pesertaAktif = null;

        // 1. Cari objek Peserta berdasarkan ID
        for (Peserta p : DataStore.daftarPeserta) {
            if (p.getIdPeserta().equals(idPeserta)) {
                pesertaAktif = p;
                break;
            }
        }

        if (pesertaAktif != null) {
            // Set Label Header
            lblNamaHeader.setText(pesertaAktif.getNamaLengkap());
            lblIdHeader.setText(pesertaAktif.getIdPeserta() + " · Terdaftar sejak 01 Oktober 2025"); 

            // Set Label Data Pribadi
            lblNik.setText(pesertaAktif.getNik());
            lblTglLahir.setText(pesertaAktif.getTanggalLahir());
            lblJenisKelamin.setText(pesertaAktif.getJenisKelamin());
            lblAgama.setText(pesertaAktif.getAgama());
            lblNoHp.setText(pesertaAktif.getNoHp());
            lblLevelBahasa.setText(pesertaAktif.getLevelBahasa());
            lblAlamat.setText(pesertaAktif.getAlamat());
            
            // Set Status & Paspor (Asumsi Getter sudah kamu tambahkan di model)
            // lblNoPaspor.setText(pesertaAktif.getNoPaspor() == null ? "Belum diinput" : pesertaAktif.getNoPaspor());
            
            // Menampilkan data Instruktur (OOP Association)
            lblInstruktur.setText(pesertaAktif.getInstrukturDamping().getNamaLengkap());

            // 2. Cari objek Program untuk mengambil data Biaya
            for (Program prg : DataStore.daftarProgram) {
                if (prg.getIdProgram().equals(pesertaAktif.getProgram())) {
                    lblProgram.setText(prg.getIdProgram() + " - " + prg.getNamaProgram());
                    lblBiaya.setText("Rp " + String.format("%,.0f", prg.getBiaya()).replace(',', '.'));
                    break;
                }
            }
            
            // Set text pada badge/label status di bawah
            lblStatusSeleksi.setText(pesertaAktif.getStatusSeleksi());
            lblStatusBayar.setText(pesertaAktif.getStatusPembayaran());
            lblStatusDokumen.setText(pesertaAktif.getStatusKeberangkatan());
        }
    }
    
    private void loadRiwayatSeleksi(String idPeserta) {
        panelRiwayatSeleksi.removeAll();
        

        boolean adaRiwayat = false;

        // Loop ke database memori DataStore
        for (Seleksi sel : DataStore.daftarSeleksi) {
            // Jika objek peserta di dalam seleksi ini sama dengan idPeserta yang sedang dibuka
            if (sel.getPeserta().getIdPeserta().equals(idPeserta)) {
                adaRiwayat = true;
                
                // --- MEMBUAT CARD PANEL SECARA DINAMIS ---
                JPanel card = new JPanel();
                card.setLayout(new java.awt.BorderLayout());
                card.setBackground(Color.WHITE);
                card.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)), // Garis bawah
                    javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10) // Padding dalam
                ));
                card.setMaximumSize(new Dimension(400, 80));

                // Komponen Kiri: Judul dan Tanggal
                JPanel panelKiri = new JPanel(new java.awt.GridLayout(2, 1));
                panelKiri.setBackground(Color.WHITE);
                javax.swing.JLabel lblJudul = new javax.swing.JLabel(sel.getJenisSeleksi());
                lblJudul.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
                
                // Asumsi kamu menambahkan atribut tanggalSeleksi di kelas Seleksi
                javax.swing.JLabel lblTanggal = new javax.swing.JLabel(sel.getTanggalSeleksi());
                lblTanggal.setFont(new java.awt.Font("Inter", java.awt.Font.PLAIN, 10));
                lblTanggal.setForeground(Color.GRAY);
                
                panelKiri.add(lblJudul);
                panelKiri.add(lblTanggal);

                // Komponen Kanan: Nilai dan Status
                JPanel panelKanan = new JPanel(new java.awt.GridLayout(2, 1));
                panelKanan.setBackground(Color.WHITE);
                
                // Asumsi kamu menambahkan atribut nilai (int) di kelas Seleksi
                javax.swing.JLabel lblNilai = new javax.swing.JLabel(String.valueOf(sel.getNilai()), javax.swing.SwingConstants.RIGHT);
                lblNilai.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 14));
                lblNilai.setForeground(new Color(150, 0, 0)); // Merah maroon

                javax.swing.JLabel lblStatus = new javax.swing.JLabel(sel.getStatusHasil(), javax.swing.SwingConstants.RIGHT);
                lblStatus.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
                
                // Ubah warna text status sesuai hasil
                if (sel.getStatusHasil().equalsIgnoreCase("Lulus")) {
                    lblStatus.setForeground(new Color(0, 150, 0)); // Hijau
                } else {
                    lblStatus.setForeground(Color.RED); // Merah
                }

                panelKanan.add(lblNilai);
                panelKanan.add(lblStatus);

                // Gabungkan komponen ke dalam Card
                card.add(panelKiri, java.awt.BorderLayout.CENTER);
                card.add(panelKanan, java.awt.BorderLayout.EAST);

                // Masukkan Card ke dalam Panel utama
                panelRiwayatSeleksi.add(card);
            }
        }

        // Jika kosong, tampilkan pesan
        if (!adaRiwayat) {
            javax.swing.JLabel lblKosong = new javax.swing.JLabel("Belum ada riwayat seleksi.");
            lblKosong.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panelRiwayatSeleksi.add(lblKosong);
        }

        // Refresh panel agar perubahan terlihat
        panelRiwayatSeleksi.revalidate();
        panelRiwayatSeleksi.repaint();
    }

    private FormDetailPeserta() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelHeader = new javax.swing.JPanel();
        lblNamaHeader = new javax.swing.JLabel();
        lblIdHeader = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        panelBody = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblTglLahir = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        lblNik = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblJenisKelamin = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblAgama = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblNoHp = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lblLevelBahasa = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        lblAlamat = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        lblProgram = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        lblInstruktur = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        lblBiaya = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblStatusSeleksi = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblStatusBayar = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblStatusDokumen = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelRiwayatSeleksi = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        lblNilai = new javax.swing.JLabel();
        lblTanggal = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        btnSimpan = new javax.swing.JButton();
        btnTutup = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelHeader.setBackground(new java.awt.Color(122, 0, 0));
        panelHeader.setPreferredSize(new java.awt.Dimension(520, 68));
        panelHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblNamaHeader.setFont(new java.awt.Font("Inter", 1, 15)); // NOI18N
        lblNamaHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblNamaHeader.setText("Muhamad Farel Fauzan");
        lblNamaHeader.setPreferredSize(new java.awt.Dimension(280, 22));
        panelHeader.add(lblNamaHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 14, -1, -1));

        lblIdHeader.setFont(new java.awt.Font("Inter", 0, 11)); // NOI18N
        lblIdHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblIdHeader.setText("PST003  ·  Terdaftar sejak 01 Oktober 2025");
        lblIdHeader.setPreferredSize(new java.awt.Dimension(300, 16));
        panelHeader.add(lblIdHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 38, -1, -1));

        jButton1.setBackground(new java.awt.Color(160, 20, 20));
        jButton1.setFont(new java.awt.Font("Inter", 1, 13)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("X");
        jButton1.setBorderPainted(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setFocusPainted(false);
        jButton1.setPreferredSize(new java.awt.Dimension(30, 30));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        panelHeader.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 20, -1, -1));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        panelHeader.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 30, 30));

        getContentPane().add(panelHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 790, -1));

        panelBody.setBackground(new java.awt.Color(248, 248, 248));
        panelBody.setPreferredSize(new java.awt.Dimension(520, 364));
        panelBody.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(122, 0, 0));
        jLabel3.setText("DATA IDENTITAS");
        jLabel3.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 16, -1, -1));

        jSeparator1.setPreferredSize(new java.awt.Dimension(480, 8));
        panelBody.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 32, 420, -1));

        jLabel4.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(90, 90, 90));
        jLabel4.setText("NIK");
        jLabel4.setPreferredSize(new java.awt.Dimension(140, 14));
        panelBody.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 44, -1, -1));

        jLabel5.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(90, 90, 90));
        jLabel5.setText("Tanggal Lahir");
        jLabel5.setPreferredSize(new java.awt.Dimension(140, 14));
        panelBody.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 44, -1, -1));

        lblTglLahir.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblTglLahir.setForeground(new java.awt.Color(10, 10, 10));
        lblTglLahir.setText("10 Januari 2007");
        lblTglLahir.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblTglLahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 60, -1, -1));

        jSeparator2.setPreferredSize(new java.awt.Dimension(480, 8));
        panelBody.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 225, 420, -1));

        lblNik.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblNik.setForeground(new java.awt.Color(10, 10, 10));
        lblNik.setText("2250800020");
        lblNik.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblNik, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        jLabel12.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(90, 90, 90));
        jLabel12.setText("Jenis Kelamin");
        jLabel12.setPreferredSize(new java.awt.Dimension(140, 14));
        panelBody.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        lblJenisKelamin.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblJenisKelamin.setForeground(new java.awt.Color(10, 10, 10));
        lblJenisKelamin.setText("Laki-laki");
        lblJenisKelamin.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblJenisKelamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 95, -1, -1));

        jLabel14.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(90, 90, 90));
        jLabel14.setText("Agama");
        jLabel14.setPreferredSize(new java.awt.Dimension(140, 14));
        panelBody.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 80, -1, -1));

        lblAgama.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblAgama.setForeground(new java.awt.Color(10, 10, 10));
        lblAgama.setText("Islam");
        lblAgama.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblAgama, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 95, -1, -1));

        jLabel16.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(90, 90, 90));
        jLabel16.setText("No. HP");
        jLabel16.setPreferredSize(new java.awt.Dimension(140, 14));
        panelBody.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        lblNoHp.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblNoHp.setForeground(new java.awt.Color(10, 10, 10));
        lblNoHp.setText("2250800020");
        lblNoHp.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblNoHp, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 135, -1, -1));

        jLabel18.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(90, 90, 90));
        jLabel18.setText("Level Bahasa");
        jLabel18.setPreferredSize(new java.awt.Dimension(140, 14));
        panelBody.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 120, -1, -1));

        lblLevelBahasa.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblLevelBahasa.setForeground(new java.awt.Color(122, 0, 0));
        lblLevelBahasa.setText("N3");
        lblLevelBahasa.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblLevelBahasa, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 135, -1, -1));

        jLabel20.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(90, 90, 90));
        jLabel20.setText("Alamat");
        jLabel20.setPreferredSize(new java.awt.Dimension(140, 14));
        panelBody.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, -1, -1));

        lblAlamat.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblAlamat.setForeground(new java.awt.Color(10, 10, 10));
        lblAlamat.setText("Kuningan, Windusengkahan RT 11 RW 04");
        lblAlamat.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblAlamat, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 175, 470, -1));

        jLabel22.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(90, 90, 90));
        jLabel22.setText("Program");
        jLabel22.setPreferredSize(new java.awt.Dimension(140, 14));
        panelBody.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, -1));

        lblProgram.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblProgram.setForeground(new java.awt.Color(10, 10, 10));
        lblProgram.setText("PRG002 - Industri Makanan");
        lblProgram.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblProgram, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, -1, -1));

        jLabel24.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(90, 90, 90));
        jLabel24.setText("Instruktur");
        jLabel24.setPreferredSize(new java.awt.Dimension(140, 14));
        panelBody.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 230, -1, -1));

        lblInstruktur.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblInstruktur.setForeground(new java.awt.Color(10, 10, 10));
        lblInstruktur.setText("Ayumi Diana");
        lblInstruktur.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblInstruktur, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 250, -1, -1));

        jLabel26.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(90, 90, 90));
        jLabel26.setText("Biaya Program");
        jLabel26.setPreferredSize(new java.awt.Dimension(140, 14));
        panelBody.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, -1));

        lblBiaya.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblBiaya.setForeground(new java.awt.Color(10, 10, 10));
        lblBiaya.setText("Rp. 38.500.000");
        lblBiaya.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblBiaya, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        jLabel28.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(90, 90, 90));
        jLabel28.setText("No. Paspor");
        jLabel28.setPreferredSize(new java.awt.Dimension(140, 14));
        panelBody.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 270, -1, -1));

        jLabel29.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(10, 10, 10));
        jLabel29.setText("Belum diinput");
        jLabel29.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 290, -1, -1));

        jPanel1.setBackground(new java.awt.Color(210, 255, 210));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        jPanel1.setPreferredSize(new java.awt.Dimension(104, 22));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblStatusSeleksi.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        lblStatusSeleksi.setForeground(new java.awt.Color(0, 122, 0));
        lblStatusSeleksi.setText("Lulus");
        jPanel1.add(lblStatusSeleksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 3, -1, -1));

        panelBody.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, -1, 20));

        jPanel2.setBackground(new java.awt.Color(210, 210, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        jPanel2.setPreferredSize(new java.awt.Dimension(104, 22));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblStatusBayar.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        lblStatusBayar.setForeground(new java.awt.Color(0, 0, 122));
        lblStatusBayar.setText("Lunas");
        jPanel2.add(lblStatusBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 3, -1, -1));

        panelBody.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 320, -1, -1));

        jPanel3.setBackground(new java.awt.Color(255, 230, 210));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        jPanel3.setPreferredSize(new java.awt.Dimension(104, 22));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblStatusDokumen.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        lblStatusDokumen.setForeground(new java.awt.Color(200, 100, 0));
        lblStatusDokumen.setText("Menunggu dokumen");
        jPanel3.add(lblStatusDokumen, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 3, -1, -1));

        panelBody.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 320, 140, -1));

        jLabel30.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(122, 0, 0));
        jLabel30.setText("DATA PROGRAM & STATUS");
        jLabel30.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, -1));

        jLabel7.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(122, 0, 0));
        jLabel7.setText("RIWAYAT SELEKSI");
        jLabel7.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, -1, -1));

        panelRiwayatSeleksi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelRiwayatSeleksi.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel31.setFont(new java.awt.Font("JetBrainsMono NF", 1, 10)); // NOI18N
        jLabel31.setText("Seleksi Wawancara");
        jPanel6.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        lblNilai.setFont(new java.awt.Font("Inter", 1, 15)); // NOI18N
        lblNilai.setForeground(new java.awt.Color(122, 0, 0));
        lblNilai.setText("55");
        jPanel6.add(lblNilai, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, -1, -1));

        lblTanggal.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        lblTanggal.setText("12 Oktober 2025");
        jPanel6.add(lblTanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        jLabel34.setBackground(new java.awt.Color(0, 122, 0));
        jLabel34.setFont(new java.awt.Font("JetBrainsMono NF", 1, 15)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(220, 10, 10));
        jLabel34.setText("Mengulang");
        jPanel6.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, -1, -1));

        panelRiwayatSeleksi.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 260, 80));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel35.setFont(new java.awt.Font("JetBrainsMono NF", 1, 10)); // NOI18N
        jLabel35.setText("Seleksi Bahasa Jepang");
        jPanel7.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel36.setFont(new java.awt.Font("Inter", 1, 15)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(122, 0, 0));
        jLabel36.setText("75");
        jPanel7.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, -1, -1));

        jLabel37.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        jLabel37.setText("06 Oktober 2025");
        jPanel7.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        jLabel38.setBackground(new java.awt.Color(0, 122, 0));
        jLabel38.setFont(new java.awt.Font("JetBrainsMono NF", 1, 15)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(0, 122, 0));
        jLabel38.setText("Lulus");
        jPanel7.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, -1, -1));

        panelRiwayatSeleksi.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 260, 80));

        jScrollPane1.setViewportView(panelRiwayatSeleksi);

        panelBody.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 290, 310));

        getContentPane().add(panelBody, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 68, 790, -1));

        btnSimpan.setBackground(new java.awt.Color(122, 0, 0));
        btnSimpan.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setText("Edit");
        btnSimpan.setBorderPainted(false);
        btnSimpan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpan.setFocusPainted(false);
        btnSimpan.setPreferredSize(new java.awt.Dimension(116, 34));
        getContentPane().add(btnSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 450, -1, -1));

        btnTutup.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        btnTutup.setForeground(new java.awt.Color(80, 80, 80));
        btnTutup.setText("Tutup");
        btnTutup.setToolTipText("Batal");
        btnTutup.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTutup.setFocusPainted(false);
        btnTutup.setPreferredSize(new java.awt.Dimension(100, 34));
        btnTutup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTutupActionPerformed(evt);
            }
        });
        getContentPane().add(btnTutup, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 450, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnTutupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTutupActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnTutupActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormDetailPeserta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormDetailPeserta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormDetailPeserta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormDetailPeserta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormDetailPeserta().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTutup;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblAgama;
    private javax.swing.JLabel lblAlamat;
    private javax.swing.JLabel lblBiaya;
    private javax.swing.JLabel lblIdHeader;
    private javax.swing.JLabel lblInstruktur;
    private javax.swing.JLabel lblJenisKelamin;
    private javax.swing.JLabel lblLevelBahasa;
    private javax.swing.JLabel lblNamaHeader;
    private javax.swing.JLabel lblNik;
    private javax.swing.JLabel lblNilai;
    private javax.swing.JLabel lblNoHp;
    private javax.swing.JLabel lblProgram;
    private javax.swing.JLabel lblStatusBayar;
    private javax.swing.JLabel lblStatusDokumen;
    private javax.swing.JLabel lblStatusSeleksi;
    private javax.swing.JLabel lblTanggal;
    private javax.swing.JLabel lblTglLahir;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelRiwayatSeleksi;
    // End of variables declaration//GEN-END:variables
}
