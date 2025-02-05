

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
 * @author Lenny
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
            cargarComboSucursal();
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
        if (comboSucursal.getSelectedIndex() <= 0) {
            mostrarError("Seleccione una sucursal válida");
            return;
        }
        String sucursalSeleccionada = comboSucursal.getSelectedItem().toString();

        if (!sucursalSeleccionada.contains(" - ")) {
            mostrarError("Formato de sucursal no válido");
            return;
        }

        int idSucursal = Integer.parseInt(sucursalSeleccionada.split(" - ")[0]);

        String sql = "SELECT p.ID_PELICULA, p.NOMBRE_PELICULA, p.DURACION_PELICULA, " +
                    "p.CATEGORIA_PELICULA, p.GENERO_PELICULA " +
                    "FROM pelicula p " +
                    "INNER JOIN establecen e ON p.ID_PELICULA = e.ID_PELICULA " +
                    "INNER JOIN sucursal s ON e.ID_SUCURSAL = s.ID_SUCURSAL " +
                    "WHERE s.ID_SUCURSAL = ?";
        
        try (PreparedStatement pst = conexion.prepareStatement(sql)) {
            pst.setInt(1, idSucursal);
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
    
    private void cargarComboSucursal() {
        comboSucursal.removeAllItems();
        comboSucursal.addItem("Seleccione una sucursal");
        
        String sql= "SELECT ID_SUCURSAL, NOMBRE_SUCURSAL FROM sucursal";
        try (PreparedStatement pst = conexion.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String item = rs.getInt("ID_SUCURSAL") + " - " + rs.getString("NOMBRE_SUCURSAL");
                comboSucursal.addItem(item);
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar combo de sucursales: " + e.getMessage());
        }
    }
    
    private void cargarComboPeliculas(int idSucursal) {
        comboPelis.removeAllItems();
        comboPelis.addItem("Seleccione una película");
        
        String sql = "SELECT ID_PELICULA, NOMBRE_PELICULA FROM pelicula " +
                    "WHERE ID_PELICULA NOT IN (" +
                    "SELECT ID_PELICULA FROM establecen WHERE ID_SUCURSAL = ?" +
                    ")";
        
        try (PreparedStatement pst = conexion.prepareStatement(sql)) {
            pst.setInt(1, idSucursal);
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
    
    private boolean validarCampos() {
        if (comboPelis.getSelectedIndex() == 0) {
            mostrarError("Debe seleccionar una película");
            return false;
        }
        if (comboSucursal.getSelectedIndex() == 0) {
            mostrarError("Debe seleccionar una sucursal");
            return false;
        }
        return true;
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
        botEliminar = new javax.swing.JButton();
        botGuardar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        comboSucursal = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(39, 29, 41));
        jPanel1.setMinimumSize(new java.awt.Dimension(1000, 600));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 600));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("LISTA DE PELICULAS");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 60, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("AGREGAR PELICULA:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 180, -1, 20));

        jPanel1.add(comboPelis, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 180, 200, -1));

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
        tabla.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tablaAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jScrollPane2.setViewportView(tabla);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 230, 780, 240));

        btn_atras.setBackground(new java.awt.Color(230, 12, 16));
        btn_atras.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_atras.setForeground(new java.awt.Color(255, 255, 255));
        btn_atras.setText("Atras");
        btn_atras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_atrasActionPerformed(evt);
            }
        });
        jPanel1.add(btn_atras, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 499, 164, 47));

        botEliminar.setBackground(new java.awt.Color(230, 12, 16));
        botEliminar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        botEliminar.setForeground(new java.awt.Color(255, 255, 255));
        botEliminar.setText("Eliminar");
        botEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botEliminarActionPerformed(evt);
            }
        });
        jPanel1.add(botEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 499, 164, 47));

        botGuardar.setBackground(new java.awt.Color(230, 12, 16));
        botGuardar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        botGuardar.setForeground(new java.awt.Color(255, 255, 255));
        botGuardar.setText("Guardar");
        botGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(botGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 499, 164, 47));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("SUCURSAL:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 180, -1, 20));

        comboSucursal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboSucursalActionPerformed(evt);
            }
        });
        jPanel1.add(comboSucursal, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 180, 110, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/LOGOIZQUIERDA.jpg"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 600));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 600));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_atrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_atrasActionPerformed
        MenuAdminBar menu = new MenuAdminBar();
        menu.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btn_atrasActionPerformed

    private void botGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botGuardarActionPerformed

        if (!validarCampos()) {
            return;
        }

        String peliculaSeleccionada = comboPelis.getSelectedItem().toString();
        String sucursalSeleccionada = comboSucursal.getSelectedItem().toString();
    
        int idPelicula = Integer.parseInt(peliculaSeleccionada.split(" - ")[0]);
        int idSucursal = Integer.parseInt(sucursalSeleccionada.split(" - ")[0]);

        String sql = "INSERT INTO establecen (ID_PELICULA, ID_SUCURSAL) VALUES (?, ?)";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idPelicula);
            pstmt.setInt(2, idSucursal);

            int resultado = pstmt.executeUpdate();
            if (resultado > 0) {
                JOptionPane.showMessageDialog(this,
                    "Asociación guardada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

                //Actualizar tabla con las películas de la sucursal seleccionada
                cargarPeliculas();
                cargarComboPeliculas(idSucursal);
            }
        } catch (SQLException e) {
            mostrarError("Error al guardar la asociación: " + e.getMessage());
        }
    }//GEN-LAST:event_botGuardarActionPerformed

    private void tablaAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tablaAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaAncestorAdded

    private void comboSucursalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboSucursalActionPerformed
        if (comboSucursal.getSelectedIndex() > 0) { //al selec una sucursal
            String sucursalSeleccionada = comboSucursal.getSelectedItem().toString();
            int idSucursal = Integer.parseInt(sucursalSeleccionada.split(" - ")[0]);

            // Cargar películas que no estan asociadas a esta sucursal
            cargarComboPeliculas(idSucursal);
            cargarPeliculas();
        }
    }//GEN-LAST:event_comboSucursalActionPerformed

    private void botEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botEliminarActionPerformed
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarError("Seleccione una película de la lista para eliminar.");
            return;
        }

        int idPelicula = (int) modeloTabla.getValueAt(filaSeleccionada, 0);

        if (comboSucursal.getSelectedIndex() <= 0) {
            mostrarError("Seleccione una sucursal válida.");
            return;
        }

        String sucursalSeleccionada = comboSucursal.getSelectedItem().toString();
    
        if (!sucursalSeleccionada.contains(" - ")) {
            mostrarError("Formato de sucursal no válido.");
            return;
        }

        int idSucursal = Integer.parseInt(sucursalSeleccionada.split(" - ")[0]);

        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar esta película de la sucursal?", 
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
    
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        // Query eliminar relación entre la sucursal y la pelicula
        String sql = "DELETE FROM establecen WHERE ID_SUCURSAL = ? AND ID_PELICULA = ?";

        try (PreparedStatement pst = conexion.prepareStatement(sql)) {
            pst.setInt(1, idSucursal);
            pst.setInt(2, idPelicula);

            int filasAfectadas = pst.executeUpdate();
        
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(this, "Película eliminada correctamente.");
                cargarPeliculas();
                cargarComboPeliculas(idSucursal);
            } else {
                mostrarError("No se encontró la película en esta sucursal.");
            }
        } catch (SQLException e) {
            mostrarError("Error al eliminar la película: " + e.getMessage());
        }
    }//GEN-LAST:event_botEliminarActionPerformed

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
    private javax.swing.JButton botEliminar;
    private javax.swing.JButton botGuardar;
    private javax.swing.JButton btn_atras;
    private javax.swing.JComboBox<String> comboPelis;
    private javax.swing.JComboBox<String> comboSucursal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
