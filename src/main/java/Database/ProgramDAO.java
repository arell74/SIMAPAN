package Database;

import Model.Program;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProgramDAO {

    public static ArrayList<Program> ambilSemua() {
        ArrayList<Program> hasil = new ArrayList<>();
        String sql = "SELECT * FROM program ORDER BY id_program";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                hasil.add(new Program(rs.getString("id_program"), rs.getString("nama_program"),
                        rs.getString("bidang_usaha"), rs.getDouble("biaya"), rs.getInt("kuota")));
            }
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
        }
        return hasil;
    }

    public static String idBerikutnya() {
        return Koneksi.idBerikutnya("program", "id_program", "PRG");
    }

    public static boolean tambah(Program p) {
        String sql = "INSERT INTO program (id_program, nama_program, bidang_usaha, biaya, kuota) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getIdProgram());
            ps.setString(2, p.getNamaProgram());
            ps.setString(3, p.getBidangUsaha());
            ps.setDouble(4, p.getBiaya());
            ps.setInt(5, p.getKuota());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }

    public static boolean ubah(Program p) {
        String sql = "UPDATE program SET nama_program = ?, bidang_usaha = ?, biaya = ?, kuota = ? "
                + "WHERE id_program = ?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNamaProgram());
            ps.setString(2, p.getBidangUsaha());
            ps.setDouble(3, p.getBiaya());
            ps.setInt(4, p.getKuota());
            ps.setString(5, p.getIdProgram());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }

    /** Gagal (dengan pesan) bila program masih dipakai peserta. */
    public static boolean hapus(String idProgram) {
        String sql = "DELETE FROM program WHERE id_program = ?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idProgram);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }
}
