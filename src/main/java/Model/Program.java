/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

public class Program {
    // Encapsulation
    private String idProgram;
    private String namaProgram;
    private String bidangUsaha;
    private double biaya; 
    private int kuota;

    public Program(String idProgram, String namaProgram, String bidangUsaha, double biaya, int kuota) {
        this.idProgram = idProgram;
        this.namaProgram = namaProgram;
        this.bidangUsaha = bidangUsaha;
        this.biaya = biaya;
        this.kuota = kuota;
    }

    public String getIdProgram() {
        return idProgram;
    }

    public void setIdProgram(String idProgram) {
        this.idProgram = idProgram;
    }

    public String getNamaProgram() {
        return namaProgram;
    }

    public void setNamaProgram(String namaProgram) {
        this.namaProgram = namaProgram;
    }

    public String getBidangUsaha() {
        return bidangUsaha;
    }

    public void setBidangUsaha(String bidangUsaha) {
        this.bidangUsaha = bidangUsaha;
    }

    public double getBiaya() {
        return biaya;
    }

    public void setBiaya(double biaya) {
        this.biaya = biaya;
    }

    public int getKuota() {
        return kuota;
    }

    public void setKuota(int kuota) {
        this.kuota = kuota;
    }
    
    @Override
    public String toString() {
        return namaProgram;
    }
}
