package Database;

import DataStore.DataStore;
import Model.CatatanEvaluasi;
import Model.Peserta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CatatanEvaluasiDAO {

    /** Panggil setelah daftarPeserta terisi. */
    public static ArrayList<CatatanEvaluasi> ambilSemua() {
        ArrayList<CatatanEvaluasi> hasil = new ArrayList<>();
        String sql = "SELECT * FROM catatan_evaluasi ORDER BY id_catatan";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Peserta p = DataStore.cariPeserta(rs.getString("id_peserta"));
                if (p == null) continue;
                hasil.add(new CatatanEvaluasi(rs.getString("id_catatan"), p,
                        rs.getString("id_instruktur"), rs.getString("tanggal"), rs.getString("isi_catatan")));
            }
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
        }
        return hasil;
    }

    public static String idBerikutnya() {
        return Koneksi.idBerikutnya("catatan_evaluasi", "id_catatan", "CAT");
    }

    public static boolean tambah(CatatanEvaluasi cat) {
        String sql = "INSERT INTO catatan_evaluasi (id_peserta, id_instruktur, tanggal, isi_catatan, id_catatan) "
                + "VALUES (?,?,?,?,?)";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            isiParameter(ps, cat);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }

    public static boolean ubah(CatatanEvaluasi cat) {
        String sql = "UPDATE catatan_evaluasi SET id_peserta=?, id_instruktur=?, tanggal=?, isi_catatan=? "
                + "WHERE id_catatan=?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            isiParameter(ps, cat);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }

    public static boolean hapus(String idCatatan) {
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM catatan_evaluasi WHERE id_catatan = ?")) {
            ps.setString(1, idCatatan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }

    // Urutan 1..4 sama untuk INSERT dan UPDATE, id_catatan ke-5
    private static void isiParameter(PreparedStatement ps, CatatanEvaluasi cat) throws SQLException {
        ps.setString(1, cat.getPeserta().getIdPeserta());
        ps.setString(2, cat.getIdInstruktur());
        ps.setString(3, cat.getTanggal());
        ps.setString(4, cat.getIsiCatatan());
        ps.setString(5, cat.getIdCatatan());
    }
}
