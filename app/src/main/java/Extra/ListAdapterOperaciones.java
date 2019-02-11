package Extra;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ruben.Paginas.ConsultaOperacion;
import com.example.ruben.Paginas.MainFragmentOperador;
import com.example.ruben.Paginas.ModificarOperacion;
import com.example.ruben.Paginas.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Entidades.Operacion;

import static android.app.Activity.RESULT_OK;

public class ListAdapterOperaciones extends ArrayAdapter<Operacion> {

    private Activity context;
    private int id;
    private ArrayList<Operacion> operaciones = new ArrayList<>();
    private String usuario;

    public ListAdapterOperaciones(Activity context, int resource, ArrayList<Operacion> objects, String usuario) {
        super(context, resource, objects);
        this.context = context;
        this.id = resource;
        this.usuario = usuario;
        this.operaciones = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(id, null);
        }

        /* Variables de consulta */


        /*try {
            obtenerOperaciones();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        final String s = operaciones.get(position).getNombre();
        TextView tvNombre = convertView.findViewById(R.id.nombreOperacion);
        tvNombre.setText(s);

        tvNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ConsultaOperacion.class);
                i.putExtra("Operacion", operaciones.get(position).getCodOperacion());
                getContext().startActivity(i);
                notifyDataSetChanged();
            }
        });

        final ImageButton b2 = convertView.findViewById(R.id.botonBorrarOperacion);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    borrarOperacion(operaciones.get(position).getCodOperacion());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                operaciones.remove(position);
                notifyDataSetChanged();
            }
        });

        final ImageButton b3 = convertView.findViewById(R.id.botonModificarOperacion);

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ModificarOperacion.class);
                i.putExtra("Operacion", operaciones.get(position).getNombre());
                getContext().startActivity(i);
            }
        });

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void obtenerOperaciones() throws SQLException {
        final Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
        String atoday = s.format(new Date(cal.getTimeInMillis()));
        cal.add(Calendar.DAY_OF_YEAR, -7);
        String aweek = s.format(new Date(cal.getTimeInMillis()));
        AzureDB azure = new AzureDB();
        azure.conectar();
        operaciones.clear();
        String query = "SELECT * FROM OPERACION WHERE USUARIO='" + usuario + "' AND FECHA BETWEEN '" + aweek + "' AND '" + atoday + "'";
        //String query = "SELECT * FROM OPERACION WHERE USUARIO='" + usuario + "' AND FECHA >= '" + aweek + "'";
        Statement stmt = azure.getConexion().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            while (rs.next()) {
                operaciones.add(new Operacion(rs.getString("CODIGO"), rs.getString("NOMBRE"), rs.getString("FECHA")));
            }
        }
        azure.desconectar();
    }

    public void borrarOperacion(String codigo) throws SQLException {
        AzureDB azure = new AzureDB();
        azure.conectar();
        String query = "DELETE FROM CONJUNTO_PARCELAS WHERE OPERACION='" + codigo + "'";
        Statement stmt = azure.getConexion().createStatement();
        boolean rs = stmt.execute(query);
        if (!rs) {
            String query2 = "DELETE FROM CONJUNTO_ARTICULOS WHERE OPERACION='" + codigo + "'";
            Statement stmt2 = azure.getConexion().createStatement();
            rs = stmt2.execute(query2);
            if(!rs) {
                String query3 = "DELETE FROM COORDENADAS_OPERACION WHERE OPERACION='" + codigo + "'";
                Statement stmt3 = azure.getConexion().createStatement();
                rs = stmt3.execute(query3);
                if(!rs) {
                    String query4 = "DELETE FROM OPERACION WHERE CODIGO='" + codigo + "'";
                    Statement stmt4 = azure.getConexion().createStatement();
                    rs = stmt4.execute(query4);
                    if (!rs) {
                        Toast.makeText(getContext(), "Operacion borrada correctamente", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        azure.desconectar();
    }

    public String obtenerCODOperacion(String nombre, AzureDB azure) throws SQLException {
        String codOp = "";
        String query = "SELECT * FROM OPERACION WHERE NOMBRE='" + nombre + "'";
        Statement stmt = azure.getConexion().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            if (rs.next()) {
                codOp = rs.getString("CODIGO");
            }
        }
        return codOp;
    }
}

                /*

                dialog.show();
            }
        });

        final ImageButton b = convertView.findViewById(R.id.botonModificarOperacion);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final ImageButton b2 = convertView.findViewById(R.id.botonBorrarOperacion);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    borrarOperacion(operaciones.get(position).getNombre());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                operaciones.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void borrarOperacion(String nombre) throws SQLException {
        AzureDB azure = new AzureDB();
        azure.conectar();
        String codOP = obtenerCODOperacion(nombre, azure);
        String query = "DELETE FROM COORDENADAS_OPERACION WHERE OPERACION='" + codOP + "'";
        Statement stmt = azure.getConexion().createStatement();
        boolean rs = stmt.execute(query);
        if (!rs) {
            query = "DELETE FROM OPERACION WHERE NOMBRE='" + nombre + "'";
            Statement stmt2 = azure.getConexion().createStatement();
            rs = stmt2.execute(query);
            if (!rs) {
                Toast.makeText(getContext(), "Operacion borrada correctamente", Toast.LENGTH_SHORT).show();
            }
        }
        azure.desconectar();
    }

    public String obtenerCODOperacion(String nombre, AzureDB azure) throws SQLException {
        String codOp = "";
        String query = "SELECT * FROM OPERACION WHERE NOMBRE='" + nombre + "'";
        Statement stmt = azure.getConexion().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            if (rs.next()) {
                codOp = rs.getString("CODIGO");
            }
        }
        return codOp;
    }

    public void obtenerConsultaOperacion(String nombre) throws SQLException {
        AzureDB azure = new AzureDB();
        azure.conectar();
        String codOP = obtenerCODOperacion(nombre, azure);
        String query = "SELECT * FROM OPERACION WHERE CODIGO='" + codOP + "'";
        Statement stmt = azure.getConexion().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            codConsulta = rs.getString("CODIGO");
            nombreConsulta = rs.getString("NOMBRE");
            fincaConsulta = obtenerCODFinca(rs.getString("PARCELA"), azure);
            fechaConsulta = rs.getString("FECHA");
            int color = rs.getInt("COLOR");

            switch (color) {
                case Color.BLUE:
                    colorConsulta = "Azul";
                    break;
                case Color.GREEN:
                    colorConsulta = "Verde";
                    break;
                case Color.RED:
                    colorConsulta = "Rojo";
                    break;
                case Color.YELLOW:
                    colorConsulta = "Amarillo";
                    break;
                case Color.MAGENTA:
                    colorConsulta = "Magenta";
                    break;
                case Color.CYAN:
                    colorConsulta = "Cyan";
                    break;
                case Color.BLACK:
                    colorConsulta = "Negro";
                    break;
                case Color.WHITE:
                    colorConsulta = "Blanco";
                    break;
            }

            tipoConsulta = rs.getString("TIPO");
            observacionesConsulta = rs.getString("OBSERVACIONES");
            parcelasConsulta.add(rs.getString("PARCELA"));
            articulosConsulta = obtenerCODArticulos(rs.getString("ARTICULOS"), azure);

        }
        azure.desconectar();
    }

    public String obtenerCODFinca(String nombre, AzureDB azure) throws SQLException {
        String codF = "";
        String query = "SELECT * FROM PARCELA WHERE CODIGO='" + nombre + "'";
        Statement stmt = azure.getConexion().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            if (rs.next()) {
                codF = rs.getString("FINCA");
            }
        }

        return codF;
    }

    public ArrayList<String> obtenerCODArticulos(String nombre, AzureDB azure) throws SQLException {
        ArrayList<String> art = new ArrayList<>();
        String query = "SELECT * FROM CONJUNTO_ARTICULOS WHERE NOMBRE LIKE '" + nombre + "%'";
        Statement stmt = azure.getConexion().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            while (rs.next()) {
                String articulo = obtenerArticulo(rs.getString("ARTICULO"), azure);
                art.add(articulo);
            }
        }

        return art;
    }

    public String obtenerArticulo(String codigo, AzureDB azure) throws SQLException {
        String nomArt = "";
        String query = "SELECT * FROM ARTICULO WHERE CODIGO='" + codigo + "'";
        Statement stmt = azure.getConexion().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            if (rs.next()) {
                nomArt = rs.getString("NOMBRE");
            }
        }
        return nomArt;
    }

}*/
