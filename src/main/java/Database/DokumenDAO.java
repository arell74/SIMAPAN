package Database;

import DataStore.DataStore;
import Model.Dokumen;
import Model.Peserta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Dokumen keberangkatan: paspor, MCU, visa, dan KTP (nomor/hasil + path file).
 * Yang disimpan di database hanya teks & path file; berkasnya tetap ada di disk.
 */
public class DokumenDAO {

    /** Panggil setelah daftarPeserta terisi. */
    public static ArrayList<Dokumen> ambilSemua() {
        ArrayList<Dokumen> hasil = new ArrayList<>();
        String sql = "SELECT * FROM dokumen ORDER BY id_dokumen";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Peserta p = DataStore.cariPeserta(rs.getString("id_peserta"));
                if (p == null) continue;
                Dokumen d = new Dokumen(rs.getString("id_dokumen"), p, rs.getString("tanggal_berangkat"));
                d.setPaspor(rs.getString("no_paspor"), rs.getString("file_paspor"));
                d.setMcu(rs.getString("hasil_mcu"), rs.getString("file_mcu"));
                d.setVisa(rs.getString("no_visa"), rs.getString("file_visa"));
                d.setKtp(rs.getString("nik_ktp"), rs.getString("file_ktp"));
                hasil.add(d);
            }
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
        }
        return hasil;
    }

    public static String idBerikutnya() {
        return Koneksi.idBerikutnya("dokumen", "id_dokumen", "DOK");
    }

    public static boolean tambah(Dokumen d) {
        String sql = "INSERT INTO dokumen (tanggal_berangkat, no_paspor, file_paspor, hasil_mcu, file_mcu, "
                + "no_visa, file_visa, nik_ktp, file_ktp, id_dokumen, id_peserta) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            isiParameter(ps, d);
            ps.setString(11, d.getPeserta().getIdPeserta());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }

    public static boolean ubah(Dokumen d) {
        String sql = "UPDATE dokumen SET tanggal_berangkat=?, no_paspor=?, file_paspor=?, hasil_mcu=?, "
                + "file_mcu=?, no_visa=?, file_visa=?, nik_ktp=?, file_ktp=? WHERE id_dokumen=?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            isiParameter(ps, d);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }

    public static boolean hapus(String idDokumen) {
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM dokumen WHERE id_dokumen = ?")) {
            ps.setString(1, idDokumen);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }

    // Parameter 1..9 = kolom data, 10 = id_dokumen (sama untuk INSERT dan UPDATE).
    private static void isiParameter(PreparedStatement ps, Dokumen d) throws SQLException {
        ps.setString(1, d.getTanggalBerangkat());
        ps.setString(2, d.getNoPaspor());
        ps.setString(3, d.getFilePaspor());
        ps.setString(4, d.getHasilMcu());
        ps.setString(5, d.getFileMcu());
        ps.setString(6, d.getNoVisa());
        ps.setString(7, d.getFileVisa());
        ps.setString(8, d.getNikKtp());
        ps.setString(9, d.getFileKtp());
        ps.setString(10, d.getIdDokumen());
    }
}
