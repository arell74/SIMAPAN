/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views.peserta;

import DataStore.DataStore;
import Model.HasilKuis;
import Model.Kuis;
import Model.Materi;
import Model.Peserta;
import Model.Program;
import Model.Seleksi;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.JPanel;

/**
 * Dashboard ringkasan untuk Peserta dengan visualisasi Java 2D:
 * Banner gradasi, Timeline status pemagangan, Gauge rata-rata nilai, dan Grafik perkembangan nilai.
 */
public class PanelDashboardPeserta extends javax.swing.JPanel {

    private String idPesertaAktif;

    // Komponen Kustom Java 2D
    private TimelinePemagangan2D panelTimeline2D;
    private GaugeNilai2D panelGauge2D;
    private GrafikNilai2D panelGrafik2D;

    public PanelDashboardPeserta(String idPeserta) {
        initComponents();
        this.idPesertaAktif = idPeserta;
        setBackground(new Color(245, 245, 245));
        muatRingkasan();
    }

    private Peserta cariPesertaAktif() {
        for (Peserta p : DataStore.daftarPeserta) {
            if ((p.getIdPeserta() != null && p.getIdPeserta().equals(idPesertaAktif))
                    || (p.getUsername() != null && p.getUsername().equals(idPesertaAktif))) {
                return p;
            }
        }
        return null;
    }

    private void muatRingkasan() {
        DataStore.muatUlang();

        Peserta pesertaAktif = cariPesertaAktif();
        String kelasPeserta = "Belum Ditentukan";
        String statusSeleksi = "Menunggu";
        String statusKeberangkatan = "Belum Berangkat";

        if (pesertaAktif != null) {
            this.idPesertaAktif = pesertaAktif.getIdPeserta();
            if (pesertaAktif.getKelas() != null && !pesertaAktif.getKelas().trim().isEmpty()) {
                kelasPeserta = pesertaAktif.getKelas();
            }

            String namaProgram = "Program Belum Ditentukan";
            for (Program prg : DataStore.daftarProgram) {
                if (Objects.equals(prg.getIdProgram(), pesertaAktif.getProgram())) {
                    namaProgram = prg.getNamaProgram();
                    break;
                }
            }

            lblSapaan.setText("Selamat Datang, " + pesertaAktif.getNamaLengkap() + "!");
            lblInfoKelas.setText("Program: " + namaProgram + "  |  Kelas: " + kelasPeserta + "  |  Level: " + pesertaAktif.getLevelBahasa());

            statusSeleksi = (pesertaAktif.getStatusSeleksi() != null && !pesertaAktif.getStatusSeleksi().trim().isEmpty())
                    ? pesertaAktif.getStatusSeleksi()
                    : "Menunggu";
            statusKeberangkatan = (pesertaAktif.getStatusKeberangkatan() != null && !pesertaAktif.getStatusKeberangkatan().trim().isEmpty())
                    ? pesertaAktif.getStatusKeberangkatan()
                    : "Belum Berangkat";

            lblStatusSeleksi.setText(statusSeleksi);

            if ("Lulus".equalsIgnoreCase(statusSeleksi)) {
                lblStatusSeleksi.setForeground(new Color(20, 120, 40));
            } else if (statusSeleksi.toLowerCase().contains("gugur") || statusSeleksi.toLowerCase().contains("tidak")) {
                lblStatusSeleksi.setForeground(new Color(180, 40, 40));
            } else {
                lblStatusSeleksi.setForeground(new Color(200, 100, 0));
            }

        } else {
            lblSapaan.setText("Halo!");
            lblInfoKelas.setText("Data peserta tidak ditemukan");
            lblStatusSeleksi.setText("-");
        }

        // Update Java 2D Timeline Status Pemagangan
        panelTimeline2D.setStatusPeserta(statusSeleksi, statusKeberangkatan);

        // Hitung Materi sesuai kelas peserta
        int jumlahMateri = 0;
        for (Materi m : DataStore.daftarMateri) {
            String target = m.getKelasTarget();
            if ("Semua Kelas".equalsIgnoreCase(target) || Objects.equals(kelasPeserta, target)) {
                jumlahMateri++;
            }
        }
        lblJumlahMateri.setText(String.valueOf(jumlahMateri));

        // Hitung Kuis Belum Dikerjakan
        int jumlahKuisBelum = 0;
        for (Kuis k : DataStore.daftarKuis) {
            String target = k.getKelasTarget();
            boolean untukKelasIni = "Semua Kelas".equalsIgnoreCase(target) || Objects.equals(kelasPeserta, target);
            if (untukKelasIni && !sudahDikerjakan(k.getIdKuis())) {
                jumlahKuisBelum++;
            }
        }
        lblJumlahKuis.setText(String.valueOf(jumlahKuisBelum));
        lblJumlahKuis.setForeground(jumlahKuisBelum > 0 ? new Color(180, 40, 40) : new Color(40, 40, 40));

        // Hitung Rata-rata Nilai & Kumpulkan Data untuk Grafik Java 2D
        int totalNilai = 0;
        int jumlahNilai = 0;
        List<ItemNilai> riwayatNilai = new ArrayList<>();

        for (Seleksi s : DataStore.daftarSeleksi) {
            if (s.getPeserta() != null && Objects.equals(s.getPeserta().getIdPeserta(), idPesertaAktif)) {
                totalNilai += s.getNilai();
                jumlahNilai++;
                riwayatNilai.add(new ItemNilai(ringkasLabel(s.getJenisSeleksi()), s.getNilai()));
            }
        }
        for (HasilKuis h : DataStore.daftarHasilKuis) {
            if (Objects.equals(h.getIdPeserta(), idPesertaAktif)
                    && "Selesai Dinilai".equalsIgnoreCase(h.getStatus())) {
                int skor = h.getNilaiAkhir();
                totalNilai += skor;
                jumlahNilai++;
                riwayatNilai.add(new ItemNilai(ringkasLabel(h.getJudulKuis()), skor));
            }
        }

        int rataRata = jumlahNilai > 0 ? Math.round((float) totalNilai / jumlahNilai) : 0;
        lblRataRata.setText(jumlahNilai > 0 ? String.valueOf(rataRata) : "-");

        // Update Java 2D Gauge & Grafik
        panelGauge2D.setData(rataRata, jumlahNilai > 0);
        panelGrafik2D.setData(riwayatNilai);
    }

