/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views.instruktur;

import DataStore.DataStore;
import Model.Instruktur;
import Model.Pengguna;
import Model.Peserta;
import Util.TabelUtil;
import Views.FormTambahInstruktur;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author arelssi
 */
public class PanelInputNilai extends javax.swing.JPanel {

    /**
     * Creates new form PanelInputNilai
     */
    
    private String idInstrukturAktif;
    private DefaultTableModel modelTabel;
    
    public PanelInputNilai(String idInstruktur) {
        initComponents();
        
        this.idInstrukturAktif = idInstruktur;
        
        setupTabelInput();
        loadDataSiswa();
        
        // Memasang "Telinga" agar tabel bereaksi saat nilai diubah
        pasangListenerTabel();
    }
    
    private void setupTabelInput() {
        tabelNilai.setRowHeight(40);
        
        // Membuat model tabel khusus di mana HANYA kolom index 4 (Nilai) yang bisa diedit
        modelTabel = new DefaultTableModel(
            new Object [][] {},
            new String [] {"No.", "ID", "Nama Siswa", "Level", "Nilai", "Status"} // Sesuaikan nama kolommu (tanpa Progres)
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Hanya kolom ke-4 (Nilai) yang bernilai TRUE (bisa diedit)
                return column == 4; 
            }
        };
        
        tabelNilai.setModel(modelTabel);
        
        TabelUtil.autoResizeKolom(tabelNilai);
        javax.swing.table.JTableHeader header = tabelNilai.getTableHeader();
        
        header.setBackground(new java.awt.Color(122, 0, 0));
        header.setForeground(java.awt.Color.WHITE);
        
