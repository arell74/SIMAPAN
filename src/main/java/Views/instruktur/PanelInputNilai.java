/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views.instruktur;
 
import DataStore.DataStore;
import Model.HasilKuis;
import Model.Instruktur;
import Model.JawabanSoal;
import Model.Kuis;
import Model.Pengguna;
import Model.Peserta;
import Util.TabelUtil;
import java.awt.Color;
import java.awt.Font;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class PanelInputNilai extends javax.swing.JPanel {

    private String idInstrukturAktif;
    private DefaultTableModel modelTabel;
    private boolean sedangMemuatData = false;

    public PanelInputNilai(String idInstruktur) {
        initComponents();

        this.idInstrukturAktif = idInstruktur;
        this.modelTabel = (DefaultTableModel) tabelNilai.getModel();

        setupTable();
        muatFilterMateri();
        loadDataSiswa();
    }

    private void setupTable() {
        JTableHeader header = tabelNilai.getTableHeader();
        header.setBackground(new Color(122, 0, 0));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Inter", Font.BOLD, 12));
        header.setOpaque(true);

        jComboBox1.addActionListener(e -> {
            if (!sedangMemuatData) {
                loadDataSiswa();
            }
        });

        // Otomatis hitung ulang status Lulus/Tidak Lulus & rata-rata saat kolom Nilai (index 4) diedit
        modelTabel.addTableModelListener(e -> {
            if (sedangMemuatData || e.getType() != TableModelEvent.UPDATE) {
                return;
            }
            int baris = e.getFirstRow();
            int kolom = e.getColumn();
            if (kolom == 4 && baris >= 0 && baris < modelTabel.getRowCount()) {
                sedangMemuatData = true;
                try {
                    Object val = modelTabel.getValueAt(baris, 4);
                    int nilai = Integer.parseInt(val != null ? val.toString().trim() : "0");
                    if (nilai < 0 || nilai > 100) {
                        JOptionPane.showMessageDialog(this, "Nilai harus di antara 0 sampai 100!",
                                "Peringatan", JOptionPane.WARNING_MESSAGE);
                        modelTabel.setValueAt("0", baris, 4);
                        modelTabel.setValueAt("Tidak Lulus", baris, 5);
                    } else {
                        String status = (nilai >= 70) ? "Lulus" : "Tidak Lulus";
                        modelTabel.setValueAt(status, baris, 5);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Kolom Nilai harus berupa angka bulat!",
                            "Error Format", JOptionPane.ERROR_MESSAGE);
                    modelTabel.setValueAt("0", baris, 4);
                    modelTabel.setValueAt("Tidak Lulus", baris, 5);
                } finally {
                    sedangMemuatData = false;
                }
                hitungRataRataKelas();
            }
        });
    }

    private String cariIdAsliInstruktur() {
        String idAsli = idInstrukturAktif;
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
        return idAsli;
    }

    private void muatFilterMateri() {
        DataStore.muatUlang();
        String idAsli = cariIdAsliInstruktur();

        Set<String> daftarMateriKuis = new LinkedHashSet<>();
        daftarMateriKuis.add("Semua Evaluasi / Kuis");
        daftarMateriKuis.add("MNH 1");
        daftarMateriKuis.add("MNH 2");
        daftarMateriKuis.add("IRODORI");

        for (Kuis k : DataStore.daftarKuis) {
            if (idAsli != null && idAsli.equals(k.getIdInstruktur()) && k.getJudulKuis() != null) {
                daftarMateriKuis.add(k.getJudulKuis());
            }
        }

        sedangMemuatData = true;
        try {
            jComboBox1.setModel(new DefaultComboBoxModel<>(daftarMateriKuis.toArray(new String[0])));
        } finally {
            sedangMemuatData = false;
        }
    }

    private int hitungNilaiPesertaDariMySQL(String idPeserta, String filterMateri) {
        int totalSkor = 0;
        int jumlahKuis = 0;

        for (HasilKuis hk : DataStore.daftarHasilKuis) {
            if (idPeserta.equals(hk.getIdPeserta())) {
                boolean cocokMateri = "Semua Evaluasi / Kuis".equals(filterMateri)
                        || "MNH 1".equals(filterMateri)
                        || "MNH 2".equals(filterMateri)
                        || "IRODORI".equals(filterMateri)
                        || (hk.getJudulKuis() != null && hk.getJudulKuis().equalsIgnoreCase(filterMateri));

                if (cocokMateri) {
                    totalSkor += hk.getNilaiAkhir();
                    jumlahKuis++;
                }
            }
        }

        return (jumlahKuis > 0) ? Math.round((float) totalSkor / jumlahKuis) : 0;
    }

    private void loadDataSiswa() {
        DataStore.muatUlang();

        sedangMemuatData = true;
        try {
            modelTabel.setRowCount(0);

            String idAsli = cariIdAsliInstruktur();
            String filterMateri = (jComboBox1.getSelectedItem() != null)
                    ? jComboBox1.getSelectedItem().toString()
                    : "Semua Evaluasi / Kuis";

            int no = 1;
            for (Peserta p : DataStore.daftarPeserta) {
                if (p.getInstrukturDamping() != null && p.getInstrukturDamping().getIdInstruktur().equals(idAsli)) {
                    int nilaiAktual = hitungNilaiPesertaDariMySQL(p.getIdPeserta(), filterMateri);
                    String status = (nilaiAktual >= 70) ? "Lulus" : "Tidak Lulus";

                    modelTabel.addRow(new Object[]{
                        String.valueOf(no++),
                        p.getIdPeserta(),
                        p.getNamaLengkap(),
                        p.getLevelBahasa(),
                        String.valueOf(nilaiAktual),
                        status
                    });
                }
            }
        } finally {
            sedangMemuatData = false;
        }

        hitungRataRataKelas();
        TabelUtil.autoResizeKolom(tabelNilai);
    }

    private void hitungRataRataKelas() {
        int totalSiswa = modelTabel.getRowCount();
        if (totalSiswa == 0) {
            lblRataRata.setText("Rata-rata: 0");
            return;
        }

        int akumulasiNilai = 0;
        int jumlahYangDinilai = 0;

        for (int i = 0; i < totalSiswa; i++) {
            try {
                Object nilaiObj = modelTabel.getValueAt(i, 4);
                if (nilaiObj != null && !nilaiObj.toString().trim().isEmpty()) {
                    int nilai = Integer.parseInt(nilaiObj.toString().trim());
                    akumulasiNilai += nilai;
                    jumlahYangDinilai++;
                }
            } catch (NumberFormatException ex) {
                // Abaikan sel yang isinya bukan angka
            }
        }

        int rataRata = (jumlahYangDinilai > 0) ? (akumulasiNilai / jumlahYangDinilai) : 0;
        lblRataRata.setText("Rata-rata: " + rataRata);
    }

    private Peserta ambilPesertaTerpilih() {
        int baris = tabelNilai.getSelectedRow();
        if (baris < 0) {
            return null;
        }

        int barisModel = tabelNilai.convertRowIndexToModel(baris);
        Object idObj = modelTabel.getValueAt(barisModel, 1);
        if (idObj == null) {
            return null;
        }

        return DataStore.cariPeserta(idObj.toString());
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
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        btnEvaluas = new javax.swing.JButton();
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

        jSeparator2.setPreferredSize(new java.awt.Dimension(980, 2));
        panelToolbar.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 54, -1, -1));

        jSeparator3.setPreferredSize(new java.awt.Dimension(980, 2));
        panelToolbar.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jComboBox1.setBackground(new java.awt.Color(80, 80, 80));
        jComboBox1.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MNH 1", "MNH 2", "IRODORI" }));
        panelToolbar.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 13, 160, 30));

        jLabel1.setText("Materi");
        panelToolbar.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        btnEvaluas.setBackground(new java.awt.Color(122, 0, 0));
        btnEvaluas.setForeground(new java.awt.Color(255, 255, 255));
        btnEvaluas.setText("Evaluasi Peserta");
        btnEvaluas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEvaluasActionPerformed(evt);
            }
        });
        panelToolbar.add(btnEvaluas, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 10, -1, 30));

        add(panelToolbar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelTabel.setBackground(new java.awt.Color(255, 255, 255));
        panelTabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelTabel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollTabel.setPreferredSize(new java.awt.Dimension(948, 460));

        tabelNilai.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        tabelNilai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No.", "ID", "Nama Instruktur", "Level", "Nilai", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class
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
        jLabel4.setText("Evaluasi peserta lpk");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, 20));
    }// </editor-fold>//GEN-END:initComponents

    private void btnEvaluasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEvaluasActionPerformed
        Peserta pesertaTerpilih = ambilPesertaTerpilih();
 
        if (pesertaTerpilih == null) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Pilih dulu salah satu baris peserta di tabel sebelum melakukan evaluasi.",
                    "Belum Ada Peserta Terpilih",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );
            return;
        }
 
        java.awt.Frame parentFrame = (java.awt.Frame) SwingUtilities.getWindowAncestor(this);
        FormEvaluasiPeserta formEvaluasi = new FormEvaluasiPeserta(parentFrame, true, pesertaTerpilih, idInstrukturAktif);
        formEvaluasi.setVisible(true);
 
        loadDataSiswa();
    }//GEN-LAST:event_btnEvaluasActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEvaluas;
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
