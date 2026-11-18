/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views.admin;

import DataStore.DataStore;
import Model.Instruktur;
import Model.Jadwal;
import Model.Peserta;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Form Detail Instruktur
 * @author arelssi
 */
public class FormDetailInstruktur extends javax.swing.JFrame {

    private String idInstrukturAktif;

    public FormDetailInstruktur(java.awt.Frame parent, boolean modal, String idInstruktur) {
        initComponents();
        this.idInstrukturAktif = idInstruktur;
        setSize(790, 517);
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
        setTitle("Form Detail Instruktur");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Pastikan BoxLayout diterapkan setelah initComponents() agar tidak tertimpa AbsoluteLayout
        panelPesertaBimbingan.setLayout(new BoxLayout(panelPesertaBimbingan, BoxLayout.Y_AXIS));

        // Sinkronkan cache DataStore dengan data terbaru dari MySQL
        DataStore.muatUlang();

        boolean berhasil = loadDataIdentitas(idInstruktur);
        if (!berhasil) {
            JOptionPane.showMessageDialog(
                    this,
                    "Data instruktur dengan ID \"" + idInstruktur + "\" tidak ditemukan di database.",
                    "Data Tidak Ditemukan",
                    JOptionPane.WARNING_MESSAGE
            );
            btnSimpan.setEnabled(false);
            btnTambahJadwal.setEnabled(false);
        }

        loadPesertaBimbingan(idInstruktur);
    }

    private boolean loadDataIdentitas(String idInstruktur) {
        Instruktur insAktif = DataStore.cariInstruktur(idInstruktur);

        if (insAktif == null) {
            return false;
        }

        lblNamaHeader.setText(insAktif.getNamaLengkap());
        lblIdHeader.setText(insAktif.getIdInstruktur() + " · " + insAktif.getSpesialisasi() + " · Level " + insAktif.getLevelJlpt());

        lblIdInstruktur.setText(insAktif.getIdInstruktur());
        lblUsername.setText(insAktif.getUsername());
        lblNoTelp.setText(insAktif.getNoTelp());
        lblRole.setText(insAktif.getRole());
        lblHakAkses.setText("<html><div style='width:460px;'>" + insAktif.getMenuOtoritas() + "</div></html>");

        lblSpesialisasi.setText(insAktif.getSpesialisasi());
        lblLevelJlpt.setText(insAktif.getLevelJlpt());
        lblKelasDiampu.setText(insAktif.getKelasDiampu());

        int jumlahJadwal = 0;
        for (Jadwal jdw : DataStore.daftarJadwal) {
            if (jdw.getIdInstruktur() != null && jdw.getIdInstruktur().equals(idInstruktur)) {
                jumlahJadwal++;
            }
        }
        lblJumlahJadwal.setText(jumlahJadwal + " Sesi Terjadwal");

        int jumlahPeserta = 0;
        for (Peserta p : DataStore.daftarPeserta) {
            if (p.getInstrukturDamping() != null
                    && idInstruktur.equals(p.getInstrukturDamping().getIdInstruktur())) {
                jumlahPeserta++;
            }
        }
        lblJumlahPeserta.setText(jumlahPeserta + " Peserta");

        if (jumlahJadwal > 0) {
            lblStatusAktif.setText("Aktif Mengajar");
            lblStatusAktif.setForeground(new Color(0, 122, 0));
            panelBadgeStatus.setBackground(new Color(210, 255, 210));
        } else {
            lblStatusAktif.setText("Belum Ada Jadwal");
            lblStatusAktif.setForeground(new Color(160, 120, 0));
            panelBadgeStatus.setBackground(new Color(255, 245, 210));
        }

        return true;
    }