        header.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 13)); 
        header.setOpaque(true);
    }

    private void pasangListenerTabel() {
        modelTabel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Jika yang berubah adalah kolom ke-4 (Nilai)
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 4) {
                    int baris = e.getFirstRow();
                    
                    try {
                        // Ambil nilai yang baru saja diketik
                        String nilaiString = modelTabel.getValueAt(baris, 4).toString();
                        int nilai = Integer.parseInt(nilaiString);
                        
                        // Validasi skala 0-100
                        if (nilai < 0) nilai = 0;
                        if (nilai > 100) nilai = 100;
                        
                        // Update sel kembali jika melebihi batas (opsional, agar aman)
                        // modelTabel.setValueAt(String.valueOf(nilai), baris, 4); 

                        // Tentukan Status (Lulus jika >= 70)
                        if (nilai >= 70) {
                            modelTabel.setValueAt("Lulus", baris, 5);
                        } else {
                            modelTabel.setValueAt("Tidak Lulus", baris, 5);
                        }
                        
                        // Hitung ulang rata-rata keseluruhan kelas
                        hitungRataRataKelas();
                        
                    } catch (NumberFormatException ex) {
                        // Jika instruktur iseng mengetik huruf, kembalikan status jadi kosong
                        modelTabel.setValueAt("-", baris, 5);
                    }
                }
            }
        });
    }
    
    private void loadDataSiswa() {
        modelTabel.setRowCount(0); // Kosongkan tabel
        
        // Cari ID asli instruktur (mencegah error username)
        String idAsli = idInstrukturAktif;
        for (Pengguna pgn : DataStore.daftarPengguna) {
            if (pgn instanceof Instruktur) {
                Instruktur ins = (Instruktur) pgn;
                if ((ins.getIdInstruktur() != null && ins.getIdInstruktur().equals(idInstrukturAktif)) || 
                    (ins.getUsername() != null && ins.getUsername().equals(idInstrukturAktif))) {
                    idAsli = ins.getIdInstruktur();
                    break;
                }
            }
        }

        int no = 1;
        for (Peserta p : DataStore.daftarPeserta) {
            // Hanya masukkan siswa dari instruktur ini
            if (p.getInstrukturDamping() != null && p.getInstrukturDamping().getIdInstruktur().equals(idAsli)) {
                
                modelTabel.addRow(new Object[]{
                    no++,
                    p.getIdPeserta(),
                    p.getNamaLengkap(),
                    p.getLevelBahasa(),
                    "0", // Nilai awal default
                    "Tidak Lulus" // Status awal
                });
            }
        }
        
        hitungRataRataKelas(); // Hitung rata-rata awal (pasti 0)
    }
    
    private void hitungRataRataKelas() {
        int totalSiswa = modelTabel.getRowCount();
        if (totalSiswa == 0) return;
        
        int akumulasiNilai = 0;
        int jumlahYangDinilai = 0;
        
        for (int i = 0; i < totalSiswa; i++) {
            try {
                Object nilaiObj = modelTabel.getValueAt(i, 4);
                if (nilaiObj != null && !nilaiObj.toString().isEmpty()) {
                    int nilai = Integer.parseInt(nilaiObj.toString());
                    akumulasiNilai += nilai;
                    jumlahYangDinilai++;
                }
            } catch (NumberFormatException ex) {
                // Abaikan sel yang isinya huruf/kosong
            }
        }
        
        int rataRata = (jumlahYangDinilai > 0) ? (akumulasiNilai / jumlahYangDinilai) : 0;
        
        // Update Label Rata-rata di pojok kanan atas (Sesuaikan nama variabelnya)
        lblRataRata.setText("Rata-rata: " + rataRata);
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
        btnTambah = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        panelTabel = new javax.swing.JPanel();
        scrollTabel = new javax.swing.JScrollPane();
        tabelNilai = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        lblRataRata = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelToolbar.setBackground(new java.awt.Color(255, 255, 255));
        panelToolbar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnTambah.setBackground(new java.awt.Color(122, 0, 0));
        btnTambah.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(255, 255, 255));
        btnTambah.setText("Simpan Nilai");
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

        jComboBox1.setBackground(new java.awt.Color(80, 80, 80));
        jComboBox1.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelToolbar.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 13, 160, 30));

        jLabel1.setText("Materi");
        panelToolbar.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        add(panelToolbar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelTabel.setBackground(new java.awt.Color(255, 255, 255));
        panelTabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelTabel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollTabel.setPreferredSize(new java.awt.Dimension(948, 460));

        tabelNilai.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        tabelNilai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "ID", "Nama Instruktur", "Level", "Nilai", "Status", "Aksi"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tabelNilai.setRowHeight(36);
        tabelNilai.setSelectionBackground(new java.awt.Color(255, 230, 230));
        tabelNilai.setSelectionForeground(new java.awt.Color(122, 0, 0));
        tabelNilai.setShowHorizontalLines(true);
        scrollTabel.setViewportView(tabelNilai);
        if (tabelNilai.getColumnModel().getColumnCount() > 0) {
            tabelNilai.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        panelTabel.add(scrollTabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        add(panelTabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, 420));

        jPanel1.setBackground(new java.awt.Color(240, 200, 200));

        lblRataRata.setFont(new java.awt.Font("Inter", 1, 15)); // NOI18N
        lblRataRata.setForeground(new java.awt.Color(122, 0, 0));
        lblRataRata.setText("Rata-rata: 90");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(lblRataRata)
                .addGap(25, 25, 25))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblRataRata)
                .addContainerGap())
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 70, 140, 30));

        jLabel3.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(90, 90, 90));
        jLabel3.setText("Skala 0–100. Lulus jika nilai ≥ 70");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, 20));

        jLabel4.setForeground(new java.awt.Color(70, 70, 70));
        jLabel4.setText("Input Nilai Materi");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, 20));
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        javax.swing.JOptionPane.showMessageDialog(this, 
            "Data nilai berhasil disimpan ke dalam sistem!", 
            "Sukses", 
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnTambahActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTambah;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblRataRata;
    private javax.swing.JPanel panelTabel;
    private javax.swing.JPanel panelToolbar;
    private javax.swing.JScrollPane scrollTabel;
    private javax.swing.JTable tabelNilai;
    // End of variables declaration//GEN-END:variables
}
