package com.example.ruben.Paginas;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Extra.AzureDB;
import Extra.ListAdapterConsulta;

public class ConsultaOperacion extends AppCompatActivity {

    private String codConsulta = "", nombreConsulta = "", fincaConsulta = "", fechaConsulta = "",
            colorConsulta = "", tipoConsulta = "", observacionesConsulta = "";
    private ArrayList<String> parcelasConsulta = new ArrayList<>(), articulosConsulta = new ArrayList<>();
    private ListAdapterConsulta parcelasAdapter = null, articulosAdapter = null;
    private ListView parcelasView, articulosView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_operacion);

        parcelasView = this.findViewById(R.id.ListViewParcelasConsultarOperacion);
        articulosView = this.findViewById(R.id.ListViewArticulosConsultarOperacion);

        ViewGroup headerParcelas = (ViewGroup) getLayoutInflater().inflate(R.layout.parcelasheader, parcelasView, false);
        ViewGroup headerArticulos = (ViewGroup)getLayoutInflater().inflate(R.layout.articulosheader, articulosView, false);
        parcelasView.setAdapter(parcelasAdapter);
        parcelasView.addHeaderView(headerParcelas);
        parcelasAdapter = new ListAdapterConsulta(this, R.layout.custom_string_view, parcelasConsulta);

        articulosView.setAdapter(articulosAdapter);
        articulosView.addHeaderView(headerArticulos);
        articulosAdapter = new ListAdapterConsulta(this, R.layout.custom_string_view, articulosConsulta);

        Bundle b = getIntent().getExtras();

        String operacion;
        if(b != null) {
            operacion = b.getString("Operacion");
            try {
                obtenerConsultaOperacion(operacion);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        TextView cod = this.findViewById(R.id.CodigoConsultarOperacion);
        TextView nom = this.findViewById(R.id.NombreConsultarOperacion);
        TextView finca = this.findViewById(R.id.FincaConsultarOperacion);
        TextView fecha = this.findViewById(R.id.FechaConsultarOperacion);
        TextView color = this.findViewById(R.id.ColorConsultarOperacion);
        TextView tipo = this.findViewById(R.id.TipoConsultarOperacion);
        TextView observaciones = this.findViewById(R.id.ObservacionesConsultarOperacion);


        cod.setText(codConsulta); nom.setText(nombreConsulta); finca.setText(fincaConsulta);
        fecha.setText(fechaConsulta); color.setText(colorConsulta); tipo.setText(tipoConsulta);
        observaciones.setText(observacionesConsulta);



        Button volverConsultarOperacion = this.findViewById(R.id.BotonVolverConsultarOperacion);

        volverConsultarOperacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    finish();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    public void obtenerConsultaOperacion(String codigo) throws SQLException {
        AzureDB azure = new AzureDB();
        azure.conectar();
        String query = "SELECT * FROM OPERACION WHERE CODIGO='" + codigo + "'";
        Statement stmt = azure.getConexion().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            codConsulta = rs.getString("CODIGO");
            nombreConsulta = rs.getString("NOMBRE");
            fechaConsulta = rs.getString("FECHA");
            int color = Integer.parseInt(rs.getString("COLOR"));
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

            String codTipo = rs.getString("TIPO");
            tipoConsulta = azure.obtenerTipoOperacion(codTipo);
            observacionesConsulta = rs.getString("OBSERVACIONES");

            ArrayList<String> parc = azure.obtenerCODParcelas(codConsulta);
            if(parc.size() == 0) parc.add(azure.obtenerParcela(rs.getString("PARCELA")));
            parcelasConsulta.addAll(parc);


            String arti = rs.getString("ARTICULOS");
            boolean tieneArticulos = azure.tieneArticulos(codTipo);
            if(!arti.equals("") && tieneArticulos) {
                ArrayList<String> art = azure.obtenerCODArticulos(codConsulta);
                if(art.size() == 0) {
                    String newArt = azure.obtenerArticulo(rs.getString("ARTICULOS"));
                    if(!newArt.equals("")) art.add(newArt);
                }
                articulosConsulta.addAll(art);
            }

            fincaConsulta = azure.obtenerFinca(rs.getString("FINCA"));

            parcelasView.setAdapter(parcelasAdapter);
            articulosView.setAdapter(articulosAdapter);

        }
        azure.desconectar();
    }
}
