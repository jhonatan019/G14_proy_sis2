/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vistas;

import baseDatos.ConexionBD;
import com.mycompany.cine2.Item;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class RegistroEmpleados extends javax.swing.JFrame {

    private Connection conexion;
    
    public RegistroEmpleados() {
        initComponents();
        setResizable(false);
        setTitle("Registro de Empleados");
        setLocationRelativeTo(null);
        try {
            conexion = ConexionBD.getConnection();
            cargarSucursales();
            cargarTabla();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage());
        }
    }
    
    // Método para limpiar campos (botón Nuevo)
    private void limpiarCampos() {
        txt_nombre.setText("");
        txt_apellido.setText("");
        txt_ci.setText("");
        txt_correo.setText("");
        txt_contra.setText("");
        if (combo_sucursal.getItemCount() > 0) {
            combo_sucursal.setSelectedIndex(0);
        }
    }

    
    // Método para cargar datos en la tabla
    private void cargarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(
            new Object[][] {},
            new String[] {"ID", "Nombre", "Apellidos", "C.I.", "Correo Electrónico", "Contraseña", "Sucursal"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(modelo);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setWidth(0);
        
        try {
            String sql = "SELECT u.*, s.NOMBRE_SUCURSAL FROM usuario u " +
                        "LEFT JOIN sucursal s ON u.ID_SUCURSAL = s.ID_SUCURSAL";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                Object[] fila = {
                    rs.getInt("ID_EMPLE"),
                    rs.getString("NOM_USUARIO"),
                    rs.getString("APELLIDOS_USUARIO"),
                    rs.getInt("CI_USUARIO"),
                    rs.getString("Correo_USUARIO"),
                    rs.getString("Contra_USUARIO"),
                    rs.getString("NOMBRE_SUCURSAL")
                };
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
        }
    }
    
    private void cargarSucursales() {
        combo_sucursal.removeAllItems();
        String sql = "SELECT ID_SUCURSAL, NOMBRE_SUCURSAL FROM sucursal";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                combo_sucursal.addItem(new Item(
                    rs.getInt("ID_SUCURSAL"),
                    rs.getString("NOMBRE_SUCURSAL")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar sucursales: " + e.getMessage());
        }
    }
    
    // Método para guardar empleado
    private void guardarEmpleado() {
        if (!validarCampos()) return;
        
        Item sucursalSeleccionada = (Item) combo_sucursal.getSelectedItem();
        if (sucursalSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una sucursal");
            return;
        }
        
        try {
            String sql = "INSERT INTO usuario (NOM_USUARIO, APELLIDOS_USUARIO, CI_USUARIO, " +
                        "Correo_USUARIO, Contra_USUARIO, CARGO_USUARIO, ID_SUCURSAL) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conexion.prepareStatement(sql);
            
            ps.setString(1, txt_nombre.getText().trim());
            ps.setString(2, txt_apellido.getText().trim());
            ps.setInt(3, Integer.parseInt(txt_ci.getText().trim()));
            ps.setString(4, txt_correo.getText().trim());
            ps.setString(5, txt_contra.getText().trim());
            ps.setInt(6, 1); // Valor por defecto para CARGO_USUARIO
            ps.setInt(7, sucursalSeleccionada.getId());
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Empleado guardado exitosamente");
            limpiarCampos();
            cargarTabla();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }
    
    // Método para editar empleado
    private void editarEmpleado() {
        if (!validarCampos()) return;
        
        Item sucursalSeleccionada = (Item) combo_sucursal.getSelectedItem();
        if (sucursalSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una sucursal");
            return;
        }
        
        int filaSeleccionada = jTable1.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado para editar");
            return;
        }

        try {
            int id = (int) jTable1.getValueAt(filaSeleccionada, 0);
            String sql = "UPDATE usuario SET NOM_USUARIO=?, APELLIDOS_USUARIO=?, " +
                        "CI_USUARIO=?, Correo_USUARIO=?, Contra_USUARIO=?, ID_SUCURSAL=? " +
                        "WHERE ID_EMPLE=?";
            
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, txt_nombre.getText().trim());
            ps.setString(2, txt_apellido.getText().trim());
            ps.setInt(3, Integer.parseInt(txt_ci.getText().trim()));
            ps.setString(4, txt_correo.getText().trim());
            ps.setString(5, txt_contra.getText().trim());
            ps.setInt(6, sucursalSeleccionada.getId());
            ps.setInt(7, id);
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Empleado actualizado exitosamente");
            limpiarCampos();
            cargarTabla();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
        }
    }
    
    // Método para eliminar empleado
     private void eliminarEmpleado() {
        int filaSeleccionada = jTable1.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado para eliminar");
            return;
        }
        
        if (JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este empleado?", 
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                int id = (int) jTable1.getValueAt(filaSeleccionada, 0);
                String sql = "DELETE FROM usuario WHERE ID_EMPLE=?";
                PreparedStatement ps = conexion.prepareStatement(sql);
                ps.setInt(1, id);
                
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Empleado eliminado exitosamente");
                limpiarCampos();
                cargarTabla();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
            }
        }
    }
    
    private boolean validarCampos() {
        if (txt_nombre.getText().trim().length() < 3) {
            JOptionPane.showMessageDialog(this, "El nombre debe tener al menos 3 caracteres");
            return false;
        }
        
        if (txt_apellido.getText().trim().length() < 3) {
            JOptionPane.showMessageDialog(this, "El apellido debe tener al menos 3 caracteres");
            return false;
        }
        
        try {
            Integer.parseInt(txt_ci.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "CI debe ser un número válido");
            return false;
        }
        
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!txt_correo.getText().matches(emailRegex)) {
            JOptionPane.showMessageDialog(this, "Ingrese un correo electrónico válido");
            return false;
        }
        
        if (txt_contra.getText().length() < 6) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 6 caracteres");
            return false;
        }
        
        return true;
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_ci = new javax.swing.JTextField();
        txt_apellido = new javax.swing.JTextField();
        txt_nombre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_contra = new javax.swing.JTextField();
        txt_correo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        guardar = new javax.swing.JButton();
        editar = new javax.swing.JButton();
        eliminar = new javax.swing.JButton();
        nuevo = new javax.swing.JButton();
        salir = new javax.swing.JButton();
        combo_sucursal = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1260, 836));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(39, 29, 41));
        jPanel1.setPreferredSize(new java.awt.Dimension(1260, 836));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("REGISTRO DE EMPLEADOS");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nombres:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Apellidos:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("C.I.:");

        txt_ci.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txt_ci.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txt_apellido.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txt_apellido.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txt_nombre.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txt_nombre.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Correo Electrónico:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Contraseña:");

        txt_contra.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txt_contra.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txt_correo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txt_correo.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTable1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Apellidos", "C.I.", "Correo Eletrónico", "Contraseña"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        guardar.setBackground(new java.awt.Color(230, 12, 16));
        guardar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        guardar.setForeground(new java.awt.Color(255, 255, 255));
        guardar.setText("Guardar");
        guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarActionPerformed(evt);
            }
        });

        editar.setBackground(new java.awt.Color(230, 12, 16));
        editar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        editar.setForeground(new java.awt.Color(255, 255, 255));
        editar.setText("Editar");
        editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarActionPerformed(evt);
            }
        });

        eliminar.setBackground(new java.awt.Color(230, 12, 16));
        eliminar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        eliminar.setForeground(new java.awt.Color(255, 255, 255));
        eliminar.setText("Eliminar");
        eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarActionPerformed(evt);
            }
        });

        nuevo.setBackground(new java.awt.Color(230, 12, 16));
        nuevo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        nuevo.setForeground(new java.awt.Color(255, 255, 255));
        nuevo.setText("Nuevo");
        nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoActionPerformed(evt);
            }
        });

        salir.setBackground(new java.awt.Color(230, 12, 16));
        salir.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        salir.setForeground(new java.awt.Color(255, 255, 255));
        salir.setText("Salir");
        salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirActionPerformed(evt);
            }
        });

        combo_sucursal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Sucursales:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_nombre, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                            .addComponent(txt_apellido))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(18, 18, 18)
                                        .addComponent(txt_contra, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(255, 255, 255))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(combo_sucursal, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(309, 309, 309))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(66, 66, 66)
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(txt_correo, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 683, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(123, 123, 123))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txt_ci, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(75, 75, 75)
                                .addComponent(editar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(85, 85, 85)
                                .addComponent(eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(80, 80, 80)
                                .addComponent(nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                                .addComponent(salir, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(60, 60, 60))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(jLabel1)
                .addGap(57, 57, 57)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txt_correo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_contra, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addComponent(txt_apellido))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(txt_ci, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7)
                    .addComponent(combo_sucursal, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editar, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(salir, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(76, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1260, 836));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoActionPerformed
     limpiarCampos();
    }//GEN-LAST:event_nuevoActionPerformed

    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarActionPerformed
     guardarEmpleado();
    }//GEN-LAST:event_guardarActionPerformed

    private void editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarActionPerformed
    editarEmpleado();
    }//GEN-LAST:event_editarActionPerformed

    private void eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarActionPerformed
    eliminarEmpleado();
    }//GEN-LAST:event_eliminarActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
    int fila = jTable1.getSelectedRow();
        if (fila >= 0) {
            txt_nombre.setText(String.valueOf(jTable1.getValueAt(fila, 1)));
            txt_apellido.setText(String.valueOf(jTable1.getValueAt(fila, 2)));
            txt_ci.setText(String.valueOf(jTable1.getValueAt(fila, 3)));
            txt_correo.setText(String.valueOf(jTable1.getValueAt(fila, 4)));
            txt_contra.setText(String.valueOf(jTable1.getValueAt(fila, 5)));
            
            // Seleccionar la sucursal correspondiente en el combo box
            String nombreSucursal = String.valueOf(jTable1.getValueAt(fila, 6));
            for (int i = 0; i < combo_sucursal.getItemCount(); i++) {
                Item item = (Item) combo_sucursal.getItemAt(i);
                if (item.toString().equals(nombreSucursal)) {
                    combo_sucursal.setSelectedIndex(i);
                    break;
                }
            }
        }
    
    }//GEN-LAST:event_jTable1MouseClicked

    private void salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirActionPerformed
        MenuAdminBar menu = new MenuAdminBar();
        menu.setVisible(true);
        this.setVisible(false);
       
    }//GEN-LAST:event_salirActionPerformed

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
    private javax.swing.JComboBox<Item> combo_sucursal;
    private javax.swing.JButton editar;
    private javax.swing.JButton eliminar;
    private javax.swing.JButton guardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton nuevo;
    private javax.swing.JButton salir;
    private javax.swing.JTextField txt_apellido;
    private javax.swing.JTextField txt_ci;
    private javax.swing.JTextField txt_contra;
    private javax.swing.JTextField txt_correo;
    private javax.swing.JTextField txt_nombre;
    // End of variables declaration//GEN-END:variables
}
