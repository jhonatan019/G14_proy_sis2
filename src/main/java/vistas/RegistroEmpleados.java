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
import javax.swing.JComboBox;
import javax.swing.JOptionPane;



/**
 *
 * @author Tommy
 */
public class RegistroEmpleados extends javax.swing.JFrame {

    private Connection conexion;
    
    public RegistroEmpleados() {
        initComponents();
        setResizable(false);
        setTitle("Registro de Empleados");
        setLocationRelativeTo(null);
        
        // Inicializar conexión y cargar datos
        try {
            conexion = ConexionBD.getConnection();
            cargarSucursales();
            cargarCargos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error de conexión: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarSucursales() {
        combo_sucursal.removeAllItems();
        combo_sucursal.addItem("Seleccione una sucursal");
        
        String sql = "SELECT NOMBRE_SUCURSAL FROM suscursal";
        try {
            PreparedStatement pst = conexion.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                combo_sucursal.addItem(rs.getString("NOMBRE_SUCURSAL"));
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar sucursales: " + e.getMessage());
        }
    }
    
    private void cargarCargos() {
        combo_cargo.removeAllItems();
        combo_cargo.addItem("Seleccione un cargo");
        combo_cargo.addItem("Empleado");
        combo_cargo.addItem("Administrador");
    }
    
      private boolean validarCampos() {
        if (txt_nombre.getText().trim().isEmpty()) {
            mostrarError("Ingrese el nombre");
            return false;
        }
        
        if (txt_apellido.getText().trim().isEmpty()) {
            mostrarError("Ingrese el apellido");
            return false;
        }
        
        if (txt_ci.getText().trim().isEmpty()) {
            mostrarError("Ingrese la cédula");
            return false;
        }
        
        if (txt_telefono.getText().trim().isEmpty()) {
            mostrarError("Ingrese el teléfono");
            return false;
        }
        
        if (combo_sucursal.getSelectedIndex() == 0) {
            mostrarError("Seleccione una sucursal");
            return false;
        }
        
        if (combo_cargo.getSelectedIndex() == 0) {
            mostrarError("Seleccione un cargo");
            return false;
        }
        
        return true;
    }
    
    private void registrarUsuario() {
        try {
            // Primero obtenemos el ID de la sucursal seleccionada
            String sqlSucursal = "SELECT ID_SUCURSAL FROM suscursal WHERE NOMBRE_SUCURSAL = ?";
            PreparedStatement pstSucursal = conexion.prepareStatement(sqlSucursal);
            pstSucursal.setString(1, combo_sucursal.getSelectedItem().toString());
            ResultSet rs = pstSucursal.executeQuery();
            
            if (rs.next()) {
                int idSucursal = rs.getInt("ID_SUCURSAL");
                
                // Determinamos el valor del cargo (0 para empleado, 1 para administrador)
                int cargo = combo_cargo.getSelectedItem().toString().equals("Administrador") ? 1 : 0;
                
                // Insertamos el nuevo usuario
                String sql = "INSERT INTO usuario (ID_SUCURSAL, NOM_USUARIO, APELLIDOS_USUARIO, " +
                           "CI_USUARIO, TELF_USUARIO, CARGO_USUARIO) VALUES (?, ?, ?, ?, ?, ?)";
                
                PreparedStatement pst = conexion.prepareStatement(sql);
                pst.setInt(1, idSucursal);
                pst.setString(2, txt_nombre.getText().trim());
                pst.setString(3, txt_apellido.getText().trim());
                pst.setInt(4, Integer.parseInt(txt_ci.getText().trim()));
                pst.setInt(5, Integer.parseInt(txt_telefono.getText().trim()));
                pst.setInt(6, cargo);
                
                int resultado = pst.executeUpdate();
                
                if (resultado > 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Usuario registrado exitosamente");
                    limpiarCampos();
                }
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al registrar usuario: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "La cédula y el teléfono deben ser números válidos",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this,
            mensaje,
            "Error",
            JOptionPane.WARNING_MESSAGE);
    }
    
    private void limpiarCampos() {
        txt_nombre.setText("");
        txt_apellido.setText("");
        txt_ci.setText("");
        txt_telefono.setText("");
        combo_sucursal.setSelectedIndex(0);
        combo_cargo.setSelectedIndex(0);
        txt_nombre.requestFocus();
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
        txt_nombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_ci = new javax.swing.JTextField();
        txt_apellido = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txt_complemento = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        campo_direccion = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_telefono = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        campo_correo = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        campo_contra = new javax.swing.JPasswordField();
        jLabel11 = new javax.swing.JLabel();
        combo_sucursal = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        combo_cargo = new javax.swing.JComboBox<>();
        btn_registrar = new javax.swing.JButton();
        btn_cancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(253, 185, 19));

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("REGISTRO DE EMPLEADOS");

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 20)); // NOI18N
        jLabel3.setText("Nombre:");

        txt_nombre.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        txt_nombre.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txt_nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nombreActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 20)); // NOI18N
        jLabel4.setText("Apellido:");

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 20)); // NOI18N
        jLabel5.setText("C.I.");

        txt_ci.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        txt_ci.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txt_ci.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_ciActionPerformed(evt);
            }
        });

        txt_apellido.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        txt_apellido.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txt_apellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_apellidoActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Roboto", 1, 20)); // NOI18N
        jLabel6.setText("Complemento:");

        txt_complemento.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        txt_complemento.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txt_complemento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_complementoActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Roboto", 1, 20)); // NOI18N
        jLabel7.setText("Dirección:");

        campo_direccion.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        campo_direccion.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        campo_direccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campo_direccionActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 20)); // NOI18N
        jLabel8.setText("Teléfono:");

        txt_telefono.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        txt_telefono.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txt_telefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_telefonoActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Roboto", 1, 20)); // NOI18N
        jLabel9.setText("Correo Electrónico:");

        campo_correo.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        campo_correo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        campo_correo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campo_correoActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Roboto", 1, 20)); // NOI18N
        jLabel10.setText("Contraseña:");

        campo_contra.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Roboto", 1, 20)); // NOI18N
        jLabel11.setText("Sucursal:");

        combo_sucursal.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        combo_sucursal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sucursal Cercado", "Sucursal Quillacollo", "Sucursal Sacaba" }));

        jLabel12.setFont(new java.awt.Font("Roboto", 1, 20)); // NOI18N
        jLabel12.setText("Cargo:");

        combo_cargo.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        combo_cargo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Empleado", "Administrador" }));

        btn_registrar.setBackground(new java.awt.Color(230, 12, 16));
        btn_registrar.setFont(new java.awt.Font("Roboto", 1, 20)); // NOI18N
        btn_registrar.setForeground(new java.awt.Color(255, 255, 255));
        btn_registrar.setText("Registrar");
        btn_registrar.setBorderPainted(false);
        btn_registrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_registrarActionPerformed(evt);
            }
        });

        btn_cancelar.setBackground(new java.awt.Color(230, 12, 16));
        btn_cancelar.setFont(new java.awt.Font("Roboto", 1, 20)); // NOI18N
        btn_cancelar.setForeground(new java.awt.Color(255, 255, 255));
        btn_cancelar.setText("Cancelar");
        btn_cancelar.setBorderPainted(false);
        btn_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btn_registrar, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(175, 175, 175)
                                .addComponent(btn_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_ci, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_complemento, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(campo_direccion, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(campo_correo, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txt_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(campo_contra, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel12)
                                .addGap(18, 18, 18)
                                .addComponent(combo_cargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(combo_sucursal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(346, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_nombre)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_apellido)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txt_ci)
                        .addComponent(jLabel5))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txt_complemento)
                        .addComponent(jLabel6)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(campo_direccion)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txt_telefono))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(campo_correo)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(campo_contra, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(combo_sucursal))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_registrar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(51, 51, 51))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(combo_cargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1250, 750));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nombreActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txt_nombreActionPerformed

    private void txt_ciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_ciActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_ciActionPerformed

    private void txt_apellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_apellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_apellidoActionPerformed

    private void txt_complementoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_complementoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_complementoActionPerformed

    private void campo_direccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campo_direccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campo_direccionActionPerformed

    private void txt_telefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_telefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_telefonoActionPerformed

    private void campo_correoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campo_correoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campo_correoActionPerformed

    private void btn_registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_registrarActionPerformed
        if (validarCampos()) {
            registrarUsuario();
        }
    }//GEN-LAST:event_btn_registrarActionPerformed

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
    MenuAdmin adminMenu = new MenuAdmin();
    adminMenu.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_btn_cancelarActionPerformed

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
            java.util.logging.Logger.getLogger(RegistroEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegistroEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegistroEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegistroEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegistroEmpleados().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cancelar;
    private javax.swing.JButton btn_registrar;
    private javax.swing.JPasswordField campo_contra;
    private javax.swing.JTextField campo_correo;
    private javax.swing.JTextField campo_direccion;
    private javax.swing.JComboBox<String> combo_cargo;
    private javax.swing.JComboBox<String> combo_sucursal;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txt_apellido;
    private javax.swing.JTextField txt_ci;
    private javax.swing.JTextField txt_complemento;
    private javax.swing.JTextField txt_nombre;
    private javax.swing.JTextField txt_telefono;
    // End of variables declaration//GEN-END:variables
}
