package Views.admin;

import DataStore.DataStore;
import Model.Dokumen;

import javax.swing.*;
import java.awt.*;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

public class FormDetailDokumen extends javax.swing.JDialog {

    private String idTarget;
    private Dokumen dokumenTarget;

    // --- Definisi Komponen GUI ---
    private JPanel panelHeader;
    private JLabel lblJudulHeader;
    private JLabel lblSubHeader;
    private JButton btnTutupHeader;

    private JPanel panelBody;

    // Kolom Kiri: Info Peserta
    private JLabel lblSectionPeserta;
    private JSeparator separatorPeserta;
    private JLabel lblNamaPeserta;
    private JTextField txtNamaPeserta;
    private JLabel lblIdPeserta;
    private JTextField txtIdPeserta;
    private JLabel lblTanggalBerangkat;
    private JTextField txtTanggalBerangkat;

    // Kolom Kanan: Status Berkas (Checklist)
    private JLabel lblSectionDokumen;
    private JSeparator separatorDokumen;
    private JLabel lblKtp;
    private JTextField txtKtp;
    private JLabel lblPaspor;
    private JTextField txtPaspor;
    private JLabel lblMcu;
    private JTextField txtMcu;
    private JLabel lblVisa;
    private JTextField txtVisa;

    private JButton btnTutup;
    private JButton btnEdit;

    // --- Definisi Warna & Font Sesuai Konvensi SIMAPAN ---
    private final Color MAROON_UTAMA = new Color(122, 0, 0);
    private final Color MAROON_GELAP = new Color(160, 20, 20);
    private final Color BG_BODY = new Color(248, 248, 248);
    private final Color BG_READONLY = new Color(240, 240, 240);
    private final Color TXT_ABU = new Color(90, 90, 90);
    private final Color TXT_UTAMA = new Color(10, 10, 10);

    private final Font FONT_BOLD_10 = new Font("Inter", Font.BOLD, 10);
    private final Font FONT_BOLD_11 = new Font("Inter", Font.BOLD, 11);
    private final Font FONT_PLAIN_11 = new Font("Inter", Font.PLAIN, 11);
    private final Font FONT_BOLD_16 = new Font("Inter", Font.BOLD, 16);

    /**
     * Constructor Manual FormDetailDokumen (Revisi Map Berkas)
     */
    public FormDetailDokumen(java.awt.Frame parent, boolean modal, String idDokumen) {
        super(parent, modal);
        this.idTarget = idDokumen;

        this.setTitle("Detail Kelengkapan Dokumen Peserta");
        this.setSize(680, 480);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
        this.getContentPane().setLayout(new AbsoluteLayout());
        this.getContentPane().setBackground(BG_BODY);

        initComponentsManual();
        loadData();
    }

