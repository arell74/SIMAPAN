package Views.admin;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import javax.swing.text.JTextComponent;

/**
 * FormDataPeserta - Modul 4 (Swing Lanjutan dan Event Handling)
 * Project SIMAPAN (Sistem Manajemen Pemagangan ke Jepang).
 *
 * Memenuhi tiga soal Modul 4:
 *  1. Form dengan validasi input   -> method validasiForm()
 *  2. Menu navigasi + konfirmasi keluar -> buatMenuBar(), keluarAplikasi()
 *  3. Tabel + event klik baris ke form  -> tblPeserta MouseListener, tampilkanKeForm()
 *
 * Komponen Swing tambahan pada versi ini:
 *  JToolBar, JToggleButton, JSplitPane, JProgressBar, JPopupMenu, JSeparator,
 *  TitledBorder, TableRowSorter + RowFilter (pencarian & filter), JScrollPane.
 */
public class FormDataPeserta extends JFrame {

    // ====================== KONSTANTA ======================
    private static final Color MARUN = new Color(0x7F, 0x00, 0x00);
    private static final Color MARUN_TERANG = new Color(0xB7, 0x1C, 0x1C);
    private static final Color LATAR = new Color(0xF4, 0xF1, 0xF1);
    private static final Color GARIS = new Color(0xDD, 0xD5, 0xD5);
    private static final Color ZEBRA = new Color(0xFB, 0xF3, 0xF3);
    private static final int UMUR_MIN = 18;
    private static final int UMUR_MAX = 35;
    private static final int TOTAL_ISIAN = 10; // 9 isian + 1 pernyataan
    private static final DateTimeFormatter FORMAT_TGL =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    private static final String[] BIDANG = {"-- Pilih Bidang --", "Manufaktur", "Pertanian",
        "Perawatan Lansia", "Konstruksi", "Perhotelan"};
    private static final String[] LEVEL = {"-- Pilih Level --", "N5", "N4", "N3", "N2", "N1"};
    // Kolom 0-6 tampil di tabel, kolom 7-8 (email & alamat) hanya disimpan di model
    private static final String[] KOLOM = {"ID", "Nama", "Tgl Lahir", "Jenis Kelamin",
        "Bidang", "Level", "No. HP", "Email", "Alamat"};

    // ====================== KOMPONEN ======================
    // Menu & toolbar
    private JMenuItem miReset, miKeluar, miDataPeserta, miRingkasan, miTentang;
    private JToggleButton tbData, tbRingkasan;

    // Panel konten (CardLayout untuk navigasi)
    private CardLayout cardLayout;
    private JPanel panelKonten;
    private JPanel panelRingkasanIsi;
    private JLabel lblStatus;
    private JLabel lblJumlah;

    // Input form
    private JTextField txtIdPeserta, txtNama, txtTanggalLahir, txtNoHp, txtEmail;
    private JRadioButton rbLaki, rbPerempuan;
    private ButtonGroup grupJenisKelamin;
    private JComboBox<String> cbBidang, cbLevel;
    private JTextArea txtAlamat;
    private JCheckBox chkVerifikasi;
    private JProgressBar pbKelengkapan;

    // Tombol
    private JButton btnSimpan, btnUbah, btnHapus, btnReset;

    // Tabel
    private JTable tblPeserta;
    private DefaultTableModel modelPeserta;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtCari;
    private JComboBox<String> cbFilterBidang;

    // State
    private int barisTerpilih = -1;      // indeks baris model yang sedang dipilih (-1 = tidak ada)
    private Component fokusError;        // komponen pertama yang gagal validasi

