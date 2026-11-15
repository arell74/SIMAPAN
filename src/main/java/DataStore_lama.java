/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package DataStore;

/**
 *
 * @author arelssi
 */
import Model.*;
import java.util.ArrayList;

public class DataStore_lama {
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
//        Program
        Program prg1 = new Program("PRG001", "Pemagangan Manufaktur", "Pabrik / Perakitan", 15000000, 30);
        Program prg2 = new Program("PRG002", "Pemagangan Caregiver", "Kesehatan / Lansia", 12000000, 20);
        Program prg3 = new Program("PRG003", "Pemagangan Pertanian", "Agrikultur", 10000000, 50);
        
        daftarProgram.add(prg1);
        daftarProgram.add(prg2);
        daftarProgram.add(prg3);
        
//        Admin
        daftarPengguna.add(new AdminLPK("admin", "admin123", "Farel (Admin LPK)"));

//        Instruktur
        Instruktur ins1 = new Instruktur("dian_p", "dian123", "Dian Priatna Kusuma, S.PD.", "INS001", "08123999748", "Kaiwa", "N2", "Mandiri");
        Instruktur ins2 = new Instruktur("yamada_s", "yamada123", "Yamada Sensei", "INS002", "08199999477", "Kanji", "N1", "Reguler");
        Instruktur ins3 = new Instruktur("zani", "zani123", "Utsukushii Zani", "INS003", "0819900004", "Budaya JP", "N1", "Reguler");
        Instruktur ins4 = new Instruktur("supandi", "pandi123", "Supandi S.S", "INS004", "081994949", "Budaya JP", "N1", "Reguler");
        
        daftarPengguna.add(ins1);
        daftarPengguna.add(ins2);
        daftarPengguna.add(ins3);
        daftarPengguna.add(ins4);

//        Peserta
        Peserta pst1 = new Peserta("3208010205050001", "yuukiPST001", "PST001", "3208010205050001", "Azril Algiffari", "2002-05-05", "Laki-laki", "0891234567", "Islam", "Kuningan", prg1.getIdProgram(), ins1, "N4", "Lunas");
        pst1.setStatusSeleksi("Lulus");
        pst1.setStatusKeberangkatan("Siap Berangkat");
        pst1.setKelas("Kelas A");

        Peserta pst2 = new Peserta("tania","tania123","PST002", "3208013001060002", "Tania Elliyanti", "2003-01-30", "Perempuan", "0897654321", "Islam", "Kuningan", prg2.getIdProgram(), ins2, "N4", "Lunas");
        pst2.setStatusSeleksi("Lulus");
        pst2.setStatusKeberangkatan("Belum Berangkat");
        pst2.setKelas("Kelas A");

        Peserta pst3 = new Peserta("akram","akram123", "PST003", "3208012008060003", "Akram Pratama Putra", "2003-08-20", "Laki-laki", "0812233445", "Islam", "Kuningan", prg3.getIdProgram(), ins3, "N3", "Lunas");
        pst3.setStatusSeleksi("Gugur Permanen");
        pst3.setStatusKeberangkatan("Batal Berangkat");
        pst3.setKelas("Kelas A");

        Peserta pst4 = new Peserta("PST004","PST004", "PST004", "3208017049510003", "Muhamad Farel Fauzan", "2003-12-17", "Laki-laki", "0856677889", "Islam", "Kuningan", prg1.getIdProgram(), ins1, "N1", "Lunas");
        pst4.setStatusSeleksi("Lulus");
        pst4.setStatusKeberangkatan("Siap Berangkat");
        pst4.setKelas("Kelas A");
        
        Peserta pst5 = new Peserta("novashabilla","180806","PST005", "3208011234560005", "Nova Shabilla", "2004-02-14", "Perempuan", "0877112233", "Islam", "Kuningan", prg2.getIdProgram(), ins3, "N5", "Belum Lunas");
        pst5.setKelas("Kelas B");
        
