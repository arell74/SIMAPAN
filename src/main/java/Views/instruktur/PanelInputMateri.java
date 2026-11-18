/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views.instruktur;

import DataStore.DataStore;
import Database.MateriDAO;
import Model.Instruktur;
import Model.Materi;
import Model.Pengguna;
import Util.TabelUtil;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 * Panel bagi Instruktur untuk mengupload materi belajar (berupa teks yang
 * ditulis langsung, atau berkas seperti PDF/DOCX/gambar) yang nantinya bisa
 * diakses oleh peserta.
 *
 * @author arelssi
 */
public class PanelInputMateri extends javax.swing.JPanel {

    private String idInstrukturAktif;
    private DefaultTableModel modelTabel;

    // Daftar materi yang SEDANG ditampilkan di tabel (index sinkron dengan baris tabel)
    private ArrayList<Materi> materiTampil = new ArrayList<>();

    private static final String KARTU_TEKS = "TEKS";
    private static final String KARTU_FILE = "FILE";

    /**
     * Creates new form PanelInputMateri
     */
    public PanelInputMateri(String idInstruktur) {
        initComponents();

        this.idInstrukturAktif = idInstruktur;

        setupComboBox();
        setupTabelMateri();
        loadDataMateri();
    }

    private void setupComboBox() {
        cbKelasTarget.removeAllItems();
        cbKelasTarget.addItem("Semua Kelas");
        cbKelasTarget.addItem("Kelas A");
        cbKelasTarget.addItem("Kelas B");

        cbTipeMateri.removeAllItems();
        cbTipeMateri.addItem("Teks");
        cbTipeMateri.addItem("File");

        cbTipeMateri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String pilihan = String.valueOf(cbTipeMateri.getSelectedItem());
                java.awt.CardLayout cl = (java.awt.CardLayout) panelDinamis.getLayout();
                if ("File".equals(pilihan)) {
                    cl.show(panelDinamis, KARTU_FILE);
                } else {
                    cl.show(panelDinamis, KARTU_TEKS);
                }
            }
        });
    }

    private void setupTabelMateri() {
        tabelMateri.setRowHeight(36);

        modelTabel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"No.", "Judul", "Kelas", "Tipe", "Isi / Nama File", "Tanggal Upload"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelMateri.setModel(modelTabel);
        tabelMateri.setSelectionBackground(new java.awt.Color(255, 230, 230));
        tabelMateri.setSelectionForeground(new java.awt.Color(122, 0, 0));

        TabelUtil.autoResizeKolom(tabelMateri);
        javax.swing.table.JTableHeader header = tabelMateri.getTableHeader();
        header.setBackground(new java.awt.Color(122, 0, 0));
        header.setForeground(java.awt.Color.WHITE);
        header.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 13));
        header.setOpaque(true);
    }

    /**
     * Mencari idInstruktur "asli" (Model.Instruktur) berdasarkan idInstrukturAktif
     * yang dikirim (bisa berupa idInstruktur ataupun username).
     */
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

    private String generateIdMateri() {
        int maxNum = 0;
        for (Materi m : DataStore.daftarMateri) {
            if (m.getIdMateri() != null && m.getIdMateri().startsWith("MAT")) {
                try {
                    int num = Integer.parseInt(m.getIdMateri().substring(3));
                    if (num > maxNum) {
                        maxNum = num;
                    }
                } catch (NumberFormatException ignore) {
                }
            }
        }
        return "MAT" + String.format("%03d", maxNum + 1);
    }

    private void loadDataMateri() {
        DataStore.muatUlang();

        modelTabel.setRowCount(0);
        materiTampil.clear();

        String idAsli = cariIdAsliInstruktur();

        int no = 1;
        for (Materi m : DataStore.daftarMateri) {
            if (m.getIdInstruktur() != null && m.getIdInstruktur().equals(idAsli)) {
                modelTabel.addRow(new Object[]{
                    no++,
                    m.getJudul(),
                    m.getKelasTarget(),
                    m.getTipe(),
                    m.getRingkasanIsi(),
                    m.getTanggalUpload()
                });
                materiTampil.add(m);
            }
        }
        TabelUtil.autoResizeKolom(tabelMateri);
    }

    private void resetForm() {
        txtJudulMateri.setText("");
        txtAreaIsiTeks.setText("");
        txtPathFile.setText("");
        cbKelasTarget.setSelectedIndex(0);
        cbTipeMateri.setSelectedIndex(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelToolbar = new javax.swing.JPanel();
        panelToolbar.setBackground(new java.awt.Color(255, 255, 255));
        panelToolbar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelToolbar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelToolbar.setPreferredSize(new java.awt.Dimension(940, 190));

        jLabelJudulForm = new javax.swing.JLabel();
        jLabelJudulForm.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 13));
        jLabelJudulForm.setForeground(new java.awt.Color(70, 70, 70));
        jLabelJudulForm.setText("Upload Materi Baru");
        panelToolbar.add(jLabelJudulForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 12, -1, 20));

        jLabelSubForm = new javax.swing.JLabel();
        jLabelSubForm.setFont(new java.awt.Font("Inter", 0, 10));
        jLabelSubForm.setForeground(new java.awt.Color(140, 140, 140));
        jLabelSubForm.setText("Bagikan materi berupa teks atau berkas kepada peserta didikmu");
        panelToolbar.add(jLabelSubForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 32, -1, 16));

        jLabelJudul = new javax.swing.JLabel();
        jLabelJudul.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 11));
        jLabelJudul.setForeground(new java.awt.Color(60, 60, 60));
        jLabelJudul.setText("Judul Materi");
        panelToolbar.add(jLabelJudul, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 58, -1, 16));

        txtJudulMateri = new javax.swing.JTextField();
        txtJudulMateri.setFont(new java.awt.Font("Inter", 0, 12));
        panelToolbar.add(txtJudulMateri, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 76, 320, 30));

        jLabelKelas = new javax.swing.JLabel();
        jLabelKelas.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 11));
        jLabelKelas.setForeground(new java.awt.Color(60, 60, 60));
        jLabelKelas.setText("Kelas Target");
        panelToolbar.add(jLabelKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 58, -1, 16));

        cbKelasTarget = new javax.swing.JComboBox<>();
        panelToolbar.add(cbKelasTarget, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 76, 150, 30));

        jLabelTipe = new javax.swing.JLabel();
        jLabelTipe.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 11));
        jLabelTipe.setForeground(new java.awt.Color(60, 60, 60));
        jLabelTipe.setText("Tipe Materi");
        panelToolbar.add(jLabelTipe, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 58, -1, 16));

        cbTipeMateri = new javax.swing.JComboBox<>();
        panelToolbar.add(cbTipeMateri, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 76, 150, 30));

        btnUpload = new javax.swing.JButton();
        btnUpload.setBackground(new java.awt.Color(122, 0, 0));
        btnUpload.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        btnUpload.setForeground(new java.awt.Color(255, 255, 255));
        btnUpload.setText("Upload Materi");
        btnUpload.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpload.setFocusPainted(false);
        btnUpload.setPreferredSize(new java.awt.Dimension(150, 30));
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });
        panelToolbar.add(btnUpload, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 76, 150, 30));

        panelDinamis = new javax.swing.JPanel();
        panelDinamis.setLayout(new java.awt.CardLayout());
        panelDinamis.setOpaque(false);

        panelKartuTeks = new javax.swing.JPanel();
        panelKartuTeks.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelKartuTeks.setOpaque(false);

        txtAreaIsiTeks = new javax.swing.JTextArea();
        txtAreaIsiTeks.setFont(new java.awt.Font("Inter", 0, 12));
        txtAreaIsiTeks.setLineWrap(true);
        txtAreaIsiTeks.setWrapStyleWord(true);
        txtAreaIsiTeks.setRows(4);

        scrollIsiTeks = new javax.swing.JScrollPane();
        scrollIsiTeks.setViewportView(txtAreaIsiTeks);
        panelKartuTeks.add(scrollIsiTeks, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 80));

        panelDinamis.add(panelKartuTeks, KARTU_TEKS);

        panelKartuFile = new javax.swing.JPanel();
        panelKartuFile.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelKartuFile.setOpaque(false);

        txtPathFile = new javax.swing.JTextField();
        txtPathFile.setFont(new java.awt.Font("Inter", 0, 12));
        txtPathFile.setEditable(false);
        panelKartuFile.add(txtPathFile, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 700, 30));

        btnPilihFile = new javax.swing.JButton();
        btnPilihFile.setBackground(new java.awt.Color(240, 240, 240));
        btnPilihFile.setFont(new java.awt.Font("Inter", 0, 11));
        btnPilihFile.setText("Pilih Berkas...");
        btnPilihFile.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPilihFile.setFocusPainted(false);
        btnPilihFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihFileActionPerformed(evt);
            }
        });
        panelKartuFile.add(btnPilihFile, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 10, 130, 30));

        panelDinamis.add(panelKartuFile, KARTU_FILE);

        panelToolbar.add(panelDinamis, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 118, 900, 60));

        add(panelToolbar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, -1, -1));

        jLabelDaftarMateri = new javax.swing.JLabel();
        jLabelDaftarMateri.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 13));
        jLabelDaftarMateri.setForeground(new java.awt.Color(70, 70, 70));
        jLabelDaftarMateri.setText("Daftar Materi yang Sudah Diupload");
        add(jLabelDaftarMateri, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 218, -1, 20));

        btnHapusMateri = new javax.swing.JButton();
        btnHapusMateri.setBackground(new java.awt.Color(180, 40, 40));
        btnHapusMateri.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 11));
        btnHapusMateri.setForeground(new java.awt.Color(255, 255, 255));
        btnHapusMateri.setText("Hapus Materi Terpilih");
        btnHapusMateri.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHapusMateri.setFocusPainted(false);
        btnHapusMateri.setPreferredSize(new java.awt.Dimension(160, 28));
        btnHapusMateri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusMateriActionPerformed(evt);
            }
        });
        add(btnHapusMateri, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 212, 160, 28));

        panelTabel = new javax.swing.JPanel();
        panelTabel.setBackground(new java.awt.Color(255, 255, 255));
        panelTabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelTabel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollTabel = new javax.swing.JScrollPane();
        tabelMateri = new javax.swing.JTable();
        tabelMateri.setFont(new java.awt.Font("Inter", 0, 12));
        scrollTabel.setViewportView(tabelMateri);
        scrollTabel.setPreferredSize(new java.awt.Dimension(940, 240));
        panelTabel.add(scrollTabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        add(panelTabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 245, 940, 240));
    }// </editor-fold>//GEN-END:initComponents

    private void btnPilihFileActionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Pilih Berkas Materi");

        javax.swing.filechooser.FileNameExtensionFilter filter =
            new javax.swing.filechooser.FileNameExtensionFilter(
                "Dokumen & Media (PDF, DOC, PPT, Gambar, Video)",
                "pdf", "doc", "docx", "ppt", "pptx", "xls", "xlsx",
                "jpg", "jpeg", "png", "mp4", "mp3", "zip");
        fileChooser.setFileFilter(filter);

        int pilihan = fileChooser.showOpenDialog(this);

        if (pilihan == javax.swing.JFileChooser.APPROVE_OPTION) {
            File fileTerpilih = fileChooser.getSelectedFile();
            txtPathFile.setText(fileTerpilih.getAbsolutePath());
        }
    }

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {
        String judul = txtJudulMateri.getText().trim();
        String kelasTarget = String.valueOf(cbKelasTarget.getSelectedItem());
        String tipe = String.valueOf(cbTipeMateri.getSelectedItem());

        if (judul.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Judul materi tidak boleh kosong!", "Peringatan",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        DataStore.muatUlang();

        String idAsli = cariIdAsliInstruktur();
        String idMateriBaru = generateIdMateri();
        String tanggal = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());

        Materi materiBaru;
        Path targetFileTersalin = null;

        if ("Teks".equals(tipe)) {
            String isiTeks = txtAreaIsiTeks.getText().trim();
            if (isiTeks.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Isi materi (teks) tidak boleh kosong!", "Peringatan",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            materiBaru = new Materi(idMateriBaru, idAsli, judul, kelasTarget,
                "Teks", isiTeks, null, null, tanggal);

        } else {
            String pathAsli = txtPathFile.getText().trim();
            if (pathAsli.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Pilih berkas materi terlebih dahulu!", "Peringatan",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String folderTujuan = System.getProperty("user.dir") + "/Brankas_Materi/" + idAsli;
                File direktori = new File(folderTujuan);
                if (!direktori.exists()) {
                    direktori.mkdirs();
                }

                File fileAsli = new File(pathAsli);
                String namaAsli = fileAsli.getName();
                String ekstensi = namaAsli.contains(".") ? namaAsli.substring(namaAsli.lastIndexOf(".")) : "";
                String namaFileBaru = idMateriBaru + ekstensi;

                Path sumberPath = Paths.get(pathAsli);
                targetFileTersalin = Paths.get(folderTujuan, namaFileBaru);
                Files.copy(sumberPath, targetFileTersalin, StandardCopyOption.REPLACE_EXISTING);

                materiBaru = new Materi(idMateriBaru, idAsli, judul, kelasTarget,
                    "File", null, namaAsli, namaFileBaru, tanggal);

            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Gagal mengupload berkas: " + e.getMessage(), "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
        }

        if (MateriDAO.tambah(materiBaru)) {
            DataStore.muatUlang();

            javax.swing.JOptionPane.showMessageDialog(this,
                "Materi \"" + judul + "\" berhasil disimpan ke database untuk " + kelasTarget + "!",
                "Sukses", javax.swing.JOptionPane.INFORMATION_MESSAGE);

            resetForm();
            loadDataMateri();
        } else if (targetFileTersalin != null) {
            try {
                Files.deleteIfExists(targetFileTersalin);
            } catch (Exception ignore) {
            }
        }
    }

    private void btnHapusMateriActionPerformed(java.awt.event.ActionEvent evt) {
        int baris = tabelMateri.getSelectedRow();
        if (baris == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Pilih dahulu materi yang ingin dihapus dari tabel!", "Peringatan",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int konfirmasi = javax.swing.JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus materi ini dari database?", "Konfirmasi Hapus",
            javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);

        if (konfirmasi == javax.swing.JOptionPane.YES_OPTION) {
            Materi materiTerpilih = materiTampil.get(baris);
            if (MateriDAO.hapus(materiTerpilih.getIdMateri())) {
                if ("File".equalsIgnoreCase(materiTerpilih.getTipe())
                        && materiTerpilih.getNamaFileTersimpan() != null) {
                    try {
                        Path fileBrankas = Paths.get(
                            System.getProperty("user.dir"),
                            "Brankas_Materi",
                            materiTerpilih.getIdInstruktur(),
                            materiTerpilih.getNamaFileTersimpan()
                        );
                        Files.deleteIfExists(fileBrankas);
                    } catch (Exception ignore) {
                    }
                }

                DataStore.muatUlang();
                loadDataMateri();
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Materi berhasil dihapus dari database!", "Sukses",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // Variables declaration
    private javax.swing.JButton btnHapusMateri;
    private javax.swing.JButton btnPilihFile;
    private javax.swing.JButton btnUpload;
    private javax.swing.JComboBox<String> cbKelasTarget;
    private javax.swing.JComboBox<String> cbTipeMateri;
    private javax.swing.JLabel jLabelDaftarMateri;
    private javax.swing.JLabel jLabelJudul;
    private javax.swing.JLabel jLabelJudulForm;
    private javax.swing.JLabel jLabelKelas;
    private javax.swing.JLabel jLabelSubForm;
    private javax.swing.JLabel jLabelTipe;
    private javax.swing.JPanel panelDinamis;
    private javax.swing.JPanel panelKartuFile;
    private javax.swing.JPanel panelKartuTeks;
    private javax.swing.JPanel panelTabel;
    private javax.swing.JPanel panelToolbar;
    private javax.swing.JScrollPane scrollIsiTeks;
    private javax.swing.JScrollPane scrollTabel;
    private javax.swing.JTable tabelMateri;
    private javax.swing.JTextArea txtAreaIsiTeks;
    private javax.swing.JTextField txtJudulMateri;
    private javax.swing.JTextField txtPathFile;
    // End of variables declaration
}