/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataStore;

/**
 *
 * @author arelssi
 */
import Model.*;
import java.util.ArrayList;

public class DataStore {
//    polymorphism
    public static ArrayList<Pengguna> daftarPengguna = new ArrayList<>();
    public static ArrayList<Peserta> daftarPeserta = new ArrayList<>();
    public static ArrayList<Program> daftarProgram = new ArrayList<>();
    public static ArrayList<Seleksi> daftarSeleksi = new ArrayList<>();
    public static ArrayList<Dokumen> daftarDokumen = new ArrayList<>();
    public static ArrayList<Jadwal> daftarJadwal = new ArrayList<>();

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
        Instruktur ins1 = new Instruktur("dian_p", "dian123", "Dian Priatna Kusuma, S.PD.", "INS001", "08123...", "Kaiwa", "N2", "Mandiri");
        Instruktur ins2 = new Instruktur("yamada_s", "yamada123", "Yamada Sensei", "INS002", "08199...", "Kanji", "N1", "Reguler");
        Instruktur ins3 = new Instruktur("farelfwzn", "farel123", "Muhamad Farel Fauzan", "INS003", "08199...", "Budaya JP", "N1", "Reguler");
        daftarPengguna.add(ins1);
        daftarPengguna.add(ins2);
        daftarPengguna.add(ins3);

//        Peserta
        Peserta pst1 = new Peserta("PST001", "3208010205050001", "Azril Algiffari", "2002-05-05", "Laki-laki", "0891234567", "Islam", "Kuningan", prg1.getIdProgram(), ins1, "N4", "Lunas");
        pst1.setStatusSeleksi("Lulus");
        pst1.setStatusKeberangkatan("Siap Berangkat");
        pst1.setKelas("Kelas A");

        Peserta pst2 = new Peserta("PST002", "3208013001060002", "Tania Elliyanti", "2003-01-30", "Perempuan", "0897654321", "Islam", "Kuningan", prg2.getIdProgram(), ins2, "N4", "Lunas");
        pst2.setStatusSeleksi("Lulus");
        pst2.setStatusKeberangkatan("Belum Berangkat");
        pst2.setKelas("Kelas A");

        Peserta pst3 = new Peserta("PST003", "3208012008060003", "Akram Pratama Putra", "2003-08-20", "Laki-laki", "0812233445", "Islam", "Kuningan", prg3.getIdProgram(), ins3, "N3", "Lunas");
        pst3.setStatusSeleksi("Gugur Permanen");
        pst3.setStatusKeberangkatan("Batal Berangkat");
        pst3.setKelas("Kelas A");

        Peserta pst4 = new Peserta("PST004", "3208017049510003", "Muhamad Farel Fauzan", "2003-12-17", "Laki-laki", "0856677889", "Islam", "Kuningan", prg1.getIdProgram(), ins1, "N1", "Lunas");
        pst4.setStatusSeleksi("Lulus");
        pst4.setStatusKeberangkatan("Siap Berangkat");
        pst4.setKelas("Kelas A");
        
        Peserta pst5 = new Peserta("PST005", "3208011234560005", "Nova Shabilla", "2004-02-14", "Perempuan", "0877112233", "Islam", "Kuningan", prg2.getIdProgram(), ins2, "N5", "Belum Lunas");
        pst5.setKelas("Kelas B");
        
//        Dokumen
        pst1.setStatusKeberangkatan("Siap Berangkat");
        pst2.setStatusKeberangkatan("Siap Berangkat");
        pst3.setStatusKeberangkatan("Menunggu Dokumen");
        pst4.setStatusKeberangkatan("Belum Berangkat");

        // Masukkan data ke daftarDokumen
        daftarDokumen.add(new Dokumen("DOK001", pst1, "B1234567", "01/02/2026"));
        daftarDokumen.add(new Dokumen("DOK002", pst2, "C9876543", "01/02/2026"));
        daftarDokumen.add(new Dokumen("DOK003", pst3, "Belum diinput", "—"));
        daftarDokumen.add(new Dokumen("DOK004", pst4, "Belum diinput", "—"));

        daftarPeserta.add(pst1);
        daftarPeserta.add(pst2);
        daftarPeserta.add(pst3);
        daftarPeserta.add(pst4);
        daftarPeserta.add(pst5);
        
//        Seleksi
        daftarSeleksi.add(new Seleksi("SEL-OLD1", pst3, "Seleksi Berkas & Administrasi", "20/09/2025", 70, "Lulus"));

        daftarSeleksi.add(new Seleksi("SEL-OLD2", pst5, "Seleksi Berkas & Administrasi", "15/09/2025", 80, "Lulus"));
        daftarSeleksi.add(new Seleksi("SEL-OLD3", pst5, "Tes Fisik & Kesamaptaan", "01/10/2025", 60, "Tidak Lulus"));


        // --- DATA SELEKSI UTAMA / TERAKHIR (Sesuai Tampilan Tabel Kamu) ---
        // Format: (idSeleksi, objekPeserta, jenisSeleksi, tanggalSeleksi, nilai, statusHasil)
        
        // 1. Azril Algiffari (PST001)
        daftarSeleksi.add(new Seleksi("SEL001", pst1, "Seleksi Bahasa Jepang", "01/10/2025", 85, "Lulus"));
        
        // 2. Tania Elliyanti (PST002)
        daftarSeleksi.add(new Seleksi("SEL002", pst2, "Seleksi Wawancara User", "01/10/2025", 78, "Lulus"));
        
        // 3. Akram Pratama Putra (PST003) - Ini menjadi percobaan ke-2
        daftarSeleksi.add(new Seleksi("SEL003", pst3, "Seleksi Bahasa Jepang", "05/10/2025", 55, "Tidak Lulus"));
        
        // 4. Muhamad Farel Fauzan (PST004)
        daftarSeleksi.add(new Seleksi("SEL004", pst4, "Seleksi Kemampuan Kerja", "10/10/2025", 92, "Lulus"));
        
        // 5. PST005 - Ini menjadi percobaan ke-3 (Gugur)
        daftarSeleksi.add(new Seleksi("SEL005", pst5, "Seleksi Bahasa Jepang", "12/10/2025", 40, "Gugur Permanen"));
        
        daftarJadwal.add(new Jadwal("JDW001", "INS003", "Senin", "08 : 00", "Minna No Nihonggo Bab 1", "A", 120, "Selesai"));
        daftarJadwal.add(new Jadwal("JDW002", "INS003", "Rabu",  "08 : 00", "Minna No Nihonggo Bab 1", "A", 90,  "Hari ini"));
        daftarJadwal.add(new Jadwal("JDW003", "INS003", "Kamis", "08 : 00", "Minna No Nihonggo Bab 1", "A", 120, "Belum Dimulai"));
        
        // Contoh jadwal untuk instruktur lain (Tidak akan muncul di dashboard INS001)
        daftarJadwal.add(new Jadwal("JDW004", "INS002", "Selasa", "10 : 00", "Percakapan Bisnis (Keigo)", "B", 120, "Hari ini"));
    }
        
        
    // Fungsi khusus untuk mengambil HANYA instruktur (digunakan untuk ComboBox di form Tambah Peserta)
    public static ArrayList<Instruktur> getHanyaInstruktur() {
        ArrayList<Instruktur> listInstruktur = new ArrayList<>();
        for (Pengguna p : daftarPengguna) {
            // Cek apakah objek pengguna ini adalah Instruktur
            if (p instanceof Instruktur) {
                listInstruktur.add((Instruktur) p);
            }
        }
        return listInstruktur;
    }
}