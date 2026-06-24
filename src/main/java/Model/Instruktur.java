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

    public Instruktur(String username, String password, String namaLengkap, 
                      String idInstruktur, String noTelp, String spesialisasi, 
                      String levelJlpt, String kelasDiampu) {
        super(username, idInstruktur, namaLengkap, "INSTRUKTUR");
        this.idInstruktur = idInstruktur;
        this.noTelp = noTelp;
        this.spesialisasi = spesialisasi;
        this.levelJlpt = levelJlpt;
        this.kelasDiampu = kelasDiampu;
    }

    public String getIdInstruktur() { return idInstruktur; }
    public String getSpesialisasi() { return spesialisasi; }
    public String getLevelJlpt() { return levelJlpt; }
    
    public String getNoTelp() {
        return noTelp;
    }

    public void setIdInstruktur(String idInstruktur) {
        this.idInstruktur = idInstruktur;
    }
   

    public String getKelasDiampu() {
        return kelasDiampu;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public void setSpesialisasi(String spesialisasi) {
        this.spesialisasi = spesialisasi;
    }

    public void setLevelJlpt(String levelJlpt) {
        this.levelJlpt = levelJlpt;
    }

    public void setKelasDiampu(String kelasDiampu) {
        this.kelasDiampu = kelasDiampu;
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