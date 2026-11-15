package Views.peserta;

import DataStore.DataStore;
import Model.Materi;
import Model.Peserta;
import Util.TabelUtil;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 * Panel bagi Peserta untuk melihat & membuka Materi yang dibagikan Instruktur,
 * baik yang berupa teks maupun berkas, sesuai dengan kelas peserta tersebut.
 */
public class PanelLihatMateri extends javax.swing.JPanel {

    private String idPesertaAktif;
    private DefaultTableModel modelTabel;

    // Materi yang SEDANG ditampilkan di tabel (index sinkron dengan baris tabel)
    private ArrayList<Materi> materiTampil = new ArrayList<>();

    public PanelLihatMateri(String idPeserta) {
        initComponents();
        this.idPesertaAktif = idPeserta;

        setupTabel();
        loadDataMateri(""); // Muat semua data saat pertama kali dibuka
    }

    private Peserta cariPesertaAktif() {
        for (Peserta p : DataStore.daftarPeserta) {
            if (p.getIdPeserta() != null && p.getIdPeserta().equals(idPesertaAktif)) {
                return p;
            }
        }
        return null;
    }

    private void setupTabel() {
        tabelMateri.setRowHeight(34);

        modelTabel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"No.", "Judul", "Kelas", "Tipe", "Tanggal Upload"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelMateri.setModel(modelTabel);
        tabelMateri.setSelectionBackground(new java.awt.Color(230, 240, 255)); 
        tabelMateri.setSelectionForeground(new java.awt.Color(0, 50, 150));

        TabelUtil.autoResizeKolom(tabelMateri);
        javax.swing.table.JTableHeader header = tabelMateri.getTableHeader();
        header.setBackground(new java.awt.Color(40, 40, 40)); 
        header.setForeground(java.awt.Color.WHITE);
        header.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 13));
        header.setOpaque(true);
    }

    private void loadDataMateri(String keyword) {
        modelTabel.setRowCount(0);
        materiTampil.clear();

        Peserta pesertaAktif = cariPesertaAktif();
        String kelasPeserta = (pesertaAktif != null) ? pesertaAktif.getKelas() : "";

        int no = 1;
        for (Materi m : DataStore.daftarMateri) {
            boolean untukKelasIni = "Semua Kelas".equals(m.getKelasTarget()) || m.getKelasTarget().equals(kelasPeserta);
            boolean cocokDenganPencarian = keyword.isEmpty() || m.getJudul().toLowerCase().contains(keyword.toLowerCase());
            
            if (untukKelasIni && cocokDenganPencarian) {
                modelTabel.addRow(new Object[]{
                    no++,
                    m.getJudul(),
                    m.getKelasTarget(),
                    m.getTipe(),
                    m.getTanggalUpload()
                });
                materiTampil.add(m);
            }
        }
        
        jLabelTotalMateri.setText("Total Materi: " + materiTampil.size());
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        setBackground(new java.awt.Color(245, 245, 245));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        // --- Judul Halaman ---
        jLabelJudulHalaman = new javax.swing.JLabel();
        jLabelJudulHalaman.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 16));
        jLabelJudulHalaman.setForeground(new java.awt.Color(50, 50, 50));
        jLabelJudulHalaman.setText("Materi Belajar Anda");
        add(jLabelJudulHalaman, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 30));

        // --- Info Total Materi ---
        jLabelTotalMateri = new javax.swing.JLabel();
        jLabelTotalMateri.setFont(new java.awt.Font("Inter", 0, 12));
        jLabelTotalMateri.setForeground(new java.awt.Color(120, 120, 120));
        jLabelTotalMateri.setText("Total Materi: 0");
        add(jLabelTotalMateri, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 25, 150, 20));

        // --- Fitur Pencarian ---
        jLabelCari = new javax.swing.JLabel();
        jLabelCari.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        jLabelCari.setForeground(new java.awt.Color(70, 70, 70));
        jLabelCari.setText("Cari Materi:");
        add(jLabelCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 65, -1, 32));

        txtCari = new javax.swing.JTextField();
        txtCari.setFont(new java.awt.Font("Inter", 0, 12));
        txtCari.setToolTipText("Ketik judul materi di sini...");
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariKeyReleased(evt);
            }
        });
        add(txtCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 65, 290, 32));

        // --- Tombol Aksi ---
        btnRefresh = new javax.swing.JButton();
        btnRefresh.setBackground(new java.awt.Color(220, 220, 220));
        btnRefresh.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        btnRefresh.setForeground(new java.awt.Color(50, 50, 50));
        btnRefresh.setText("Segarkan");
        btnRefresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadDataMateri(txtCari.getText());
            }
        });
        add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 65, 100, 32));

        // TOMBOL BARU: Unduh Materi
        btnUnduhMateri = new javax.swing.JButton();
        btnUnduhMateri.setBackground(new java.awt.Color(0, 102, 204)); // Warna biru untuk aksi download
        btnUnduhMateri.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        btnUnduhMateri.setForeground(new java.awt.Color(255, 255, 255));
        btnUnduhMateri.setText("Unduh Berkas");
        btnUnduhMateri.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUnduhMateri.setFocusPainted(false);
        btnUnduhMateri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnduhMateriActionPerformed(evt);
            }
        });
        add(btnUnduhMateri, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 65, 140, 32));

        btnBukaMateri = new javax.swing.JButton();
        btnBukaMateri.setBackground(new java.awt.Color(20, 120, 40)); 
        btnBukaMateri.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        btnBukaMateri.setForeground(new java.awt.Color(255, 255, 255));
        btnBukaMateri.setText("Buka Materi Terpilih");
        btnBukaMateri.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBukaMateri.setFocusPainted(false);
        btnBukaMateri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBukaMateriActionPerformed(evt);
            }
        });
        add(btnBukaMateri, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 65, 160, 32));

        // --- Tabel Area ---
        panelTabel = new javax.swing.JPanel();
        panelTabel.setBackground(new java.awt.Color(255, 255, 255));
        panelTabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelTabel.setLayout(new java.awt.BorderLayout()); 

        scrollTabel = new javax.swing.JScrollPane();
        scrollTabel.setBorder(null); 
        tabelMateri = new javax.swing.JTable();
        tabelMateri.setFont(new java.awt.Font("Inter", 0, 12));
        tabelMateri.setGridColor(new java.awt.Color(240, 240, 240));
        tabelMateri.setShowHorizontalLines(true);
        tabelMateri.setShowVerticalLines(false); 
        scrollTabel.setViewportView(tabelMateri);
        
        panelTabel.add(scrollTabel, java.awt.BorderLayout.CENTER);
        add(panelTabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 900, 470));
    }

    private void txtCariKeyReleased(java.awt.event.KeyEvent evt) {
        String keyword = txtCari.getText().trim();
        loadDataMateri(keyword);
    }

    private void btnBukaMateriActionPerformed(java.awt.event.ActionEvent evt) {
        int baris = tabelMateri.getSelectedRow();
        if (baris == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Pilih dahulu materi yang ingin dibuka dari tabel!", "Peringatan",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        Materi materiTerpilih = materiTampil.get(baris);

        if ("Teks".equals(materiTerpilih.getTipe())) {
            javax.swing.JTextArea areaIsi = new javax.swing.JTextArea(materiTerpilih.getIsiTeks());
            areaIsi.setLineWrap(true);
            areaIsi.setWrapStyleWord(true);
            areaIsi.setEditable(false);
            areaIsi.setFont(new java.awt.Font("Inter", 0, 13));
            areaIsi.setMargin(new java.awt.Insets(10, 10, 10, 10)); 

            javax.swing.JScrollPane scrollIsi = new javax.swing.JScrollPane(areaIsi);
            scrollIsi.setPreferredSize(new java.awt.Dimension(550, 350));
            scrollIsi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));

            javax.swing.JOptionPane.showMessageDialog(this, scrollIsi,
                "Materi: " + materiTerpilih.getJudul(), javax.swing.JOptionPane.PLAIN_MESSAGE);

        } else {
            try {
                String pathFile = System.getProperty("user.dir") + "/Brankas_Materi/"
                        + materiTerpilih.getIdInstruktur() + "/" + materiTerpilih.getNamaFileTersimpan();
                java.io.File file = new java.io.File(pathFile);

                if (!file.exists()) {
                    javax.swing.JOptionPane.showMessageDialog(this,
                        "Berkas materi tidak ditemukan di perangkat ini.", "Peringatan",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop.getDesktop().open(file);
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this,
                        "Perangkat ini tidak mendukung pembukaan berkas otomatis.\nLokasi berkas: " + pathFile,
                        "Info", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Gagal membuka berkas: " + e.getMessage(), "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // LOGIKA BARU UNTUK MENGUNDUH MATERI
    private void btnUnduhMateriActionPerformed(java.awt.event.ActionEvent evt) {
        int baris = tabelMateri.getSelectedRow();
        if (baris == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Pilih dahulu materi yang ingin diunduh dari tabel!", "Peringatan",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        Materi materiTerpilih = materiTampil.get(baris);

        if ("Teks".equalsIgnoreCase(materiTerpilih.getTipe())) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Materi ini berupa teks bacaan langsung, bukan berkas yang bisa diunduh.\nSilakan gunakan tombol 'Buka Materi Terpilih'.", 
                "Info", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String folderAsal = System.getProperty("user.dir") + "/Brankas_Materi/" + materiTerpilih.getIdInstruktur();
        java.io.File fileAsal = new java.io.File(folderAsal, materiTerpilih.getNamaFileTersimpan());

        if (!fileAsal.exists()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Berkas tidak ditemukan di sistem! Mungkin instruktur sudah menghapusnya.", "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Simpan Materi Sebagai...");
        fileChooser.setSelectedFile(new java.io.File(materiTerpilih.getNamaFileAsli()));

        int pilihan = fileChooser.showSaveDialog(this);

        if (pilihan == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File fileTujuan = fileChooser.getSelectedFile();

            try {
                java.nio.file.Path sumberPath = fileAsal.toPath();
                java.nio.file.Path targetPath = fileTujuan.toPath();
                java.nio.file.Files.copy(sumberPath, targetPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                javax.swing.JOptionPane.showMessageDialog(this,
                    "Materi berhasil diunduh dan disimpan!",
                    "Sukses", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Gagal mengunduh berkas: " + e.getMessage(), "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private javax.swing.JButton btnBukaMateri;
    private javax.swing.JButton btnUnduhMateri; // Deklarasi tombol baru
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel jLabelCari;
    private javax.swing.JLabel jLabelJudulHalaman;
    private javax.swing.JLabel jLabelTotalMateri;
    private javax.swing.JPanel panelTabel;
    private javax.swing.JScrollPane scrollTabel;
    private javax.swing.JTable tabelMateri;
    private javax.swing.JTextField txtCari;
}