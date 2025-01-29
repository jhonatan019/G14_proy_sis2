
package cine;

import dba.Mysql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;


public class CrearSucursal extends javax.swing.JFrame {

    ArrayList<String> suscursales;
    PreparedStatement ps;
    Connection conn;
    Statement sent;
    ResultSet rs;
    
    public CrearSucursal() {
        initComponents();
        this.setLocationRelativeTo(null);
        conn = Mysql.getConnection();
        suscursales = new ArrayList<>();
        suscursales();
    }

    void limpiar(){
        jTexNombre.setText("");
        jTexDireccion.setText("");
        jTexReferencia.setText("");
        
    }
    void suscursales() {
        try {
            sent = conn.createStatement();
            rs = sent.executeQuery("Select * from suscursal");
            while (rs.next()) {
                String nombre = rs.getString("NOMBRE_SUCURSAL");
                suscursales.add(nombre);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    boolean esLetra() {
        String nombre = jTexNombre.getText();
        for (char c : nombre.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') {
                return false;
            }
        }
        return true;
    }
    
    boolean esDireccionValida(){
        String direccion = jTexDireccion.getText();
        for(char c : direccion.toCharArray()){
            if(c != '.' && !Character.isLetterOrDigit(c) && c != ' '){
                return false;
            }
        }
        return true;
    }
   boolean esNumero() {
        boolean esNumerico;
        try {
        // Verifica si el texto es un número
        long numero = Long.parseLong(jTexReferencia.getText()); // Usamos long para evitar problemas con números grandes
        // Verifica que el número tenga 7 o 8 dígitos
        if (jTexReferencia.getText().length() == 7 || jTexReferencia.getText().length() == 8) {
            esNumerico = true;
        } else {
            esNumerico = false;
        }
        } catch (NumberFormatException e) {
          esNumerico = false;
    }
    return esNumerico;
}

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTexNombre = new javax.swing.JTextField();
        jTexDireccion = new javax.swing.JTextField();
        jTexReferencia = new javax.swing.JTextField();
        jButGuardar = new javax.swing.JButton();
        jButCancelar = new javax.swing.JButton();
        jLaFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("REGISTRO DE SUCURSAL");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(324, 64, 710, 50));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("NOMBRE:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(213, 219, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("DIRECCION:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(213, 286, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Nro REFERENCIA:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(213, 353, -1, -1));

        jTexNombre.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTexNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTexNombreActionPerformed(evt);
            }
        });
        jPanel1.add(jTexNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(482, 215, 296, 45));

        jTexDireccion.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jPanel1.add(jTexDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(482, 277, 296, 45));

        jTexReferencia.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jPanel1.add(jTexReferencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(482, 340, 296, 45));

        jButGuardar.setBackground(new java.awt.Color(255, 0, 0));
        jButGuardar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jButGuardar.setForeground(new java.awt.Color(255, 255, 255));
        jButGuardar.setText("Guardar");
        jButGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(jButGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 499, 320, 47));

        jButCancelar.setBackground(new java.awt.Color(255, 0, 0));
        jButCancelar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jButCancelar.setForeground(new java.awt.Color(255, 255, 255));
        jButCancelar.setText("Cancelar");
        jButCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButCancelarActionPerformed(evt);
            }
        });
        jPanel1.add(jButCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 499, 320, 47));

        jLaFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/LOGOIZQUIERDA.jpg"))); // NOI18N
        jPanel1.add(jLaFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 610));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1000, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTexNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTexNombreActionPerformed

    }//GEN-LAST:event_jTexNombreActionPerformed

    private void jButGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButGuardarActionPerformed
     if (jTexNombre.getText().length() != 0 && jTexDireccion.getText().length() != 0 && jTexReferencia.getText().length() != 0) {
        if (esLetra()) {
            if (esDireccionValida()) {
                if (esNumero()) {
                    if (!suscursales.contains(jTexNombre.getText().trim())) {
                        try {

                            PreparedStatement checkName = conn.prepareStatement("SELECT COUNT(*) FROM suscursal WHERE NOMBRE_SUCURSAL = ?");
                            checkName.setString(1, jTexNombre.getText());
                            ResultSet rsName = checkName.executeQuery();
                            rsName.next();
                            int countName = rsName.getInt(1);
                            rsName.close();
                            checkName.close();
                            PreparedStatement checkAddress = conn.prepareStatement("SELECT COUNT(*) FROM suscursal WHERE DIRECCION_SUCURSAL = ?");
                            checkAddress.setString(1, jTexDireccion.getText());
                            ResultSet rsAddress = checkAddress.executeQuery();
                            rsAddress.next();
                            int countAddress = rsAddress.getInt(1);
                            rsAddress.close();
                            checkAddress.close();

                            if (countName > 0) {
                                JOptionPane.showMessageDialog(null, "Nombre de la sucursal ya está en uso");
                            } else if (countAddress > 0) {
                                JOptionPane.showMessageDialog(null, "Dirección de la sucursal ya está en uso");
                            } else {
                                String sql = "INSERT INTO suscursal (NOMBRE_SUCURSAL, DIRECCION_SUCURSAL, REFERENCIA_SUCURSAL) VALUES(?,?,?)";
                                PreparedStatement ps = conn.prepareStatement(sql);
                                ps.setString(1, jTexNombre.getText());
                                ps.setString(2, jTexDireccion.getText());
                                ps.setString(3, jTexReferencia.getText());
                                int n = ps.executeUpdate();
                                if (n > 0) {
                                    JOptionPane.showMessageDialog(null, "Datos guardados correctamente");
                                }
                                ps.close();
                                limpiar();
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Error al guardar los datos" + e.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Nombre de la sucursal ya está en uso");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Revise el número ingresado");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Revise la dirección ingresada");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Revise el nombre ingresado");
        }
    } else {
        JOptionPane.showMessageDialog(null, "Datos incompletos para el registro");
    }       
    }//GEN-LAST:event_jButGuardarActionPerformed

    private void jButCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelarActionPerformed
      if (!jTexNombre.getText().trim().isEmpty() || !jTexDireccion.getText().trim().isEmpty() || !jTexReferencia.getText().trim().isEmpty()) {
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Estás seguro de cancelar el registro?",
            "Confirmar cancelación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if (confirmacion == JOptionPane.YES_OPTION) {
            limpiar(); 
        }
    } 
    }//GEN-LAST:event_jButCancelarActionPerformed

  
    public static void main(String args[]) {
    
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CrearSucursal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButCancelar;
    private javax.swing.JButton jButGuardar;
    private javax.swing.JLabel jLaFondo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTexDireccion;
    private javax.swing.JTextField jTexNombre;
    private javax.swing.JTextField jTexReferencia;
    // End of variables declaration//GEN-END:variables
}
