/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author arelssi
 */
import Views.FormLogin;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;


public class Main {
    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
        } catch (Exception ex) {
            System.err.println("Gagal memuat FlatLaf");
        }

        java.awt.EventQueue.invokeLater(() -> {
            new FormLogin().setVisible(true); 
        });
    }
}
