package Database;

import Model.Materi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/** Yang disimpan di database hanya teks dan nama berkas; berkasnya tetap di folder Brankas_Materi. */
public class MateriDAO {

    public static ArrayList<Materi> ambilSemua() {
        ArrayList<Materi> hasil = new ArrayList<>();
        String sql = "SELECT * FROM materi ORDER BY id_materi";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                hasil.add(new Materi(rs.getString("id_materi"), rs.getString("id_instruktur"),
                        rs.getString("judul"), rs.getString("kelas_target"), rs.getString("tipe"),
                        rs.getString("isi_teks"), rs.getString("nama_file_asli"),
                        rs.getString("nama_file_tersimpan"), rs.getString("tanggal_upload")));
            }
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
        }
        return hasil;
    }

    public static String idBerikutnya() {
        return Koneksi.idBerikutnya("materi", "id_materi", "MAT");
    }

    public static boolean tambah(Materi m) {
        String sql = "INSERT INTO materi (id_materi, id_instruktur, judul, kelas_target, tipe, isi_teks, "
                + "nama_file_asli, nama_file_tersimpan, tanggal_upload) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getIdMateri());
            ps.setString(2, m.getIdInstruktur());
            ps.setString(3, m.getJudul());
            ps.setString(4, m.getKelasTarget());
            ps.setString(5, m.getTipe());
            ps.setString(6, m.getIsiTeks());
            ps.setString(7, m.getNamaFileAsli());
            ps.setString(8, m.getNamaFileTersimpan());
            ps.setString(9, m.getTanggalUpload());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }

    /** Tipe, instruktur, dan tanggal upload tidak bisa diubah (sama seperti di kelas Materi). */
    public static boolean ubah(Materi m) {
        String sql = "UPDATE materi SET judul=?, kelas_target=?, isi_teks=?, nama_file_asli=?, "
                + "nama_file_tersimpan=? WHERE id_materi=?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getJudul());
            ps.setString(2, m.getKelasTarget());
            ps.setString(3, m.getIsiTeks());
            ps.setString(4, m.getNamaFileAsli());
            ps.setString(5, m.getNamaFileTersimpan());
            ps.setString(6, m.getIdMateri());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }

    public static boolean hapus(String idMateri) {
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM materi WHERE id_materi = ?")) {
            ps.setString(1, idMateri);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }
}
