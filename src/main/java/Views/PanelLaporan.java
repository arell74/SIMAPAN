/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views;

import DataStore.DataStore;
import Model.Dokumen;
import Model.Peserta;
import Model.Program;
import Model.Seleksi;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.Phrase;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author arelssi
 */
public class PanelLaporan extends javax.swing.JPanel {

    /**
     * Creates new form PanelLaporan
     */
    public PanelLaporan() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(980, 616));
        setupTable();
        setupFilter();
        generateLaporan();
    }
    
    private void setupTable() {
        // 1. Styling Header Tabel (Warna, Font, dan Latar Belakang)
        tabelLaporan.getTableHeader().setFont(new Font("Inter", Font.BOLD, 12));
        tabelLaporan.getTableHeader().setBackground(new Color(122, 0, 0)); // Merah maroon
        tabelLaporan.getTableHeader().setForeground(Color.WHITE);
        tabelLaporan.getTableHeader().setOpaque(false);
        
        // 2. Styling Baris Data
        tabelLaporan.setRowHeight(36); // Memberikan ruang agar teks tidak terlalu mepet
        tabelLaporan.setSelectionBackground(new Color(255, 230, 230)); // Warna saat baris diklik
        
        // 3. (Opsional) Mengunci tabel agar cell-nya tidak bisa diklik 2x/diedit manual oleh user
        tabelLaporan.setDefaultEditor(Object.class, null);
    }
    
    private void setupFilter() {
        cbProgram.removeAllItems();
        cbProgram.addItem("Semua");
        
        // Memuat daftar program dari DataStore
        for (Program prg : DataStore.daftarProgram) {
            cbProgram.addItem(prg.getNamaProgram());
        }
        
        cbPeriode.removeAllItems();
        cbPeriode.addItem("Semua");
        cbPeriode.addItem("2026");
        cbPeriode.addItem("2025");
    }
    
    private void generateLaporan() {
        DefaultTableModel model = (DefaultTableModel) tabelLaporan.getModel();
        model.setRowCount(0);
        
        if (cbProgram.getSelectedItem() == null) {
            return;
        }

        String filterProgram = cbProgram.getSelectedItem().toString();
        
        // Variabel untuk menghitung Kartu Statistik (KPI)
        int totalPeserta = 0;
        int jumlahLulus = 0;
        int jumlahSiapBerangkat = 0;
        
        // Variabel untuk Rata-rata Nilai
        int akumulasiNilai = 0;
        int jumlahDataNilai = 0;

        // Variabel untuk Distribusi (Progress Bar Mockup)
        int progMesin = 0, progPenginapan = 0, progMakanan = 0;
        int statSiap = 0, statMenunggu = 0, statBelum = 0;

        int no = 1;

        for (Peserta p : DataStore.daftarPeserta) {
            String namaProgramPeserta = "";
            for (Program prg : DataStore.daftarProgram) {
                if (prg.getIdProgram().equals(p.getProgram())) {
                    namaProgramPeserta = prg.getNamaProgram();
                    break;
                }
            }

            // Jika filter tidak sama dengan "Semua" dan tidak cocok dengan peserta ini, lewati!
            if (!filterProgram.equals("Semua") && !namaProgramPeserta.equalsIgnoreCase(filterProgram)) {
                continue; 
            }

            // 2. HITUNG STATISTIK UTAMA (Total, Lulus, Keberangkatan)
            totalPeserta++;
            if (p.getStatusSeleksi().equalsIgnoreCase("Lulus")) jumlahLulus++;
            if (p.getStatusKeberangkatan().equalsIgnoreCase("Siap Berangkat")) jumlahSiapBerangkat++;

            // Hitung Distribusi Program (Bisa disesuaikan dengan nama program di database-mu)
            if (namaProgramPeserta.contains("Mesin") || namaProgramPeserta.contains("Manufaktur")) progMesin++;
            else if (namaProgramPeserta.contains("Penginapan") || namaProgramPeserta.contains("Caregiver")) progPenginapan++;
            else progMakanan++; // Default as makanan/pertanian
            
            // Hitung Distribusi Status
            if (p.getStatusKeberangkatan().equalsIgnoreCase("Siap Berangkat")) statSiap++;
            else if (p.getStatusKeberangkatan().equalsIgnoreCase("Menunggu Dokumen")) statMenunggu++;
            else statBelum++;

            // 3. MENCARI NILAI RATA-RATA DARI RIWAYAT SELEKSI
            int nilaiTotalPesertaIni = 0;
            int countSeleksi = 0;
            for (Seleksi sel : DataStore.daftarSeleksi) {
                if (sel.getPeserta().getIdPeserta().equals(p.getIdPeserta())) {
                    nilaiTotalPesertaIni += sel.getNilai();
                    countSeleksi++;
                    
                    akumulasiNilai += sel.getNilai();
                    jumlahDataNilai++;
                }
            }
            int rataNilaiPeserta = (countSeleksi > 0) ? (nilaiTotalPesertaIni / countSeleksi) : 0;

            // 4. MENCARI DOKUMEN & TANGGAL KEBERANGKATAN
            String noPaspor = "Belum diinput";
            String tglBerangkat = "—";
            for (Dokumen dok : DataStore.daftarDokumen) {
                if (dok.getPeserta().getIdPeserta().equals(p.getIdPeserta())) {
                    noPaspor = dok.getNoPaspor();
                    tglBerangkat = dok.getTanggalBerangkat();
                    break;
                }
            }

            // 5. MASUKKAN DATA KE TABEL
            model.addRow(new Object[]{
                no++,
                p.getIdPeserta(),
                p.getNamaLengkap(),
                namaProgramPeserta,
                rataNilaiPeserta,
                noPaspor,
                tglBerangkat,
                p.getStatusKeberangkatan()
            });
        }

        // 6. UPDATE UI (KARTU & GRAFIK)
        updateStatistikUI(totalPeserta, jumlahLulus, jumlahSiapBerangkat, akumulasiNilai, jumlahDataNilai);
        
         updateDistribusiUI(progMesin, progPenginapan, progMakanan, statSiap, statMenunggu, statBelum);
    }
    
    // Method untuk mengupdate nilai angka dan grafik Progress Bar di bagian Distribusi
    private void updateDistribusiUI(int progMesin, int progPenginapan, int progMakanan, int statSiap, int statMenunggu, int statBelum) {
        
        // 1. UPDATE LABEL ANGKA (Ganti nama variabelnya sesuai dengan yang ada di NetBeans kamu)
        lblAngkaMesin.setText(String.valueOf(progMesin));
        lblAngkaPenginapan.setText(String.valueOf(progPenginapan));
        lblAngkaMakanan.setText(String.valueOf(progMakanan));
        
        lblAngkaSiap.setText(String.valueOf(statSiap));
        lblAngkaMenunggu.setText(String.valueOf(statMenunggu));
        lblAngkaBelum.setText(String.valueOf(statBelum));

        // 2. UPDATE PROGRESS BAR (Jika kamu menggunakan komponen JProgressBar)
        // Hitung total untuk menjadikan nilai maksimal (100% / panjang full bar)
        int totalProgram = progMesin + progPenginapan + progMakanan;
        if (totalProgram > 0) {
            // Set batas maksimal bar (Bukan 100, tapi jumlah total datanya)
            barMesin.setMaximum(totalProgram);
            barPenginapan.setMaximum(totalProgram);
            barMakanan.setMaximum(totalProgram);
            
            // Set isi/panjang warna bar-nya
            barMesin.setValue(progMesin);
            barPenginapan.setValue(progPenginapan);
            barMakanan.setValue(progMakanan);
        } else {
            // Kosongkan bar jika data tidak ada
            barMesin.setValue(0);
            barPenginapan.setValue(0);
            barMakanan.setValue(0);
        }

        // Lakukan hal yang sama untuk bar Status Keberangkatan
        int totalStatus = statSiap + statMenunggu + statBelum;
        if (totalStatus > 0) {
            barSiap.setMaximum(totalStatus);
            barMenunggu.setMaximum(totalStatus);
            barBelum.setMaximum(totalStatus);
            
            barSiap.setValue(statSiap);
            barMenunggu.setValue(statMenunggu);
            barBelum.setValue(statBelum);
        } else {
            barSiap.setValue(0);
            barMenunggu.setValue(0);
            barBelum.setValue(0);
        }
    }
    
    private void updateStatistikUI(int totalPeserta, int lulus, int siapBerangkat, int akumulasiNilai, int jumlahDataNilai) {
        
        // Total Peserta
        lblTotalPeserta.setText(String.valueOf(totalPeserta));
        
        // Tingkat Kelulusan
        int persentaseLulus = (totalPeserta > 0) ? (lulus * 100 / totalPeserta) : 0;
        lblPersentaseLulus.setText(persentaseLulus + " %");
        lblKeteranganLulus.setText(lulus + " dari " + totalPeserta + " peserta lulus");
        
        // Siap Berangkat
        lblKeteranganLulus.setText(String.valueOf(siapBerangkat));
        
        // Rata-rata Nilai Seluruhnya
        int rataRataTotal = (jumlahDataNilai > 0) ? (akumulasiNilai / jumlahDataNilai) : 0;
        lblRataNilai.setText(String.valueOf(rataRataTotal));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlToolbar = new javax.swing.JPanel();
        btnReset = new javax.swing.JButton();
        cbProgram = new javax.swing.JComboBox<>();
        btnEkspor = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbPeriode = new javax.swing.JComboBox<>();
        panelTabel = new javax.swing.JPanel();
        lblTotalData = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelLaporan = new javax.swing.JTable();
        kartuTotal3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lblRataNilai = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        kartuTotal4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        lblTotalPeserta = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        kartuTotal5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        lblPersentaseLulus = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        kartuTotal6 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        lblKeteranganLulus = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        panelPerProgram = new javax.swing.JPanel();
        barSiap = new javax.swing.JProgressBar();
        jPanel3 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblAngkaSiap = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        barMenunggu = new javax.swing.JProgressBar();
        jLabel19 = new javax.swing.JLabel();
        barBelum = new javax.swing.JProgressBar();
        jLabel20 = new javax.swing.JLabel();
        lblAngkaMenunggu = new javax.swing.JLabel();
        lblAngkaBelum = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        panelProgram = new javax.swing.JPanel();
        barMesin = new javax.swing.JProgressBar();
        jPanel8 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        lblAngkaMesin = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        barPenginapan = new javax.swing.JProgressBar();
        jLabel27 = new javax.swing.JLabel();
        barMakanan = new javax.swing.JProgressBar();
        jLabel28 = new javax.swing.JLabel();
        lblAngkaPenginapan = new javax.swing.JLabel();
        lblAngkaMakanan = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlToolbar.setBackground(new java.awt.Color(255, 255, 255));
        pnlToolbar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        pnlToolbar.setPreferredSize(new java.awt.Dimension(980, 56));
        pnlToolbar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        pnlToolbar.add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, 90, 34));

        cbProgram.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua" }));
        cbProgram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbProgramActionPerformed(evt);
            }
        });
        pnlToolbar.add(cbProgram, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 130, 34));

        btnEkspor.setBackground(new java.awt.Color(0, 122, 0));
        btnEkspor.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        btnEkspor.setForeground(new java.awt.Color(255, 255, 255));
        btnEkspor.setText("Ekspor Laporan");
        btnEkspor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEksporActionPerformed(evt);
            }
        });
        pnlToolbar.add(btnEkspor, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 20, -1, 34));

        jLabel2.setFont(new java.awt.Font("Inter", 0, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(30, 30, 30));
        jLabel2.setText("Program:");
        pnlToolbar.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, -1, -1));

        jLabel4.setFont(new java.awt.Font("Inter", 0, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(30, 30, 30));
        jLabel4.setText("Periode: ");
        pnlToolbar.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 30, -1, -1));

        cbPeriode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua" }));
        cbPeriode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPeriodeActionPerformed(evt);
            }
        });
        pnlToolbar.add(cbPeriode, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 140, 34));

        add(pnlToolbar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 980, 70));

        panelTabel.setBackground(new java.awt.Color(255, 255, 255));
        panelTabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelTabel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTotalData.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        lblTotalData.setForeground(new java.awt.Color(120, 120, 120));
        lblTotalData.setText("Rekap Keberangkatan Peserta");
        panelTabel.add(lblTotalData, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setToolTipText("");
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tabelLaporan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "ID", "Nama Peserta", "Program", "Nilai", "No. Paspor", "tgl_berangkat", "Status"
            }
        ));
        jScrollPane1.setViewportView(tabelLaporan);
        if (tabelLaporan.getColumnModel().getColumnCount() > 0) {
            tabelLaporan.getColumnModel().getColumn(0).setMaxWidth(50);
            tabelLaporan.getColumnModel().getColumn(1).setPreferredWidth(50);
            tabelLaporan.getColumnModel().getColumn(2).setPreferredWidth(180);
            tabelLaporan.getColumnModel().getColumn(3).setPreferredWidth(150);
            tabelLaporan.getColumnModel().getColumn(4).setMaxWidth(70);
        }

        panelTabel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 910, 180));

        add(panelTabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 910, 210));

        kartuTotal3.setBackground(new java.awt.Color(255, 255, 255));
        kartuTotal3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        kartuTotal3.setPreferredSize(new java.awt.Dimension(296, 56));
        kartuTotal3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(130, 130, 130));
        jLabel7.setText("dari seluruh seleksi");
        kartuTotal3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        lblRataNilai.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        lblRataNilai.setForeground(new java.awt.Color(255, 165, 0));
        lblRataNilai.setText("0");
        kartuTotal3.add(lblRataNilai, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jPanel4.setBackground(new java.awt.Color(255, 235, 235));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/star.png")); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(60, 40));
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, 40, -1));

        kartuTotal3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 40, 40));

        jLabel9.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(130, 130, 130));
        jLabel9.setText("Rata-rata Nilai");
        kartuTotal3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        add(kartuTotal3, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 80, 217, 120));

        kartuTotal4.setBackground(new java.awt.Color(255, 255, 255));
        kartuTotal4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        kartuTotal4.setPreferredSize(new java.awt.Dimension(300, 56));
        kartuTotal4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(130, 130, 130));
        jLabel8.setText("Peserta Terdaftar");
        kartuTotal4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        lblTotalPeserta.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        lblTotalPeserta.setForeground(new java.awt.Color(122, 0, 0));
        lblTotalPeserta.setText("0");
        kartuTotal4.add(lblTotalPeserta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jPanel5.setBackground(new java.awt.Color(255, 235, 235));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/user.png")); // NOI18N
        jLabel3.setText("i");
        jLabel3.setPreferredSize(new java.awt.Dimension(60, 40));
        jPanel5.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 0, 30, 40));

        kartuTotal4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 40, 40));

        jLabel10.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(130, 130, 130));
        jLabel10.setText("Total Peserta");
        kartuTotal4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        add(kartuTotal4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 217, 120));

        kartuTotal5.setBackground(new java.awt.Color(255, 255, 255));
        kartuTotal5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        kartuTotal5.setPreferredSize(new java.awt.Dimension(300, 56));
        kartuTotal5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(130, 130, 130));
        jLabel11.setText("4 dari 6 peserta lulus");
        kartuTotal5.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        lblPersentaseLulus.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        lblPersentaseLulus.setForeground(new java.awt.Color(0, 122, 0));
        lblPersentaseLulus.setText("0 %");
        kartuTotal5.add(lblPersentaseLulus, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jPanel6.setBackground(new java.awt.Color(255, 235, 235));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/check.png")); // NOI18N
        jLabel5.setPreferredSize(new java.awt.Dimension(60, 40));
        jPanel6.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, 40, -1));

        kartuTotal5.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 40, 40));

        jLabel12.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(130, 130, 130));
        jLabel12.setText("Tingkat Kelulusan");
        kartuTotal5.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        add(kartuTotal5, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 80, 217, 120));

        kartuTotal6.setBackground(new java.awt.Color(255, 255, 255));
        kartuTotal6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        kartuTotal6.setPreferredSize(new java.awt.Dimension(296, 56));
        kartuTotal6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(130, 130, 130));
        jLabel13.setText("dokumen lengkap");
        kartuTotal6.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        lblKeteranganLulus.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        lblKeteranganLulus.setForeground(new java.awt.Color(0, 0, 122));
        lblKeteranganLulus.setText("0");
        kartuTotal6.add(lblKeteranganLulus, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jPanel7.setBackground(new java.awt.Color(255, 235, 235));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/airplane (1).png")); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(60, 40));
        jPanel7.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, 40, -1));

        kartuTotal6.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 40, 40));

        jLabel14.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(130, 130, 130));
        jLabel14.setText("Siap Berangkat");
        kartuTotal6.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        add(kartuTotal6, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 80, 217, 120));

        panelPerProgram.setBackground(new java.awt.Color(255, 255, 255));
        panelPerProgram.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelPerProgram.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        barSiap.setForeground(new java.awt.Color(0, 0, 122));
        barSiap.setValue(30);
        panelPerProgram.add(barSiap, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 60, -1, 10));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(110, 110, 110));
        jLabel15.setText("Rekap status seluruh peserta lulus ");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 20, -1, -1));

        jLabel16.setFont(new java.awt.Font("Inter", 1, 9)); // NOI18N
        jLabel16.setText("Status Keberangkatan");
        jPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 7, -1, -1));

        panelPerProgram.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 447, 40));

        lblAngkaSiap.setFont(new java.awt.Font("Inter", 1, 13)); // NOI18N
        lblAngkaSiap.setForeground(new java.awt.Color(30, 30, 30));
        lblAngkaSiap.setText("2");
        panelPerProgram.add(lblAngkaSiap, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 57, -1, -1));

        jLabel18.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(30, 30, 30));
        jLabel18.setText("Menunggu Dokumen");
        panelPerProgram.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        barMenunggu.setForeground(new java.awt.Color(222, 122, 0));
        barMenunggu.setValue(40);
        panelPerProgram.add(barMenunggu, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 80, -1, 10));

        jLabel19.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(30, 30, 30));
        jLabel19.setText("Belum Berangkat");
        panelPerProgram.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        barBelum.setForeground(new java.awt.Color(100, 100, 100));
        barBelum.setValue(10);
        panelPerProgram.add(barBelum, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, -1, 10));

        jLabel20.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(30, 30, 30));
        jLabel20.setText("Siap Berangkat");
        panelPerProgram.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        lblAngkaMenunggu.setFont(new java.awt.Font("Inter", 1, 13)); // NOI18N
        lblAngkaMenunggu.setForeground(new java.awt.Color(30, 30, 30));
        lblAngkaMenunggu.setText("4");
        panelPerProgram.add(lblAngkaMenunggu, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 77, 10, -1));

        lblAngkaBelum.setFont(new java.awt.Font("Inter", 1, 13)); // NOI18N
        lblAngkaBelum.setForeground(new java.awt.Color(30, 30, 30));
        lblAngkaBelum.setText("1");
        panelPerProgram.add(lblAngkaBelum, new org.netbeans.lib.awtextra.AbsoluteConstraints(403, 100, -1, -1));

        add(panelPerProgram, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 210, 447, 150));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 260, -1, -1));

        panelProgram.setBackground(new java.awt.Color(255, 255, 255));
        panelProgram.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelProgram.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        barMesin.setForeground(new java.awt.Color(122, 0, 0));
        barMesin.setValue(20);
        panelProgram.add(barMesin, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 60, -1, 10));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel23.setFont(new java.awt.Font("Inter", 1, 10)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(110, 110, 110));
        jLabel23.setText("Distribusi peserta berdasarkan program");
        jPanel8.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 20, -1, -1));

        jLabel24.setFont(new java.awt.Font("Inter", 1, 9)); // NOI18N
        jLabel24.setText("Peserta Per program");
        jPanel8.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 7, -1, -1));

        panelProgram.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 447, 40));

        lblAngkaMesin.setFont(new java.awt.Font("Inter", 1, 13)); // NOI18N
        lblAngkaMesin.setForeground(new java.awt.Color(30, 30, 30));
        lblAngkaMesin.setText("2");
        panelProgram.add(lblAngkaMesin, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 57, -1, -1));

        jLabel26.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(30, 30, 30));
        jLabel26.setText("Penginapan");
        panelProgram.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        barPenginapan.setForeground(new java.awt.Color(122, 0, 0));
        barPenginapan.setValue(70);
        panelProgram.add(barPenginapan, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 80, -1, 10));

        jLabel27.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(30, 30, 30));
        jLabel27.setText("Industri Makanan");
        panelProgram.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        barMakanan.setForeground(new java.awt.Color(122, 0, 0));
        barMakanan.setValue(70);
        panelProgram.add(barMakanan, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, -1, 10));

        jLabel28.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(30, 30, 30));
        jLabel28.setText("Industri Mesin");
        panelProgram.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        lblAngkaPenginapan.setFont(new java.awt.Font("Inter", 1, 13)); // NOI18N
        lblAngkaPenginapan.setForeground(new java.awt.Color(30, 30, 30));
        lblAngkaPenginapan.setText("7");
        panelProgram.add(lblAngkaPenginapan, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 77, 10, -1));

        lblAngkaMakanan.setFont(new java.awt.Font("Inter", 1, 13)); // NOI18N
        lblAngkaMakanan.setForeground(new java.awt.Color(30, 30, 30));
        lblAngkaMakanan.setText("7");
        panelProgram.add(lblAngkaMakanan, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 100, -1, -1));

        add(panelProgram, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 447, 150));
    }// </editor-fold>//GEN-END:initComponents

    private void cbPeriodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPeriodeActionPerformed
        generateLaporan();
    }//GEN-LAST:event_cbPeriodeActionPerformed

    private void cbProgramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbProgramActionPerformed
        generateLaporan();
    }//GEN-LAST:event_cbProgramActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        cbProgram.setSelectedIndex(0);
        cbPeriode.setSelectedIndex(0);
        generateLaporan();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnEksporActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEksporActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Laporan PDF");
        
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Documents", "pdf"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }

            try {
                // 1. SEMBUNYIKAN TOMBOL DAN KESELURUHAN PANEL TABEL
                btnReset.setVisible(false);
                btnEkspor.setVisible(false);
                panelTabel.setVisible(false);

                int fullWidth = this.getWidth();
                int fullHeight = this.getHeight();
                
                BufferedImage imageCapture = new BufferedImage(fullWidth, fullHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = imageCapture.createGraphics();
                this.printAll(g2d); 
                g2d.dispose();

                btnReset.setVisible(true);
                btnEkspor.setVisible(true);
                panelTabel.setVisible(true);
                int batasCropBawah = panelTabel.getY(); 
                if (batasCropBawah == 0) batasCropBawah = fullHeight / 2; 
                BufferedImage croppedImage = imageCapture.getSubimage(0, 0, fullWidth, batasCropBawah);


                // 3. SETUP DOKUMEN PDF
                Document document = new Document(PageSize.A4.rotate(), 30, 30, 30, 30);
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                // --- HALAMAN 1: DASHBOARD & RINGKASAN EKSEKUTIF ---
                com.itextpdf.text.Font fontJudul = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font fontTanggal = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.NORMAL, com.itextpdf.text.BaseColor.GRAY);
                com.itextpdf.text.Font fontDeskripsi = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.NORMAL);
                
                // Judul
                com.itextpdf.text.Paragraph judulLaporan = new com.itextpdf.text.Paragraph("LAPORAN STATISTIK PEMAGANGAN - LPK YUUKI KUNINGAN", fontJudul);
                judulLaporan.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(judulLaporan);
                
                // Tanggal
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
                String waktuCetak = sdf.format(new java.util.Date());
                com.itextpdf.text.Paragraph infoCetak = new com.itextpdf.text.Paragraph("Dicetak pada: " + waktuCetak, fontTanggal);
                infoCetak.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(infoCetak);
                
                document.add(new com.itextpdf.text.Paragraph(" ")); 

                Image pdfImage = Image.getInstance(croppedImage, null);
                pdfImage.scaleToFit(document.getPageSize().getWidth() - 60, document.getPageSize().getHeight() - 150);
                pdfImage.setAlignment(Image.ALIGN_CENTER);
                document.add(pdfImage);

                document.add(new com.itextpdf.text.Paragraph(" "));
                String teksRingkasan = "Ringkasan Eksekutif:\n" +
                                       "Berdasarkan visualisasi data di atas, laporan ini merepresentasikan distribusi kelulusan " +
                                       "dan kesiapan dokumen dari seluruh peserta yang terdaftar di sistem. Rincian detail dari masing-masing " +
                                       "peserta (termasuk nomor paspor dan jadwal keberangkatan) dapat dilihat pada halaman lampiran berikutnya.";
                                       
                com.itextpdf.text.Paragraph ringkasan = new com.itextpdf.text.Paragraph(teksRingkasan, fontDeskripsi);
                ringkasan.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                document.add(ringkasan);

                document.newPage();

                com.itextpdf.text.Paragraph judulTabel = new com.itextpdf.text.Paragraph("Lampiran: Rincian Data Keberangkatan Peserta", fontJudul);
                judulTabel.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                judulTabel.setSpacingAfter(20f); 
                document.add(judulTabel);

                int jumlahKolom = tabelLaporan.getColumnCount();
                PdfPTable pdfTable = new PdfPTable(jumlahKolom);
                pdfTable.setWidthPercentage(100); 

                com.itextpdf.text.Font fontHeader = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD);
                for (int i = 0; i < jumlahKolom; i++) {
                    PdfPCell cell = new PdfPCell(new Phrase(tabelLaporan.getColumnName(i), fontHeader));
                    cell.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
                    cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    cell.setPadding(6f);
                    pdfTable.addCell(cell);
                }

                com.itextpdf.text.Font fontData = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10);
                for (int baris = 0; baris < tabelLaporan.getRowCount(); baris++) {
                    for (int kolom = 0; kolom < jumlahKolom; kolom++) {
                        Object nilaiCell = tabelLaporan.getValueAt(baris, kolom);
                        String teksCell = (nilaiCell == null) ? "" : nilaiCell.toString();
                        
                        PdfPCell cell = new PdfPCell(new Phrase(teksCell, fontData));
                        cell.setPadding(5f);
                        pdfTable.addCell(cell);
                    }
                }

                document.add(pdfTable);
                document.close();

                JOptionPane.showMessageDialog(this, 
                    "Berhasil!\nLaporan tersimpan di: " + filePath, 
                    "Ekspor Sukses", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                btnReset.setVisible(true);
                btnEkspor.setVisible(true);
                panelTabel.setVisible(true); 
                
                JOptionPane.showMessageDialog(this, 
                    "Gagal mengekspor laporan: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEksporActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barBelum;
    private javax.swing.JProgressBar barMakanan;
    private javax.swing.JProgressBar barMenunggu;
    private javax.swing.JProgressBar barMesin;
    private javax.swing.JProgressBar barPenginapan;
    private javax.swing.JProgressBar barSiap;
    private javax.swing.JButton btnEkspor;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<String> cbPeriode;
    private javax.swing.JComboBox<String> cbProgram;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel kartuTotal3;
    private javax.swing.JPanel kartuTotal4;
    private javax.swing.JPanel kartuTotal5;
    private javax.swing.JPanel kartuTotal6;
    private javax.swing.JLabel lblAngkaBelum;
    private javax.swing.JLabel lblAngkaMakanan;
    private javax.swing.JLabel lblAngkaMenunggu;
    private javax.swing.JLabel lblAngkaMesin;
    private javax.swing.JLabel lblAngkaPenginapan;
    private javax.swing.JLabel lblAngkaSiap;
    private javax.swing.JLabel lblKeteranganLulus;
    private javax.swing.JLabel lblPersentaseLulus;
    private javax.swing.JLabel lblRataNilai;
    private javax.swing.JLabel lblTotalData;
    private javax.swing.JLabel lblTotalPeserta;
    private javax.swing.JPanel panelPerProgram;
    private javax.swing.JPanel panelProgram;
    private javax.swing.JPanel panelTabel;
    private javax.swing.JPanel pnlToolbar;
    private javax.swing.JTable tabelLaporan;
    // End of variables declaration//GEN-END:variables
}
