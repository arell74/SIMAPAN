/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/*
 * @author arelssi
 */
public class Materi {
    private String idMateri;
    private String idInstruktur;
    private String judul;
    private String kelasTarget;   // "Kelas A", "Kelas B", atau "Semua Kelas"
    private String tipe;          // "Teks" atau "File"
    private String isiTeks;       // Diisi jika tipe == "Teks"
    private String namaFileAsli;  // Nama asli file yang diupload (jika tipe == "File")
    private String namaFileTersimpan; // Nama file setelah disalin ke Brankas_Materi
    private String tanggalUpload;

    public Materi(String idMateri, String idInstruktur, String judul, String kelasTarget,
                  String tipe, String isiTeks, String namaFileAsli, String namaFileTersimpan,
                  String tanggalUpload) {
        this.idMateri = idMateri;
        this.idInstruktur = idInstruktur;
        this.judul = judul;
        this.kelasTarget = kelasTarget;
        this.tipe = tipe;
        this.isiTeks = isiTeks;
        this.namaFileAsli = namaFileAsli;
        this.namaFileTersimpan = namaFileTersimpan;
        this.tanggalUpload = tanggalUpload;
    }

    public String getIdMateri() { return idMateri; }
    public String getIdInstruktur() { return idInstruktur; }
    public String getJudul() { return judul; }
    public String getKelasTarget() { return kelasTarget; }
    public String getTipe() { return tipe; }
    public String getIsiTeks() { return isiTeks; }
    public String getNamaFileAsli() { return namaFileAsli; }
    public String getNamaFileTersimpan() { return namaFileTersimpan; }
    public String getTanggalUpload() { return tanggalUpload; }

    public void setJudul(String judul) { this.judul = judul; }
    public void setKelasTarget(String kelasTarget) { this.kelasTarget = kelasTarget; }
    public void setIsiTeks(String isiTeks) { this.isiTeks = isiTeks; }
    public void setNamaFileAsli(String namaFileAsli) { this.namaFileAsli = namaFileAsli; }
    public void setNamaFileTersimpan(String namaFileTersimpan) { this.namaFileTersimpan = namaFileTersimpan; }

    /**
     * Ringkasan singkat isi materi untuk ditampilkan di tabel:
     * nama file jika berupa berkas, atau cuplikan teks jika berupa teks.
     */
    public String getRingkasanIsi() {
        if ("File".equals(tipe)) {
            return namaFileAsli != null ? namaFileAsli : "-";
        } else {
            if (isiTeks == null || isiTeks.isEmpty()) return "-";
            return isiTeks.length() > 40 ? isiTeks.substring(0, 40) + "..." : isiTeks;
        }
    }
}