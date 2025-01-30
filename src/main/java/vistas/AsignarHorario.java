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
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Tommy
 */
public class AsignarHorario extends javax.swing.JFrame {

    private Connection conexion;
    private DefaultTableModel modeloTabla;
    
    public AsignarHorario() {
    initComponents();
    setResizable(false);
    setTitle("Asignar Horarios");
    setLocationRelativeTo(null);
    
    // Agregar el evento de clic en la tabla
    jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jTable1MouseClicked(evt);
        }
    });
    
    try {
        conexion = ConexionBD.getConnection();
        configurarTabla();
        cargarPeliculas();
        cargarSalas();
        cargarFunciones();
    } catch (SQLException e) {
        mostrarError("Error de conexión: " + e.getMessage());
    }
}
    
    private void configurarTabla() {
    modeloTabla = (DefaultTableModel) jTable1.getModel();
    modeloTabla.setRowCount(0);
    modeloTabla.setColumnCount(0);
    
    // Configurar las columnas en el orden correcto
    modeloTabla.addColumn("ID");
    modeloTabla.addColumn("Película");
    modeloTabla.addColumn("Fecha");
    modeloTabla.addColumn("Hora Inicio");
    modeloTabla.addColumn("Sala");
    modeloTabla.addColumn("Disponibilidad");
    modeloTabla.addColumn("Estado");
}
    
    private void cargarPeliculas() {
        combo_pelicula.removeAllItems();
        combo_pelicula.addItem("Seleccione una película");
        
        String sql = "SELECT ID_PELICULA, NOMBRE_PELICULA FROM pelicula";
        try (PreparedStatement pst = conexion.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String item = rs.getInt("ID_PELICULA") + " - " + rs.getString("NOMBRE_PELICULA");
                combo_pelicula.addItem(item);
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar películas: " + e.getMessage());
        }
    }
    
    private void cargarSalas() {
        combo_sala.removeAllItems();
        combo_sala.addItem("Seleccione una sala");
        
        String sql = "SELECT ID_SALA, NUMERO_SALA FROM sala WHERE ESTADO_SALA = 1";
        try (PreparedStatement pst = conexion.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String item = rs.getInt("ID_SALA") + " - Sala " + rs.getString("NUMERO_SALA");
                combo_sala.addItem(item);
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar salas: " + e.getMessage());
        }
    }
    
    private void cargarFunciones() {
    modeloTabla.setRowCount(0);
    
    String sql = "SELECT f.ID_HORARIO, p.NOMBRE_PELICULA, f.FECHA_FUNCION, " +
                "f.FUNCION_INICIO, s.NUMERO_SALA, f.ASIENTOS_DISPONIBLES_FUNCION, " +
                "CASE WHEN f.FECHA_FUNCION >= CURRENT_DATE THEN 'Activa' ELSE 'Finalizada' END as ESTADO " +
                "FROM funcion f " +
                "INNER JOIN pelicula p ON f.ID_PELICULA = p.ID_PELICULA " +
                "INNER JOIN sala s ON f.ID_SALA = s.ID_SALA " +
                "ORDER BY f.FECHA_FUNCION DESC, f.FUNCION_INICIO DESC";
                
    try (PreparedStatement pst = conexion.prepareStatement(sql)) {
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Object[] fila = {
                rs.getInt("ID_HORARIO"),
                rs.getString("NOMBRE_PELICULA"),
                rs.getDate("FECHA_FUNCION"),
                rs.getTime("FUNCION_INICIO"),
                rs.getString("NUMERO_SALA"),
                rs.getString("ASIENTOS_DISPONIBLES_FUNCION"),
                rs.getString("ESTADO")
            };
            modeloTabla.addRow(fila);
        }
    } catch (SQLException e) {
        mostrarError("Error al cargar funciones: " + e.getMessage());
    }
}
    
    private int obtenerCapacidadSala(int idSala) throws SQLException {
        String sql = "SELECT CAPACIDAD_SALA FROM sala WHERE ID_SALA = ?";
        try (PreparedStatement pst = conexion.prepareStatement(sql)) {
            pst.setInt(1, idSala);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("CAPACIDAD_SALA");
            }
        }
        return 0;
    }
    
    private boolean validarCampos() {
        if (combo_pelicula.getSelectedIndex() == 0) {
            mostrarError("Seleccione una película");
            return false;
        }
        
        if (combo_sala.getSelectedIndex() == 0) {
            mostrarError("Seleccione una sala");
            return false;
        }
        
        if (txt_fecha.getText().trim().isEmpty()) {
            mostrarError("Ingrese una fecha");
            return false;
        }
        
        if (txt_horario.getText().trim().isEmpty()) {
            mostrarError("Ingrese un horario");
            return false;
        }
        
        // Validar formato de fecha (YYYY-MM-DD)
        try {
            java.sql.Date.valueOf(txt_fecha.getText());
        } catch (IllegalArgumentException e) {
            mostrarError("Formato de fecha incorrecto. Use: YYYY-MM-DD");
            return false;
        }
        
        // Validar formato de hora (HH:MM)
        try {
            java.sql.Time.valueOf(txt_horario.getText() + ":00");
        } catch (IllegalArgumentException e) {
            mostrarError("Formato de hora incorrecto. Use: HH:MM");
            return false;
        }
        
        return true;
    }
    
    private void limpiarCampos() {
        combo_pelicula.setSelectedIndex(0);
        combo_sala.setSelectedIndex(0);
        txt_fecha.setText("");
        txt_horario.setText("");
    }
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
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
    
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
    int fila = jTable1.getSelectedRow();
    if(fila >= 0) {
        cargarDatosFuncion(fila);
    }
}
    
    private void cargarDatosFuncion(int fila) {
    try {
        int idHorario = (int) jTable1.getValueAt(fila, 0);
        String sql = "SELECT f.ID_PELICULA, f.ID_SALA, f.FECHA_FUNCION, " +
                    "f.FUNCION_INICIO FROM funcion f WHERE f.ID_HORARIO = ?";
        
        PreparedStatement pst = conexion.prepareStatement(sql);
        pst.setInt(1, idHorario);
        ResultSet rs = pst.executeQuery();
        
        if(rs.next()) {
            // Seleccionar la película en el combo
            for(int i = 0; i < combo_pelicula.getItemCount(); i++) {
                String item = combo_pelicula.getItemAt(i);
                if(item.startsWith(rs.getInt("ID_PELICULA") + " - ")) {
                    combo_pelicula.setSelectedIndex(i);
                    break;
                }
            }
            
            // Seleccionar la sala en el combo
            for(int i = 0; i < combo_sala.getItemCount(); i++) {
                String item = combo_sala.getItemAt(i);
                if(item.startsWith(rs.getInt("ID_SALA") + " - ")) {
                    combo_sala.setSelectedIndex(i);
                    break;
                }
            }
            
            // Establecer fecha y hora
            txt_fecha.setText(rs.getDate("FECHA_FUNCION").toString());
            txt_horario.setText(rs.getTime("FUNCION_INICIO").toString().substring(0, 5));
        }
    } catch (SQLException e) {
        mostrarError("Error al cargar datos de la función: " + e.getMessage());
    }
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
        jLabel4 = new javax.swing.JLabel();
        combo_pelicula = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txt_horario = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_fecha = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        combo_sala = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btn_atras = new javax.swing.JButton();
        btn_modificar = new javax.swing.JButton();
        btn_eliminar = new javax.swing.JButton();
        btn_guardarFuncion = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(39, 29, 41));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("ASIGNAR HORARIOS");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 170, 767, 50));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("PELICULA :");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 290, 130, 32));

        combo_pelicula.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(combo_pelicula, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 290, 186, 25));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("HORARIO :");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 340, 116, 24));
        jPanel1.add(txt_horario, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 340, 190, 25));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("FECHA :");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 290, 96, 24));
        jPanel1.add(txt_fecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 290, 190, 25));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("SALA :");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 290, 66, 32));

        combo_sala.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(combo_sala, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 290, 90, 25));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Pelicula", "Fecha", "Hora", "Sala", "Disponibilidad", "Estado"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 380, 750, 210));

        btn_atras.setBackground(new java.awt.Color(230, 12, 16));
        btn_atras.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_atras.setForeground(new java.awt.Color(255, 255, 255));
        btn_atras.setText("Atras");
        btn_atras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_atrasActionPerformed(evt);
            }
        });
        jPanel1.add(btn_atras, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 640, 164, 47));

        btn_modificar.setBackground(new java.awt.Color(230, 12, 16));
        btn_modificar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_modificar.setForeground(new java.awt.Color(255, 255, 255));
        btn_modificar.setText("Modificar");
        btn_modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_modificarActionPerformed(evt);
            }
        });
        jPanel1.add(btn_modificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 640, 164, 47));

        btn_eliminar.setBackground(new java.awt.Color(230, 12, 16));
        btn_eliminar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_eliminar.setForeground(new java.awt.Color(255, 255, 255));
        btn_eliminar.setText("Eliminar");
        btn_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminarActionPerformed(evt);
            }
        });
        jPanel1.add(btn_eliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 640, 164, 47));

        btn_guardarFuncion.setBackground(new java.awt.Color(230, 12, 16));
        btn_guardarFuncion.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_guardarFuncion.setForeground(new java.awt.Color(255, 255, 255));
        btn_guardarFuncion.setText("Guardar Funcion");
        btn_guardarFuncion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarFuncionActionPerformed(evt);
            }
        });
        jPanel1.add(btn_guardarFuncion, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 640, 240, 47));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/WhatsApp Image 2025-01-27 at 9.10.20 PM.jpeg"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1210, 770));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1210, 770));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_atrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_atrasActionPerformed
    MenuAdmin adminMenu = new MenuAdmin();
    adminMenu.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_btn_atrasActionPerformed

    private void btn_guardarFuncionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarFuncionActionPerformed
    if (!validarCampos()) return;
        
        try {
            // Obtener IDs de los combos
            String[] peliculaPartes = combo_pelicula.getSelectedItem().toString().split(" - ");
            String[] salaPartes = combo_sala.getSelectedItem().toString().split(" - ");
            int idPelicula = Integer.parseInt(peliculaPartes[0]);
            int idSala = Integer.parseInt(salaPartes[0]);
            
            // Preparar los datos de la función
            String sql = "INSERT INTO funcion (ID_PELICULA, ID_SALA, FECHA_FUNCION, " +
                        "FUNCION_INICIO, FUNCION_FIN, ASIENTOS_DISPONIBLES_FUNCION, " +
                        "ASIENTOS_TOTALES_FUNCION) VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement pst = conexion.prepareStatement(sql)) {
                // Convertir fecha y hora
                java.sql.Date fecha = java.sql.Date.valueOf(txt_fecha.getText());
                java.sql.Time horaInicio = java.sql.Time.valueOf(txt_horario.getText() + ":00");
                
                // Calcular hora de fin (asumiendo 2 horas de duración)
                Calendar cal = Calendar.getInstance();
                cal.setTime(horaInicio);
                cal.add(Calendar.HOUR, 2);
                java.sql.Time horaFin = new java.sql.Time(cal.getTimeInMillis());
                
                // Obtener capacidad total de la sala
                int capacidadSala = obtenerCapacidadSala(idSala);
                
                pst.setInt(1, idPelicula);
                pst.setInt(2, idSala);
                pst.setDate(3, fecha);
                pst.setTime(4, horaInicio);
                pst.setTime(5, horaFin);
                pst.setString(6, String.valueOf(capacidadSala));
                pst.setInt(7, capacidadSala);
                
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Función registrada exitosamente");
                limpiarCampos();
                cargarFunciones();
            }
        } catch (SQLException e) {
            mostrarError("Error al guardar la función: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_guardarFuncionActionPerformed

    private void btn_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modificarActionPerformed
     int filaSeleccionada = jTable1.getSelectedRow();
    if (filaSeleccionada == -1) {
        mostrarError("Seleccione una función para modificar");
        return;
    }
    
    if (!validarCampos()) return;
    
    try {
        int idHorario = (int) jTable1.getValueAt(filaSeleccionada, 0);
        String[] peliculaPartes = combo_pelicula.getSelectedItem().toString().split(" - ");
        String[] salaPartes = combo_sala.getSelectedItem().toString().split(" - ");
        int idPelicula = Integer.parseInt(peliculaPartes[0]);
        int idSala = Integer.parseInt(salaPartes[0]);
        
        String sql = "UPDATE funcion SET ID_PELICULA = ?, ID_SALA = ?, " +
                    "FECHA_FUNCION = ?, FUNCION_INICIO = ? " +
                    "WHERE ID_HORARIO = ?";
        
        PreparedStatement pst = conexion.prepareStatement(sql);
        pst.setInt(1, idPelicula);
        pst.setInt(2, idSala);
        pst.setDate(3, java.sql.Date.valueOf(txt_fecha.getText()));
        pst.setTime(4, java.sql.Time.valueOf(txt_horario.getText() + ":00"));
        pst.setInt(5, idHorario);
        
        int resultado = pst.executeUpdate();
        if(resultado > 0) {
            JOptionPane.showMessageDialog(this, 
                "Función modificada exitosamente", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            cargarFunciones();
            limpiarCampos();
        }
        
    } catch (SQLException e) {
        mostrarError("Error al modificar la función: " + e.getMessage());
    }
    }//GEN-LAST:event_btn_modificarActionPerformed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
   int filaSeleccionada = jTable1.getSelectedRow();
    if (filaSeleccionada == -1) {
        mostrarError("Seleccione una función para eliminar");
        return;
    }
    
    int idHorario = (int) jTable1.getValueAt(filaSeleccionada, 0);
    
    if (JOptionPane.showConfirmDialog(this, 
        "¿Está seguro de eliminar esta función?", 
        "Confirmar eliminación", 
        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        
        try {
            // Primero eliminamos la función
            String sqlDelete = "DELETE FROM funcion WHERE ID_HORARIO = ?";
            try (PreparedStatement pst = conexion.prepareStatement(sqlDelete)) {
                pst.setInt(1, idHorario);
                pst.executeUpdate();
                
                // Ahora reiniciamos el auto-incremento
                String sqlReset = "ALTER TABLE funcion AUTO_INCREMENT = 1";
                try (PreparedStatement pstReset = conexion.prepareStatement(sqlReset)) {
                    pstReset.executeUpdate();
                }
                
                JOptionPane.showMessageDialog(this, "Función eliminada exitosamente");
                cargarFunciones();
            }
        } catch (SQLException e) {
            mostrarError("Error al eliminar la función: " + e.getMessage());
        }
    }
    }//GEN-LAST:event_btn_eliminarActionPerformed

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
            java.util.logging.Logger.getLogger(AsignarHorario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AsignarHorario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AsignarHorario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AsignarHorario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AsignarHorario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_atras;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_guardarFuncion;
    private javax.swing.JButton btn_modificar;
    private javax.swing.JComboBox<String> combo_pelicula;
    private javax.swing.JComboBox<String> combo_sala;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txt_fecha;
    private javax.swing.JTextField txt_horario;
    // End of variables declaration//GEN-END:variables
}