        Peserta pst6 = new Peserta("un","pas", "PST006", "3208011102040001", "Mikasa", "2004-02-11", "Laki-laki", "081122334455", "Islam", "Kuningan", prg1.getIdProgram(), ins3, "N5", "Lunas");
        pst6.setStatusSeleksi("Lulus");
        pst6.setStatusKeberangkatan("Menunggu Dokumen");
        pst6.setKelas("Kelas A");

        Peserta pst7 = new Peserta("un","pas","PST007", "3208012203050002", "Faida Nur Mufidah", "2005-03-22", "Perempuan", "082233445566", "Islam", "Kuningan", prg2.getIdProgram(), ins3, "N4", "Belum Lunas");
        pst7.setStatusSeleksi("Menunggu");
        pst7.setStatusKeberangkatan("Belum Berangkat");
        pst7.setKelas("Kelas A");

        Peserta pst8 = new Peserta("un","pas","PST008", "3208011506030003", "Rizky Aditya", "2003-06-15", "Laki-laki", "083344556677", "Islam", "Cirebon", prg1.getIdProgram(), ins3, "N4", "Lunas");
        pst8.setStatusSeleksi("Lulus");
        pst8.setStatusKeberangkatan("Siap Berangkat");
        pst8.setKelas("Kelas B");

        Peserta pst9 = new Peserta("un","pas","PST009", "3208010809040004", "Dewi Lestari", "2004-09-08", "Perempuan", "084455667788", "Islam", "Kuningan", prg3.getIdProgram(), ins3, "N3", "Lunas");
        pst9.setStatusSeleksi("Lulus");
        pst9.setStatusKeberangkatan("Siap Berangkat");
        pst9.setKelas("Kelas B");

        Peserta pst10 = new Peserta("un","pas","PST010", "3208012512020005", "Ahmad Fauzi", "2002-12-25", "Laki-laki", "085566778899", "Islam", "Kuningan", prg1.getIdProgram(), ins3, "N5", "Belum Lunas");
        pst10.setStatusSeleksi("Gugur Permanen");
        pst10.setStatusKeberangkatan("Batal Berangkat");
        pst10.setKelas("Kelas A");

        Peserta pst11 = new Peserta("un","pas","PST011", "3208011402050006", "Rina Melati", "2005-02-14", "Perempuan", "086677889900", "Islam", "Kuningan", prg2.getIdProgram(), ins3, "N4", "Lulus");
        pst11.setStatusSeleksi("Lulus");
        pst11.setStatusKeberangkatan("Menunggu Dokumen");
        pst11.setKelas("Kelas A");

        Peserta pst12 = new Peserta("un","pas","PST012", "3208010307040007", "Dimas Anggara", "2004-07-03", "Laki-laki", "087788990011", "Islam", "Majalengka", prg1.getIdProgram(), ins3, "N5", "Belum Lunas");
        pst12.setStatusSeleksi("Menunggu");
        pst12.setStatusKeberangkatan("Belum Berangkat");
        pst12.setKelas("Kelas B");

        Peserta pst13 = new Peserta("un","pas","PST013", "3208012810050008", "Putri Maharani", "2005-10-28", "Perempuan", "088899001122", "Islam", "Kuningan", prg3.getIdProgram(), ins3, "N3", "Lunas");
        pst13.setStatusSeleksi("Lulus");
        pst13.setStatusKeberangkatan("Siap Berangkat");
        pst13.setKelas("Kelas B");

        Peserta pst14 = new Peserta("un","pas","PST014", "3208011911030009", "Gilang Ramadhan", "2003-11-19", "Laki-laki", "089900112233", "Islam", "Kuningan", prg2.getIdProgram(), ins3, "N4", "Lulus");
        pst14.setStatusSeleksi("Lulus");
        pst14.setStatusKeberangkatan("Belum Berangkat");
        pst14.setKelas("Kelas A");

