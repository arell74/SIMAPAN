/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views.peserta;

import DataStore.DataStore;
import Model.HasilKuis;
import Model.Seleksi;
import Util.TabelUtil;
import javax.swing.table.DefaultTableModel;

/**
 * Panel bagi Peserta untuk melihat:
 * 1) Nilai per Materi (hasil seleksi/tes yang diinput Instruktur)
 * 2) Hasil pengerjaan Kuis beserta evaluasi/catatan Instruktur
 */
public class PanelNilaiPeserta extends javax.swing.JPanel {

    private String idPesertaAktif;
    private DefaultTableModel modelTabelNilaiMateri;
    private DefaultTableModel modelTabelHasilKuis;

    public PanelNilaiPeserta(String idPeserta) {
        initComponents();
        this.idPesertaAktif = idPeserta;

        setupTabelNilaiMateri();
        setupTabelHasilKuis();
        
        loadSemuaData();
    }

    private void loadSemuaData() {
        loadNilaiMateri();
        loadHasilKuis();
    }

    private void setupTabelNilaiMateri() {
        tabelNilaiMateri.setRowHeight(34);

        modelTabelNilaiMateri = new DefaultTableModel(
            new Object[][]{},
            new String[]{"No.", "Jenis Seleksi / Materi", "Tanggal", "Nilai", "Status"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelNilaiMateri.setModel(modelTabelNilaiMateri);
        dandaniHeaderTabel(tabelNilaiMateri);
    }

    private void setupTabelHasilKuis() {
        tabelHasilKuis.setRowHeight(34);

        modelTabelHasilKuis = new DefaultTableModel(
            new Object[][]{},
            new String[]{"No.", "Judul Kuis", "Tanggal Dikerjakan", "Skor (Nilai)", "Status", "Catatan Instruktur"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelHasilKuis.setModel(modelTabelHasilKuis);
        dandaniHeaderTabel(tabelHasilKuis);
    }

    private void dandaniHeaderTabel(javax.swing.JTable tabel) {
        tabel.setSelectionBackground(new java.awt.Color(230, 240, 255)); // Biru muda ala macOS
        tabel.setSelectionForeground(new java.awt.Color(0, 50, 150));
        tabel.setShowVerticalLines(false); // Estetika modern
        tabel.setGridColor(new java.awt.Color(240, 240, 240));

        TabelUtil.autoResizeKolom(tabel);
        javax.swing.table.JTableHeader header = tabel.getTableHeader();
        header.setBackground(new java.awt.Color(40, 40, 40));
        header.setForeground(java.awt.Color.WHITE);
        header.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        header.setOpaque(true);
    }

    private void loadNilaiMateri() {
        modelTabelNilaiMateri.setRowCount(0);
        int no = 1;
        int totalNilai = 0;
        int jumlahData = 0;

        for (Seleksi s : DataStore.daftarSeleksi) {
            if (s.getPeserta() != null && s.getPeserta().getIdPeserta() != null
                    && s.getPeserta().getIdPeserta().equals(idPesertaAktif)) {
                
                modelTabelNilaiMateri.addRow(new Object[]{
                    no++,
                    s.getJenisSeleksi(),
                    s.getTanggalSeleksi(),
                    s.getNilai(),
                    s.getStatusHasil()
                });
                
                totalNilai += s.getNilai();
                jumlahData++;
            }
        }

        if (jumlahData > 0) {
            int rataRata = Math.round((float) totalNilai / jumlahData);
            lblRataMateri.setText("Rata-rata Nilai: " + rataRata);
        } else {
            lblRataMateri.setText("Rata-rata Nilai: -");
            modelTabelNilaiMateri.addRow(new Object[]{"-", "Belum ada nilai materi yang diinput instruktur", "-", "-", "-"});
        }
    }

    private void loadHasilKuis() {
        modelTabelHasilKuis.setRowCount(0);
        int no = 1;
        int totalNilaiKuis = 0;
        int jumlahKuis = 0;

        for (HasilKuis h : DataStore.daftarHasilKuis) {
            if (h.getIdPeserta() != null && h.getIdPeserta().equals(idPesertaAktif)) {
                String catatan = h.getCatatanInstruktur();
                if (catatan == null || catatan.isEmpty()) {
                    catatan = "Tidak ada catatan";
                }

                modelTabelHasilKuis.addRow(new Object[]{
                    no++,
                    h.getJudulKuis(),
                    h.getTanggalPengerjaan(),
                    h.getTotalPoinDiperoleh() + " / " + h.getTotalPoinMaksimal() + " (" + h.getNilaiAkhir() + ")",
                    h.getStatus(),
                    catatan
                });
                
                totalNilaiKuis += h.getNilaiAkhir();
                jumlahKuis++;
            }
        }

        if (jumlahKuis > 0) {
            int rataRata = Math.round((float) totalNilaiKuis / jumlahKuis);
            lblRataKuis.setText("Rata-rata Kuis: " + rataRata);
        } else {
            lblRataKuis.setText("Rata-rata Kuis: -");
            modelTabelHasilKuis.addRow(new Object[]{"-", "Belum ada kuis yang dikerjakan", "-", "-", "-", "-"});
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(245, 245, 245));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        // ================= SECTION 1: NILAI MATERI =================
        jLabelNilaiMateri = new javax.swing.JLabel();
        jLabelNilaiMateri.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 14));
        jLabelNilaiMateri.setForeground(new java.awt.Color(50, 50, 50));
        jLabelNilaiMateri.setText("Rekap Nilai Materi / Seleksi");
        add(jLabelNilaiMateri, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 250, 20));

        lblRataMateri = new javax.swing.JLabel();
        lblRataMateri.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        lblRataMateri.setForeground(new java.awt.Color(20, 120, 40)); // Warna hijau positif
        lblRataMateri.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRataMateri.setText("Rata-rata Nilai: 0");
        add(lblRataMateri, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 20, 200, 20));

        panelTabelNilaiMateri = new javax.swing.JPanel();
        panelTabelNilaiMateri.setBackground(new java.awt.Color(255, 255, 255));
        panelTabelNilaiMateri.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelTabelNilaiMateri.setLayout(new java.awt.BorderLayout());

        scrollNilaiMateri = new javax.swing.JScrollPane();
        scrollNilaiMateri.setBorder(null);
        tabelNilaiMateri = new javax.swing.JTable();
        scrollNilaiMateri.setViewportView(tabelNilaiMateri);
        panelTabelNilaiMateri.add(scrollNilaiMateri, java.awt.BorderLayout.CENTER);

        add(panelTabelNilaiMateri, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 900, 220));

