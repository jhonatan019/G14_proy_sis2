/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vistas;

import baseDatos.ConexionBD;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Font;
import com.mycompany.cine2.Item;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class PeliculaMasVendida extends javax.swing.JFrame {

    private Connection conexion;
    
    public PeliculaMasVendida() {
        initComponents();
        setResizable(false);
        setTitle("Pelicula mas Vendida");
        setLocationRelativeTo(null);
        
        inicializarTabla();
        cargarComboSucursales();
        cargarComboMeses();
        
        btn_ver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_verActionPerformed(evt);
            }
        });
    }
    
    private void inicializarTabla() {
        String[] titulos = {"ID", "Pelicula mas Vendida", "Categoria", "Genero", 
                           "Entradas Vendidas", "Monto", "Sucursal"};
        tabla_vendidas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            titulos
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    private void cargarComboSucursales() {
        combo_sucursal.removeAllItems();
        try {
            Connection conn = ConexionBD.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID_SUCURSAL, NOMBRE_SUCURSAL FROM sucursal");
            
            while (rs.next()) {
                combo_sucursal.addItem(new Item(
                    rs.getInt("ID_SUCURSAL"),
                    rs.getString("NOMBRE_SUCURSAL")
                ));
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error al cargar sucursales: " + ex.getMessage());
        }
    }

    private void cargarComboMeses() {
        combo_mes.removeAllItems();
        java.time.YearMonth mesActual = java.time.YearMonth.now();
        
        for (int i = 0; i < 12; i++) {
            java.time.YearMonth mes = mesActual.minusMonths(i);
            String formato = String.format("%d-%02d", 
                mes.getYear(), 
                mes.getMonthValue()
            );
            combo_mes.addItem(new Item(
                mes.getYear() * 100 + mes.getMonthValue(),
                formato
            ));
        }
    }

    private double obtenerPrecioBoleto() {
        try {
            Connection conn = ConexionBD.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                "SELECT VALOR_CONFIG FROM configuracion WHERE NOMBRE_CONFIG = 'PRECIO_BOLETO'"
            );
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                double precio = rs.getDouble("VALOR_CONFIG");
                rs.close();
                pstmt.close();
                conn.close();
                return precio;
            }
            
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error al obtener precio del boleto: " + ex.getMessage());
        }
        return 35.00; 
    }

    private void btn_verActionPerformed(java.awt.event.ActionEvent evt) {
        if (combo_sucursal.getSelectedItem() == null || combo_mes.getSelectedItem() == null) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Por favor seleccione mes y sucursal");
            return;
        }

        Item sucursal = (Item) combo_sucursal.getSelectedItem();
        Item mes = (Item) combo_mes.getSelectedItem();
        
        double precioBoleto = obtenerPrecioBoleto();
        
        javax.swing.table.DefaultTableModel model = 
            (javax.swing.table.DefaultTableModel) tabla_vendidas.getModel();
        model.setRowCount(0);
        
        String sql = "SELECT " +
                    "p.ID_PELICULA, " +
                    "p.NOMBRE_PELICULA, " +
                    "p.CATEGORIA_PELICULA, " +
                    "p.GENERO_PELICULA, " +
                    "COUNT(b.ID_BOLETO) as entradas_vendidas, " +
                    "COUNT(b.ID_BOLETO) * ? as monto_total, " +
                    "s.NOMBRE_SUCURSAL " +
                    "FROM pelicula p " +
                    "JOIN funcion f ON p.ID_PELICULA = f.ID_PELICULA " +
                    "JOIN sala sa ON f.ID_SALA = sa.ID_SALA " +
                    "JOIN sucursal s ON sa.ID_SUCURSAL = s.ID_SUCURSAL " +
                    "JOIN butaca bu ON f.ID_HORARIO = bu.ID_HORARIO " +
                    "JOIN boleto b ON bu.ID_VENTA = b.ID_VENTA " +
                    "JOIN venta v ON b.ID_VENTA = v.ID_VENTA " +
                    "WHERE s.ID_SUCURSAL = ? " +
                    "AND YEAR(v.FECHA_VENTA) = ? " +
                    "AND MONTH(v.FECHA_VENTA) = ? " +
                    "GROUP BY p.ID_PELICULA, p.NOMBRE_PELICULA, " +
                    "p.CATEGORIA_PELICULA, p.GENERO_PELICULA, s.NOMBRE_SUCURSAL " +
                    "ORDER BY entradas_vendidas DESC " +
                    "LIMIT 1";
        
        try {
            Connection conn = ConexionBD.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            int yearMonth = mes.getId();
            int year = yearMonth / 100;
            int month = yearMonth % 100;
            
            pstmt.setDouble(1, precioBoleto);
            pstmt.setInt(2, sucursal.getId());
            pstmt.setInt(3, year);
            pstmt.setInt(4, month);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Object[] fila = new Object[]{
                    rs.getInt("ID_PELICULA"),
                    rs.getString("NOMBRE_PELICULA"),
                    rs.getString("CATEGORIA_PELICULA"),
                    rs.getString("GENERO_PELICULA"),
                    rs.getInt("entradas_vendidas"),
                    rs.getDouble("monto_total"),
                    rs.getString("NOMBRE_SUCURSAL")
                };
                model.addRow(fila);
            }
            
            if (model.getRowCount() == 0) {
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "No se encontraron ventas para el período y sucursal seleccionados");
            }
            
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error al mostrar datos: " + ex.getMessage());
        }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_vendidas = new javax.swing.JTable();
        btn_atras = new javax.swing.JButton();
        btn_emitirPDF = new javax.swing.JButton();
        combo_mes = new javax.swing.JComboBox<>();
        combo_sucursal = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        btn_ver = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1260, 836));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(39, 29, 41));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("PELICULA MAS VENDIDA");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("MES:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 240, 60, -1));

        tabla_vendidas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tabla_vendidas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Película mas Vendida", "Categoría", "Genero", "Entradas Vendidas", "Monto", "Sucursal"
            }
        ));
        jScrollPane1.setViewportView(tabla_vendidas);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 310, 860, 190));

        btn_atras.setBackground(new java.awt.Color(230, 12, 16));
        btn_atras.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_atras.setForeground(new java.awt.Color(255, 255, 255));
        btn_atras.setText("Atrás");
        btn_atras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_atrasActionPerformed(evt);
            }
        });
        jPanel1.add(btn_atras, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 700, 190, 40));

        btn_emitirPDF.setBackground(new java.awt.Color(230, 12, 16));
        btn_emitirPDF.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_emitirPDF.setForeground(new java.awt.Color(255, 255, 255));
        btn_emitirPDF.setText("Emitir PDF");
        btn_emitirPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_emitirPDFActionPerformed(evt);
            }
        });
        jPanel1.add(btn_emitirPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(876, 694, 180, 50));

        combo_mes.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jPanel1.add(combo_mes, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 240, 160, 30));

        combo_sucursal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        combo_sucursal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_sucursalActionPerformed(evt);
            }
        });
        jPanel1.add(combo_sucursal, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 240, 160, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("SUCRUSAL:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 240, -1, -1));

        btn_ver.setBackground(new java.awt.Color(0, 51, 204));
        btn_ver.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_ver.setForeground(new java.awt.Color(255, 255, 255));
        btn_ver.setText("Ver");
        jPanel1.add(btn_ver, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 240, 120, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/LOGOIZQUIERDA.jpg"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1110, 640));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1250, 820));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_atrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_atrasActionPerformed
    Reportes reporte = new Reportes();
    reporte.setVisible(true);
    this.setVisible(false);
    }//GEN-LAST:event_btn_atrasActionPerformed

    private void combo_sucursalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_sucursalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_combo_sucursalActionPerformed

    private void btn_emitirPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_emitirPDFActionPerformed
     if (tabla_vendidas.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, 
            "Primero debe generar un reporte para exportar a PDF");
        return;
    }

    try {
        Document documento = new Document(PageSize.A4);
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar reporte PDF");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF files (*.pdf)", "pdf"));
        
        // Obtener mes y nombre de archivo
        Item mes = (Item) combo_mes.getSelectedItem();
        String nombreArchivo = "PeliculaMasVendida-" + mes.toString() + ".pdf";
        fileChooser.setSelectedFile(new File(nombreArchivo));
        
        if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        String rutaPDF = fileChooser.getSelectedFile().getAbsolutePath();
        
        PdfWriter.getInstance(documento, new FileOutputStream(rutaPDF));
        documento.open();
        
        // Agregar logo con la ruta correcta
        try {
            Image logo = Image.getInstance("src/main/resources/images/logo.jpg");
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            documento.add(logo);
        } catch (Exception e) {
            System.out.println("No se pudo cargar el logo: " + e.getMessage());
        }
        
        // Título del documento
        Paragraph titulo = new Paragraph();
        titulo.add(new Chunk("CINE PELICULON\n", 
            FontFactory.getFont("Helvetica", 20, Font.BOLD)));
        titulo.add(new Chunk("Reporte de Película Más Vendida\n\n", 
            FontFactory.getFont("Helvetica", 16, Font.BOLD)));
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);
        
        // Información del reporte
        Item sucursal = (Item) combo_sucursal.getSelectedItem();
        
        Paragraph info = new Paragraph();
        info.add(new Chunk("Sucursal: " + sucursal.toString() + "\n"));
        info.add(new Chunk("Período: " + mes.toString() + "\n"));
        info.add(new Chunk("Fecha de emisión: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()) + "\n\n"));
        documento.add(info);
        
        // Crear tabla
        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidthPercentage(100);
        
        // Encabezados
        String[] encabezados = {"ID", "Película", "Categoría", "Género", "Entradas", "Monto", "Sucursal"};
        for (String encabezado : encabezados) {
            PdfPCell celda = new PdfPCell(new Phrase(encabezado, 
                FontFactory.getFont("Helvetica", 11, Font.BOLD)));
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setPadding(5);
            tabla.addCell(celda);
        }
        
        // Datos
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tabla_vendidas.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                PdfPCell celda = new PdfPCell(new Phrase(String.valueOf(model.getValueAt(i, j))));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPadding(5);
                tabla.addCell(celda);
            }
        }
        documento.add(tabla);
        
        // Pie de página
        Paragraph pie = new Paragraph("\n\nEste es un documento generado por tu cine favorito.", 
            FontFactory.getFont("Helvetica", 10, Font.ITALIC));
        pie.setAlignment(Element.ALIGN_CENTER);
        documento.add(pie);
        
        documento.close();
        
        JOptionPane.showMessageDialog(this, 
            "El reporte se ha generado exitosamente:\n" + rutaPDF);
        
        // Abrir el PDF automáticamente
        Desktop.getDesktop().open(new File(rutaPDF));
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Error al generar el PDF: " + e.getMessage());
        e.printStackTrace();
    }
    }//GEN-LAST:event_btn_emitirPDFActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PeliculaMasVendida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PeliculaMasVendida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PeliculaMasVendida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PeliculaMasVendida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PeliculaMasVendida().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_atras;
    private javax.swing.JButton btn_emitirPDF;
    private javax.swing.JButton btn_ver;
    private javax.swing.JComboBox<Item> combo_mes;
    private javax.swing.JComboBox<Item> combo_sucursal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabla_vendidas;
    // End of variables declaration//GEN-END:variables
}
