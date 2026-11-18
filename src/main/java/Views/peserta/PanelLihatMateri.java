package Views.peserta;

import DataStore.DataStore;
import Model.Materi;
import Model.Peserta;
import Util.TabelUtil;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
        loadDataMateri("");
    }

    private Peserta cariPesertaAktif() {
        for (Peserta p : DataStore.daftarPeserta) {
            if ((p.getIdPeserta() != null && p.getIdPeserta().equals(idPesertaAktif))
                    || (p.getUsername() != null && p.getUsername().equals(idPesertaAktif))) {
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
        tabelMateri.setSelectionBackground(new Color(230, 240, 255));
        tabelMateri.setSelectionForeground(new Color(0, 50, 150));

        javax.swing.table.JTableHeader header = tabelMateri.getTableHeader();
        header.setBackground(new Color(40, 40, 40));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Inter", Font.BOLD, 13));
        header.setOpaque(true);

        tabelMateri.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tabelMateri.getSelectedRow() != -1) {
                    btnBukaMateriActionPerformed(null);
                }
            }
        });
    }

    private void loadDataMateri(String keyword) {
        DataStore.muatUlang();

        modelTabel.setRowCount(0);
        materiTampil.clear();

        Peserta pesertaAktif = cariPesertaAktif();
        String kelasPeserta = "";
        if (pesertaAktif != null) {
            this.idPesertaAktif = pesertaAktif.getIdPeserta();
            kelasPeserta = (pesertaAktif.getKelas() != null) ? pesertaAktif.getKelas() : "";
        }

        String kataKunci = (keyword == null) ? "" : keyword.trim().toLowerCase();

        int no = 1;
        for (Materi m : DataStore.daftarMateri) {
            String target = m.getKelasTarget();
            boolean untukKelasIni = "Semua Kelas".equalsIgnoreCase(target) || Objects.equals(kelasPeserta, target);
            String judul = (m.getJudul() != null) ? m.getJudul() : "";
            boolean cocokDenganPencarian = kataKunci.isEmpty() || judul.toLowerCase().contains(kataKunci);

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
        TabelUtil.autoResizeKolom(tabelMateri);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        setBackground(new Color(245, 245, 245));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        // --- Judul Halaman ---
        jLabelJudulHalaman = new javax.swing.JLabel();
        jLabelJudulHalaman.setFont(new Font("Inter", Font.BOLD, 16));
        jLabelJudulHalaman.setForeground(new Color(50, 50, 50));
        jLabelJudulHalaman.setText("Materi Belajar Anda");
        add(jLabelJudulHalaman, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 30));

        // --- Info Total Materi ---
        jLabelTotalMateri = new javax.swing.JLabel();
        jLabelTotalMateri.setFont(new Font("Inter", Font.PLAIN, 12));
        jLabelTotalMateri.setForeground(new Color(120, 120, 120));
        jLabelTotalMateri.setText("Total Materi: 0");
        add(jLabelTotalMateri, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 25, 150, 20));

        // --- Fitur Pencarian ---
        jLabelCari = new javax.swing.JLabel();
        jLabelCari.setFont(new Font("Inter", Font.BOLD, 12));
        jLabelCari.setForeground(new Color(70, 70, 70));
        jLabelCari.setText("Cari Materi:");
        add(jLabelCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 65, -1, 32));

        txtCari = new javax.swing.JTextField();
        txtCari.setFont(new Font("Inter", Font.PLAIN, 12));
        txtCari.setToolTipText("Ketik judul materi di sini...");
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariKeyReleased(evt);
            }
        });
        add(txtCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 65, 290, 32));

        // --- Tombol Aksi ---
        btnRefresh = new javax.swing.JButton();
        btnRefresh.setBackground(new Color(220, 220, 220));
        btnRefresh.setFont(new Font("Inter", Font.BOLD, 12));
        btnRefresh.setForeground(new Color(50, 50, 50));
        btnRefresh.setText("Segarkan");
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadDataMateri(txtCari.getText());
            }
        });
        add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 65, 100, 32));

        btnUnduhMateri = new javax.swing.JButton();
        btnUnduhMateri.setBackground(new Color(0, 102, 204));
        btnUnduhMateri.setFont(new Font("Inter", Font.BOLD, 12));
        btnUnduhMateri.setForeground(Color.WHITE);
        btnUnduhMateri.setText("Unduh Berkas");
        btnUnduhMateri.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUnduhMateri.setFocusPainted(false);
        btnUnduhMateri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnduhMateriActionPerformed(evt);
            }
        });
        add(btnUnduhMateri, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 65, 140, 32));

        btnBukaMateri = new javax.swing.JButton();
        btnBukaMateri.setBackground(new Color(20, 120, 40));
        btnBukaMateri.setFont(new Font("Inter", Font.BOLD, 12));
        btnBukaMateri.setForeground(Color.WHITE);
        btnBukaMateri.setText("Buka Materi Terpilih");
        btnBukaMateri.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBukaMateri.setFocusPainted(false);
        btnBukaMateri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBukaMateriActionPerformed(evt);
            }
        });
        add(btnBukaMateri, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 65, 160, 32));

        // --- Tabel Area ---
        panelTabel = new javax.swing.JPanel();
        panelTabel.setBackground(Color.WHITE);
        panelTabel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panelTabel.setLayout(new java.awt.BorderLayout());

        scrollTabel = new javax.swing.JScrollPane();
        scrollTabel.setBorder(null);
        tabelMateri = new javax.swing.JTable();
        tabelMateri.setFont(new Font("Inter", Font.PLAIN, 12));
        tabelMateri.setGridColor(new Color(240, 240, 240));
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
            JOptionPane.showMessageDialog(this,
                "Pilih dahulu materi yang ingin dibuka dari tabel!", "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Materi materiTerpilih = materiTampil.get(baris);

        if ("Teks".equalsIgnoreCase(materiTerpilih.getTipe())) {
            JTextArea areaIsi = new JTextArea(materiTerpilih.getIsiTeks() == null ? "" : materiTerpilih.getIsiTeks());
            areaIsi.setLineWrap(true);
            areaIsi.setWrapStyleWord(true);
            areaIsi.setEditable(false);
            areaIsi.setFont(new Font("Inter", Font.PLAIN, 13));
            areaIsi.setMargin(new Insets(10, 10, 10, 10));

            JScrollPane scrollIsi = new JScrollPane(areaIsi);
            scrollIsi.setPreferredSize(new Dimension(550, 350));
            scrollIsi.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

            JOptionPane.showMessageDialog(this, scrollIsi,
                "Materi: " + materiTerpilih.getJudul(), JOptionPane.PLAIN_MESSAGE);

        } else {
            try {
                File folderAsal = new File(System.getProperty("user.dir")
                        + File.separator + "Brankas_Materi"
                        + File.separator + materiTerpilih.getIdInstruktur());
                File file = new File(folderAsal, materiTerpilih.getNamaFileTersimpan());

                if (!file.exists()) {
                    JOptionPane.showMessageDialog(this,
                        "Berkas materi tidak ditemukan di perangkat ini.", "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Perangkat ini tidak mendukung pembukaan berkas otomatis.\nLokasi berkas: " + file.getAbsolutePath(),
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Gagal membuka berkas: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void btnUnduhMateriActionPerformed(java.awt.event.ActionEvent evt) {
        int baris = tabelMateri.getSelectedRow();
        if (baris == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih dahulu materi yang ingin diunduh dari tabel!", "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Materi materiTerpilih = materiTampil.get(baris);

        if ("Teks".equalsIgnoreCase(materiTerpilih.getTipe())) {
            JOptionPane.showMessageDialog(this,
                "Materi ini berupa teks bacaan langsung, bukan berkas yang bisa diunduh.\nSilakan gunakan tombol 'Buka Materi Terpilih'.",
                "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        File folderAsal = new File(System.getProperty("user.dir")
                + File.separator + "Brankas_Materi"
                + File.separator + materiTerpilih.getIdInstruktur());
        File fileAsal = new File(folderAsal, materiTerpilih.getNamaFileTersimpan());

        if (!fileAsal.exists()) {
            JOptionPane.showMessageDialog(this,
                "Berkas tidak ditemukan di sistem! Mungkin instruktur sudah menghapusnya.", "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String namaDefault = (materiTerpilih.getNamaFileAsli() != null && !materiTerpilih.getNamaFileAsli().isEmpty())
                ? materiTerpilih.getNamaFileAsli()
                : materiTerpilih.getNamaFileTersimpan();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Materi Sebagai...");
        fileChooser.setSelectedFile(new File(namaDefault));

        int pilihan = fileChooser.showSaveDialog(this);

        if (pilihan == JFileChooser.APPROVE_OPTION) {
            File fileTujuan = fileChooser.getSelectedFile();

            try {
                Path sumberPath = fileAsal.toPath();
                Path targetPath = fileTujuan.toPath();
                Files.copy(sumberPath, targetPath, StandardCopyOption.REPLACE_EXISTING);

                JOptionPane.showMessageDialog(this,
                    "Materi berhasil diunduh dan disimpan!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Gagal mengunduh berkas: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private javax.swing.JButton btnBukaMateri;
    private javax.swing.JButton btnUnduhMateri;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel jLabelCari;
    private javax.swing.JLabel jLabelJudulHalaman;
    private javax.swing.JLabel jLabelTotalMateri;
    private javax.swing.JPanel panelTabel;
    private javax.swing.JScrollPane scrollTabel;
    private javax.swing.JTable tabelMateri;
    private javax.swing.JTextField txtCari;
}