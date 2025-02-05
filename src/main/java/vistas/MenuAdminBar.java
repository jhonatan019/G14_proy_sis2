
/* OJO PIOJO
 */
package vistas;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

import vistas.PanelRegistrarPelicula;
import vistas.CrearSucursal;
//import vistas.;
//import vistas.;
import vistas.ListaDePeliculas;
/*import vistas.;
import vistas.;
import vistas.;
import vistas.;*/

/**
 *
 * @author LENNY
 */
public class MenuAdminBar extends javax.swing.JFrame {
    
    private JMenuItem mniRegistrar;
    private JMenuItem mniFunciones;
    private JMenuItem mniReportes;
    private JMenuItem mniCerrarSesion;

    private JMenu menuRegistrar;
    private JMenu menuFunciones;
    private JMenu menuReportes;

    private JMenuItem mniPelicula;
    private JMenuItem mniSucursal;
    private JMenuItem mniEmpleado;

    private JMenuItem mniHorario;
    private JMenuItem mniListaPeliculas;
    private JMenuItem mniSalas;

    private JMenuItem mniTop5;
    private JMenuItem mniMasIngresos;
    private JMenuItem mniPeliculaMasVendida;
    
    Color mColorFondo = new Color(94, 17, 47);

    /**
     * Creates new form MenuAdminBar
     */
    public MenuAdminBar() {
        initComponents();
        
        //Menu
        menuRegistrar = new JMenu("REGISTRAR");
        menuFunciones = new JMenu("FUNCIONES");
        menuReportes = new JMenu("REPORTES");

        //Crear ítems para "Registrar"
        mniPelicula = new JMenuItem("Película");
        mniSucursal = new JMenuItem("Sucursal");
        mniEmpleado = new JMenuItem("Empleado");

        // para "Funciones"
        mniHorario = new JMenuItem("Asignar Horarios");
        mniListaPeliculas = new JMenuItem("Agregar Película-Sucursal");
        mniSalas = new JMenuItem("Gestionar Salas");

        //para "Reportes"
        mniTop5 = new JMenuItem("TOP 5 FAVORITOS");
        mniMasIngresos = new JMenuItem("Sucursal con mas Ingresos");
        mniPeliculaMasVendida = new JMenuItem("Película Más Vendida");

        mniCerrarSesion = new JMenuItem("CERRAR SESIÓN");

        // Agregar ítems al menu "Registrar"
        menuRegistrar.add(mniPelicula);
        menuRegistrar.add(mniSucursal);
        menuRegistrar.add(mniEmpleado);

        // Agregar ítems al menu "Funciones"
        menuFunciones.add(mniHorario);
        menuFunciones.add(mniListaPeliculas);
        menuFunciones.add(mniSalas);

        // Agregar ítems al menu "Reportes"
        menuReportes.add(mniTop5);
        menuReportes.add(mniMasIngresos);
        menuReportes.add(mniPeliculaMasVendida);

        // Agregar Menus a la barra superior de menu bar
        MenuBar.add(menuRegistrar);
        MenuBar.add(menuFunciones);
        MenuBar.add(menuReportes);
        MenuBar.add(mniCerrarSesion);

        configurarEstilos();
        agregarListeners();
    }
    private void configurarEstilos() {
        Font fuente = new Font("Segoe UI", Font.BOLD, 16);
        EmptyBorder margen = new EmptyBorder(10, 20, 10, 20);

        menuRegistrar.setOpaque(true);
        menuRegistrar.setBackground(mColorFondo);
        menuRegistrar.setForeground(Color.white);
        menuRegistrar.setFont(fuente);
        menuRegistrar.setBorder(margen); //Aplicar margenes

        menuFunciones.setOpaque(true);
        menuFunciones.setBackground(mColorFondo);
        menuFunciones.setForeground(Color.white);
        menuFunciones.setFont(fuente);
        menuFunciones.setBorder(margen);

        menuReportes.setOpaque(true);
        menuReportes.setBackground(mColorFondo);
        menuReportes.setForeground(Color.white);
        menuReportes.setFont(fuente);
        menuReportes.setBorder(margen);

        mniCerrarSesion.setOpaque(true);
        mniCerrarSesion.setBackground(mColorFondo);
        mniCerrarSesion.setForeground(Color.white);
        mniCerrarSesion.setFont(fuente);
        mniCerrarSesion.setBorder(margen);

        // Aplicamos la fuentey el margen alos items
        mniPelicula.setFont(fuente);
        mniPelicula.setBorder(margen);

        mniSucursal.setFont(fuente);
        mniSucursal.setBorder(margen);

        mniEmpleado.setFont(fuente);
        mniEmpleado.setBorder(margen);

        mniHorario.setFont(fuente);
        mniHorario.setBorder(margen);

        mniListaPeliculas.setFont(fuente);
        mniListaPeliculas.setBorder(margen);

        mniSalas.setFont(fuente);
        mniSalas.setBorder(margen);

        mniTop5.setFont(fuente);
        mniTop5.setBorder(margen);

        mniMasIngresos.setFont(fuente);
        mniMasIngresos.setBorder(margen);

        mniPeliculaMasVendida.setFont(fuente);
        mniPeliculaMasVendida.setBorder(margen);
    }

