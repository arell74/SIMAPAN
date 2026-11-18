package Database;

import Model.Kuis;
import Model.Soal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Kuis (1) -> Soal (banyak). Satu objek Kuis menyimpan ArrayList<Soal>,
 * di database dipecah menjadi tabel kuis dan tabel soal.
 */
public class KuisDAO {

    public static ArrayList<Kuis> ambilSemua() {
        ArrayList<Kuis> hasil = new ArrayList<>();
        HashMap<String, ArrayList<Soal>> soalPerKuis = new HashMap<>();

        try (Connection c = Koneksi.getConnection()) {
            // 1. semua soal, dikelompokkan per kuis (urutan dipertahankan)
            try (PreparedStatement ps = c.prepareStatement("SELECT * FROM soal ORDER BY id_kuis, urutan, id_soal");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Soal s = new Soal(rs.getString("id_soal"), rs.getString("tipe_soal"),
                            rs.getString("pertanyaan"), rs.getString("pilihan_a"), rs.getString("pilihan_b"),
                            rs.getString("pilihan_c"), rs.getString("pilihan_d"),
                            rs.getString("kunci_jawaban"), rs.getInt("poin"));
                    soalPerKuis.computeIfAbsent(rs.getString("id_kuis"), k -> new ArrayList<>()).add(s);
                }
            }
            // 2. kuis, lengkap dengan daftar soalnya
            try (PreparedStatement ps = c.prepareStatement("SELECT * FROM kuis ORDER BY id_kuis");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id_kuis");
                    ArrayList<Soal> daftar = soalPerKuis.getOrDefault(id, new ArrayList<>());
                    hasil.add(new Kuis(id, rs.getString("id_instruktur"), rs.getString("judul_kuis"),
                            rs.getString("kelas_target"), rs.getString("deskripsi"),
                            rs.getInt("batas_waktu_menit"), rs.getString("tanggal_dibuat"), daftar));
                }
            }
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
        }
        return hasil;
    }

    public static String idBerikutnya() {
        return Koneksi.idBerikutnya("kuis", "id_kuis", "KUIS");
    }

    public static String idSoalBerikutnya() {
        return Koneksi.idBerikutnya("soal", "id_soal", "SOAL");
    }

    /** Menyimpan kuis beserta seluruh soalnya dalam satu transaksi. */
    public static boolean tambah(Kuis k) {
        String sql = "INSERT INTO kuis (id_instruktur, judul_kuis, kelas_target, deskripsi, "
                + "batas_waktu_menit, tanggal_dibuat, id_kuis) VALUES (?,?,?,?,?,?,?)";
        try (Connection c = Koneksi.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                isiParameterKuis(ps, k);
                ps.executeUpdate();
                simpanSoal(c, k);
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

    /** Memperbarui kuis; soal yang masih ada di daftar di-update, yang sudah dibuang dari daftar dihapus. */
    public static boolean ubah(Kuis k) {
        String sql = "UPDATE kuis SET id_instruktur=?, judul_kuis=?, kelas_target=?, deskripsi=?, "
                + "batas_waktu_menit=?, tanggal_dibuat=? WHERE id_kuis=?";
        try (Connection c = Koneksi.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                isiParameterKuis(ps, k);
                ps.executeUpdate();
                simpanSoal(c, k);
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

    /** Soal ikut terhapus (ON DELETE CASCADE). */
    public static boolean hapus(String idKuis) {
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM kuis WHERE id_kuis = ?")) {
            ps.setString(1, idKuis);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }

    // Urutan 1..6 sama untuk INSERT dan UPDATE, id_kuis ke-7
    private static void isiParameterKuis(PreparedStatement ps, Kuis k) throws SQLException {
        ps.setString(1, k.getIdInstruktur());
        ps.setString(2, k.getJudulKuis());
        ps.setString(3, k.getKelasTarget());
        ps.setString(4, k.getDeskripsi());
        ps.setInt(5, k.getBatasWaktuMenit());
        ps.setString(6, k.getTanggalDibuat());
        ps.setString(7, k.getIdKuis());
    }

    /**
     * Upsert semua soal milik kuis, lalu buang soal lain milik kuis yang sama.
     * Dipilih upsert (bukan hapus-lalu-tulis-ulang) agar ID soal tetap stabil
     * ketika nanti jawaban peserta dihubungkan ke soal.
     */
    private static void simpanSoal(Connection c, Kuis k) throws SQLException {
        String upsert = "INSERT INTO soal (id_soal, id_kuis, urutan, tipe_soal, pertanyaan, pilihan_a, "
                + "pilihan_b, pilihan_c, pilihan_d, kunci_jawaban, poin) VALUES (?,?,?,?,?,?,?,?,?,?,?) "
                + "ON DUPLICATE KEY UPDATE id_kuis=VALUES(id_kuis), urutan=VALUES(urutan), "
                + "tipe_soal=VALUES(tipe_soal), pertanyaan=VALUES(pertanyaan), pilihan_a=VALUES(pilihan_a), "
                + "pilihan_b=VALUES(pilihan_b), pilihan_c=VALUES(pilihan_c), pilihan_d=VALUES(pilihan_d), "
                + "kunci_jawaban=VALUES(kunci_jawaban), poin=VALUES(poin)";
        ArrayList<Soal> daftar = k.getDaftarSoal();

        try (PreparedStatement ps = c.prepareStatement(upsert)) {
            int urutan = 1;
            for (Soal s : daftar) {
                ps.setString(1, s.getIdSoal());
                ps.setString(2, k.getIdKuis());
                ps.setInt(3, urutan++);
                ps.setString(4, s.getTipeSoal());
                ps.setString(5, s.getPertanyaan());
                ps.setString(6, s.getPilihanA());
                ps.setString(7, s.getPilihanB());
                ps.setString(8, s.getPilihanC());
                ps.setString(9, s.getPilihanD());
                ps.setString(10, s.getKunciJawaban());
                ps.setInt(11, s.getPoin());
                ps.addBatch();
            }
            ps.executeBatch();
        }

        StringBuilder sb = new StringBuilder("DELETE FROM soal WHERE id_kuis = ?");
        if (!daftar.isEmpty()) {
            sb.append(" AND id_soal NOT IN (");
            for (int i = 0; i < daftar.size(); i++) sb.append(i == 0 ? "?" : ",?");
            sb.append(")");
        }
        try (PreparedStatement ps = c.prepareStatement(sb.toString())) {
            ps.setString(1, k.getIdKuis());
            for (int i = 0; i < daftar.size(); i++) ps.setString(i + 2, daftar.get(i).getIdSoal());
            ps.executeUpdate();
        }
    }
}
