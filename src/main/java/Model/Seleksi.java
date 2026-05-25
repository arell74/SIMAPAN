/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author arelssi
 */
public class Seleksi {
    private String idSeleksi;
    private Peserta peserta; 
    private String jenisSeleksi; 
    private String tanggalSeleksi;
    private int nilai;
    private String statusHasil;  

    public Seleksi(String idSeleksi, Peserta peserta, String jenisSeleksi, String tanggalSeleksi, int nilai, String statusHasil) {
        this.idSeleksi = idSeleksi;
        this.peserta = peserta;
        this.jenisSeleksi = jenisSeleksi;
        this.tanggalSeleksi = tanggalSeleksi;
        this.nilai = nilai;
        this.statusHasil = statusHasil;
    }

    public String getIdSeleksi() {
        return idSeleksi;
    }

    public Peserta getPeserta() {
        return peserta;
    }
    

    public String getJenisSeleksi() {
        return jenisSeleksi;
    }

    public void setJenisSeleksi(String jenisSeleksi) {
        this.jenisSeleksi = jenisSeleksi;
    }

    public String getTanggalSeleksi() {
        return tanggalSeleksi;
    }

    public void setTanggalSeleksi(String tanggalSeleksi) {
        this.tanggalSeleksi = tanggalSeleksi;
    }

    public int getNilai() {
        return nilai;
    }

    public void setNilai(int nilai) {
        this.nilai = nilai;
    }

    public String getStatusHasil() {
        return statusHasil;
    }

    public void setStatusHasil(String statusHasil) {
        this.statusHasil = statusHasil;
    }

}