    // ====================== KONSTRUKTOR ======================
    public FormDataPeserta() {
        setTitle("SIMAPAN - Data Peserta Pemagangan");
        setSize(1060, 800);
        setMinimumSize(new Dimension(980, 740));
        setLocationRelativeTo(null);
        // Penutupan jendela ditangani sendiri agar bisa menampilkan dialog konfirmasi
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                keluarAplikasi();
            }
        });

        setJMenuBar(buatMenuBar());

        JPanel atas = new JPanel(new BorderLayout());
        atas.add(buatHeader(), BorderLayout.NORTH);
        atas.add(buatToolBar(), BorderLayout.SOUTH);

        add(atas, BorderLayout.NORTH);
        add(buatKonten(), BorderLayout.CENTER);
        add(buatStatusBar(), BorderLayout.SOUTH);

        isiDataAwal();
        aturModeTombol(false);
        perbaruiStatus();
        perbaruiKelengkapan();
    }

    // ====================== MENU NAVIGASI ======================
    private JMenuBar buatMenuBar() {
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

        JMenuBar bar = new JMenuBar();

        JMenu mnBerkas = new JMenu("Berkas");
        miReset = new JMenuItem("Reset Form");
        miReset.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, mask));
        miKeluar = new JMenuItem("Keluar");
        miKeluar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, mask));
        mnBerkas.add(miReset);
        mnBerkas.addSeparator();
        mnBerkas.add(miKeluar);

        JMenu mnNavigasi = new JMenu("Navigasi");
        miDataPeserta = new JMenuItem("Data Peserta");
        miDataPeserta.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, mask));
        miRingkasan = new JMenuItem("Ringkasan Peserta");
        miRingkasan.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, mask));
        mnNavigasi.add(miDataPeserta);
        mnNavigasi.add(miRingkasan);

        JMenu mnBantuan = new JMenu("Bantuan");
        miTentang = new JMenuItem("Tentang Aplikasi");
        mnBantuan.add(miTentang);

        bar.add(mnBerkas);
        bar.add(mnNavigasi);
        bar.add(mnBantuan);

        // --- Event menu ---
        miReset.addActionListener(e -> resetForm());
        miKeluar.addActionListener(e -> keluarAplikasi());
        miDataPeserta.addActionListener(e -> tampilkanHalaman("data"));
        miRingkasan.addActionListener(e -> tampilkanHalaman("ringkasan"));
        miTentang.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "SIMAPAN - Sistem Manajemen Pemagangan ke Jepang\n"
                + "Modul: Data Peserta (Modul 4)",
                "Tentang Aplikasi", JOptionPane.INFORMATION_MESSAGE));

        return bar;
    }

    /** Berpindah halaman (CardLayout) sekaligus menyinkronkan tombol toolbar. */
    private void tampilkanHalaman(String kartu) {
        if ("ringkasan".equals(kartu)) {
            perbaruiRingkasan();
        }
        cardLayout.show(panelKonten, kartu);
        tbData.setSelected("data".equals(kartu));
        tbRingkasan.setSelected("ringkasan".equals(kartu));
    }

    // ====================== DIALOG KONFIRMASI KELUAR ======================
    private void keluarAplikasi() {
        int pilihan = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin keluar dari aplikasi?",
                "Konfirmasi Keluar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (pilihan == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    // ====================== HEADER, TOOLBAR & STATUS BAR ======================
    private JPanel buatHeader() {
        // Header dengan gradasi marun
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, MARUN, getWidth(), 0, MARUN_TERANG));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(14, 22, 14, 22));

        JLabel judul = new JLabel("Data Peserta Pemagangan");
        judul.setForeground(Color.WHITE);
        judul.setFont(judul.getFont().deriveFont(Font.BOLD, 22f));

        JLabel sub = new JLabel("Kelola data calon peserta magang ke Jepang");
        sub.setForeground(new Color(0xF5, 0xD6, 0xD6));
        sub.setFont(sub.getFont().deriveFont(12f));

        JPanel teks = new JPanel(new GridBagLayout());
        teks.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.anchor = GridBagConstraints.WEST;
        g.gridy = 0; teks.add(judul, g);
        g.gridy = 1; g.insets = new Insets(2, 0, 0, 0); teks.add(sub, g);

        // Sisi kanan: lencana SIMAPAN + tanggal hari ini
        JLabel lencana = new JLabel("SIMAPAN", SwingConstants.CENTER);
        lencana.setForeground(MARUN);
        lencana.setFont(lencana.getFont().deriveFont(Font.BOLD, 13f));
        lencana.setOpaque(true);
        lencana.setBackground(Color.WHITE);
        lencana.setBorder(BorderFactory.createEmptyBorder(5, 16, 5, 16));

        String tanggal = LocalDate.now().format(
                DateTimeFormatter.ofPattern("EEEE, dd MMMM uuuu", Locale.forLanguageTag("id-ID")));
        JLabel lblTanggal = new JLabel(tanggal);
        lblTanggal.setForeground(new Color(0xF5, 0xD6, 0xD6));
        lblTanggal.setFont(lblTanggal.getFont().deriveFont(11f));

        JPanel kanan = new JPanel(new GridBagLayout());
        kanan.setOpaque(false);
        GridBagConstraints k = new GridBagConstraints();
        k.gridx = 0; k.anchor = GridBagConstraints.EAST;
        k.gridy = 0; kanan.add(lencana, k);
        k.gridy = 1; k.insets = new Insets(6, 0, 0, 0); kanan.add(lblTanggal, k);

        header.add(teks, BorderLayout.WEST);
        header.add(kanan, BorderLayout.EAST);
        return header;
    }

    private JToolBar buatToolBar() {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);
        tb.setBackground(Color.WHITE);
        tb.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, GARIS),
                BorderFactory.createEmptyBorder(4, 14, 4, 14)));

        tbData = new JToggleButton("Data Peserta", true);
        tbRingkasan = new JToggleButton("Ringkasan");
        ButtonGroup grup = new ButtonGroup();
        grup.add(tbData);
        grup.add(tbRingkasan);
        for (JToggleButton t : new JToggleButton[]{tbData, tbRingkasan}) {
            t.setFocusPainted(false);
            t.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            t.setMargin(new Insets(5, 14, 5, 14));
        }
        tbData.addActionListener(e -> tampilkanHalaman("data"));
        tbRingkasan.addActionListener(e -> tampilkanHalaman("ringkasan"));

        JButton tbReset = new JButton("Reset Form");
        JButton tbKeluar = new JButton("Keluar");
        for (JButton b : new JButton[]{tbReset, tbKeluar}) {
            b.setFocusPainted(false);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.setMargin(new Insets(5, 14, 5, 14));
        }
        tbReset.addActionListener(e -> resetForm());
        tbKeluar.addActionListener(e -> keluarAplikasi());

        tb.add(tbData);
        tb.add(Box.createHorizontalStrut(6));
        tb.add(tbRingkasan);
        tb.addSeparator(new Dimension(18, 24));
        tb.add(tbReset);
        tb.add(Box.createHorizontalGlue());
        tb.add(tbKeluar);
        return tb;
    }

    private JPanel buatStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, GARIS),
                BorderFactory.createEmptyBorder(5, 14, 5, 14)));
        lblStatus = new JLabel(" ");
        lblStatus.setForeground(new Color(0x55, 0x55, 0x55));
        bar.add(lblStatus, BorderLayout.WEST);
        return bar;
    }

    // ====================== KONTEN (CARD LAYOUT) ======================
    private JPanel buatKonten() {
        cardLayout = new CardLayout();
        panelKonten = new JPanel(cardLayout);
        panelKonten.add(buatPanelData(), "data");
        panelKonten.add(buatPanelRingkasan(), "ringkasan");
        return panelKonten;
    }

    private JPanel buatPanelData() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LATAR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 14, 8, 14));

        // Form dan tabel dipisah JSplitPane agar tinggi keduanya bisa diatur pengguna
        JScrollPane spForm = new JScrollPane(buatPanelForm());
        spForm.setBorder(null);
        spForm.getVerticalScrollBar().setUnitIncrement(14);
        spForm.getViewport().setBackground(LATAR);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spForm, buatPanelTabel());
        split.setBorder(null);
        split.setDividerSize(9);
        split.setOneTouchExpandable(true);
        split.setResizeWeight(0.0);
        split.setDividerLocation(375);

        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    // ---------- FORM INPUT ----------
    private JPanel buatPanelForm() {
        txtIdPeserta = new JTextField(12);
        txtNama = new JTextField(20);
        txtTanggalLahir = new JTextField(12);
        txtTanggalLahir.setToolTipText("Format: yyyy-MM-dd (contoh: 2003-05-21)");
        txtNoHp = new JTextField(14);
        txtEmail = new JTextField(20);
        for (JTextField t : new JTextField[]{txtIdPeserta, txtNama, txtTanggalLahir, txtNoHp, txtEmail}) {
            gayaField(t);
        }

        rbLaki = new JRadioButton("Laki-laki");
        rbPerempuan = new JRadioButton("Perempuan");
        rbLaki.setOpaque(false);
        rbPerempuan.setOpaque(false);
        grupJenisKelamin = new ButtonGroup();
        grupJenisKelamin.add(rbLaki);
        grupJenisKelamin.add(rbPerempuan);
        JPanel panelJk = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelJk.setOpaque(false);
        panelJk.add(rbLaki);
        panelJk.add(rbPerempuan);

        cbBidang = new JComboBox<>(BIDANG);
        cbLevel = new JComboBox<>(LEVEL);
        cbBidang.setBackground(Color.WHITE);
        cbLevel.setBackground(Color.WHITE);

        txtAlamat = new JTextArea(2, 20);
        txtAlamat.setLineWrap(true);
        txtAlamat.setWrapStyleWord(true);
        txtAlamat.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        JScrollPane spAlamat = new JScrollPane(txtAlamat);
        spAlamat.setBorder(BorderFactory.createLineBorder(GARIS));

        chkVerifikasi = new JCheckBox("Saya menyatakan data di atas sudah benar");
        chkVerifikasi.setOpaque(false);

        // ----- Kartu kiri: identitas -----
        JPanel kartuIdentitas = buatKartu("Identitas Peserta");
        tambahField(kartuIdentitas, 0, "ID Peserta *", txtIdPeserta);
        tambahField(kartuIdentitas, 1, "Nama Lengkap *", txtNama);
        tambahField(kartuIdentitas, 2, "Tanggal Lahir *", txtTanggalLahir);
        tambahField(kartuIdentitas, 3, "Jenis Kelamin *", panelJk);

        // ----- Kartu kanan: program & kontak -----
        JPanel kartuProgram = buatKartu("Program & Kontak");
        tambahField(kartuProgram, 0, "Bidang *", cbBidang);
        tambahField(kartuProgram, 1, "Level JLPT *", cbLevel);
        tambahField(kartuProgram, 2, "No. HP *", txtNoHp);
        tambahField(kartuProgram, 3, "Email *", txtEmail);

        JPanel barisAtas = new JPanel(new GridLayout(1, 2, 12, 0));
        barisAtas.setOpaque(false);
        barisAtas.add(kartuIdentitas);
        barisAtas.add(kartuProgram);

        // ----- Kartu bawah: alamat + pernyataan -----
        JPanel kartuAlamat = buatKartu("Alamat & Pernyataan");
        tambahField(kartuAlamat, 0, "Alamat *", spAlamat);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 4, 2, 4);
        c.gridx = 1; c.gridy = 1; c.anchor = GridBagConstraints.WEST;
        kartuAlamat.add(chkVerifikasi, c);

        // ----- Bar aksi: progress kelengkapan + tombol -----
        pbKelengkapan = new JProgressBar(0, TOTAL_ISIAN);
        pbKelengkapan.setStringPainted(true);
        pbKelengkapan.setForeground(MARUN);
        pbKelengkapan.setBackground(Color.WHITE);
        pbKelengkapan.setPreferredSize(new Dimension(220, 22));

        btnSimpan = new TombolBulat("Simpan", MARUN, Color.WHITE, false);
        btnUbah = new TombolBulat("Ubah", new Color(0x37, 0x47, 0x4F), Color.WHITE, false);
        btnHapus = new TombolBulat("Hapus", new Color(0xC6, 0x28, 0x28), new Color(0xC6, 0x28, 0x28), true);
        btnReset = new TombolBulat("Reset", new Color(0x75, 0x75, 0x75), new Color(0x55, 0x55, 0x55), true);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelTombol.setOpaque(false);
        panelTombol.add(btnReset);
        panelTombol.add(btnHapus);
        panelTombol.add(btnUbah);
        panelTombol.add(btnSimpan);

        JPanel panelProgress = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelProgress.setOpaque(false);
        panelProgress.add(pbKelengkapan);

        JPanel barAksi = new JPanel(new BorderLayout());
        barAksi.setOpaque(false);
        barAksi.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
        barAksi.add(panelProgress, BorderLayout.WEST);
        barAksi.add(panelTombol, BorderLayout.EAST);

        // ----- Susun semua -----
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(LATAR);
        form.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        barisAtas.setAlignmentX(Component.LEFT_ALIGNMENT);
        kartuAlamat.setAlignmentX(Component.LEFT_ALIGNMENT);
        barAksi.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(barisAtas);
        form.add(Box.createVerticalStrut(8));
        form.add(kartuAlamat);
        form.add(Box.createVerticalStrut(8));
        form.add(barAksi);

        // --- Pantau kelengkapan form ---
        pantauKelengkapan(txtIdPeserta, txtNama, txtTanggalLahir, txtNoHp, txtEmail, txtAlamat);
        rbLaki.addActionListener(e -> perbaruiKelengkapan());
        rbPerempuan.addActionListener(e -> perbaruiKelengkapan());
        cbBidang.addActionListener(e -> perbaruiKelengkapan());
        cbLevel.addActionListener(e -> perbaruiKelengkapan());
        chkVerifikasi.addActionListener(e -> perbaruiKelengkapan());

        // --- Event tombol ---
        btnSimpan.addActionListener(e -> simpanData());
        btnUbah.addActionListener(e -> ubahData());
        btnHapus.addActionListener(e -> hapusData());
        btnReset.addActionListener(e -> resetForm());

        return form;
    }

    /** Kartu putih dengan judul marun (TitledBorder). */
    private JPanel buatKartu(String judul) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(GARIS), " " + judul + " ");
        tb.setTitleColor(MARUN);
        tb.setTitleFont(tb.getTitleFont().deriveFont(Font.BOLD, 12f));
        p.setBorder(BorderFactory.createCompoundBorder(tb,
                BorderFactory.createEmptyBorder(2, 8, 6, 8)));
        return p;
    }

    /** Satu baris label + isian (label kiri, isian melebar ke kanan). */
    private void tambahField(JPanel p, int baris, String label, Component isian) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.gridy = baris;
        c.anchor = GridBagConstraints.NORTHWEST;

        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(105, 22));
        lbl.setForeground(new Color(0x44, 0x44, 0x44));
        c.gridx = 0; c.weightx = 0; c.fill = GridBagConstraints.NONE;
        p.add(lbl, c);

        c.gridx = 1; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        p.add(isian, c);
    }

    /** Border isian: abu-abu biasa, marun saat fokus. */
    private void gayaField(JTextField t) {
        final javax.swing.border.Border biasa = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GARIS), BorderFactory.createEmptyBorder(3, 6, 3, 6));
        final javax.swing.border.Border fokus = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MARUN), BorderFactory.createEmptyBorder(3, 6, 3, 6));
        t.setBorder(biasa);
        t.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                t.setBorder(fokus);
            }

            @Override
            public void focusLost(FocusEvent e) {
                t.setBorder(biasa);
            }
        });
    }

    private void pantauKelengkapan(JTextComponent... fields) {
        DocumentListener dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                perbaruiKelengkapan();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                perbaruiKelengkapan();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                perbaruiKelengkapan();
            }
        };
        for (JTextComponent f : fields) {
            f.getDocument().addDocumentListener(dl);
        }
    }

    private void perbaruiKelengkapan() {
        if (pbKelengkapan == null) {
            return;
        }
        int n = 0;
        if (!txtIdPeserta.getText().trim().isEmpty()) n++;
        if (!txtNama.getText().trim().isEmpty()) n++;
        if (!txtTanggalLahir.getText().trim().isEmpty()) n++;
        if (jenisKelaminTerpilih() != null) n++;
        if (cbBidang.getSelectedIndex() > 0) n++;
        if (cbLevel.getSelectedIndex() > 0) n++;
        if (!txtNoHp.getText().trim().isEmpty()) n++;
        if (!txtEmail.getText().trim().isEmpty()) n++;
        if (!txtAlamat.getText().trim().isEmpty()) n++;
        if (chkVerifikasi.isSelected()) n++;
        pbKelengkapan.setValue(n);
        pbKelengkapan.setString("Kelengkapan form: " + n + "/" + TOTAL_ISIAN);
    }

    // ---------- TABEL ----------
    private JPanel buatPanelTabel() {
        modelPeserta = new DefaultTableModel(KOLOM, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPeserta = new JTable(modelPeserta);
        tblPeserta.setRowHeight(28);
        tblPeserta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPeserta.setSelectionBackground(new Color(0xE8, 0xC8, 0xC8));
        tblPeserta.setSelectionForeground(Color.BLACK);
        tblPeserta.setShowVerticalLines(false);
        tblPeserta.setShowHorizontalLines(true);
        tblPeserta.setGridColor(new Color(0xEE, 0xE6, 0xE6));
        tblPeserta.setIntercellSpacing(new Dimension(0, 1));
        tblPeserta.setFillsViewportHeight(true);

        // Email & Alamat disembunyikan dari tampilan, tetapi datanya tetap ada di model
        tblPeserta.removeColumn(tblPeserta.getColumnModel().getColumn(8));
        tblPeserta.removeColumn(tblPeserta.getColumnModel().getColumn(7));

        // Lebar kolom
        int[] lebar = {70, 170, 90, 100, 120, 55, 110};
        for (int i = 0; i < lebar.length; i++) {
            tblPeserta.getColumnModel().getColumn(i).setPreferredWidth(lebar[i]);
        }

        // Urutkan (klik header) + filter
        sorter = new TableRowSorter<>(modelPeserta);
        tblPeserta.setRowSorter(sorter);

        gayaHeaderTabel();
        gayaIsiTabel();

        // Popup menu klik kanan
        JPopupMenu popup = new JPopupMenu();
        JMenuItem miPopupUbah = new JMenuItem("Tampilkan ke Form");
        JMenuItem miPopupHapus = new JMenuItem("Hapus Peserta");
        miPopupUbah.addActionListener(e -> {
            if (barisTerpilih >= 0) {
                txtNama.requestFocusInWindow();
            }
        });
        miPopupHapus.addActionListener(e -> hapusData());
        popup.add(miPopupUbah);
        popup.addSeparator();
        popup.add(miPopupHapus);

        // EVENT KLIK BARIS -> tampilkan data ke form
        tblPeserta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    pilihBaris(e);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                tampilPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                tampilPopup(e);
            }

            private void tampilPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int baris = tblPeserta.rowAtPoint(e.getPoint());
                    if (baris >= 0) {
                        tblPeserta.setRowSelectionInterval(baris, baris);
                        tampilkanKeForm(tblPeserta.convertRowIndexToModel(baris));
                        popup.show(tblPeserta, e.getX(), e.getY());
                    }
                }
            }

            private void pilihBaris(MouseEvent e) {
                int baris = tblPeserta.rowAtPoint(e.getPoint());
                if (baris >= 0) {
                    tampilkanKeForm(tblPeserta.convertRowIndexToModel(baris));
                }
            }
        });

        // Bar pencarian + filter
        JLabel lblJudul = new JLabel("Daftar Peserta");
        lblJudul.setFont(lblJudul.getFont().deriveFont(Font.BOLD, 14f));
        lblJudul.setForeground(MARUN);

        txtCari = new JTextField(16);
        gayaField(txtCari);
        txtCari.setToolTipText("Cari berdasarkan ID, nama, bidang, dll.");
        txtCari.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                terapkanFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                terapkanFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                terapkanFilter();
            }
        });

        String[] opsiFilter = BIDANG.clone();
        opsiFilter[0] = "Semua Bidang";
        cbFilterBidang = new JComboBox<>(opsiFilter);
        cbFilterBidang.setBackground(Color.WHITE);
        cbFilterBidang.addActionListener(e -> terapkanFilter());

        lblJumlah = new JLabel(" ");
        lblJumlah.setForeground(new Color(0x66, 0x66, 0x66));

        JPanel filter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filter.setOpaque(false);
        filter.add(lblJumlah);
        filter.add(new JLabel("Cari:"));
        filter.add(txtCari);
        filter.add(cbFilterBidang);

        JPanel barAtas = new JPanel(new BorderLayout());
        barAtas.setOpaque(false);
        barAtas.setBorder(BorderFactory.createEmptyBorder(2, 2, 6, 2));
        barAtas.add(lblJudul, BorderLayout.WEST);
        barAtas.add(filter, BorderLayout.EAST);

        JScrollPane sp = new JScrollPane(tblPeserta);
        sp.setBorder(BorderFactory.createLineBorder(GARIS));
        sp.getViewport().setBackground(Color.WHITE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(barAtas, BorderLayout.NORTH);
        panel.add(sp, BorderLayout.CENTER);
        return panel;
    }

    /** HEADER TABEL MERAH: latar marun, teks putih tebal. */
    private void gayaHeaderTabel() {
        JTableHeader h = tblPeserta.getTableHeader();
        h.setReorderingAllowed(false);
        h.setBackground(MARUN);
        h.setForeground(Color.WHITE);
        h.setPreferredSize(new Dimension(0, 34));
        h.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, false, false, row, column);
                setOpaque(true);
                setBackground(MARUN);
                setForeground(Color.WHITE);
                setFont(getFont().deriveFont(Font.BOLD, 12f));
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 0, 1, MARUN_TERANG),
                        BorderFactory.createEmptyBorder(0, 8, 0, 8)));
                int kolomModel = table.convertColumnIndexToModel(column);
                setHorizontalAlignment(kolomModel == 0 || kolomModel == 5 ? CENTER : LEFT);
                return this;
            }
        });
    }

    /** Isi tabel: baris belang (zebra), padding, ID & Level rata tengah. */
    private void gayaIsiTabel() {
        tblPeserta.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : ZEBRA);
                }
                int kolomModel = table.convertColumnIndexToModel(column);
                setHorizontalAlignment(kolomModel == 0 || kolomModel == 5 ? CENTER : LEFT);
                return this;
            }
        });
    }

    private void terapkanFilter() {
        List<RowFilter<Object, Object>> daftar = new ArrayList<>();
        String q = txtCari.getText().trim();
        if (!q.isEmpty()) {
            daftar.add(RowFilter.regexFilter("(?i)" + Pattern.quote(q)));
        }
        if (cbFilterBidang.getSelectedIndex() > 0) {
            String bidang = cbFilterBidang.getSelectedItem().toString();
            daftar.add(RowFilter.regexFilter("^" + Pattern.quote(bidang) + "$", 4));
        }
        sorter.setRowFilter(daftar.isEmpty() ? null
                : RowFilter.<DefaultTableModel, Integer>andFilter(daftar));
        perbaruiStatus();
    }

    // ---------- PANEL RINGKASAN (tujuan navigasi kedua) ----------
    private JScrollPane buatPanelRingkasan() {
        panelRingkasanIsi = new JPanel();
        panelRingkasanIsi.setLayout(new BoxLayout(panelRingkasanIsi, BoxLayout.Y_AXIS));
        panelRingkasanIsi.setBackground(LATAR);
        panelRingkasanIsi.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JScrollPane sp = new JScrollPane(panelRingkasanIsi);
        sp.setBorder(null);
        sp.getViewport().setBackground(LATAR);
        sp.getVerticalScrollBar().setUnitIncrement(14);
        return sp;
    }

    private void perbaruiRingkasan() {
        Map<String, Integer> perBidang = new LinkedHashMap<>();
        Map<String, Integer> perLevel = new LinkedHashMap<>();
        for (int i = 1; i < BIDANG.length; i++) {
            perBidang.put(BIDANG[i], 0);
        }
        for (int i = 1; i < LEVEL.length; i++) {
            perLevel.put(LEVEL[i], 0);
        }
        for (int i = 0; i < modelPeserta.getRowCount(); i++) {
            perBidang.merge(modelPeserta.getValueAt(i, 4).toString(), 1, Integer::sum);
            perLevel.merge(modelPeserta.getValueAt(i, 5).toString(), 1, Integer::sum);
        }
        int total = modelPeserta.getRowCount();

        panelRingkasanIsi.removeAll();
        panelRingkasanIsi.add(buatKartuTotal(total));
        panelRingkasanIsi.add(Box.createVerticalStrut(12));
        panelRingkasanIsi.add(buatKartuStatistik("Berdasarkan Bidang", perBidang, total));
        panelRingkasanIsi.add(Box.createVerticalStrut(12));
        panelRingkasanIsi.add(buatKartuStatistik("Berdasarkan Level JLPT", perLevel, total));
        panelRingkasanIsi.add(Box.createVerticalGlue());
        panelRingkasanIsi.revalidate();
        panelRingkasanIsi.repaint();
    }

    private JPanel buatKartuTotal(int total) {
        JPanel kartu = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, MARUN, getWidth(), 0, MARUN_TERANG));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        kartu.setOpaque(false);
        kartu.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));
        kartu.setAlignmentX(Component.LEFT_ALIGNMENT);
        kartu.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel angka = new JLabel(String.valueOf(total));
        angka.setForeground(Color.WHITE);
        angka.setFont(angka.getFont().deriveFont(Font.BOLD, 40f));

        JLabel ket = new JLabel("Total peserta terdaftar");
        ket.setForeground(new Color(0xF5, 0xD6, 0xD6));
        ket.setFont(ket.getFont().deriveFont(14f));

        kartu.add(angka, BorderLayout.WEST);
        kartu.add(ket, BorderLayout.CENTER);
        ket.setBorder(BorderFactory.createEmptyBorder(14, 16, 0, 0));
        return kartu;
    }

    private JPanel buatKartuStatistik(String judul, Map<String, Integer> data, int total) {
        JPanel kartu = buatKartu(judul);
        kartu.setAlignmentX(Component.LEFT_ALIGNMENT);
        int baris = 0;
        for (Map.Entry<String, Integer> e : data.entrySet()) {
            JLabel lbl = new JLabel(e.getKey());
            lbl.setPreferredSize(new Dimension(140, 22));

            JProgressBar pb = new JProgressBar(0, Math.max(total, 1));
            pb.setValue(e.getValue());
            pb.setStringPainted(true);
            pb.setString(e.getValue() + " peserta");
            pb.setForeground(MARUN);
            pb.setBackground(new Color(0xF1, 0xE9, 0xE9));
            pb.setPreferredSize(new Dimension(300, 22));

            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(4, 4, 4, 4);
            c.gridy = baris++;
            c.gridx = 0; c.anchor = GridBagConstraints.WEST;
            kartu.add(lbl, c);
            c.gridx = 1; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
            kartu.add(pb, c);
        }
        kartu.setMaximumSize(new Dimension(Integer.MAX_VALUE, kartu.getPreferredSize().height + 10));
        return kartu;
    }

    // ====================== DATA AWAL (CONTOH) ======================
    private void isiDataAwal() {
        tambahKeTabel("PST001", "Andi Pratama", "2002-03-14", "Laki-laki", "Manufaktur", "N4",
                "081234567801", "andi.pratama@mail.com", "Jl. Siliwangi No. 12, Kuningan");
        tambahKeTabel("PST002", "Siti Rahmawati", "2003-07-09", "Perempuan", "Perawatan Lansia", "N3",
                "081234567802", "siti.rahma@mail.com", "Jl. Cut Nyak Dien No. 5, Kuningan");
        tambahKeTabel("PST003", "Budi Santoso", "2001-11-30", "Laki-laki", "Pertanian", "N5",
                "081234567803", "budi.santoso@mail.com", "Desa Ciawigebang RT 02/01, Kuningan");
        tambahKeTabel("PST004", "Dewi Lestari", "2004-01-25", "Perempuan", "Perhotelan", "N2",
                "081234567804", "dewi.lestari@mail.com", "Jl. Ahmad Yani No. 40, Cirebon");
    }

    private void tambahKeTabel(String id, String nama, String tgl, String jk, String bidang,
            String level, String hp, String email, String alamat) {
        modelPeserta.addRow(new Object[]{id, nama, tgl, jk, bidang, level, hp, email, alamat});
    }

    // ====================== CRUD ======================
    private void simpanData() {
        if (!validasiForm(true)) {
            return;
        }
        tambahKeTabel(txtIdPeserta.getText().trim().toUpperCase(), txtNama.getText().trim(),
                txtTanggalLahir.getText().trim(), jenisKelaminTerpilih(),
                cbBidang.getSelectedItem().toString(), cbLevel.getSelectedItem().toString(),
                txtNoHp.getText().trim(), txtEmail.getText().trim(), txtAlamat.getText().trim());
        JOptionPane.showMessageDialog(this, "Data peserta berhasil disimpan!",
                "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        resetForm();
        perbaruiStatus();
    }

    private void ubahData() {
        if (barisTerpilih < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data pada tabel terlebih dahulu!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validasiForm(false)) {
            return;
        }
        // Kolom 0 (ID) tidak diubah
        modelPeserta.setValueAt(txtNama.getText().trim(), barisTerpilih, 1);
        modelPeserta.setValueAt(txtTanggalLahir.getText().trim(), barisTerpilih, 2);
        modelPeserta.setValueAt(jenisKelaminTerpilih(), barisTerpilih, 3);
        modelPeserta.setValueAt(cbBidang.getSelectedItem().toString(), barisTerpilih, 4);
        modelPeserta.setValueAt(cbLevel.getSelectedItem().toString(), barisTerpilih, 5);
        modelPeserta.setValueAt(txtNoHp.getText().trim(), barisTerpilih, 6);
        modelPeserta.setValueAt(txtEmail.getText().trim(), barisTerpilih, 7);
        modelPeserta.setValueAt(txtAlamat.getText().trim(), barisTerpilih, 8);
        JOptionPane.showMessageDialog(this, "Data peserta berhasil diubah!",
                "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        resetForm();
    }

    private void hapusData() {
        if (barisTerpilih < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data pada tabel terlebih dahulu!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nama = modelPeserta.getValueAt(barisTerpilih, 1).toString();
        int pilihan = JOptionPane.showConfirmDialog(this,
                "Hapus data peserta \"" + nama + "\"?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (pilihan == JOptionPane.YES_OPTION) {
            modelPeserta.removeRow(barisTerpilih);
            resetForm();
            perbaruiStatus();
        }
    }

    // ====================== EVENT KLIK BARIS ======================
    /** Menampilkan data baris tabel (indeks model) ke seluruh komponen form. */
    private void tampilkanKeForm(int baris) {
        barisTerpilih = baris;
        txtIdPeserta.setText(modelPeserta.getValueAt(baris, 0).toString());
        txtNama.setText(modelPeserta.getValueAt(baris, 1).toString());
        txtTanggalLahir.setText(modelPeserta.getValueAt(baris, 2).toString());

        if ("Laki-laki".equals(modelPeserta.getValueAt(baris, 3))) {
            rbLaki.setSelected(true);
        } else {
            rbPerempuan.setSelected(true);
        }
        cbBidang.setSelectedItem(modelPeserta.getValueAt(baris, 4));
        cbLevel.setSelectedItem(modelPeserta.getValueAt(baris, 5));
        txtNoHp.setText(modelPeserta.getValueAt(baris, 6).toString());
        txtEmail.setText(modelPeserta.getValueAt(baris, 7).toString());
        txtAlamat.setText(modelPeserta.getValueAt(baris, 8).toString());
        chkVerifikasi.setSelected(true);

        aturModeTombol(true);
        perbaruiKelengkapan();
        lblStatus.setText("Mengedit peserta: " + txtIdPeserta.getText() + " - " + txtNama.getText());
    }

    // ====================== VALIDASI ======================
    /**
     * Memeriksa seluruh input. Semua kesalahan dikumpulkan lalu ditampilkan sekaligus.
     * @param modeTambah true jika sedang menambah data (ID harus belum terdaftar)
     */
    private boolean validasiForm(boolean modeTambah) {
        List<String> error = new ArrayList<>();
        fokusError = null;

        String id = txtIdPeserta.getText().trim().toUpperCase();
        if (id.isEmpty()) {
            catatError(error, "ID Peserta wajib diisi.", txtIdPeserta);
        } else if (!id.matches("PST\\d{3}")) {
            catatError(error, "ID Peserta harus berformat PST + 3 angka (contoh: PST005).", txtIdPeserta);
        } else if (modeTambah && idSudahAda(id)) {
            catatError(error, "ID Peserta " + id + " sudah terdaftar.", txtIdPeserta);
        }

        String nama = txtNama.getText().trim();
        if (nama.isEmpty()) {
            catatError(error, "Nama lengkap wajib diisi.", txtNama);
        } else if (!nama.matches("[\\p{L} .'-]{3,50}")) {
            catatError(error, "Nama hanya boleh berisi huruf (3-50 karakter), tanpa angka.", txtNama);
        }

        String tgl = txtTanggalLahir.getText().trim();
        if (tgl.isEmpty()) {
            catatError(error, "Tanggal lahir wajib diisi.", txtTanggalLahir);
        } else {
            try {
                LocalDate lahir = LocalDate.parse(tgl, FORMAT_TGL);
                int umur = Period.between(lahir, LocalDate.now()).getYears();
                if (lahir.isAfter(LocalDate.now()) || umur < UMUR_MIN || umur > UMUR_MAX) {
                    catatError(error, "Umur peserta harus antara " + UMUR_MIN + " sampai "
                            + UMUR_MAX + " tahun.", txtTanggalLahir);
                }
            } catch (DateTimeParseException ex) {
                catatError(error, "Tanggal lahir harus valid dengan format yyyy-MM-dd (contoh: 2003-05-21).",
                        txtTanggalLahir);
            }
        }

        if (jenisKelaminTerpilih() == null) {
            catatError(error, "Jenis kelamin wajib dipilih.", rbLaki);
        }
        if (cbBidang.getSelectedIndex() == 0) {
            catatError(error, "Bidang wajib dipilih.", cbBidang);
        }
        if (cbLevel.getSelectedIndex() == 0) {
            catatError(error, "Level JLPT wajib dipilih.", cbLevel);
        }
        String hp = txtNoHp.getText().trim();
        if (hp.isEmpty()) {
            catatError(error, "No. HP wajib diisi.", txtNoHp);
        } else if (!hp.matches("08\\d{8,11}")) {
            catatError(error, "No. HP harus diawali 08 dan berisi 10-13 digit angka.", txtNoHp);
        }
        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {
            catatError(error, "Email wajib diisi.", txtEmail);
        } else if (!email.matches("[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+")) {
            catatError(error, "Format email tidak valid (contoh: nama@mail.com).", txtEmail);
        }
        if (txtAlamat.getText().trim().length() < 5) {
            catatError(error, "Alamat wajib diisi (minimal 5 karakter).", txtAlamat);
        }
        if (!chkVerifikasi.isSelected()) {
            catatError(error, "Centang pernyataan bahwa data sudah benar.", chkVerifikasi);
        }

        if (!error.isEmpty()) {
            StringBuilder sb = new StringBuilder("Periksa kembali isian berikut:\n");
            for (String s : error) {
                sb.append("  - ").append(s).append('\n');
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Validasi Gagal", JOptionPane.ERROR_MESSAGE);
            if (fokusError != null) {
                fokusError.requestFocusInWindow();
            }
            return false;
        }
        return true;
    }

    private void catatError(List<String> daftar, String pesan, Component komponen) {
        daftar.add(pesan);
        if (fokusError == null) {
            fokusError = komponen; // fokus ke isian pertama yang salah
        }
    }

    private boolean idSudahAda(String id) {
        for (int i = 0; i < modelPeserta.getRowCount(); i++) {
            if (id.equalsIgnoreCase(modelPeserta.getValueAt(i, 0).toString())) {
                return true;
            }
        }
        return false;
    }

    private String jenisKelaminTerpilih() {
        if (rbLaki.isSelected()) {
            return "Laki-laki";
        }
        if (rbPerempuan.isSelected()) {
            return "Perempuan";
        }
        return null;
    }

    // ====================== RESET & STATE ======================
    private void resetForm() {
        txtIdPeserta.setText("");
        txtNama.setText("");
        txtTanggalLahir.setText("");
        txtNoHp.setText("");
        txtEmail.setText("");
        txtAlamat.setText("");
        grupJenisKelamin.clearSelection();
        cbBidang.setSelectedIndex(0);
        cbLevel.setSelectedIndex(0);
        chkVerifikasi.setSelected(false);
        tblPeserta.clearSelection();
        barisTerpilih = -1;
        aturModeTombol(false);
        perbaruiStatus();
        perbaruiKelengkapan();
        txtIdPeserta.requestFocusInWindow();
    }

    /** edit = true: baris dipilih (Ubah/Hapus aktif, ID dikunci). edit = false: mode tambah. */
    private void aturModeTombol(boolean edit) {
        txtIdPeserta.setEnabled(!edit);
        btnSimpan.setEnabled(!edit);
        btnUbah.setEnabled(edit);
        btnHapus.setEnabled(edit);
        btnReset.setEnabled(true);
    }

    private void perbaruiStatus() {
        lblStatus.setText("Total peserta: " + modelPeserta.getRowCount()
                + "   |   Klik baris pada tabel untuk menampilkan data ke form"
                + "   |   Klik kanan untuk menu cepat");
        if (lblJumlah != null) {
            lblJumlah.setText("Menampilkan " + tblPeserta.getRowCount() + " dari "
                    + modelPeserta.getRowCount() + " peserta");
        }
    }

    // ====================== TOMBOL BULAT KUSTOM ======================
    /** JButton dengan sudut membulat; outline = true -> gaya garis tepi (tanpa isi). */
    private static class TombolBulat extends JButton {

        private final Color warna;
        private final boolean outline;

        TombolBulat(String teks, Color warna, Color teksWarna, boolean outline) {
            super(teks);
            this.warna = warna;
            this.outline = outline;
            setForeground(teksWarna);
            setFont(getFont().deriveFont(Font.BOLD, 12f));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(100, 34));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth() - 1;
            int h = getHeight() - 1;
            if (!isEnabled()) {
                g2.setColor(new Color(0xEC, 0xE8, 0xE8));
                g2.fillRoundRect(0, 0, w, h, 14, 14);
            } else if (outline) {
                g2.setColor(getModel().isRollover() || getModel().isPressed()
                        ? new Color(0xF8, 0xEC, 0xEC) : Color.WHITE);
                g2.fillRoundRect(0, 0, w, h, 14, 14);
                g2.setColor(warna);
                g2.drawRoundRect(0, 0, w, h, 14, 14);
            } else {
                Color c = warna;
                if (getModel().isPressed()) {
                    c = warna.darker();
                } else if (getModel().isRollover()) {
                    c = warna.brighter();
                }
                g2.setColor(c);
                g2.fillRoundRect(0, 0, w, h, 14, 14);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ====================== MAIN ======================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormDataPeserta().setVisible(true));
    }
}