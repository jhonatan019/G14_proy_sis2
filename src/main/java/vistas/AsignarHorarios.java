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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class AsignarHorarios extends javax.swing.JFrame {
   
    private Connection conexion;
    private DefaultTableModel modeloTabla;
    
    public AsignarHorarios() {
        initComponents();
        
        setResizable(false);
        setTitle("Asignar Horarios");
        setLocationRelativeTo(null);
        configurarTabla();
        cargarCombos();
    }
    
    private void configurarTabla() {
    String[] columnas = {"ID", "Película", "Fecha", "Hora Inicio", "Hora Fin", "Sala"};
    modeloTabla = new DefaultTableModel(columnas, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    tabla.setModel(modeloTabla);
    cargarFunciones();
}


    private void cargarCombos() {
        cargarPeliculas();
        cargarSalas();
        cargarSucursales();
    }

    private void cargarPeliculas() {
        String sql = "SELECT ID_PELICULA, NOMBRE_PELICULA FROM pelicula";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            combo_pelicula.removeAllItems();
            while (rs.next()) {
                combo_pelicula.addItem(new Item(rs.getInt("ID_PELICULA"), 
                                              rs.getString("NOMBRE_PELICULA")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar películas: " + e.getMessage());
        }
    }

    private void cargarSalas() {
        String sql = "SELECT ID_SALA, NUMERO_SALA FROM sala";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            combo_sala.removeAllItems();
            while (rs.next()) {
                combo_sala.addItem(new Item(rs.getInt("ID_SALA"), 
                                          "Sala " + rs.getString("NUMERO_SALA")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar salas: " + e.getMessage());
        }
    }

    private void cargarSucursales() {
        String sql = "SELECT ID_SUCURSAL, NOMBRE_SUCURSAL FROM sucursal";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            combo_sucursal.removeAllItems();
            while (rs.next()) {
                combo_sucursal.addItem(new Item(rs.getInt("ID_SUCURSAL"), 
                                              rs.getString("NOMBRE_SUCURSAL")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar sucursales: " + e.getMessage());
        }
    }

    private void cargarFunciones() {
    modeloTabla.setRowCount(0);
    String sql = "SELECT f.ID_HORARIO, p.NOMBRE_PELICULA, f.FECHA_FUNCION, " +
                "f.FUNCION_INICIO, f.FUNCION_FIN, s.NUMERO_SALA " +
                "FROM funcion f " +
                "JOIN pelicula p ON f.ID_PELICULA = p.ID_PELICULA " +
                "JOIN sala s ON f.ID_SALA = s.ID_SALA";
    
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            modeloTabla.addRow(new Object[]{
                rs.getInt("ID_HORARIO"),
                rs.getString("NOMBRE_PELICULA"),
                rs.getDate("FECHA_FUNCION"),
                rs.getTime("FUNCION_INICIO"),
                rs.getTime("FUNCION_FIN"), 
                rs.getString("NUMERO_SALA")
            });
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}
  private boolean validarCampos() {
       if (combo_pelicula.getSelectedIndex() == -1 ||
           combo_sala.getSelectedIndex() == -1 ||
           combo_sucursal.getSelectedIndex() == -1 ||
           txt_fecha.getText().trim().isEmpty() ||
           txt_horario_inicio.getText().trim().isEmpty() ||
           txt_horario_fin.getText().trim().isEmpty()) {
           JOptionPane.showMessageDialog(this, "Complete todos los campos");
           return false;
       }
       if (!txt_horario_inicio.getText().matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$") ||
        !txt_horario_fin.getText().matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
        JOptionPane.showMessageDialog(this, "Formato de hora inválido. Use HH:mm");
        return false;
    }
       try {
           LocalDate.parse(txt_fecha.getText());
           LocalTime.parse(txt_horario_inicio.getText());
           LocalTime.parse(txt_horario_fin.getText());
       } catch (DateTimeParseException e) {
           JOptionPane.showMessageDialog(this, "Formato de fecha u hora inválido");
           return false;
       }
       return true;
   }


     private void limpiarCampos() {
       txt_fecha.setText("");
       txt_horario_inicio.setText("");
       txt_horario_fin.setText("");
       combo_pelicula.setSelectedIndex(0);
       combo_sala.setSelectedIndex(0);
       combo_sucursal.setSelectedIndex(0);
       tabla.clearSelection();
   }

     private void resetearCampos() {
   txt_fecha.setText("");
   txt_horario_inicio.setText("");
   txt_horario_fin.setText("");
   combo_pelicula.setSelectedIndex(0);
   combo_sala.setSelectedIndex(0);
   combo_sucursal.setSelectedIndex(0);
   tabla.clearSelection();
}
   
  private boolean existeFuncionDuplicada(int idSala, String fecha, String horaInicio, String horaFin) {
   String sql = "SELECT COUNT(*) FROM funcion WHERE ID_SALA = ? AND FECHA_FUNCION = ? " +
               "AND ((FUNCION_INICIO BETWEEN ? AND ?) OR (FUNCION_FIN BETWEEN ? AND ?))";
   try (Connection conn = ConexionBD.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
       ps.setInt(1, idSala);
       ps.setDate(2, java.sql.Date.valueOf(fecha));
       ps.setTime(3, java.sql.Time.valueOf(horaInicio + ":00"));
       ps.setTime(4, java.sql.Time.valueOf(horaFin + ":00"));
       ps.setTime(5, java.sql.Time.valueOf(horaInicio + ":00"));
       ps.setTime(6, java.sql.Time.valueOf(horaFin + ":00"));
       ResultSet rs = ps.executeQuery();
       rs.next();
       return rs.getInt(1) > 0;
   } catch (SQLException e) {
       JOptionPane.showMessageDialog(null, "Error al validar duplicados: " + e.getMessage());
       return true;
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
        jLabel3 = new javax.swing.JLabel();
        combo_pelicula = new javax.swing.JComboBox<>();
        txt_horario_inicio = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_fecha = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        combo_sala = new javax.swing.JComboBox<>();
        btn_atras = new javax.swing.JButton();
        btn_modificar = new javax.swing.JButton();
        btn_eliminar = new javax.swing.JButton();
        btn_guardarFuncion = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        combo_sucursal = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        txt_horario_fin = new javax.swing.JTextField();
        nuevo = new javax.swing.JButton();
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
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 130, 767, 50));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("PELICULA :");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, 130, 32));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("HORARIO INICIO:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, 210, 24));

        combo_pelicula.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        combo_pelicula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_peliculaActionPerformed(evt);
            }
        });
        jPanel1.add(combo_pelicula, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 250, 190, 40));

        txt_horario_inicio.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt_horario_inicio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_horario_inicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_horario_inicioActionPerformed(evt);
            }
        });
        jPanel1.add(txt_horario_inicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 310, 190, 40));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("FECHA :");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 260, 96, 24));

        txt_fecha.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt_fecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txt_fecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 250, 190, 40));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("SALA :");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 250, 90, 32));

        combo_sala.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        combo_sala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_salaActionPerformed(evt);
            }
        });
        jPanel1.add(combo_sala, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 250, 90, 28));

        btn_atras.setBackground(new java.awt.Color(230, 12, 16));
        btn_atras.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_atras.setForeground(new java.awt.Color(255, 255, 255));
        btn_atras.setText("Atras");
        btn_atras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_atrasActionPerformed(evt);
            }
        });
        jPanel1.add(btn_atras, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 710, 164, 47));

        btn_modificar.setBackground(new java.awt.Color(230, 12, 16));
        btn_modificar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_modificar.setForeground(new java.awt.Color(255, 255, 255));
        btn_modificar.setText("Modificar");
        btn_modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_modificarActionPerformed(evt);
            }
        });
        jPanel1.add(btn_modificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 710, 164, 47));

        btn_eliminar.setBackground(new java.awt.Color(230, 12, 16));
        btn_eliminar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_eliminar.setForeground(new java.awt.Color(255, 255, 255));
        btn_eliminar.setText("Eliminar");
        btn_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminarActionPerformed(evt);
            }
        });
        jPanel1.add(btn_eliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 710, 164, 47));

        btn_guardarFuncion.setBackground(new java.awt.Color(230, 12, 16));
        btn_guardarFuncion.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_guardarFuncion.setForeground(new java.awt.Color(255, 255, 255));
        btn_guardarFuncion.setText("Guardar Funcion");
        btn_guardarFuncion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarFuncionActionPerformed(evt);
            }
        });
        jPanel1.add(btn_guardarFuncion, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 710, 240, 47));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("SUCURSAL:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 320, 140, 20));

        combo_sucursal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        combo_sucursal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_sucursalActionPerformed(evt);
            }
        });
        jPanel1.add(combo_sucursal, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 310, 190, 40));

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Pelicula", "Fecha", "Hora", "Sala"
            }
        ));
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabla);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 410, 870, 230));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("HORARIO FIN:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 370, 170, -1));

        txt_horario_fin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt_horario_fin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_horario_fin.setToolTipText("");
        jPanel1.add(txt_horario_fin, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 360, 190, 40));

        nuevo.setBackground(new java.awt.Color(230, 12, 16));
        nuevo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        nuevo.setForeground(new java.awt.Color(255, 255, 255));
        nuevo.setText("Nuevo");
        nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoActionPerformed(evt);
            }
        });
        jPanel1.add(nuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 710, 130, 47));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/LOGOIZQUIERDA.jpg"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 770, 640));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1260, 836));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_atrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_atrasActionPerformed
     /*   MenuAdmin adminMenu = new MenuAdmin();
        adminMenu.setVisible(true);
        this.dispose();*/
    }//GEN-LAST:event_btn_atrasActionPerformed

    private void btn_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modificarActionPerformed
       int row = tabla.getSelectedRow();
   if (row == -1) {
       JOptionPane.showMessageDialog(this, "Seleccione una función para modificar");
       return;
   }

   if (!validarCampos()) return;
   
   int idSala = ((Item)combo_sala.getSelectedItem()).getId();
   int idHorario = (int)tabla.getValueAt(row, 0);
   String fecha = txt_fecha.getText();
   String horaInicio = txt_horario_inicio.getText().trim();
   String horaFin = txt_horario_fin.getText().trim();

   String sql = "UPDATE funcion SET ID_PELICULA = ?, ID_SALA = ?, FECHA_FUNCION = ?, " +
               "FUNCION_INICIO = ?, FUNCION_FIN = ? WHERE ID_HORARIO = ?";
               
   try (Connection conn = ConexionBD.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
       ps.setInt(1, ((Item)combo_pelicula.getSelectedItem()).getId());
       ps.setInt(2, idSala);
       ps.setDate(3, java.sql.Date.valueOf(fecha));
       ps.setTime(4, java.sql.Time.valueOf(horaInicio + ":00"));
       ps.setTime(5, java.sql.Time.valueOf(horaFin + ":00"));
       ps.setInt(6, idHorario);
       ps.executeUpdate();
       cargarFunciones();
       resetearCampos();
       JOptionPane.showMessageDialog(this, "Función modificada exitosamente");
   } catch (SQLException e) {
       JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage());
   }
    }//GEN-LAST:event_btn_modificarActionPerformed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
       int row = tabla.getSelectedRow();
   if (row == -1) {
       JOptionPane.showMessageDialog(this, "Seleccione una función para eliminar");
       return;
   }
   
   if (JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar esta función?", 
       "Confirmar eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
       
       String sql = "DELETE FROM funcion WHERE ID_HORARIO = ?";
       
       try (Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
           ps.setInt(1, (int)tabla.getValueAt(row, 0));
           ps.executeUpdate();
           cargarFunciones();
           resetearCampos();
           JOptionPane.showMessageDialog(this, "Función eliminada exitosamente");
       } catch (SQLException e) {
           JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
       }
   }
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void btn_guardarFuncionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarFuncionActionPerformed
     if (!validarCampos()) return;
   
   int idSala = ((Item)combo_sala.getSelectedItem()).getId();
   String fecha = txt_fecha.getText();
   String horaInicio = txt_horario_inicio.getText().trim();
   String horaFin = txt_horario_fin.getText().trim();
   
   if (existeFuncionDuplicada(idSala, fecha, horaInicio, horaFin)) {
       JOptionPane.showMessageDialog(this, "Ya existe una función en ese horario para esta sala");
       return;
   }

   String sql = "INSERT INTO funcion (ID_PELICULA, ID_SALA, FECHA_FUNCION, FUNCION_INICIO, " +
               "FUNCION_FIN, ASIENTOS_DISPONIBLES_FUNCION, ASIENTOS_TOTALES_FUNCION) " +
               "SELECT ?, ?, ?, ?, ?, CAPACIDAD_SALA, CAPACIDAD_SALA FROM sala WHERE ID_SALA = ?";
               
   try (Connection conn = ConexionBD.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
       ps.setInt(1, ((Item)combo_pelicula.getSelectedItem()).getId());
       ps.setInt(2, idSala);
       ps.setDate(3, java.sql.Date.valueOf(fecha));
       ps.setTime(4, java.sql.Time.valueOf(horaInicio + ":00"));
       ps.setTime(5, java.sql.Time.valueOf(horaFin + ":00"));
       ps.setInt(6, idSala);
       ps.executeUpdate();
       cargarFunciones();
       resetearCampos();
       JOptionPane.showMessageDialog(this, "Función guardada exitosamente");
   } catch (SQLException e) {
       JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
   }
    }//GEN-LAST:event_btn_guardarFuncionActionPerformed

    private void combo_salaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_salaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_combo_salaActionPerformed

    private void txt_horario_inicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_horario_inicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_horario_inicioActionPerformed

    private void combo_sucursalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_sucursalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_combo_sucursalActionPerformed

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
      int row = tabla.getSelectedRow();
       if (row != -1) {
           txt_fecha.setText(tabla.getValueAt(row, 2).toString());
           txt_horario_inicio.setText(tabla.getValueAt(row, 3).toString());
           txt_horario_fin.setText(tabla.getValueAt(row, 4).toString());
       }
   
    }//GEN-LAST:event_tablaMouseClicked

    private void nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoActionPerformed

    private void combo_peliculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_peliculaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_combo_peliculaActionPerformed

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
            java.util.logging.Logger.getLogger(AsignarHorarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AsignarHorarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AsignarHorarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AsignarHorarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AsignarHorarios().setVisible(true);
            }
        });
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_atras;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_guardarFuncion;
    private javax.swing.JButton btn_modificar;
    private javax.swing.JComboBox<Item> combo_pelicula;
    private javax.swing.JComboBox<Item> combo_sala;
    private javax.swing.JComboBox<Item> combo_sucursal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton nuevo;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txt_fecha;
    private javax.swing.JTextField txt_horario_fin;
    private javax.swing.JTextField txt_horario_inicio;
    // End of variables declaration//GEN-END:variables
}
