/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views.instruktur;

import DataStore.DataStore;
import Model.Instruktur;
import Model.Kuis;
import Model.Pengguna;
import Model.Soal;
import Util.TabelUtil;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 * Panel bagi Instruktur untuk membuat Kuis berisi soal Pilihan Ganda dan/atau
 * Essay yang nantinya bisa dikerjakan oleh Peserta (role Siswa).
 */
public class PanelInputKuis extends javax.swing.JPanel {

    private String idInstrukturAktif;

    private DefaultTableModel modelTabelSoal;
    private DefaultTableModel modelTabelKuis;

    private ArrayList<Soal> soalSementara = new ArrayList<>();
    private ArrayList<Kuis> kuisTampil = new ArrayList<>();

    private static final String KARTU_PG = "PG";
    private static final String KARTU_ESSAY = "ESSAY";

    public PanelInputKuis(String idInstruktur) {
        initComponents();

        this.idInstrukturAktif = idInstruktur;

        setupComboBox();
        setupTabelSoal();
        setupTabelKuis();
        loadDaftarKuis();
    }

    private void setupComboBox() {
        cbKelasTargetKuis.removeAllItems();
        cbKelasTargetKuis.addItem("Semua Kelas");
        cbKelasTargetKuis.addItem("Kelas A");
        cbKelasTargetKuis.addItem("Kelas B");

        cbTipeSoal.removeAllItems();
        cbTipeSoal.addItem(Soal.TIPE_PILIHAN_GANDA);
        cbTipeSoal.addItem(Soal.TIPE_ESSAY);

        cbKunciJawaban.removeAllItems();
        cbKunciJawaban.addItem("A");
        cbKunciJawaban.addItem("B");
        cbKunciJawaban.addItem("C");
        cbKunciJawaban.addItem("D");

        txtPoinSoal.setText("10");
        txtBatasWaktu.setText("0");

        cbTipeSoal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String pilihan = String.valueOf(cbTipeSoal.getSelectedItem());
                java.awt.CardLayout cl = (java.awt.CardLayout) panelDinamisSoal.getLayout();
                if (Soal.TIPE_ESSAY.equals(pilihan)) {
                    cl.show(panelDinamisSoal, KARTU_ESSAY);
                } else {
                    cl.show(panelDinamisSoal, KARTU_PG);
                }
            }
        });
    }

    private void setupTabelSoal() {
        tabelSoal.setRowHeight(32);

        modelTabelSoal = new DefaultTableModel(
            new Object[][]{},
            new String[]{"No.", "Tipe", "Pertanyaan", "Poin"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelSoal.setModel(modelTabelSoal);
        dandaniHeaderTabel(tabelSoal);
    }

    private void setupTabelKuis() {
        tabelKuis.setRowHeight(32);

        modelTabelKuis = new DefaultTableModel(
            new Object[][]{},
            new String[]{"No.", "Judul Kuis", "Kelas", "Jumlah Soal", "Total Poin", "Tanggal Dibuat"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelKuis.setModel(modelTabelKuis);
        dandaniHeaderTabel(tabelKuis);
    }

    private void dandaniHeaderTabel(javax.swing.JTable tabel) {
        tabel.setSelectionBackground(new java.awt.Color(255, 230, 230));
        tabel.setSelectionForeground(new java.awt.Color(122, 0, 0));

        TabelUtil.autoResizeKolom(tabel);
        javax.swing.table.JTableHeader header = tabel.getTableHeader();
        header.setBackground(new java.awt.Color(122, 0, 0));
        header.setForeground(java.awt.Color.WHITE);
        header.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        header.setOpaque(true);
    }

    private String cariIdAsliInstruktur() {
        String idAsli = idInstrukturAktif;
        for (Pengguna pgn : DataStore.daftarPengguna) {
            if (pgn instanceof Instruktur) {
                Instruktur ins = (Instruktur) pgn;
                if ((ins.getIdInstruktur() != null && ins.getIdInstruktur().equals(idInstrukturAktif))
                        || (ins.getUsername() != null && ins.getUsername().equals(idInstrukturAktif))) {
                    idAsli = ins.getIdInstruktur();
                    break;
                }
            }
        }
        return idAsli;
    }

    private void loadDaftarKuis() {
        modelTabelKuis.setRowCount(0);
        kuisTampil.clear();

        String idAsli = cariIdAsliInstruktur();

        int no = 1;
        for (Kuis k : DataStore.daftarKuis) {
            if (k.getIdInstruktur() != null && k.getIdInstruktur().equals(idAsli)) {
                modelTabelKuis.addRow(new Object[]{
                    no++,
                    k.getJudulKuis(),
                    k.getKelasTarget(),
                    k.getJumlahSoal(),
                    k.getTotalPoin(),
                    k.getTanggalDibuat()
                });
                kuisTampil.add(k);
            }
        }
    }

    private void muatUlangTabelSoalSementara() {
        modelTabelSoal.setRowCount(0);
        int no = 1;
        for (Soal s : soalSementara) {
            modelTabelSoal.addRow(new Object[]{
                no++,
                s.getTipeSoal(),
                s.getCuplikanPertanyaan(),
                s.getPoin()
            });
        }
    }

    private void resetFormSoal() {
        txtAreaPertanyaan.setText("");
        txtPilihanA.setText("");
        txtPilihanB.setText("");
        txtPilihanC.setText("");
        txtPilihanD.setText("");
        txtAreaKunciEssay.setText("");
        cbKunciJawaban.setSelectedIndex(0);
        txtPoinSoal.setText("10");
    }

    private void resetFormKuisPenuh() {
        txtJudulKuis.setText("");
        txtDeskripsiKuis.setText("");
        txtBatasWaktu.setText("0");
        cbKelasTargetKuis.setSelectedIndex(0);
        cbTipeSoal.setSelectedIndex(0);
        resetFormSoal();
        soalSementara.clear();
        muatUlangTabelSoalSementara();
    }

    private int ambilAngka(String teks, int nilaiDefault) {
        try {
            return Integer.parseInt(teks.trim());
        } catch (Exception e) {
            return nilaiDefault;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());

        scrollUtama = new javax.swing.JScrollPane();
        scrollUtama.setBorder(null);
        scrollUtama.getVerticalScrollBar().setUnitIncrement(16);

        panelKonten = new javax.swing.JPanel();
        panelKonten.setBackground(new java.awt.Color(245, 245, 245));
        panelKonten.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelKonten.setPreferredSize(new java.awt.Dimension(940, 1070));

        // ===================== SECTION 1: INFORMASI KUIS =====================
        panelInfoKuis = new javax.swing.JPanel();
        panelInfoKuis.setBackground(new java.awt.Color(255, 255, 255));
        panelInfoKuis.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelInfoKuis.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelJudulForm = new javax.swing.JLabel();
        jLabelJudulForm.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 14));
        jLabelJudulForm.setForeground(new java.awt.Color(70, 70, 70));
        jLabelJudulForm.setText("Buat Kuis Baru");
        panelInfoKuis.add(jLabelJudulForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, -1, 20));

        jLabelSubForm = new javax.swing.JLabel();
        jLabelSubForm.setFont(new java.awt.Font("Inter", 0, 11));
        jLabelSubForm.setForeground(new java.awt.Color(140, 140, 140));
        jLabelSubForm.setText("Isi informasi umum kuis, lalu tambahkan soal pada bagian di bawah");
        panelInfoKuis.add(jLabelSubForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 35, -1, 16));

        jLabelJudulKuis = new javax.swing.JLabel();
        jLabelJudulKuis.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        jLabelJudulKuis.setForeground(new java.awt.Color(60, 60, 60));
        jLabelJudulKuis.setText("Judul Kuis");
        panelInfoKuis.add(jLabelJudulKuis, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 65, -1, 16));

        txtJudulKuis = new javax.swing.JTextField();
        txtJudulKuis.setFont(new java.awt.Font("Inter", 0, 12));
        panelInfoKuis.add(txtJudulKuis, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 85, 450, 32));

        jLabelKelasKuis = new javax.swing.JLabel();
        jLabelKelasKuis.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        jLabelKelasKuis.setForeground(new java.awt.Color(60, 60, 60));
        jLabelKelasKuis.setText("Kelas Target");
        panelInfoKuis.add(jLabelKelasKuis, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 65, -1, 16));

        cbKelasTargetKuis = new javax.swing.JComboBox<>();
        cbKelasTargetKuis.setFont(new java.awt.Font("Inter", 0, 12));
        panelInfoKuis.add(cbKelasTargetKuis, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 85, 200, 32));

        jLabelWaktuKuis = new javax.swing.JLabel();
        jLabelWaktuKuis.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        jLabelWaktuKuis.setForeground(new java.awt.Color(60, 60, 60));
        jLabelWaktuKuis.setText("Batas Waktu (menit, 0 = no limit)");
        panelInfoKuis.add(jLabelWaktuKuis, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 65, -1, 16));

        txtBatasWaktu = new javax.swing.JTextField();
        txtBatasWaktu.setFont(new java.awt.Font("Inter", 0, 12));
        panelInfoKuis.add(txtBatasWaktu, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 85, 210, 32));

        jLabelDeskripsiKuis = new javax.swing.JLabel();
        jLabelDeskripsiKuis.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        jLabelDeskripsiKuis.setForeground(new java.awt.Color(60, 60, 60));
        jLabelDeskripsiKuis.setText("Deskripsi (opsional)");
        panelInfoKuis.add(jLabelDeskripsiKuis, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, 16));

        txtDeskripsiKuis = new javax.swing.JTextField();
        txtDeskripsiKuis.setFont(new java.awt.Font("Inter", 0, 12));
        panelInfoKuis.add(txtDeskripsiKuis, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 900, 32));

        panelKonten.add(panelInfoKuis, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 940, 200));

        // ===================== SECTION 2: TAMBAH SOAL =====================
        panelBuatSoal = new javax.swing.JPanel();
        panelBuatSoal.setBackground(new java.awt.Color(255, 255, 255));
        panelBuatSoal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelBuatSoal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelBuatSoal = new javax.swing.JLabel();
        jLabelBuatSoal.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 14));
        jLabelBuatSoal.setForeground(new java.awt.Color(70, 70, 70));
        jLabelBuatSoal.setText("Tambah Soal");
        panelBuatSoal.add(jLabelBuatSoal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, -1, 20));

        jLabelTipeSoal = new javax.swing.JLabel();
        jLabelTipeSoal.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        jLabelTipeSoal.setForeground(new java.awt.Color(60, 60, 60));
        jLabelTipeSoal.setText("Tipe Soal");
        panelBuatSoal.add(jLabelTipeSoal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 45, -1, 16));

        cbTipeSoal = new javax.swing.JComboBox<>();
        cbTipeSoal.setFont(new java.awt.Font("Inter", 0, 12));
        panelBuatSoal.add(cbTipeSoal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 65, 200, 32));

        jLabelPoinSoal = new javax.swing.JLabel();
        jLabelPoinSoal.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        jLabelPoinSoal.setForeground(new java.awt.Color(60, 60, 60));
        jLabelPoinSoal.setText("Poin");
        panelBuatSoal.add(jLabelPoinSoal, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 45, -1, 16));

        txtPoinSoal = new javax.swing.JTextField();
        txtPoinSoal.setFont(new java.awt.Font("Inter", 0, 12));
        panelBuatSoal.add(txtPoinSoal, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 65, 80, 32));

        jLabelPertanyaan = new javax.swing.JLabel();
        jLabelPertanyaan.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        jLabelPertanyaan.setForeground(new java.awt.Color(60, 60, 60));
        jLabelPertanyaan.setText("Pertanyaan");
        panelBuatSoal.add(jLabelPertanyaan, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 45, -1, 16));

        txtAreaPertanyaan = new javax.swing.JTextArea();
        txtAreaPertanyaan.setFont(new java.awt.Font("Inter", 0, 12));
        txtAreaPertanyaan.setLineWrap(true);
        txtAreaPertanyaan.setWrapStyleWord(true);
        txtAreaPertanyaan.setRows(3);

        scrollPertanyaan = new javax.swing.JScrollPane();
        scrollPertanyaan.setViewportView(txtAreaPertanyaan);
        panelBuatSoal.add(scrollPertanyaan, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 65, 580, 65));

        panelDinamisSoal = new javax.swing.JPanel();
        panelDinamisSoal.setLayout(new java.awt.CardLayout());
        panelDinamisSoal.setOpaque(false);

        // --- Kartu Pilihan Ganda ---
        panelKartuPG = new javax.swing.JPanel();
        panelKartuPG.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelKartuPG.setOpaque(false);

        jLabelPilihanA = new javax.swing.JLabel();
        jLabelPilihanA.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 11));
        jLabelPilihanA.setText("Pilihan A");
        panelKartuPG.add(jLabelPilihanA, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 14));

        txtPilihanA = new javax.swing.JTextField();
        txtPilihanA.setFont(new java.awt.Font("Inter", 0, 12));
        panelKartuPG.add(txtPilihanA, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 15, 440, 30));

        jLabelPilihanB = new javax.swing.JLabel();
        jLabelPilihanB.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 11));
        jLabelPilihanB.setText("Pilihan B");
        panelKartuPG.add(jLabelPilihanB, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 0, -1, 14));

        txtPilihanB = new javax.swing.JTextField();
        txtPilihanB.setFont(new java.awt.Font("Inter", 0, 12));
        panelKartuPG.add(txtPilihanB, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 15, 440, 30));

        jLabelPilihanC = new javax.swing.JLabel();
        jLabelPilihanC.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 11));
        jLabelPilihanC.setText("Pilihan C");
        panelKartuPG.add(jLabelPilihanC, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 55, -1, 14));

        txtPilihanC = new javax.swing.JTextField();
        txtPilihanC.setFont(new java.awt.Font("Inter", 0, 12));
        panelKartuPG.add(txtPilihanC, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 440, 30));

        jLabelPilihanD = new javax.swing.JLabel();
        jLabelPilihanD.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 11));
        jLabelPilihanD.setText("Pilihan D");
        panelKartuPG.add(jLabelPilihanD, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 55, -1, 14));

        txtPilihanD = new javax.swing.JTextField();
        txtPilihanD.setFont(new java.awt.Font("Inter", 0, 12));
        panelKartuPG.add(txtPilihanD, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 70, 440, 30));

        jLabelKunciJawaban = new javax.swing.JLabel();
        jLabelKunciJawaban.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 11));
        jLabelKunciJawaban.setText("Kunci Jawaban Benar");
        panelKartuPG.add(jLabelKunciJawaban, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 115, -1, 14));

        cbKunciJawaban = new javax.swing.JComboBox<>();
        cbKunciJawaban.setFont(new java.awt.Font("Inter", 0, 12));
        panelKartuPG.add(cbKunciJawaban, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 107, 100, 30));

        panelDinamisSoal.add(panelKartuPG, KARTU_PG);

        // --- Kartu Essay ---
        panelKartuEssay = new javax.swing.JPanel();
        panelKartuEssay.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelKartuEssay.setOpaque(false);

        jLabelKunciEssay = new javax.swing.JLabel();
        jLabelKunciEssay.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 11));
        jLabelKunciEssay.setText("Kunci Jawaban / Rubrik Referensi (opsional)");
        panelKartuEssay.add(jLabelKunciEssay, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 14));

        txtAreaKunciEssay = new javax.swing.JTextArea();
        txtAreaKunciEssay.setFont(new java.awt.Font("Inter", 0, 12));
        txtAreaKunciEssay.setLineWrap(true);
        txtAreaKunciEssay.setWrapStyleWord(true);
        txtAreaKunciEssay.setRows(3);

        scrollKunciEssay = new javax.swing.JScrollPane();
        scrollKunciEssay.setViewportView(txtAreaKunciEssay);
        panelKartuEssay.add(scrollKunciEssay, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 17, 900, 120));

        panelDinamisSoal.add(panelKartuEssay, KARTU_ESSAY);

        panelBuatSoal.add(panelDinamisSoal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 145, 900, 140));

        btnTambahSoal = new javax.swing.JButton();
        btnTambahSoal.setBackground(new java.awt.Color(122, 0, 0));
        btnTambahSoal.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        btnTambahSoal.setForeground(new java.awt.Color(255, 255, 255));
        btnTambahSoal.setText("+ Tambah Soal");
        btnTambahSoal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTambahSoal.setFocusPainted(false);
        btnTambahSoal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahSoalActionPerformed(evt);
            }
        });
        panelBuatSoal.add(btnTambahSoal, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 295, 160, 35));

        panelKonten.add(panelBuatSoal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 215, 940, 345));

        // ===================== SECTION 3: DAFTAR SOAL SEMENTARA =====================
        jLabelDaftarSoal = new javax.swing.JLabel();
        jLabelDaftarSoal.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 14));
        jLabelDaftarSoal.setForeground(new java.awt.Color(70, 70, 70));
        jLabelDaftarSoal.setText("Soal dalam Kuis Ini (Belum Disimpan)");
        panelKonten.add(jLabelDaftarSoal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 580, -1, 20));

        btnHapusSoal = new javax.swing.JButton();
        btnHapusSoal.setBackground(new java.awt.Color(180, 40, 40));
        btnHapusSoal.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        btnHapusSoal.setForeground(new java.awt.Color(255, 255, 255));
        btnHapusSoal.setText("Hapus Terpilih");
        btnHapusSoal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHapusSoal.setFocusPainted(false);
        btnHapusSoal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusSoalActionPerformed(evt);
            }
        });
        panelKonten.add(btnHapusSoal, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 575, 150, 30));

        panelTabelSoal = new javax.swing.JPanel();
        panelTabelSoal.setLayout(new java.awt.BorderLayout());

        scrollTabelSoal = new javax.swing.JScrollPane();
        tabelSoal = new javax.swing.JTable();
        tabelSoal.setFont(new java.awt.Font("Inter", 0, 12));
        scrollTabelSoal.setViewportView(tabelSoal);
        panelTabelSoal.add(scrollTabelSoal, java.awt.BorderLayout.CENTER);

        panelKonten.add(panelTabelSoal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 615, 900, 160));

        btnSimpanKuis = new javax.swing.JButton();
        btnSimpanKuis.setBackground(new java.awt.Color(20, 120, 40));
        btnSimpanKuis.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 14));
        btnSimpanKuis.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpanKuis.setText("Simpan Kuis Lengkap");
        btnSimpanKuis.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpanKuis.setFocusPainted(false);
        btnSimpanKuis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanKuisActionPerformed(evt);
            }
        });
        panelKonten.add(btnSimpanKuis, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 785, 220, 42));

        // ===================== SECTION 4: DAFTAR KUIS TERSIMPAN =====================
        jLabelDaftarKuis = new javax.swing.JLabel();
        jLabelDaftarKuis.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 14));
        jLabelDaftarKuis.setForeground(new java.awt.Color(70, 70, 70));
        jLabelDaftarKuis.setText("Daftar Kuis yang Sudah Dibuat");
        panelKonten.add(jLabelDaftarKuis, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 845, -1, 20));

        btnHapusKuis = new javax.swing.JButton();
        btnHapusKuis.setBackground(new java.awt.Color(180, 40, 40));
        btnHapusKuis.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        btnHapusKuis.setForeground(new java.awt.Color(255, 255, 255));
        btnHapusKuis.setText("Hapus Kuis");
        btnHapusKuis.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHapusKuis.setFocusPainted(false);
        btnHapusKuis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusKuisActionPerformed(evt);
            }
        });
        panelKonten.add(btnHapusKuis, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 840, 150, 30));

        panelTabelKuis = new javax.swing.JPanel();
        panelTabelKuis.setLayout(new java.awt.BorderLayout());

        scrollTabelKuis = new javax.swing.JScrollPane();
        tabelKuis = new javax.swing.JTable();
        tabelKuis.setFont(new java.awt.Font("Inter", 0, 12));
        scrollTabelKuis.setViewportView(tabelKuis);
        panelTabelKuis.add(scrollTabelKuis, java.awt.BorderLayout.CENTER);

        panelKonten.add(panelTabelKuis, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 880, 900, 170));

        scrollUtama.setViewportView(panelKonten);

        add(scrollUtama, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahSoalActionPerformed(java.awt.event.ActionEvent evt) {
        String pertanyaan = txtAreaPertanyaan.getText().trim();
        String tipe = String.valueOf(cbTipeSoal.getSelectedItem());
        int poin = ambilAngka(txtPoinSoal.getText(), 10);

        if (pertanyaan.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Pertanyaan tidak boleh kosong!", "Peringatan",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idSoalBaru = "SOAL" + String.format("%03d", soalSementara.size() + 1);
        Soal soalBaru;

        if (Soal.TIPE_PILIHAN_GANDA.equals(tipe)) {
            String a = txtPilihanA.getText().trim();
            String b = txtPilihanB.getText().trim();
            String c = txtPilihanC.getText().trim();
            String d = txtPilihanD.getText().trim();

            if (a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Semua pilihan (A, B, C, D) harus diisi untuk soal Pilihan Ganda!", "Peringatan",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            String kunci = String.valueOf(cbKunciJawaban.getSelectedItem());
            soalBaru = Soal.buatPilihanGanda(idSoalBaru, pertanyaan, a, b, c, d, kunci, poin);

        } else {
            String kunciEssay = txtAreaKunciEssay.getText().trim();
            soalBaru = Soal.buatEssay(idSoalBaru, pertanyaan, kunciEssay.isEmpty() ? null : kunciEssay, poin);
        }

        soalSementara.add(soalBaru);
        muatUlangTabelSoalSementara();
        resetFormSoal();
    }

    private void btnHapusSoalActionPerformed(java.awt.event.ActionEvent evt) {
        int baris = tabelSoal.getSelectedRow();
        if (baris == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Pilih dahulu soal yang ingin dihapus dari tabel!", "Peringatan",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        soalSementara.remove(baris);
        muatUlangTabelSoalSementara();
    }

    private void btnSimpanKuisActionPerformed(java.awt.event.ActionEvent evt) {
        String judul = txtJudulKuis.getText().trim();
        String kelasTarget = String.valueOf(cbKelasTargetKuis.getSelectedItem());
        String deskripsi = txtDeskripsiKuis.getText().trim();
        int batasWaktu = ambilAngka(txtBatasWaktu.getText(), 0);

        if (judul.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Judul kuis tidak boleh kosong!", "Peringatan",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (soalSementara.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Tambahkan minimal 1 soal sebelum menyimpan kuis!", "Peringatan",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idAsli = cariIdAsliInstruktur();
        String idKuisBaru = "KUIS" + String.format("%03d", DataStore.daftarKuis.size() + 1);
        String tanggal = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());

        Kuis kuisBaru = new Kuis(idKuisBaru, idAsli, judul, kelasTarget, deskripsi, batasWaktu,
            tanggal, new ArrayList<>(soalSementara));
        DataStore.daftarKuis.add(kuisBaru);

        javax.swing.JOptionPane.showMessageDialog(this,
            "Kuis \"" + judul + "\" berhasil disimpan dengan " + kuisBaru.getJumlahSoal()
                + " soal (total " + kuisBaru.getTotalPoin() + " poin) untuk " + kelasTarget + "!",
            "Sukses", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        resetFormKuisPenuh();
        loadDaftarKuis();
    }

    private void btnHapusKuisActionPerformed(java.awt.event.ActionEvent evt) {
        int baris = tabelKuis.getSelectedRow();
        if (baris == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Pilih dahulu kuis yang ingin dihapus dari tabel!", "Peringatan",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int konfirmasi = javax.swing.JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus kuis ini beserta seluruh soalnya?", "Konfirmasi Hapus",
            javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);

        if (konfirmasi == javax.swing.JOptionPane.YES_OPTION) {
            Kuis kuisTerpilih = kuisTampil.get(baris);
            DataStore.daftarKuis.remove(kuisTerpilih);
            loadDaftarKuis();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHapusKuis;
    private javax.swing.JButton btnHapusSoal;
    private javax.swing.JButton btnSimpanKuis;
    private javax.swing.JButton btnTambahSoal;
    private javax.swing.JComboBox<String> cbKelasTargetKuis;
    private javax.swing.JComboBox<String> cbKunciJawaban;
    private javax.swing.JComboBox<String> cbTipeSoal;
    private javax.swing.JLabel jLabelBuatSoal;
    private javax.swing.JLabel jLabelDaftarKuis;
    private javax.swing.JLabel jLabelDaftarSoal;
    private javax.swing.JLabel jLabelDeskripsiKuis;
    private javax.swing.JLabel jLabelJudulForm;
    private javax.swing.JLabel jLabelJudulKuis;
    private javax.swing.JLabel jLabelKelasKuis;
    private javax.swing.JLabel jLabelKunciEssay;
    private javax.swing.JLabel jLabelKunciJawaban;
    private javax.swing.JLabel jLabelPertanyaan;
    private javax.swing.JLabel jLabelPilihanA;
    private javax.swing.JLabel jLabelPilihanB;
    private javax.swing.JLabel jLabelPilihanC;
    private javax.swing.JLabel jLabelPilihanD;
    private javax.swing.JLabel jLabelPoinSoal;
    private javax.swing.JLabel jLabelSubForm;
    private javax.swing.JLabel jLabelTipeSoal;
    private javax.swing.JLabel jLabelWaktuKuis;
    private javax.swing.JPanel panelBuatSoal;
    private javax.swing.JPanel panelDinamisSoal;
    private javax.swing.JPanel panelInfoKuis;
    private javax.swing.JPanel panelKartuEssay;
    private javax.swing.JPanel panelKartuPG;
    private javax.swing.JPanel panelKonten;
    private javax.swing.JPanel panelTabelKuis;
    private javax.swing.JPanel panelTabelSoal;
    private javax.swing.JScrollPane scrollKunciEssay;
    private javax.swing.JScrollPane scrollPertanyaan;
    private javax.swing.JScrollPane scrollTabelKuis;
    private javax.swing.JScrollPane scrollTabelSoal;
    private javax.swing.JScrollPane scrollUtama;
    private javax.swing.JTable tabelKuis;
    private javax.swing.JTable tabelSoal;
    private javax.swing.JTextArea txtAreaKunciEssay;
    private javax.swing.JTextArea txtAreaPertanyaan;
    private javax.swing.JTextField txtBatasWaktu;
    private javax.swing.JTextField txtDeskripsiKuis;
    private javax.swing.JTextField txtJudulKuis;
    private javax.swing.JTextField txtPilihanA;
    private javax.swing.JTextField txtPilihanB;
    private javax.swing.JTextField txtPilihanC;
    private javax.swing.JTextField txtPilihanD;
    private javax.swing.JTextField txtPoinSoal;
    // End of variables declaration//GEN-END:variables
}