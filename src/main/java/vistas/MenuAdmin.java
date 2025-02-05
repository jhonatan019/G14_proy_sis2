/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vistas;

/**
 *
 * @author Tommy
 */
public class MenuAdmin extends javax.swing.JFrame {

    /**
     * Creates new form MenuAdmin
     */
    public MenuAdmin() {
        initComponents();
        setResizable(false);
        setTitle("Menú Administrador");
        setLocationRelativeTo(null);
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
        buttonAsignarHoararios = new javax.swing.JButton();
        buttonAgregarPeli = new javax.swing.JButton();
        buttonEstablecerSalas = new javax.swing.JButton();
        buttonRegistrarSucur = new javax.swing.JButton();
        buttonRegistrarPeli = new javax.swing.JButton();
        buttonRegistrarEmpleado = new javax.swing.JButton();
        buttonReportes = new javax.swing.JButton();
        buttonCerrarSesionAd = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(39, 29, 41));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        buttonAsignarHoararios.setBackground(new java.awt.Color(253, 185, 19));
        buttonAsignarHoararios.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        buttonAsignarHoararios.setText("ASIGNAR HORARIOS");
        buttonAsignarHoararios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAsignarHoarariosActionPerformed(evt);
            }
        });
        jPanel1.add(buttonAsignarHoararios, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 380, 354, 70));

        buttonAgregarPeli.setBackground(new java.awt.Color(253, 185, 19));
        buttonAgregarPeli.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        buttonAgregarPeli.setText("AGREGAR PELICULA");
        buttonAgregarPeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAgregarPeliActionPerformed(evt);
            }
        });
        jPanel1.add(buttonAgregarPeli, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 300, 354, 70));

        buttonEstablecerSalas.setBackground(new java.awt.Color(253, 185, 19));
        buttonEstablecerSalas.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        buttonEstablecerSalas.setText("ESTABLECER SALAS");
        buttonEstablecerSalas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEstablecerSalasActionPerformed(evt);
            }
        });
        jPanel1.add(buttonEstablecerSalas, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 220, 354, 70));

        buttonRegistrarSucur.setBackground(new java.awt.Color(253, 185, 19));
        buttonRegistrarSucur.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        buttonRegistrarSucur.setText("REGISTRAR SUCURSAL");
        buttonRegistrarSucur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRegistrarSucurActionPerformed(evt);
            }
        });
        jPanel1.add(buttonRegistrarSucur, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 140, 354, 70));

        buttonRegistrarPeli.setBackground(new java.awt.Color(253, 185, 19));
        buttonRegistrarPeli.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        buttonRegistrarPeli.setText("REGISTRAR PELICULA");
        buttonRegistrarPeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRegistrarPeliActionPerformed(evt);
            }
        });
        jPanel1.add(buttonRegistrarPeli, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 60, 354, 70));

        buttonRegistrarEmpleado.setBackground(new java.awt.Color(253, 185, 19));
        buttonRegistrarEmpleado.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        buttonRegistrarEmpleado.setText("REGISTRO DE EMPLEADOS");
        buttonRegistrarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRegistrarEmpleadoActionPerformed(evt);
            }
        });
        jPanel1.add(buttonRegistrarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 460, 354, 70));

        buttonReportes.setBackground(new java.awt.Color(253, 185, 19));
        buttonReportes.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        buttonReportes.setText("REPORTES");
        buttonReportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonReportesActionPerformed(evt);
            }
        });
        jPanel1.add(buttonReportes, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 540, 354, 70));

        buttonCerrarSesionAd.setBackground(new java.awt.Color(253, 185, 19));
        buttonCerrarSesionAd.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        buttonCerrarSesionAd.setText("CERRAR SESION");
        buttonCerrarSesionAd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCerrarSesionAdActionPerformed(evt);
            }
        });
        jPanel1.add(buttonCerrarSesionAd, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 620, 354, 70));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Calibri", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ADMINISTRADOR");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, 360, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Letter - 45.jpg"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 1113, 685));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1250, 750));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAsignarHoarariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAsignarHoarariosActionPerformed
    AsignarHorario asignar = new AsignarHorario();
    asignar.setVisible(true);
    this.setVisible(false);
    }//GEN-LAST:event_buttonAsignarHoarariosActionPerformed

    private void buttonAgregarPeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAgregarPeliActionPerformed

        SucursalPelicula selecPeli = new  SucursalPelicula();
        selecPeli.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_buttonAgregarPeliActionPerformed

    private void buttonEstablecerSalasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEstablecerSalasActionPerformed
    SeleccionarSucursal seleccionar = new SeleccionarSucursal();
    seleccionar.setVisible(true);
    this.setVisible(false);
    }//GEN-LAST:event_buttonEstablecerSalasActionPerformed

    private void buttonRegistrarSucurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRegistrarSucurActionPerformed
    CrearSucursal sucursal = new CrearSucursal();
    sucursal.setVisible(true);
    this.setVisible(false);
    }//GEN-LAST:event_buttonRegistrarSucurActionPerformed

    private void buttonRegistrarPeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRegistrarPeliActionPerformed

        PanelRegistrarPelicula panelPelicula = new PanelRegistrarPelicula();
        panelPelicula.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_buttonRegistrarPeliActionPerformed

    private void buttonRegistrarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRegistrarEmpleadoActionPerformed
    RegistroEmpleados registro = new RegistroEmpleados();
    registro.setVisible(true);
    this.setVisible(false);
    }//GEN-LAST:event_buttonRegistrarEmpleadoActionPerformed

    private void buttonReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReportesActionPerformed
    ReservaDeEntradas reservaEntrada = new ReservaDeEntradas();
    reservaEntrada.setVisible(true);
    this.setVisible(false);
    }//GEN-LAST:event_buttonReportesActionPerformed

    private void buttonCerrarSesionAdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCerrarSesionAdActionPerformed
    dispose();
    }//GEN-LAST:event_buttonCerrarSesionAdActionPerformed

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
            java.util.logging.Logger.getLogger(MenuAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAgregarPeli;
    private javax.swing.JButton buttonAsignarHoararios;
    private javax.swing.JButton buttonCerrarSesionAd;
    private javax.swing.JButton buttonEstablecerSalas;
    private javax.swing.JButton buttonRegistrarEmpleado;
    private javax.swing.JButton buttonRegistrarPeli;
    private javax.swing.JButton buttonRegistrarSucur;
    private javax.swing.JButton buttonReportes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
