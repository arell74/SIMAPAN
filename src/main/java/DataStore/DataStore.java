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
    // 1 List ini menampung Admin dan Instruktur sekaligus (Polymorphism)
    public static ArrayList<Pengguna> daftarPengguna = new ArrayList<>();
    public static ArrayList<Peserta> daftarPeserta = new ArrayList<>();
    public static ArrayList<Program> daftarProgram = new ArrayList<>();

    static {
        // --- Data Admin ---
        daftarPengguna.add(new AdminLPK("admin", "admin123", "Farel (Admin LPK)"));

        // --- Data Instruktur (Sekarang bisa login!) ---
        Instruktur ins1 = new Instruktur("dian_p", "dian123", "Dian Priatna Kusuma, S.PD.", "INS001", "08123...", "Kaiwa", "N2", "Mandiri");
        Instruktur ins2 = new Instruktur("yamada_s", "yamada123", "Yamada Sensei", "INS002", "08199...", "Kanji", "N1", "Reguler");
        daftarPengguna.add(ins1);
        daftarPengguna.add(ins2);

        // --- Data Peserta (Tetap sama seperti sebelumnya) ---
        // (contoh 1 data saja agar singkat)
        daftarPeserta.add(new Peserta("PST001", "3208010...", "Azril Algiffari", "2002-05-05", "Laki-laki", "089...", "Islam", "Kuningan", "PRG001", ins1, "N4", "Lunas"));
        
        daftarProgram.add(new Program("PRG001", "Pemagangan Manufaktur", "Pabrik / Manufaktur", 15000000, 30));
        daftarProgram.add(new Program("PRG002", "Pemagangan Caregiver", "Kesehatan / Lansia", 12000000, 20));
        daftarProgram.add(new Program("PRG003", "Pemagangan Pertanian", "Agrikultur", 10000000, 50));
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