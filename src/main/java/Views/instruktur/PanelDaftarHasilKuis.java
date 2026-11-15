package Views.instruktur;

import DataStore.DataStore;
import Model.HasilKuis;
import Model.Peserta;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PanelDaftarHasilKuis extends JPanel {

    private String idInstrukturAktif;
    private JTable tabelDaftar;
    private DefaultTableModel modelTabel;

    public PanelDaftarHasilKuis(String idInstruktur) {
        this.idInstrukturAktif = idInstruktur;
        initComponents();
        loadData();
    }

    private void loadData() {
        modelTabel.setRowCount(0);
        int no = 1;
        
        // Melist semua hasil kuis yang ada di DataStore
        for (HasilKuis h : DataStore.daftarHasilKuis) {
            String namaSiswa = "Tidak Diketahui";
            
            // Cari nama peserta berdasarkan ID-nya
            for (Peserta p : DataStore.daftarPeserta) {
                if (p.getIdPeserta() != null && p.getIdPeserta().equals(h.getIdPeserta())) {
                    namaSiswa = p.getNamaLengkap();
                    break;
                }
            }

            // Memasukkan data ke tabel, sesuai dengan method yang ada di Model.HasilKuis
            modelTabel.addRow(new Object[]{
                no++,
                h.getIdHasil(),
                namaSiswa,
                h.getJudulKuis(),
                h.getTanggalPengerjaan(), 
                h.getNilaiAkhir(),        
                h.getStatus()
            });
        }
    }

    private void bukaPanelPenilaian() {
        int baris = tabelDaftar.getSelectedRow();
        if (baris == -1) {
            JOptionPane.showMessageDialog(this, "Pilih dahulu data siswa dari tabel!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idHasilTerpilih = modelTabel.getValueAt(baris, 1).toString();
        HasilKuis hasilTerpilih = null;
        Peserta pesertaTerpilih = null;

        // Cari objek HasilKuis aslinya
        for (HasilKuis h : DataStore.daftarHasilKuis) {
            if (h.getIdHasil().equals(idHasilTerpilih)) {
                hasilTerpilih = h;
                break;
            }
        }

        // Cari objek Peserta aslinya
        if (hasilTerpilih != null) {
            for (Peserta p : DataStore.daftarPeserta) {
                if (p.getIdPeserta().equals(hasilTerpilih.getIdPeserta())) {
                    pesertaTerpilih = p;
                    break;
                }
            }
        }

        // Memanggil fungsi showPanel yang ada di InstrukturFrame
        InstrukturFrame parent = (InstrukturFrame) javax.swing.SwingUtilities.getWindowAncestor(this);
        if (parent != null && hasilTerpilih != null && pesertaTerpilih != null) {
            parent.showPanel(new PanelPenilaianHasilKuis(hasilTerpilih, pesertaTerpilih, idInstrukturAktif));
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(248, 248, 248));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // HEADER
        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.setOpaque(false);
        JLabel lblJudul = new JLabel("Daftar Hasil Kuis Masuk");
        lblJudul.setFont(new Font("Inter", Font.BOLD, 18));
        panelAtas.add(lblJudul, BorderLayout.WEST);
        add(panelAtas, BorderLayout.NORTH);

        // TABEL
        modelTabel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"No.", "ID Hasil", "Nama Peserta", "Judul Kuis", "Tgl Pengerjaan", "Skor", "Status"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelDaftar = new JTable(modelTabel);
        tabelDaftar.setRowHeight(35);
        tabelDaftar.setFont(new Font("Inter", Font.PLAIN, 12));
        
        tabelDaftar.getTableHeader().setBackground(new Color(122, 0, 0));
        tabelDaftar.getTableHeader().setForeground(Color.WHITE);
        tabelDaftar.getTableHeader().setFont(new Font("Inter", Font.BOLD, 12));
        tabelDaftar.getTableHeader().setOpaque(true);

        JScrollPane scrollTabel = new JScrollPane(tabelDaftar);
        add(scrollTabel, BorderLayout.CENTER);

        // FOOTER (Tombol)
        JPanel panelBawah = new JPanel();
        panelBawah.setOpaque(false);
        
        JButton btnNilai = new JButton("Beri Penilaian / Lihat Detail");
        btnNilai.setBackground(new Color(122, 0, 0));
        btnNilai.setForeground(Color.WHITE);
        btnNilai.setFont(new Font("Inter", Font.BOLD, 12));
        btnNilai.setFocusPainted(false);
        btnNilai.addActionListener(e -> bukaPanelPenilaian());
        
        panelBawah.add(btnNilai);
        add(panelBawah, BorderLayout.SOUTH);
    }
}