/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Views.instruktur;

/**
 *
 * @author arelssi
 */
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SuitJepang extends Frame implements ActionListener {

    Label hasil;
    Random random = new Random();

    public SuitJepang() {
        setTitle("Suit Jepang");
        setSize(300, 200);
        setLayout(new FlowLayout());

        Label judul = new Label("Pilih Batu, Gunting, atau Kertas");
        add(judul);

        Button batu = new Button("Batu");
        Button gunting = new Button("Gunting");
        Button kertas = new Button("Kertas");

        add(batu);
        add(gunting);
        add(kertas);

        hasil = new Label("Hasil akan muncul di sini");
        add(hasil);

        batu.addActionListener(this);
        gunting.addActionListener(this);
        kertas.addActionListener(this);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        String pemain = e.getActionCommand();

        String[] pilihan = {"Batu", "Gunting", "Kertas"};
        String komputer = pilihan[random.nextInt(3)];

        if (pemain.equals(komputer)) {
            hasil.setText("Seri! Komputer: " + komputer);
        }
        else if (
            (pemain.equals("Batu") && komputer.equals("Gunting")) ||
            (pemain.equals("Gunting") && komputer.equals("Kertas")) ||
            (pemain.equals("Kertas") && komputer.equals("Batu"))
        ) {
            hasil.setText("Menang! Komputer: " + komputer);
        }
        else {
            hasil.setText("Kalah! Komputer: " + komputer);
        }
    }

    public static void main(String[] args) {
        new SuitJepang();
    }
}
