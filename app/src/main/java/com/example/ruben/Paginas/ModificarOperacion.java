package com.example.ruben.Paginas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import Entidades.Articulo;
import Entidades.Parcela;
import Extra.AzureDB;
import Extra.CheckboxAdapterArticulos;
import Extra.CheckboxAdapterParcela;
import Extra.ListAdapterArticulos;
import Extra.ListAdapterConsulta;

public class ModificarOperacion extends AppCompatActivity {

    private String codConsulta = "", nombreConsulta = "", fincaConsulta = "", fechaConsulta = "",
            colorConsulta = "", tipoConsulta = "", observacionesConsulta = "";
    private ArrayList<String> parcelasConsulta = new ArrayList<>();
    private ArrayList<String> articulosConsulta = new ArrayList<>();
    private ListAdapterConsulta parcelasAdapter = null;
    private ListAdapterArticulos articulosAdapter = null;
    private ListView parcelasView, articulosView;
    private String[] coloresArray = {"AZUL"};
    private int colorPos = 0, color = 0;
    private TextView fecha, observaciones;
    private AzureDB azure = new AzureDB();
    private ArrayList<Articulo> listCArticulos;
    private CheckboxAdapterArticulos articulosCAdapter;
    private ListView checkedArticulos;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_operacion);

        parcelasView = this.findViewById(R.id.ListViewParcelasModificarOperacion);
        articulosView = this.findViewById(R.id.ListViewArticulosModificarOperacion);
        ViewGroup headerParcelas = (ViewGroup) getLayoutInflater().inflate(R.layout.parcelasheader, parcelasView, false);
        ViewGroup headerArticulos = (ViewGroup)getLayoutInflater().inflate(R.layout.articulosheader, articulosView, false);
        parcelasView.setAdapter(parcelasAdapter);
        parcelasView.addHeaderView(headerParcelas);
        parcelasAdapter = new ListAdapterConsulta(this, R.layout.custom_string_view, parcelasConsulta);


        /*final Spinner SpinnerColorModificarOperacion = this.findViewById(R.id.ColorModificarOperacion);
        ArrayAdapter<String> listColores = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, coloresArray);
        listColores.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerColorModificarOperacion.setAdapter(listColores);*/

        listCArticulos = new ArrayList<>();
        try {
            obtenerArticulos("");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        articulosView.setAdapter(articulosAdapter);
        articulosView.addHeaderView(headerArticulos);
        articulosAdapter = new ListAdapterArticulos(ModificarOperacion.this, R.layout.custom_listarticulos_view, articulosConsulta, listCArticulos);

        Bundle b = getIntent().getExtras();

        String operacion = "";
        if(b != null) {
            operacion = b.getString("Operacion");
            try {
                obtenerConsultaOperacion(operacion);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }



        TextView cod = this.findViewById(R.id.CodigoModificarOperacion);
        TextView nom = this.findViewById(R.id.NombreModificarOperacion);
        TextView finca = this.findViewById(R.id.FincaModificarOperacion);
        TextView col = this.findViewById(R.id.ColorModificarOperacion);
        fecha = this.findViewById(R.id.ModificarFechaOperacion);
        TextView tipo = this.findViewById(R.id.TipoModificarOperacion);
        observaciones = this.findViewById(R.id.ObservacionesModificarOperacion);

        cod.setText(codConsulta); nom.setText(nombreConsulta); finca.setText(fincaConsulta);
        col.setText(colorConsulta); fecha.setText(fechaConsulta); tipo.setText(tipoConsulta);
        observaciones.setText(observacionesConsulta);


        //SpinnerColorModificarOperacion.setSelection(colorPos);
        /*SpinnerColorModificarOperacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        colorConsulta = "Azul";
                        color = Color.BLUE;
                        break;
                    case 1:
                        colorConsulta = "Verde";
                        color = Color.GREEN;
                        break;
                    case 2:
                        colorConsulta = "Rojo";
                        color = Color.RED;
                        break;
                    case 3:
                        colorConsulta = "Amarillo";
                        color = Color.YELLOW;
                        break;
                    case 4:
                        colorConsulta = "Magenta";
                        color = Color.MAGENTA;
                        break;
                    case 5:
                        colorConsulta = "Cyan";
                        color = Color.CYAN;
                        break;
                    case 6:
                        colorConsulta = "Negro";
                        color = Color.BLACK;
                        break;
                    case 7:
                        colorConsulta = "Blanco";
                        color = Color.WHITE;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });*/

        Calendar cal = Calendar.getInstance();
        fecha = this.findViewById(R.id.ModificarFechaOperacion);
        fecha.setText(cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR));
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        fecha.setText(date);
                    }
                };
                new DatePickerDialog(fecha.getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        observaciones = this.findViewById(R.id.ObservacionesModificarOperacion);
        observaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ModificarOperacion.this);
                dialog.setContentView(R.layout.comentarios_dialog);
                Button aceptarObservacion = dialog.findViewById(R.id.BotonAceptarObservacion);
                Button cancelarObservacion = dialog.findViewById(R.id.BotonCancelarObservacion);

                final EditText cajaComentarios = dialog.findViewById(R.id.cajaComentarios);

                aceptarObservacion.setEnabled(true);
                aceptarObservacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cajaComentarios.getText().toString().equals("")) cajaComentarios.setText("Observaciones");
                        observaciones.setText(cajaComentarios.getText().toString());
                        dialog.dismiss();
                    }
                });

                cancelarObservacion.setEnabled(true);
                cancelarObservacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
                dialog.show();
            }
        });

        final Button aceptarModificarOperacion = this.findViewById(R.id.BotonAceptarModificarOperacion);

        final String finalOperacion = operacion;
        aceptarModificarOperacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    actualizarOperacion(finalOperacion, fecha.getText().toString(), observaciones.getText().toString(), articulosConsulta);
                    finish();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });

        final Button cancelarModificarOperacion = this.findViewById(R.id.BotonCancelarModificarOperacion);
        cancelarModificarOperacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    finish();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });


        checkedArticulos = this.findViewById(R.id.ListViewArticulosDBCrearOperacion);
        checkedArticulos.addHeaderView(headerArticulos);



        final GridLayout contornoArticulos = this.findViewById(R.id.GridArticulosModificarOperacion);
        Button addArticulo;
        final ConstraintLayout UIAddArticulo = this.findViewById(R.id.UIModificarOperacionAddArticulo);
        addArticulo = this.findViewById(R.id.BotonAddArticuloModificarOperacion);
        addArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    azure.conectar();
                    if (azure.hayArticulos()) {
                        actualizarCheckedArticulos();
                        UIAddArticulo.setVisibility(View.VISIBLE);
                        aceptarModificarOperacion.setVisibility(View.INVISIBLE);
                        cancelarModificarOperacion.setVisibility(View.INVISIBLE);
                        parcelasView.setVisibility(View.INVISIBLE);
                        articulosView.setVisibility(View.INVISIBLE);
                        checkedArticulos.setAdapter(articulosCAdapter);
                        contornoArticulos.setVisibility(View.INVISIBLE);
                        /*listCArticulos = new ArrayList<>();
                        try {
                            obtenerArticulos("");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }*/

                        //articulosCAdapter = new CheckboxAdapterArticulos(ModificarOperacion.this, R.layout.custom_checklist_view, listCArticulos);
                        //checkedArticulos.setAdapter(articulosCAdapter);
                        //obtenerTodo();
                    } else {
                        Toast.makeText(ModificarOperacion.this, "No hay artículos disponibles", Toast.LENGTH_SHORT).show();
                    }
                    azure.desconectar();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        final EditText filtrarArticulo = this.findViewById(R.id.buscarArticulo);
        filtrarArticulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    obtenerArticulos(s.toString());
                    checkedArticulos.setAdapter(articulosCAdapter);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final Button botonAceptarArticulos = this.findViewById(R.id.BotonAceptarArticulos);
        botonAceptarArticulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                articulosConsulta.clear();
                if (articulosCAdapter.getNumArticulos() > 0) {
                    for (Articulo a : listCArticulos) {
                        if (a.esTildado()) {
                            articulosConsulta.add(a.getNomArticulo());
                        }
                    }


                    articulosView.setAdapter(articulosAdapter);
                    articulosAdapter = new ListAdapterArticulos(ModificarOperacion.this, R.layout.custom_listarticulos_view, articulosConsulta, listCArticulos);
                    parcelasView.setVisibility(View.VISIBLE);
                    articulosView.setVisibility(View.VISIBLE);
                    aceptarModificarOperacion.setVisibility(View.VISIBLE);
                    cancelarModificarOperacion.setVisibility(View.VISIBLE);
                    contornoArticulos.setVisibility(View.VISIBLE);
                    UIAddArticulo.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(ModificarOperacion.this, "No hay artículos disponibles", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button botonCancelarArticulos = this.findViewById(R.id.BotonCancelarArticulos);
        botonCancelarArticulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIAddArticulo.setVisibility(View.INVISIBLE);
                aceptarModificarOperacion.setVisibility(View.VISIBLE);
                cancelarModificarOperacion.setVisibility(View.VISIBLE);
                aceptarModificarOperacion.setVisibility(View.VISIBLE);
                cancelarModificarOperacion.setVisibility(View.VISIBLE);
                contornoArticulos.setVisibility(View.VISIBLE);
                parcelasView.setVisibility(View.VISIBLE);
                articulosView.setVisibility(View.VISIBLE);
                articulosView.setAdapter(articulosAdapter);
                articulosAdapter = new ListAdapterArticulos(ModificarOperacion.this, R.layout.custom_listarticulos_view, articulosConsulta, listCArticulos);

            }
        });
    }

    public void obtenerArticulos(String nombre) throws SQLException {
        azure.conectar();
        listCArticulos.clear();
        String query = "";
        if (nombre.equals("")) {
            query = "SELECT * FROM ARTICULO";
        } else query = "SELECT * FROM ARTICULO WHERE NOMBRE LIKE '" + nombre + "%'";
        Statement stmt = azure.getConexion().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            while (rs.next()) {
                listCArticulos.add(new Articulo(rs.getString("CODIGO"), rs.getString("NOMBRE"), /*rs.getString("DESCRIPCION"),*/ false));
            }
        }
        azure.desconectar();
    }

    public void actualizarCheckedArticulos() {

        for(int i = 0; i < articulosConsulta.size(); i++) {
            for (int j = 0; j < listCArticulos.size(); j++) {
                if(articulosConsulta.get(i).equals(listCArticulos.get(j).getNomArticulo())) {
                    listCArticulos.get(j).setTildado(true);
                }
            }
        }

        articulosCAdapter = new CheckboxAdapterArticulos(ModificarOperacion.this, R.layout.custom_checklist_view, listCArticulos);
        checkedArticulos.setAdapter(articulosCAdapter);
    }

    public void actualizarOperacion(String nombre, String fecha, String observaciones, ArrayList<String> listArticulos) throws SQLException {
        AzureDB azure = new AzureDB();
        azure.conectar();
        String codconja = "CART_";
        boolean existeConjunto = false;
        if(listArticulos.size() > 1) {
            for (int j = 0; j < listArticulos.size(); j++) {
                codconja += listArticulos.get(j).charAt(0) + "_";
            }
            codconja += nombre;

            if(!azure.existeConjuntoArticulo(codconja, nombre)) {
                existeConjunto = true;
                String queryArt = "";
                for (int i = 0; i < listArticulos.size(); i++) {
                    queryArt += "INSERT INTO CONJUNTO_ARTICULOS (NOMBRE, ARTICULO) VALUES ('" + codconja + "', '" + azure.obtenerCODArticulo(listArticulos.get(i)) + "');";
                }

                Statement stmtArt = azure.getConexion().createStatement();
                stmtArt.execute(queryArt);
            }
        } else if (listArticulos.size() == 1) {
            if(!azure.existeConjuntoArticulo(codconja, nombre)) {
                existeConjunto = true;
                codconja = azure.obtenerCODArticulo(listArticulos.get(0));
            }
        } else codconja = " - ";

        if(existeConjunto) {
            try
            {
                PreparedStatement ps = azure.getConexion().prepareStatement(
                        "UPDATE OPERACION SET FECHA = ?, OBSERVACIONES = ?, ARTICULOS = ? WHERE NOMBRE = ?");

                // set the preparedstatement parameters
                ps.setString(1, fecha);
                ps.setInt(2, color);
                ps.setString(3, observaciones);
                ps.setString(4, codconja);
                ps.setString(5, nombre);

                ps.executeUpdate();
                ps.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

    }



    public void obtenerConsultaOperacion(String nombre) throws SQLException {
        AzureDB azure = new AzureDB();
        azure.conectar();
        String codOP = azure.obtenerCODOperacion(nombre);
        String query = "SELECT * FROM OPERACION WHERE CODIGO='" + codOP + "'";
        Statement stmt = azure.getConexion().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            codConsulta = rs.getString("CODIGO");
            nombreConsulta = rs.getString("NOMBRE");
            fechaConsulta = rs.getString("FECHA");
            int color = rs.getInt("COLOR");

            switch (color) {
                case Color.BLUE:
                    colorConsulta = "Azul";
                    color = Color.BLUE;
                    colorPos = 0;
                    break;
                case Color.GREEN:
                    colorConsulta = "Verde";
                    color = Color.GREEN;
                    colorPos = 1;
                    break;
                case Color.RED:
                    colorConsulta = "Rojo";
                    color = Color.RED;
                    colorPos = 2;
                    break;
                case Color.YELLOW:
                    colorConsulta = "Amarillo";
                    color = Color.YELLOW;
                    colorPos = 3;
                    break;
                case Color.MAGENTA:
                    colorConsulta = "Magenta";
                    color = Color.MAGENTA;
                    colorPos = 4;
                    break;
                case Color.CYAN:
                    colorConsulta = "Cyan";
                    color = Color.CYAN;
                    colorPos = 5;
                    break;
                case Color.BLACK:
                    colorConsulta = "Negro";
                    color = Color.BLACK;
                    colorPos = 6;
                    break;
                case Color.WHITE:
                    colorConsulta = "Blanco";
                    color = Color.WHITE;
                    colorPos = 7;
                    break;
            }

            coloresArray[0] = colorConsulta;

            tipoConsulta = azure.obtenerTipoOperacion(rs.getString("TIPO"));
            observacionesConsulta = rs.getString("OBSERVACIONES");

            ArrayList<String> parc = azure.obtenerCODParcelas(rs.getString("PARCELA"));
            if(parc.size() == 0) parc.add(azure.obtenerParcela(rs.getString("PARCELA")));
            parcelasConsulta.addAll(parc);

            ArrayList<String> art = azure.obtenerCODArticulos(rs.getString("ARTICULOS"));
            if(art.size() == 0) art.add(azure.obtenerArticulo(rs.getString("ARTICULOS")));
            articulosConsulta.addAll(art);

            fincaConsulta = azure.obtenerFinca(rs.getString("FINCA"));

            parcelasView.setAdapter(parcelasAdapter);
            articulosView.setAdapter(articulosAdapter);
        }
        azure.desconectar();
    }
}