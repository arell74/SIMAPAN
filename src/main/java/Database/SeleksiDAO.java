package Database;

import DataStore.DataStore;
import Model.Peserta;
import Model.Seleksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SeleksiDAO {

    /** Panggil setelah daftarPeserta terisi. */
    public static ArrayList<Seleksi> ambilSemua() {
        ArrayList<Seleksi> hasil = new ArrayList<>();
        String sql = "SELECT * FROM seleksi ORDER BY id_seleksi";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Peserta p = DataStore.cariPeserta(rs.getString("id_peserta"));
                if (p == null) continue;
                hasil.add(new Seleksi(rs.getString("id_seleksi"), p, rs.getString("jenis_seleksi"),
                        rs.getString("tanggal_seleksi"), rs.getInt("nilai"), rs.getString("status_hasil")));
            }
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
        }
        return hasil;
    }

    public static String idBerikutnya() {
        return Koneksi.idBerikutnya("seleksi", "id_seleksi", "SEL");
    }

    public static boolean tambah(Seleksi s) {
        String sql = "INSERT INTO seleksi (id_seleksi, id_peserta, jenis_seleksi, tanggal_seleksi, nilai, status_hasil) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getIdSeleksi());
            ps.setString(2, s.getPeserta().getIdPeserta());
            ps.setString(3, s.getJenisSeleksi());
            ps.setString(4, s.getTanggalSeleksi());
            ps.setInt(5, s.getNilai());
            ps.setString(6, s.getStatusHasil());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }

    public static boolean ubah(Seleksi s) {
        String sql = "UPDATE seleksi SET id_peserta = ?, jenis_seleksi = ?, tanggal_seleksi = ?, "
                + "nilai = ?, status_hasil = ? WHERE id_seleksi = ?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getPeserta().getIdPeserta());
            ps.setString(2, s.getJenisSeleksi());
            ps.setString(3, s.getTanggalSeleksi());
            ps.setInt(4, s.getNilai());
            ps.setString(5, s.getStatusHasil());
            ps.setString(6, s.getIdSeleksi());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }

    public static boolean hapus(String idSeleksi) {
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM seleksi WHERE id_seleksi = ?")) {
            ps.setString(1, idSeleksi);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }
}
