/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views.admin;
import DataStore.DataStore;
import Model.Instruktur;
import Util.TombolAksiEditor;
import Util.TombolAksiRenderer;
import Util.TabelUtil;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author arelssi
 */
public class PanelDataInstruktur extends javax.swing.JPanel {

    public PanelDataInstruktur() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(980, 616));
        setBackground(new java.awt.Color(245, 245, 245));

        setupTable();
        DataStore.muatUlang();
        loadTableData("");
    }

    private void setupTable() {
        tabelInstruktur.getTableHeader().setFont(new Font("Inter", Font.BOLD, 12));
        tabelInstruktur.getTableHeader().setBackground(new Color(122, 0, 0));
        tabelInstruktur.getTableHeader().setForeground(Color.WHITE);
        tabelInstruktur.getTableHeader().setOpaque(false);
        tabelInstruktur.setRowHeight(36);
        tabelInstruktur.setSelectionBackground(new Color(255, 230, 230));

        tabelInstruktur.getColumnModel().getColumn(7).setCellRenderer(new TombolAksiRenderer());
        tabelInstruktur.getColumnModel().getColumn(7).setCellEditor(
            new TombolAksiEditor(new TombolAksiEditor.AksiListener() {

                @Override
                public void onDetail(int baris) {
                    String id = tabelInstruktur.getValueAt(baris, 1).toString();
                    java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(PanelDataInstruktur.this);
                    if (parentWindow instanceof java.awt.Frame) {
                        FormDetailInstruktur form = new FormDetailInstruktur(
                            (java.awt.Frame) parentWindow,
                            true,
                            id
                        );
                        form.setVisible(true);
                    }
                }

                @Override
                public void onEdit(int baris) {
                    String idTarget = tabelInstruktur.getValueAt(baris, 1).toString().trim();
                    java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(PanelDataInstruktur.this);
                    if (parentWindow instanceof java.awt.Frame) {
                        FormEditInstruktur formEdit = new FormEditInstruktur(
                            (java.awt.Frame) parentWindow,
                            true,
                            idTarget,
                            PanelDataInstruktur.this
                        );
                        tampilkanFormDenganRefresh(formEdit);
                    }
                }

                @Override
                public void onHapus(int baris) {
                    hapusData(baris);
                }
            })
        );
    }

    // Refresh cache MySQL & tabel otomatis saat form Tambah/Edit ditutup
    private void tampilkanFormDenganRefresh(java.awt.Window form) {
        form.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                DataStore.muatUlang();
                loadTableData("");
            }
        });
        form.setVisible(true);
    }

    // Method untuk Load Data ke JTable
    public void loadTableData(String keyword) {
        DefaultTableModel model = (DefaultTableModel) tabelInstruktur.getModel();
        model.setRowCount(0);

        int no = 1;

        for (Instruktur ins : DataStore.getHanyaInstruktur()) {
            boolean match = keyword.isEmpty() ||
                            ins.getNamaLengkap().toLowerCase().contains(keyword.toLowerCase()) ||
                            ins.getIdInstruktur().toLowerCase().contains(keyword.toLowerCase());

            if (match) {
                model.addRow(new Object[]{
                    no++,
                    ins.getIdInstruktur(),
                    ins.getNamaLengkap(),
                    ins.getSpesialisasi(),
                    ins.getLevelJlpt(),
                    ins.getNoTelp(),
                    ins.getKelasDiampu(),
                    ""
                });
            }
        }

        lblTotalData.setText("Menampilkan " + model.getRowCount() + " data Instruktur");

        try {
            Util.TabelUtil.autoResizeKolom(tabelInstruktur);
        } catch (Exception e) {
            System.out.println("TabelUtil belum tersedia: " + e.getMessage());
        }
    }

    // Hapus data instruktur dari MySQL via PenggunaDAO
    private void hapusData(int baris) {
        String idInstruktur = tabelInstruktur.getValueAt(baris, 1).toString();
        String nama = tabelInstruktur.getValueAt(baris, 2).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus instruktur " + nama + "?\nAkun login instruktur ini juga akan ikut terhapus.",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (Database.PenggunaDAO.hapusInstruktur(idInstruktur)) {
                DataStore.muatUlang();
                JOptionPane.showMessageDialog(this, "Data Instruktur Berhasil Dihapus!");
                loadTableData("");
            }
            // Jika gagal (misal masih membimbing peserta), popup error sudah ditampilkan oleh DAO
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelToolbar = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        panelTabel = new javax.swing.JPanel();
        scrollTabel = new javax.swing.JScrollPane();
        tabelInstruktur = new javax.swing.JTable();
        lblTotalData = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelToolbar.setBackground(new java.awt.Color(255, 255, 255));
        panelToolbar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/search.png")); // NOI18N
        jLabel3.setText("C");
        jLabel3.setPreferredSize(new java.awt.Dimension(24, 28));
        panelToolbar.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 14, -1, -1));

        txtCari.setPreferredSize(new java.awt.Dimension(280, 30));
        panelToolbar.add(txtCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 14, 270, -1));

        btnCari.setBackground(new java.awt.Color(80, 80, 80));
        btnCari.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        btnCari.setForeground(new java.awt.Color(255, 255, 255));
        btnCari.setText("Cari");
        btnCari.setPreferredSize(new java.awt.Dimension(70, 30));
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });
        panelToolbar.add(btnCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(334, 14, -1, -1));

        btnReset.setBackground(new java.awt.Color(240, 240, 240));
        btnReset.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(80, 80, 80));
        btnReset.setText("Reset");
        btnReset.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(210, 210, 210)));
        btnReset.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReset.setFocusPainted(false);
        btnReset.setPreferredSize(new java.awt.Dimension(70, 30));
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        panelToolbar.add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(412, 14, -1, -1));

        btnTambah.setBackground(new java.awt.Color(122, 0, 0));
        btnTambah.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(255, 255, 255));
        btnTambah.setText("Tambah Instruktur");
        btnTambah.setPreferredSize(new java.awt.Dimension(148, 30));
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });
        panelToolbar.add(btnTambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 14, -1, -1));

        jSeparator2.setPreferredSize(new java.awt.Dimension(980, 2));
        panelToolbar.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 54, -1, -1));

        jSeparator3.setPreferredSize(new java.awt.Dimension(980, 2));
        panelToolbar.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        add(panelToolbar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelTabel.setBackground(new java.awt.Color(255, 255, 255));
        panelTabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelTabel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollTabel.setPreferredSize(new java.awt.Dimension(948, 460));

        tabelInstruktur.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        tabelInstruktur.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "ID Instruktur", "Nama Instruktur", "Spesialisasi", "Level JLPT", "No. Telp", "Kelas Diampu", "Aksi"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tabelInstruktur.setRowHeight(36);
        tabelInstruktur.setSelectionBackground(new java.awt.Color(255, 230, 230));
        tabelInstruktur.setSelectionForeground(new java.awt.Color(122, 0, 0));
        tabelInstruktur.setShowHorizontalLines(true);
        scrollTabel.setViewportView(tabelInstruktur);

        panelTabel.add(scrollTabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        add(panelTabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, 450));

        lblTotalData.setFont(new java.awt.Font("Inter", 0, 11)); // NOI18N
        lblTotalData.setForeground(new java.awt.Color(120, 120, 120));
        lblTotalData.setText("Menampilkan 0 data Instruktur");
        lblTotalData.setPreferredSize(new java.awt.Dimension(300, 20));
        add(lblTotalData, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
         loadTableData(txtCari.getText());
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
         txtCari.setText("");
        loadTableData("");
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        FormTambahInstruktur form = new FormTambahInstruktur(PanelDataInstruktur.this);
        form.setVisible(true);
    }//GEN-LAST:event_btnTambahActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnTambah;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblTotalData;
    private javax.swing.JPanel panelTabel;
    private javax.swing.JPanel panelToolbar;
    private javax.swing.JScrollPane scrollTabel;
    private javax.swing.JTable tabelInstruktur;
    private javax.swing.JTextField txtCari;
    // End of variables declaration//GEN-END:variables
}
