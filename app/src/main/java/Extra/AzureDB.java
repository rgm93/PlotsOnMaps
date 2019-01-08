package Extra;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Entidades.Usuario;

public class AzureDB extends AsyncTask<String, String, String> {

    public Connection conexion;
    public String mensajeConexion = "";
    public String name = "";

    public AzureDB () {}

    @Override
    protected String doInBackground(String... strings) {
        conexion = conectar();
        if(conexion == null) {
            mensajeConexion = "¡Verifica tu conexión de internet!";
        } else {
            mensajeConexion = "¡Conexión realizada!";
        }
        return mensajeConexion;
    }

    @SuppressLint("AuthLeak")
    public Connection conectar() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String conexionURL;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conexionURL = "jdbc:jtds:sqlserver://plotsonmaps.database.windows.net:1433;DatabaseName=PlotsOnMapsDB;user=rgm93@plotsonmaps;password=Ruben07091993plotsonmaps;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;";
            conexion = DriverManager.getConnection(conexionURL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return conexion;
    }

    public void desconectar() {
        try {
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Connection getConexion() { return conexion; }

    public Usuario comprobarUsuario(String username, String password) throws SQLException {
        Usuario usuario = null;
        if(conexion != null) {
            String query = "SELECT * FROM USUARIO WHERE USERNAME='" + username + "' AND PASS='" + password + "'";
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs != null) {
                if (rs.next()) {
                    usuario = new Usuario(rs.getString("CODIGO"), rs.getString("USERNAME"), rs.getString("PASS"), rs.getString("TIPO"));
                }
            }
        }

        return usuario;
    }

    public String obtenerCODOperacion(String nombre) throws SQLException {
        String codOp = "";
        String query = "SELECT * FROM OPERACION WHERE NOMBRE='" + nombre + "'";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            if (rs.next()) {
                codOp = rs.getString("CODIGO");
            }
        }
        return codOp;
    }

