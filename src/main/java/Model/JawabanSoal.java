/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 * Menyimpan jawaban seorang Peserta untuk SATU butir Soal di dalam sebuah Kuis.
 * Datanya "snapshot" (disalin) dari Soal aslinya saat kuis dikerjakan, supaya
 * kalau soal aslinya diedit/dihapus nanti, riwayat jawaban siswa tidak ikut rusak.
 *
 * @author arelssi
 */
public class JawabanSoal {
    private String idSoal;
    private String tipeSoal;          // "Pilihan Ganda" atau "Essay"
    private String pertanyaan;
    private String jawabanSiswa;      // "A"/"B"/"C"/"D" untuk PG, atau teks bebas untuk Essay
    private String kunciJawaban;      // Disalin dari Soal, sebagai referensi/pembanding
    private int poinMaksimal;
    private int poinDiperoleh;
    private boolean sudahDinilai;      // true untuk PG (otomatis), false untuk Essay sampai dinilai instruktur

    public JawabanSoal(String idSoal, String tipeSoal, String pertanyaan, String jawabanSiswa,
                        String kunciJawaban, int poinMaksimal, int poinDiperoleh, boolean sudahDinilai) {
        this.idSoal = idSoal;
        this.tipeSoal = tipeSoal;
        this.pertanyaan = pertanyaan;
        this.jawabanSiswa = jawabanSiswa;
        this.kunciJawaban = kunciJawaban;
        this.poinMaksimal = poinMaksimal;
        this.poinDiperoleh = poinDiperoleh;
        this.sudahDinilai = sudahDinilai;
    }

    public String getIdSoal() { return idSoal; }
    public String getTipeSoal() { return tipeSoal; }
    public String getPertanyaan() { return pertanyaan; }
    public String getJawabanSiswa() { return jawabanSiswa; }
    public String getKunciJawaban() { return kunciJawaban; }
    public int getPoinMaksimal() { return poinMaksimal; }
    public int getPoinDiperoleh() { return poinDiperoleh; }
    public boolean isSudahDinilai() { return sudahDinilai; }

    public void setPoinDiperoleh(int poinDiperoleh) { this.poinDiperoleh = poinDiperoleh; }
    public void setSudahDinilai(boolean sudahDinilai) { this.sudahDinilai = sudahDinilai; }

    public boolean isPilihanGanda() {
        return Soal.TIPE_PILIHAN_GANDA.equals(tipeSoal);
    }
}