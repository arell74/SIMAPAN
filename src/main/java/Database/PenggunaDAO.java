package Database;

import Model.AdminLPK;
import Model.Instruktur;
import Model.Pengguna;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Akses data Pengguna. Polimorfisme tetap dipertahankan: hasilnya satu
 * ArrayList<Pengguna> yang berisi objek AdminLPK dan Instruktur.
 */
public class PenggunaDAO {

    public static ArrayList<Pengguna> ambilSemua() {
        ArrayList<Pengguna> hasil = new ArrayList<>();
        String sql = "SELECT p.username, p.password, p.nama_lengkap, p.role, "
                + "i.id_instruktur, i.no_telp, i.spesialisasi, i.level_jlpt, i.kelas_diampu "
                + "FROM pengguna p LEFT JOIN instruktur i ON i.username = p.username "
                + "WHERE p.role <> 'PESERTA' "   // peserta dimuat oleh PesertaDAO
                + "ORDER BY p.role, i.id_instruktur";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String nama = rs.getString("nama_lengkap");
                if ("INSTRUKTUR".equals(rs.getString("role"))) {
                    hasil.add(new Instruktur(username, password, nama,
                            rs.getString("id_instruktur"), rs.getString("no_telp"),
                            rs.getString("spesialisasi"), rs.getString("level_jlpt"),
                            rs.getString("kelas_diampu")));
                } else {
                    hasil.add(new AdminLPK(username, password, nama));
                }
            }
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
        }
        return hasil;
    }

    public static String idInstrukturBerikutnya() {
        return Koneksi.idBerikutnya("instruktur", "id_instruktur", "INS");
    }

    /** Menyimpan ke 2 tabel sekaligus dalam satu transaksi. */
    public static boolean tambahInstruktur(Instruktur i) {
        String sqlPengguna = "INSERT INTO pengguna (username, password, nama_lengkap, role) "
                + "VALUES (?, ?, ?, 'INSTRUKTUR')";
        String sqlInstruktur = "INSERT INTO instruktur "
                + "(id_instruktur, username, no_telp, spesialisasi, level_jlpt, kelas_diampu) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = Koneksi.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement p1 = c.prepareStatement(sqlPengguna);
                 PreparedStatement p2 = c.prepareStatement(sqlInstruktur)) {
                p1.setString(1, i.getUsername());
                p1.setString(2, i.getPassword());
                p1.setString(3, i.getNamaLengkap());
                p1.executeUpdate();

                p2.setString(1, i.getIdInstruktur());
                p2.setString(2, i.getUsername());
                p2.setString(3, i.getNoTelp());
                p2.setString(4, i.getSpesialisasi());
                p2.setString(5, i.getLevelJlpt());
                p2.setString(6, i.getKelasDiampu());
                p2.executeUpdate();
                c.commit();
                return true;
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }

    public static boolean ubahInstruktur(Instruktur i) {
        String sqlPengguna = "UPDATE pengguna SET password = ?, nama_lengkap = ? WHERE username = ?";
        String sqlInstruktur = "UPDATE instruktur SET no_telp = ?, spesialisasi = ?, "
                + "level_jlpt = ?, kelas_diampu = ? WHERE id_instruktur = ?";
        try (Connection c = Koneksi.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement p1 = c.prepareStatement(sqlPengguna);
                 PreparedStatement p2 = c.prepareStatement(sqlInstruktur)) {
                p1.setString(1, i.getPassword());
                p1.setString(2, i.getNamaLengkap());
                p1.setString(3, i.getUsername());
                p1.executeUpdate();

                p2.setString(1, i.getNoTelp());
                p2.setString(2, i.getSpesialisasi());
                p2.setString(3, i.getLevelJlpt());
                p2.setString(4, i.getKelasDiampu());
                p2.setString(5, i.getIdInstruktur());
                p2.executeUpdate();
                c.commit();
                return true;
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }

    /** Gagal (dengan pesan) bila instruktur masih membimbing peserta. */
    public static boolean hapusInstruktur(String idInstruktur) {
        String sql = "DELETE FROM pengguna WHERE username = "
                + "(SELECT username FROM instruktur WHERE id_instruktur = ?)";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idInstruktur);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }
}