        Peserta pst15 = new Peserta("un","pas","PST015", "3208010701040010", "Ayu Wandira", "2004-01-07", "Perempuan", "081011223344", "Islam", "Cirebon", prg1.getIdProgram(), ins3, "N5", "Lulus");
        pst15.setStatusSeleksi("Lulus");
        pst15.setStatusKeberangkatan("Menunggu Dokumen");
        pst15.setKelas("Kelas B");
        
//        Dokumen
        pst1.setStatusKeberangkatan("Siap Berangkat");
        pst2.setStatusKeberangkatan("Siap Berangkat");
        pst3.setStatusKeberangkatan("Menunggu Dokumen");
        pst4.setStatusKeberangkatan("Belum Berangkat");

        Dokumen dok1 = new Dokumen("DOK001", pst1, "01/02/2026");
        dok1.setPaspor("B1234567", "berkas_b1234567.pdf");
        dok1.setKtp("3208123456780001", "ktp_azril.jpg"); // Tambahan dummy agar checklist KTP hijau
        daftarDokumen.add(dok1);

        Dokumen dok2 = new Dokumen("DOK002", pst2, "01/02/2026");
        dok2.setPaspor("C9876543", "berkas_c9876543.pdf");
        daftarDokumen.add(dok2);

        Dokumen dok3 = new Dokumen("DOK003", pst3, "—");
        daftarDokumen.add(dok3);

        Dokumen dok4 = new Dokumen("DOK004", pst4, "—");
        daftarDokumen.add(dok4);

        daftarPeserta.add(pst1);
        daftarPeserta.add(pst2);
        daftarPeserta.add(pst3);
        daftarPeserta.add(pst4);
        daftarPeserta.add(pst5);
        daftarPeserta.add(pst6);
        daftarPeserta.add(pst7);
        daftarPeserta.add(pst8);
        daftarPeserta.add(pst9);
        daftarPeserta.add(pst10);
        daftarPeserta.add(pst11);
        daftarPeserta.add(pst12);
        daftarPeserta.add(pst13);
        daftarPeserta.add(pst14);
        daftarPeserta.add(pst15);
        daftarPengguna.addAll(daftarPeserta);
        
//        Seleksi
        daftarSeleksi.add(new Seleksi("SEL-OLD1", pst3, "Seleksi Berkas & Administrasi", "20/09/2025", 70, "Lulus"));

        daftarSeleksi.add(new Seleksi("SEL-OLD2", pst5, "Seleksi Berkas & Administrasi", "15/09/2025", 80, "Lulus"));
        daftarSeleksi.add(new Seleksi("SEL-OLD3", pst5, "Tes Fisik & Kesamaptaan", "01/10/2025", 60, "Tidak Lulus"));


        daftarSeleksi.add(new Seleksi("SEL001", pst1, "Seleksi Bahasa Jepang", "01/10/2025", 85, "Lulus"));
        daftarSeleksi.add(new Seleksi("SEL002", pst2, "Seleksi Wawancara User", "01/10/2025", 78, "Lulus"));
        daftarSeleksi.add(new Seleksi("SEL003", pst3, "Seleksi Bahasa Jepang", "05/10/2025", 55, "Tidak Lulus"));
        daftarSeleksi.add(new Seleksi("SEL004", pst4, "Seleksi Kemampuan Kerja", "10/10/2025", 92, "Lulus"));
        
        // 5. PST005 - Ini menjadi percobaan ke-3 (Gugur)
        daftarSeleksi.add(new Seleksi("SEL005", pst5, "Seleksi Bahasa Jepang", "12/10/2025", 40, "Gugur Permanen"));
        
        daftarJadwal.add(new Jadwal("JDW001", "INS003", "Senin", "08 : 00", "Minna No Nihonggo Bab 1", "A", 120, "Selesai"));
        daftarJadwal.add(new Jadwal("JDW002", "INS003", "Rabu",  "08 : 00", "Minna No Nihonggo Bab 1", "A", 90,  "Hari ini"));
        daftarJadwal.add(new Jadwal("JDW003", "INS003", "Kamis", "08 : 00", "Minna No Nihonggo Bab 1", "A", 120, "Belum Dimulai"));
        
