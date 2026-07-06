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
 * (dan belum pernah dikerjakan), lalu mengerjakannya. Soal Pilihan Ganda
 * langsung dikoreksi otomatis; soal Essay disimpan menunggu penilaian manual
 * dari Instruktur.
 *
 * @author arelssi
 */
public class PanelKerjakanKuis extends javax.swing.JPanel {

    private static final String KARTU_DAFTAR = "DAFTAR";
    private static final String KARTU_KERJAKAN = "KERJAKAN";

    private String idPesertaAktif;
    private DefaultTableModel modelTabelDaftar;
    private ArrayList<Kuis> kuisTampil = new ArrayList<>();

    private Kuis kuisSedangDikerjakan;
    private ArrayList<Object> daftarInputJawaban = new ArrayList<>();

    /**
     * Creates new form PanelKerjakanKuis
     */
    public PanelKerjakanKuis(String idPeserta) {
        initComponents();
        this.idPesertaAktif = idPeserta;

        setupTabelDaftar();
        loadDaftarKuis();
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
        tabelDaftarKuis.setSelectionBackground(new java.awt.Color(255, 230, 230));
        tabelDaftarKuis.setSelectionForeground(new java.awt.Color(122, 0, 0));

        TabelUtil.autoResizeKolom(tabelDaftarKuis);
        javax.swing.table.JTableHeader header = tabelDaftarKuis.getTableHeader();
        header.setBackground(new java.awt.Color(122, 0, 0));
        header.setForeground(java.awt.Color.WHITE);
        header.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 13));
        header.setOpaque(true);
    }

    private void loadDaftarKuis() {
        modelTabelDaftar.setRowCount(0);
        kuisTampil.clear();

        Peserta pesertaAktif = cariPesertaAktif();
        String kelasPeserta = (pesertaAktif != null) ? pesertaAktif.getKelas() : "";

        int no = 1;
        for (Kuis k : DataStore.daftarKuis) {
            boolean untukKelasIni = "Semua Kelas".equals(k.getKelasTarget()) || k.getKelasTarget().equals(kelasPeserta);
            if (untukKelasIni && !sudahDikerjakan(k.getIdKuis())) {
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
    }

    private void bangunFormSoal(Kuis kuis) {
        lblJudulKerjakan.setText(kuis.getJudulKuis());

        String deskripsi = (kuis.getDeskripsi() == null || kuis.getDeskripsi().isEmpty())
                ? "Tidak ada deskripsi tambahan." : kuis.getDeskripsi();
        String infoWaktu = kuis.getBatasWaktuMenit() == 0 ? "Tanpa batas waktu" : (kuis.getBatasWaktuMenit() + " menit");
        lblDeskripsiKerjakan.setText("<html>" + deskripsi + "<br/>Batas waktu: " + infoWaktu + "</html>");

        panelDaftarSoalDinamis.removeAll();
        daftarInputJawaban.clear();
        panelDaftarSoalDinamis.setLayout(new javax.swing.BoxLayout(panelDaftarSoalDinamis, javax.swing.BoxLayout.Y_AXIS));

        int nomor = 1;
        for (Soal soal : kuis.getDaftarSoal()) {
            javax.swing.JPanel panelSatuSoal = buatPanelSatuSoal(nomor, soal);
            panelSatuSoal.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
            panelSatuSoal.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, panelSatuSoal.getPreferredSize().height));
            panelDaftarSoalDinamis.add(panelSatuSoal);
            panelDaftarSoalDinamis.add(javax.swing.Box.createVerticalStrut(12));
            nomor++;
        }

        panelDaftarSoalDinamis.revalidate();
        panelDaftarSoalDinamis.repaint();
        scrollSoal.getVerticalScrollBar().setValue(0);
    }

    /**
     * Membuat 1 blok soal (nomor + pertanyaan + area jawaban), dan mendaftarkan
     * komponen inputnya (ButtonGroup untuk PG, JTextArea untuk Essay) ke
     * daftarInputJawaban supaya bisa dibaca lagi saat submit.
     */
    private javax.swing.JPanel buatPanelSatuSoal(int nomor, Soal soal) {
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setBackground(new java.awt.Color(255, 255, 255));
        panel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)),
            javax.swing.BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.JLabel lblPertanyaan = new javax.swing.JLabel(
            "<html><b>Soal " + nomor + "</b> (" + soal.getPoin() + " poin) - " + soal.getPertanyaan() + "</html>");
        lblPertanyaan.setFont(new java.awt.Font("Inter", 0, 13));
        lblPertanyaan.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        panel.add(lblPertanyaan);
        panel.add(javax.swing.Box.createVerticalStrut(8));

        if (soal.isPilihanGanda()) {
            ButtonGroup grup = new ButtonGroup();

            String[] label = {"A", "B", "C", "D"};
            String[] isiPilihan = {soal.getPilihanA(), soal.getPilihanB(), soal.getPilihanC(), soal.getPilihanD()};

            for (int i = 0; i < label.length; i++) {
                javax.swing.JRadioButton radio = new javax.swing.JRadioButton(label[i] + ". " + isiPilihan[i]);
                radio.setActionCommand(label[i]);
                radio.setBackground(new java.awt.Color(255, 255, 255));
                radio.setFont(new java.awt.Font("Inter", 0, 12));
                radio.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                grup.add(radio);
                panel.add(radio);
            }

            daftarInputJawaban.add(grup);

        } else {
            javax.swing.JTextArea areaJawaban = new javax.swing.JTextArea(4, 40);
            areaJawaban.setLineWrap(true);
            areaJawaban.setWrapStyleWord(true);
            areaJawaban.setFont(new java.awt.Font("Inter", 0, 12));

            javax.swing.JScrollPane scrollJawaban = new javax.swing.JScrollPane(areaJawaban);
            scrollJawaban.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
            scrollJawaban.setPreferredSize(new java.awt.Dimension(860, 90));
            scrollJawaban.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 90));
            panel.add(scrollJawaban);

            daftarInputJawaban.add(areaJawaban);
        }

        return panel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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
        jLabelJudulDaftar.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 13));
        jLabelJudulDaftar.setForeground(new java.awt.Color(70, 70, 70));
        jLabelJudulDaftar.setText("Kuis yang Belum Dikerjakan");
        panelDaftar.add(jLabelJudulDaftar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, -1, 20));

        btnKerjakan = new javax.swing.JButton();
        btnKerjakan.setBackground(new java.awt.Color(122, 0, 0));
        btnKerjakan.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 11));
        btnKerjakan.setForeground(new java.awt.Color(255, 255, 255));
        btnKerjakan.setText("Kerjakan Kuis Terpilih");
        btnKerjakan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnKerjakan.setFocusPainted(false);
        btnKerjakan.setPreferredSize(new java.awt.Dimension(170, 28));
        btnKerjakan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKerjakanActionPerformed(evt);
            }
        });
        panelDaftar.add(btnKerjakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 10, 170, 28));

        panelTabelDaftar = new javax.swing.JPanel();
        panelTabelDaftar.setBackground(new java.awt.Color(255, 255, 255));
        panelTabelDaftar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelTabelDaftar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollTabelDaftar = new javax.swing.JScrollPane();
        tabelDaftarKuis = new javax.swing.JTable();
        tabelDaftarKuis.setFont(new java.awt.Font("Inter", 0, 12));
        scrollTabelDaftar.setViewportView(tabelDaftarKuis);
        panelTabelDaftar.add(scrollTabelDaftar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelDaftar.add(panelTabelDaftar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 45, 920, 540));

        panelKartuUtama.add(panelDaftar, KARTU_DAFTAR);

        // ===================== KARTU KERJAKAN KUIS =====================
        panelKerjakan = new javax.swing.JPanel();
        panelKerjakan.setBackground(new java.awt.Color(245, 245, 245));
        panelKerjakan.setLayout(new java.awt.BorderLayout());

        panelInfoKerjakan = new javax.swing.JPanel();
        panelInfoKerjakan.setBackground(new java.awt.Color(255, 255, 255));
        panelInfoKerjakan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelInfoKerjakan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelInfoKerjakan.setPreferredSize(new java.awt.Dimension(940, 90));

        btnKembali = new javax.swing.JButton();
        btnKembali.setBackground(new java.awt.Color(230, 230, 230));
        btnKembali.setFont(new java.awt.Font("Inter", 0, 11));
        btnKembali.setText("< Kembali");
        btnKembali.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnKembali.setFocusPainted(false);
        btnKembali.setPreferredSize(new java.awt.Dimension(100, 26));
        btnKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliActionPerformed(evt);
            }
        });
        panelInfoKerjakan.add(btnKembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 12, 100, 26));

        lblJudulKerjakan = new javax.swing.JLabel();
        lblJudulKerjakan.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 15));
        lblJudulKerjakan.setForeground(new java.awt.Color(40, 40, 40));
        lblJudulKerjakan.setText("Judul Kuis");
        panelInfoKerjakan.add(lblJudulKerjakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 780, 22));

        lblDeskripsiKerjakan = new javax.swing.JLabel();
        lblDeskripsiKerjakan.setFont(new java.awt.Font("Inter", 0, 11));
        lblDeskripsiKerjakan.setForeground(new java.awt.Color(130, 130, 130));
        lblDeskripsiKerjakan.setText("Deskripsi");
        panelInfoKerjakan.add(lblDeskripsiKerjakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 34, 780, 40));

        panelKerjakan.add(panelInfoKerjakan, java.awt.BorderLayout.NORTH);

        panelDaftarSoalDinamis = new javax.swing.JPanel();
        panelDaftarSoalDinamis.setBackground(new java.awt.Color(245, 245, 245));

        scrollSoal = new javax.swing.JScrollPane();
        scrollSoal.setBorder(null);
        scrollSoal.setViewportView(panelDaftarSoalDinamis);
        scrollSoal.getVerticalScrollBar().setUnitIncrement(16);
        panelKerjakan.add(scrollSoal, java.awt.BorderLayout.CENTER);

        panelBawahKerjakan = new javax.swing.JPanel();
        panelBawahKerjakan.setBackground(new java.awt.Color(255, 255, 255));
        panelBawahKerjakan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelBawahKerjakan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelBawahKerjakan.setPreferredSize(new java.awt.Dimension(940, 60));

        btnSubmitJawaban = new javax.swing.JButton();
        btnSubmitJawaban.setBackground(new java.awt.Color(20, 120, 40));
        btnSubmitJawaban.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 13));
        btnSubmitJawaban.setForeground(new java.awt.Color(255, 255, 255));
        btnSubmitJawaban.setText("Submit Jawaban");
        btnSubmitJawaban.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSubmitJawaban.setFocusPainted(false);
        btnSubmitJawaban.setPreferredSize(new java.awt.Dimension(200, 36));
        btnSubmitJawaban.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitJawabanActionPerformed(evt);
            }
        });
        panelBawahKerjakan.add(btnSubmitJawaban, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 12, 200, 36));

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
                "Masih ada " + jumlahBelumDijawab + " soal yang belum dijawab.\nTetap submit sekarang?",
                "Konfirmasi", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE);
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
        String pesanHasil = "Jawaban berhasil disubmit!\n\n"
            + "Skor Pilihan Ganda (otomatis): " + hasilBaru.getTotalPoinDiperoleh()
            + " / " + hasilBaru.getTotalPoinMaksimal() + " poin";
        if (jumlahEssayMenunggu > 0) {
            pesanHasil += "\n\n" + jumlahEssayMenunggu + " soal Essay menunggu penilaian dari Instruktur.";
        }

        javax.swing.JOptionPane.showMessageDialog(this, pesanHasil,
            "Kuis Selesai", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        kuisSedangDikerjakan = null;
        loadDaftarKuis();

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

    // Variables declaration
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnKerjakan;
    private javax.swing.JButton btnSubmitJawaban;
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
    // End of variables declaration
}