/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vistas;

import baseDatos.ConexionBD;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;


public class Butacas extends javax.swing.JFrame {

    private HashMap<JButton, Boolean> butacasEstado = new HashMap<>();
    private JButton[] butacas;
    private int maxSeleccionadas;
    private int seleccionadas = 0;
    private double precio; 
    private String nombreCliente;
    private String apellidoCliente;
    private String ci;
    private String sala;
    private String horario;
     private String idHorario;
    private String pelicula;
    private Connection conn;

    public Butacas(int maxSeleccionadas, double precio, String pelicula, 
               String sala, String horario, String idHorario, 
               String nombreCliente, String apellidoCliente, String ci) {
    this.maxSeleccionadas = maxSeleccionadas;
    this.precio = precio;
    this.nombreCliente = nombreCliente;
    this.apellidoCliente = apellidoCliente;
    this.ci = ci;
    this.sala = sala;
    this.horario = horario;
    this.pelicula = pelicula;
    this.idHorario = idHorario; 

    try {
        conn = ConexionBD.getConnection();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage());
    }

    initComponents();
    inicializarButacas();
    cargarButacasOcupadas();
    actualizarTotal();
    setResizable(false);
    setTitle("Selección de Butacas");
    setLocationRelativeTo(null);
    
    txt_pelicula.setText(pelicula);
    txt_sala.setText("Sala " + sala);
    txt_horario.setText(horario);
}
    
    public Butacas() {
        initComponents();
        inicializarButacas();
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
        if (butacasEstado.get(butaca)) {
            // Permitir deselección
            butaca.setBackground(Color.GREEN);
            butacasEstado.put(butaca, false);
            seleccionadas--;
        } else if (seleccionadas < maxSeleccionadas) {
            butaca.setBackground(Color.RED);
            butacasEstado.put(butaca, true);
            seleccionadas++;
        } else {
            JOptionPane.showMessageDialog(this, "Has alcanzado el límite de " + maxSeleccionadas + " butacas.");
        }
        actualizarTotal();
    }
    