    private void loadPesertaBimbingan(String idInstruktur) {
        panelPesertaBimbingan.removeAll();

        boolean adaPeserta = false;

        for (Peserta p : DataStore.daftarPeserta) {
            if (p.getInstrukturDamping() != null
                    && idInstruktur.equals(p.getInstrukturDamping().getIdInstruktur())) {
                adaPeserta = true;

                JPanel card = new JPanel();
                card.setLayout(new java.awt.BorderLayout());
                card.setBackground(Color.WHITE);
                card.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                        javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

                JPanel panelKiri = new JPanel(new java.awt.GridLayout(2, 1));
                panelKiri.setBackground(Color.WHITE);

                javax.swing.JLabel lblNama = new javax.swing.JLabel(p.getNamaLengkap());
                lblNama.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));

                String kelasTampil = (p.getKelas() != null && !p.getKelas().trim().isEmpty())
                        ? p.getKelas()
                        : "Belum Ditentukan";
                javax.swing.JLabel lblIdKelas = new javax.swing.JLabel(p.getIdPeserta() + " · " + kelasTampil);
                lblIdKelas.setFont(new java.awt.Font("Inter", java.awt.Font.PLAIN, 10));
                lblIdKelas.setForeground(Color.GRAY);

                panelKiri.add(lblNama);
                panelKiri.add(lblIdKelas);

                JPanel panelKanan = new JPanel(new java.awt.GridLayout(2, 1));
                panelKanan.setBackground(Color.WHITE);

                javax.swing.JLabel lblLevel = new javax.swing.JLabel(p.getLevelBahasa(), javax.swing.SwingConstants.RIGHT);
                lblLevel.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 14));
                lblLevel.setForeground(new Color(150, 0, 0));

                javax.swing.JLabel lblStatus = new javax.swing.JLabel(p.getStatusSeleksi(), javax.swing.SwingConstants.RIGHT);
                lblStatus.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));

                if ("Lulus".equalsIgnoreCase(p.getStatusSeleksi())) {
                    lblStatus.setForeground(new Color(0, 150, 0));
                } else if (p.getStatusSeleksi() != null && p.getStatusSeleksi().toLowerCase().contains("gugur")) {
                    lblStatus.setForeground(Color.RED);
                } else {
                    lblStatus.setForeground(new Color(200, 100, 0));
                }

                panelKanan.add(lblLevel);
                panelKanan.add(lblStatus);

                card.add(panelKiri, java.awt.BorderLayout.CENTER);
                card.add(panelKanan, java.awt.BorderLayout.EAST);

                panelPesertaBimbingan.add(card);
            }
        }

        if (!adaPeserta) {
            javax.swing.JLabel lblKosong = new javax.swing.JLabel("Belum ada peserta bimbingan.");
            lblKosong.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panelPesertaBimbingan.add(lblKosong);
        }

        panelPesertaBimbingan.revalidate();
        panelPesertaBimbingan.repaint();
    }

    private void tampilkanDialogTambahJadwal() {
        javax.swing.JDialog dialog = new javax.swing.JDialog(this, "Tambah Jadwal Mengajar", true);
        dialog.setSize(350, 320);
        dialog.setLocationRelativeTo(this);

        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.GridLayout(6, 2, 10, 10));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        javax.swing.JComboBox<String> cbHari = new javax.swing.JComboBox<>(new String[]{"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"});
        javax.swing.JTextField txtJam = new javax.swing.JTextField("08:00");
        javax.swing.JTextField txtMateri = new javax.swing.JTextField();
        javax.swing.JComboBox<String> cbKelas = new javax.swing.JComboBox<>(new String[]{"Kelas A", "Kelas B", "Mandiri"});
        javax.swing.JTextField txtDurasi = new javax.swing.JTextField("120");

        panel.add(new javax.swing.JLabel("Hari:")); panel.add(cbHari);
        panel.add(new javax.swing.JLabel("Jam (HH:mm):")); panel.add(txtJam);
        panel.add(new javax.swing.JLabel("Materi:")); panel.add(txtMateri);
        panel.add(new javax.swing.JLabel("Kelas:")); panel.add(cbKelas);
        panel.add(new javax.swing.JLabel("Durasi (menit):")); panel.add(txtDurasi);

        javax.swing.JButton btnSimpanJadwal = new javax.swing.JButton("Simpan");
        btnSimpanJadwal.setBackground(new java.awt.Color(122, 0, 0));
        btnSimpanJadwal.setForeground(java.awt.Color.WHITE);
        panel.add(new javax.swing.JLabel());
        panel.add(btnSimpanJadwal);

        dialog.add(panel);

        btnSimpanJadwal.addActionListener(e -> {
            String materi = txtMateri.getText() == null ? "" : txtMateri.getText().trim();
            if (materi.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Materi tidak boleh kosong!", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!txtJam.getText().matches("^([01]\\d|2[0-3]):[0-5]\\d$")) {
                JOptionPane.showMessageDialog(dialog, "Format jam harus HH:mm, contoh: 08:00", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int durasi;
            try {
                durasi = Integer.parseInt(txtDurasi.getText().trim());
                if (durasi <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Durasi harus lebih besar dari 0 menit.", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(dialog, "Durasi harus berupa angka (menit).", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String idJadwal = "JDW" + System.currentTimeMillis();
                Jadwal jBaru = new Jadwal(
                        idJadwal,
                        this.idInstrukturAktif,
                        cbHari.getSelectedItem().toString(),
                        txtJam.getText(),
                        materi,
                        cbKelas.getSelectedItem().toString(),
                        durasi,
                        "Belum Dimulai"
                );

                DataStore.daftarJadwal.add(jBaru);
                loadDataIdentitas(this.idInstrukturAktif);

                JOptionPane.showMessageDialog(dialog, "Jadwal berhasil ditambahkan!");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Gagal menyimpan jadwal: " + ex.getMessage(),
                        "Terjadi Kesalahan",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        dialog.setVisible(true);
    }

    private FormDetailInstruktur() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelHeader = new javax.swing.JPanel();
        lblNamaHeader = new javax.swing.JLabel();
        lblIdHeader = new javax.swing.JLabel();
        btnTutupHeader = new javax.swing.JButton();
        panelAvatar = new javax.swing.JPanel();
        panelBody = new javax.swing.JPanel();
        lblJudulIdentitas = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblLabelIdInstruktur = new javax.swing.JLabel();
        lblLabelUsername = new javax.swing.JLabel();
        lblIdInstruktur = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        lblLabelNoTelp = new javax.swing.JLabel();
        lblLabelRole = new javax.swing.JLabel();
        lblNoTelp = new javax.swing.JLabel();
        lblRole = new javax.swing.JLabel();
        lblLabelHakAkses = new javax.swing.JLabel();
        lblHakAkses = new javax.swing.JLabel();
        lblJudulKeahlian = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        lblLabelSpesialisasi = new javax.swing.JLabel();
        lblLabelLevelJlpt = new javax.swing.JLabel();
        lblSpesialisasi = new javax.swing.JLabel();
        lblLevelJlpt = new javax.swing.JLabel();
        lblLabelKelasDiampu = new javax.swing.JLabel();
        lblLabelJumlahJadwal = new javax.swing.JLabel();
        lblKelasDiampu = new javax.swing.JLabel();
        lblJumlahJadwal = new javax.swing.JLabel();
        panelBadgePeserta = new javax.swing.JPanel();
        lblJumlahPeserta = new javax.swing.JLabel();
        panelBadgeStatus = new javax.swing.JPanel();
        lblStatusAktif = new javax.swing.JLabel();
        lblJudulPeserta = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelPesertaBimbingan = new javax.swing.JPanel();
        btnTambahJadwal = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        btnTutup = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelHeader.setBackground(new java.awt.Color(122, 0, 0));
        panelHeader.setPreferredSize(new java.awt.Dimension(790, 68));
        panelHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblNamaHeader.setFont(new java.awt.Font("Inter", 1, 15)); // NOI18N
        lblNamaHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblNamaHeader.setText("Dian Priatna Kusuma, S.PD.");
        lblNamaHeader.setPreferredSize(new java.awt.Dimension(400, 22));
        panelHeader.add(lblNamaHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 14, -1, -1));

        lblIdHeader.setFont(new java.awt.Font("Inter", 0, 11)); // NOI18N
        lblIdHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblIdHeader.setText("INS001  ·  Kaiwa  ·  Level N2");
        lblIdHeader.setPreferredSize(new java.awt.Dimension(400, 16));
        panelHeader.add(lblIdHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 38, -1, -1));

        btnTutupHeader.setBackground(new java.awt.Color(160, 20, 20));
        btnTutupHeader.setFont(new java.awt.Font("Inter", 1, 13)); // NOI18N
        btnTutupHeader.setForeground(new java.awt.Color(255, 255, 255));
        btnTutupHeader.setText("X");
        btnTutupHeader.setBorderPainted(false);
        btnTutupHeader.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTutupHeader.setFocusPainted(false);
        btnTutupHeader.setPreferredSize(new java.awt.Dimension(30, 30));
        btnTutupHeader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTutupHeaderActionPerformed(evt);
            }
        });
        panelHeader.add(btnTutupHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 20, -1, -1));

        panelAvatar.setBackground(new java.awt.Color(160, 20, 20));
        panelAvatar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 60, 60)));

        javax.swing.GroupLayout panelAvatarLayout = new javax.swing.GroupLayout(panelAvatar);
        panelAvatar.setLayout(panelAvatarLayout);
        panelAvatarLayout.setHorizontalGroup(
            panelAvatarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panelAvatarLayout.setVerticalGroup(
            panelAvatarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        panelHeader.add(panelAvatar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 30, 30));

        getContentPane().add(panelHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 790, -1));

        panelBody.setBackground(new java.awt.Color(248, 248, 248));
        panelBody.setPreferredSize(new java.awt.Dimension(790, 364));
        panelBody.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblJudulIdentitas.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        lblJudulIdentitas.setForeground(new java.awt.Color(122, 0, 0));
        lblJudulIdentitas.setText("DATA IDENTITAS");
        lblJudulIdentitas.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblJudulIdentitas, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 16, -1, -1));

        jSeparator1.setPreferredSize(new java.awt.Dimension(420, 8));
        panelBody.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 32, 420, -1));

        lblLabelIdInstruktur.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        lblLabelIdInstruktur.setForeground(new java.awt.Color(90, 90, 90));
        lblLabelIdInstruktur.setText("ID Instruktur");
        lblLabelIdInstruktur.setPreferredSize(new java.awt.Dimension(140, 14));
        panelBody.add(lblLabelIdInstruktur, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 44, -1, -1));

        lblLabelUsername.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        lblLabelUsername.setForeground(new java.awt.Color(90, 90, 90));
        lblLabelUsername.setText("Username");
        lblLabelUsername.setPreferredSize(new java.awt.Dimension(140, 14));
        panelBody.add(lblLabelUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 44, -1, -1));

        lblIdInstruktur.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblIdInstruktur.setForeground(new java.awt.Color(10, 10, 10));
        lblIdInstruktur.setText("INS001");
        lblIdInstruktur.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblIdInstruktur, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        lblUsername.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblUsername.setForeground(new java.awt.Color(10, 10, 10));
        lblUsername.setText("dian_p");
        lblUsername.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 60, -1, -1));

        lblLabelNoTelp.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        lblLabelNoTelp.setForeground(new java.awt.Color(90, 90, 90));
        lblLabelNoTelp.setText("No. Telp");
        lblLabelNoTelp.setPreferredSize(new java.awt.Dimension(140, 14));
        panelBody.add(lblLabelNoTelp, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        lblLabelRole.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        lblLabelRole.setForeground(new java.awt.Color(90, 90, 90));
        lblLabelRole.setText("Role");
        lblLabelRole.setPreferredSize(new java.awt.Dimension(140, 14));
        panelBody.add(lblLabelRole, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 80, -1, -1));

        lblNoTelp.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblNoTelp.setForeground(new java.awt.Color(10, 10, 10));
        lblNoTelp.setText("08123...");
        lblNoTelp.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblNoTelp, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 95, -1, -1));

        lblRole.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblRole.setForeground(new java.awt.Color(122, 0, 0));
        lblRole.setText("INSTRUKTUR");
        lblRole.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblRole, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 95, -1, -1));

        lblLabelHakAkses.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        lblLabelHakAkses.setForeground(new java.awt.Color(90, 90, 90));
        lblLabelHakAkses.setText("Hak Akses Sistem");
        lblLabelHakAkses.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblLabelHakAkses, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        lblHakAkses.setFont(new java.awt.Font("Inter", 0, 11)); // NOI18N
        lblHakAkses.setForeground(new java.awt.Color(10, 10, 10));
        lblHakAkses.setText("Akses Terbatas: Input Nilai dan Evaluasi Peserta Magang.");
        lblHakAkses.setPreferredSize(new java.awt.Dimension(440, 32));
        panelBody.add(lblHakAkses, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 135, -1, -1));

        lblJudulKeahlian.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        lblJudulKeahlian.setForeground(new java.awt.Color(122, 0, 0));
        lblJudulKeahlian.setText("DATA KEAHLIAN & KELAS");
        lblJudulKeahlian.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblJudulKeahlian, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, -1));

        jSeparator2.setPreferredSize(new java.awt.Dimension(420, 8));
        panelBody.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 225, 420, -1));

        lblLabelSpesialisasi.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        lblLabelSpesialisasi.setForeground(new java.awt.Color(90, 90, 90));
        lblLabelSpesialisasi.setText("Spesialisasi");
        lblLabelSpesialisasi.setPreferredSize(new java.awt.Dimension(140, 14));
        panelBody.add(lblLabelSpesialisasi, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, -1));

        lblLabelLevelJlpt.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        lblLabelLevelJlpt.setForeground(new java.awt.Color(90, 90, 90));
        lblLabelLevelJlpt.setText("Level JLPT");
        lblLabelLevelJlpt.setPreferredSize(new java.awt.Dimension(140, 14));
        panelBody.add(lblLabelLevelJlpt, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 230, -1, -1));

        lblSpesialisasi.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblSpesialisasi.setForeground(new java.awt.Color(10, 10, 10));
        lblSpesialisasi.setText("Kaiwa");
        lblSpesialisasi.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblSpesialisasi, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, -1, -1));

        lblLevelJlpt.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblLevelJlpt.setForeground(new java.awt.Color(122, 0, 0));
        lblLevelJlpt.setText("N2");
        lblLevelJlpt.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblLevelJlpt, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 250, -1, -1));

        lblLabelKelasDiampu.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        lblLabelKelasDiampu.setForeground(new java.awt.Color(90, 90, 90));
        lblLabelKelasDiampu.setText("Kelas Diampu");
        lblLabelKelasDiampu.setPreferredSize(new java.awt.Dimension(140, 14));
        panelBody.add(lblLabelKelasDiampu, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, -1));

        lblLabelJumlahJadwal.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        lblLabelJumlahJadwal.setForeground(new java.awt.Color(90, 90, 90));
        lblLabelJumlahJadwal.setText("Jadwal Mengajar");
        lblLabelJumlahJadwal.setPreferredSize(new java.awt.Dimension(140, 14));
        panelBody.add(lblLabelJumlahJadwal, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 270, -1, -1));

        lblKelasDiampu.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblKelasDiampu.setForeground(new java.awt.Color(10, 10, 10));
        lblKelasDiampu.setText("Mandiri");
        lblKelasDiampu.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblKelasDiampu, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        lblJumlahJadwal.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblJumlahJadwal.setForeground(new java.awt.Color(10, 10, 10));
        lblJumlahJadwal.setText("0 Sesi Terjadwal");
        lblJumlahJadwal.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBody.add(lblJumlahJadwal, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 290, -1, -1));

        panelBadgePeserta.setBackground(new java.awt.Color(210, 230, 255));
        panelBadgePeserta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelBadgePeserta.setPreferredSize(new java.awt.Dimension(130, 24));
        panelBadgePeserta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblJumlahPeserta.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblJumlahPeserta.setForeground(new java.awt.Color(0, 60, 160));
        lblJumlahPeserta.setText("0 Peserta");
        panelBadgePeserta.add(lblJumlahPeserta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 4, -1, -1));

        panelBody.add(panelBadgePeserta, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 130, 24));

        panelBadgeStatus.setBackground(new java.awt.Color(210, 255, 210));
        panelBadgeStatus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelBadgeStatus.setPreferredSize(new java.awt.Dimension(145, 24));
        panelBadgeStatus.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblStatusAktif.setFont(new java.awt.Font("Inter", 1, 11)); // NOI18N
        lblStatusAktif.setForeground(new java.awt.Color(0, 122, 0));
        lblStatusAktif.setText("Aktif Mengajar");
        panelBadgeStatus.add(lblStatusAktif, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 4, -1, -1));

        panelBody.add(panelBadgeStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 320, 145, 24));

        lblJudulPeserta.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        lblJudulPeserta.setForeground(new java.awt.Color(122, 0, 0));
        lblJudulPeserta.setText("DAFTAR PESERTA BIMBINGAN");
        lblJudulPeserta.setPreferredSize(new java.awt.Dimension(260, 14));
        panelBody.add(lblJudulPeserta, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 10, -1, -1));

        panelPesertaBimbingan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelPesertaBimbingan.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setViewportView(panelPesertaBimbingan);

        panelBody.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 290, 314));

        // Geser tombol + Tambah Jadwal ke x=315 agar tidak bertumpuk dengan panelBadgeStatus
        btnTambahJadwal.setBackground(new java.awt.Color(0, 90, 160));
        btnTambahJadwal.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        btnTambahJadwal.setForeground(new java.awt.Color(255, 255, 255));
        btnTambahJadwal.setText("+ Tambah Jadwal");
        btnTambahJadwal.setBorderPainted(false);
        btnTambahJadwal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTambahJadwal.setFocusPainted(false);
        btnTambahJadwal.setPreferredSize(new java.awt.Dimension(135, 24));
        btnTambahJadwal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahJadwalActionPerformed(evt);
            }
        });
        panelBody.add(btnTambahJadwal, new org.netbeans.lib.awtextra.AbsoluteConstraints(315, 320, 135, 24));

        getContentPane().add(panelBody, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 68, 790, -1));

        btnSimpan.setBackground(new java.awt.Color(122, 0, 0));
        btnSimpan.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setText("Edit");
        btnSimpan.setBorderPainted(false);
        btnSimpan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpan.setFocusPainted(false);
        btnSimpan.setPreferredSize(new java.awt.Dimension(116, 34));
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
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

    private void btnTutupHeaderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTutupHeaderActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnTutupHeaderActionPerformed

    private void btnTambahJadwalActionPerformed(java.awt.event.ActionEvent evt) {
        tampilkanDialogTambahJadwal();
    }

    private void btnTutupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTutupActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnTutupActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        FormEditInstruktur formEdit = new FormEditInstruktur(this, true, idInstrukturAktif, null);
        formEdit.setVisible(true);

        // Refresh data setelah dialog edit ditutup
        DataStore.muatUlang();
        loadDataIdentitas(idInstrukturAktif);
        loadPesertaBimbingan(idInstrukturAktif);
    }//GEN-LAST:event_btnSimpanActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormDetailInstruktur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormDetailInstruktur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormDetailInstruktur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormDetailInstruktur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormDetailInstruktur(null, true, "INS001").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambahJadwal;
    private javax.swing.JButton btnTutup;
    private javax.swing.JButton btnTutupHeader;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblHakAkses;
    private javax.swing.JLabel lblIdHeader;
    private javax.swing.JLabel lblIdInstruktur;
    private javax.swing.JLabel lblJudulIdentitas;
    private javax.swing.JLabel lblJudulKeahlian;
    private javax.swing.JLabel lblJudulPeserta;
    private javax.swing.JLabel lblJumlahJadwal;
    private javax.swing.JLabel lblJumlahPeserta;
    private javax.swing.JLabel lblKelasDiampu;
    private javax.swing.JLabel lblLabelHakAkses;
    private javax.swing.JLabel lblLabelIdInstruktur;
    private javax.swing.JLabel lblLabelJumlahJadwal;
    private javax.swing.JLabel lblLabelKelasDiampu;
    private javax.swing.JLabel lblLabelLevelJlpt;
    private javax.swing.JLabel lblLabelNoTelp;
    private javax.swing.JLabel lblLabelRole;
    private javax.swing.JLabel lblLabelSpesialisasi;
    private javax.swing.JLabel lblLabelUsername;
    private javax.swing.JLabel lblLevelJlpt;
    private javax.swing.JLabel lblNamaHeader;
    private javax.swing.JLabel lblNoTelp;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSpesialisasi;
    private javax.swing.JLabel lblStatusAktif;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPanel panelAvatar;
    private javax.swing.JPanel panelBadgePeserta;
    private javax.swing.JPanel panelBadgeStatus;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelPesertaBimbingan;
    // End of variables declaration//GEN-END:variables
}