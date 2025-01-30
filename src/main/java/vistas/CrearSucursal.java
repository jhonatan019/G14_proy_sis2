/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vistas;

import baseDatos.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Tommy
 */
public class CrearSucursal extends javax.swing.JFrame {

    private Connection conexion;
    
    public CrearSucursal() {
        initComponents();
        setResizable(false);
        setTitle("Crear Sucursal");
        setLocationRelativeTo(null);
        
        // Inicializar conexión
        try {
            conexion = ConexionBD.getConnection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al conectar con la base de datos: " + e.getMessage(),
                "Error de Conexión",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validarCampos() {
        if (jTexNombre.getText().trim().isEmpty()) {
            mostrarError("El nombre de la sucursal es obligatorio");
            jTexNombre.requestFocus();
            return false;
        }
        
        if (jTexDireccion.getText().trim().isEmpty()) {
            mostrarError("La dirección de la sucursal es obligatoria");
            jTexDireccion.requestFocus();
            return false;
        }
        
        if (jTexReferencia.getText().trim().isEmpty()) {
            mostrarError("El número de referencia es obligatorio");
            jTexReferencia.requestFocus();
            return false;
        }
        
        // Validar longitud máxima (varchar(20))
        if (jTexNombre.getText().length() > 20) {
            mostrarError("El nombre no puede exceder los 20 caracteres");
            jTexNombre.requestFocus();
            return false;
        }
        
        if (jTexDireccion.getText().length() > 20) {
            mostrarError("La dirección no puede exceder los 20 caracteres");
            jTexDireccion.requestFocus();
            return false;
        }
        
        if (jTexReferencia.getText().length() > 20) {
            mostrarError("La referencia no puede exceder los 20 caracteres");
            jTexReferencia.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void guardarSucursal() {
        String sql = "INSERT INTO suscursal (NOMBRE_SUCURSAL, DIRECCION_SUCURSAL, REFERENCIA_SUCURSAL) " +
                    "VALUES (?, ?, ?)";
                    
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, jTexNombre.getText().trim());
            pstmt.setString(2, jTexDireccion.getText().trim());
            pstmt.setString(3, jTexReferencia.getText().trim());
            
            int resultado = pstmt.executeUpdate();
            
            if (resultado > 0) {
                JOptionPane.showMessageDialog(this,
                    "Sucursal registrada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al registrar la sucursal: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this,
            mensaje,
            "Error de validación",
            JOptionPane.WARNING_MESSAGE);
    }
    
    private void limpiarCampos() {
        jTexNombre.setText("");
        jTexDireccion.setText("");
        jTexReferencia.setText("");
        jTexNombre.requestFocus();
    }
    
    // Agregar este método para cerrar la conexión
    private void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
    
    @Override
    public void dispose() {
        cerrarConexion();
        super.dispose();
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
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButGuardar = new javax.swing.JButton();
        jTexNombre = new javax.swing.JTextField();
        jTexDireccion = new javax.swing.JTextField();
        jTexReferencia = new javax.swing.JTextField();
        jButCancelar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(39, 29, 41));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("REGISTRO DE SUCURSAL");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 130, 710, 50));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("NOMBRE:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 290, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("DIRECCION:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 360, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Nro REFERENCIA:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 420, -1, -1));

        jButGuardar.setBackground(new java.awt.Color(255, 0, 0));
        jButGuardar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jButGuardar.setForeground(new java.awt.Color(255, 255, 255));
        jButGuardar.setText("Guardar");
        jButGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(jButGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 570, 320, 47));

        jTexNombre.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        jTexNombre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTexNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTexNombreActionPerformed(evt);
            }
        });
        jPanel1.add(jTexNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 280, 296, 45));

        jTexDireccion.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        jTexDireccion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTexDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 350, 296, 45));

        jTexReferencia.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        jTexReferencia.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTexReferencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 410, 296, 45));

        jButCancelar.setBackground(new java.awt.Color(255, 0, 0));
        jButCancelar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jButCancelar.setForeground(new java.awt.Color(255, 255, 255));
        jButCancelar.setText("Cancelar");
        jButCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButCancelarActionPerformed(evt);
            }
        });
        jPanel1.add(jButCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 570, 320, 47));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/WhatsApp Image 2025-01-27 at 9.10.20 PM.jpeg"))); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 960, 600));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1240, 750));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButGuardarActionPerformed
      if (validarCampos()) {
            guardarSucursal();
        }
    }//GEN-LAST:event_jButGuardarActionPerformed

    private void jTexNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTexNombreActionPerformed

    }//GEN-LAST:event_jTexNombreActionPerformed

    private void jButCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelarActionPerformed
      
      MenuAdmin adminMenu = new MenuAdmin();
      adminMenu.setVisible(true);
      this.dispose();
    }//GEN-LAST:event_jButCancelarActionPerformed

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
            java.util.logging.Logger.getLogger(CrearSucursal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CrearSucursal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CrearSucursal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CrearSucursal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CrearSucursal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButCancelar;
    private javax.swing.JButton jButGuardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTexDireccion;
    private javax.swing.JTextField jTexNombre;
    private javax.swing.JTextField jTexReferencia;
    // End of variables declaration//GEN-END:variables
}
