/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views.admin;

import DataStore.DataStore;
import Model.Peserta;
import Util.TombolAksiEditor;
import Util.TombolAksiRenderer;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author arelssi
 */
public class PanelDataPeserta extends javax.swing.JPanel {

    /**
     * Creates new form DataPeserta
     */

     public PanelDataPeserta() {
        initComponents();
        setupTable();
        loadTableData(""); 
    }
 
    private void setupTable() {
        tabelPeserta.getTableHeader().setFont(new Font("Inter", Font.BOLD, 12));
        tabelPeserta.getTableHeader().setBackground(new Color(122, 0, 0));
        tabelPeserta.getTableHeader().setForeground(Color.WHITE);
        tabelPeserta.setRowHeight(36);
        
        tabelPeserta.getColumnModel().getColumn(6).setCellRenderer(new TombolAksiRenderer());
        tabelPeserta.getColumnModel().getColumn(6).setCellEditor(new TombolAksiEditor(new TombolAksiEditor.AksiListener() {
            @Override
            public void onDetail(int baris) {
                String id = tabelPeserta.getValueAt(baris, 1).toString();
        
                java.awt.Window parentWindow = javax.swing.SwingUtilities.getWindowAncestor(PanelDataPeserta.this);
                if (parentWindow instanceof java.awt.Frame) {
                    FormDetailPeserta detailForm = new FormDetailPeserta((java.awt.Frame) parentWindow, true, id);
                    detailForm.setVisible(true);
                }
            }
 
            @Override
            public void onEdit(int baris) {
                String id = tabelPeserta.getValueAt(baris, 1).toString();
        
                java.awt.Window parentWindow = javax.swing.SwingUtilities.getWindowAncestor(PanelDataPeserta.this);
                
                if (parentWindow instanceof java.awt.Frame) {
                    FormEditPeserta editForm = new FormEditPeserta((java.awt.Frame) parentWindow, true, id);
                    // DIUBAH: tabel di-refresh SAAT form ditutup (bukan langsung setelah setVisible)
                    tampilkanFormDenganRefresh(editForm);
                }
            }
 
            @Override
            public void onHapus(int baris) {
                hapusData(baris);
            }
        }));
    }
 
