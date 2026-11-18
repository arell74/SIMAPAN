package Database;

import DataStore.DataStore;
import Model.Instruktur;
import Model.Peserta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Peserta adalah turunan Pengguna, jadi datanya tersimpan di 2 tabel:
 *  - pengguna : username, password, nama_lengkap, role = 'PESERTA'
 *  - peserta  : data khusus peserta
 * Insert/update/hapus dilakukan dalam satu transaksi.
 */
public class PesertaDAO {

    // Urutan parameter 1..14 sama untuk INSERT dan UPDATE (id_peserta ke-14),
    // INSERT menambah username sebagai parameter ke-15.
    private static final String SQL_INSERT =
            "INSERT INTO peserta (nik, tanggal_lahir, jenis_kelamin, no_hp, agama, alamat, "
            + "id_program, id_instruktur, level_bahasa, status_pembayaran, kelas, status_seleksi, "
            + "status_keberangkatan, id_peserta, username) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private static final String SQL_UPDATE =
            "UPDATE peserta SET nik=?, tanggal_lahir=?, jenis_kelamin=?, no_hp=?, agama=?, alamat=?, "
            + "id_program=?, id_instruktur=?, level_bahasa=?, status_pembayaran=?, kelas=?, "
            + "status_seleksi=?, status_keberangkatan=? WHERE id_peserta=?";
    
    public static ArrayList<Peserta> ambilSemua() {
        ArrayList<Peserta> hasil = new ArrayList<>();
        String sql = "SELECT pe.*, pg.password, pg.nama_lengkap "
                + "FROM peserta pe JOIN pengguna pg ON pg.username = pe.username "
                + "ORDER BY pe.id_peserta";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Instruktur ins = DataStore.cariInstruktur(rs.getString("id_instruktur"));
                Peserta p = new Peserta(
                        rs.getString("username"), rs.getString("password"),
                        rs.getString("id_peserta"), rs.getString("nik"), rs.getString("nama_lengkap"),
                        rs.getString("tanggal_lahir"),          
                        rs.getString("jenis_kelamin"), rs.getString("no_hp"),
                        rs.getString("agama"), rs.getString("alamat"),
                        rs.getString("id_program"), ins, rs.getString("level_bahasa"),
                        rs.getString("status_pembayaran"),
                        rs.getString("status_seleksi"), rs.getString("status_keberangkatan"));
                p.setKelas(rs.getString("kelas"));
                hasil.add(p);
            }
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
        }
        return hasil;
    }

    public static String idBerikutnya() {
        return Koneksi.idBerikutnya("peserta", "id_peserta", "PST");
    }

    public static boolean tambah(Peserta p) {
        return tambah(p, true);
    }

    /** tampilkanError=false dipakai saat import massal agar tidak muncul popup per baris. */
    public static boolean tambah(Peserta p, boolean tampilkanError) {
        String sqlPengguna = "INSERT INTO pengguna (username, password, nama_lengkap, role) "
                + "VALUES (?, ?, ?, 'PESERTA')";
        try (Connection c = Koneksi.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement p1 = c.prepareStatement(sqlPengguna);
                 PreparedStatement p2 = c.prepareStatement(SQL_INSERT)) {
                p1.setString(1, p.getUsername());
                p1.setString(2, p.getPassword());
                p1.setString(3, p.getNamaLengkap());
                p1.executeUpdate();

                isiParameter(p2, p);
                p2.setString(15, p.getUsername());
                p2.executeUpdate();
                c.commit();
                return true;
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            if (tampilkanError) Koneksi.tampilkanError(e);
            return false;
        }
    }

    public static boolean ubah(Peserta p) {
        return ubah(p, true);
    }

    /** Dipakai juga untuk memperbarui status seleksi / keberangkatan / kelas. Username tidak diubah. */
    public static boolean ubah(Peserta p, boolean tampilkanError) {
        String sqlPengguna = "UPDATE pengguna SET password = ?, nama_lengkap = ? "
                + "WHERE username = (SELECT username FROM peserta WHERE id_peserta = ?)";
        try (Connection c = Koneksi.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement p1 = c.prepareStatement(sqlPengguna);
                 PreparedStatement p2 = c.prepareStatement(SQL_UPDATE)) {
                p1.setString(1, p.getPassword());
                p1.setString(2, p.getNamaLengkap());
                p1.setString(3, p.getIdPeserta());
                p1.executeUpdate();

                isiParameter(p2, p);
                p2.executeUpdate();
                c.commit();
                return true;
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            if (tampilkanError) Koneksi.tampilkanError(e);
            return false;
        }
    }

    /** Menghapus baris pengguna; peserta, seleksi, dan dokumen ikut terhapus (ON DELETE CASCADE). */
    public static boolean hapus(String idPeserta) {
        String sql = "DELETE FROM pengguna WHERE username = "
                + "(SELECT username FROM peserta WHERE id_peserta = ?)";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idPeserta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }

    private static void isiParameter(PreparedStatement ps, Peserta p) throws SQLException {
        Instruktur damping = p.getInstrukturDamping();
        ps.setString(1, p.getNik());
        ps.setString(2, p.getTanggalLahir());                 // format yyyy-MM-dd
        ps.setString(3, p.getJenisKelamin());
        ps.setString(4, p.getNoHp());
        ps.setString(5, p.getAgama());
        ps.setString(6, p.getAlamat());
        ps.setString(7, p.getProgram());                      // berisi ID program, mis. PRG001
        ps.setString(8, damping == null ? null : damping.getIdInstruktur());
        ps.setString(9, p.getLevelBahasa());
        ps.setString(10, atau(p.getStatusPembayaran(), "Belum Lunas"));
        ps.setString(11, atau(p.getKelas(), "Belum Ditentukan"));
        ps.setString(12, atau(p.getStatusSeleksi(), "Belum Mulai"));
        ps.setString(13, atau(p.getStatusKeberangkatan(), "Belum Berangkat"));
        ps.setString(14, p.getIdPeserta());
    }

    private static String atau(String nilai, String bawaan) {
        return (nilai == null || nilai.trim().isEmpty()) ? bawaan : nilai;
    }
}
