//package Util;
//
//import Database.Koneksi;
//import java.awt.Component;
//import java.io.InputStream;
//import java.sql.Connection;
//import java.util.Map;
//import javax.swing.JOptionPane;
//import net.sf.jasperreports.engine.JasperCompileManager;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
//import net.sf.jasperreports.view.JasperViewer;
//
//public class ReportUtil {
//
//    public static void tampilkanLaporan(Component parent, String resourceJrxml, Map<String, Object> parameter) {
//        try {
//            InputStream stream = ReportUtil.class.getResourceAsStream(resourceJrxml);
//            if (stream == null) {
//                JOptionPane.showMessageDialog(parent,
//                        "File template laporan tidak ditemukan: " + resourceJrxml,
//                        "Error Laporan", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            Connection conn = Koneksi.getConnection();
//            JasperReport jasperReport = JasperCompileManager.compileReport(stream);
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, conn);
//
//            if (jasperPrint.getPages().isEmpty()) {
//                JOptionPane.showMessageDialog(parent,
//                        "Belum ada data nilai atau kuis untuk dicetak.",
//                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
//                return;
//            }
//
//            JasperViewer.viewReport(jasperPrint, false);
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(parent,
//                    "Gagal mencetak laporan JasperReport:\n" + e.getMessage(),
//                    "Error JasperReport", JOptionPane.ERROR_MESSAGE);
//            e.printStackTrace();
//        }
//    }
//}
package Util;

import Database.Koneksi;
import java.awt.Component;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class ReportUtil {

    public static void tampilkanLaporan(Component parent, String resourceJrxml, Map<String, Object> parameter) {
        tampilkanLaporan(parent, resourceJrxml, parameter, "SIMAPAN - Pratinjau Laporan");
    }

    public static void tampilkanLaporan(Component parent, String resourceJrxml, Map<String, Object> parameter, String judulJendela) {
        try (InputStream stream = ReportUtil.class.getResourceAsStream(resourceJrxml)) {
            if (stream == null) {
                JOptionPane.showMessageDialog(parent,
                        "File template laporan tidak ditemukan: " + resourceJrxml,
                        "Error Laporan", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Connection conn = Koneksi.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(parent,
                        "Koneksi ke database MySQL gagal. Pastikan layanan MySQL sudah aktif.",
                        "Error Koneksi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(stream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, conn);

            if (jasperPrint.getPages().isEmpty()) {
                JOptionPane.showMessageDialog(parent,
                        "Tidak ada data yang tersedia untuk dicetak pada laporan ini.",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle(judulJendela);
            viewer.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent,
                    "Gagal mencetak laporan JasperReport:\n" + e.getMessage(),
                    "Error JasperReport", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    public static void tampilkanLaporanDataSource(
            Component parent,
            String resourceJrxml,
            Map<String, Object> parameter,
            net.sf.jasperreports.engine.JRDataSource dataSource,
            String judulJendela) {
        try (InputStream stream = ReportUtil.class.getResourceAsStream(resourceJrxml)) {
            if (stream == null) {
                JOptionPane.showMessageDialog(parent,
                        "File template laporan tidak ditemukan: " + resourceJrxml,
                        "Error Laporan", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(stream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);

            if (jasperPrint.getPages().isEmpty()) {
                JOptionPane.showMessageDialog(parent,
                        "Tidak ada data peserta yang dapat dicetak.",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle(judulJendela);
            viewer.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent,
                    "Gagal mencetak laporan JasperReport:\n" + e.getMessage(),
                    "Error JasperReport", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}