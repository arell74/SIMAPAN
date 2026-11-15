/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views.peserta;

import DataStore.DataStore;
import Model.HasilKuis;
import Model.JawabanSoal;
import Model.Kuis;
import Model.Peserta;
import Model.Soal;
import Util.TabelUtil;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.table.DefaultTableModel;

/**
 * Panel bagi Peserta untuk melihat daftar Kuis yang tersedia untuk kelasnya
 * dan mengerjakannya dengan antarmuka yang bersih dan fokus.
 */
public class PanelKerjakanKuis extends javax.swing.JPanel {

    private static final String KARTU_DAFTAR = "DAFTAR";
    private static final String KARTU_KERJAKAN = "KERJAKAN";

    private String idPesertaAktif;
    private DefaultTableModel modelTabelDaftar;
    private ArrayList<Kuis> kuisTampil = new ArrayList<>();

    private Kuis kuisSedangDikerjakan;
    private ArrayList<Object> daftarInputJawaban = new ArrayList<>();

    public PanelKerjakanKuis(String idPeserta) {
        initComponents();
        this.idPesertaAktif = idPeserta;

        setupTabelDaftar();
        loadDaftarKuis(""); // Muat semua kuis saat awal
    }

    private Peserta cariPesertaAktif() {
        for (Peserta p : DataStore.daftarPeserta) {
            if (p.getIdPeserta() != null && p.getIdPeserta().equals(idPesertaAktif)) {
                return p;
            }
        }
        return null;
    }

    private boolean sudahDikerjakan(String idKuis) {
        for (HasilKuis h : DataStore.daftarHasilKuis) {
            if (h.getIdKuis().equals(idKuis) && h.getIdPeserta().equals(idPesertaAktif)) {
                return true;
            }
        }
        return false;
    }

    private void setupTabelDaftar() {
        tabelDaftarKuis.setRowHeight(34);

        modelTabelDaftar = new DefaultTableModel(
            new Object[][]{},
            new String[]{"No.", "Judul Kuis", "Kelas", "Jumlah Soal", "Total Poin", "Batas Waktu"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelDaftarKuis.setModel(modelTabelDaftar);
        
        // Styling Tabel Modern
        tabelDaftarKuis.setSelectionBackground(new java.awt.Color(230, 240, 255));
        tabelDaftarKuis.setSelectionForeground(new java.awt.Color(0, 50, 150));
        tabelDaftarKuis.setShowVerticalLines(false);
        tabelDaftarKuis.setGridColor(new java.awt.Color(240, 240, 240));

        TabelUtil.autoResizeKolom(tabelDaftarKuis);
        javax.swing.table.JTableHeader header = tabelDaftarKuis.getTableHeader();
        header.setBackground(new java.awt.Color(40, 40, 40));
        header.setForeground(java.awt.Color.WHITE);
        header.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 13));
        header.setOpaque(true);
    }

    // Overload method untuk mendukung pencarian
    private void loadDaftarKuis(String keyword) {
        modelTabelDaftar.setRowCount(0);
        kuisTampil.clear();

        Peserta pesertaAktif = cariPesertaAktif();
        String kelasPeserta = (pesertaAktif != null) ? pesertaAktif.getKelas() : "";

        int no = 1;
        for (Kuis k : DataStore.daftarKuis) {
            boolean untukKelasIni = "Semua Kelas".equals(k.getKelasTarget()) || k.getKelasTarget().equals(kelasPeserta);
            boolean cocokPencarian = keyword.isEmpty() || k.getJudulKuis().toLowerCase().contains(keyword.toLowerCase());
            
            if (untukKelasIni && !sudahDikerjakan(k.getIdKuis()) && cocokPencarian) {
                modelTabelDaftar.addRow(new Object[]{
                    no++,
                    k.getJudulKuis(),
                    k.getKelasTarget(),
                    k.getJumlahSoal(),
                    k.getTotalPoin(),
                    k.getBatasWaktuMenit() == 0 ? "Tanpa batas" : (k.getBatasWaktuMenit() + " menit")
                });
                kuisTampil.add(k);
            }
        }
        
        jLabelInfoKuis.setText("Tersedia " + kuisTampil.size() + " kuis untuk dikerjakan");
    }

