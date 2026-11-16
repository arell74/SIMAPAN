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
import java.awt.Color;
import java.util.Objects;

/**
 * Dashboard ringkasan untuk Peserta: menyapa dengan nama, program & kelasnya, 
 * lalu menampilkan ringkasan materi, kuis, nilai rata-rata, dan status seleksi.
 */
public class PanelDashboardPeserta extends javax.swing.JPanel {

    private String idPesertaAktif;

    /**
     * Creates new form PanelDashboardPeserta
     */
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

            String statusSeleksi = (pesertaAktif.getStatusSeleksi() != null && !pesertaAktif.getStatusSeleksi().trim().isEmpty())
                    ? pesertaAktif.getStatusSeleksi()
                    : "Menunggu";
            lblStatusSeleksi.setText(statusSeleksi);

            if ("Lulus".equalsIgnoreCase(statusSeleksi)) {
                lblStatusSeleksi.setForeground(new Color(20, 120, 40)); // Hijau
            } else if (statusSeleksi.toLowerCase().contains("gugur") || statusSeleksi.toLowerCase().contains("tidak")) {
                lblStatusSeleksi.setForeground(new Color(180, 40, 40)); // Merah
            } else {
                lblStatusSeleksi.setForeground(new Color(200, 100, 0)); // Oranye untuk Menunggu
            }

        } else {
            lblSapaan.setText("Halo!");
            lblInfoKelas.setText("Data peserta tidak ditemukan");
            lblStatusSeleksi.setText("-");
        }

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

        // Hitung Rata-rata Nilai (gabungan nilai seleksi & hasil kuis yang sudah selesai dinilai)
        int totalNilai = 0;
        int jumlahNilai = 0;
        for (Seleksi s : DataStore.daftarSeleksi) {
            if (s.getPeserta() != null && Objects.equals(s.getPeserta().getIdPeserta(), idPesertaAktif)) {
                totalNilai += s.getNilai();
                jumlahNilai++;
            }
        }
        for (HasilKuis h : DataStore.daftarHasilKuis) {
            if (Objects.equals(h.getIdPeserta(), idPesertaAktif)
                    && "Selesai Dinilai".equalsIgnoreCase(h.getStatus())) {
                totalNilai += h.getNilaiAkhir();
                jumlahNilai++;
            }
        }
        lblRataRata.setText(jumlahNilai > 0 ? String.valueOf(Math.round((float) totalNilai / jumlahNilai)) : "-");
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

        // ================= HEADER SAMBUTAN =================
        panelSambutan = new javax.swing.JPanel();
        panelSambutan.setBackground(new java.awt.Color(122, 0, 0));
        panelSambutan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSapaan = new javax.swing.JLabel();
        lblSapaan.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 24));
        lblSapaan.setForeground(new java.awt.Color(255, 255, 255));
        lblSapaan.setText("Selamat Datang!");
        panelSambutan.add(lblSapaan, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 25, 600, 32));

        lblInfoKelas = new javax.swing.JLabel();
        lblInfoKelas.setFont(new java.awt.Font("Inter", 0, 13));
        lblInfoKelas.setForeground(new java.awt.Color(240, 200, 200));
        lblInfoKelas.setText("Program - Kelas - Level");
        panelSambutan.add(lblInfoKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 60, 600, 20));

        add(panelSambutan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 900, 110));

        // ================= KARTU RINGKASAN (4 Kolom) =================
        panelKartuMateri = buatKartuRingkasan("Materi Tersedia");
        lblJumlahMateri = ambilLabelData(panelKartuMateri);
        add(panelKartuMateri, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 210, 110));

        panelKartuKuis = buatKartuRingkasan("Kuis Tertunda");
        lblJumlahKuis = ambilLabelData(panelKartuKuis);
        add(panelKartuKuis, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, 210, 110));

        panelKartuNilai = buatKartuRingkasan("Rata-rata Nilai");
        lblRataRata = ambilLabelData(panelKartuNilai);
        add(panelKartuNilai, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 150, 210, 110));

        panelKartuStatus = buatKartuRingkasanTeks("Status Seleksi");
        lblStatusSeleksi = ambilLabelData(panelKartuStatus);
        add(panelKartuStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 150, 210, 110));

    }// </editor-fold>//GEN-END:initComponents

    private javax.swing.JPanel buatKartuRingkasan(String judul) {
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setBackground(new java.awt.Color(255, 255, 255));
        panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(210, 210, 210), 1, true));
        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.JLabel lblJudul = new javax.swing.JLabel();
        lblJudul.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        lblJudul.setForeground(new java.awt.Color(130, 130, 130));
        lblJudul.setText(judul);
        panel.add(lblJudul, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, 180, 18));

        javax.swing.JLabel lblData = new javax.swing.JLabel();
        lblData.setName("lblData");
        lblData.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 36));
        lblData.setForeground(new java.awt.Color(40, 40, 40));
        lblData.setText("0");
        panel.add(lblData, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 45, 180, 45));

        return panel;
    }

    private javax.swing.JPanel buatKartuRingkasanTeks(String judul) {
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setBackground(new java.awt.Color(255, 255, 255));
        panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(210, 210, 210), 1, true));
        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.JLabel lblJudul = new javax.swing.JLabel();
        lblJudul.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        lblJudul.setForeground(new java.awt.Color(130, 130, 130));
        lblJudul.setText(judul);
        panel.add(lblJudul, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, 180, 18));

        javax.swing.JLabel lblData = new javax.swing.JLabel();
        lblData.setName("lblData");
        lblData.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 16));
        lblData.setForeground(new java.awt.Color(40, 40, 40));
        lblData.setText("-");
        panel.add(lblData, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 45, 180, 45));

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