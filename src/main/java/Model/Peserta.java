package Model;

// 1. Tambahkan extends Pengguna
public class Peserta extends Pengguna {
    // Encapsulation
    private String idPeserta;
    private String nik;
    private String tanggalLahir;
    private String jenisKelamin;
    private String noHp;
    private String agama;
    private String alamat;
    
    // Data Program & Akademik
    private String program;
    private Instruktur instrukturDamping;
    private String levelBahasa;
    private String statusPembayaran;
    private String kelas = "Belum Ditentukan";
    
    private String statusSeleksi;        
    private String statusKeberangkatan;
    private String kehadiranHariIni = "Hadir"; // Default Hadir

    // 2. Tambahkan parameter username dan password di constructor utama
    public Peserta(String username, String password, String idPeserta, String nik, String namaLengkap, 
                   String tanggalLahir, String jenisKelamin, String noHp, String agama, String alamat, 
                   String program, Instruktur instrukturDamping, String levelBahasa, 
                   String statusPembayaran, String statusSeleksi, String statusKeberangkatan) {
                   
        // Panggil constructor Pengguna (Superclass)
        // Set role default sebagai "PESERTA"
        super(username, password, namaLengkap, "PESERTA");
        
        this.idPeserta = idPeserta;
        this.nik = nik;
        this.tanggalLahir = tanggalLahir;
        this.jenisKelamin = jenisKelamin;
        this.noHp = noHp;
        this.agama = agama;
        this.alamat = alamat;
        this.program = program;
        this.instrukturDamping = instrukturDamping;
        this.levelBahasa = levelBahasa;
        this.statusPembayaran = statusPembayaran;
        this.statusSeleksi = statusSeleksi;
        this.statusKeberangkatan = statusKeberangkatan;
    }
    
    // Constructor kedua juga harus disesuaikan
    public Peserta(String username, String password, String idPeserta, String nik, String namaLengkap, 
                   String tanggalLahir, String jenisKelamin, String noHp, String agama, String alamat, 
                   String program, Instruktur instrukturDamping, String levelBahasa, 
                   String statusPembayaran) {
                   
        // Lempar ke constructor pertama
        this(username, password, idPeserta, nik, namaLengkap, tanggalLahir, jenisKelamin, noHp, agama, 
             alamat, program, instrukturDamping, levelBahasa, statusPembayaran, 
             "Belum Mulai", "Belum Berangkat");
    }

    // 3. Override method dari superclass Pengguna
    @Override
    public String getMenuOtoritas() {
        return "Akses Terbatas: Melihat Profil, Status Seleksi, dan Info Keberangkatan.";
    }

    // ==========================================
    // Getter & Setter di bawah sini tetap sama
    // (Tidak perlu menulis ulang getter/setter namaLengkap 
    // karena sudah diwarisi dari class Pengguna)
    // ==========================================

    public String getIdPeserta() { return idPeserta; }
    public void setIdPeserta(String idPeserta) { this.idPeserta = idPeserta; }

    public String getNik() { return nik; }
    public void setNik(String nik) { this.nik = nik; }

    public String getTanggalLahir() { return tanggalLahir; }
    public void setTanggalLahir(String tanggalLahir) { this.tanggalLahir = tanggalLahir; }

    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    public String getNoHp() { return noHp; }
    public void setNoHp(String noHp) { this.noHp = noHp; }

    public String getAgama() { return agama; }
    public void setAgama(String agama) { this.agama = agama; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }

    public Instruktur getInstrukturDamping() { return instrukturDamping; }
    public void setInstrukturDamping(Instruktur instrukturDamping) { this.instrukturDamping = instrukturDamping; }

    public String getLevelBahasa() { return levelBahasa; }
    public void setLevelBahasa(String levelBahasa) { this.levelBahasa = levelBahasa; }

    public String getStatusPembayaran() { return statusPembayaran; }
    public void setStatusPembayaran(String statusPembayaran) { this.statusPembayaran = statusPembayaran; }

    public String getStatusSeleksi() { return statusSeleksi; }
    public void setStatusSeleksi(String statusSeleksi) { this.statusSeleksi = statusSeleksi; }

    public String getStatusKeberangkatan() { return statusKeberangkatan; }
    public void setStatusKeberangkatan(String statusKeberangkatan) { this.statusKeberangkatan = statusKeberangkatan; }
    
    public String getKelas() { return kelas; }
    public void setKelas(String kelas) { this.kelas = kelas; }
    
    public String getKehadiranHariIni() { return kehadiranHariIni; }
    public void setKehadiranHariIni(String kehadiranHariIni) { this.kehadiranHariIni = kehadiranHariIni; }
}