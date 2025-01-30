
package dba;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MySQL {
    public Connection cn;

    public static Connection getConnection() {
        Connection cn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinepeliculon?useSSL=false&serverTimezone=UTC", "root", "");
            
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el driver de MySQL.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return cn;
    }
    public static void main(String[] args) {
        try (Connection cn = getConnection()) {
            if (cn != null) {
                System.out.println("Conexión verificada correctamente.");
            } else {
                System.out.println("No se pudo conectar a la base de datos.");
            }
        } catch (Exception e) {
            System.out.println("Error general: " + e.getMessage());
        }
}
}

