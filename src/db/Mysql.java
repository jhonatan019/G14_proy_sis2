
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Mysql {
    private static final String URL = "jdbc:mysql://localhost:3306/cinepeliculon?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("❌ Error en la conexión: " + e.getMessage());
            return null;
        }
    }

    public int registrarPelicula(String nombrePeli, int duracion, String sipnopsis, String genero, String categoria, String rutaImagen) {
        int res = 0;
        Connection cn = getConnection();
        
        if (cn == null) {
            System.out.println("No se pudo establecer la conexión.");
            return res;
        }

        String sql = "INSERT INTO pelicula (NOMBRE_PELICULA, DURACION_PELICULA, SIPNOPSIS_PELICULA, GENERO_PELICULA, CATEGORIA_PELICULA, IMAGEN_PELICULA) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombrePeli);
            ps.setInt(2, duracion);
            ps.setString(3, sipnopsis);
            ps.setString(4, genero);
            ps.setString(5, categoria);
            ps.setString(6, rutaImagen);
            
            res = ps.executeUpdate();
            System.out.println(" Película registrada correctamente.");
            
        } catch (SQLException e) {
            System.out.println(" Error al registrar la película: " + e.getMessage());
        } finally {
            try {
                if (cn != null) cn.close();
            } catch (SQLException e) {
                System.out.println("️ Error al cerrar la conexión: " + e.getMessage());
            }
        }
        return res;
    }
}
