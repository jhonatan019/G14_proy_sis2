/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vistas;

import baseDatos.ConexionBD;
import com.mycompany.cine2.Item;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


public class ReservaDeEntradas extends javax.swing.JFrame {

    
    private Connection conexion;
    private int idEmpleadoActual;
    private int idSucursalEmpleado;
    
    public ReservaDeEntradas() {
        initComponents();
        setResizable(false);
        setTitle("Reserva de Entradas");
        setLocationRelativeTo(null);
        
        // Obtener el ID del empleado que inició sesión
        idEmpleadoActual = InicioSesion.SesionUsuario.getIdEmpleado();
        
        // Obtener la sucursal del empleado
        obtenerSucursalEmpleado();
        
        // Deshabilitar el combo de sucursal ya que será fijo
        combo_sucursal.setEnabled(false);
        
        cargarPeliculas();
        
        combo_pelicula.addActionListener(e -> peliculaSeleccionada());
        combo_horario.addActionListener(e -> horarioSeleccionado());
    } 
    
    public void setDatosCliente(String nombre, String apellido, String ci) {
    // Asumiendo que tus campos de texto se llaman txt_nombre, txt_apellido y txt_ci
    if (txt_nombre != null) txt_nombre.setText(nombre);
    if (txt_apellido != null) txt_apellido.setText(apellido);
    if (txt_ci != null) txt_ci.setText(ci);
    }
    
    private void obtenerSucursalEmpleado() {
    String sql = "SELECT u.ID_SUCURSAL, s.NOMBRE_SUCURSAL " +
                "FROM usuario u " +
                "INNER JOIN sucursal s ON u.ID_SUCURSAL = s.ID_SUCURSAL " +
                "WHERE u.ID_EMPLE = ?";
                
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, idEmpleadoActual);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            idSucursalEmpleado = rs.getInt("ID_SUCURSAL");
            String nombreSucursal = rs.getString("NOMBRE_SUCURSAL");
            
