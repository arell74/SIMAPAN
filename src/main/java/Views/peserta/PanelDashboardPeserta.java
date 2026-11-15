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
import Model.Seleksi;

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
        setBackground(new java.awt.Color(245, 245, 245)); // Background abu-abu muda agar kartu lebih menonjol
        muatRingkasan();
    }

    private Peserta cariPesertaAktif() {
        for (Peserta p : DataStore.daftarPeserta) {
            if (p.getIdPeserta() != null && p.getIdPeserta().equals(idPesertaAktif)) {
                return p;
            }
        }
        return null;
    }

    private void muatRingkasan() {
        Peserta pesertaAktif = cariPesertaAktif();
        String kelasPeserta = "Belum Ditentukan";

        if (pesertaAktif != null) {
            kelasPeserta = pesertaAktif.getKelas();
            
            // Mengambil nama program dari DataStore berdasarkan ID Program yang dimiliki peserta
            String namaProgram = "Program Belum Ditentukan";
            for (Model.Program prg : DataStore.daftarProgram) {
                if (prg.getIdProgram().equals(pesertaAktif.getProgram())) {
                    namaProgram = prg.getNamaProgram();
                    break;
                }
            }

            lblSapaan.setText("Selamat Datang, " + pesertaAktif.getNamaLengkap() + "!");
            lblInfoKelas.setText("Program: " + namaProgram + "  |  Kelas: " + kelasPeserta + "  |  Level: " + pesertaAktif.getLevelBahasa());
            
            // Set Status Seleksi di kartu ke-4
            lblStatusSeleksi.setText(pesertaAktif.getStatusSeleksi());
            
            // Ubah warna teks status berdasarkan kondisinya
            if ("Lulus".equalsIgnoreCase(pesertaAktif.getStatusSeleksi())) {
                lblStatusSeleksi.setForeground(new java.awt.Color(20, 120, 40)); // Hijau
            } else if (pesertaAktif.getStatusSeleksi().contains("Gugur")) {
                lblStatusSeleksi.setForeground(new java.awt.Color(180, 40, 40)); // Merah
            } else {
                lblStatusSeleksi.setForeground(new java.awt.Color(200, 100, 0)); // Oranye untuk Menunggu
            }
            
        } else {
            lblSapaan.setText("Halo!");
            lblInfoKelas.setText("Data peserta tidak ditemukan");
            lblStatusSeleksi.setText("-");
        }

        // Hitung Materi
        int jumlahMateri = 0;
        for (Materi m : DataStore.daftarMateri) {
            if ("Semua Kelas".equals(m.getKelasTarget()) || m.getKelasTarget().equals(kelasPeserta)) {
                jumlahMateri++;
            }
        }
        lblJumlahMateri.setText(String.valueOf(jumlahMateri));

        // Hitung Kuis Belum Dikerjakan
        int jumlahKuisBelum = 0;
        for (Kuis k : DataStore.daftarKuis) {
            boolean untukKelasIni = "Semua Kelas".equals(k.getKelasTarget()) || k.getKelasTarget().equals(kelasPeserta);
            if (untukKelasIni && !sudahDikerjakan(k.getIdKuis())) {
                jumlahKuisBelum++;
            }
        }
        lblJumlahKuis.setText(String.valueOf(jumlahKuisBelum));

        // Hitung Rata-rata Nilai
        int totalNilai = 0;
        int jumlahNilai = 0;
        for (Seleksi s : DataStore.daftarSeleksi) {
            if (s.getPeserta() != null && s.getPeserta().getIdPeserta() != null
                    && s.getPeserta().getIdPeserta().equals(idPesertaAktif)) {
                totalNilai += s.getNilai();
                jumlahNilai++;
            }
        }
        lblRataRata.setText(jumlahNilai > 0 ? String.valueOf(Math.round((float) totalNilai / jumlahNilai)) : "-");
    }

    private boolean sudahDikerjakan(String idKuis) {
        for (HasilKuis h : DataStore.daftarHasilKuis) {
            if (h.getIdKuis().equals(idKuis) && h.getIdPeserta().equals(idPesertaAktif)) {
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
        panelSambutan.setBackground(new java.awt.Color(122, 0, 0)); // Warna identitas LPK
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
        // Lebar total area 900px. Dibagi 4 kartu (lebar 210px) + 3 celah (lebar 20px) = 900px.
        
        // Kartu 1: Materi
        panelKartuMateri = buatKartuRingkasan("Materi Tersedia");
        lblJumlahMateri = ambilLabelData(panelKartuMateri);
        add(panelKartuMateri, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 210, 110));

        // Kartu 2: Kuis
        panelKartuKuis = buatKartuRingkasan("Kuis Tertunda");
        lblJumlahKuis = ambilLabelData(panelKartuKuis);
        // Jika ada kuis tertunda, kita bisa set warnanya jadi merah nanti di logic
        add(panelKartuKuis, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, 210, 110));

        // Kartu 3: Nilai
        panelKartuNilai = buatKartuRingkasan("Rata-rata Nilai");
        lblRataRata = ambilLabelData(panelKartuNilai);
        add(panelKartuNilai, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 150, 210, 110));
        
        // Kartu 4: Status Seleksi
        panelKartuStatus = buatKartuRingkasanTeks("Status Seleksi");
        lblStatusSeleksi = ambilLabelData(panelKartuStatus);
        add(panelKartuStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 150, 210, 110));
        
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Helper untuk membuat kartu berisi ANGKA besar.
     */
    private javax.swing.JPanel buatKartuRingkasan(String judul) {
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setBackground(new java.awt.Color(255, 255, 255));
        // Menggunakan border transparan atau abu-abu halus agar elegan (macOS style)
        panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(210, 210, 210), 1, true));
        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.JLabel lblJudul = new javax.swing.JLabel();
        lblJudul.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        lblJudul.setForeground(new java.awt.Color(130, 130, 130));
        lblJudul.setText(judul);
        panel.add(lblJudul, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, 180, 18));

        javax.swing.JLabel lblData = new javax.swing.JLabel();
        lblData.setName("lblData");
        lblData.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 36)); // Diperbesar sedikit agar lebih gagah
        lblData.setForeground(new java.awt.Color(40, 40, 40)); // Warna abu tua, bukan merah (agar netral)
        lblData.setText("0");
        panel.add(lblData, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 45, 180, 45));

        return panel;
    }
    
    /**
     * Helper untuk membuat kartu berisi TEKS (seperti status), font disesuaikan.
     */
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
        lblData.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 16)); // Font lebih kecil untuk teks
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
    private javax.swing.JLabel lblStatusSeleksi; // Tambahan variabel baru
    private javax.swing.JPanel panelKartuKuis;
    private javax.swing.JPanel panelKartuMateri;
    private javax.swing.JPanel panelKartuNilai;
    private javax.swing.JPanel panelKartuStatus; // Tambahan variabel baru
    private javax.swing.JPanel panelSambutan;
    // End of variables declaration//GEN-END:variables
}