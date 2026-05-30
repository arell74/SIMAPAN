/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author arelssi
 */
public class Dokumen {
    private String idDokumen;
    private Peserta peserta;
    private String noPaspor;
    private String tanggalBerangkat;

    public Dokumen(String idDokumen, Peserta peserta, String noPaspor, String tanggalBerangkat) {
        this.idDokumen = idDokumen;
        this.peserta = peserta;
        this.noPaspor = noPaspor;
        this.tanggalBerangkat = tanggalBerangkat;
    }

    // Getter
    public String getIdDokumen() { return idDokumen; }
    public Peserta getPeserta() { return peserta; }
    public String getNoPaspor() { return noPaspor; }
    public String getTanggalBerangkat() { return tanggalBerangkat; }

    // Setter
    public void setNoPaspor(String noPaspor) { this.noPaspor = noPaspor; }
    public void setTanggalBerangkat(String tanggalBerangkat) { this.tanggalBerangkat = tanggalBerangkat; }
}