    private void agregarListeners() {
        mniPelicula.addActionListener(e -> {
            new PanelRegistrarPelicula().setVisible(true); // Abre vista PanelRegistrarPelicula
        });

        mniSucursal.addActionListener(e -> {
            new CrearSucursal().setVisible(true); // Abre la vista CrearSucursal
        });

        //AUMENTEN LAS DEMAS VISTAS.
        /*mniEmpleado.addActionListener(e -> {
            new //PONER AQUI EL NOMBRE DE LA  VISTA//().setVisible(true); //PARA REGISTRO DE EMMPLEADOS
        });

        mniHorario.addActionListener(e -> {
            new //PONER AQUI EL NOMBRE DE LA  VISTA//().setVisible(true); //PARA ASIGNAR HORARIO DE LAS FUNCIONES
        });*/
        
        mniListaPeliculas.addActionListener(e -> {
            new ListaDePeliculas("").setVisible(true);
        });

        /*mniSalas.addActionListener(e -> {
            new //PONER AQUI EL NOMBRE DE LA  VISTA//().setVisible(true); //PARA GESTION DE SALAS
        });

        mniTop5.addActionListener(e -> {
            new //PONER AQUI EL NOMBRE DE LA  VISTA//().setVisible(true); //PARA TOP 5 PELICULAS
        });

        mniMasIngresos.addActionListener(e -> {
            new //PONER AQUI EL NOMBRE DE LA  VISTA//().setVisible(true); //PARA SUCURSAL CON MAYOR INGRESOS
        });

        mniPeliculaMasVendida.addActionListener(e -> {
            new //PONER AQUI EL NOMBRE DE LA  VISTA//().setVisible(true); //PARA PELICULA MAS VENDIDA
        });*/


    mniCerrarSesion.addActionListener(e -> {
        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Estás seguro de que deseas cerrar sesión?",
            "Cerrar Sesión",
            JOptionPane.YES_NO_OPTION
        );
        if (respuesta == JOptionPane.YES_OPTION) {
            System.exit(0); // Cierra la aplicación
        }
    });
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
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        MenuBar = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(38, 29, 41));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Serif", 1, 30)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("✨🎬 Bienvenido al Panel de Administración de CINE PELICULÓN 🎥🍿");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Aquí tienes el control total para llevar la magia del cine a todos nuestros espectadores.");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, -1, 60));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Gestiona películas, horarios, salas y más de manera eficiente y sin complicaciones.");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 270, -1, -1));

        jLabel5.setFont(new java.awt.Font("Serif", 3, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("🚀 Optimiza la experiencia, administra con facilidad y haz que cada función sea inolvidable.");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 300, -1, -1));

        jLabel6.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("📽️ ¡Luces, cámara... acción! 🎟️✨\n");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 390, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/LOGOCENTRO.jpg"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 600));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 600));
        setJMenuBar(MenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(MenuAdminBar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuAdminBar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuAdminBar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuAdminBar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuAdminBar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
