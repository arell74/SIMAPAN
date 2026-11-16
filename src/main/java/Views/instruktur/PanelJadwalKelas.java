/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views.instruktur;

import DataStore.DataStore;
import Database.JadwalDAO;
import Model.Instruktur;
import Model.Jadwal;
import Model.Pengguna;
import Util.TabelUtil;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author arelssi
 */
public class PanelJadwalKelas extends javax.swing.JPanel {

    private String idInstrukturAktif;
    private int mingguAktif = 0;
    private boolean sedangUpdateNavigasi = false;
    private final List<Jadwal> jadwalTampil = new ArrayList<>();

    public PanelJadwalKelas(String idInstruktur) {
        initComponents();

        this.idInstrukturAktif = idInstruktur;

        setupTable();
        updateNavigasiMinggu();
    }

    private void setupTable() {
        tabelJadwal.setRowHeight(40);
        javax.swing.table.JTableHeader header = tabelJadwal.getTableHeader();

        header.setBackground(new java.awt.Color(122, 0, 0));
        header.setForeground(java.awt.Color.WHITE);
        header.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 13));
        header.setOpaque(true);

        // Klik ganda pada baris jadwal untuk mengubah status (Terjadwal <-> Selesai) ke MySQL
        tabelJadwal.setToolTipText("Klik ganda pada baris untuk mengubah status jadwal");
        tabelJadwal.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int baris = tabelJadwal.getSelectedRow();
                    if (baris >= 0 && baris < jadwalTampil.size()) {
                        ubahStatusJadwal(jadwalTampil.get(baris));
                    }
                }
            }
        });
    }

    private void ubahStatusJadwal(Jadwal jadwal) {
        String statusSekarang = jadwal.getStatusJadwal();
        String statusBaru = "Selesai".equalsIgnoreCase(statusSekarang) ? "Terjadwal" : "Selesai";

        int konfirmasi = JOptionPane.showConfirmDialog(
            this,
            "Ubah status materi \"" + jadwal.getNamaMateri() + "\" (" + jadwal.getHari() + ") menjadi \"" + statusBaru + "\"?",
            "Konfirmasi Status Jadwal",
            JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            jadwal.setStatusJadwal(statusBaru);
            if (JadwalDAO.ubah(jadwal)) {
                loadTableData();
            } else {
                DataStore.muatUlang();
            }
        }
    }

    private void loadTableData() {
        DataStore.muatUlang();

        DefaultTableModel model = (DefaultTableModel) tabelJadwal.getModel();
        model.setRowCount(0);
        jadwalTampil.clear();

        // 1. Cari ID Instruktur Asli (mendukung input berupa ID Instruktur maupun Username)
        String idAsli = this.idInstrukturAktif;
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

        int no = 1;

        // 2. Looping data jadwal milik instruktur aktif dari cache MySQL
        for (Jadwal j : DataStore.daftarJadwal) {
            if (j.getIdInstruktur() != null && j.getIdInstruktur().equals(idAsli)) {
                jadwalTampil.add(j);
                model.addRow(new Object[]{
                    String.valueOf(no++),
                    j.getJamMulai(),
                    j.getHari(),
                    j.getNamaMateri(),
                    "Kelas " + j.getNamaKelas(),
                    j.getDurasi() + " Menit",
                    j.getStatusJadwal()
                });
            }
        }

        String labelMinggu = (cbMinggu.getSelectedItem() != null) ? cbMinggu.getSelectedItem().toString() : "Minggu Ini";
        lblTotalData.setText("Detail Jadwal " + labelMinggu + " (" + model.getRowCount() + " jadwal · klik ganda baris untuk ubah status)");

        TabelUtil.autoResizeKolom(tabelJadwal);
    }

    private void updateNavigasiMinggu() {
        LocalDate acuan = LocalDate.now().plusWeeks(mingguAktif);
        LocalDate senin = acuan.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sabtu = senin.plusDays(5);

        int mingguKe = acuan.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
        lblMingguKe.setText("Minggu ke-" + mingguKe);

        Locale localeId = new Locale("id", "ID");
        DateTimeFormatter fmtHari = DateTimeFormatter.ofPattern("d", localeId);
        DateTimeFormatter fmtBulan = DateTimeFormatter.ofPattern("d MMM", localeId);

        if (senin.getMonth() == sabtu.getMonth()) {
            lblTanggalBanner.setText(senin.format(fmtHari) + " - " + sabtu.format(fmtBulan));
        } else {
            lblTanggalBanner.setText(senin.format(fmtBulan) + " - " + sabtu.format(fmtBulan));
        }

        sedangUpdateNavigasi = true;
        try {
            if (mingguAktif == 0) {
                cbMinggu.setSelectedItem("Minggu Ini");
            } else if (mingguAktif == 1) {
                cbMinggu.setSelectedItem("Minggu Depan");
            } else if (mingguAktif == -1) {
                cbMinggu.setSelectedItem("Minggu Lalu");
            } else {
                cbMinggu.setSelectedIndex(-1);
            }
        } finally {
            sedangUpdateNavigasi = false;
        }

        loadTableData();
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelTabel = new javax.swing.JPanel();
        scrollTabel = new javax.swing.JScrollPane();
        tabelJadwal = new javax.swing.JTable();
        lblTotalData = new javax.swing.JLabel();
        panelKartu1 = new javax.swing.JPanel();
        lblTanggalBanner = new javax.swing.JLabel();
        lblMingguKe = new javax.swing.JLabel();
        btnNext = new javax.swing.JButton();
        cbMinggu = new javax.swing.JComboBox<>();
        btnPrev = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelTabel.setBackground(new java.awt.Color(255, 255, 255));
        panelTabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelTabel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollTabel.setPreferredSize(new java.awt.Dimension(948, 460));

        tabelJadwal.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        tabelJadwal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "Jam", "Hari", "Materi", "Kelas", "Durasi", "Status Jadwal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tabelJadwal.setRowHeight(36);
        tabelJadwal.setSelectionBackground(new java.awt.Color(255, 230, 230));
        tabelJadwal.setSelectionForeground(new java.awt.Color(122, 0, 0));
        tabelJadwal.setShowHorizontalLines(true);
        scrollTabel.setViewportView(tabelJadwal);

        panelTabel.add(scrollTabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        add(panelTabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, -1, 370));

        lblTotalData.setFont(new java.awt.Font("Inter", 0, 11)); // NOI18N
        lblTotalData.setForeground(new java.awt.Color(120, 120, 120));
        lblTotalData.setText("Detail Jadwal Minggu Ini");
        lblTotalData.setPreferredSize(new java.awt.Dimension(300, 20));
        add(lblTotalData, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        panelKartu1.setBackground(new java.awt.Color(122, 0, 0));
        panelKartu1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelKartu1.setPreferredSize(new java.awt.Dimension(210, 100));
        panelKartu1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTanggalBanner.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        lblTanggalBanner.setForeground(new java.awt.Color(255, 255, 255));
        lblTanggalBanner.setText("12 - 16 Mei");
        lblTanggalBanner.setPreferredSize(new java.awt.Dimension(100, 36));
        panelKartu1.add(lblTanggalBanner, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 230, -1));

        lblMingguKe.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        lblMingguKe.setForeground(new java.awt.Color(240, 170, 170));
        lblMingguKe.setText("Minggu ke-20");
        lblMingguKe.setPreferredSize(new java.awt.Dimension(140, 16));
        panelKartu1.add(lblMingguKe, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 45, 260, -1));

        btnNext.setBackground(new java.awt.Color(255, 200, 200));
        btnNext.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        btnNext.setForeground(new java.awt.Color(122, 0, 0));
        btnNext.setText(">");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });
        panelKartu1.add(btnNext, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 30, 50, 40));

        cbMinggu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Minggu Ini", "Minggu Depan", "Minggu Lalu" }));
        cbMinggu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMingguActionPerformed(evt);
            }
        });
        panelKartu1.add(cbMinggu, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 30, 120, 40));

        btnPrev.setBackground(new java.awt.Color(255, 200, 200));
        btnPrev.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        btnPrev.setForeground(new java.awt.Color(122, 0, 0));
        btnPrev.setText("<");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });
        panelKartu1.add(btnPrev, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 50, 40));

        add(panelKartu1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 950, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        mingguAktif++; 
        updateNavigasiMinggu(); 
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        mingguAktif--;
        updateNavigasiMinggu();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void cbMingguActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMingguActionPerformed
        if (sedangUpdateNavigasi || cbMinggu.getSelectedItem() == null) {
            return;
        }

        String pilihan = cbMinggu.getSelectedItem().toString();
        if (pilihan.equals("Minggu Ini")) {
            mingguAktif = 0;
        } else if (pilihan.equals("Minggu Depan")) {
            mingguAktif = 1;
        } else if (pilihan.equals("Minggu Lalu")) {
            mingguAktif = -1;
        }

        updateNavigasiMinggu();
    }//GEN-LAST:event_cbMingguActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JComboBox<String> cbMinggu;
    private javax.swing.JLabel lblMingguKe;
    private javax.swing.JLabel lblTanggalBanner;
    private javax.swing.JLabel lblTotalData;
    private javax.swing.JPanel panelKartu1;
    private javax.swing.JPanel panelTabel;
    private javax.swing.JScrollPane scrollTabel;
    private javax.swing.JTable tabelJadwal;
    // End of variables declaration//GEN-END:variables
}
