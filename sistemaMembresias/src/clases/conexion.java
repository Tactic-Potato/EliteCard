package clases;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexion {
    private String url;
    private String usuario;
    private String password;

    public conexion(String url, String usuario, String password) {
        this.url = url;
        this.usuario = usuario;
        this.password = password;
    }

    public Connection conectar() throws SQLException {
        Connection conn = DriverManager.getConnection(url, usuario, password);
        return conn;
    }

    public void desconectar(Connection conn) throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
}