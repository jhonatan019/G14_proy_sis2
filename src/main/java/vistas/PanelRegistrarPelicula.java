
package vistas;

import baseDatos.ConexionBD;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Lenny
 */
public class PanelRegistrarPelicula extends javax.swing.JFrame {

    private String rutaImagen = "";
    private Connection conexion;

    public PanelRegistrarPelicula() {
        initComponents();
        setResizable(false);
        setTitle("Panel de Registro de Peliculas");
        setLocationRelativeTo(null);

        /*/ Asegurarse que el label tenga un tamaño mínimo
        labelPreviewImagen.setPreferredSize(new Dimension(1000, 600)); // o el tamaño que prefieras

        // Si quieres un borde para ver mejor el área de la imagen
        labelPreviewImagen.setBorder(BorderFactory.createLineBorder(Color.GRAY));*/

        try {
            conexion = ConexionBD.getConnection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al conectar con la base de datos: " + e.getMessage(),
                    "Error de Conexión",
                    JOptionPane.ERROR_MESSAGE);
        }

        llenarComboCategorias();
        llenarComboGeneros();
    }

    private void llenarComboCategorias() {
        comboCategoria.removeAllItems();
        comboCategoria.addItem("Seleccione");
        comboCategoria.addItem("Estreno");
        comboCategoria.addItem("Normal");
        comboCategoria.addItem("Preventa");
    }

    private void llenarComboGeneros() {
        comboGenero.removeAllItems();
        comboGenero.addItem("Seleccione");
        comboGenero.addItem("Acción");
        comboGenero.addItem("Comedia");
        comboGenero.addItem("Drama");
        comboGenero.addItem("Terror");
        comboGenero.addItem("Ci-Ficc");
    }

    private void mostrarPreviewImagen(String ruta) {
        try {
            ImageIcon imagen = new ImageIcon(ruta);
            Image img = imagen.getImage().getScaledInstance(
                    labelPreviewImagen.getWidth(),
                    labelPreviewImagen.getHeight(),
                    Image.SCALE_SMOOTH
            );
            labelPreviewImagen.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar la imagen: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarDuracion() {
        String duracion = txtduracion.getText().trim();
        String formatoValido = "\\d{2}:\\d{2}:\\d{2}";

        if (!duracion.matches(formatoValido)) {
            mostrarError("El formato de la duración debe ser hh:mm:ss");
            return false;
        }

        String[] partes = duracion.split(":");
        int horas = Integer.parseInt(partes[0]);
        int minutos = Integer.parseInt(partes[1]);
        int segundos = Integer.parseInt(partes[2]);

        if (horas < 0 || horas > 23 || minutos < 0 || minutos > 59 || segundos < 0 || segundos > 59) {
            mostrarError("Ingrese una duración válida en formato hh:mm:ss");
            return false;
        }

        if (horas == 0 && minutos < 30) {
            mostrarError("La duración mínima debe ser de al menos 30 minutos");
            return false;
        }

        return true;
    }

    private boolean validarCampos() {
        if (txtnombre.getText().trim().isEmpty()) {
            mostrarError("El nombre de la película es obligatorio");
            return false;
        }

        if (txtduracion.getText().trim().isEmpty()) {
            mostrarError("La duración de la película es obligatoria");
            return false;
        }

        if (!validarDuracion()) {
            return false;
        }

        if (txtsipnopsis.getText().trim().isEmpty()) {
            mostrarError("La sinopsis de la película es obligatoria");
            return false;
        }

        if (comboGenero.getSelectedIndex() == 0) {
            mostrarError("Debe seleccionar un género");
            return false;
        }

        if (comboCategoria.getSelectedIndex() == 0) {
            mostrarError("Debe seleccionar una categoría");
            return false;
        }

        if (rutaImagen.isEmpty()) {
            mostrarError("Debe seleccionar una imagen para la película");
            return false;
        }

        return true;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this,
                mensaje,
                "Error de validación",
                JOptionPane.WARNING_MESSAGE);
    }

    private void limpiarCampos() {
        txtnombre.setText("");
        txtduracion.setText("");
        txtsipnopsis.setText("");
        comboGenero.setSelectedIndex(0);
        comboCategoria.setSelectedIndex(0);
        rutaImagen = "";
        // Si hay un preview de imagen, también limpiarlo
        if (labelPreviewImagen != null) {
            labelPreviewImagen.setIcon(null);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        n1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        comboGenero = new javax.swing.JComboBox<>();
        comboCategoria = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtsipnopsis = new javax.swing.JTextArea();
        txtduracion = new javax.swing.JTextField();
        txtnombre = new javax.swing.JTextField();
        botonGuardarRegistroPelicula = new javax.swing.JButton();
        botonCancelarRegistrodePeli = new javax.swing.JButton();
        labelPreviewImagen = new javax.swing.JLabel();
        labelAgregarImagen = new javax.swing.JLabel();
        botonAgregarImagen = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(39, 29, 41));
        jPanel1.setMinimumSize(new java.awt.Dimension(1000, 600));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 600));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("REGISTRAR PELICULA");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 50, 500, 50));

        n1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        n1.setForeground(new java.awt.Color(255, 255, 255));
        n1.setText("NOMBRE: ");
        jPanel1.add(n1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, -1, 20));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("DURACION:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 240, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("SIPNOPSIS:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 280, -1, 20));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("CATEGORIA:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 410, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("GENERO:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 450, -1, 20));

        comboGenero.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        comboGenero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Acción", "Comedia", "Drama", "Terror", "Ci-Ficc" }));
        jPanel1.add(comboGenero, new org.netbeans.lib.awtextra.AbsoluteConstraints(251, 450, 200, 25));

        comboCategoria.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        comboCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A", "B", "C" }));
        comboCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboCategoriaActionPerformed(evt);
            }
        });
        jPanel1.add(comboCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(251, 410, 200, 25));

        txtsipnopsis.setColumns(20);
        txtsipnopsis.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtsipnopsis.setLineWrap(true);
        txtsipnopsis.setRows(5);
        txtsipnopsis.setWrapStyleWord(true);
        jScrollPane1.setViewportView(txtsipnopsis);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(251, 280, -1, -1));

        txtduracion.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtduracion.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtduracion.setToolTipText("");
        txtduracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtduracionActionPerformed(evt);
            }
        });
        jPanel1.add(txtduracion, new org.netbeans.lib.awtextra.AbsoluteConstraints(251, 240, 200, 30));

        txtnombre.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtnombre.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtnombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnombreActionPerformed(evt);
            }
        });
        jPanel1.add(txtnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(251, 198, 200, 30));

        botonGuardarRegistroPelicula.setBackground(new java.awt.Color(230, 12, 16));
        botonGuardarRegistroPelicula.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        botonGuardarRegistroPelicula.setForeground(new java.awt.Color(255, 255, 255));
        botonGuardarRegistroPelicula.setText("Guardar ");
        botonGuardarRegistroPelicula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarRegistroPeliculaActionPerformed(evt);
            }
        });
        jPanel1.add(botonGuardarRegistroPelicula, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 499, 320, 47));

        botonCancelarRegistrodePeli.setBackground(new java.awt.Color(230, 12, 16));
        botonCancelarRegistrodePeli.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        botonCancelarRegistrodePeli.setForeground(new java.awt.Color(255, 255, 255));
        botonCancelarRegistrodePeli.setText("Cancelar");
        botonCancelarRegistrodePeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarRegistrodePeliActionPerformed(evt);
            }
        });
        jPanel1.add(botonCancelarRegistrodePeli, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 499, 320, 47));
        jPanel1.add(labelPreviewImagen, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 260, 200, 200));

        labelAgregarImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/labelImagenPelicula.png"))); // NOI18N
        jPanel1.add(labelAgregarImagen, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 260, 200, 200));

        botonAgregarImagen.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        botonAgregarImagen.setText("AGREGAR IMAGEN");
        botonAgregarImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAgregarImagenActionPerformed(evt);
            }
        });
        jPanel1.add(botonAgregarImagen, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 200, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/LOGOIZQUIERDA.jpg"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 980, 590));
        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 140, -1, 60));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1140, 690));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCategoriaActionPerformed

    }//GEN-LAST:event_comboCategoriaActionPerformed

    private void txtduracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtduracionActionPerformed
        validarDuracion();
    }//GEN-LAST:event_txtduracionActionPerformed

    private void txtnombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnombreActionPerformed
        String nombre = txtnombre.getText();
        if (nombre.length() > 30) {
            txtnombre.setText(nombre.substring(0, 30));
        }
    }//GEN-LAST:event_txtnombreActionPerformed

    private void botonGuardarRegistroPeliculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarRegistroPeliculaActionPerformed
        if (validarCampos()) {
            try {
                String consulta = "SELECT COUNT(*) FROM pelicula WHERE NOMBRE_PELICULA = ? AND SIPNOPSIS_PELICULA = ?";
                PreparedStatement pstmtCheck = conexion.prepareStatement(consulta);
                pstmtCheck.setString(1, txtnombre.getText().trim());
                pstmtCheck.setString(2, txtsipnopsis.getText().trim());

                ResultSet result = pstmtCheck.executeQuery();
                result.next();
                int existe = result.getInt(1);

                if (existe > 0) {
                    JOptionPane.showMessageDialog(this,
                            "La película ya está registrada con el mismo nombre y sinopsis.",
                            "Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String sql = "INSERT INTO pelicula (NOMBRE_PELICULA, DURACION_PELICULA, "
                        + "SIPNOPSIS_PELICULA, GENERO_PELICULA, CATEGORIA_PELICULA, "
                        + "IMAGEN_PELICULA) VALUES (?, ?, ?, ?, ?, ?)";

                PreparedStatement pstmt = conexion.prepareStatement(sql);
                pstmt.setString(1, txtnombre.getText());
                pstmt.setString(2, txtduracion.getText());
                pstmt.setString(3, txtsipnopsis.getText());
                pstmt.setString(4, comboGenero.getSelectedItem().toString());
                pstmt.setString(5, comboCategoria.getSelectedItem().toString());
                pstmt.setString(6, rutaImagen);

                int resultado = pstmt.executeUpdate();
                if (resultado > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Película registrada exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    limpiarCampos();
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Error al registrar la película: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_botonGuardarRegistroPeliculaActionPerformed

    private void botonCancelarRegistrodePeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarRegistrodePeliActionPerformed
        MenuAdminBar menu = new MenuAdminBar();
        menu.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_botonCancelarRegistrodePeliActionPerformed

    private void botonAgregarImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAgregarImagenActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Imágenes", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            rutaImagen = archivoSeleccionado.getAbsolutePath();
            // Mostrar preview de la imagen
            mostrarPreviewImagen(rutaImagen);
        }
    }//GEN-LAST:event_botonAgregarImagenActionPerformed

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
            java.util.logging.Logger.getLogger(PanelRegistrarPelicula.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PanelRegistrarPelicula.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PanelRegistrarPelicula.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PanelRegistrarPelicula.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PanelRegistrarPelicula().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonAgregarImagen;
    private javax.swing.JButton botonCancelarRegistrodePeli;
    private javax.swing.JButton botonGuardarRegistroPelicula;
    private javax.swing.JComboBox<String> comboCategoria;
    private javax.swing.JComboBox<String> comboGenero;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelAgregarImagen;
    private javax.swing.JLabel labelPreviewImagen;
    private javax.swing.JLabel n1;
    private javax.swing.JTextField txtduracion;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextArea txtsipnopsis;
    // End of variables declaration//GEN-END:variables
}
