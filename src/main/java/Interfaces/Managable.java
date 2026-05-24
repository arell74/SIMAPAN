/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;
import java.util.List;
/**
 *
 * @author arelssi
 */
public interface Managable<T> {
    void tambah(T data);
    void edit(int index, T data);
    void hapus(int index);
    List<T> cari(String keywords);
}