        // ================= SECTION 2: HASIL KUIS =================
        jLabelHasilKuis = new javax.swing.JLabel();
        jLabelHasilKuis.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 14));
        jLabelHasilKuis.setForeground(new java.awt.Color(50, 50, 50));
        jLabelHasilKuis.setText("Riwayat Pengerjaan Kuis");
        add(jLabelHasilKuis, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 220, 30));

        lblRataKuis = new javax.swing.JLabel();
        lblRataKuis.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        lblRataKuis.setForeground(new java.awt.Color(20, 120, 40));
        lblRataKuis.setText("Rata-rata Kuis: 0");
        add(lblRataKuis, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 295, 150, 20));

        btnLihatCatatan = new javax.swing.JButton();
        btnLihatCatatan.setBackground(new java.awt.Color(122, 0, 0));
        btnLihatCatatan.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        btnLihatCatatan.setForeground(new java.awt.Color(255, 255, 255));
        btnLihatCatatan.setText("Lihat Catatan Penuh");
        btnLihatCatatan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLihatCatatan.setFocusPainted(false);
        btnLihatCatatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatCatatanActionPerformed(evt);
            }
        });
        add(btnLihatCatatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 290, 160, 32));

        btnRefresh = new javax.swing.JButton();
        btnRefresh.setBackground(new java.awt.Color(220, 220, 220));
        btnRefresh.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        btnRefresh.setForeground(new java.awt.Color(50, 50, 50));
        btnRefresh.setText("Segarkan Data");
        btnRefresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadSemuaData();
            }
        });
        add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 290, 140, 32));

        panelTabelHasilKuis = new javax.swing.JPanel();
        panelTabelHasilKuis.setBackground(new java.awt.Color(255, 255, 255));
        panelTabelHasilKuis.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelTabelHasilKuis.setLayout(new java.awt.BorderLayout());

        scrollHasilKuis = new javax.swing.JScrollPane();
        scrollHasilKuis.setBorder(null);
        tabelHasilKuis = new javax.swing.JTable();
        scrollHasilKuis.setViewportView(tabelHasilKuis);
        panelTabelHasilKuis.add(scrollHasilKuis, java.awt.BorderLayout.CENTER);

        add(panelTabelHasilKuis, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 900, 240));
    }// </editor-fold>//GEN-END:initComponents

    private void btnLihatCatatanActionPerformed(java.awt.event.ActionEvent evt) {
        int baris = tabelHasilKuis.getSelectedRow();
        if (baris == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Pilih dahulu kuis yang ingin dilihat catatannya dari tabel!", "Peringatan",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String judul = modelTabelHasilKuis.getValueAt(baris, 1).toString();
        String catatan = modelTabelHasilKuis.getValueAt(baris, 5).toString();

        if (judul.equals("Belum ada kuis yang dikerjakan")) return; // Proteksi baris kosong

        javax.swing.JTextArea areaCatatan = new javax.swing.JTextArea(catatan);
        areaCatatan.setLineWrap(true);
        areaCatatan.setWrapStyleWord(true);
        areaCatatan.setEditable(false);
        areaCatatan.setFont(new java.awt.Font("Inter", 0, 13));
        areaCatatan.setMargin(new java.awt.Insets(10, 10, 10, 10));

        javax.swing.JScrollPane scrollCatatan = new javax.swing.JScrollPane(areaCatatan);
        scrollCatatan.setPreferredSize(new java.awt.Dimension(400, 200));

        javax.swing.JOptionPane.showMessageDialog(this, scrollCatatan,
            "Evaluasi Instruktur - " + judul, javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLihatCatatan;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel jLabelHasilKuis;
    private javax.swing.JLabel jLabelNilaiMateri;
    private javax.swing.JLabel lblRataKuis;
    private javax.swing.JLabel lblRataMateri;
    private javax.swing.JPanel panelTabelHasilKuis;
    private javax.swing.JPanel panelTabelNilaiMateri;
    private javax.swing.JScrollPane scrollHasilKuis;
    private javax.swing.JScrollPane scrollNilaiMateri;
    private javax.swing.JTable tabelHasilKuis;
    private javax.swing.JTable tabelNilaiMateri;
    // End of variables declaration//GEN-END:variables
}