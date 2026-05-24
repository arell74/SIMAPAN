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
    private String idPeserta;
    private int nilai;
    private int percobaan;
    private String status;

    public Seleksi(String idPeserta, int nilai, int percobaan, String status) {
        this.idPeserta = idPeserta;
        this.nilai = nilai;
        this.percobaan = percobaan;
        this.status = status;
    }
    
    public void updateStatus(){
        if(nilai >= 70){
            this.status = "LULUS";
        } else if (percobaan >= 3){
            this.status = "GUGUR PERMANEN";
        } else {
            this.status = "GAGAL";
        }
    }

    public String getIdPeserta() {
        return idPeserta;
    }

    public int getNilai() {
        return nilai;
    }

    public int getPercobaan() {
        return percobaan;
    }

    public String getStatus() {
        return status;
    }
    
    
}
