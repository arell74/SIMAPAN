package DataStore;

import Database.CatatanEvaluasiDAO;
import Database.DokumenDAO;
import Database.HasilKuisDAO;
import Database.JadwalDAO;
import Database.KuisDAO;
import Database.MateriDAO;
import Database.PenggunaDAO;
import Database.PesertaDAO;
import Database.ProgramDAO;
import Database.SeleksiDAO;
import Model.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * DataStore berfungsi sebagai cache dari database MySQL untuk: program, pengguna (admin,
 * instruktur, peserta), seleksi, dokumen, jadwal, materi, kuis (beserta soal), dan catatan evaluasi.
 * Hasil kuis (beserta jawabannya) juga dimuat dari database.
 * Yang MASIH in-memory (belum dimigrasi): kehadiran peserta (kehadiranHariIni).
 *
 * PENTING: deklarasi list harus berada SEBELUM blok static { } di bawah.
 */
public class DataStore {
//    polymorphism
    public static ArrayList<Pengguna> daftarPengguna = new ArrayList<>();
    public static ArrayList<Peserta> daftarPeserta = new ArrayList<>();
    public static ArrayList<Program> daftarProgram = new ArrayList<>();
    public static ArrayList<Seleksi> daftarSeleksi = new ArrayList<>();
    public static ArrayList<Dokumen> daftarDokumen = new ArrayList<>();
    public static ArrayList<Jadwal> daftarJadwal = new ArrayList<>();
    public static ArrayList<Materi> daftarMateri = new ArrayList<>();
    public static ArrayList<Kuis> daftarKuis = new ArrayList<>();
    public static ArrayList<HasilKuis> daftarHasilKuis = new ArrayList<>();
    public static ArrayList<CatatanEvaluasi> daftarCatatanEvaluasi = new ArrayList<>();
    public static Pengguna penggunaLogin;

    static {
        muatUlang();   // seluruh data awal kini ada di db/simapan.sql
    }

    /**
     * Mengisi ulang list dari database. Urutan penting karena ada relasi antar objek:
     * program -> admin/instruktur -> peserta -> seleksi, dokumen, catatan -> jadwal, materi, kuis -> hasil kuis.
     * Memakai clear()+addAll() supaya referensi list yang dipegang panel tetap valid.
     */
    public static void muatUlang() {
        // kehadiranHariIni belum disimpan di database (data harian), jadi diselamatkan dulu
        // agar tidak kembali ke "Hadir" setiap kali muatUlang() dipanggil.
        HashMap<String, String> kehadiran = new HashMap<>();
        for (Peserta p : daftarPeserta) {
            kehadiran.put(p.getIdPeserta(), p.getKehadiranHariIni());
        }

        daftarProgram.clear();
        daftarProgram.addAll(ProgramDAO.ambilSemua());

        daftarPengguna.clear();
        daftarPengguna.addAll(PenggunaDAO.ambilSemua());      // Admin + Instruktur

        daftarPeserta.clear();
        daftarPeserta.addAll(PesertaDAO.ambilSemua());
        for (Peserta p : daftarPeserta) {
            String k = kehadiran.get(p.getIdPeserta());
            if (k != null) p.setKehadiranHariIni(k);
        }
        // Peserta adalah turunan Pengguna -> ikut masuk daftarPengguna (polimorfisme, login peserta).
        daftarPengguna.addAll(daftarPeserta);

        daftarSeleksi.clear();
        daftarSeleksi.addAll(SeleksiDAO.ambilSemua());

        daftarDokumen.clear();
        daftarDokumen.addAll(DokumenDAO.ambilSemua());

        daftarCatatanEvaluasi.clear();
        daftarCatatanEvaluasi.addAll(CatatanEvaluasiDAO.ambilSemua());

        daftarJadwal.clear();
        daftarJadwal.addAll(JadwalDAO.ambilSemua());

        daftarMateri.clear();
        daftarMateri.addAll(MateriDAO.ambilSemua());

        daftarKuis.clear();
        daftarKuis.addAll(KuisDAO.ambilSemua());

        daftarHasilKuis.clear();
        daftarHasilKuis.addAll(HasilKuisDAO.ambilSemua());

        // penggunaLogin harus menunjuk ke objek yang baru dimuat, bukan objek lama
        if (penggunaLogin != null) {
            for (Pengguna p : daftarPengguna) {
                if (p.getUsername().equals(penggunaLogin.getUsername())) {
                    penggunaLogin = p;
                    break;
                }
            }
        }
    }

    public static ArrayList<Instruktur> getHanyaInstruktur() {
        ArrayList<Instruktur> listInstruktur = new ArrayList<>();
        for (Pengguna p : daftarPengguna) {
            if (p instanceof Instruktur) {
                listInstruktur.add((Instruktur) p);
            }
        }
        return listInstruktur;
    }

    public static Instruktur cariInstruktur(String idInstruktur) {
        if (idInstruktur == null) return null;
        for (Instruktur i : getHanyaInstruktur()) {
            if (idInstruktur.equals(i.getIdInstruktur())) return i;
        }
        return null;
    }

    public static Peserta cariPeserta(String idPeserta) {
        if (idPeserta == null) return null;
        for (Peserta p : daftarPeserta) {
            if (idPeserta.equals(p.getIdPeserta())) return p;
        }
        return null;
    }

    public static Program cariProgram(String idProgram) {
        if (idProgram == null) return null;
        for (Program p : daftarProgram) {
            if (idProgram.equals(p.getIdProgram())) return p;
        }
        return null;
    }
}
