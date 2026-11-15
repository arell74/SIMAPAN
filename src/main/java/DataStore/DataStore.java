package DataStore;

import Database.DokumenDAO;
import Database.PenggunaDAO;
import Database.PesertaDAO;
import Database.ProgramDAO;
import Database.SeleksiDAO;
import Model.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * DataStore kini berfungsi sebagai cache dari database MySQL untuk:
 * program, pengguna (admin, instruktur, peserta), seleksi, dan dokumen.
 * Jadwal, materi, kuis, hasil kuis, dan catatan evaluasi MASIH in-memory (belum dimigrasi).
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
        // Program, Admin, Instruktur, Peserta, Dokumen, Seleksi -> dari database
        muatUlang();

        // ===== Bagian di bawah ini BELUM dimigrasi, tetap data dummy di memori =====
        daftarJadwal.add(new Jadwal("JDW001", "INS003", "Senin", "08 : 00", "Minna No Nihonggo Bab 1", "A", 120, "Selesai"));
        daftarJadwal.add(new Jadwal("JDW002", "INS003", "Rabu",  "08 : 00", "Minna No Nihonggo Bab 1", "A", 90,  "Hari ini"));
        daftarJadwal.add(new Jadwal("JDW003", "INS003", "Kamis", "08 : 00", "Minna No Nihonggo Bab 1", "A", 120, "Belum Dimulai"));

        // Contoh jadwal untuk instruktur lain (Tidak akan muncul di dashboard INS001)
        daftarJadwal.add(new Jadwal("JDW004", "INS002", "Selasa", "10 : 00", "Percakapan Bisnis (Keigo)", "B", 120, "Hari ini"));

//        =========================== Materi (dummy) ===========================
        Materi mat1 = new Materi("MAT001", "INS001", "Pengenalan Huruf Hiragana",
            "Kelas A", "Teks",
            "Hiragana adalah salah satu huruf dasar dalam Bahasa Jepang yang digunakan untuk menulis kata-kata "
            + "asli Jepang. Terdapat 46 huruf dasar yang dimulai dari deret vokal a-i-u-e-o, lalu diikuti "
            + "konsonan ka-ki-ku-ke-ko, sa-shi-su-se-so, dan seterusnya. Pelajari dan hafalkan urutan ini "
            + "sebelum lanjut ke materi berikutnya.",
            null, null, "01/10/2025");
        daftarMateri.add(mat1);

        Materi mat2 = new Materi("MAT002", "INS001", "Kosakata Sehari-hari (Aisatsu)",
            "Kelas A", "Teks",
            "Aisatsu (salam) yang wajib dihafal: Ohayou Gozaimasu (Selamat pagi), Konnichiwa (Selamat siang), "
            + "Konbanwa (Selamat malam), Arigatou Gozaimasu (Terima kasih), Sumimasen (Permisi/maaf), "
            + "Itadakimasu (Selamat makan), dan Otsukaresama Desu (Terima kasih atas kerja kerasnya).",
            null, null, "03/10/2025");
        daftarMateri.add(mat2);

        Materi mat3 = new Materi("MAT003", "INS002", "Latihan Kanji N1 - Bab 1",
            "Kelas B", "File",
            null, "kanji_bab1.pdf", "MAT003.pdf", "05/10/2025");
        daftarMateri.add(mat3);

        Materi mat4 = new Materi("MAT004", "INS003", "Tata Bahasa Dasar - Partikel Wa dan Ga",
            "Semua Kelas", "Teks",
            "Partikel 'Wa' (ditulis dengan huruf Ha) digunakan untuk menandai topik kalimat, sedangkan partikel "
            + "'Ga' digunakan untuk menandai subjek yang menjadi fokus/informasi baru. Contoh: 'Watashi wa "
            + "gakusei desu' (Saya adalah siswa) menekankan topik 'saya', sementara 'Dare ga kimasu ka?' "
            + "(Siapa yang datang?) menekankan subjek yang ditanyakan.",
            null, null, "07/10/2025");
        daftarMateri.add(mat4);

        Materi mat5 = new Materi("MAT005", "INS001", "Video Latihan Percakapan Restoran",
            "Kelas A", "File",
            null, "percakapan_restoran.mp4", "MAT005.mp4", "10/10/2025");
        daftarMateri.add(mat5);

//        =========================== Kuis (dummy) ===========================
        ArrayList<Soal> soalKuis1 = new ArrayList<>();
        soalKuis1.add(Soal.buatPilihanGanda("SOAL001", "Huruf「あ」dibaca sebagai...",
            "A", "I", "U", "E", "A", 10));
        soalKuis1.add(Soal.buatPilihanGanda("SOAL002", "Huruf「こ」dibaca sebagai...",
            "Ko", "Ku", "Ka", "Ke", "A", 10));
        soalKuis1.add(Soal.buatEssay("SOAL003",
            "Tuliskan 5 huruf hiragana vokal dasar (a, i, u, e, o) beserta cara bacanya!",
            "a dibaca a, i dibaca i, u dibaca u, e dibaca e, o dibaca o (sesuai bunyi vokalnya)", 20));

        Kuis kuis1 = new Kuis("KUIS001", "INS001", "Kuis Hiragana Dasar", "Kelas A",
            "Kuis untuk menguji pemahaman huruf hiragana dasar dan cara membacanya.", 20, "02/10/2025", soalKuis1);
        daftarKuis.add(kuis1);

        ArrayList<Soal> soalKuis2 = new ArrayList<>();
        soalKuis2.add(Soal.buatPilihanGanda("SOAL004", "Partikel yang menunjukkan topik kalimat adalah...",
            "Ga", "Wa", "Wo", "Ni", "B", 15));
        soalKuis2.add(Soal.buatPilihanGanda("SOAL005", "Kalimat \"Watashi ___ gakusei desu\" menggunakan partikel...",
            "Ga", "Wa", "De", "Mo", "B", 15));
        soalKuis2.add(Soal.buatEssay("SOAL006",
            "Jelaskan perbedaan penggunaan partikel Wa dan Ga dalam kalimat Bahasa Jepang!",
            "Wa menandai topik kalimat, Ga menandai subjek/informasi baru yang menjadi fokus.", 20));

        Kuis kuis2 = new Kuis("KUIS002", "INS003", "Kuis Partikel Wa dan Ga", "Semua Kelas",
            "Kuis pemahaman tata bahasa dasar mengenai partikel Wa dan Ga.", 15, "08/10/2025", soalKuis2);
        daftarKuis.add(kuis2);

        ArrayList<Soal> soalKuis3 = new ArrayList<>();
        soalKuis3.add(Soal.buatPilihanGanda("SOAL007", "Cara baca kanji 学校 yang benar adalah...",
            "Gakkou", "Gakusei", "Sensei", "Kaisha", "A", 10));
        soalKuis3.add(Soal.buatEssay("SOAL008",
            "Buatlah 1 kalimat sederhana menggunakan kanji 会社 (perusahaan)!",
            "Contoh: Watashi wa kaisha ni ikimasu (Saya pergi ke perusahaan).", 20));

        Kuis kuis3 = new Kuis("KUIS003", "INS002", "Kuis Kanji N1 Bab 1", "Kelas B",
            "Kuis latihan kanji dasar level N1 sesuai materi Bab 1.", 0, "10/10/2025", soalKuis3);
        daftarKuis.add(kuis3);
    }

    /**
     * Mengisi ulang list dari database. Urutan penting karena ada relasi antar objek:
     * program -> admin/instruktur -> peserta -> seleksi & dokumen.
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
