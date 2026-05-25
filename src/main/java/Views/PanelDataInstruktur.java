/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views;
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
    /**
     * Creates new form PanelDataStruktur
     */
    public PanelDataInstruktur() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(980, 616));
        setBackground(new java.awt.Color(245, 245, 245));
        
        setupTable();
        loadTableData("");
    }
    
    private void setupTable() {
        // Styling Header Tabel
        tabelInstruktur.getTableHeader().setFont(new Font("Inter", Font.BOLD, 12));
        tabelInstruktur.getTableHeader().setBackground(new Color(122, 0, 0));
        tabelInstruktur.getTableHeader().setForeground(Color.WHITE);
        tabelInstruktur.getTableHeader().setOpaque(false);
        tabelInstruktur.setRowHeight(36);
        tabelInstruktur.setSelectionBackground(new Color(255, 230, 230));

        // Pasang Renderer dan Editor Aksi (Menggunakan index kolom ke-6 karena "Aksi" ada di paling akhir)
        tabelInstruktur.getColumnModel().getColumn(7).setCellRenderer(new TombolAksiRenderer());
        tabelInstruktur.getColumnModel().getColumn(7).setCellEditor(
            new TombolAksiEditor(new TombolAksiEditor.AksiListener() {
                
                @Override
                public void onDetail(int baris) {
                    String id = tabelInstruktur.getValueAt(baris, 1).toString();
                    // Nanti buat FormDetailInstruktur
                    // FormDetailInstruktur detail = new FormDetailInstruktur((java.awt.Frame) SwingUtilities.getWindowAncestor(PanelDataInstruktur.this), true, id);
                    // detail.setVisible(true);
                    JOptionPane.showMessageDialog(PanelDataInstruktur.this, "Menampilkan detail instruktur: " + id);
                }

                @Override
                public void onEdit(int baris) {
                    String id = tabelInstruktur.getValueAt(baris, 1).toString();
                    // Nanti panggil FormEditInstruktur
                    JOptionPane.showMessageDialog(PanelDataInstruktur.this, "Edit instruktur: " + id);
                }

                @Override
                public void onHapus(int baris) {
                    hapusData(baris);
                }
            })
        );
    }

    // Method untuk Load Data ke JTable
    private void loadTableData(String keyword) {
        DefaultTableModel model = (DefaultTableModel) tabelInstruktur.getModel();
        model.setRowCount(0); // Kosongkan tabel sebelum diisi ulang

        int no = 1;
        
        // Memanggil helper method untuk mengambil khusus data Instruktur
        for (Instruktur ins : DataStore.getHanyaInstruktur()) {
            
            // Logika Pencarian (Search)
            boolean match = keyword.isEmpty() || 
                            ins.getNamaLengkap().toLowerCase().contains(keyword.toLowerCase()) || 
                            ins.getIdInstruktur().toLowerCase().contains(keyword.toLowerCase());

            if (match) {
                // Menambahkan baris dengan mengambil data dari Getter
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

    private void hapusData(int baris) {
        String idInstruktur = tabelInstruktur.getValueAt(baris, 1).toString();
        String nama = tabelInstruktur.getValueAt(baris, 2).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus instruktur " + nama + "?", 
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
           
            boolean sukses = DataStore.daftarPengguna.removeIf(p -> 
                p instanceof Instruktur && ((Instruktur) p).getIdInstruktur().equals(idInstruktur)
            );
            
            if (sukses) {
                JOptionPane.showMessageDialog(this, "Data Instruktur Berhasil Dihapus!");
                // Refresh tabel kembali ke keadaan awal
                loadTableData("");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data instruktur!", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
        FormTambahInstruktur form = new FormTambahInstruktur(
            (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), true);
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
