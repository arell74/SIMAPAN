package Model;

public class Dokumen {
    private String idDokumen;
    private Peserta peserta;
    private String tanggalBerangkat;

    private String noPaspor;
    private String filePaspor;

    private String hasilMcu;
    private String fileMcu;
    
    private String noVisa;
    private String fileVisa;

    private String nikKtp;
    private String fileKtp;

    public Dokumen(String idDokumen, Peserta peserta, String tanggalBerangkat) {
        this.idDokumen = idDokumen;
        this.peserta = peserta;
        this.tanggalBerangkat = tanggalBerangkat;
    }

    // --- Getters ---
    public String getIdDokumen() { return idDokumen; }
    public Peserta getPeserta() { return peserta; }
    public String getTanggalBerangkat() { return tanggalBerangkat; }
    
    public String getNoPaspor() { return noPaspor; }
    public String getFilePaspor() { return filePaspor; }
    public String getHasilMcu() { return hasilMcu; }
    public String getFileMcu() { return fileMcu; }
    public String getNoVisa() { return noVisa; }
    public String getFileVisa() { return fileVisa; }
    public String getNikKtp() { return nikKtp; }
    public String getFileKtp() { return fileKtp; }

    // --- Setters (Penting untuk Form Input!) ---
    public void setPaspor(String noPaspor, String filePaspor) {
        this.noPaspor = noPaspor;
        this.filePaspor = filePaspor;
    }

    public void setMcu(String hasilMcu, String fileMcu) {
        this.hasilMcu = hasilMcu;
        this.fileMcu = fileMcu;
    }

    public void setVisa(String noVisa, String fileVisa) {
        this.noVisa = noVisa;
        this.fileVisa = fileVisa;
    }

    public void setKtp(String nikKtp, String fileKtp) {
        this.nikKtp = nikKtp;
        this.fileKtp = fileKtp;
    }
}