package Views.admin;

import DataStore.DataStore;
import Database.DokumenDAO;
import Database.PesertaDAO;
import Model.Dokumen;
import Model.Peserta;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class FormEditDokumen extends javax.swing.JFrame {

    private String idTarget;
    private Dokumen dokumenTarget;
    private PanelDokumenBerangkat panelInduk;

    // Menyimpan path sumber jika admin mengganti file berkas
    private String pathSumberKtp = null;
    private String pathSumberPaspor = null;
    private String pathSumberMcu = null;
    private String pathSumberVisa = null;

    // --- Komponen GUI ---
    private JPanel panelHeader, panelBody, panelFooter;
    private JLabel lblJudulHeader, lblSubHeader;
    private JButton btnTutupHeader, btnBatal, btnSimpan;

    private JTextField txtIdDokumen, txtIdPeserta, txtNamaPeserta, txtTanggalBerangkat;
    private JComboBox<String> cmbStatusKeberangkatan;

    private JTextField txtNikKtp, txtFileKtp;
    private JTextField txtNoPaspor, txtFilePaspor;
    private JTextField txtHasilMcu, txtFileMcu;
    private JTextField txtNoVisa, txtFileVisa;

    private JButton btnPilihKtp, btnPilihPaspor, btnPilihMcu, btnPilihVisa;

    // --- Warna & Font Konvensi SIMAPAN ---
    private final Color MAROON_UTAMA = new Color(122, 0, 0);
    private final Color MAROON_GELAP = new Color(160, 20, 20);
    private final Color BG_BODY = Color.WHITE;
    private final Color BG_FOOTER = new Color(248, 248, 248);
    private final Color BG_READONLY = new Color(242, 242, 242);
    private final Color TXT_ABU = new Color(80, 80, 80);
    private final Color TXT_UTAMA = new Color(30, 30, 30);

    private final Font FONT_BOLD_10 = new Font("Inter", Font.BOLD, 10);
    private final Font FONT_BOLD_11 = new Font("Inter", Font.BOLD, 11);
    private final Font FONT_BOLD_12 = new Font("Inter", Font.BOLD, 12);
    private final Font FONT_PLAIN_11 = new Font("Inter", Font.PLAIN, 11);
    private final Font FONT_BOLD_15 = new Font("Inter", Font.BOLD, 15);

    /**
     * Constructor utama memakai idDokumen (konsisten dengan FormEditPeserta)
     */
    public FormEditDokumen(java.awt.Frame parent, boolean modal, String idDokumen) {
        this.idTarget = idDokumen;
        setupWindow(parent);
        initComponentsManual();
        loadData();
    }

    /**
     * Constructor alternatif jika dipanggil langsung dari PanelDokumenBerangkat
     */
    public FormEditDokumen(PanelDokumenBerangkat panelInduk, Dokumen dokumenDikirim) {
        this.panelInduk = panelInduk;
        this.idTarget = (dokumenDikirim != null) ? dokumenDikirim.getIdDokumen() : "";
        setupWindow(null);
        initComponentsManual();
        loadData();
    }

    private void setupWindow(java.awt.Frame parent) {
        this.setTitle("Edit Kelengkapan Dokumen");
        this.setSize(680, 565);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
        this.getContentPane().setLayout(new AbsoluteLayout());
    }

    private void initComponentsManual() {
        // ==========================================
        // 1. HEADER
        // ==========================================
        panelHeader = new JPanel(new AbsoluteLayout());
        panelHeader.setBackground(MAROON_UTAMA);

        lblJudulHeader = new JLabel("Edit Kelengkapan Dokumen");
        lblJudulHeader.setForeground(Color.WHITE);
        lblJudulHeader.setFont(FONT_BOLD_15);
        panelHeader.add(lblJudulHeader, new AbsoluteConstraints(20, 14, 380, 22));

        lblSubHeader = new JLabel("Perbarui nomor dokumen, berkas lampiran, dan status keberangkatan");
        lblSubHeader.setForeground(new Color(255, 200, 200));
        lblSubHeader.setFont(FONT_PLAIN_11);
        panelHeader.add(lblSubHeader, new AbsoluteConstraints(20, 38, 420, 16));

        btnTutupHeader = new JButton("X");
        btnTutupHeader.setBackground(MAROON_GELAP);
        btnTutupHeader.setForeground(Color.WHITE);
        btnTutupHeader.setFont(FONT_BOLD_12);
        btnTutupHeader.setBorderPainted(false);
        btnTutupHeader.setFocusPainted(false);
        btnTutupHeader.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTutupHeader.addActionListener(e -> this.dispose());
        panelHeader.add(btnTutupHeader, new AbsoluteConstraints(625, 18, 35, 30));

        this.getContentPane().add(panelHeader, new AbsoluteConstraints(0, 0, 680, 68));

        // ==========================================
        // 2. BODY
        // ==========================================
        panelBody = new JPanel(new AbsoluteLayout());
        panelBody.setBackground(BG_BODY);

        // --- SECTION 1: IDENTITAS & KEBERANGKATAN ---
        JLabel lblSec1 = createSectionLabel("DATA PESERTA & JADWAL KEBERANGKATAN");
        panelBody.add(lblSec1, new AbsoluteConstraints(20, 12, 350, 16));

        JSeparator sep1 = new JSeparator();
        panelBody.add(sep1, new AbsoluteConstraints(20, 30, 640, 8));

        panelBody.add(createFieldLabel("ID Dokumen"), new AbsoluteConstraints(20, 38, 120, 16));
        txtIdDokumen = createReadOnlyField();
        panelBody.add(txtIdDokumen, new AbsoluteConstraints(20, 56, 130, 30));

        panelBody.add(createFieldLabel("ID Peserta"), new AbsoluteConstraints(165, 38, 120, 16));
        txtIdPeserta = createReadOnlyField();
        panelBody.add(txtIdPeserta, new AbsoluteConstraints(165, 56, 135, 30));

        panelBody.add(createFieldLabel("Nama Lengkap Peserta"), new AbsoluteConstraints(315, 38, 250, 16));
        txtNamaPeserta = createReadOnlyField();
        panelBody.add(txtNamaPeserta, new AbsoluteConstraints(315, 56, 345, 30));

        panelBody.add(createFieldLabel("Estimasi Tanggal Berangkat (YYYY-MM-DD)"), new AbsoluteConstraints(20, 96, 280, 16));
        txtTanggalBerangkat = createEditableField();
        panelBody.add(txtTanggalBerangkat, new AbsoluteConstraints(20, 114, 280, 30));

        panelBody.add(createFieldLabel("Status Keberangkatan Peserta"), new AbsoluteConstraints(315, 96, 280, 16));
        cmbStatusKeberangkatan = new JComboBox<>(new String[]{
            "Belum Berangkat", "Menunggu Dokumen", "Siap Berangkat", "Sudah Berangkat"
        });
        cmbStatusKeberangkatan.setFont(FONT_PLAIN_11);
        panelBody.add(cmbStatusKeberangkatan, new AbsoluteConstraints(315, 114, 345, 30));

        // --- SECTION 2: BERKAS PERSYARATAN ---
        JLabel lblSec2 = createSectionLabel("DETAIL BERKAS PERSYARATAN (KOSONGKAN JIKA BELUM ADA)");
        panelBody.add(lblSec2, new AbsoluteConstraints(20, 158, 450, 16));

        JSeparator sep2 = new JSeparator();
        panelBody.add(sep2, new AbsoluteConstraints(20, 176, 640, 8));

        // Row 1: E-KTP
        panelBody.add(createFieldLabel("NIK E-KTP"), new AbsoluteConstraints(20, 184, 200, 16));
        txtNikKtp = createEditableField();
        panelBody.add(txtNikKtp, new AbsoluteConstraints(20, 202, 260, 30));

        panelBody.add(createFieldLabel("Berkas KTP (PDF/JPG/PNG)"), new AbsoluteConstraints(295, 184, 220, 16));
        txtFileKtp = createReadOnlyField();
        panelBody.add(txtFileKtp, new AbsoluteConstraints(295, 202, 265, 30));

        btnPilihKtp = createBrowseButton();
        btnPilihKtp.addActionListener(e -> pilihBerkas("KTP"));
        panelBody.add(btnPilihKtp, new AbsoluteConstraints(568, 202, 92, 30));

        // Row 2: Paspor
        panelBody.add(createFieldLabel("Nomor Paspor"), new AbsoluteConstraints(20, 240, 200, 16));
        txtNoPaspor = createEditableField();
        panelBody.add(txtNoPaspor, new AbsoluteConstraints(20, 258, 260, 30));

        panelBody.add(createFieldLabel("Berkas Paspor"), new AbsoluteConstraints(295, 240, 220, 16));
        txtFilePaspor = createReadOnlyField();
        panelBody.add(txtFilePaspor, new AbsoluteConstraints(295, 258, 265, 30));

        btnPilihPaspor = createBrowseButton();
        btnPilihPaspor.addActionListener(e -> pilihBerkas("Paspor"));
        panelBody.add(btnPilihPaspor, new AbsoluteConstraints(568, 258, 92, 30));

        // Row 3: Medical Check-Up
        panelBody.add(createFieldLabel("Hasil / Keterangan MCU"), new AbsoluteConstraints(20, 296, 200, 16));
        txtHasilMcu = createEditableField();
        panelBody.add(txtHasilMcu, new AbsoluteConstraints(20, 314, 260, 30));

        panelBody.add(createFieldLabel("Berkas Hasil MCU"), new AbsoluteConstraints(295, 296, 220, 16));
        txtFileMcu = createReadOnlyField();
        panelBody.add(txtFileMcu, new AbsoluteConstraints(295, 314, 265, 30));

        btnPilihMcu = createBrowseButton();
        btnPilihMcu.addActionListener(e -> pilihBerkas("MedicalCheck-Up"));
        panelBody.add(btnPilihMcu, new AbsoluteConstraints(568, 314, 92, 30));

        // Row 4: Visa
        panelBody.add(createFieldLabel("Nomor Visa / CoE"), new AbsoluteConstraints(20, 352, 200, 16));
        txtNoVisa = createEditableField();
        panelBody.add(txtNoVisa, new AbsoluteConstraints(20, 370, 260, 30));

        panelBody.add(createFieldLabel("Berkas Visa"), new AbsoluteConstraints(295, 352, 220, 16));
        txtFileVisa = createReadOnlyField();
        panelBody.add(txtFileVisa, new AbsoluteConstraints(295, 370, 265, 30));

        btnPilihVisa = createBrowseButton();
        btnPilihVisa.addActionListener(e -> pilihBerkas("Visa"));
        panelBody.add(btnPilihVisa, new AbsoluteConstraints(568, 370, 92, 30));

        this.getContentPane().add(panelBody, new AbsoluteConstraints(0, 68, 680, 415));

        // ==========================================
        // 3. FOOTER
        // ==========================================
        panelFooter = new JPanel(new AbsoluteLayout());
        panelFooter.setBackground(BG_FOOTER);

        JSeparator sepFooter = new JSeparator();
        sepFooter.setForeground(new Color(220, 220, 220));
        panelFooter.add(sepFooter, new AbsoluteConstraints(0, 0, 680, 2));

        btnBatal = new JButton("Batal");
        btnBatal.setFont(FONT_PLAIN_11);
        btnBatal.setForeground(TXT_ABU);
        btnBatal.setBackground(Color.WHITE);
        btnBatal.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        btnBatal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBatal.addActionListener(e -> this.dispose());
        panelFooter.add(btnBatal, new AbsoluteConstraints(400, 12, 100, 34));

        btnSimpan = new JButton("Simpan Perubahan");
        btnSimpan.setFont(FONT_BOLD_12);
        btnSimpan.setBackground(MAROON_UTAMA);
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setBorderPainted(false);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSimpan.addActionListener(e -> simpanPerubahan());
        panelFooter.add(btnSimpan, new AbsoluteConstraints(510, 12, 150, 34));

        this.getContentPane().add(panelFooter, new AbsoluteConstraints(0, 483, 680, 60));
    }

    // --- Helper UI ---
    private JLabel createSectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_BOLD_10);
        lbl.setForeground(MAROON_UTAMA);
        return lbl;
    }

    private JLabel createFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_BOLD_11);
        lbl.setForeground(TXT_ABU);
        return lbl;
    }

    private JTextField createEditableField() {
        JTextField field = new JTextField();
        field.setFont(FONT_PLAIN_11);
        field.setForeground(TXT_UTAMA);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return field;
    }

    private JTextField createReadOnlyField() {
        JTextField field = createEditableField();
        field.setEditable(false);
        field.setBackground(BG_READONLY);
        field.setForeground(new Color(100, 100, 100));
        return field;
    }

    private JButton createBrowseButton() {
        JButton btn = new JButton("Pilih File");
        btn.setFont(FONT_BOLD_11);
        btn.setBackground(new Color(235, 235, 235));
        btn.setForeground(TXT_UTAMA);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- Load Data dari Cache / MySQL ---
    private void loadData() {
        DataStore.muatUlang();

        for (Dokumen dok : DataStore.daftarDokumen) {
            if (dok.getIdDokumen().equals(idTarget)) {
                this.dokumenTarget = dok;
                break;
            }
        }

        if (dokumenTarget == null || dokumenTarget.getPeserta() == null) {
            JOptionPane.showMessageDialog(this, "Data dokumen tidak ditemukan di database!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
            return;
        }

        Peserta p = dokumenTarget.getPeserta();
        txtIdDokumen.setText(dokumenTarget.getIdDokumen());
        txtIdPeserta.setText(p.getIdPeserta());
        txtNamaPeserta.setText(p.getNamaLengkap());

        String tgl = dokumenTarget.getTanggalBerangkat();
        txtTanggalBerangkat.setText(tgl != null ? tgl : "");

        if (p.getStatusKeberangkatan() != null) {
            cmbStatusKeberangkatan.setSelectedItem(p.getStatusKeberangkatan());
        }

        txtNikKtp.setText(nilaiTeks(dokumenTarget.getNikKtp()));
        txtFileKtp.setText(nilaiTeks(dokumenTarget.getFileKtp()));

        txtNoPaspor.setText(nilaiTeks(dokumenTarget.getNoPaspor()));
        txtFilePaspor.setText(nilaiTeks(dokumenTarget.getFilePaspor()));

        txtHasilMcu.setText(nilaiTeks(dokumenTarget.getHasilMcu()));
        txtFileMcu.setText(nilaiTeks(dokumenTarget.getFileMcu()));

        txtNoVisa.setText(nilaiTeks(dokumenTarget.getNoVisa()));
        txtFileVisa.setText(nilaiTeks(dokumenTarget.getFileVisa()));
    }

    private String nilaiTeks(String val) {
        return (val == null || val.equals("-")) ? "" : val;
    }

    private void pilihBerkas(String jenis) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Pilih Berkas " + jenis);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Dokumen PDF & Gambar", "pdf", "jpg", "jpeg", "png"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            switch (jenis) {
                case "KTP":
                    pathSumberKtp = file.getAbsolutePath();
                    txtFileKtp.setText(file.getName());
                    break;
                case "Paspor":
                    pathSumberPaspor = file.getAbsolutePath();
                    txtFilePaspor.setText(file.getName());
                    break;
                case "MedicalCheck-Up":
                    pathSumberMcu = file.getAbsolutePath();
                    txtFileMcu.setText(file.getName());
                    break;
                case "Visa":
                    pathSumberVisa = file.getAbsolutePath();
                    txtFileVisa.setText(file.getName());
                    break;
            }
        }
    }

    private String salinFileKeBrankas(String pathSumber, String jenisLabel, String namaFileLama, String idPeserta) throws Exception {
        if (pathSumber == null || pathSumber.trim().isEmpty()) {
            return (namaFileLama == null || namaFileLama.trim().isEmpty()) ? null : namaFileLama;
        }

        String folderTujuan = System.getProperty("user.dir") + "/Brankas_Dokumen/" + idPeserta;
        File direktori = new File(folderTujuan);
        if (!direktori.exists()) {
            direktori.mkdirs();
        }

        int titikTerakhir = pathSumber.lastIndexOf(".");
        String ekstensi = (titikTerakhir != -1) ? pathSumber.substring(titikTerakhir) : "";
        String namaFileBaru = idPeserta + "_" + jenisLabel + ekstensi;

        Path sumber = Paths.get(pathSumber);
        Path target = Paths.get(folderTujuan, namaFileBaru);
        Files.copy(sumber, target, StandardCopyOption.REPLACE_EXISTING);

        return namaFileBaru;
    }

    private void simpanPerubahan() {
        String tglBerangkat = txtTanggalBerangkat.getText().trim();

        // Validasi format tanggal jika diisi
        if (!tglBerangkat.isEmpty()) {
            try {
                LocalDate.parse(tglBerangkat);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "Format tanggal berangkat harus YYYY-MM-DD (contoh: 2026-12-01) atau kosongkan!",
                        "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else {
            tglBerangkat = null;
        }

        try {
            Peserta peserta = dokumenTarget.getPeserta();
            String idPeserta = peserta.getIdPeserta();

            // 1. Salin file baru jika ada yang diganti
            String fileKtpFinal = salinFileKeBrankas(pathSumberKtp, "KTP", dokumenTarget.getFileKtp(), idPeserta);
            String filePasporFinal = salinFileKeBrankas(pathSumberPaspor, "Paspor", dokumenTarget.getFilePaspor(), idPeserta);
            String fileMcuFinal = salinFileKeBrankas(pathSumberMcu, "MedicalCheck-Up", dokumenTarget.getFileMcu(), idPeserta);
            String fileVisaFinal = salinFileKeBrankas(pathSumberVisa, "Visa", dokumenTarget.getFileVisa(), idPeserta);

            // 2. Susun objek Dokumen yang diperbarui
            Dokumen dokumenUpdate = new Dokumen(dokumenTarget.getIdDokumen(), peserta, tglBerangkat);
            dokumenUpdate.setKtp(kosongKeNull(txtNikKtp.getText()), fileKtpFinal);
            dokumenUpdate.setPaspor(kosongKeNull(txtNoPaspor.getText()), filePasporFinal);
            dokumenUpdate.setMcu(kosongKeNull(txtHasilMcu.getText()), fileMcuFinal);
            dokumenUpdate.setVisa(kosongKeNull(txtNoVisa.getText()), fileVisaFinal);

            // 3. Perbarui status keberangkatan peserta
            String statusBaru = cmbStatusKeberangkatan.getSelectedItem().toString();
            peserta.setStatusKeberangkatan(statusBaru);

            // 4. Simpan ke database MySQL
            boolean okDokumen = DokumenDAO.ubah(dokumenUpdate);
            boolean okPeserta = okDokumen && PesertaDAO.ubah(peserta);

            // 5. Muat ulang cache DataStore agar sinkron dengan MySQL
            DataStore.muatUlang();

            if (okDokumen && okPeserta) {
                if (panelInduk != null) {
                    panelInduk.loadTableData("");
                    panelInduk.loadStatistics();
                }
                JOptionPane.showMessageDialog(this, "Data dokumen berhasil diperbarui!",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan perubahan dokumen: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String kosongKeNull(String teks) {
        String bersih = teks.trim();
        return bersih.isEmpty() ? null : bersih;
    }
}