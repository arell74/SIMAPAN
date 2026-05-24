/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author arelssi
 */
public class AdminLPK extends Pengguna {
    
    public AdminLPK(String username, String password, String namaLengkap) {
        super(username, password, namaLengkap, "ADMIN");
    }

    @Override
    public String getMenuOtoritas() {
        return "Akses Penuh: Manajemen Peserta, Instruktur, dan Keberangkatan.";
    }
}