    private void bangunFormSoal(Kuis kuis) {
        lblJudulKerjakan.setText(kuis.getJudulKuis());

        String deskripsi = (kuis.getDeskripsi() == null || kuis.getDeskripsi().isEmpty())
                ? "Tidak ada instruksi tambahan." : kuis.getDeskripsi();
        String infoWaktu = kuis.getBatasWaktuMenit() == 0 ? "Tanpa batas waktu" : (kuis.getBatasWaktuMenit() + " menit");
        
        lblDeskripsiKerjakan.setText("<html>" + deskripsi + "<br/><br/><b>Total:</b> " + kuis.getJumlahSoal() + " Soal &nbsp;|&nbsp; <b>Waktu:</b> " + infoWaktu + "</html>");

        panelDaftarSoalDinamis.removeAll();
        daftarInputJawaban.clear();
        panelDaftarSoalDinamis.setLayout(new javax.swing.BoxLayout(panelDaftarSoalDinamis, javax.swing.BoxLayout.Y_AXIS));

        // Tambahkan ruang kosong sedikit di atas agar tidak terlalu mepet dengan header
        panelDaftarSoalDinamis.add(javax.swing.Box.createVerticalStrut(15));

        int nomor = 1;
        for (Soal soal : kuis.getDaftarSoal()) {
            javax.swing.JPanel panelSatuSoal = buatPanelSatuSoal(nomor, soal);
            panelSatuSoal.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            
            // Batasi lebar maksimal agar nyaman dibaca (tidak terlalu memanjang ke ujung layar)
            panelSatuSoal.setMaximumSize(new java.awt.Dimension(850, panelSatuSoal.getPreferredSize().height));
            
            panelDaftarSoalDinamis.add(panelSatuSoal);
            panelDaftarSoalDinamis.add(javax.swing.Box.createVerticalStrut(20)); // Celah antar soal lebih renggang
            nomor++;
        }

        panelDaftarSoalDinamis.revalidate();
        panelDaftarSoalDinamis.repaint();
        scrollSoal.getVerticalScrollBar().setValue(0);
    }

    /**
     * Membuat 1 blok soal dengan desain kartu yang bersih dan lega.
     */
    private javax.swing.JPanel buatPanelSatuSoal(int nomor, Soal soal) {
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setBackground(new java.awt.Color(255, 255, 255));
        
        // Padding dalam (EmptyBorder) diperbesar menjadi 20px agar lega
        panel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(210, 210, 210), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.JLabel lblPertanyaan = new javax.swing.JLabel(
            "<html><span style='color:#7A0000;'><b>Soal " + nomor + "</b> (" + soal.getPoin() + " poin)</span><br/><br/>" + soal.getPertanyaan() + "</html>");
        lblPertanyaan.setFont(new java.awt.Font("Inter", 0, 14)); // Font pertanyaan diperbesar
        lblPertanyaan.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        panel.add(lblPertanyaan);
        panel.add(javax.swing.Box.createVerticalStrut(15)); // Jarak antara pertanyaan dan pilihan jawaban

        if (soal.isPilihanGanda()) {
            ButtonGroup grup = new ButtonGroup();

            String[] label = {"A", "B", "C", "D"};
            String[] isiPilihan = {soal.getPilihanA(), soal.getPilihanB(), soal.getPilihanC(), soal.getPilihanD()};

            for (int i = 0; i < label.length; i++) {
                javax.swing.JRadioButton radio = new javax.swing.JRadioButton(label[i] + ". " + isiPilihan[i]);
                radio.setActionCommand(label[i]);
                radio.setBackground(new java.awt.Color(255, 255, 255));
                radio.setFont(new java.awt.Font("Inter", 0, 13)); // Font jawaban
                radio.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                radio.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                
                grup.add(radio);
                panel.add(radio);
                panel.add(javax.swing.Box.createVerticalStrut(8)); // Jarak antar pilihan ganda
            }

            daftarInputJawaban.add(grup);

        } else {
            javax.swing.JTextArea areaJawaban = new javax.swing.JTextArea(5, 40);
            areaJawaban.setLineWrap(true);
            areaJawaban.setWrapStyleWord(true);
            areaJawaban.setFont(new java.awt.Font("Inter", 0, 13));
            areaJawaban.setMargin(new java.awt.Insets(10, 10, 10, 10)); // Padding dalam text area

            javax.swing.JScrollPane scrollJawaban = new javax.swing.JScrollPane(areaJawaban);
            scrollJawaban.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
            scrollJawaban.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 120));
            panel.add(scrollJawaban);