            // Limpiar y agregar la única sucursal al combo
            combo_sucursal.removeAllItems();
            combo_sucursal.addItem(new Item(idSucursalEmpleado, nombreSucursal));
        } else {
            JOptionPane.showMessageDialog(this, 
                "No se encontró una sucursal asignada para este empleado",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al obtener sucursal del empleado: " + ex.getMessage());
    }
}
    
    private void cargarPeliculas() {
        combo_pelicula.removeAllItems();
        String sql = "SELECT DISTINCT p.ID_PELICULA, p.NOMBRE_PELICULA FROM pelicula p " +
                    "INNER JOIN funcion f ON p.ID_PELICULA = f.ID_PELICULA " +
                    "INNER JOIN sala s ON f.ID_SALA = s.ID_SALA " +
                    "WHERE f.FECHA_FUNCION >= CURRENT_DATE " +
                    "AND s.ID_SUCURSAL = ?";
                    
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idSucursalEmpleado);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                combo_pelicula.addItem(new Item(
                    rs.getInt("ID_PELICULA"),
                    rs.getString("NOMBRE_PELICULA")
                ));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar películas: " + ex.getMessage());
        }
    }
    
    private void peliculaSeleccionada() {
        Item peliculaSeleccionada = (Item) combo_pelicula.getSelectedItem();
        if (peliculaSeleccionada == null) return;
        
        cargarHorarios(peliculaSeleccionada.getId());
        cargarDatosPelicula(peliculaSeleccionada.getId());
    }
    
    private void cargarHorarios(int idPelicula) {
        combo_horario.removeAllItems();
        String sql = "SELECT f.ID_HORARIO, " +
                    "CONCAT(DATE_FORMAT(f.FECHA_FUNCION, '%d/%m/%Y'), ' ', " +
                    "TIME_FORMAT(f.FUNCION_INICIO, '%H:%i')) as HORARIO, " +
                    "(f.ASIENTOS_TOTALES_FUNCION - COUNT(b.ID_BUTACAS)) as DISPONIBLES " +
                    "FROM funcion f " +
                    "INNER JOIN sala s ON f.ID_SALA = s.ID_SALA " +
                    "LEFT JOIN butaca b ON f.ID_HORARIO = b.ID_HORARIO " +
                    "WHERE f.ID_PELICULA = ? " +
                    "AND s.ID_SUCURSAL = ? " +
                    "AND CONCAT(f.FECHA_FUNCION, ' ', f.FUNCION_INICIO) >= NOW() " +
                    "GROUP BY f.ID_HORARIO " +
                    "HAVING DISPONIBLES > 0";  
                        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idPelicula);
            ps.setInt(2, idSucursalEmpleado);
            ResultSet rs = ps.executeQuery();
            
            boolean hayFunciones = false;
            while (rs.next()) {
                hayFunciones = true;
                combo_horario.addItem(new Item(
                    rs.getInt("ID_HORARIO"),
                    rs.getString("HORARIO")  
                ));
            }
            
            if (!hayFunciones) {
                JOptionPane.showMessageDialog(this, 
                    "No hay funciones disponibles para esta película en esta sucursal.",
                    "Sin funciones disponibles",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar horarios: " + ex.getMessage());
        }
    }
    
    private void horarioSeleccionado() {
    Item horarioSeleccionado = (Item) combo_horario.getSelectedItem();
    if (horarioSeleccionado == null) return;
    
    cargarSala(horarioSeleccionado.getId());
    cargarButacasDisponibles(horarioSeleccionado.getId());
    cargarSucursal(horarioSeleccionado.getId());
}

    private void cargarSala(int idHorario) {
    combo_sala.removeAllItems();
    String sql = "SELECT s.ID_SALA, s.NUMERO_SALA " +
                "FROM sala s " +
                "INNER JOIN funcion f ON s.ID_SALA = f.ID_SALA " +
                "WHERE f.ID_HORARIO = ? " +
                "AND s.ID_SUCURSAL = ?";
                
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, idHorario);
        ps.setInt(2, idSucursalEmpleado);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            combo_sala.addItem("Sala " + rs.getString("NUMERO_SALA"));
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al cargar sala: " + ex.getMessage());
    }
}
    
    private void cargarButacasDisponibles(int idHorario) {
    combo_butacas.removeAllItems();
    String sql = "SELECT f.ASIENTOS_TOTALES_FUNCION, " +
                "(f.ASIENTOS_TOTALES_FUNCION - COUNT(b.ID_BUTACAS)) as DISPONIBLES " +
                "FROM funcion f " +
                "INNER JOIN sala s ON f.ID_SALA = s.ID_SALA " +
                "LEFT JOIN butaca b ON f.ID_HORARIO = b.ID_HORARIO " +
                "WHERE f.ID_HORARIO = ? " +
                "AND s.ID_SUCURSAL = ? " +
                "GROUP BY f.ID_HORARIO, f.ASIENTOS_TOTALES_FUNCION";
                
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, idHorario);
        ps.setInt(2, idSucursalEmpleado);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            int disponibles = rs.getInt("DISPONIBLES");
            if (disponibles <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Esta función está llena. Por favor seleccione otro horario.",
                    "Función sin butacas disponibles",
                    JOptionPane.INFORMATION_MESSAGE);
                
                combo_butacas.setEnabled(false);
                btn_butacas.setEnabled(false);
            } else {
                combo_butacas.setEnabled(true);
                btn_butacas.setEnabled(true);
                
                for (int i = 1; i <= disponibles; i++) {
                    combo_butacas.addItem(i);
                }
            }
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al cargar butacas: " + ex.getMessage());
    }
}
    
    private void cargarSucursal(int idHorario) {
        combo_sucursal.removeAllItems();
        String sql = "SELECT s.ID_SUCURSAL, s.NOMBRE_SUCURSAL FROM sucursal s " +
                    "INNER JOIN sala sa ON s.ID_SUCURSAL = sa.ID_SUCURSAL " +
                    "INNER JOIN funcion f ON sa.ID_SALA = f.ID_SALA " +
                    "WHERE f.ID_HORARIO = ?";
                    
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idHorario);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                combo_sucursal.addItem(new Item(
                    rs.getInt("ID_SUCURSAL"),
                    rs.getString("NOMBRE_SUCURSAL")
                ));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar sucursal: " + ex.getMessage());
        }
    }
    
    private void cargarDatosPelicula(int idPelicula) {
    String sql = "SELECT SIPNOPSIS_PELICULA, IMAGEN_PELICULA FROM pelicula WHERE ID_PELICULA = ?";
    
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, idPelicula);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            
            sinopsis.setText(rs.getString("SIPNOPSIS_PELICULA"));
            
            
            String rutaImagen = rs.getString("IMAGEN_PELICULA");
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                try {
                    ImageIcon icono = new ImageIcon(rutaImagen);
                    
                    
                    int labelWidth = imagen.getWidth();
                    int labelHeight = imagen.getHeight();
                    
                    if (labelWidth <= 0) labelWidth = imagen.getPreferredSize().width;
                    if (labelHeight <= 0) labelHeight = imagen.getPreferredSize().height;
                    
                    
                    Image img = icono.getImage();
                    float proporcionImagen = (float) img.getWidth(null) / img.getHeight(null);
                    float proporcionLabel = (float) labelWidth / labelHeight;
                    
                    int newWidth, newHeight;
                    
                    if (proporcionImagen > proporcionLabel) {
                       
                        newWidth = labelWidth;
                        newHeight = (int) (labelWidth / proporcionImagen);
                    } else {
                        
                        newHeight = labelHeight;
                        newWidth = (int) (labelHeight * proporcionImagen);
                    }
                    
                   
                    Image imgRedimensionada = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                    imagen.setIcon(new ImageIcon(imgRedimensionada));
                    
                } catch (Exception e) {
                    System.out.println("Error al cargar la imagen: " + e.getMessage());
                    imagen.setIcon(null);
                }
            } else {
                imagen.setIcon(null);
            }
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al cargar datos de la película: " + ex.getMessage());
    }
}
    
    private boolean validarCampos() {
        if (txt_nombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es requerido");
            return false;
        }
        
        if (txt_apellido.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El apellido es requerido");
            return false;
        }
        
        if (txt_ci.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El CI es requerido");
        return false;
    }
    
    try {
        Integer.parseInt(txt_ci.getText().trim()); 
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "El CI debe ser un número válido");
        return false;
    }
        
        if (combo_pelicula.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una película");
            return false;
        }
        
        if (combo_horario.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un horario");
            return false;
        }
        
        if (combo_butacas.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar cantidad de butacas");
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
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        combo_pelicula = new javax.swing.JComboBox<>();
        combo_horario = new javax.swing.JComboBox<>();
        combo_butacas = new javax.swing.JComboBox<>();
        combo_sala = new javax.swing.JComboBox<>();
        txt_nombre = new javax.swing.JTextField();
        txt_apellido = new javax.swing.JTextField();
        txt_ci = new javax.swing.JTextField();
        combo_sucursal = new javax.swing.JComboBox<>();
        imagen = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        sinopsis = new javax.swing.JTextArea();
        btn_butacas = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1260, 836));
        setPreferredSize(new java.awt.Dimension(1260, 836));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(39, 29, 41));
        jPanel1.setMinimumSize(new java.awt.Dimension(1260, 836));
        jPanel1.setPreferredSize(new java.awt.Dimension(1260, 836));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("RESERVA DE ENTRADAS");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Pélicula:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Horario:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Butacas:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Sala:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Nombre Cliente:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Apellido Cliente:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("C.I.:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Sucursal:");

        combo_pelicula.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        combo_horario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        combo_butacas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        combo_sala.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        txt_nombre.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt_nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nombreActionPerformed(evt);
            }
        });

        txt_apellido.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        txt_ci.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        combo_sucursal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        imagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pelicula.png"))); // NOI18N

        sinopsis.setEditable(false);
        sinopsis.setBackground(new java.awt.Color(39, 29, 41));
        sinopsis.setColumns(20);
        sinopsis.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        sinopsis.setForeground(new java.awt.Color(255, 255, 255));
        sinopsis.setLineWrap(true);
        sinopsis.setRows(5);
        sinopsis.setWrapStyleWord(true);
        sinopsis.setBorder(null);
        sinopsis.setCaretColor(new java.awt.Color(39, 29, 41));
        sinopsis.setDisabledTextColor(new java.awt.Color(39, 29, 41));
        jScrollPane1.setViewportView(sinopsis);

        btn_butacas.setBackground(new java.awt.Color(230, 12, 16));
        btn_butacas.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn_butacas.setForeground(new java.awt.Color(255, 255, 255));
        btn_butacas.setText("Seleccionar Butacas");
        btn_butacas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_butacasActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(230, 12, 16));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Cancelar Venta");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Sinopsis");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(txt_ci, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(116, 116, 116)
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(combo_sucursal, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel5))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(combo_pelicula, 0, 180, Short.MAX_VALUE)
                                            .addComponent(combo_horario, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(combo_butacas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(combo_sala, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel7))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txt_apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(135, 135, 135)
                                .addComponent(imagen, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(36, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_butacas, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(231, 231, 231)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(240, 240, 240))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jLabel1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(combo_pelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(combo_horario, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(combo_butacas, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(combo_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txt_apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(imagen, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(txt_ci, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(combo_sucursal, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_butacas, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1260, 820));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nombreActionPerformed

    private void btn_butacasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_butacasActionPerformed
   if (!validarCampos()) return;
        
        Item pelicula = (Item) combo_pelicula.getSelectedItem();
        Item horario = (Item) combo_horario.getSelectedItem();
        Item sucursal = (Item) combo_sucursal.getSelectedItem();
        int cantidadButacas = (Integer) combo_butacas.getSelectedItem();
        String nombre = txt_nombre.getText().trim();
        String apellido = txt_apellido.getText().trim();
        String ci = txt_ci.getText().trim();
        
        Butacas vistaBuletas = new Butacas(
            pelicula.getId(),
            horario.getId(),
            sucursal.getId(),
            cantidadButacas,
            nombre,
            apellido,
            ci  
        );
        vistaBuletas.setVisible(true);
        this.dispose(); 
    
    }//GEN-LAST:event_btn_butacasActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    MenuEmpleado menu = new MenuEmpleado();
    menu.setVisible(true);
    this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

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
    private javax.swing.JButton btn_butacas;
    private javax.swing.JComboBox<Integer> combo_butacas;
    private javax.swing.JComboBox<Item> combo_horario;
    private javax.swing.JComboBox<Item> combo_pelicula;
    private javax.swing.JComboBox<String> combo_sala;
    private javax.swing.JComboBox<Item> combo_sucursal;
    private javax.swing.JLabel imagen;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea sinopsis;
    private javax.swing.JTextField txt_apellido;
    private javax.swing.JTextField txt_ci;
    private javax.swing.JTextField txt_nombre;
    // End of variables declaration//GEN-END:variables
}
