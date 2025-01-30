/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vistas;

import baseDatos.ConexionBD;
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

/**
 *
 * @author Tommy
 */
public class SalasSucursal extends javax.swing.JFrame {

    private Connection conexion;
    private int idSucursal;
    private DefaultTableModel modeloTabla;
    private JTable tablaSalas;
    
    public SalasSucursal(int idSucursal, String nombreSucursal) {
        initComponents();
        setResizable(false);
        setTitle("Salas Sucursal - " + nombreSucursal);
        setLocationRelativeTo(null);
        
        this.idSucursal = idSucursal;
        
        try {
            conexion = ConexionBD.getConnection();
            configurarTabla();
            cargarSalas();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error de conexión: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    public SalasSucursal() {
    initComponents();
    setResizable(false);
    setTitle("Salas Sucursal");
    setLocationRelativeTo(null);
}
    
    private void configurarTabla() {
    modeloTabla = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 2;
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
    modeloTabla.addColumn("Capacidad");
    
    tablaSalas = new JTable(modeloTabla);
    tablaSalas.getColumnModel().getColumn(0).setPreferredWidth(50);
    tablaSalas.getColumnModel().getColumn(1).setPreferredWidth(100);
    tablaSalas.getColumnModel().getColumn(2).setPreferredWidth(80);
    tablaSalas.getColumnModel().getColumn(3).setPreferredWidth(80);
    
    JScrollPane scrollPane = new JScrollPane(tablaSalas);
    
    // Agregar al jPanel1 en lugar del contentPane
    jPanel1.setLayout(new AbsoluteLayout()); // Asegurarse que el panel use AbsoluteLayout
    jPanel1.add(scrollPane, new AbsoluteConstraints(200, 100, 800, 400));
}
    
    private void cargarSalas() {
        modeloTabla.setRowCount(0);
        
        String sql = "SELECT ID_SALA, NUMERO_SALA, ESTADO_SALA, CAPACIDAD_SALA " +
                    "FROM sala WHERE ID_SUCURSAL = ?";
        
        try (PreparedStatement pst = conexion.prepareStatement(sql)) {
            pst.setInt(1, idSucursal);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getInt("ID_SALA");
                fila[1] = rs.getString("NUMERO_SALA");
                fila[2] = rs.getInt("ESTADO_SALA") == 1; // Convertir a boolean
                fila[3] = rs.getInt("CAPACIDAD_SALA");
                modeloTabla.addRow(fila);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar salas: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
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
        btn_atras = new javax.swing.JButton();
        btn_guardar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(203, 203, 203)
                .addComponent(btn_atras, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83)
                .addComponent(btn_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 254, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(393, 393, 393))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(536, 536, 536)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_atras, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 82, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1180, 740));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        try {
            String sql = "UPDATE sala SET ESTADO_SALA = ? WHERE ID_SALA = ?";
            PreparedStatement pst = conexion.prepareStatement(sql);

            boolean hayCambios = false;

            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                int idSala = (Integer) modeloTabla.getValueAt(i, 0);
                boolean estado = (Boolean) modeloTabla.getValueAt(i, 2);

                pst.setInt(1, estado ? 1 : 0);
                pst.setInt(2, idSala);
                pst.addBatch();

                hayCambios = true;
            }

            if (hayCambios) {
                pst.executeBatch();
                JOptionPane.showMessageDialog(this,
                    "Cambios guardados exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar cambios: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btn_guardarActionPerformed

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
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        java.util.logging.Logger.getLogger(SalasSucursal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }

    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new SalasSucursal(1, "Cercado").setVisible(true);
        }
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_atras;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
