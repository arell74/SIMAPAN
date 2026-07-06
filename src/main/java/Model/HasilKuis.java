/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.ArrayList;

/**
 * Menyimpan hasil pengerjaan satu Kuis oleh satu Peserta: seluruh jawabannya,
 * skor yang sudah otomatis terhitung (dari soal Pilihan Ganda), serta status
 * dan catatan evaluasi dari Instruktur (untuk soal Essay yang perlu dinilai manual).
 *
 * @author arelssi
 */
public class HasilKuis {
    public static final String STATUS_MENUNGGU_PENILAIAN = "Menunggu Penilaian Essay";
    public static final String STATUS_SELESAI_DINILAI = "Selesai Dinilai";

    private String idHasil;
    private String idKuis;
    private String judulKuis;      // snapshot judul, biar tetap tampil walau Kuis aslinya dihapus
    private String idPeserta;
    private String tanggalPengerjaan;
    private ArrayList<JawabanSoal> daftarJawaban;
    private String catatanInstruktur; // Feedback/evaluasi dari instruktur, diisi belakangan (boleh null)

    public HasilKuis(String idHasil, String idKuis, String judulKuis, String idPeserta,
                      String tanggalPengerjaan, ArrayList<JawabanSoal> daftarJawaban) {
        this.idHasil = idHasil;
        this.idKuis = idKuis;
        this.judulKuis = judulKuis;
        this.idPeserta = idPeserta;
        this.tanggalPengerjaan = tanggalPengerjaan;
        this.daftarJawaban = (daftarJawaban != null) ? daftarJawaban : new ArrayList<>();
        this.catatanInstruktur = null;
    }

    public String getIdHasil() { return idHasil; }
    public String getIdKuis() { return idKuis; }
    public String getJudulKuis() { return judulKuis; }
    public String getIdPeserta() { return idPeserta; }
    public String getTanggalPengerjaan() { return tanggalPengerjaan; }
    public ArrayList<JawabanSoal> getDaftarJawaban() { return daftarJawaban; }
    public String getCatatanInstruktur() { return catatanInstruktur; }

    public void setCatatanInstruktur(String catatanInstruktur) {
        this.catatanInstruktur = catatanInstruktur;
    }

    public int getTotalPoinMaksimal() {
        int total = 0;
        for (JawabanSoal j : daftarJawaban) {
            total += j.getPoinMaksimal();
        }
        return total;
    }

    public int getTotalPoinDiperoleh() {
        int total = 0;
        for (JawabanSoal j : daftarJawaban) {
            total += j.getPoinDiperoleh();
        }
        return total;
    }

    /** Nilai akhir dalam skala 0-100, berdasarkan poin yang SUDAH diketahui saat ini. */
    public int getNilaiAkhir() {
        int maksimal = getTotalPoinMaksimal();
        if (maksimal == 0) return 0;
        return Math.round((getTotalPoinDiperoleh() * 100.0f) / maksimal);
    }

    public int getJumlahEssayBelumDinilai() {
        int jumlah = 0;
        for (JawabanSoal j : daftarJawaban) {
            if (!j.isPilihanGanda() && !j.isSudahDinilai()) {
                jumlah++;
            }
        }
        return jumlah;
    }

    /** Status keseluruhan hasil kuis ini: masih ada essay yang belum dinilai instruktur, atau sudah selesai semua. */
    public String getStatus() {
        return getJumlahEssayBelumDinilai() > 0 ? STATUS_MENUNGGU_PENILAIAN : STATUS_SELESAI_DINILAI;
    }
}