    private void initComponentsManual() {
        // ==========================================
        // 1. PANEL HEADER
        // ==========================================
        panelHeader = new JPanel(new AbsoluteLayout());
        panelHeader.setBackground(MAROON_UTAMA);

        lblJudulHeader = new JLabel("Map Kelengkapan Dokumen");
        lblJudulHeader.setForeground(Color.WHITE);
        lblJudulHeader.setFont(FONT_BOLD_16);
        panelHeader.add(lblJudulHeader, new AbsoluteConstraints(20, 15, 350, 20));

        lblSubHeader = new JLabel("Status kelengkapan berkas persyaratan keberangkatan");
        lblSubHeader.setForeground(new Color(220, 220, 220));
        lblSubHeader.setFont(new Font("Inter", Font.PLAIN, 10));
        panelHeader.add(lblSubHeader, new AbsoluteConstraints(20, 35, 350, 20));

        btnTutupHeader = new JButton("X");
        btnTutupHeader.setBackground(MAROON_GELAP);
        btnTutupHeader.setForeground(Color.WHITE);
        btnTutupHeader.setFocusPainted(false);
        btnTutupHeader.setBorderPainted(false);
        btnTutupHeader.setFont(FONT_BOLD_11);
        btnTutupHeader.addActionListener(e -> this.dispose());
        panelHeader.add(btnTutupHeader, new AbsoluteConstraints(610, 15, 45, 30));

        this.getContentPane().add(panelHeader, new AbsoluteConstraints(0, 0, 680, 68));

        // ==========================================
        // 2. PANEL BODY
        // ==========================================
        panelBody = new JPanel(new AbsoluteLayout());
        panelBody.setBackground(BG_BODY);

        // --- KOLOM KIRI (Info Peserta Pemilik Dokumen) ---
        lblSectionPeserta = new JLabel("IDENTITAS PESERTA");
        lblSectionPeserta.setForeground(MAROON_UTAMA);
        lblSectionPeserta.setFont(FONT_BOLD_10);
        panelBody.add(lblSectionPeserta, new AbsoluteConstraints(20, 15, 200, 20));

        separatorPeserta = new JSeparator();
        panelBody.add(separatorPeserta, new AbsoluteConstraints(20, 35, 280, 10));

        lblNamaPeserta = new JLabel("Nama Lengkap Peserta");
        lblNamaPeserta.setForeground(TXT_ABU);
        lblNamaPeserta.setFont(FONT_BOLD_11);
        panelBody.add(lblNamaPeserta, new AbsoluteConstraints(20, 50, 280, 20));

        txtNamaPeserta = createReadOnlyField();
        panelBody.add(txtNamaPeserta, new AbsoluteConstraints(20, 70, 280, 30));

        lblIdPeserta = new JLabel("ID Peserta");
        lblIdPeserta.setForeground(TXT_ABU);
        lblIdPeserta.setFont(FONT_BOLD_11);
        panelBody.add(lblIdPeserta, new AbsoluteConstraints(20, 110, 280, 20));

        txtIdPeserta = createReadOnlyField();
        panelBody.add(txtIdPeserta, new AbsoluteConstraints(20, 130, 280, 30));

        lblTanggalBerangkat = new JLabel("Estimasi Keberangkatan");
        lblTanggalBerangkat.setForeground(TXT_ABU);
        lblTanggalBerangkat.setFont(FONT_BOLD_11);
        panelBody.add(lblTanggalBerangkat, new AbsoluteConstraints(20, 170, 280, 20));

        txtTanggalBerangkat = createReadOnlyField();
        txtTanggalBerangkat.setForeground(new Color(0, 100, 0));
        panelBody.add(txtTanggalBerangkat, new AbsoluteConstraints(20, 190, 280, 30));

        // --- KOLOM KANAN (Status Kelengkapan Berkas) ---
        lblSectionDokumen = new JLabel("STATUS KELENGKAPAN BERKAS");
        lblSectionDokumen.setForeground(MAROON_UTAMA);
        lblSectionDokumen.setFont(FONT_BOLD_10);
        panelBody.add(lblSectionDokumen, new AbsoluteConstraints(340, 15, 200, 20));

        separatorDokumen = new JSeparator();
        panelBody.add(separatorDokumen, new AbsoluteConstraints(340, 35, 300, 10));

        // 1. KTP
        lblKtp = new JLabel("E-KTP / Kartu Identitas");
        lblKtp.setForeground(TXT_ABU);
        lblKtp.setFont(FONT_BOLD_11);
        panelBody.add(lblKtp, new AbsoluteConstraints(340, 50, 300, 20));

        txtKtp = createReadOnlyField();
        panelBody.add(txtKtp, new AbsoluteConstraints(340, 70, 300, 30));

        // 2. Paspor
        lblPaspor = new JLabel("Paspor Keberangkatan");
        lblPaspor.setForeground(TXT_ABU);
        lblPaspor.setFont(FONT_BOLD_11);
        panelBody.add(lblPaspor, new AbsoluteConstraints(340, 110, 300, 20));

        txtPaspor = createReadOnlyField();
        panelBody.add(txtPaspor, new AbsoluteConstraints(340, 130, 300, 30));

        // 3. Medical Check-Up
        lblMcu = new JLabel("Hasil Medical Check-Up (MCU)");
        lblMcu.setForeground(TXT_ABU);
        lblMcu.setFont(FONT_BOLD_11);
        panelBody.add(lblMcu, new AbsoluteConstraints(340, 170, 300, 20));

        txtMcu = createReadOnlyField();
        panelBody.add(txtMcu, new AbsoluteConstraints(340, 190, 300, 30));

        // 4. Visa
        lblVisa = new JLabel("Visa / Izin Tinggal");
        lblVisa.setForeground(TXT_ABU);
        lblVisa.setFont(FONT_BOLD_11);
        panelBody.add(lblVisa, new AbsoluteConstraints(340, 230, 300, 20));

        txtVisa = createReadOnlyField();
        panelBody.add(txtVisa, new AbsoluteConstraints(340, 250, 300, 30));

        this.getContentPane().add(panelBody, new AbsoluteConstraints(0, 68, 680, 320));

        // ==========================================
        // 3. PANEL FOOTER (Tombol Aksi)
        // ==========================================
        btnTutup = new JButton("Tutup");
        btnTutup.setFont(FONT_BOLD_11);
        btnTutup.setBackground(Color.WHITE);
        btnTutup.setForeground(TXT_UTAMA);
        btnTutup.setFocusPainted(false);
        btnTutup.addActionListener(e -> this.dispose());
        this.getContentPane().add(btnTutup, new AbsoluteConstraints(420, 395, 100, 35));

        btnEdit = new JButton("Buka Form Input");
        btnEdit.setFont(FONT_BOLD_11);
        btnEdit.setBackground(MAROON_UTAMA);
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.addActionListener(e -> aksiEdit());
        this.getContentPane().add(btnEdit, new AbsoluteConstraints(530, 395, 110, 35));
    }