private void cargarButacasOcupadas() {
    String sql = """
        SELECT DISTINCT b.NUMERO_BUTACAS 
        FROM butaca b
        JOIN funcion f ON b.ID_HORARIO = f.ID_HORARIO
        JOIN pelicula p ON f.ID_PELICULA = p.ID_PELICULA
        WHERE f.ID_SALA = ? 
        AND p.NOMBRE_PELICULA = ?
        AND f.FUNCION_INICIO = ?
        AND f.FECHA_FUNCION = CURRENT_DATE
        AND b.ESTADO_BUTACAS = 'OCUPADA'
    """;

    try (PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setString(1, sala);
        pst.setString(2, pelicula);
        pst.setString(3, horario);
        
        System.out.println("Debug - Parámetros de búsqueda:");
        System.out.println("Sala: " + sala);
        System.out.println("Película: " + pelicula);
        System.out.println("Horario: " + horario);
        
        ResultSet rs = pst.executeQuery();
        
        while (rs.next()) {
            int numeroButaca = rs.getInt("NUMERO_BUTACAS");
            System.out.println("Butaca ocupada encontrada: " + numeroButaca);
            if (numeroButaca <= butacas.length) {
                butacas[numeroButaca - 1].setBackground(Color.RED);
                butacas[numeroButaca - 1].setEnabled(false);
                butacasEstado.put(butacas[numeroButaca - 1], true);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Error al cargar butacas ocupadas: " + e.getMessage());
    }
}

    private void actualizarTotal() {
        double total = seleccionadas * precio;
        txt_total.setText(String.format("%.2f Bs.", total));
    }
     
    
     
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_pelicula = new javax.swing.JTextField();
        txt_sala = new javax.swing.JTextField();
        txt_horario = new javax.swing.JTextField();
        txt_total = new javax.swing.JTextField();
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
        btn_Cerrar = new javax.swing.JButton();
        btn_Reservar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(1180, 800));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(98, 31, 51));
        jPanel1.setPreferredSize(new java.awt.Dimension(1180, 800));

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Pelicula:");

        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Sala:");

        jLabel3.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Horario:");

        jLabel4.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Total:");

        txt_pelicula.setEditable(false);
        txt_pelicula.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        txt_pelicula.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_pelicula.setFocusable(false);

        txt_sala.setEditable(false);
        txt_sala.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        txt_sala.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_sala.setFocusable(false);

        txt_horario.setEditable(false);
        txt_horario.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        txt_horario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_horario.setFocusable(false);

        txt_total.setEditable(false);
        txt_total.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        txt_total.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_total.setFocusable(false);
        txt_total.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_totalActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 204, 51));

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
                        .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(30, 30, 30)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(15, 15, 15))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        btn_Cerrar.setText("Cerrar");
        btn_Cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CerrarActionPerformed(evt);
            }
        });

        btn_Reservar.setText("Reservar");
        btn_Reservar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ReservarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(287, 287, 287)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1))
                        .addGap(96, 96, 96)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_sala)
                            .addComponent(txt_pelicula)
                            .addComponent(txt_horario, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_total, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(188, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(btn_Cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Reservar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(90, 90, 90))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_pelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txt_total, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(txt_horario, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Reservar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(74, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1180, 900));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_totalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_totalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_totalActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton16ActionPerformed

    private void btn_CerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CerrarActionPerformed
     
    // Crear una nueva instancia de ReservaDeEntradas con los datos actuales
    ReservaDeEntradas reserva = new ReservaDeEntradas();
    
    // Establecer los datos que ya teníamos
    reserva.setDatosPelicula(pelicula);
    reserva.setDatosHorario(horario);
    reserva.setCantidadButacas(String.valueOf(maxSeleccionadas));
    reserva.setDatosCliente(nombreCliente, apellidoCliente, ci);
    
    // Hacer visible la ventana de ReservaDeEntradas
    reserva.setVisible(true);
    
    // Cerrar la ventana actual de Butacas
    dispose();

    
        
    }//GEN-LAST:event_btn_CerrarActionPerformed

    private void btn_ReservarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ReservarActionPerformed
        if (seleccionadas < maxSeleccionadas) {
        JOptionPane.showMessageDialog(this, 
            "Debe seleccionar " + maxSeleccionadas + " butacas");
        return;
    }
    
    try {
        conn.setAutoCommit(false);
        
        // Verificar si el cliente ya existe y obtener su ID si existe
        String sqlVerificarCliente = "SELECT ID_CLIENTE FROM clientes WHERE CI_CLIENTE = ?";
        PreparedStatement pstVerificar = conn.prepareStatement(sqlVerificarCliente);
        pstVerificar.setString(1, ci);
        ResultSet rsVerificar = pstVerificar.executeQuery();
        
        int idCliente;
        boolean clienteExiste = rsVerificar.next();
        
        if (clienteExiste) {
            // Si el cliente existe, usar su ID
            idCliente = rsVerificar.getInt("ID_CLIENTE");
            
            // Crear un JDialog personalizado que se cerrará automáticamente
            JDialog dialog = new JDialog();
            dialog.setUndecorated(true); // Sin bordes ni botones de ventana
            JLabel mensaje = new JLabel("Cliente ya registrado, continuando con la reserva...");
            mensaje.setFont(new Font("Arial", Font.PLAIN, 14));
            mensaje.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            dialog.add(mensaje);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            
            // Timer para cerrar el diálogo después de 2 segundos
            Timer timer = new Timer(2000, e -> dialog.dispose());
            timer.setRepeats(false);
            timer.start();
            
            dialog.setVisible(true);
        } else {
            // Si el cliente no existe, insertarlo
            String sqlCliente = """
                INSERT INTO clientes 
                (NOMBRE_CLIENTE, APELLIDO_CLIENTE, CI_CLIENTE) 
                VALUES (?, ?, ?)
            """;
            PreparedStatement pstCliente = conn.prepareStatement(sqlCliente, 
                Statement.RETURN_GENERATED_KEYS);
            pstCliente.setString(1, nombreCliente);
            pstCliente.setString(2, apellidoCliente);
            pstCliente.setString(3, ci);
            pstCliente.executeUpdate();
            
            try (ResultSet rsCliente = pstCliente.getGeneratedKeys()) {
                if (rsCliente.next()) {
                    idCliente = rsCliente.getInt(1);
                } else {
                    throw new SQLException("No se pudo obtener el ID del cliente");
                }
            }
        }

        // 1. Obtener ID_HORARIO de la función
        String sqlFuncion = """
            SELECT ID_HORARIO 
            FROM funcion 
            WHERE ID_SALA = ? 
            AND ID_PELICULA = (SELECT ID_PELICULA FROM pelicula WHERE NOMBRE_PELICULA = ?)
            AND FUNCION_INICIO = ?
            AND FECHA_FUNCION = CURRENT_DATE
        """;
        
        PreparedStatement pstFuncion = conn.prepareStatement(sqlFuncion);
        pstFuncion.setString(1, sala);
        pstFuncion.setString(2, pelicula);
        pstFuncion.setString(3, horario);
        ResultSet rsFuncion = pstFuncion.executeQuery();
        
        if (!rsFuncion.next()) {
            throw new SQLException("No se encontró la función especificada");
        }
        
        int idHorario = rsFuncion.getInt("ID_HORARIO");

        // 2. Insertar venta
        String sqlVenta = """
            INSERT INTO venta 
            (ID_CLIENTE, MONTO_TOTAL_VENTA, FECHA_VENTA) 
            VALUES (?, ?, CURRENT_DATE)
        """;
        PreparedStatement pstVenta = conn.prepareStatement(sqlVenta, 
            Statement.RETURN_GENERATED_KEYS);
        pstVenta.setInt(1, idCliente);
        pstVenta.setDouble(2, maxSeleccionadas * precio);
        pstVenta.executeUpdate();
        
        int idVenta = 0;
        try (ResultSet rsVenta = pstVenta.getGeneratedKeys()) {
            if (rsVenta.next()) {
                idVenta = rsVenta.getInt(1);
            }
        }

        // 3. Insertar boleto
        String sqlBoleto = """
            INSERT INTO boleto 
            (ID_VENTA, CANTIDAD_DE_BOLETOS) 
            VALUES (?, ?)
        """;
        PreparedStatement pstBoleto = conn.prepareStatement(sqlBoleto);
        pstBoleto.setInt(1, idVenta);
        pstBoleto.setInt(2, maxSeleccionadas);
        pstBoleto.executeUpdate();

        // 4. Insertar butacas
        String sqlButaca = """
            INSERT INTO butaca 
            (ID_SALA, ID_VENTA, ID_HORARIO, NUMERO_BUTACAS, ESTADO_BUTACAS) 
            VALUES (?, ?, ?, ?, 'OCUPADA')
        """;
        PreparedStatement pstButaca = conn.prepareStatement(sqlButaca);
        
        for (int i = 0; i < butacas.length; i++) {
            if (butacasEstado.get(butacas[i]) && 
                butacas[i].getBackground() == Color.RED) {
                pstButaca.setString(1, sala);
                pstButaca.setInt(2, idVenta);
                pstButaca.setInt(3, idHorario);
                pstButaca.setInt(4, i + 1);
                pstButaca.executeUpdate();
            }
        }

        // 5. Actualizar asientos disponibles
        String sqlUpdateFuncion = """
            UPDATE funcion 
            SET ASIENTOS_DISPONIBLES_FUNCION = CAST(CAST(ASIENTOS_DISPONIBLES_FUNCION AS SIGNED) - ? AS CHAR)
            WHERE ID_HORARIO = ?
        """;
        PreparedStatement pstUpdateFuncion = conn.prepareStatement(sqlUpdateFuncion);
        pstUpdateFuncion.setInt(1, maxSeleccionadas);
        pstUpdateFuncion.setInt(2, idHorario);
        pstUpdateFuncion.executeUpdate();

        // 6. Confirmar transacción
        conn.commit();
        JOptionPane.showMessageDialog(this, "Reserva realizada con éxito");
        dispose();
        
    } catch (SQLException e) {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Error completo: " + e.toString());
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Error al realizar la reserva: " + e.getMessage());
    }
    }//GEN-LAST:event_btn_ReservarActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | 
                 IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Butacas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(() -> {
            new Butacas().setVisible(true);
        });
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Cerrar;
    private javax.swing.JButton btn_Reservar;
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
