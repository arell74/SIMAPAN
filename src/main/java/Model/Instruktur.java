/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author arelssi
 */
public class Instruktur extends Pengguna {
    private String idInstruktur;
    private String noTelp;
    private String spesialisasi;
    private String levelJlpt;
    private String kelasDiampu;

    // Konstruktor super() memanggil milik Pengguna
    public Instruktur(String username, String password, String namaLengkap, 
                      String idInstruktur, String noTelp, String spesialisasi, 
                      String levelJlpt, String kelasDiampu) {
        super(username, password, namaLengkap, "INSTRUKTUR");
        this.idInstruktur = idInstruktur;
        this.noTelp = noTelp;
        this.spesialisasi = spesialisasi;
        this.levelJlpt = levelJlpt;
        this.kelasDiampu = kelasDiampu;
    }

    // Getter untuk atribut spesifik Instruktur
    public String getIdInstruktur() { return idInstruktur; }
    public String getSpesialisasi() { return spesialisasi; }
    public String getLevelJlpt() { return levelJlpt; }

    public String getNoTelp() {
        return noTelp;
    }

    public String getKelasDiampu() {
        return kelasDiampu;
    }
    

    @Override
    public String getMenuOtoritas() {
        return "Akses Terbatas: Input Nilai dan Evaluasi Peserta Magang.";
    }

    // Override toString agar saat dimasukkan ke JComboBox, yang muncul adalah namanya
    @Override
    public String toString() {
        return getNamaLengkap() + " (" + levelJlpt + ")";
    }
}