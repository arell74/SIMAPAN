/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/*
 * @author arelssi
 */
public class Soal {
    public static final String TIPE_PILIHAN_GANDA = "Pilihan Ganda";
    public static final String TIPE_ESSAY = "Essay";

    private String idSoal;
    private String tipeSoal;
    private String pertanyaan;
    private String pilihanA;
    private String pilihanB;
    private String pilihanC;
    private String pilihanD;
    private String kunciJawaban;
    private int poin;

    public Soal(String idSoal, String tipeSoal, String pertanyaan, String pilihanA, String pilihanB,
                String pilihanC, String pilihanD, String kunciJawaban, int poin) {
        this.idSoal = idSoal;
        this.tipeSoal = tipeSoal;
        this.pertanyaan = pertanyaan;
        this.pilihanA = pilihanA;
        this.pilihanB = pilihanB;
        this.pilihanC = pilihanC;
        this.pilihanD = pilihanD;
        this.kunciJawaban = kunciJawaban;
        this.poin = poin;
    }

    /** Factory method untuk membuat soal bertipe Pilihan Ganda. */
    public static Soal buatPilihanGanda(String idSoal, String pertanyaan, String pilihanA,
                                         String pilihanB, String pilihanC, String pilihanD,
                                         String kunciJawaban, int poin) {
        return new Soal(idSoal, TIPE_PILIHAN_GANDA, pertanyaan, pilihanA, pilihanB, pilihanC,
                pilihanD, kunciJawaban, poin);
    }

    /** Factory method untuk membuat soal bertipe Essay. kunciReferensi boleh null/kosong. */
    public static Soal buatEssay(String idSoal, String pertanyaan, String kunciReferensi, int poin) {
        return new Soal(idSoal, TIPE_ESSAY, pertanyaan, null, null, null, null, kunciReferensi, poin);
    }

    public String getIdSoal() { return idSoal; }
    public String getTipeSoal() { return tipeSoal; }
    public String getPertanyaan() { return pertanyaan; }
    public String getPilihanA() { return pilihanA; }
    public String getPilihanB() { return pilihanB; }
    public String getPilihanC() { return pilihanC; }
    public String getPilihanD() { return pilihanD; }
    public String getKunciJawaban() { return kunciJawaban; }
    public int getPoin() { return poin; }

    public void setPertanyaan(String pertanyaan) { this.pertanyaan = pertanyaan; }
    public void setKunciJawaban(String kunciJawaban) { this.kunciJawaban = kunciJawaban; }
    public void setPoin(int poin) { this.poin = poin; }

    public boolean isPilihanGanda() {
        return TIPE_PILIHAN_GANDA.equals(tipeSoal);
    }

    /** Cuplikan pertanyaan singkat, untuk ditampilkan di tabel. */
    public String getCuplikanPertanyaan() {
        if (pertanyaan == null || pertanyaan.isEmpty()) return "-";
        return pertanyaan.length() > 60 ? pertanyaan.substring(0, 60) + "..." : pertanyaan;
    }
}