            daftarInputJawaban.add(areaJawaban);
        }

        return panel;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());

        panelKartuUtama = new javax.swing.JPanel();
        panelKartuUtama.setLayout(new java.awt.CardLayout());

        // ===================== KARTU DAFTAR KUIS =====================
        panelDaftar = new javax.swing.JPanel();
        panelDaftar.setBackground(new java.awt.Color(245, 245, 245));
        panelDaftar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelJudulDaftar = new javax.swing.JLabel();
        jLabelJudulDaftar.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 16));
        jLabelJudulDaftar.setForeground(new java.awt.Color(50, 50, 50));
        jLabelJudulDaftar.setText("Tugas Kuis Tersedia");
        panelDaftar.add(jLabelJudulDaftar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 30));

        jLabelInfoKuis = new javax.swing.JLabel();
        jLabelInfoKuis.setFont(new java.awt.Font("Inter", 0, 12));
        jLabelInfoKuis.setForeground(new java.awt.Color(120, 120, 120));
        jLabelInfoKuis.setText("Tersedia 0 kuis untuk dikerjakan");
        panelDaftar.add(jLabelInfoKuis, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 25, 250, 20));

        // --- Fitur Pencarian ---
        jLabelCari = new javax.swing.JLabel();
        jLabelCari.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        jLabelCari.setForeground(new java.awt.Color(70, 70, 70));
        jLabelCari.setText("Cari Kuis:");
        panelDaftar.add(jLabelCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 65, -1, 32));

        txtCari = new javax.swing.JTextField();
        txtCari.setFont(new java.awt.Font("Inter", 0, 12));
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                loadDaftarKuis(txtCari.getText().trim());
            }
        });
        panelDaftar.add(txtCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 65, 300, 32));

        btnRefresh = new javax.swing.JButton();
        btnRefresh.setBackground(new java.awt.Color(220, 220, 220));
        btnRefresh.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        btnRefresh.setForeground(new java.awt.Color(50, 50, 50));
        btnRefresh.setText("Segarkan");
        btnRefresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadDaftarKuis(txtCari.getText());
            }
        });
        panelDaftar.add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 65, 100, 32));

        btnKerjakan = new javax.swing.JButton();
        btnKerjakan.setBackground(new java.awt.Color(20, 120, 40)); // Warna hijau untuk aksi utama (Call to Action)
        btnKerjakan.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        btnKerjakan.setForeground(new java.awt.Color(255, 255, 255));
        btnKerjakan.setText("Kerjakan Kuis Terpilih");
        btnKerjakan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnKerjakan.setFocusPainted(false);
        btnKerjakan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKerjakanActionPerformed(evt);
            }
        });
        panelDaftar.add(btnKerjakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 65, 180, 32));

        // --- Area Tabel ---
        panelTabelDaftar = new javax.swing.JPanel();
        panelTabelDaftar.setBackground(new java.awt.Color(255, 255, 255));
        panelTabelDaftar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelTabelDaftar.setLayout(new java.awt.BorderLayout());

        scrollTabelDaftar = new javax.swing.JScrollPane();
        scrollTabelDaftar.setBorder(null);
        tabelDaftarKuis = new javax.swing.JTable();
        scrollTabelDaftar.setViewportView(tabelDaftarKuis);
        panelTabelDaftar.add(scrollTabelDaftar, java.awt.BorderLayout.CENTER);

        panelDaftar.add(panelTabelDaftar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 900, 470));

        panelKartuUtama.add(panelDaftar, KARTU_DAFTAR);

        // ===================== KARTU KERJAKAN KUIS =====================
        panelKerjakan = new javax.swing.JPanel();
        panelKerjakan.setBackground(new java.awt.Color(240, 240, 240));
        panelKerjakan.setLayout(new java.awt.BorderLayout());

        panelInfoKerjakan = new javax.swing.JPanel();
        panelInfoKerjakan.setBackground(new java.awt.Color(255, 255, 255));
        panelInfoKerjakan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(210, 210, 210)));
        panelInfoKerjakan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelInfoKerjakan.setPreferredSize(new java.awt.Dimension(940, 100)); // Ditinggikan sedikit

        btnKembali = new javax.swing.JButton();
        btnKembali.setBackground(new java.awt.Color(240, 240, 240));
        btnKembali.setFont(new java.awt.Font("Inter", 0, 12));
        btnKembali.setText("← Batal");
        btnKembali.setToolTipText("Kembali ke daftar kuis");
        btnKembali.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnKembali.setFocusPainted(false);
        btnKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliActionPerformed(evt);
            }
        });
        panelInfoKerjakan.add(btnKembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 32));

        lblJudulKerjakan = new javax.swing.JLabel();
        lblJudulKerjakan.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 18));
        lblJudulKerjakan.setForeground(new java.awt.Color(40, 40, 40));
        lblJudulKerjakan.setText("Judul Kuis");
        panelInfoKerjakan.add(lblJudulKerjakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 780, 24));

        lblDeskripsiKerjakan = new javax.swing.JLabel();
        lblDeskripsiKerjakan.setFont(new java.awt.Font("Inter", 0, 13));
        lblDeskripsiKerjakan.setForeground(new java.awt.Color(100, 100, 100));
        lblDeskripsiKerjakan.setText("Deskripsi");
        panelInfoKerjakan.add(lblDeskripsiKerjakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 45, 780, 45));

        panelKerjakan.add(panelInfoKerjakan, java.awt.BorderLayout.NORTH);

        panelDaftarSoalDinamis = new javax.swing.JPanel();
        panelDaftarSoalDinamis.setBackground(new java.awt.Color(240, 240, 240));

        scrollSoal = new javax.swing.JScrollPane();
        scrollSoal.setBorder(null);
        scrollSoal.setViewportView(panelDaftarSoalDinamis);
        scrollSoal.getVerticalScrollBar().setUnitIncrement(20); // Scroll lebih mulus
        panelKerjakan.add(scrollSoal, java.awt.BorderLayout.CENTER);

        panelBawahKerjakan = new javax.swing.JPanel();
        panelBawahKerjakan.setBackground(new java.awt.Color(255, 255, 255));
        panelBawahKerjakan.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(210, 210, 210)));
        panelBawahKerjakan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelBawahKerjakan.setPreferredSize(new java.awt.Dimension(940, 70));

        btnSubmitJawaban = new javax.swing.JButton();
        btnSubmitJawaban.setBackground(new java.awt.Color(122, 0, 0));
        btnSubmitJawaban.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 14));
        btnSubmitJawaban.setForeground(new java.awt.Color(255, 255, 255));
        btnSubmitJawaban.setText("Kumpulkan Jawaban");
        btnSubmitJawaban.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSubmitJawaban.setFocusPainted(false);
        btnSubmitJawaban.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitJawabanActionPerformed(evt);
            }
        });
        
        // Memposisikan tombol submit di kanan bawah agar terasa seperti tombol "Next/Finish" konvensional
        panelBawahKerjakan.add(btnSubmitJawaban, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 15, 210, 42));

        panelKerjakan.add(panelBawahKerjakan, java.awt.BorderLayout.SOUTH);

        panelKartuUtama.add(panelKerjakan, KARTU_KERJAKAN);

        add(panelKartuUtama, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnKerjakanActionPerformed(java.awt.event.ActionEvent evt) {
        int baris = tabelDaftarKuis.getSelectedRow();
        if (baris == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Pilih dahulu kuis yang ingin dikerjakan dari tabel!", "Peringatan",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        kuisSedangDikerjakan = kuisTampil.get(baris);
        bangunFormSoal(kuisSedangDikerjakan);

        java.awt.CardLayout cl = (java.awt.CardLayout) panelKartuUtama.getLayout();
        cl.show(panelKartuUtama, KARTU_KERJAKAN);
    }

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {
        java.awt.CardLayout cl = (java.awt.CardLayout) panelKartuUtama.getLayout();
        cl.show(panelKartuUtama, KARTU_DAFTAR);
    }

    private void btnSubmitJawabanActionPerformed(java.awt.event.ActionEvent evt) {
        if (kuisSedangDikerjakan == null) return;

        int jumlahBelumDijawab = 0;
        for (int i = 0; i < kuisSedangDikerjakan.getDaftarSoal().size(); i++) {
            Soal soal = kuisSedangDikerjakan.getDaftarSoal().get(i);
            Object input = daftarInputJawaban.get(i);

            if (soal.isPilihanGanda()) {
                if (ambilJawabanTerpilih((ButtonGroup) input) == null) {
                    jumlahBelumDijawab++;
                }
            } else {
                if (((javax.swing.JTextArea) input).getText().trim().isEmpty()) {
                    jumlahBelumDijawab++;
                }
            }
        }

        if (jumlahBelumDijawab > 0) {
            int lanjut = javax.swing.JOptionPane.showConfirmDialog(this,
                "Masih ada " + jumlahBelumDijawab + " soal yang belum dijawab.\nYakin ingin mengumpulkan jawaban sekarang?",
                "Konfirmasi Submit", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE);
            if (lanjut != javax.swing.JOptionPane.YES_OPTION) {
                return;
            }
        } else {
             int lanjut = javax.swing.JOptionPane.showConfirmDialog(this,
                "Anda sudah menjawab seluruh soal.\nKumpulkan jawaban sekarang?",
                "Konfirmasi Submit", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
            if (lanjut != javax.swing.JOptionPane.YES_OPTION) {
                return;
            }
        }

        ArrayList<JawabanSoal> daftarJawaban = new ArrayList<>();

        for (int i = 0; i < kuisSedangDikerjakan.getDaftarSoal().size(); i++) {
            Soal soal = kuisSedangDikerjakan.getDaftarSoal().get(i);
            Object input = daftarInputJawaban.get(i);

            if (soal.isPilihanGanda()) {
                String jawabanTerpilih = ambilJawabanTerpilih((ButtonGroup) input);
                boolean benar = jawabanTerpilih != null && jawabanTerpilih.equals(soal.getKunciJawaban());
                int poin = benar ? soal.getPoin() : 0;

                daftarJawaban.add(new JawabanSoal(soal.getIdSoal(), soal.getTipeSoal(), soal.getPertanyaan(),
                    jawabanTerpilih, soal.getKunciJawaban(), soal.getPoin(), poin, true));

            } else {
                String jawabanEssay = ((javax.swing.JTextArea) input).getText().trim();

                daftarJawaban.add(new JawabanSoal(soal.getIdSoal(), soal.getTipeSoal(), soal.getPertanyaan(),
                    jawabanEssay, soal.getKunciJawaban(), soal.getPoin(), 0, false));
            }
        }

        String idHasilBaru = "HSL" + String.format("%03d", DataStore.daftarHasilKuis.size() + 1);
        String tanggal = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date());

        HasilKuis hasilBaru = new HasilKuis(idHasilBaru, kuisSedangDikerjakan.getIdKuis(),
            kuisSedangDikerjakan.getJudulKuis(), idPesertaAktif, tanggal, daftarJawaban);

        DataStore.daftarHasilKuis.add(hasilBaru);

        int jumlahEssayMenunggu = hasilBaru.getJumlahEssayBelumDinilai();
        String pesanHasil = "Jawaban berhasil dikumpulkan!\n\n"
            + "Skor Pilihan Ganda (Otomatis): " + hasilBaru.getTotalPoinDiperoleh()
            + " / " + hasilBaru.getTotalPoinMaksimal() + " poin";
        
        if (jumlahEssayMenunggu > 0) {
            pesanHasil += "\n\nMenunggu instruktur menilai " + jumlahEssayMenunggu + " soal Essay Anda.";
        }

        javax.swing.JOptionPane.showMessageDialog(this, pesanHasil,
            "Kuis Selesai", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        kuisSedangDikerjakan = null;
        loadDaftarKuis(""); // Refresh tabel

        java.awt.CardLayout cl = (java.awt.CardLayout) panelKartuUtama.getLayout();
        cl.show(panelKartuUtama, KARTU_DAFTAR);
    }

    private String ambilJawabanTerpilih(ButtonGroup grup) {
        Enumeration<AbstractButton> elemen = grup.getElements();
        while (elemen.hasMoreElements()) {
            AbstractButton tombol = elemen.nextElement();
            if (tombol.isSelected()) {
                return tombol.getActionCommand();
            }
        }
        return null;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnKerjakan;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSubmitJawaban;
    private javax.swing.JLabel jLabelCari;
    private javax.swing.JLabel jLabelInfoKuis;
    private javax.swing.JLabel jLabelJudulDaftar;
    private javax.swing.JLabel lblDeskripsiKerjakan;
    private javax.swing.JLabel lblJudulKerjakan;
    private javax.swing.JPanel panelBawahKerjakan;
    private javax.swing.JPanel panelDaftar;
    private javax.swing.JPanel panelDaftarSoalDinamis;
    private javax.swing.JPanel panelInfoKerjakan;
    private javax.swing.JPanel panelKartuUtama;
    private javax.swing.JPanel panelKerjakan;
    private javax.swing.JPanel panelTabelDaftar;
    private javax.swing.JScrollPane scrollSoal;
    private javax.swing.JScrollPane scrollTabelDaftar;
    private javax.swing.JTable tabelDaftarKuis;
    private javax.swing.JTextField txtCari;
    // End of variables declaration//GEN-END:variables
}