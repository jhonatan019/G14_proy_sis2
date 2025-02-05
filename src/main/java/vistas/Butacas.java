/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vistas;

import baseDatos.ConexionBD;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Butacas extends javax.swing.JFrame {
    private boolean reservaRealizada = false;
    private HashMap<JButton, Boolean> butacasEstado = new HashMap<>();
    private JButton[] butacas;
    private int maxSeleccionadas;
    private int seleccionadas = 0;
    private double precioEntrada; 
    private String nombreCliente;
    private String apellidoCliente;
    private String ci;
    private int idSala;
    private int idHorario;
    private int idPelicula;
    private Connection conn;

    public Butacas(int idPelicula, int idHorario, int idSala, int cantidadButacas,
                   String nombreCliente, String apellidoCliente, String ci) {
        try {
            conn = ConexionBD.getConnection();
            
            this.idPelicula = idPelicula;
            this.idHorario = idHorario;
            this.idSala = idSala;
            this.maxSeleccionadas = cantidadButacas;
            this.nombreCliente = nombreCliente;
            this.apellidoCliente = apellidoCliente;
            this.ci = ci;
            
            initComponents();
            cargarPrecioEntrada();
            cargarDatosFuncion();
            inicializarButacas();
            cargarButacasOcupadas();
            actualizarTotal();
            
            setResizable(false);
            setTitle("Selección de Butacas");
            setLocationRelativeTo(null);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage());
        }
    }
    
    private void cargarPrecioEntrada() throws SQLException {
        String sql = "SELECT VALOR_CONFIG FROM configuracion WHERE NOMBRE_CONFIG = 'PRECIO_BOLETO'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                precioEntrada = rs.getDouble("VALOR_CONFIG");
            }
        }
    }
    
    private void cargarDatosFuncion() throws SQLException {
        String sql = """
            SELECT p.NOMBRE_PELICULA, s.NUMERO_SALA, 
                   DATE_FORMAT(f.FECHA_FUNCION, '%d/%m/%Y') as FECHA,
                   TIME_FORMAT(f.FUNCION_INICIO, '%H:%i') as HORA
            FROM funcion f
            JOIN pelicula p ON f.ID_PELICULA = p.ID_PELICULA
            JOIN sala s ON f.ID_SALA = s.ID_SALA
            WHERE f.ID_HORARIO = ?
        """;
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idHorario);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                txt_pelicula.setText(rs.getString("NOMBRE_PELICULA"));
                txt_sala.setText("Sala " + rs.getString("NUMERO_SALA"));
                txt_horario.setText(rs.getString("FECHA") + " " + rs.getString("HORA"));
            }
        }
    }
    
    private void inicializarButacas() {
        butacas = new JButton[]{
            jButton1, jButton2, jButton3, jButton4, jButton5, jButton6, jButton7, jButton8, jButton9, jButton10,
            jButton11, jButton12, jButton13, jButton14, jButton15, jButton16, jButton17, jButton18, jButton19, jButton20,
            jButton21, jButton22, jButton23, jButton24, jButton25, jButton26, jButton27, jButton28, jButton29, jButton30
        };

        for (JButton butaca : butacas) {
            butacasEstado.put(butaca, false);
            butaca.setBackground(Color.GREEN);
            butaca.addActionListener(e -> reservarButaca(butaca));
        }
    }
    
    private void reservarButaca(JButton butaca) {
        if (!butaca.isEnabled()) return; 
        
        if (butacasEstado.get(butaca)) {
            // Deseleccionar butaca
            butaca.setBackground(Color.GREEN);
            butacasEstado.put(butaca, false);
            seleccionadas--;
        } else if (seleccionadas < maxSeleccionadas) {
            // Seleccionar butaca
            butaca.setBackground(Color.YELLOW);
            butacasEstado.put(butaca, true);
            seleccionadas++;
        } else {
            JOptionPane.showMessageDialog(this, 
                "Has alcanzado el límite de " + maxSeleccionadas + " butacas.");
        }
        actualizarTotal();
    }
    
    private void cargarButacasOcupadas() {
        String sql = """
            SELECT NUMERO_BUTACAS 
            FROM butaca 
            WHERE ID_HORARIO = ? 
            AND ESTADO_BUTACAS = 'OCUPADA'
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idHorario);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                int numeroButaca = rs.getInt("NUMERO_BUTACAS");
                if (numeroButaca > 0 && numeroButaca <= butacas.length) {
                    JButton butaca = butacas[numeroButaca - 1];
                    butaca.setBackground(Color.RED);
                    butaca.setEnabled(false);
                    butacasEstado.put(butaca, false);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar butacas ocupadas: " + e.getMessage());
        }
    }

    private void actualizarTotal() {
        double total = seleccionadas * precioEntrada;
        txt_total.setText(String.format("%.2f Bs.", total));
    }
    
    
    private int insertarObtenerCliente() throws SQLException {
       
        String sqlVerificar = "SELECT ID_CLIENTE FROM clientes WHERE CI_CLIENTE = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlVerificar)) {
            ps.setString(1, ci);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("ID_CLIENTE");
            }
        }
        
        
        String sqlInsertar = """
            INSERT INTO clientes (NOMBRE_CLIENTE, APELLIDO_CLIENTE, CI_CLIENTE)
            VALUES (?, ?, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sqlInsertar, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombreCliente);
            ps.setString(2, apellidoCliente);
            ps.setString(3, ci);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("No se pudo obtener el ID del cliente");
        }
    }
    
    private int insertarVenta(int idCliente) throws SQLException {
        String sql = """
            INSERT INTO venta (ID_CLIENTE, MONTO_TOTAL_VENTA, FECHA_VENTA)
            VALUES (?, ?, CURRENT_DATE)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idCliente);
            ps.setDouble(2, maxSeleccionadas * precioEntrada);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("No se pudo obtener el ID de la venta");
        }
    }
    
    private void insertarBoleto(int idVenta) throws SQLException {
        String sql = "INSERT INTO boleto (ID_VENTA, CANTIDAD_DE_BOLETOS) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            ps.setInt(2, maxSeleccionadas);
            ps.executeUpdate();
        }
    }
    
    private void insertarButacas(int idVenta) throws SQLException {
        String sql = """
            INSERT INTO butaca (ID_SALA, ID_VENTA, ID_HORARIO, NUMERO_BUTACAS, ESTADO_BUTACAS)
            VALUES (?, ?, ?, ?, 'OCUPADA')
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < butacas.length; i++) {
                if (butacasEstado.get(butacas[i]) && 
                    butacas[i].getBackground() == Color.YELLOW) {
                    ps.setInt(1, idSala);
                    ps.setInt(2, idVenta);
                    ps.setInt(3, idHorario);
                    ps.setInt(4, i + 1);
                    ps.executeUpdate();
                }
            }
        }
    }
    
    private void actualizarAsientosDisponibles() throws SQLException {
        String sql = """
            UPDATE funcion
            SET ASIENTOS_DISPONIBLES_FUNCION = CAST(CAST(ASIENTOS_DISPONIBLES_FUNCION AS SIGNED) - ? AS CHAR)
            WHERE ID_HORARIO = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maxSeleccionadas);
            ps.setInt(2, idHorario);
            ps.executeUpdate();
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
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_pelicula = new javax.swing.JTextField();
        txt_sala = new javax.swing.JTextField();
        txt_horario = new javax.swing.JTextField();
        txt_total = new javax.swing.JTextField();
        btn_regresar = new javax.swing.JButton();
        btn_emitir = new javax.swing.JButton();
        btn_reservar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(39, 29, 41));
        setMinimumSize(new java.awt.Dimension(1260, 836));
        setPreferredSize(new java.awt.Dimension(1260, 936));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(39, 29, 41));
        jPanel1.setMinimumSize(new java.awt.Dimension(1260, 836));
        jPanel1.setPreferredSize(new java.awt.Dimension(1260, 936));

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));

        jButton1.setText("A1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("A2");

        jButton3.setText("A3");

        jButton4.setText("A4");

        jButton5.setText("A5");

        jButton6.setText("A6");

        jButton7.setText("A25");

        jButton8.setText("A26");

        jButton9.setText("A7");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("A8");

        jButton11.setText("A9");

        jButton12.setText("A10");

        jButton13.setText("A11");

        jButton14.setText("A12");

        jButton15.setText("A27");

        jButton16.setText("A28");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.setText("A13");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setText("A14");

        jButton19.setText("A15");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton20.setText("A16");

        jButton21.setText("A17");

        jButton22.setText("A18");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton23.setText("A29");

        jButton24.setText("A30");

        jButton25.setText("A19");

        jButton26.setText("A20");

        jButton27.setText("A21");

        jButton28.setText("A22");

        jButton29.setText("A23");

        jButton30.setText("A24");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Pélicula:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Horario:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Sala:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Total:");

        txt_pelicula.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt_pelicula.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txt_sala.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt_sala.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_sala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_salaActionPerformed(evt);
            }
        });

        txt_horario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt_horario.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txt_total.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt_total.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        btn_regresar.setBackground(new java.awt.Color(230, 12, 16));
        btn_regresar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_regresar.setForeground(new java.awt.Color(255, 255, 255));
        btn_regresar.setText("Regresar");
        btn_regresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_regresarActionPerformed(evt);
            }
        });

        btn_emitir.setBackground(new java.awt.Color(230, 12, 16));
        btn_emitir.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_emitir.setForeground(new java.awt.Color(255, 255, 255));
        btn_emitir.setText("Emitir");
        btn_emitir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_emitirActionPerformed(evt);
            }
        });

        btn_reservar.setBackground(new java.awt.Color(230, 12, 16));
        btn_reservar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_reservar.setForeground(new java.awt.Color(255, 255, 255));
        btn_reservar.setText("Reservar");
        btn_reservar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reservarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(209, 209, 209)
                        .addComponent(btn_regresar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(220, 220, 220)
                        .addComponent(btn_emitir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(376, 376, 376)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_horario, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_pelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txt_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(txt_total, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addComponent(btn_reservar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(138, 138, 138))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_horario, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txt_pelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_total, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(23, 23, 23)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_regresar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_emitir, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_reservar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1250, 860));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton30ActionPerformed

    private void txt_salaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_salaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_salaActionPerformed

    private void btn_regresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_regresarActionPerformed
    dispose();
    }//GEN-LAST:event_btn_regresarActionPerformed

    private void btn_reservarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reservarActionPerformed
     if (seleccionadas < maxSeleccionadas) {
        JOptionPane.showMessageDialog(this,
            "Debe seleccionar " + maxSeleccionadas + " butacas", 
            "Selección incompleta",
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        conn.setAutoCommit(false);
        
       
        String sqlCliente = "SELECT ID_CLIENTE FROM clientes WHERE CI_CLIENTE = ?";
        PreparedStatement psCliente = conn.prepareStatement(sqlCliente);
        psCliente.setString(1, ci);
        ResultSet rsCliente = psCliente.executeQuery();
        
        int idCliente;
        if (rsCliente.next()) {
            idCliente = rsCliente.getInt("ID_CLIENTE");
        } else {
            
            String sqlInsertCliente = "INSERT INTO clientes (NOMBRE_CLIENTE, APELLIDO_CLIENTE, CI_CLIENTE) VALUES (?, ?, ?)";
            PreparedStatement psInsertCliente = conn.prepareStatement(sqlInsertCliente, Statement.RETURN_GENERATED_KEYS);
            psInsertCliente.setString(1, nombreCliente);
            psInsertCliente.setString(2, apellidoCliente);
            psInsertCliente.setString(3, ci);
            psInsertCliente.executeUpdate();
            
            ResultSet rsGeneratedKeys = psInsertCliente.getGeneratedKeys();
            if (rsGeneratedKeys.next()) {
                idCliente = rsGeneratedKeys.getInt(1);
            } else {
                throw new SQLException("No se pudo obtener el ID del cliente");
            }
        }
        
        // Insertar venta
        String sqlVenta = "INSERT INTO venta (ID_CLIENTE, MONTO_TOTAL_VENTA, FECHA_VENTA) VALUES (?, ?, CURRENT_DATE)";
        PreparedStatement psVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
        double montoTotal = maxSeleccionadas * precioEntrada;
        psVenta.setInt(1, idCliente);
        psVenta.setDouble(2, montoTotal);
        psVenta.executeUpdate();
        
        int idVenta;
        ResultSet rsVenta = psVenta.getGeneratedKeys();
        if (rsVenta.next()) {
            idVenta = rsVenta.getInt(1);
        } else {
            throw new SQLException("No se pudo obtener el ID de la venta");
        }
        
        // Insertar boleto
        String sqlBoleto = "INSERT INTO boleto (ID_VENTA, CANTIDAD_DE_BOLETOS) VALUES (?, ?)";
        PreparedStatement psBoleto = conn.prepareStatement(sqlBoleto);
        psBoleto.setInt(1, idVenta);
        psBoleto.setInt(2, maxSeleccionadas);
        psBoleto.executeUpdate();
        
        // Insertar butacas
        String sqlButaca = "INSERT INTO butaca (ID_SALA, ID_VENTA, ID_HORARIO, NUMERO_BUTACAS, ESTADO_BUTACAS) VALUES (?, ?, ?, ?, 'OCUPADA')";
        PreparedStatement psButaca = conn.prepareStatement(sqlButaca);
        
        for (int i = 0; i < butacas.length; i++) {
            if (butacasEstado.get(butacas[i]) && butacas[i].getBackground() == Color.YELLOW) {
                psButaca.setInt(1, idSala);
                psButaca.setInt(2, idVenta);
                psButaca.setInt(3, idHorario);
                psButaca.setInt(4, i + 1);
                psButaca.executeUpdate();
            }
        }
        
        // Actualizar asientos disponibles en función
        String sqlUpdateFuncion = "UPDATE funcion SET ASIENTOS_DISPONIBLES_FUNCION = CAST(CAST(ASIENTOS_DISPONIBLES_FUNCION AS SIGNED) - ? AS CHAR) WHERE ID_HORARIO = ?";
        PreparedStatement psUpdateFuncion = conn.prepareStatement(sqlUpdateFuncion);
        psUpdateFuncion.setInt(1, maxSeleccionadas);
        psUpdateFuncion.setInt(2, idHorario);
        psUpdateFuncion.executeUpdate();
        
        conn.commit();
        JOptionPane.showMessageDialog(this, "Reserva realizada con éxito!");
        btn_emitir.setEnabled(true);
        btn_reservar.setEnabled(false);
        reservaRealizada = true;
        
    } catch (SQLException e) {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, 
            "Error al realizar la reserva: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    }//GEN-LAST:event_btn_reservarActionPerformed

    private void btn_emitirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_emitirActionPerformed
    if (!reservaRealizada) {
        JOptionPane.showMessageDialog(this,
            "Debe realizar la reserva antes de emitir el comprobante",
            "Reserva pendiente",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    try {
        // Crear tamaño personalizado para ticket
        Rectangle ticketSize = new Rectangle(290f, 500f);
        Document documento = new Document(ticketSize, 20f, 20f, 20f, 20f);
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Entrada PDF");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF files (*.pdf)", "pdf"));
        
        String fecha = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        String nombreArchivo = "Entrada-" + nombreCliente + "-" + fecha + ".pdf";
        fileChooser.setSelectedFile(new File(nombreArchivo));
        
        if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        String rutaPDF = fileChooser.getSelectedFile().getAbsolutePath();
        if (!rutaPDF.toLowerCase().endsWith(".pdf")) {
            rutaPDF += ".pdf";
        }
        
        PdfWriter.getInstance(documento, new FileOutputStream(rutaPDF));
        documento.open();
        
        // Logo 
        try {
            Image logo = Image.getInstance("src/main/resources/images/logo.jpg");
            logo.scaleToFit(70, 70); // Reducido de 100 a 70
            logo.setAlignment(Element.ALIGN_CENTER);
            documento.add(logo);
        } catch (Exception e) {
            System.out.println("No se pudo cargar el logo: " + e.getMessage());
        }
        
        // Título 
        Paragraph titulo = new Paragraph();
        titulo.add(new Chunk("CINE PELICULON\n", 
            FontFactory.getFont("Helvetica", 16, Font.BOLD))); // Reducido de 20 a 16
        titulo.add(new Chunk("ENTRADA\n\n", 
            FontFactory.getFont("Helvetica", 14, Font.BOLD))); // Reducido de 16 a 14
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);
        
        // Información de la película
        Paragraph info = new Paragraph();
        info.add(new Chunk("PELÍCULA: " + txt_pelicula.getText() + "\n", 
            FontFactory.getFont("Helvetica", 12, Font.BOLD))); // Reducido de 14 a 12
        info.add(new Chunk("SALA: " + txt_sala.getText() + "\n", 
            FontFactory.getFont("Helvetica", 10))); // Reducido de 12 a 10
        info.add(new Chunk("HORARIO: " + txt_horario.getText() + "\n\n", 
            FontFactory.getFont("Helvetica", 10)));
        documento.add(info);
        
        // Información del cliente
        Paragraph cliente = new Paragraph();
        cliente.add(new Chunk("DATOS DEL CLIENTE\n", 
            FontFactory.getFont("Helvetica", 10, Font.BOLD)));
        cliente.add(new Chunk("Nombre: " + nombreCliente + " " + apellidoCliente + "\n",
            FontFactory.getFont("Helvetica", 10)));
        cliente.add(new Chunk("CI: " + ci + "\n\n",
            FontFactory.getFont("Helvetica", 10)));
        documento.add(cliente);
        
        // Butacas
        Paragraph butacasInfo = new Paragraph();
        butacasInfo.add(new Chunk("BUTACAS SELECCIONADAS\n", 
            FontFactory.getFont("Helvetica", 10, Font.BOLD)));
        
        List<Integer> butacasSeleccionadas = new ArrayList<>();
        for (int i = 0; i < butacas.length; i++) {
            if (butacasEstado.get(butacas[i]) && 
                butacas[i].getBackground() == Color.YELLOW) {
                butacasSeleccionadas.add(i + 1);
            }
        }
        
        butacasInfo.add(new Chunk("Números: " + butacasSeleccionadas.toString()
            .replace("[", "").replace("]", "") + "\n",
            FontFactory.getFont("Helvetica", 10)));
        butacasInfo.add(new Chunk("Cantidad: " + maxSeleccionadas + "\n\n",
            FontFactory.getFont("Helvetica", 10)));
        documento.add(butacasInfo);
        
        // Precios
        Paragraph precios = new Paragraph();
        precios.add(new Chunk("DETALLE DE PAGO\n", 
            FontFactory.getFont("Helvetica", 10, Font.BOLD)));
        precios.add(new Chunk(String.format("Precio por entrada: %.2f Bs.\n", precioEntrada),
            FontFactory.getFont("Helvetica", 10)));
        precios.add(new Chunk(String.format("Total: %.2f Bs.\n\n", 
            precioEntrada * maxSeleccionadas),
            FontFactory.getFont("Helvetica", 10)));
        documento.add(precios);
        
        // Pie de página
        Paragraph pie = new Paragraph();
        pie.add(new Chunk("\n\nEste boleto es válido solo para la función indicada.\n", 
            FontFactory.getFont("Helvetica", 8)));
        pie.add(new Chunk("Por favor, preséntelo al ingresar a la sala.\n", 
            FontFactory.getFont("Helvetica", 8)));
        pie.add(new Chunk("CINE PELICULON - Tu mejor opción de entretenimiento", 
            FontFactory.getFont("Helvetica", 8, Font.ITALIC)));
        pie.setAlignment(Element.ALIGN_CENTER);
        documento.add(pie);
        
        documento.close();
        
        JOptionPane.showMessageDialog(this, 
            "La entrada se ha generado exitosamente:\n" + rutaPDF);
        
        Desktop.getDesktop().open(new File(rutaPDF));
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Error al generar el PDF: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    this.setVisible(false);
    }//GEN-LAST:event_btn_emitirActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_emitir;
    private javax.swing.JButton btn_regresar;
    private javax.swing.JButton btn_reservar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField txt_horario;
    private javax.swing.JTextField txt_pelicula;
    private javax.swing.JTextField txt_sala;
    private javax.swing.JTextField txt_total;
    // End of variables declaration//GEN-END:variables
}