    // BARU: membuka form (JFrame non-modal) lalu me-refresh tabel ketika form itu ditutup.
    // Dipakai oleh tombol Edit dan tombol Tambah.
    private void tampilkanFormDenganRefresh(javax.swing.JFrame form) {
        form.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                loadTableData(txtCari.getText());
            }
        });
        form.setVisible(true);
    }
 
    public void loadTableData(String keyword) {
        DefaultTableModel model = (DefaultTableModel) tabelPeserta.getModel();
        model.setRowCount(0);
 
        int no = 1;
        
        for (Peserta p : DataStore.daftarPeserta) {
            boolean match = keyword.isEmpty() || 
                            p.getNamaLengkap().toLowerCase().contains(keyword.toLowerCase()) || 
                            p.getIdPeserta().toLowerCase().contains(keyword.toLowerCase());
            String namaIns = (p.getInstrukturDamping() != null) ? p.getInstrukturDamping().getNamaLengkap() : "-";
            if (match) {
                model.addRow(new Object[]{
                    no++,
                    p.getIdPeserta(),
                    p.getNamaLengkap(),
                    p.getNik(),
                    p.getProgram(),
                    p.getStatusSeleksi(),
                    namaIns,
                    ""
                });
            }
        }
 
        lblTotalData.setText("Menampilkan " + model.getRowCount() + " data peserta");
        
        try {
            Util.TabelUtil.autoResizeKolom(tabelPeserta);
        } catch (Exception e) {
            System.out.println("TabelUtil error/belum ada: " + e.getMessage());
        }
    }
 
    // DIUBAH: menghapus lewat DAO (MySQL), bukan removeIf() pada list di memori
    private void hapusData(int baris) {
        String idPeserta = tabelPeserta.getValueAt(baris, 1).toString();
        String nama = tabelPeserta.getValueAt(baris, 2).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Yakin ingin menghapus data " + nama + "?\nData seleksi dan dokumen peserta ini juga ikut terhapus.", 
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (Database.PesertaDAO.hapus(idPeserta)) {
                DataStore.muatUlang();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                loadTableData(txtCari.getText());
            }
            // kalau gagal, popup error sudah ditampilkan oleh DAO
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
        txtCari = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        btnEkspor = new javax.swing.JButton();
        btnImpor = new javax.swing.JButton();
        panelTabel = new javax.swing.JPanel();
        scrollTabel = new javax.swing.JScrollPane();
        tabelPeserta = new javax.swing.JTable();
        lblTotalData = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelToolbar.setBackground(new java.awt.Color(255, 255, 255));
        panelToolbar.setPreferredSize(new java.awt.Dimension(980, 56));
        panelToolbar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtCari.setPreferredSize(new java.awt.Dimension(280, 30));
        txtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariActionPerformed(evt);
            }
        });
        panelToolbar.add(txtCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 14, 270, -1));

        jButton1.setBackground(new java.awt.Color(80, 80, 80));
        jButton1.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Cari");
        jButton1.setPreferredSize(new java.awt.Dimension(70, 30));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        panelToolbar.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(334, 14, -1, -1));

        jButton2.setBackground(new java.awt.Color(240, 240, 240));
        jButton2.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(80, 80, 80));
        jButton2.setText("Reset");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(210, 210, 210)));
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setFocusPainted(false);
        jButton2.setPreferredSize(new java.awt.Dimension(70, 30));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        panelToolbar.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(412, 14, -1, -1));

        btnTambah.setBackground(new java.awt.Color(122, 0, 0));
        btnTambah.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(255, 255, 255));
        btnTambah.setText("Tambah Peserta");
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

        jLabel3.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/search.png")); // NOI18N
        jLabel3.setText("C");
        jLabel3.setPreferredSize(new java.awt.Dimension(24, 28));
        panelToolbar.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 14, -1, -1));

        btnEkspor.setText("Ekspor");
        btnEkspor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEksporActionPerformed(evt);
            }
        });
        panelToolbar.add(btnEkspor, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 15, -1, 30));

        btnImpor.setText("Import");
        btnImpor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImporActionPerformed(evt);
            }
        });
        panelToolbar.add(btnImpor, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 15, -1, 30));

        add(panelToolbar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelTabel.setBackground(new java.awt.Color(255, 255, 255));
        panelTabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelTabel.setPreferredSize(new java.awt.Dimension(948, 460));
        panelTabel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollTabel.setPreferredSize(new java.awt.Dimension(948, 460));

        tabelPeserta.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        tabelPeserta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "ID Peserta", "Nama Lengkap", "NIK", "Program", "Status Seleksi", "Aksi"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tabelPeserta.setRowHeight(36);
        tabelPeserta.setSelectionBackground(new java.awt.Color(255, 230, 230));
        tabelPeserta.setSelectionForeground(new java.awt.Color(122, 0, 0));
        tabelPeserta.setShowHorizontalLines(true);
        scrollTabel.setViewportView(tabelPeserta);
        if (tabelPeserta.getColumnModel().getColumnCount() > 0) {
            tabelPeserta.getColumnModel().getColumn(0).setPreferredWidth(40);
            tabelPeserta.getColumnModel().getColumn(1).setPreferredWidth(80);
            tabelPeserta.getColumnModel().getColumn(2).setPreferredWidth(140);
            tabelPeserta.getColumnModel().getColumn(3).setPreferredWidth(110);
            tabelPeserta.getColumnModel().getColumn(4).setPreferredWidth(110);
            tabelPeserta.getColumnModel().getColumn(5).setPreferredWidth(85);
            tabelPeserta.getColumnModel().getColumn(6).setPreferredWidth(100);
        }

        panelTabel.add(scrollTabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        add(panelTabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, 450));

        lblTotalData.setFont(new java.awt.Font("Inter", 0, 11)); // NOI18N
        lblTotalData.setForeground(new java.awt.Color(120, 120, 120));
        lblTotalData.setText("Menampilkan 0 data peserta");
        lblTotalData.setPreferredSize(new java.awt.Dimension(300, 20));
        add(lblTotalData, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        FormTambahPeserta form = new FormTambahPeserta(null, true);
        form.setVisible(true);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        
    }//GEN-LAST:event_txtCariActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        loadTableData(txtCari.getText());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        txtCari.setText("");
        loadTableData("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnEksporActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEksporActionPerformed
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Simpan Data Excel");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }
            
            try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
                org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Data Peserta");
                
                org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
                int excelColIndex = 0;
                
                // Looping kolom tabel (kurangi 1 agar kolom 'Aksi' yang isinya tombol tidak ikut terekspor)
                for (int i = 0; i < tabelPeserta.getColumnCount() - 1; i++) {
                    org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(excelColIndex++);
                    cell.setCellValue(tabelPeserta.getColumnName(i));
                }
                
                for (int i = 0; i < tabelPeserta.getRowCount(); i++) {
                    org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                    excelColIndex = 0;
                    for (int j = 0; j < tabelPeserta.getColumnCount() - 1; j++) {
                        org.apache.poi.ss.usermodel.Cell cell = row.createCell(excelColIndex++);
                        Object value = tabelPeserta.getValueAt(i, j);
                        cell.setCellValue(value != null ? value.toString() : "");
                    }
                }

                java.io.FileOutputStream out = new java.io.FileOutputStream(filePath);
                workbook.write(out);
                out.close();
                
                javax.swing.JOptionPane.showMessageDialog(this, "Data berhasil diekspor ke Excel!", "Sukses", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Gagal mengekspor data: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnEksporActionPerformed

    private void btnImporActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImporActionPerformed
         javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Pilih File Excel untuk Diimpor");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
        
        if (fileChooser.showOpenDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            
            try (java.io.FileInputStream fis = new java.io.FileInputStream(file);
                 org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook(fis)) {
                 
                org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
                java.util.Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();
                
                if (rowIterator.hasNext()) {
                    rowIterator.next(); 
                }
                
                int jumlahBaru = 0;
                int jumlahUpdate = 0;
                int jumlahGagal = 0;   // BARU
                
                while (rowIterator.hasNext()) {
                    org.apache.poi.ss.usermodel.Row row = rowIterator.next();
                    org.apache.poi.ss.usermodel.DataFormatter formatter = new org.apache.poi.ss.usermodel.DataFormatter();
                    
                    // Urutan kolom = hasil ekspor. Col 0: No (diabaikan)
                    String idPeserta = formatter.formatCellValue(row.getCell(1)).trim(); // Col 1: ID
                    String nama = formatter.formatCellValue(row.getCell(2)).trim();      // Col 2: Nama
                    String nik = formatter.formatCellValue(row.getCell(3)).trim();       // Col 3: NIK
                    String program = formatter.formatCellValue(row.getCell(4)).trim();   // Col 4: Program (ID, mis. PRG001)
                    String statusSeleksi = formatter.formatCellValue(row.getCell(5)).trim(); // Col 5: Status
                    
                    // Lewati baris jika ID kosong atau tidak sengaja membaca header "ID Peserta"
                    if (idPeserta.isEmpty() || idPeserta.equalsIgnoreCase("ID Peserta")) continue;
                    
                    // DIUBAH: cari lewat cache DataStore, simpan lewat DAO
                    Model.Peserta ada = DataStore.cariPeserta(idPeserta);
                    
                    if (ada != null) {
                        // UPDATE: timpa data (Instruktur, Tgl Lahir, dll sengaja tidak disentuh)
                        ada.setNamaLengkap(nama);
                        ada.setNik(nik);
                        ada.setProgram(program);
                        ada.setStatusSeleksi(statusSeleksi);
                        
                        // false = jangan munculkan popup error per baris
                        if (Database.PesertaDAO.ubah(ada, false)) jumlahUpdate++; else jumlahGagal++;
                        
                    } else {
                        // INSERT: NIK dipakai sebagai username, jadi tidak boleh kosong
                        if (nik.isEmpty()) { jumlahGagal++; continue; }
                        
                        Model.Peserta pesertaBaru = new Model.Peserta(
                            nik,             // 1. Username
                            "yuuki" + nik,   // 2. Password
                            idPeserta,       // 3. ID Peserta
                            nik,             // 4. NIK
                            nama,            // 5. Nama Lengkap
                            "1999-01-01",    // 6. Tanggal Lahir
                            "Laki-laki",     // 7. Jenis Kelamin
                            "-",             // 8. No HP
                            "-",             // 9. Agama
                            "-",             // 10. Alamat
                            program,         // 11. Program
                            null,            // 12. Instruktur Damping (kolom id_instruktur boleh NULL)
                            "-",             // 13. Level Bahasa
                            "Belum Lunas",   // 14. Status Pembayaran
                            statusSeleksi,   // 15. Status Seleksi
                            "Belum Berangkat"// 16. Status Keberangkatan
                        );
                        if (Database.PesertaDAO.tambah(pesertaBaru, false)) jumlahBaru++; else jumlahGagal++;
                    }
                }
                
                // Muat ulang dari database: menampilkan hasil import SEKALIGUS membuang
                // perubahan di memori pada baris yang gagal disimpan
                DataStore.muatUlang();
                loadTableData("");
                
                String pesan = "Import Selesai!\n" +
                               "- Data Baru Ditambahkan: " + jumlahBaru + "\n" +
                               "- Data Lama Diperbarui: " + jumlahUpdate + "\n" +
                               "- Baris Gagal (program/NIK tidak valid atau duplikat): " + jumlahGagal;
                javax.swing.JOptionPane.showMessageDialog(this, pesan, "Sukses", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Gagal mengimpor data! Pastikan format Excel sesuai. Error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnImporActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEkspor;
    private javax.swing.JButton btnImpor;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblTotalData;
    private javax.swing.JPanel panelTabel;
    private javax.swing.JPanel panelToolbar;
    private javax.swing.JScrollPane scrollTabel;
    private javax.swing.JTable tabelPeserta;
    private javax.swing.JTextField txtCari;
    // End of variables declaration//GEN-END:variables
}
