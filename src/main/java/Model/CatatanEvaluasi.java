/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 * @author arelssi
 */
public class CatatanEvaluasi {

    private String idCatatan;
    private Peserta peserta;
    private String idInstruktur;
    private String tanggal;
    private String isiCatatan;

    public CatatanEvaluasi(String idCatatan, Peserta peserta, String idInstruktur, String tanggal, String isiCatatan) {
        this.idCatatan = idCatatan;
        this.peserta = peserta;
        this.idInstruktur = idInstruktur;
        this.tanggal = tanggal;
        this.isiCatatan = isiCatatan;
    }

    public String getIdCatatan() {
        return idCatatan;
    }

    public void setIdCatatan(String idCatatan) {
        this.idCatatan = idCatatan;
    }

    public Peserta getPeserta() {
        return peserta;
    }

    public void setPeserta(Peserta peserta) {
        this.peserta = peserta;
    }

    public String getIdInstruktur() {
        return idInstruktur;
    }

    public void setIdInstruktur(String idInstruktur) {
        this.idInstruktur = idInstruktur;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getIsiCatatan() {
        return isiCatatan;
    }

    public void setIsiCatatan(String isiCatatan) {
        this.isiCatatan = isiCatatan;
    }
}