        // Contoh jadwal untuk instruktur lain (Tidak akan muncul di dashboard INS001)
        daftarJadwal.add(new Jadwal("JDW004", "INS002", "Selasa", "10 : 00", "Percakapan Bisnis (Keigo)", "B", 120, "Hari ini"));

//        =========================== Materi (dummy) ===========================
        Materi mat1 = new Materi("MAT001", ins1.getIdInstruktur(), "Pengenalan Huruf Hiragana",
            "Kelas A", "Teks",
            "Hiragana adalah salah satu huruf dasar dalam Bahasa Jepang yang digunakan untuk menulis kata-kata "
            + "asli Jepang. Terdapat 46 huruf dasar yang dimulai dari deret vokal a-i-u-e-o, lalu diikuti "
            + "konsonan ka-ki-ku-ke-ko, sa-shi-su-se-so, dan seterusnya. Pelajari dan hafalkan urutan ini "
            + "sebelum lanjut ke materi berikutnya.",
            null, null, "01/10/2025");
        daftarMateri.add(mat1);

        Materi mat2 = new Materi("MAT002", ins1.getIdInstruktur(), "Kosakata Sehari-hari (Aisatsu)",
            "Kelas A", "Teks",
            "Aisatsu (salam) yang wajib dihafal: Ohayou Gozaimasu (Selamat pagi), Konnichiwa (Selamat siang), "
            + "Konbanwa (Selamat malam), Arigatou Gozaimasu (Terima kasih), Sumimasen (Permisi/maaf), "
            + "Itadakimasu (Selamat makan), dan Otsukaresama Desu (Terima kasih atas kerja kerasnya).",
            null, null, "03/10/2025");
        daftarMateri.add(mat2);

        Materi mat3 = new Materi("MAT003", ins2.getIdInstruktur(), "Latihan Kanji N1 - Bab 1",
            "Kelas B", "File",
            null, "kanji_bab1.pdf", "MAT003.pdf", "05/10/2025");
        daftarMateri.add(mat3);

        Materi mat4 = new Materi("MAT004", ins3.getIdInstruktur(), "Tata Bahasa Dasar - Partikel Wa dan Ga",
            "Semua Kelas", "Teks",
            "Partikel 'Wa' (ditulis dengan huruf Ha) digunakan untuk menandai topik kalimat, sedangkan partikel "
            + "'Ga' digunakan untuk menandai subjek yang menjadi fokus/informasi baru. Contoh: 'Watashi wa "
            + "gakusei desu' (Saya adalah siswa) menekankan topik 'saya', sementara 'Dare ga kimasu ka?' "
            + "(Siapa yang datang?) menekankan subjek yang ditanyakan.",
            null, null, "07/10/2025");
        daftarMateri.add(mat4);

        Materi mat5 = new Materi("MAT005", ins1.getIdInstruktur(), "Video Latihan Percakapan Restoran",
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

        Kuis kuis1 = new Kuis("KUIS001", ins1.getIdInstruktur(), "Kuis Hiragana Dasar", "Kelas A",
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

        Kuis kuis2 = new Kuis("KUIS002", ins3.getIdInstruktur(), "Kuis Partikel Wa dan Ga", "Semua Kelas",
            "Kuis pemahaman tata bahasa dasar mengenai partikel Wa dan Ga.", 15, "08/10/2025", soalKuis2);
        daftarKuis.add(kuis2);

        ArrayList<Soal> soalKuis3 = new ArrayList<>();
        soalKuis3.add(Soal.buatPilihanGanda("SOAL007", "Cara baca kanji 学校 yang benar adalah...",
            "Gakkou", "Gakusei", "Sensei", "Kaisha", "A", 10));
        soalKuis3.add(Soal.buatEssay("SOAL008",
            "Buatlah 1 kalimat sederhana menggunakan kanji 会社 (perusahaan)!",
            "Contoh: Watashi wa kaisha ni ikimasu (Saya pergi ke perusahaan).", 20));

        Kuis kuis3 = new Kuis("KUIS003", ins2.getIdInstruktur(), "Kuis Kanji N1 Bab 1", "Kelas B",
            "Kuis latihan kanji dasar level N1 sesuai materi Bab 1.", 0, "10/10/2025", soalKuis3);
        daftarKuis.add(kuis3);
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
}