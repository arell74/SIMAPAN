/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Views.instruktur;

/**
 *
 * @author arelssi
 *
 */
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class PasswordGenerator extends Frame implements ActionListener {

    TextField panjang, hasil;
    Button generate;
    Random random = new Random();

    public PasswordGenerator() {

        setTitle("Password Generator");
        setSize(350, 200);
        setLayout(new FlowLayout());

        add(new Label("Panjang Password:"));

        panjang = new TextField(10);
        add(panjang);

        // Hanya boleh mengetik angka
        panjang.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }
        });

        generate = new Button("Generate");
        add(generate);

        add(new Label("Password:"));

        hasil = new TextField(20);
        hasil.setEditable(false);
        add(hasil);

        generate.addActionListener(this);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        int jumlah = Integer.parseInt(panjang.getText());

        if (jumlah <= 0) {
            hasil.setText("Masukkan angka > 0");
            return;
        }

        String karakter =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz" +
            "0123456789!@#$%";

        String password = "";

        for (int i = 0; i < jumlah; i++) {
            int posisi = random.nextInt(karakter.length());
            password += karakter.charAt(posisi);
        }

        hasil.setText(password);
    }

    public static void main(String[] args) {
        new PasswordGenerator();
    }
}
