/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views.admin;

import DataStore.DataStore;
import Model.Dokumen;
import Util.TabelUtil;
import Util.TombolAksiEditor;
import Util.TombolAksiRenderer;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author arelssi
 */
public class PanelDokumenBerangkat extends javax.swing.JPanel {

    public PanelDokumenBerangkat() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(980, 616));
        setupComboBoxFilter();
        setupTable();

        // Sinkronkan cache DataStore dengan data terbaru dari MySQL
        DataStore.muatUlang();
        loadTableData("");
        loadStatistics();
    }

    private void setupComboBoxFilter() {
        cbFilter.removeAllItems();
        cbFilter.addItem("Semua");
        cbFilter.addItem("Siap Berangkat");
        cbFilter.addItem("Menunggu Dokumen");
        cbFilter.addItem("Belum Berangkat");

        cbFilter.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (cbFilter.getSelectedItem() != null) {
                    loadTableData("");
                }
            }
        });
    }

    private void setupTable() {
        tabelDokumen.getTableHeader().setFont(new Font("Inter", Font.BOLD, 12));
        tabelDokumen.getTableHeader().setBackground(new Color(122, 0, 0));
        tabelDokumen.getTableHeader().setForeground(Color.WHITE);
        tabelDokumen.setRowHeight(36);

        // Pasang Renderer dan Editor di kolom Aksi (Indeks ke-8)
        tabelDokumen.getColumnModel().getColumn(8).setCellRenderer(new TombolAksiRenderer());
        tabelDokumen.getColumnModel().getColumn(8).setCellEditor(new TombolAksiEditor(new TombolAksiEditor.AksiListener() {

            @Override
            public void onDetail(int baris) {
                String id = tabelDokumen.getValueAt(baris, 1).toString();
                java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(PanelDokumenBerangkat.this);
                if (parentWindow instanceof java.awt.Frame) {
                    FormDetailDokumen form = new FormDetailDokumen((java.awt.Frame) parentWindow, true, id);
                    form.setVisible(true);
                }
            }

            @Override
            public void onEdit(int baris) {
                String idDokumen = tabelDokumen.getValueAt(baris, 1).toString();

                Dokumen dokumenTerpilih = null;
                for (Dokumen dok : DataStore.daftarDokumen) {
                    if (dok.getIdDokumen().equals(idDokumen)) {
                        dokumenTerpilih = dok;
                        break;
                    }
                }

                if (dokumenTerpilih != null) {
                   FormEditDokumen form = new FormEditDokumen(PanelDokumenBerangkat.this, dokumenTerpilih);
                   tampilkanFormDenganRefresh(form);
                }
            }

            @Override
            public void onHapus(int baris) {
                hapusData(baris);
            }
        }));
    }

    // Refresh cache MySQL, tabel, dan kartu statistik saat form input/edit ditutup
    private void tampilkanFormDenganRefresh(javax.swing.JFrame form) {
        form.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                DataStore.muatUlang();
                loadTableData("");
                loadStatistics();
            }
        });
        form.setVisible(true);
    }

    private void hapusData(int baris) {
        String idDokumen = tabelDokumen.getValueAt(baris, 1).toString();
        String namaPeserta = tabelDokumen.getValueAt(baris, 3).toString();

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Yakin ingin menghapus data dokumen (" + idDokumen + ") milik " + namaPeserta + "?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (Database.DokumenDAO.hapus(idDokumen)) {
                DataStore.muatUlang();
                JOptionPane.showMessageDialog(this, "Data dokumen berhasil dihapus!");
                loadTableData("");
                loadStatistics();
            }
            // Jika gagal, popup error sudah ditangani oleh DokumenDAO / Koneksi
        }
    }

    private String kalkulasiKelengkapan(Model.Dokumen dok) {
        int jumlahLengkap = 0;

        if (dok.getNoPaspor() != null && !dok.getNoPaspor().trim().isEmpty() && !dok.getNoPaspor().equals("-")) jumlahLengkap++;
        if (dok.getNikKtp() != null && !dok.getNikKtp().trim().isEmpty() && !dok.getNikKtp().equals("-")) jumlahLengkap++;
        if (dok.getHasilMcu() != null && !dok.getHasilMcu().trim().isEmpty() && !dok.getHasilMcu().equals("-")) jumlahLengkap++;
        if (dok.getNoVisa() != null && !dok.getNoVisa().trim().isEmpty() && !dok.getNoVisa().equals("-")) jumlahLengkap++;

        if (jumlahLengkap == 4) {
            return "Lengkap";
        } else {
            return jumlahLengkap + "/4 Berkas";
        }
    }

    // --- LOGIKA MENAMPILKAN & MENCARI DATA ---
    public void loadTableData(String keyword) {
        DefaultTableModel model = (DefaultTableModel) tabelDokumen.getModel();
        model.setRowCount(0);

        String filterStatus = (cbFilter.getSelectedItem() != null)
                ? cbFilter.getSelectedItem().toString()
                : "Semua";

        int no = 1;
        for (Dokumen dok : DataStore.daftarDokumen) {
            if (dok.getPeserta() == null) continue;

            String idDok = dok.getIdDokumen();
            String idPeserta = dok.getPeserta().getIdPeserta();
            String namaPeserta = dok.getPeserta().getNamaLengkap();
            String statusBerangkat = (dok.getPeserta().getStatusKeberangkatan() != null)
                    ? dok.getPeserta().getStatusKeberangkatan()
                    : "Belum Berangkat";
            String tglBerangkat = (dok.getTanggalBerangkat() != null && !dok.getTanggalBerangkat().trim().isEmpty())
                    ? dok.getTanggalBerangkat()
                    : "-";

            boolean textMatch = keyword.isEmpty() ||
                                idDok.toLowerCase().contains(keyword.toLowerCase()) ||
                                idPeserta.toLowerCase().contains(keyword.toLowerCase()) ||
                                namaPeserta.toLowerCase().contains(keyword.toLowerCase());

            boolean statusMatch = filterStatus.equals("Semua") ||
                                  statusBerangkat.equalsIgnoreCase(filterStatus);

            if (textMatch && statusMatch) {
                model.addRow(new Object[]{
                    no++,
                    idDok,
                    idPeserta,
                    namaPeserta,
                    dok.getPeserta().getProgram(),
                    kalkulasiKelengkapan(dok),
                    tglBerangkat,
                    statusBerangkat,
                    "" // Kolom Aksi
                });
            }
        }
        lblTotalData.setText("Menampilkan " + model.getRowCount() + " data peserta");

        try {
            TabelUtil.autoResizeKolom(tabelDokumen);
        } catch (Exception e) {
            System.out.println("TabelUtil error/belum ada: " + e.getMessage());
        }
    }

    // --- LOGIKA MENGHITUNG KARTU STATISTIK ---
    public void loadStatistics() {
        int totalLulus = 0;
        int dokumenLengkap = 0;
        int siapBerangkat = 0;

        for (Model.Peserta p : DataStore.daftarPeserta) {
            if ("Lulus".equalsIgnoreCase(p.getStatusSeleksi())) {
                totalLulus++;
            }
        }

        for (Model.Dokumen dok : DataStore.daftarDokumen) {
            if ("Lengkap".equals(kalkulasiKelengkapan(dok))) {
                dokumenLengkap++;
            }

            if (dok.getPeserta() != null && "Siap Berangkat".equalsIgnoreCase(dok.getPeserta().getStatusKeberangkatan())) {
                siapBerangkat++;
            }
        }

        lblTotalLulus.setText(String.valueOf(totalLulus));
        lblDokumenLengkap.setText(String.valueOf(dokumenLengkap));
        lblSiapBerangkat.setText(String.valueOf(siapBerangkat));
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        kartuTotal2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblSiapBerangkat = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        pnlToolbar = new javax.swing.JPanel();
        txtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        cbFilter = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        kartuTotal = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        lblTotalLulus = new javax.swing.JLabel();
        kartuTotal1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        lblDokumenLengkap = new javax.swing.JLabel();
        PanelDokumenKeberangkatan = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelDokumen = new javax.swing.JTable();
        lblTotalData = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        kartuTotal2.setBackground(new java.awt.Color(255, 255, 255));
        kartuTotal2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        kartuTotal2.setPreferredSize(new java.awt.Dimension(296, 56));
        kartuTotal2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setForeground(new java.awt.Color(130, 130, 130));
        jLabel5.setText("Siap Berangkat");
        kartuTotal2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        lblSiapBerangkat.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        lblSiapBerangkat.setForeground(new java.awt.Color(200, 0, 0));
        lblSiapBerangkat.setText("0");
        kartuTotal2.add(lblSiapBerangkat, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, -1, -1));

        jPanel3.setBackground(new java.awt.Color(255, 235, 235));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/airplane.png")); // NOI18N
        jLabel9.setText("jLabel8");
        jLabel9.setPreferredSize(new java.awt.Dimension(32, 32));
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, -1, -1));

        kartuTotal2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 20, 60, 60));

        add(kartuTotal2, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 30, -1, 100));

        pnlToolbar.setBackground(new java.awt.Color(255, 255, 255));
        pnlToolbar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        pnlToolbar.setPreferredSize(new java.awt.Dimension(980, 56));
        pnlToolbar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariActionPerformed(evt);
            }
        });
        pnlToolbar.add(txtCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 34));

        btnCari.setBackground(new java.awt.Color(80, 80, 80));
        btnCari.setForeground(new java.awt.Color(255, 255, 255));
        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });
        pnlToolbar.add(btnCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, -1, 34));

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        pnlToolbar.add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, 90, 34));

        cbFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua" }));
        cbFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFilterActionPerformed(evt);
            }
        });
        pnlToolbar.add(cbFilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 20, 170, 34));

        jButton3.setBackground(new java.awt.Color(122, 0, 0));
        jButton3.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Input Dokumen");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        pnlToolbar.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 20, -1, 34));

        add(pnlToolbar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 156, 930, 70));

        kartuTotal.setBackground(new java.awt.Color(255, 255, 255));
        kartuTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        kartuTotal.setPreferredSize(new java.awt.Dimension(296, 56));
        kartuTotal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setForeground(new java.awt.Color(130, 130, 130));
        jLabel1.setText("Total Lulus Seleksi");
        kartuTotal.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        jPanel1.setBackground(new java.awt.Color(255, 235, 235));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/check.png")); // NOI18N
        jLabel10.setText("jLabel8");
        jLabel10.setPreferredSize(new java.awt.Dimension(32, 32));
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, -1, -1));

        kartuTotal.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 60, 60));

        lblTotalLulus.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        lblTotalLulus.setForeground(new java.awt.Color(0, 140, 60));
        lblTotalLulus.setText("0");
        kartuTotal.add(lblTotalLulus, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, -1, -1));

        add(kartuTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, 100));

        kartuTotal1.setBackground(new java.awt.Color(255, 255, 255));
        kartuTotal1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        kartuTotal1.setPreferredSize(new java.awt.Dimension(296, 56));
        kartuTotal1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setForeground(new java.awt.Color(130, 130, 130));
        jLabel3.setText("Dokumen");
        kartuTotal1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        jPanel2.setBackground(new java.awt.Color(255, 235, 235));
        jPanel2.setPreferredSize(new java.awt.Dimension(60, 60));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/exam.png")); // NOI18N
        jLabel12.setText("jLabel8");
        jLabel12.setPreferredSize(new java.awt.Dimension(32, 32));
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, -1, -1));

        kartuTotal1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        lblDokumenLengkap.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        lblDokumenLengkap.setForeground(new java.awt.Color(122, 0, 0));
        lblDokumenLengkap.setText("0");
        kartuTotal1.add(lblDokumenLengkap, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, -1, -1));

        add(kartuTotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(345, 30, -1, 100));

        PanelDokumenKeberangkatan.setBackground(new java.awt.Color(255, 255, 255));
        PanelDokumenKeberangkatan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        PanelDokumenKeberangkatan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabelDokumen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "ID Dokumen", "ID Peserta", "Nama Peserta", "Program", "No. Paspor", "Tgl Berangkat", "Status Keberangkatan", "Aksi"
            }
        ));
        jScrollPane1.setViewportView(tabelDokumen);
        if (tabelDokumen.getColumnModel().getColumnCount() > 0) {
            tabelDokumen.getColumnModel().getColumn(0).setMaxWidth(50);
            tabelDokumen.getColumnModel().getColumn(8).setMinWidth(90);
        }

        PanelDokumenKeberangkatan.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 37, 928, 332));

        lblTotalData.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        lblTotalData.setForeground(new java.awt.Color(120, 120, 120));
        lblTotalData.setText("Menampilkan 0 data seleksi");
        PanelDokumenKeberangkatan.add(lblTotalData, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 10, -1, -1));

        add(PanelDokumenKeberangkatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 225, 930, 370));
    }// </editor-fold>//GEN-END:initComponents

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        txtCari.setText("");
        cbFilter.setSelectedIndex(0);
        loadTableData("");
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        loadTableData(txtCari.getText());
    }//GEN-LAST:event_btnCariActionPerformed

    private void cbFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFilterActionPerformed
        loadTableData(txtCari.getText());
    }//GEN-LAST:event_cbFilterActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int barisTerpilih = tabelDokumen.getSelectedRow();
        
        if (barisTerpilih == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Silakan klik/pilih salah satu data peserta di tabel terlebih dahulu!", "Peringatan", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String idDokumen = tabelDokumen.getValueAt(barisTerpilih, 1).toString();
        
        Dokumen dokumenTerpilih = null;
        for (Dokumen dok : DataStore.daftarDokumen) {
            if (dok.getIdDokumen().equals(idDokumen)) {
                dokumenTerpilih = dok;
                break;
            }
        }
        if (dokumenTerpilih != null) {
            FormInputDokumen form = new FormInputDokumen(PanelDokumenBerangkat.this, dokumenTerpilih);
            form.setVisible(true);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Aduh, data dokumen tidak ditemukan di memori!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelDokumenKeberangkatan;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<String> cbFilter;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel kartuTotal;
    private javax.swing.JPanel kartuTotal1;
    private javax.swing.JPanel kartuTotal2;
    private javax.swing.JLabel lblDokumenLengkap;
    private javax.swing.JLabel lblSiapBerangkat;
    private javax.swing.JLabel lblTotalData;
    private javax.swing.JLabel lblTotalLulus;
    private javax.swing.JPanel pnlToolbar;
    private javax.swing.JTable tabelDokumen;
    private javax.swing.JTextField txtCari;
    // End of variables declaration//GEN-END:variables
}
