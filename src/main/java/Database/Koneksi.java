package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Pengelola koneksi MySQL. Default XAMPP: user "root", password kosong, port 3306.
 */
public class Koneksi {

    private static final String URL =
            "jdbc:mysql://localhost:3306/simapan?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /** Dipanggil dari Main sebelum membuka login, untuk memastikan MySQL sudah jalan. */
    public static boolean tes() {
        try (Connection c = getConnection()) {
            return c.isValid(2);
        } catch (SQLException e) {
            tampilkanError(e);
            return false;
        }
    }

    /** ID berikutnya, contoh: idBerikutnya("peserta", "id_peserta", "PST") -> "PST006". */
    public static String idBerikutnya(String tabel, String kolom, String prefix) {
        String sql = "SELECT MAX(CAST(SUBSTRING(" + kolom + ", " + (prefix.length() + 1)
                + ") AS UNSIGNED)) FROM " + tabel;
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int terakhir = rs.next() ? rs.getInt(1) : 0;   // NULL dibaca sebagai 0
            return String.format("%s%03d", prefix, terakhir + 1);
        } catch (SQLException e) {
            tampilkanError(e);
            return prefix + "001";
        }
    }

    public static void tampilkanError(SQLException e) {
        String pesan;
        switch (e.getErrorCode()) {
            case 1062:
                pesan = "Data duplikat: ID, NIK, atau username sudah dipakai.";
                break;
            case 1451:
                pesan = "Data tidak bisa dihapus/diubah karena masih dipakai data lain.";
                break;
            case 1452:
                pesan = "Data referensi (program/instruktur/peserta) tidak ditemukan.";
                break;
            default:
                pesan = "Kesalahan database: " + e.getMessage()
                        + "\n\nPastikan MySQL di XAMPP sudah Start dan database 'simapan' sudah diimport.";
        }
        JOptionPane.showMessageDialog(null, pesan, "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}
