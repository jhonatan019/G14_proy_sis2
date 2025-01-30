/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vistas;

import baseDatos.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Tommy
 */
public class ListaDePeliculas extends javax.swing.JFrame {

    private Connection conexion;
    private DefaultTableModel modeloTabla;
    private String nombreSucursal;
    
     public ListaDePeliculas(String nombreSucursal) {
        initComponents();
        setResizable(false);
        setTitle("Lista de Películas - " + nombreSucursal);
        setLocationRelativeTo(null);
        this.nombreSucursal = nombreSucursal;
        
        try {
            conexion = ConexionBD.getConnection();
            configurarTabla();
            cargarPeliculas();
            cargarComboPeliculas();
        } catch (SQLException e) {
            mostrarError("Error de conexión: " + e.getMessage());
        }
    }
    
    private void configurarTabla() {
        modeloTabla = (DefaultTableModel) tabla.getModel();
        modeloTabla.setRowCount(0);
        modeloTabla.setColumnCount(0);
        
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Película");
        modeloTabla.addColumn("Duración");
        modeloTabla.addColumn("Categoría");
        modeloTabla.addColumn("Género");
    }
    
    private void cargarPeliculas() {
        modeloTabla.setRowCount(0);
        
        String sql = "SELECT p.ID_PELICULA, p.NOMBRE_PELICULA, p.DURACION_PELICULA, " +
                    "p.CATEGORIA_PELICULA, p.GENERO_PELICULA " +
                    "FROM pelicula p " +
                    "INNER JOIN establecen e ON p.ID_PELICULA = e.ID_PELICULA " +
                    "INNER JOIN suscursal s ON e.ID_SUCURSAL = s.ID_SUCURSAL " +
                    "WHERE s.NOMBRE_SUCURSAL = ?";
                    
        try (PreparedStatement pst = conexion.prepareStatement(sql)) {
            pst.setString(1, nombreSucursal);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("ID_PELICULA"),
                    rs.getString("NOMBRE_PELICULA"),
                    rs.getString("DURACION_PELICULA"),
                    rs.getString("CATEGORIA_PELICULA"),
                    rs.getString("GENERO_PELICULA")
                };
                modeloTabla.addRow(fila);
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar películas: " + e.getMessage());
        }
    }
    
    private void cargarComboPeliculas() {
        comboPelis.removeAllItems();
        comboPelis.addItem("Seleccione una película");
        
        String sql = "SELECT ID_PELICULA, NOMBRE_PELICULA FROM pelicula";
        try (PreparedStatement pst = conexion.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String item = rs.getInt("ID_PELICULA") + " - " + rs.getString("NOMBRE_PELICULA");
                comboPelis.addItem(item);
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar combo de películas: " + e.getMessage());
        }
    }
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this,
            mensaje,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
    
    @Override
    public void dispose() {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
        super.dispose();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        comboPelis = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        btn_atras = new javax.swing.JButton();
        btn_eliminar = new javax.swing.JButton();
        btn_editar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(39, 29, 41));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("LISTA DE PELICULAS");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 80, -1, -1));

        jLabel3.setFont(new java.awt.Font("Arial Black", 1, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("AGREGAR PELICULA");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 210, -1, -1));

        comboPelis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(comboPelis, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 210, 200, -1));

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tabla);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 280, 780, 240));

        btn_atras.setBackground(new java.awt.Color(230, 12, 16));
        btn_atras.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_atras.setForeground(new java.awt.Color(255, 255, 255));
        btn_atras.setText("Atras");
        btn_atras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_atrasActionPerformed(evt);
            }
        });
        jPanel1.add(btn_atras, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 610, 164, 47));

        btn_eliminar.setBackground(new java.awt.Color(230, 12, 16));
        btn_eliminar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_eliminar.setForeground(new java.awt.Color(255, 255, 255));
        btn_eliminar.setText("Eliminar");
        jPanel1.add(btn_eliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 610, 164, 47));

        btn_editar.setBackground(new java.awt.Color(230, 12, 16));
        btn_editar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_editar.setForeground(new java.awt.Color(255, 255, 255));
        btn_editar.setText("Editar");
        jPanel1.add(btn_editar, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 610, 164, 47));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/LOGOIZQUIERDA.jpg"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 1057, 649));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1250, 760));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_atrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_atrasActionPerformed
    MenuAdmin menu = new MenuAdmin();
    menu.setVisible(true);
    this.setVisible(false);
    }//GEN-LAST:event_btn_atrasActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ClassNotFoundException ex) {
        java.util.logging.Logger.getLogger(ListaDePeliculas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
        java.util.logging.Logger.getLogger(ListaDePeliculas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
        java.util.logging.Logger.getLogger(ListaDePeliculas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        java.util.logging.Logger.getLogger(ListaDePeliculas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            // Podemos usar "Cercado" como valor por defecto para pruebas
            new ListaDePeliculas("Cercado").setVisible(true);
        }
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_atras;
    private javax.swing.JButton btn_editar;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JComboBox<String> comboPelis;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
