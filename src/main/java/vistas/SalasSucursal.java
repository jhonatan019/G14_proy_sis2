/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vistas;

import baseDatos.ConexionBD;
import com.mycompany.cine2.Item;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;



public class SalasSucursal extends javax.swing.JFrame {

    
    private int idSucursal;
    private DefaultTableModel modeloTabla;
    private JTable tablaSalas;
    
   
    public SalasSucursal() {
    initComponents();
    setResizable(false);
    setTitle("Salas Sucursal");
    setLocationRelativeTo(null);
    
    configurarTabla();
    cargarSucursales();
}
    
    private void configurarTabla() {
    modeloTabla = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 2; // Solo la columna de estado es editable
        }
        
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 2) {
                return Boolean.class;
            }
            return super.getColumnClass(columnIndex);
        }
    };
    
    modeloTabla.addColumn("ID Sala");
    modeloTabla.addColumn("Número de Sala");
    modeloTabla.addColumn("Habilitada");
    
    tablaSalas = new JTable(modeloTabla);
    tablaSalas.getColumnModel().getColumn(0).setPreferredWidth(50);
    tablaSalas.getColumnModel().getColumn(1).setPreferredWidth(100);
    tablaSalas.getColumnModel().getColumn(2).setPreferredWidth(80);
    
    JScrollPane scrollPane = new JScrollPane(tablaSalas);
    jPanel1.setLayout(new AbsoluteLayout());
    jPanel1.add(scrollPane, new AbsoluteConstraints(200, 100, 800, 400));
}
    
    private void cargarSalas() {
    modeloTabla.setRowCount(0);
    
    String sql = "SELECT ID_SALA, NUMERO_SALA, ESTADO_SALA " +
                "FROM sala WHERE ID_SUCURSAL = ?";
    
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idSucursal);
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            Object[] fila = new Object[3];
            fila[0] = rs.getInt("ID_SALA");
            fila[1] = rs.getString("NUMERO_SALA");
            fila[2] = rs.getInt("ESTADO_SALA") == 1;
            modeloTabla.addRow(fila);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this,
            "Error al cargar salas: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
}
 
    private void cargarSucursales() {
    String sql = "SELECT ID_SUCURSAL, NOMBRE_SUCURSAL FROM sucursal";
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ResultSet rs = ps.executeQuery();
        combo_sucursal.removeAllItems();
        while (rs.next()) {
            combo_sucursal.addItem(new Item(rs.getInt("ID_SUCURSAL"), 
                                          rs.getString("NOMBRE_SUCURSAL")));
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al cargar sucursales: " + e.getMessage());
    }
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btn_atras = new javax.swing.JButton();
        btn_guardar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        combo_sucursal = new javax.swing.JComboBox<>();
        btn_ver = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(39, 29, 41));

        btn_atras.setBackground(new java.awt.Color(230, 12, 16));
        btn_atras.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_atras.setForeground(new java.awt.Color(255, 255, 255));
        btn_atras.setText("Atras");
        btn_atras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_atrasActionPerformed(evt);
            }
        });

        btn_guardar.setBackground(new java.awt.Color(230, 12, 16));
        btn_guardar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_guardar.setForeground(new java.awt.Color(255, 255, 255));
        btn_guardar.setText("Guardar Cambios");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("SALAS-SUCURSAL");

        combo_sucursal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        combo_sucursal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_sucursalActionPerformed(evt);
            }
        });

        btn_ver.setBackground(new java.awt.Color(0, 51, 204));
        btn_ver.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_ver.setForeground(new java.awt.Color(255, 255, 255));
        btn_ver.setText("Ver");
        btn_ver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_verActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(390, 390, 390))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(203, 203, 203)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btn_atras, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83)
                        .addComponent(btn_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 254, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(combo_sucursal, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_ver, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(179, 179, 179))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(combo_sucursal, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ver, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(518, 518, 518)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_atras, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 81, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1180, 740));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
       String sql = "UPDATE sala SET ESTADO_SALA = ? WHERE ID_SALA = ?";
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {
        
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            int idSala = (Integer) modeloTabla.getValueAt(i, 0);
            boolean estado = (Boolean) modeloTabla.getValueAt(i, 2);
            pst.setInt(1, estado ? 1 : 0);
            pst.setInt(2, idSala);
            pst.addBatch();
        }
        
        pst.executeBatch();
        JOptionPane.showMessageDialog(this,
            "Estados de salas actualizados exitosamente",
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE);
            
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this,
            "Error al guardar cambios: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    }//GEN-LAST:event_btn_guardarActionPerformed

    private void btn_atrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_atrasActionPerformed
        MenuAdminBar menu = new MenuAdminBar();
        menu.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btn_atrasActionPerformed

    private void btn_verActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_verActionPerformed
    if (combo_sucursal.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(this, "Seleccione una sucursal");
        return;
    }
    
    Item sucursalSeleccionada = (Item) combo_sucursal.getSelectedItem();
    idSucursal = sucursalSeleccionada.getId();
    cargarSalas();
    }//GEN-LAST:event_btn_verActionPerformed

    private void combo_sucursalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_sucursalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_combo_sucursalActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_atras;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JButton btn_ver;
    private javax.swing.JComboBox<Item> combo_sucursal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
