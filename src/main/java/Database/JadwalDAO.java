package Database;

import Model.Jadwal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JadwalDAO {

    public static ArrayList<Jadwal> ambilSemua() {
        ArrayList<Jadwal> hasil = new ArrayList<>();
        String sql = "SELECT * FROM jadwal ORDER BY id_jadwal";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                hasil.add(new Jadwal(rs.getString("id_jadwal"), rs.getString("id_instruktur"),
                        rs.getString("hari"), rs.getString("jam_mulai"), rs.getString("nama_materi"),
                        rs.getString("nama_kelas"), rs.getInt("durasi"), rs.getString("status_jadwal")));
            }
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
        }
        return hasil;
    }

    public static String idBerikutnya() {
        return Koneksi.idBerikutnya("jadwal", "id_jadwal", "JDW");
    }

    public static boolean tambah(Jadwal j) {
        String sql = "INSERT INTO jadwal (hari, jam_mulai, nama_materi, nama_kelas, durasi, status_jadwal, "
                + "id_instruktur, id_jadwal) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            isiParameter(ps, j);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }

    public static boolean ubah(Jadwal j) {
        String sql = "UPDATE jadwal SET hari=?, jam_mulai=?, nama_materi=?, nama_kelas=?, durasi=?, "
                + "status_jadwal=?, id_instruktur=? WHERE id_jadwal=?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            isiParameter(ps, j);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }

    public static boolean hapus(String idJadwal) {
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM jadwal WHERE id_jadwal = ?")) {
            ps.setString(1, idJadwal);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }

    // Urutan 1..8 sama untuk INSERT dan UPDATE
    private static void isiParameter(PreparedStatement ps, Jadwal j) throws SQLException {
        ps.setString(1, j.getHari());
        ps.setString(2, j.getJamMulai());
        ps.setString(3, j.getNamaMateri());
        ps.setString(4, j.getNamaKelas());
        ps.setInt(5, j.getDurasi());
        ps.setString(6, j.getStatusJadwal());
        ps.setString(7, j.getIdInstruktur());
        ps.setString(8, j.getIdJadwal());
    }
}