    /**
     * Helper membuat text field read-only
     */
    private JTextField createReadOnlyField() {
        JTextField field = new JTextField();
        field.setFont(FONT_PLAIN_11);
        field.setEditable(false);
        field.setBackground(BG_READONLY);
        field.setForeground(TXT_UTAMA);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }

    /**
     * Helper memformat tampilan status berkas
     * Menghasilkan: ✅ 3208xxx  [ktp.jpg] atau ❌ Belum Diinput
     */
    private String formatStatusBerkas(String nomor, String namaFile) {
        boolean adaNomor = (nomor != null && !nomor.trim().isEmpty() && !nomor.equals("-"));
        boolean adaFile = (namaFile != null && !namaFile.trim().isEmpty());

        if (!adaNomor && !adaFile) {
            return "❌ Belum Diinput";
        }

        String labelNomor = adaNomor ? nomor : "Tersedia";
        String file = adaFile ? namaFile : "Tanpa Lampiran";
        return "✅ " + labelNomor + "  [" + file + "]";
    }

    private void loadData() {
        // Pastikan cache DataStore memuat data terbaru dari MySQL
        DataStore.muatUlang();

        boolean dataDitemukan = false;

        for (Dokumen dok : DataStore.daftarDokumen) {
            if (dok.getIdDokumen().equals(idTarget)) {
                this.dokumenTarget = dok;

                // 1. Set Identitas Peserta
                if (dok.getPeserta() != null) {
                    txtIdPeserta.setText(dok.getPeserta().getIdPeserta());
                    txtNamaPeserta.setText(dok.getPeserta().getNamaLengkap());
                } else {
                    txtIdPeserta.setText("-");
                    txtNamaPeserta.setText("Data Peserta Tidak Valid");
                }

                String tglBerangkat = dok.getTanggalBerangkat();
                txtTanggalBerangkat.setText((tglBerangkat != null && !tglBerangkat.trim().isEmpty())
                        ? tglBerangkat
                        : "Belum ditentukan");

                // 2. Set Status Checklist Berkas
                txtKtp.setText(formatStatusBerkas(dok.getNikKtp(), dok.getFileKtp()));
                txtPaspor.setText(formatStatusBerkas(dok.getNoPaspor(), dok.getFilePaspor()));
                txtMcu.setText(formatStatusBerkas(dok.getHasilMcu(), dok.getFileMcu()));
                txtVisa.setText(formatStatusBerkas(dok.getNoVisa(), dok.getFileVisa()));

                // Reset & atur warna indikator status
                aturWarnaField(txtKtp);
                aturWarnaField(txtPaspor);
                aturWarnaField(txtMcu);
                aturWarnaField(txtVisa);

                dataDitemukan = true;
                break;
            }
        }

        if (!dataDitemukan) {
            JOptionPane.showMessageDialog(this, "Data Dokumen tidak ditemukan di database!", "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }

    private void aturWarnaField(JTextField field) {
        if (field.getText().startsWith("✅")) {
            field.setForeground(new Color(0, 100, 0));
        } else {
            field.setForeground(TXT_UTAMA);
        }
    }

    private void aksiEdit() {
        if (dokumenTarget == null) return;

        // Buka FormInputDokumen dengan panelInduk = null karena dibuka dari dialog detail
        FormInputDokumen formInput = new FormInputDokumen(null, dokumenTarget);
        formInput.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // Muat ulang tampilan detail begitu form input ditutup
                loadData();
            }
        });
        formInput.setVisible(true);
    }
}