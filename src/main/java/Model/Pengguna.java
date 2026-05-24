package Model;

public abstract class Pengguna {
    private String username;
    private String password;
    private String namaLengkap;
    private String role; 

    public Pengguna(String username, String password, String namaLengkap, String role) {
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getNamaLengkap() { return namaLengkap; }
    public String getRole() { return role; }

    public boolean validasiLogin(String u, String p) {
        return this.username.equals(u) && this.password.equals(p);
    }

    // Abstract Method (Pilar Abstraction)
    public abstract String getMenuOtoritas();
}