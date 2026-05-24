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
    private Peserta peserta; // RELASI OBJEK: Objek Seleksi menyimpan Objek Peserta utuh
    private String jenisSeleksi; // Contoh: "Wawancara", "Ujian Tulis", "Tes Fisik"
    private String statusHasil;  // Contoh: "Lulus", "Gugur Permanen", "Menunggu"

    public Seleksi(String idSeleksi, Peserta peserta, String jenisSeleksi, String statusHasil) {
        this.idSeleksi = idSeleksi;
        this.peserta = peserta;
        this.jenisSeleksi = jenisSeleksi;
        this.statusHasil = statusHasil;
    }

    public String getIdSeleksi() {
        return idSeleksi;
    }

    public void setIdSeleksi(String idSeleksi) {
        this.idSeleksi = idSeleksi;
    }

    public Peserta getPeserta() {
        return peserta;
    }

    public void setPeserta(Peserta peserta) {
        this.peserta = peserta;
    }

    public String getJenisSeleksi() {
        return jenisSeleksi;
    }

    public void setJenisSeleksi(String jenisSeleksi) {
        this.jenisSeleksi = jenisSeleksi;
    }

    public String getStatusHasil() {
        return statusHasil;
    }

    public void setStatusHasil(String statusHasil) {
        this.statusHasil = statusHasil;
    }

    
}