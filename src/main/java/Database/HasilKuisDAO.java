package Database;

import Model.HasilKuis;
import Model.JawabanSoal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * HasilKuis (1) -> JawabanSoal (banyak). Jawaban adalah snapshot dari soal saat kuis dikerjakan,
 * jadi tabel jawaban_soal tidak punya foreign key ke tabel soal.
 */
public class HasilKuisDAO {

    public static ArrayList<HasilKuis> ambilSemua() {
        ArrayList<HasilKuis> hasil = new ArrayList<>();
        HashMap<String, ArrayList<JawabanSoal>> jawabanPerHasil = new HashMap<>();

        try (Connection c = Koneksi.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement("SELECT * FROM jawaban_soal ORDER BY id_hasil, urutan, id_soal");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    JawabanSoal j = new JawabanSoal(rs.getString("id_soal"), rs.getString("tipe_soal"),
                            rs.getString("pertanyaan"), rs.getString("jawaban_siswa"),
                            rs.getString("kunci_jawaban"), rs.getInt("poin_maksimal"),
                            rs.getInt("poin_diperoleh"), rs.getBoolean("sudah_dinilai"));
                    jawabanPerHasil.computeIfAbsent(rs.getString("id_hasil"), k -> new ArrayList<>()).add(j);
                }
            }
            try (PreparedStatement ps = c.prepareStatement("SELECT * FROM hasil_kuis ORDER BY id_hasil");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id_hasil");
                    HasilKuis h = new HasilKuis(id, rs.getString("id_kuis"), rs.getString("judul_kuis"),
                            rs.getString("id_peserta"), rs.getString("tanggal_pengerjaan"),
                            jawabanPerHasil.getOrDefault(id, new ArrayList<>()));
                    h.setCatatanInstruktur(rs.getString("catatan_instruktur"));
                    hasil.add(h);
                }
            }
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
        }
        return hasil;
    }

    public static String idBerikutnya() {
        return Koneksi.idBerikutnya("hasil_kuis", "id_hasil", "HSL");
    }

    /** Menyimpan hasil beserta seluruh jawabannya dalam satu transaksi. */
    public static boolean tambah(HasilKuis h) {
        String sqlHasil = "INSERT INTO hasil_kuis (id_hasil, id_kuis, judul_kuis, id_peserta, "
                + "tanggal_pengerjaan, catatan_instruktur) VALUES (?,?,?,?,?,?)";
        String sqlJawaban = "INSERT INTO jawaban_soal (id_hasil, id_soal, urutan, tipe_soal, pertanyaan, "
                + "jawaban_siswa, kunci_jawaban, poin_maksimal, poin_diperoleh, sudah_dinilai) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = Koneksi.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement p1 = c.prepareStatement(sqlHasil);
                 PreparedStatement p2 = c.prepareStatement(sqlJawaban)) {
                p1.setString(1, h.getIdHasil());
                p1.setString(2, h.getIdKuis());
                p1.setString(3, h.getJudulKuis());
                p1.setString(4, h.getIdPeserta());
                p1.setString(5, h.getTanggalPengerjaan());
                p1.setString(6, h.getCatatanInstruktur());
                p1.executeUpdate();

                int urutan = 1;
                for (JawabanSoal j : h.getDaftarJawaban()) {
                    p2.setString(1, h.getIdHasil());
                    p2.setString(2, j.getIdSoal());
                    p2.setInt(3, urutan++);
                    p2.setString(4, j.getTipeSoal());
                    p2.setString(5, j.getPertanyaan());
                    p2.setString(6, j.getJawabanSiswa());
                    p2.setString(7, j.getKunciJawaban());
                    p2.setInt(8, j.getPoinMaksimal());
                    p2.setInt(9, j.getPoinDiperoleh());
                    p2.setBoolean(10, j.isSudahDinilai());
                    p2.addBatch();
                }
                p2.executeBatch();
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

    /**
     * Dipakai saat instruktur menilai essay: memperbarui catatan serta poin dan status
     * penilaian tiap jawaban. Bagian lain dari hasil adalah snapshot dan tidak berubah
     * (sama seperti di kelas HasilKuis/JawabanSoal yang tidak punya setter-nya).
     */
    public static boolean ubah(HasilKuis h) {
        String sqlHasil = "UPDATE hasil_kuis SET catatan_instruktur = ? WHERE id_hasil = ?";
        String sqlJawaban = "UPDATE jawaban_soal SET poin_diperoleh = ?, sudah_dinilai = ? "
                + "WHERE id_hasil = ? AND id_soal = ?";
        try (Connection c = Koneksi.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement p1 = c.prepareStatement(sqlHasil);
                 PreparedStatement p2 = c.prepareStatement(sqlJawaban)) {
                p1.setString(1, h.getCatatanInstruktur());
                p1.setString(2, h.getIdHasil());
                p1.executeUpdate();

                for (JawabanSoal j : h.getDaftarJawaban()) {
                    p2.setInt(1, j.getPoinDiperoleh());
                    p2.setBoolean(2, j.isSudahDinilai());
                    p2.setString(3, h.getIdHasil());
                    p2.setString(4, j.getIdSoal());
                    p2.addBatch();
                }
                p2.executeBatch();
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

    /** Jawaban ikut terhapus (ON DELETE CASCADE). */
    public static boolean hapus(String idHasil) {
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM hasil_kuis WHERE id_hasil = ?")) {
            ps.setString(1, idHasil);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Koneksi.tampilkanError(e);
            return false;
        }
    }
}
