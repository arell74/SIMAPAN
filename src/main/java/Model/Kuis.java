/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.ArrayList;

/**
 * Merepresentasikan satu Kuis yang dibuat oleh Instruktur, berisi kumpulan
 * Soal (Pilihan Ganda dan/atau Essay) yang nantinya dikerjakan oleh Peserta.
 *
 * @author arelssi
 */
public class Kuis {
    private String idKuis;
    private String idInstruktur;
    private String judulKuis;
    private String kelasTarget;
    private String deskripsi;
    private int batasWaktuMenit;
    private String tanggalDibuat;
    private ArrayList<Soal> daftarSoal;

    public Kuis(String idKuis, String idInstruktur, String judulKuis, String kelasTarget,
                String deskripsi, int batasWaktuMenit, String tanggalDibuat, ArrayList<Soal> daftarSoal) {
        this.idKuis = idKuis;
        this.idInstruktur = idInstruktur;
        this.judulKuis = judulKuis;
        this.kelasTarget = kelasTarget;
        this.deskripsi = deskripsi;
        this.batasWaktuMenit = batasWaktuMenit;
        this.tanggalDibuat = tanggalDibuat;
        this.daftarSoal = (daftarSoal != null) ? daftarSoal : new ArrayList<>();
    }

    public String getIdKuis() { return idKuis; }
    public String getIdInstruktur() { return idInstruktur; }
    public String getJudulKuis() { return judulKuis; }
    public String getKelasTarget() { return kelasTarget; }
    public String getDeskripsi() { return deskripsi; }
    public int getBatasWaktuMenit() { return batasWaktuMenit; }
    public String getTanggalDibuat() { return tanggalDibuat; }
    public ArrayList<Soal> getDaftarSoal() { return daftarSoal; }

    public void setJudulKuis(String judulKuis) { this.judulKuis = judulKuis; }
    public void setKelasTarget(String kelasTarget) { this.kelasTarget = kelasTarget; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public void setBatasWaktuMenit(int batasWaktuMenit) { this.batasWaktuMenit = batasWaktuMenit; }

    public void tambahSoal(Soal soal) {
        this.daftarSoal.add(soal);
    }

    public void hapusSoal(Soal soal) {
        this.daftarSoal.remove(soal);
    }

    public int getJumlahSoal() {
        return daftarSoal.size();
    }

    public int getTotalPoin() {
        int total = 0;
        for (Soal s : daftarSoal) {
            total += s.getPoin();
        }
        return total;
    }
}