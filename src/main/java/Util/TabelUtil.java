/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
/**
 *
 * @author arelssi
 */
public class TabelUtil {
    public static void autoResizeKolom(JTable tabel) {
        final int margin = 15;

        for (int column = 0; column < tabel.getColumnCount(); column++) {
            int lebarOptimal = 50;

            TableCellRenderer headerRenderer = tabel.getTableHeader().getDefaultRenderer();
            Object headerValue = tabel.getColumnModel().getColumn(column).getHeaderValue();
            Component headerComp = headerRenderer.getTableCellRendererComponent(tabel, headerValue, false, false, 0, column);
            lebarOptimal = Math.max(lebarOptimal, headerComp.getPreferredSize().width);

            for (int row = 0; row < tabel.getRowCount(); row++) {
                TableCellRenderer cellRenderer = tabel.getCellRenderer(row, column);
                Component cellComp = tabel.prepareRenderer(cellRenderer, row, column);
                lebarOptimal = Math.max(lebarOptimal, cellComp.getPreferredSize().width + margin);
            }

            TableColumn tableColumn = tabel.getColumnModel().getColumn(column);
            tableColumn.setPreferredWidth(lebarOptimal);
        }
    }
}
