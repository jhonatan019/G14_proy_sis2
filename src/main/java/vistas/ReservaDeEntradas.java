/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vistas;

import baseDatos.ConexionBD;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Tommy
 */
public class ReservaDeEntradas extends javax.swing.JFrame {

    private Connection conn;
    
    public ReservaDeEntradas() {
        initComponents();
        setResizable(false);
        setTitle("Reserva de Entradas");
        setLocationRelativeTo(null);
        
        try {
            conn = ConexionBD.getConnection();
            cargarPeliculas();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos: " + e.getMessage());
        }
    }
    
    private void cargarPeliculas() {
        String sql = "SELECT NOMBRE_PELICULA FROM pelicula";
        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            cmbx_pelicula.removeAllItems();
            while (rs.next()) {
                cmbx_pelicula.addItem(rs.getString("NOMBRE_PELICULA"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar películas: " + e.getMessage());
        }
    }

    private void cargarHorarios(String nombrePelicula) {
    System.out.println("Intentando cargar horarios para película: " + nombrePelicula);
    
    // Primero verificamos si la película existe y obtenemos su ID
    String sqlVerificarPelicula = "SELECT ID_PELICULA FROM pelicula WHERE NOMBRE_PELICULA = ?";
    try (PreparedStatement pstVerificar = conn.prepareStatement(sqlVerificarPelicula)) {
        pstVerificar.setString(1, nombrePelicula);
        ResultSet rsVerificar = pstVerificar.executeQuery();
        
        if (rsVerificar.next()) {
            System.out.println("ID_PELICULA encontrado: " + rsVerificar.getInt("ID_PELICULA"));
        } else {
            System.out.println("No se encontró la película en la base de datos");
            return;
        }
    } catch (SQLException e) {
        System.out.println("Error al verificar película: " + e.getMessage());
        e.printStackTrace();
    }
    
    // Consulta principal con más detalles para debugging
    String sql = """
        SELECT f.FUNCION_INICIO, f.FECHA_FUNCION, p.NOMBRE_PELICULA 
        FROM funcion f 
        JOIN pelicula p ON f.ID_PELICULA = p.ID_PELICULA 
        WHERE p.NOMBRE_PELICULA = ? 
        AND f.FECHA_FUNCION = CURRENT_DATE
    """;
    
    try (PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setString(1, nombrePelicula);
        
        System.out.println("Ejecutando consulta SQL: " + pst.toString());
        ResultSet rs = pst.executeQuery();
        
        // Verificar la fecha actual en la base de datos
        try (Statement stmtDate = conn.createStatement()) {
            ResultSet rsDate = stmtDate.executeQuery("SELECT CURRENT_DATE");
            if (rsDate.next()) {
                System.out.println("Fecha actual en BD: " + rsDate.getString(1));
            }
        }
        
        cmbx_horario.removeAllItems();
        int contadorHorarios = 0;
        
        while (rs.next()) {
            String horario = rs.getString("FUNCION_INICIO");
            String fecha = rs.getString("FECHA_FUNCION");
            System.out.println("Horario encontrado: " + horario + " para fecha: " + fecha);
            cmbx_horario.addItem(horario);
            contadorHorarios++;
        }
        
        System.out.println("Total de horarios cargados: " + contadorHorarios);
        
        if (contadorHorarios == 0) {
            // Hacer una consulta adicional para verificar si hay funciones sin filtro de fecha
            String sqlTodas = """
                SELECT COUNT(*) as total, MIN(FECHA_FUNCION) as primera_fecha, MAX(FECHA_FUNCION) as ultima_fecha 
                FROM funcion f 
                JOIN pelicula p ON f.ID_PELICULA = p.ID_PELICULA 
                WHERE p.NOMBRE_PELICULA = ?
            """;
            
            try (PreparedStatement pstTodas = conn.prepareStatement(sqlTodas)) {
                pstTodas.setString(1, nombrePelicula);
                ResultSet rsTodas = pstTodas.executeQuery();
                
                if (rsTodas.next()) {
                    System.out.println("Total de funciones sin filtro de fecha: " + rsTodas.getInt("total"));
                    System.out.println("Rango de fechas: " + rsTodas.getString("primera_fecha") + 
                                     " hasta " + rsTodas.getString("ultima_fecha"));
                }
            }
        }
        
    } catch (SQLException e) {
        System.out.println("Error en la consulta principal: " + e.getMessage());
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar horarios: " + e.getMessage());
    }
}
    
    private void cargarSinopsisEImagen(String nombrePelicula) {
    String sql = "SELECT SIPNOPSIS_PELICULA, IMAGEN_PELICULA FROM pelicula WHERE NOMBRE_PELICULA = ?";
    try (PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setString(1, nombrePelicula);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            // Cargar sinopsis en el TextArea
            String sinopsis = rs.getString("SIPNOPSIS_PELICULA");
            jTextArea1.setText(sinopsis);
            
            // Cargar imagen si tienes un JLabel para mostrarla
            String rutaImagen = rs.getString("IMAGEN_PELICULA");
           

            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                try {
                    ImageIcon imagen = new ImageIcon(rutaImagen);
                    
                    // Asumiendo que tienes un JLabel llamado labelImagen
                    Image img = imagen.getImage().getScaledInstance(
                        labelImagen.getWidth(), 
                        labelImagen.getHeight(), 
                        Image.SCALE_SMOOTH
                    );
                    labelImagen.setIcon(new ImageIcon(img));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al cargar datos de la película: " + e.getMessage());
    }
}
    
    public void setDatosPelicula(String pelicula) {
      
        cmbx_pelicula.setSelectedItem(pelicula); // o txt_pelicula.setText(pelicula);
    }

    public void setDatosHorario(String horario) {
        cmbx_horario.setSelectedItem(horario); // o txt_horario.setText(horario);
    }


    public void setCantidadButacas(String cantidad) {
        cmbx_cantidadButacas.setSelectedItem(cantidad); // o el componente que uses
    }

    public void setDatosCliente(String nombre, String apellido, String ci) {
        txt_nombre.setText(nombre);
        txt_apellido.setText(apellido);
        txt_ci.setText(ci);
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
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cmbx_pelicula = new javax.swing.JComboBox<>();
        cmbx_horario = new javax.swing.JComboBox<>();
        cmbx_cantidadButacas = new javax.swing.JComboBox<>();
        txt_nombre = new javax.swing.JTextField();
        txt_apellido = new javax.swing.JTextField();
        txt_ci = new javax.swing.JTextField();
        btn_SeleccionarButacas = new javax.swing.JButton();
        btn_cancelarVenta = new javax.swing.JButton();
        labelImagen = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 204, 51));
        jPanel1.setPreferredSize(new java.awt.Dimension(1180, 800));

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 40)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("RESERVA DE ENTRADAS");

        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel2.setText("Pélicula:");

        jLabel3.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel3.setText("Horario:");

        jLabel4.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel4.setText("Cantidad de Butacas:");

        jLabel5.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel5.setText("Nombre Cliente:");

        jLabel6.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel6.setText("Apellido Cliente:");

        jLabel7.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel7.setText("C.I. :");

        cmbx_pelicula.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        cmbx_pelicula.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cmbx_pelicula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbx_peliculaActionPerformed(evt);
            }
        });

        cmbx_horario.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N

        cmbx_cantidadButacas.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        cmbx_cantidadButacas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5" }));
        cmbx_cantidadButacas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbx_cantidadButacasActionPerformed(evt);
            }
        });

        txt_nombre.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        txt_nombre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_nombre.setBorder(null);
        txt_nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nombreActionPerformed(evt);
            }
        });

        txt_apellido.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        txt_apellido.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txt_ci.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        txt_ci.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        btn_SeleccionarButacas.setBackground(new java.awt.Color(255, 51, 51));
        btn_SeleccionarButacas.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        btn_SeleccionarButacas.setText("Seleccionar Butacas");
        btn_SeleccionarButacas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_SeleccionarButacas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SeleccionarButacasActionPerformed(evt);
            }
        });

        btn_cancelarVenta.setBackground(new java.awt.Color(255, 51, 51));
        btn_cancelarVenta.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        btn_cancelarVenta.setText("Cancelar Venta");
        btn_cancelarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_cancelarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelarVentaActionPerformed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(8);
        jTextArea1.setTabSize(5);
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setFocusable(false);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_ci, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(198, 198, 198)
                                .addComponent(btn_SeleccionarButacas, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(41, 41, 41)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbx_horario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbx_pelicula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbx_cantidadButacas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btn_cancelarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cmbx_pelicula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(cmbx_horario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cmbx_cantidadButacas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(126, 126, 126)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(txt_apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addComponent(labelImagen, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txt_ci, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_SeleccionarButacas, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cancelarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(63, 63, 63))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbx_peliculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbx_peliculaActionPerformed
         String peliculaSeleccionada = cmbx_pelicula.getSelectedItem().toString();
        cargarHorarios(peliculaSeleccionada);
        cargarSinopsisEImagen(peliculaSeleccionada);
    }//GEN-LAST:event_cmbx_peliculaActionPerformed

    private void cmbx_cantidadButacasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbx_cantidadButacasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbx_cantidadButacasActionPerformed

    private void btn_SeleccionarButacasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SeleccionarButacasActionPerformed
    
    if (txt_nombre.getText().isEmpty() || txt_apellido.getText().isEmpty() || 
        txt_ci.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor complete todos los campos");
        return;
    }
    try {
        String sql = """
            SELECT f.ID_SALA, f.ASIENTOS_DISPONIBLES_FUNCION, f.ID_HORARIO 
            FROM funcion f 
            JOIN pelicula p ON p.ID_PELICULA = f.ID_PELICULA 
            WHERE p.NOMBRE_PELICULA = ? 
            AND f.FUNCION_INICIO = ?
            AND f.FECHA_FUNCION = CURRENT_DATE
        """;
        
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, cmbx_pelicula.getSelectedItem().toString());
        pst.setString(2, cmbx_horario.getSelectedItem().toString());
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            int idSala = rs.getInt("ID_SALA");
            int idHorario = rs.getInt("ID_HORARIO"); // Obtiene el ID_HORARIO
            int asientosDisponibles = Integer.parseInt(rs.getString("ASIENTOS_DISPONIBLES_FUNCION"));
            int cantidad = Integer.parseInt(cmbx_cantidadButacas.getSelectedItem().toString());
            
            if (cantidad > asientosDisponibles) {
                JOptionPane.showMessageDialog(this, 
                    "No hay suficientes asientos disponibles para esta función.");
                return;
            }
            
            double precio = 35.0;
            
            String nombreCliente = txt_nombre.getText();
            String apellidoCliente = txt_apellido.getText();
            String ci = txt_ci.getText();
            
            dispose();
            Butacas but = new Butacas(
                cantidad, 
                precio, 
                cmbx_pelicula.getSelectedItem().toString(),
                String.valueOf(idSala),
                cmbx_horario.getSelectedItem().toString(),
                String.valueOf(idHorario), // Ahora pasamos ID_HORARIO
                
                nombreCliente, 
                apellidoCliente, 
                ci
            );
            but.setVisible(true);
            but.setLocationRelativeTo(null);
        } else {
            JOptionPane.showMessageDialog(this, 
                "No se encontró la función para la película y horario seleccionados.");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al obtener datos: " + e.getMessage());
    }
        
    }//GEN-LAST:event_btn_SeleccionarButacasActionPerformed

    private void txt_nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nombreActionPerformed

    private void btn_cancelarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarVentaActionPerformed
    dispose();
    }//GEN-LAST:event_btn_cancelarVentaActionPerformed

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
            java.util.logging.Logger.getLogger(ReservaDeEntradas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReservaDeEntradas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReservaDeEntradas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReservaDeEntradas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReservaDeEntradas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_SeleccionarButacas;
    private javax.swing.JButton btn_cancelarVenta;
    private javax.swing.JComboBox<String> cmbx_cantidadButacas;
    private javax.swing.JComboBox<String> cmbx_horario;
    private javax.swing.JComboBox<String> cmbx_pelicula;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel labelImagen;
    private javax.swing.JTextField txt_apellido;
    private javax.swing.JTextField txt_ci;
    private javax.swing.JTextField txt_nombre;
    // End of variables declaration//GEN-END:variables
}