    private String ringkasLabel(String teks) {
        if (teks == null || teks.trim().isEmpty()) {
            return "Tes";
        }
        return teks.length() > 11 ? teks.substring(0, 10) + "…" : teks;
    }

    private boolean sudahDikerjakan(String idKuis) {
        for (HasilKuis h : DataStore.daftarHasilKuis) {
            if (Objects.equals(h.getIdKuis(), idKuis) && Objects.equals(h.getIdPeserta(), idPesertaAktif)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        // ================= 1. HEADER SAMBUTAN (JAVA 2D GRADIENT & MOTIF) =================
        panelSambutan = new BannerSambutan2D();
        panelSambutan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSapaan = new javax.swing.JLabel();
        lblSapaan.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 24));
        lblSapaan.setForeground(new java.awt.Color(255, 255, 255));
        lblSapaan.setText("Selamat Datang!");
        panelSambutan.add(lblSapaan, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 24, 650, 32));

        lblInfoKelas = new javax.swing.JLabel();
        lblInfoKelas.setFont(new java.awt.Font("Inter", 0, 13));
        lblInfoKelas.setForeground(new java.awt.Color(245, 210, 210));
        lblInfoKelas.setText("Program - Kelas - Level");
        panelSambutan.add(lblInfoKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 60, 650, 20));

        add(panelSambutan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 900, 108));

        // ================= 2. KARTU RINGKASAN (4 Kolom) =================
        panelKartuMateri = buatKartuRingkasan("Materi Tersedia");
        lblJumlahMateri = ambilLabelData(panelKartuMateri);
        add(panelKartuMateri, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 145, 210, 105));

        panelKartuKuis = buatKartuRingkasan("Kuis Tertunda");
        lblJumlahKuis = ambilLabelData(panelKartuKuis);
        add(panelKartuKuis, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 145, 210, 105));

        panelKartuNilai = buatKartuRingkasan("Rata-rata Nilai");
        lblRataRata = ambilLabelData(panelKartuNilai);
        add(panelKartuNilai, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 145, 210, 105));

        panelKartuStatus = buatKartuRingkasanTeks("Status Seleksi");
        lblStatusSeleksi = ambilLabelData(panelKartuStatus);
        add(panelKartuStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 145, 210, 105));

        // ================= 3. STEPPER TIMELINE PEMAGANGAN (JAVA 2D) =================
        panelTimeline2D = new TimelinePemagangan2D();
        add(panelTimeline2D, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 268, 900, 105));

        // ================= 4. GAUGE SKOR & GRAFIK NILAI (JAVA 2D) =================
        panelGauge2D = new GaugeNilai2D();
        add(panelGauge2D, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 290, 220));

        panelGrafik2D = new GrafikNilai2D();
        add(panelGrafik2D, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 390, 590, 220));

    }// </editor-fold>//GEN-END:initComponents

    private javax.swing.JPanel buatKartuRingkasan(String judul) {
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setBackground(new java.awt.Color(255, 255, 255));
        panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220), 1, true));
        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.JLabel lblJudul = new javax.swing.JLabel();
        lblJudul.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        lblJudul.setForeground(new java.awt.Color(130, 130, 130));
        lblJudul.setText(judul);
        panel.add(lblJudul, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 14, 180, 18));

        javax.swing.JLabel lblData = new javax.swing.JLabel();
        lblData.setName("lblData");
        lblData.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 34));
        lblData.setForeground(new java.awt.Color(40, 40, 40));
        lblData.setText("0");
        panel.add(lblData, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 42, 180, 45));

        return panel;
    }

    private javax.swing.JPanel buatKartuRingkasanTeks(String judul) {
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setBackground(new java.awt.Color(255, 255, 255));
        panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220), 1, true));
        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.JLabel lblJudul = new javax.swing.JLabel();
        lblJudul.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        lblJudul.setForeground(new java.awt.Color(130, 130, 130));
        lblJudul.setText(judul);
        panel.add(lblJudul, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 14, 180, 18));

        javax.swing.JLabel lblData = new javax.swing.JLabel();
        lblData.setName("lblData");
        lblData.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 16));
        lblData.setForeground(new java.awt.Color(40, 40, 40));
        lblData.setText("-");
        panel.add(lblData, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 42, 180, 45));

        return panel;
    }

    private javax.swing.JLabel ambilLabelData(javax.swing.JPanel panelKartu) {
        for (java.awt.Component c : panelKartu.getComponents()) {
            if (c instanceof javax.swing.JLabel && "lblData".equals(c.getName())) {
                return (javax.swing.JLabel) c;
            }
        }
        return new javax.swing.JLabel();
    }

    // =========================================================================
    // INNER CLASS JAVA 2D #1: BANNER SAMBUTAN BERGRADASI
    // =========================================================================
    private static class BannerSambutan2D extends JPanel {

        public BannerSambutan2D() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Gradasi marun LPK Yuuki
            GradientPaint gp = new GradientPaint(0, 0, new Color(122, 0, 0), w, h, new Color(69, 10, 10));
            g2.setPaint(gp);
            g2.fill(new RoundRectangle2D.Double(0, 0, w, h, 16, 16));

            // Ornamen lingkaran transparan ala Hinomaru Jepang di kanan
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.09f));
            g2.setColor(Color.WHITE);
            g2.fill(new Ellipse2D.Double(w - 150, -35, 180, 180));
            g2.fill(new Ellipse2D.Double(w - 220, 35, 120, 120));

            g2.dispose();
        }
    }

    // =========================================================================
    // INNER CLASS JAVA 2D #2: STEPPER TIMELINE STATUS PEMAGANGAN
    // =========================================================================
    private static class TimelinePemagangan2D extends JPanel {

        private int tahapAktif = 2;
        private String keteranganStatus = "Menunggu Hasil Seleksi";

        public TimelinePemagangan2D() {
            setOpaque(false);
        }

        public void setStatusPeserta(String statusSeleksi, String statusKeberangkatan) {
            if ("Sudah Berangkat".equalsIgnoreCase(statusKeberangkatan) || "Berangkat".equalsIgnoreCase(statusKeberangkatan)) {
                tahapAktif = 4;
                keteranganStatus = "Berangkat ke Jepang";
            } else if ("Lulus".equalsIgnoreCase(statusSeleksi)) {
                tahapAktif = 3;
                keteranganStatus = "Lulus Seleksi — Tahap Pelatihan";
            } else if (statusSeleksi != null && (statusSeleksi.toLowerCase().contains("tidak") || statusSeleksi.toLowerCase().contains("gugur"))) {
                tahapAktif = 2;
                keteranganStatus = "Evaluasi Ulang Seleksi";
            } else {
                tahapAktif = 2;
                keteranganStatus = "Tahap Seleksi (" + (statusSeleksi != null ? statusSeleksi : "Menunggu") + ")";
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Kartu dasar
            g2.setColor(Color.WHITE);
            g2.fill(new RoundRectangle2D.Double(0, 0, w - 1, h - 1, 14, 14));
            g2.setColor(new Color(220, 220, 220));
            g2.draw(new RoundRectangle2D.Double(0, 0, w - 1, h - 1, 14, 14));

            // Judul kiri atas
            g2.setFont(new Font("Inter", Font.BOLD, 12));
            g2.setColor(new Color(60, 60, 60));
            g2.drawString("Alur Progres Pemagangan Jepang", 18, 22);

            // Badge status kanan atas
            g2.setFont(new Font("Inter", Font.BOLD, 11));
            g2.setColor(new Color(122, 0, 0));
            int statW = g2.getFontMetrics().stringWidth(keteranganStatus);
            g2.drawString(keteranganStatus, w - statW - 18, 22);

            String[] tahap = {"1. Registrasi", "2. Tes Seleksi", "3. Pelatihan Kelas", "4. Keberangkatan"};
            int marginX = 95;
            int yTitik = 54;
            int jarak = (w - (marginX * 2)) / (tahap.length - 1);

            // Garis penghubung
            for (int i = 0; i < tahap.length - 1; i++) {
                int x1 = marginX + (i * jarak);
                int x2 = marginX + ((i + 1) * jarak);
                g2.setColor((i + 1 < tahapAktif) ? new Color(20, 120, 40) : new Color(210, 215, 220));
                g2.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x1, yTitik, x2, yTitik);
            }

            // Lingkaran tahap & label
            int r = 13;
            g2.setFont(new Font("Inter", Font.BOLD, 11));
            FontMetrics fm = g2.getFontMetrics();

            for (int i = 0; i < tahap.length; i++) {
                int cx = marginX + (i * jarak);
                int step = i + 1;

                if (step < tahapAktif) {
                    g2.setColor(new Color(20, 120, 40));
                } else if (step == tahapAktif) {
                    g2.setColor(new Color(122, 0, 0, 40));
                    g2.fill(new Ellipse2D.Double(cx - r - 4, yTitik - r - 4, (r + 4) * 2, (r + 4) * 2));
                    g2.setColor(new Color(122, 0, 0));
                } else {
                    g2.setColor(new Color(160, 165, 175));
                }

                g2.fill(new Ellipse2D.Double(cx - r, yTitik - r, r * 2, r * 2));

                g2.setColor(Color.WHITE);
                String ikon = (step < tahapAktif) ? "✓" : String.valueOf(step);
                g2.drawString(ikon, cx - (fm.stringWidth(ikon) / 2), yTitik + 4);

                g2.setColor(step <= tahapAktif ? new Color(40, 40, 40) : new Color(130, 130, 130));
                g2.drawString(tahap[i], cx - (fm.stringWidth(tahap[i]) / 2), yTitik + 30);
            }

            g2.dispose();
        }
    }

    // =========================================================================
    // INNER CLASS JAVA 2D #3: RADIAL GAUGE RATA-RATA NILAI
    // =========================================================================
    private static class GaugeNilai2D extends JPanel {

        private int nilai = 0;
        private boolean adaData = false;

        public GaugeNilai2D() {
            setOpaque(false);
        }

        public void setData(int nilai, boolean adaData) {
            this.nilai = Math.max(0, Math.min(100, nilai));
            this.adaData = adaData;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Kartu dasar
            g2.setColor(Color.WHITE);
            g2.fill(new RoundRectangle2D.Double(0, 0, w - 1, h - 1, 14, 14));
            g2.setColor(new Color(220, 220, 220));
            g2.draw(new RoundRectangle2D.Double(0, 0, w - 1, h - 1, 14, 14));

            // Judul kartu
            g2.setFont(new Font("Inter", Font.BOLD, 12));
            g2.setColor(new Color(60, 60, 60));
            g2.drawString("Indeks Capaian Akademik", 18, 24);

            int size = 122;
            int x = (w - size) / 2;
            int y = 44;

            // Track lingkaran abu-abu
            g2.setStroke(new BasicStroke(12f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(230, 232, 236));
            g2.draw(new Arc2D.Double(x, y, size, size, 0, 360, Arc2D.OPEN));

            // Busur progres nilai
            Color warnaBusur = !adaData ? new Color(180, 180, 180)
                    : (nilai >= 75) ? new Color(20, 120, 40)
                    : (nilai >= 70) ? new Color(122, 0, 0) : new Color(210, 110, 0);
            g2.setColor(warnaBusur);
            double sudut = -(nilai * 360.0 / 100.0);
            g2.draw(new Arc2D.Double(x, y, size, size, 90, sudut, Arc2D.OPEN));

            // Angka tengah
            g2.setFont(new Font("Inter", Font.BOLD, 26));
            g2.setColor(new Color(30, 30, 30));
            String teksSkor = adaData ? String.valueOf(nilai) : "-";
            FontMetrics fmSkor = g2.getFontMetrics();
            g2.drawString(teksSkor, (w - fmSkor.stringWidth(teksSkor)) / 2, y + (size / 2) + 6);

            g2.setFont(new Font("Inter", Font.PLAIN, 10));
            g2.setColor(new Color(120, 120, 120));
            String sub = "dari 100";
            g2.drawString(sub, (w - g2.getFontMetrics().stringWidth(sub)) / 2, y + (size / 2) + 22);

            // Keterangan KKM di bawah
            String predikat = !adaData ? "Belum ada nilai tercatat"
                    : (nilai >= 70 ? "Memenuhi KKM (≥ 70)" : "Perlu Peningkatan (< 70)");
            g2.setFont(new Font("Inter", Font.BOLD, 11));
            g2.setColor(warnaBusur);
            g2.drawString(predikat, (w - g2.getFontMetrics().stringWidth(predikat)) / 2, h - 18);

            g2.dispose();
        }
    }

    // =========================================================================
    // INNER CLASS JAVA 2D #4: GRAFIK BATANG PERKEMBANGAN NILAI & GARIS KKM 70
    // =========================================================================
    private static class ItemNilai {
        final String label;
        final int skor;

        ItemNilai(String label, int skor) {
            this.label = label;
            this.skor = Math.max(0, Math.min(100, skor));
        }
    }

    private static class GrafikNilai2D extends JPanel {

        private List<ItemNilai> data = new ArrayList<>();

        public GrafikNilai2D() {
            setOpaque(false);
        }

        public void setData(List<ItemNilai> data) {
            this.data = (data != null) ? data : new ArrayList<>();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Kartu dasar
            g2.setColor(Color.WHITE);
            g2.fill(new RoundRectangle2D.Double(0, 0, w - 1, h - 1, 14, 14));
            g2.setColor(new Color(220, 220, 220));
            g2.draw(new RoundRectangle2D.Double(0, 0, w - 1, h - 1, 14, 14));

            // Judul grafik
            g2.setFont(new Font("Inter", Font.BOLD, 12));
            g2.setColor(new Color(60, 60, 60));
            g2.drawString("Grafik Perkembangan Nilai Seleksi & Kuis", 18, 24);

            // Legenda KKM kanan atas
            g2.setFont(new Font("Inter", Font.BOLD, 10));
            g2.setColor(new Color(200, 50, 50));
            String legKkm = "--- Batas KKM (70)";
            g2.drawString(legKkm, w - g2.getFontMetrics().stringWidth(legKkm) - 18, 24);

            int padLeft = 42;
            int padRight = 24;
            int padTop = 44;
            int padBottom = 34;
            int areaW = w - padLeft - padRight;
            int areaH = h - padTop - padBottom;

            // Grid horizontal 0, 50, 100
            g2.setFont(new Font("Inter", Font.PLAIN, 9));
            int[] ticks = {0, 50, 100};
            for (int t : ticks) {
                int yGrid = padTop + areaH - (int) ((t / 100.0) * areaH);
                g2.setColor(new Color(235, 238, 242));
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(padLeft, yGrid, padLeft + areaW, yGrid);

                g2.setColor(new Color(140, 140, 140));
                g2.drawString(String.valueOf(t), 16, yGrid + 3);
            }

            // Garis putus-putus KKM 70
            int yKkm = padTop + areaH - (int) ((70 / 100.0) * areaH);
            float[] dash = {5f, 5f};
            g2.setColor(new Color(210, 60, 60, 180));
            g2.setStroke(new BasicStroke(1.3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash, 0f));
            g2.drawLine(padLeft, yKkm, padLeft + areaW, yKkm);

            if (data.isEmpty()) {
                g2.setFont(new Font("Inter", Font.ITALIC, 12));
                g2.setColor(new Color(140, 140, 140));
                String kosong = "Belum ada riwayat nilai seleksi atau kuis yang selesai dinilai.";
                g2.drawString(kosong, padLeft + (areaW - g2.getFontMetrics().stringWidth(kosong)) / 2, padTop + (areaH / 2));
                g2.dispose();
                return;
            }

            // Maksimal 7 data terbaru agar batang tetap proporsional
            int mulai = Math.max(0, data.size() - 7);
            List<ItemNilai> tampil = data.subList(mulai, data.size());

            int n = tampil.size();
            int slotW = areaW / n;
            int barW = Math.min(44, Math.max(20, slotW - 22));

            for (int i = 0; i < n; i++) {
                ItemNilai item = tampil.get(i);
                int barH = (int) ((item.skor / 100.0) * areaH);
                int xBar = padLeft + (i * slotW) + ((slotW - barW) / 2);
                int yBar = padTop + areaH - barH;

                Color atas = (item.skor >= 70) ? new Color(122, 0, 0) : new Color(217, 119, 6);
                Color bawah = (item.skor >= 70) ? new Color(175, 40, 40) : new Color(245, 158, 11);
                g2.setPaint(new GradientPaint(xBar, yBar, atas, xBar, yBar + barH, bawah));
                g2.fill(new RoundRectangle2D.Double(xBar, yBar, barW, Math.max(barH, 4), 8, 8));

                // Angka nilai di atas batang
                g2.setFont(new Font("Inter", Font.BOLD, 10));
                g2.setColor(new Color(30, 30, 30));
                String sSkor = String.valueOf(item.skor);
                g2.drawString(sSkor, xBar + (barW - g2.getFontMetrics().stringWidth(sSkor)) / 2, yBar - 5);

                // Label bawah sumbu X
                g2.setFont(new Font("Inter", Font.PLAIN, 9));
                g2.setColor(new Color(90, 90, 90));
                g2.drawString(item.label, xBar + (barW - g2.getFontMetrics().stringWidth(item.label)) / 2, padTop + areaH + 16);
            }

            g2.dispose();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblInfoKelas;
    private javax.swing.JLabel lblJumlahKuis;
    private javax.swing.JLabel lblJumlahMateri;
    private javax.swing.JLabel lblRataRata;
    private javax.swing.JLabel lblSapaan;
    private javax.swing.JLabel lblStatusSeleksi;
    private javax.swing.JPanel panelKartuKuis;
    private javax.swing.JPanel panelKartuMateri;
    private javax.swing.JPanel panelKartuNilai;
    private javax.swing.JPanel panelKartuStatus;
    private javax.swing.JPanel panelSambutan;
    // End of variables declaration//GEN-END:variables
}