/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author arelssi
 */
public class Jadwal {
    private String idJadwal;
    private String idInstruktur; 
    private String hari;
    private String jamMulai;
    private String namaMateri;
    private String namaKelas;
    private int durasi;
    private String statusJadwal;

    // --- CONSTRUCTOR ---
    public Jadwal(String idJadwal, String idInstruktur, String hari, String jamMulai, 
                  String namaMateri, String namaKelas, int durasi, String statusJadwal) {
        this.idJadwal = idJadwal;
        this.idInstruktur = idInstruktur;
        this.hari = hari;
        this.jamMulai = jamMulai;
        this.namaMateri = namaMateri;
        this.namaKelas = namaKelas;
        this.durasi = durasi;
        this.statusJadwal = statusJadwal;
    }

    // --- GETTER & SETTER ---
    public String getIdJadwal() { return idJadwal; }
    public void setIdJadwal(String idJadwal) { this.idJadwal = idJadwal; }

    public String getIdInstruktur() { return idInstruktur; }
    public void setIdInstruktur(String idInstruktur) { this.idInstruktur = idInstruktur; }

    public String getHari() { return hari; }
    public void setHari(String hari) { this.hari = hari; }

    public String getJamMulai() { return jamMulai; }
    public void setJamMulai(String jamMulai) { this.jamMulai = jamMulai; }

    public String getNamaMateri() { return namaMateri; }
    public void setNamaMateri(String namaMateri) { this.namaMateri = namaMateri; }

    public String getNamaKelas() { return namaKelas; }
    public void setNamaKelas(String namaKelas) { this.namaKelas = namaKelas; }

    public int getDurasi() { return durasi; }
    public void setDurasi(int durasi) { this.durasi = durasi; }

    public String getStatusJadwal() { return statusJadwal; }
    public void setStatusJadwal(String statusJadwal) { this.statusJadwal = statusJadwal; }
}