    public String obtenerFinca(String codigo) throws SQLException {
        String nomFinca = "";
        String query = "SELECT * FROM FINCA WHERE CODIGO='" + codigo + "'";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            if (rs.next()) {
                nomFinca = rs.getString("NOMBRE");
            }
        }
        return nomFinca;
    }

    public ArrayList<String> obtenerCODArticulos(String codigo) throws SQLException {
        ArrayList<String> art = new ArrayList<>();
        String query = "SELECT * FROM CONJUNTO_ARTICULOS WHERE NOMBRE LIKE '" + codigo + "%'";
        String articulo = "";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            while (rs.next()) {
                articulo = obtenerArticulo(rs.getString("ARTICULO"));
                art.add(articulo);
            }
        }

        if(articulo.equals("")) {
            String query2 = "SELECT * FROM ARTICULO WHERE CODIGO='" + codigo + "'";
            Statement stmt2 = conexion.createStatement();
            ResultSet rs2 = stmt2.executeQuery(query2);
            if (rs2 != null) {
                if (rs2.next()) {
                    articulo = rs2.getString("NOMBRE");
                    if(!articulo.equals(" - "))  art.add(articulo);
                }
            }
        }

        return art;
    }

    public ArrayList<String> obtenerCODParcelas(String codigo) throws SQLException {
        ArrayList<String> parc = new ArrayList<>();
        String query = "SELECT * FROM CONJUNTO_PARCELAS WHERE NOMBRE = '" + codigo + "'";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String parcela = "";
        if (rs != null) {
            while (rs.next()) {
                parcela = obtenerParcela(rs.getString("PARCELA"));
                parc.add(parcela);
            }
        }

        if (parcela.equals("")){
            String query2 = "SELECT * FROM PARCELA WHERE CODIGO = '" + codigo + "'";
            Statement stmt2 = conexion.createStatement();
            ResultSet rs2 = stmt2.executeQuery(query2);
            if (rs2 != null) {
                while (rs2.next()) {
                    String parcela2 = rs2.getString("NOMBRE");
                    parc.add(parcela2);
                }
            }
        }

        return parc;
    }

    public String obtenerParcela(String codigo) throws SQLException {
        String nomParc = "";
        String query = "SELECT * FROM PARCELA WHERE CODIGO='" + codigo + "'";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            if (rs.next()) {
                nomParc = rs.getString("NOMBRE");
            }
        }
        return nomParc;
    }

    public String obtenerArticulo(String codigo) throws SQLException {
        String nomArt = "";
        String query = "SELECT * FROM ARTICULO WHERE CODIGO='" + codigo + "'";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            if (rs.next()) {
                nomArt = rs.getString("NOMBRE");
            }
        }
        return nomArt;
    }

    public String obtenerTipoOperacion(String codigo) throws SQLException {
        String nomTp = "";
        String query = "SELECT * FROM TIPO_OPERACION WHERE CODIGO='" + codigo + "'";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            if (rs.next()) {
                nomTp = rs.getString("NOMBRE");
            }
        }
        return nomTp;
    }

    public boolean existeOperacion(String cod) throws SQLException {
        boolean existe = false;
        String query = "SELECT * FROM OPERACION WHERE CODIGO='" + cod + "'";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            existe = true;
        }
        return existe;
    }

    public boolean existeOperacionNombre(String nombre) throws SQLException {
        boolean existe = false;
        String query = "SELECT * FROM OPERACION WHERE NOMBRE='" + nombre + "'";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            existe = true;
        }
        return existe;
    }

    public int obtenerColorFinca(String codigo) throws SQLException {
        int color = 0;
        String query = "SELECT * FROM FINCA WHERE CODIGO='" + codigo + "'";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            if (rs.next()) {
                color = rs.getInt("COLOR");
            }
        }
        return color;
    }



    public boolean hayFincas() throws SQLException {
        boolean valido = false;
        String query;
        query = "SELECT * FROM FINCA";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            valido = true;
        }
        return valido;
    }

    public boolean hayParcelas() throws SQLException {
        boolean valido = false;
        String query;
        query = "SELECT * FROM PARCELA";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            valido = true;
        }
        return valido;
    }

    public boolean hayArticulos() throws SQLException {
        boolean valido = false;
        String query;
        query = "SELECT * FROM ARTICULO";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            valido = true;
        }
        return valido;
    }

    public boolean existeConjuntoArticulo(String articulo, String operacion) throws SQLException {
        boolean existe = false;
        String query = "SELECT * FROM OPERACION WHERE ARTICULOS='" + articulo + "' AND NOMBRE='" + operacion + "'";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            existe = true;
        }
        return existe;
    }

    public String obtenerCODArticulo(String nombre) throws SQLException {
        String codArt = "";
        String query = "SELECT * FROM ARTICULO WHERE NOMBRE='" + nombre + "'";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            if (rs.next()) {
                codArt = rs.getString("CODIGO");
            }
        }
        return codArt;
    }



    public String obtenerCODFinca(String nombre) throws SQLException {
        String codf = "";
        String query = "SELECT * FROM FINCA WHERE NOMBRE='" + nombre + "'";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            if (rs.next()) {
                codf = rs.getString("CODIGO");
            }
        }
        return codf;
    }

    public String obtenerCODFincaDesdeParcela(String codigo) throws SQLException {
        String codf = "";
        String query = "SELECT * FROM PARCELA WHERE CODIGO='" + codigo + "'";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            if (rs.next()) {
                codf = rs.getString("FINCA");
            }
        }
        return codf;
    }

    public String obtenerCODParcela(String nombre) throws SQLException {
        String codp = "";
        String query = "SELECT * FROM PARCELA WHERE NOMBRE='" + nombre + "'";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            if (rs.next()) {
                codp = rs.getString("CODIGO");
            }
        }

        return codp;
    }

    public String obtenerCODTipoOperacion(String nombre) throws SQLException {
        String codTOp = "";
        String query = "SELECT * FROM TIPO_OPERACION WHERE NOMBRE='" + nombre + "'";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            if (rs.next()) {
                codTOp = rs.getString("CODIGO");
            }
        }
        return codTOp;
    }
}
