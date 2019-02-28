
package com.example.ruben.Paginas;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.icu.text.AlphabeticIndex;
import android.location.Location;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Entidades.Articulo;
import Entidades.Operacion;
import Entidades.Parcela;
import Entidades.Usuario;
import Extra.AzureDB;
import Extra.CheckboxAdapterArticulos;
import Extra.CheckboxAdapterParcela;
import Extra.ListAdapterArticulos;
import Extra.ListAdapterOperaciones;

import static android.content.ContentValues.TAG;

@SuppressLint("ValidFragment")
public class MainFragmentOperador extends Fragment
        implements View.OnTouchListener, OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /* Variables para Mapa */

    private GoogleMap mMap;
    private GoogleApiClient cliente;
    private Marker marcador;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;

    /* Variables para Polilineas */

    private Polyline polilinea;

    /* Variables para Dibujos */

    private ArrayList<LatLng> arrayPuntosLineas = new ArrayList<>();
    private PolylineOptions opcionesPolilineaDibujo;
    private GestureDetector detectorGesto = null;
    private boolean dibujar = false, parcela = false;
    private View mapaView;
    private Polyline polilineaDibujo;
    private ArrayList<Polyline> polilineasTrazo = new ArrayList<>();
    private EditText codigoOperacion, nombreOperacion;
    private TextView observaciones;
    private PolylineOptions colorDibujo = new PolylineOptions();
    private ArrayList<Integer> arrayColores = new ArrayList<>();

    /* Variables DIU */

    View rootView = null;

    private ArrayAdapter<String> listFincas;
    //private ArrayAdapter<String> listVariedades;
    private ArrayAdapter<String> listOperaciones;
    private ArrayList<String> listArticulos;
    private ArrayList<Operacion> listConsultaOperaciones;
    private Spinner spinnerFincas, spinnerOperaciones, spinnerArticulos;

    /* * * * * * * * * * * * * * * * * * * * * * * */

    private ListView checkedParcelas, checkedArticulos, articulos, operaciones;
    private ArrayList<Parcela> listCParcelas;
    private ArrayList<Articulo> listCArticulos;
    private CheckboxAdapterArticulos articulosCAdapter;
    private CheckboxAdapterParcela parcelaAdapter;
    private ListAdapterArticulos articulosAdapter;
    private ListAdapterOperaciones operacionesAdapter;

    /* * * * * * * * * * * * * * * * * * * * * * * */

    private String[] nombreFincas2;
    private String[] nombreOperaciones;
    private String[] nombreArticulos;
    private ArrayList<String> nomFincas = new ArrayList<>();
    public ArrayList<String> nomVariedades = new ArrayList<>();
    public ArrayList<String> nomOperaciones = new ArrayList<>();
    public ArrayList<String> nomArticulos = new ArrayList<>();
    private int spinnerPosFincas = 0, spinnerPosParcelas = 0, spinnerPosOperaciones = 0, spinnerPosArticulos = 0;
    private ArrayList<Integer> posiciones = new ArrayList<>();
    private ArrayList<Boolean> parcelasComprobadas = new ArrayList<>();

    private Button addArticulo;

    FloatingActionButton fabCrearOperacion, fabConsultarOperacion, fabCerrarSesion,
            aceptarParcela, cancelarParcela, aceptarOperacion, cancelarOperacion,
            aceptarCrearOperacion, cancelarCrearOperacion;
    private TextView fechaOperacion;
    private DatePickerDialog.OnDateSetListener fecha;

    //private String[] coloresArray = {"Azul", "Verde", "Rojo", "Amarillo", "Magenta", "Cyan", "Negro", "Blanco"};
    private String[] coloresArray = {""};
    private AzureDB azure = new AzureDB();
    private Usuario usuario;
    private ProgressBar pgsBar;


    @SuppressLint("ValidFragment")
    public MainFragmentOperador(GoogleMap mMap, GoogleApiClient cliente, Usuario usuario) {
        this.mMap = mMap;
        this.cliente = cliente;
        this.usuario = usuario;
    }

    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(retain);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_operador_2, container, false);

        /* Inicialización de Spinners*/

        /* Spinner de color de dibujos */
        Spinner SpinnerColorCrearOperacion = rootView.findViewById(R.id.SpinnerColorCrearOperacion);
        ArrayAdapter<String> listColores = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, coloresArray);
        listColores.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerColorCrearOperacion.setAdapter(listColores);
        int colorPos = 0;
        SpinnerColorCrearOperacion.setSelection(colorPos);

        /*SpinnerColorCrearOperacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        colorDibujo.color(Color.BLUE);
                        break;
                    case 1:
                        colorDibujo.color(Color.GREEN);
                        break;
                    case 2:
                        colorDibujo.color(Color.RED);
                        break;
                    case 3:
                        colorDibujo.color(Color.YELLOW);
                        break;
                    case 4:
                        colorDibujo.color(Color.MAGENTA);
                        break;
                    case 5:
                        colorDibujo.color(Color.CYAN);
                        break;
                    case 6:
                        colorDibujo.color(Color.BLACK);
                        break;
                    case 7:
                        colorDibujo.color(Color.WHITE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/

        /* Spinner de nombre de fincas disponibles para la vista de operaciones*/
        spinnerFincas = rootView.findViewById(R.id.SpinnerFincaCrearOperacion);
        nombreFincas2 = nomFincas.toArray(new String[0]);
        listFincas = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, nombreFincas2);
        listFincas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFincas.setAdapter(listFincas);
        spinnerFincas.setSelection(spinnerPosFincas);

        spinnerFincas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (spinnerPosFincas != position) {
                    try {
                        spinnerPosFincas = spinnerFincas.getSelectedItemPosition();
                        actualizarDatosParcelas();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else spinnerPosFincas = spinnerFincas.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        /*spinnerFincas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    actualizarDatosParcelas();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        spinnerFincas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        /* CheckList de nombres de parcelas con sus checks disponibles para la vista de creación de operaciones*/
        checkedParcelas = rootView.findViewById(R.id.ListViewParcelasCrearOperacion);
        checkedParcelas.setAdapter(parcelaAdapter);
        ViewGroup headerParcelas = (ViewGroup) inflater.inflate(R.layout.parcelasheader, checkedParcelas, false);
        checkedParcelas.addHeaderView(headerParcelas);
        listCParcelas = new ArrayList<>();
        parcelaAdapter = new CheckboxAdapterParcela(getActivity(), R.layout.custom_checklist_view, listCParcelas);

        /* CheckList de nombres de articulos con sus checks disponibles para la vista de creación de operaciones*/
        checkedArticulos = rootView.findViewById(R.id.ListViewArticulosDBCrearOperacion);
        checkedArticulos.setAdapter(articulosCAdapter);
        ViewGroup headerArticulos = (ViewGroup) inflater.inflate(R.layout.articulosheader, articulos, false);
        checkedArticulos.addHeaderView(headerArticulos);
        listCArticulos = new ArrayList<>();
        articulosCAdapter = new CheckboxAdapterArticulos(getActivity(), R.layout.custom_checklist_view, listCArticulos);

        /* ListView de nombres de artículos añadidos para la vista de creación de operaciones*/
        articulos = rootView.findViewById(R.id.ListViewArticulosCrearOperacion);
        articulos.setAdapter(articulosAdapter);
        ViewGroup headerArticulos2 = (ViewGroup) inflater.inflate(R.layout.articulosheader, articulos, false);
        articulos.addHeaderView(headerArticulos2);
        listArticulos = new ArrayList<>();
        articulosAdapter = new ListAdapterArticulos(getActivity(), R.layout.custom_listarticulos_view, listArticulos, listCArticulos);



        /* ListView de nombres de operaciones añadidos para la vista de consulta de operaciones*/
        operaciones = rootView.findViewById(R.id.ListViewOperaciones);
        operaciones.setAdapter(operacionesAdapter);
        listConsultaOperaciones = new ArrayList<>();
        operacionesAdapter = new ListAdapterOperaciones(getActivity(), R.layout.custom_consulta_view, listConsultaOperaciones, usuario.getCod());

        /* Spinner de nombres de operaciones disponibles para la vista de operaciones */
        spinnerOperaciones = rootView.findViewById(R.id.SpinnerTipoCrearOperacion);
        nombreOperaciones = nomOperaciones.toArray(new String[0]);
        listOperaciones = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, nombreOperaciones);
        listOperaciones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOperaciones.setAdapter(listOperaciones);
        spinnerOperaciones.setSelection(spinnerPosOperaciones);

        spinnerOperaciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (spinnerPosOperaciones != position) {
                    try {
                        spinnerPosOperaciones = spinnerOperaciones.getSelectedItemPosition();
                        actualizarDatosTipoOperacion();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else spinnerPosOperaciones = spinnerOperaciones.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        /*spinnerOperaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    actualizarDatosTipoOperacion();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });*/

        return rootView;
    }

    @SuppressLint("SetTextI18n")
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (validarGoogleServices()) {
            if (!isStateSaved()) iniciarMapa();
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            /* Vista de búsqueda y configuración de terreno */

            Button botonBuscar = getActivity().findViewById(R.id.botonBuscarO);
            final ConstraintLayout terrenos = getActivity().findViewById(R.id.tipoTerrenoO);

            final boolean[] activadoVistaTerreno = {false};
            Button botonTerreno = getActivity().findViewById(R.id.botonTerrenoO);
            botonTerreno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!activadoVistaTerreno[0]) {
                        terrenos.setVisibility(View.VISIBLE);
                        activadoVistaTerreno[0] = true;
                    } else {
                        terrenos.setVisibility(View.INVISIBLE);
                        activadoVistaTerreno[0] = false;
                    }
                }
            });

            Button botonTerrenoNormal = getActivity().findViewById(R.id.normalO);
            Button botonTerrenoTerreno = getActivity().findViewById(R.id.terrenoO);
            Button botonTerrenoSatelite = getActivity().findViewById(R.id.sateliteO);
            Button botonTerrenoHibrido = getActivity().findViewById(R.id.hibridoO);

            botonTerrenoNormal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            });

            botonTerrenoTerreno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                }
            });

            botonTerrenoSatelite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
            });

            botonTerrenoHibrido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }
            });

            /* Listener para búsqueda de una localización */
            botonBuscar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    geoLocalizar(v);
                }
            });

            pgsBar = getActivity().findViewById(R.id.pBar);

            Calendar cal = Calendar.getInstance();
            fechaOperacion = getActivity().findViewById(R.id.DatePickerFechaOperacion);
            SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
            String atoday = s.format(new Date(cal.getTimeInMillis()));
            fechaOperacion.setText(atoday);
            fechaOperacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar cal = Calendar.getInstance();
                    int a = cal.get(Calendar.YEAR);
                    int m = cal.get(Calendar.MONTH);
                    int d = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            month = month + 1;
                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            cal.set(Calendar.MONTH, month - 1);
                            cal.set(Calendar.YEAR, year);
                            SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
                            String atoday = s.format(new Date(cal.getTimeInMillis()));
                            fechaOperacion.setText(atoday);
                        }
                    }, a, m, d);
                    dialog.show();
                }
            });

            observaciones = getActivity().findViewById(R.id.inputObservacionesCrearOperacion);
            observaciones.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.comentarios_dialog);
                    Button aceptarObservacion = dialog.findViewById(R.id.BotonAceptarObservacion);
                    Button cancelarObservacion = dialog.findViewById(R.id.BotonCancelarObservacion);

                    final EditText cajaComentarios = dialog.findViewById(R.id.cajaComentarios);

                    aceptarObservacion.setEnabled(true);
                    aceptarObservacion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (cajaComentarios.getText().toString().equals(""))
                                cajaComentarios.setText("Observaciones");
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

            /* Carga de la zona de dibujo */
            mapaView = getActivity().findViewById(R.id.drawer_viewO);

            /* Menu flotante */

            final ConstraintLayout UICrearOperacion = getActivity().findViewById(R.id.UICrearOperacion);
            fabCrearOperacion = getActivity().findViewById(R.id.FloatButtonCrearOperacion);
            fabCrearOperacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        azure.conectar();
                        if (azure.hayFincas()) {
                            if (azure.hayParcelas()) {
                                UICrearOperacion.setVisibility(View.VISIBLE);
                                codigoOperacion = getActivity().findViewById(R.id.inputCodigoCrearOperacion);
                                nombreOperacion = getActivity().findViewById(R.id.inputNombreCrearOperacion);
                                fabCrearOperacion.setVisibility(View.INVISIBLE);
                                fabConsultarOperacion.setVisibility(View.INVISIBLE);
                                fabCerrarSesion.setVisibility(View.INVISIBLE);
                                Toast toastd = Toast.makeText(getActivity(), "Debe posicionarse previamente en la zona a realizar la operación", Toast.LENGTH_LONG);
                                View viewd = toastd.getView();
                                viewd.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                                TextView text = viewd.findViewById(android.R.id.message);
                                text.setTextColor(Color.WHITE);
                                toastd.show();
                                actualizarDatosParcelas();
                                actualizarDatosTipoOperacion();
                                dibujar = false;
                                mapaView.setClickable(dibujar);
                                //obtenerTodo();
                            } else {
                                Toast.makeText(getActivity(), "No hay parcelas disponibles", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "No hay fincas disponibles", Toast.LENGTH_SHORT).show();
                        }
                        azure.desconectar();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            Button bvdib = UICrearOperacion.findViewById(R.id.BotonAceptarCrearOperacion);
            bvdib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!codigoOperacion.getText().toString().isEmpty() && !nombreOperacion.getText().toString().isEmpty() && !nomOperaciones.isEmpty() && !listCParcelas.isEmpty()) {
                        if((!listArticulos.isEmpty() && addArticulo.isEnabled()) || (listArticulos.isEmpty() && !addArticulo.isEnabled())) {
                            try {
                                azure.conectar();
                                if (!azure.existeOperacion(codigoOperacion.getText().toString()) && !azure.existeOperacionNombre(nombreOperacion.getText().toString())) {
                                    int contador = 0;
                                    int i = 0;
                                    while (i < listCParcelas.size()) {
                                        if (listCParcelas.get(i).esTildado()) {
                                            contador++;
                                        }
                                        i++;
                                    }
                                    if (contador > 0) {
                                        posiciones = parcelaAdapter.getParcelaPos();
                                        parcelasComprobadas.clear();
                                        for (int j = 0; j < posiciones.size(); j++) {
                                            parcelasComprobadas.add(false);
                                        }
                                        UICrearOperacion.setVisibility(View.INVISIBLE);
                                        Toast t = Toast.makeText(getActivity(), "¡Datos guardados!", Toast.LENGTH_SHORT);
                                        View viewt = t.getView();
                                        viewt.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                                        TextView text = viewt.findViewById(android.R.id.message);
                                        text.setTextColor(Color.WHITE);
                                        t.show();
                                        dibujar = true;
                                        mapaView.setClickable(dibujar);
                                    } else {
                                        Toast.makeText(getActivity(), "No hay parcelas seleccionadas.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "El código/nombre ya existe", Toast.LENGTH_SHORT).show();
                                }
                                azure.desconectar();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "Hay artículos sin añadir.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Hay campos sin rellenar.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            Button bvdibCancelar = UICrearOperacion.findViewById(R.id.BotonCancelarCrearOperacion);
            bvdibCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dibujar = false;
                    mapaView.setClickable(dibujar);
                    UICrearOperacion.setVisibility(View.INVISIBLE);
                    fabCrearOperacion.setVisibility(View.VISIBLE);
                    fabConsultarOperacion.setVisibility(View.VISIBLE);
                    fabCerrarSesion.setVisibility(View.VISIBLE);
                }
            });

            aceptarOperacion = getActivity().findViewById(R.id.FloatBotonAceptarOperacion);
            aceptarOperacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dibujar = false;
                    mapaView.setClickable(dibujar);
                    esconderTecladoAndroid(v);
                    try {
                        agregarOperacion();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    codigoOperacion.setText("");
                    nombreOperacion.setText("");
                    observaciones.setText("");
                    Calendar cal = Calendar.getInstance();
                    fechaOperacion = getActivity().findViewById(R.id.DatePickerFechaOperacion);
                    fechaOperacion.setText(cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR));

                    try {
                        actualizarSpinners();
                        actualizarDatosParcelas();
                        actualizarDatosTipoOperacion();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    aceptarOperacion.setVisibility(View.INVISIBLE);
                    cancelarOperacion.setVisibility(View.INVISIBLE);
                    fabCrearOperacion.setVisibility(View.VISIBLE);
                    fabConsultarOperacion.setVisibility(View.VISIBLE);
                    fabCerrarSesion.setVisibility(View.VISIBLE);
                }
            });

            cancelarOperacion = getActivity().findViewById(R.id.FloatBotonCancelarOperacion);
            cancelarOperacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    esconderTecladoAndroid(v);
                    dibujar = false;
                    eliminarDibujos();
                    aceptarOperacion.setVisibility(View.INVISIBLE);
                    cancelarOperacion.setVisibility(View.INVISIBLE);
                    fabCrearOperacion.setVisibility(View.VISIBLE);
                    fabConsultarOperacion.setVisibility(View.VISIBLE);
                    fabCerrarSesion.setVisibility(View.VISIBLE);
                    mapaView.setClickable(dibujar);
                }
            });


            final ConstraintLayout UIConsultarOperacion = getActivity().findViewById(R.id.UIConsultarOperacion);
            fabConsultarOperacion = getActivity().findViewById(R.id.FloatButtonConsultarOperacion);
            fabConsultarOperacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        azure.conectar();
                        if (azure.hayFincas()) {
                            if (azure.hayParcelas()) {
                                UIConsultarOperacion.setVisibility(View.VISIBLE);
                                fabCrearOperacion.setVisibility(View.INVISIBLE);
                                fabConsultarOperacion.setVisibility(View.INVISIBLE);
                                fabCerrarSesion.setVisibility(View.INVISIBLE);
                                dibujar = false;
                                mapaView.setClickable(dibujar);
                                obtenerOperaciones();
                                //obtenerTodo();
                            } else {
                                Toast.makeText(getActivity(), "No hay parcelas disponibles", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "No hay fincas disponibles", Toast.LENGTH_SHORT).show();
                        }
                        azure.desconectar();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            fabCerrarSesion = getActivity().findViewById(R.id.FloatButtonCerrarSesion);
            fabCerrarSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);
                }
            });

            final ConstraintLayout UIAddArticulo = getActivity().findViewById(R.id.UIAddArticulo);
            addArticulo = getActivity().findViewById(R.id.BotonAddArticuloCrearOperacion);
            addArticulo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        azure.conectar();
                        if (azure.hayArticulos()) {
                            UIAddArticulo.setVisibility(View.VISIBLE);
                            fabCrearOperacion.setVisibility(View.INVISIBLE);
                            fabConsultarOperacion.setVisibility(View.INVISIBLE);
                            fabCerrarSesion.setVisibility(View.INVISIBLE);
                            actualizarCheckedArticulos();
                            //obtenerTodo();
                        } else {
                            Toast.makeText(getActivity(), "No hay artículos disponibles", Toast.LENGTH_SHORT).show();
                        }
                        azure.desconectar();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            final EditText[] filtrarArticulo = {getActivity().findViewById(R.id.buscarArticulo)};
            filtrarArticulo[0].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        obtenerArticulos2(s.toString());
                        checkedArticulos.setAdapter(articulosCAdapter);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            final Button botonAceptarArticulos = getActivity().findViewById(R.id.BotonAceptarArticulos);
            botonAceptarArticulos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //listArticulos.clear();
                    filtrarArticulo[0] = getActivity().findViewById(R.id.buscarArticulo);
                    if (articulosCAdapter.getNumArticulos() > 0) {
                        for (Articulo a : listCArticulos) {
                            if (a.esTildado() && !listArticulos.contains(a.getNomArticulo())) {
                                listArticulos.add(a.getNomArticulo());
                            }
                            else if (!a.esTildado() && listArticulos.contains(a.getNomArticulo())) {
                                listArticulos.remove(a.getNomArticulo());
                            }
                        }

                        articulosAdapter = new ListAdapterArticulos(getActivity(), R.layout.custom_listarticulos_view, listArticulos, listCArticulos);
                        articulos.setAdapter(articulosAdapter);

                        //articulosCAdapter.clear();
                        UIAddArticulo.setVisibility(View.INVISIBLE);
                    } else {
                        Toast.makeText(getActivity(), "No hay artículos disponibles", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            final Button botonCancelarArticulos = getActivity().findViewById(R.id.BotonCancelarArticulos);
            botonCancelarArticulos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UIAddArticulo.setVisibility(View.INVISIBLE);
                }
            });

            final Button botonVolverConsultar = getActivity().findViewById(R.id.BotonVolverConsultar);
            botonVolverConsultar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIConsultarOperacion.setVisibility(View.INVISIBLE);
                    fabCrearOperacion.setVisibility(View.VISIBLE);
                    fabConsultarOperacion.setVisibility(View.VISIBLE);
                    fabCerrarSesion.setVisibility(View.VISIBLE);
                    try {
                        obtenerTodo();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /* Método que inicializa el mapa */
    private void iniciarMapa() {
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapfO);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        cliente = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .enableAutoManage(getActivity(), this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        cliente.connect();
    }

    /* Método que verifica si los servicios de Google son compatibles con el dispositivo */
    public boolean validarGoogleServices() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int verificado = api.isGooglePlayServicesAvailable(getActivity());
        if (verificado == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(verificado)) {
            Dialog ventana = api.getErrorDialog(getActivity(), verificado, 0);
            ventana.show();
        } else {
            Toast.makeText(getActivity(), "No se ha podido conectar a los Servicios de Google", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
    }

    /* Carga del evento de gestos para la creación del dibujo */
    public boolean onTouch(View v, MotionEvent event) {
        return detectorGesto.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    /* Carga de métodos con tratamientos sobre el mapa */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.3433600, -6.9460370)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        if (mMap != null) {

            try {
                obtenerTodo();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            /* Listener para la creación del dibujo en la zona de dibujos (View) */
            mapaView.setOnTouchListener(new View.OnTouchListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    boolean b = false;
                    if (dibujar) {

                        float x = event.getRawX();
                        float y = event.getRawY();

                        int x_co = Math.round(x);
                        int y_co = Math.round(y);

                        Point x_y_points = new Point(x_co, y_co);
                        LatLng latLng = mMap.getProjection().fromScreenLocation(x_y_points);

                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            opcionesPolilineaDibujo = colorDibujo;
                            arrayColores.add(opcionesPolilineaDibujo.getColor());
                            polilineaDibujo = mMap.addPolyline(opcionesPolilineaDibujo);
                            //System.out.println("Coordenada: " + latLng.latitude + ", " + latLng.longitude);
                        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                            arrayPuntosLineas.add(latLng);
                            polilineaDibujo.setPoints(arrayPuntosLineas);
                            //System.out.println("Coordenada: " + latLng.latitude + ", " + latLng.longitude);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {

                            Toast a = Toast.makeText(getActivity(), "Trazado procesado", Toast.LENGTH_LONG);
                            a.setGravity(Gravity.TOP|Gravity.CENTER, 0, 45);
                            View view = a.getView();
                            view.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                            TextView text = view.findViewById(android.R.id.message);
                            text.setTextColor(Color.WHITE);

                            Toast c = Toast.makeText(getActivity(), "¡El dibujo no pasa por las parcelas definidas!", Toast.LENGTH_SHORT);
                            c.setGravity(Gravity.TOP|Gravity.CENTER, 0, 45);
                            View viewc = c.getView();
                            viewc.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                            TextView textc = viewc.findViewById(android.R.id.message);
                            textc.setTextColor(Color.WHITE);


                            if (posiciones.size() > 1) {
                                try {
                                    if (!dibujoDentroZonas(arrayPuntosLineas, spinnerPosFincas, posiciones)) {
                                        int i = 0;
                                        while (i < posiciones.size() && !b) {
                                            if (dibujoDentroZona(arrayPuntosLineas, spinnerPosFincas, posiciones.get(i))) {
                                                polilineasTrazo.add(polilineaDibujo);
                                                //aceptarOperacion.setVisibility(View.VISIBLE);
                                                b = true;
                                            } else i++;

                                        }if(!b) {
                                            c.show();
                                            polilineaDibujo.remove();
                                        }
                                    } else {
                                        polilineasTrazo.add(polilineaDibujo);
                                        b = true;
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                arrayPuntosLineas.clear();
                            } else {
                                spinnerPosParcelas = posiciones.get(0);
                                try {
                                    if (!dibujoDentroZona(arrayPuntosLineas, spinnerPosFincas, spinnerPosParcelas)) {
                                        c.show();
                                        polilineaDibujo.remove();

                                    } else {
                                        polilineasTrazo.add(polilineaDibujo);
                                        //aceptarOperacion.setVisibility(View.VISIBLE);
                                        b = true;
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                arrayPuntosLineas.clear();
                            }

                            if(b) a.show();
                            cancelarOperacion.setVisibility(View.VISIBLE);
                            //alert.dismiss();
                            //pgsBar.setVisibility(v.GONE);

                            try {
                                if(contieneTodasParcelas()){
                                    aceptarOperacion.setVisibility(View.VISIBLE);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return b;
                }
            });
        }

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }


    public boolean contieneTodasParcelas() throws SQLException {
        boolean encontrado = true;

        int i = 0;
        while (i < parcelasComprobadas.size() && encontrado) {
            if(!parcelasComprobadas.get(i)) {
                encontrado = false;
            }
            else i++;
        }
        return encontrado;
    }

    /* Método que trata con los dibujos realizados dentro/fuera del recinto */
    public boolean dibujoDentroZona(ArrayList<LatLng> dibujo, int posF, int posP) throws SQLException {
        boolean b = true;
        List<LatLng> coord = obtenerParcela(posF, posP);

        int k = 0;
        while (k < dibujo.size() && b) {
            double latD = dibujo.get(k).latitude;
            double logD = dibujo.get(k).longitude;
            LatLng l = new LatLng(latD, logD);

            b = PolyUtil.containsLocation(l, coord, true); // if (PolyUtil.containsLocation(l, coord, true)) b = true;  else b = false;
            k++;
        }

        while (posP >= parcelasComprobadas.size()) {
            posP -= 1;
        }
        if(b) parcelasComprobadas.set(posP, true);

        return b;
    }

    /* Método que subdivide la operación en trozos para localizarla en las parcelas a tratar */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean dibujoEntreZonas(ArrayList<LatLng> dibujo, ArrayList<Integer> posP, List<List<LatLng>> arrayCoord) throws SQLException {


        List<List<LatLng>> partes = new ArrayList<>();
        boolean b = true;
        int tam = dibujo.size();
        int divisor = posP.size();

        if (tam % divisor != 0) {
            tam = tam - 1;
        }

        int resultado = tam / divisor;

        int i = 0, c = 0;
        while (i < posP.size() && b) {
            if (resultado > dibujo.size() - 1) {
                while (resultado >= dibujo.size() - 1) {
                    resultado--;
                }
            }
            try {
                List<LatLng> aux = dibujo.subList(c, resultado);
                partes.add(i, aux);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                b = false;
            }

            c = resultado;
            resultado += resultado;
            i++;
        }

        if (b) {
            int j = 0, k = 0, contador = 0;
            if (partes.size() > 1) {
                while (j < partes.size()) {
                    while (k < 1) {
                        LatLng aux = new LatLng(partes.get(j).get(k).latitude, partes.get(j).get(k).longitude);
                        if (PolyUtil.containsLocation(aux, arrayCoord.get(j), true)) {
                            contador++;
                            parcelasComprobadas.set(j, true);
                        }
                        k++;
                    }
                    k = 0;
                    j++;
                }

                if (contador == partes.size()) b = true;
                else {
                    partes.clear();

                    int n = 0;
                    int m = 0;
                    while (m < posP.size()) {
                        boolean partir = false;
                        ArrayList<LatLng> ll = new ArrayList<>();
                        while (n < dibujo.size() && !partir) {
                            LatLng aux = new LatLng(dibujo.get(n).latitude, dibujo.get(n).longitude);
                            if (PolyUtil.containsLocation(aux, arrayCoord.get(m), true)) {
                                ll.add(aux);
                            } else partir = true;
                            n++;
                        }
                        if (!ll.isEmpty()) {
                            partes.add((List<LatLng>) ll.clone());
                        }
                        m++;
                        ll.clear();
                    }

                    contador = 0;

                    if (partes.size() > 1 && partes.size() <= arrayCoord.size()) {
                        i = 0;
                        while(i < arrayCoord.size()) {
                            j = 0;
                            k = 0;
                            while (j < partes.size()) {
                                while (k < 1) {
                                    LatLng aux = new LatLng(partes.get(j).get(k).latitude, partes.get(j).get(k).longitude);
                                    if (PolyUtil.containsLocation(aux, arrayCoord.get(i), true)) {
                                        contador++;
                                        parcelasComprobadas.set(i, true);
                                    }
                                    k++;
                                }
                                k = 0;
                                j++;
                            }
                            i++;
                        }
                    }

                    // Dibujar al contrario
                    else {
                        partes.clear();

                        int n2 = 0;
                        int m2 = posP.size()-1;
                        while (m2 >= 0) {
                            boolean partir = false;
                            ArrayList<LatLng> ll = new ArrayList<>();
                            while (n2 < dibujo.size() && !partir) {
                                LatLng aux = new LatLng(dibujo.get(n2).latitude, dibujo.get(n2).longitude);
                                if (PolyUtil.containsLocation(aux, arrayCoord.get(m2), true)) {
                                    ll.add(aux);
                                } else partir = true;
                                n2++;
                            }
                            if (!ll.isEmpty()) {
                                partes.add((List<LatLng>) ll.clone());
                            }
                            m2--;
                            ll.clear();
                        }

                        contador = 0;

                        if (partes.size()  > 1 && partes.size() <= arrayCoord.size()) {
                            i = arrayCoord.size()-1;
                            while(i >= 0) {
                                j = partes.size()-1;
                                k = 1;
                                while (j >= 0) {
                                    while (k > 0) {
                                        LatLng aux = new LatLng(partes.get(j).get(k).latitude, partes.get(j).get(k).longitude);
                                        if (PolyUtil.containsLocation(aux, arrayCoord.get(i), true)) {
                                            contador++;
                                            parcelasComprobadas.set(i, true);
                                        }
                                        k--;
                                    }
                                    k = 1;
                                    j--;
                                }
                                i--;
                            }
                        }

                        /*

                        if(contador < 2) {
                            j = 0;
                            k = partes.size();
                            int sum = k;
                            while (j < partes.size()) {
                                while (k > 0) {
                                    LatLng aux = new LatLng(partes.get(j).get(k-1).latitude, partes.get(j).get(k-1).longitude);
                                    if (PolyUtil.containsLocation(aux, arrayCoord.get(sum), true)) {
                                        contador++;
                                        parcelasComprobadas.set(k, true);
                                    }
                                    k--;
                                }
                                k = partes.size();
                                j++;
                                sum -= 1;
                            }
                        }
                         */
                    }

                    if (contador > 1 && contador <= arrayCoord.size()) b = true;
                    else b = false;
                }
            }
        }

        return b;
    }

    /* Método que trata con los dibujos realizados dentro/fuera de varios recintos */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean dibujoDentroZonas(ArrayList<LatLng> dibujo, int posF, ArrayList<Integer> posP) throws SQLException {
        boolean b = true;
        List<List<LatLng>> arrayCoord = obtenerParcelas(posF, posP);

        if (!dibujoEntreZonas(dibujo, posP, arrayCoord)) {
            b = false;
        }

        return b;
    }

    /* Método que calcula el centro de un polígono para obtener su localización */
    /*private LatLng geoLocalizarFinca(ArrayList<LatLng> polygonPointsList){
        LatLng centerLatLng = null;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i = 0 ; i < polygonPointsList.size() ; i++)
        {
            builder.include(polygonPointsList.get(i));
        }
        LatLngBounds bounds = builder.build();
        centerLatLng =  bounds.getCenter();

        return centerLatLng;
    }*/





    /*private LatLng geoLocalizarParcela(Polyline polylinePointsList){
        LatLng centerLatLng = null;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i = 0 ; i < polylinePointsList.getPoints().size() ; i++)
        {
            builder.include(polylinePointsList.getPoints().get(i));
        }
        LatLngBounds bounds = builder.build();
        centerLatLng =  bounds.getCenter();

        return centerLatLng;
    }*/


    /* Método que busca una localización en base a su latitud y longitud */
    public void buscarLocalizacion(double latitud, double longitud, float zoom) {
        LatLng ll = new LatLng(latitud, longitud);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.moveCamera(update);
    }

    /* Método que geolocaliza la posición a buscar */
    public void geoLocalizar(View view) {
        /* Esconder teclado de Android */
        esconderTecladoAndroid(view);

        /* Recogemos la dirección a buscar */
        EditText textoLocalizacion = (EditText) getActivity().findViewById(R.id.textoLocalizacionO);
        String localizacion = textoLocalizacion.getText().toString();

        /* Si no está vacío el campo de búsqueda, obtenemos la dirección */
        if (!localizacion.equals("")) {
            Geocoder geocoder = new Geocoder(getActivity());
            List<Address> listaDirecciones = new ArrayList<>();

            try {
                listaDirecciones = geocoder.getFromLocationName(localizacion, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (listaDirecciones.size() != 0) {
                Address direccion = listaDirecciones.get(0);
                String localidad = direccion.getLocality();

                double latitud = direccion.getLatitude();
                double longitud = direccion.getLongitude();
                buscarLocalizacion(latitud, longitud, 15);
                setMarcador(localidad, latitud, longitud);
            } else {
                Toast.makeText(getActivity(), "No se ha encontrado la búsqueda", Toast.LENGTH_SHORT).show();
            }

        }
    }

    /* Método que coloca el marcador en el mapa en una localización concreta */
    public void setMarcador(String localizacion, double latitud, double longitud) {
        if (marcador != null) {
            marcador.remove();
        }

        MarkerOptions marcadorOp = new MarkerOptions()
                .title(localizacion)
                .draggable(true)
                .position(new LatLng(latitud, longitud));
        marcador = mMap.addMarker(marcadorOp);
    }

    /* Método que elimina todos los dibujos pintados */
    private void eliminarDibujos() {
        arrayPuntosLineas.clear();
        if (polilineasTrazo.size() > 1) {
            for (int i = 0; i < polilineasTrazo.size(); i++) {
                for (int j = 0; j < polilineasTrazo.get(i).getPoints().size(); j++) {
                    polilineasTrazo.get(i).getPoints().remove(j);
                }
                polilineasTrazo.get(i).remove();
            }
            polilineasTrazo.clear();
            polilineaDibujo.remove();
            arrayColores.remove(arrayColores.size() - 1);
        } else {
            polilineaDibujo.remove();
            polilineasTrazo.clear();
        }
    }

    /* Método que esconde el teclado de Android */
    public void esconderTecladoAndroid(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest peticionLocalizacion = LocationRequest.create();
        peticionLocalizacion.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        peticionLocalizacion.setInterval(1000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(cliente, peticionLocalizacion, (com.google.android.gms.location.LocationListener) this);
    }

    public void onConnectionSuspended(int i) {
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    /* Métodos de la base de datos AzureDB */


    public boolean agregarOperacion() throws SQLException {
        boolean valido = false;
        azure.conectar();
        String codTipoOp = azure.obtenerCODTipoOperacion(listOperaciones.getItem(spinnerPosOperaciones));
        boolean rs;

        String codconja = "";
        if(listArticulos.size() > 1) {
            for (int j = 0; j < listArticulos.size(); j++) {
                codconja += listArticulos.get(j) + " ";
            }

        } else if (listArticulos.size() == 1) {
            codconja = azure.obtenerCODArticulo(listArticulos.get(0));
        } else codconja = " - ";


        int contador = 0;

        String codconjp = "";
        ArrayList<String> parcelasTildadas = new ArrayList<>();
        for (int j = 0; j < listCParcelas.size(); j++) {
            if (listCParcelas.get(j).esTildado()) {
                contador++;
                parcelasTildadas.add(listCParcelas.get(j).getNomParcela());
            }
        }

        if(contador > 1) {
            for (int j = 0; j < parcelasTildadas.size(); j++) {
                codconjp += parcelasTildadas.get(j);
                if(j+1 < parcelasTildadas.size()) codconjp += " ";
            }
        } else codconjp = azure.obtenerCODParcela(listCParcelas.get(0).getNomParcela());

        String codf = azure.obtenerCODFinca(listFincas.getItem(spinnerPosFincas));

        if (observaciones.getText().toString().equals("Observaciones")) observaciones.setText(" - ");

        String query = "INSERT INTO OPERACION VALUES ('" + codigoOperacion.getText().toString() + "', '" + nombreOperacion.getText().toString() + "', '" + colorDibujo.getColor() + "', '" + codTipoOp + "', '" + fechaOperacion.getText().toString() + "', '" + fechaOperacion.getText().toString() + "', '" + codconja + "', '" + observaciones.getText().toString() + "', '" + polilineasTrazo.size() + "', '" + codconjp + "', '" + codf + "', '" + usuario.getCod() + "');";
        Statement stmt = azure.getConexion().createStatement();
        rs = stmt.execute(query);
        stmt.close();
        if (!rs) {
            if(contador > 1) {
                String queryPar;
                Statement stmtPar = azure.getConexion().createStatement();
                for (int i = 0; i < parcelasTildadas.size(); i++) {
                    String p = azure.obtenerCODParcela(parcelasTildadas.get(i));
                    queryPar = "INSERT INTO CONJUNTO_PARCELAS (OPERACION, PARCELA) VALUES ('" + codigoOperacion.getText().toString() + "', '" + p + "')";
                    stmtPar.execute(queryPar);
                }
                stmtPar.close();
            }

            if(listArticulos.size() > 1) {
                String queryArt = "";
                Statement stmtArt = azure.getConexion().createStatement();
                for (int i = 0; i < listArticulos.size(); i++) {
                    queryArt += "INSERT INTO CONJUNTO_ARTICULOS (OPERACION, ARTICULO) VALUES ('" + codigoOperacion.getText().toString() + "', '" + azure.obtenerCODArticulo(listArticulos.get(i)) + "');";
                }
                stmtArt.execute(queryArt);
                stmtArt.close();
                listArticulos.clear();
            }

            boolean coord = agregarCoordenadas_Operacion();
            String mm;
            if (coord) {
                mm = "Operación realizada correctamente";
                valido = true;
            } else {
                mm = "Error en las coordenadas";
                valido = false;
            }
            Toast toastd = Toast.makeText(getActivity(), mm, Toast.LENGTH_LONG);
            toastd.setGravity(Gravity.TOP|Gravity.CENTER, 0, 45);
            View viewd = toastd.getView();
            viewd.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
            TextView text = viewd.findViewById(android.R.id.message);
            text.setTextColor(Color.WHITE);
            toastd.show();
        }

        polilineasTrazo.clear();
        azure.desconectar();
        return valido;
    }

    public boolean agregarCoordenadas_Operacion() throws SQLException {
        boolean valido = true;
        AzureDB azure = new AzureDB();
        azure.conectar();
        Connection c = null;
        PreparedStatement pr = null;
        for (int i = 0; i < polilineasTrazo.size(); i++) {
            azure.conectar();
            c = azure.getConexion();
            c.setAutoCommit(false);
            pr = c.prepareStatement("INSERT INTO COORDENADAS_OPERACION (LAT, LONG, TROZO, OPERACION) VALUES (?, ?, ? , ?)");

            boolean tePasaste = false;
            int sumatoria = 10;
            try {
                for (int j = 0; j < polilineasTrazo.get(i).getPoints().size(); j++) {
                    Float lat = Float.parseFloat(String.valueOf(polilineasTrazo.get(i).getPoints().get(j).latitude));
                    Float log = Float.parseFloat(String.valueOf(polilineasTrazo.get(i).getPoints().get(j).longitude));

                    pr.setFloat(1, lat);
                    pr.setFloat(2, log);
                    pr.setInt(3, i);
                    pr.setString(4, codigoOperacion.getText().toString());
                    pr.addBatch();
                    pr.clearParameters();
                    if(j == sumatoria){
                        sumatoria += 10;
                        tePasaste = true;
                        pr.executeBatch();
                    }
                }

                if (!tePasaste) pr.executeBatch();
                c.commit();
            } catch (SQLException ex) {
                valido = false;
                System.out.println(ex.getMessage());
            }
        }

        pr.close();
        c.close();

        return valido;
    }



    public void obtenerTodo() throws SQLException {
        azure.conectar();
        String query = "SELECT * FROM FINCA";
        Statement stmt = azure.getConexion().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ArrayList<LatLng> coordenadasParcela = new ArrayList<>();
        ArrayList<LatLng> coordenadasOperacion = new ArrayList<>();
        polilinea = null;
        mMap.clear();
        nomFincas.clear();
        listCParcelas.clear();
        nomArticulos.clear();
        nomOperaciones.clear();
        nomVariedades.clear();
        if (rs != null) {
            while (rs.next()) {
                String codf = rs.getString("CODIGO");
                String nomf = rs.getString("NOMBRE");
                int colorf = rs.getInt("COLOR");
                nomFincas.add(rs.getString("NOMBRE"));
                arrayColores.add(colorf);
                int j = 0;
                query = "SELECT * FROM PARCELA WHERE FINCA='" + codf + "'";
                Statement stmt2 = azure.getConexion().createStatement();
                ResultSet rs2 = stmt2.executeQuery(query);
                if (rs2 != null) {
                    while (rs2.next()) {
                        String codp = rs2.getString("CODIGO");
                        String nomp = rs2.getString("NOMBRE");
                        boolean conjunta = rs2.getBoolean("CONJUNTA");
                        query = "SELECT * FROM COORDENADAS_PARCELA WHERE PARCELA='" + codp + "'";
                        Statement stmt3 = azure.getConexion().createStatement();
                        ResultSet rs3 = stmt3.executeQuery(query);
                        if (rs3 != null) {
                            while (rs3.next()) {
                                if(j == 0) {
                                    LatLng par = new LatLng(rs3.getFloat("LAT"), rs3.getFloat("LONG"));
                                    mMap.addMarker(new MarkerOptions()
                                            .position(par)
                                            .title("Finca: " + nomf + " - Parcela: " + nomp)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                    );
                                }
                                coordenadasParcela.add(new LatLng(rs3.getFloat("LAT"), rs3.getFloat("LONG")));
                                j++;
                            }
                            PolylineOptions opcionesPolilinea = new PolylineOptions()
                                    .visible(true)
                                    .color(colorf)
                                    .width(10);
                            polilinea = mMap.addPolyline(opcionesPolilinea);
                            polilinea.setPoints(coordenadasParcela);

                            coordenadasParcela.clear();
                            j = 0;
                            //listCParcelas.add(new Parcela(codp, nomp, (ArrayList<LatLng>) polilinea.getPoints(), conjunta));
                        }
                        String cod_conj_parc = "";
                        /*query = "SELECT * FROM CONJUNTO_PARCELAS WHERE PARCELA='" + codp + "'";
                        Statement stmt9 = azure.getConexion().createStatement();

                        ResultSet rs9 = stmt9.executeQuery(query);
                        if (rs9 != null) {
                            while (rs9.next()) {
                                cod_conj_parc += azure.obtenerParcela(rs9.getString("PARCELA")) + " ";
                            }
                        }*/

                        final Calendar cal = Calendar.getInstance();
                        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        String atoday = s.format(new Date(cal.getTimeInMillis()));
                        cal.add(Calendar.MONTH, 0);
                        while (cal.get(Calendar.DATE) > 1) {
                            cal.add(Calendar.DATE, -1); // Substract 1 day until first day of month.
                        }
                        String aweek = s.format(new Date(cal.getTimeInMillis()));
                        //String queryFecha = "SELECT * FROM OPERACION WHERE USUARIO='" + usuario.getCod() + "' AND FECHA BETWEEN '" + aweek + "' AND '" + atoday + "';";

                        /*if (!cod_conj_parc.equals("")) {
                            query = "SELECT * FROM OPERACION WHERE USUARIO='" + usuario.getCod() + "' AND PARCELA LIKE '%" + cod_conj_parc + "%' AND FECHA BETWEEN '" + aweek + "' AND '" + atoday + "';";
                            Statement stmt4 = azure.getConexion().createStatement();
                            ResultSet rs4 = stmt4.executeQuery(query);
                            if (rs4 != null) {
                                while (rs4.next()) {
                                    String codop = rs4.getString("CODIGO");
                                    String nomop = rs4.getString("NOMBRE");
                                    int colorop = rs4.getInt("COLOR");
                                    int numTrozos = rs4.getInt("TROZOS");
                                    query = "SELECT * FROM COORDENADAS_OPERACION WHERE OPERACION='" + codop + "'";
                                    Statement stmt5 = azure.getConexion().createStatement();
                                    ResultSet rs5 = stmt5.executeQuery(query);
                                    if (rs5 != null) {
                                        opcionesPolilineaDibujo = new PolylineOptions()
                                                .visible(true)
                                                .color(colorop)
                                                .width(10);
                                        int i = 0, m = 0;
                                        boolean partir = false;

                                        while (i < numTrozos) {
                                            while (rs5.next() && !partir) {
                                                int trozo = rs5.getInt("TROZO");
                                                if(m == 0) {
                                                    LatLng par = new LatLng(rs5.getFloat("LAT"), rs5.getFloat("LONG"));
                                                    mMap.addMarker(new MarkerOptions()
                                                            .position(par)
                                                            .title("Operación: " + nomop + " - Trozo: " + (trozo+1))
                                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                                    );
                                                }
                                                if (trozo == i) {
                                                    coordenadasOperacion.add(new LatLng(rs5.getFloat("LAT"), rs5.getFloat("LONG")));
                                                    m++;
                                                } else {
                                                    polilineaDibujo = mMap.addPolyline(opcionesPolilineaDibujo);
                                                    polilineaDibujo.setPoints(coordenadasOperacion);
                                                    coordenadasOperacion.clear();
                                                    partir = true;
                                                    coordenadasOperacion.add(new LatLng(rs5.getFloat("LAT"), rs5.getFloat("LONG")));
                                                    m = 0;
                                                }
                                            }
                                            if (!rs5.next()) {
                                                polilineaDibujo = mMap.addPolyline(opcionesPolilineaDibujo);
                                                polilineaDibujo.setPoints(coordenadasOperacion);
                                                coordenadasOperacion.clear();
                                                m = 0;
                                            }
                                            i++;
                                            partir = false;
                                        }
                                    }
                                }
                            }
                        }*/

                        query = "SELECT * FROM OPERACION WHERE USUARIO='" + usuario.getCod() + "' AND PARCELA LIKE '%" + codp + "%' AND FECHA BETWEEN '" + aweek + "' AND '" + atoday + "';";
                        Statement stmt4 = azure.getConexion().createStatement();
                        ResultSet rs4 = stmt4.executeQuery(query);
                        if (rs4 != null) {
                            while (rs4.next()) {
                                String codop = rs4.getString("CODIGO");
                                String nomop = rs4.getString("NOMBRE");
                                int colorop = rs4.getInt("COLOR");
                                int numTrozos = rs4.getInt("TROZOS");
                                query = "SELECT * FROM COORDENADAS_OPERACION WHERE OPERACION='" + codop + "'";
                                Statement stmt5 = azure.getConexion().createStatement();
                                ResultSet rs5 = stmt5.executeQuery(query);
                                if (rs5 != null) {
                                    opcionesPolilineaDibujo = new PolylineOptions()
                                            .visible(true)
                                            .color(colorop)
                                            .width(10);
                                    int i = 0, m = 0;;
                                    boolean partir = false;
                                    while (i < numTrozos) {
                                        while (rs5.next() && !partir) {
                                            int trozo = rs5.getInt("TROZO");
                                            if(m == 0) {
                                                LatLng par = new LatLng(rs5.getFloat("LAT"), rs5.getFloat("LONG"));
                                                mMap.addMarker(new MarkerOptions()
                                                        .position(par)
                                                        .title("Operación: " + nomop + " - Trozo: " + (trozo+1))
                                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                                );
                                            }
                                            if (trozo == i) {
                                                coordenadasOperacion.add(new LatLng(rs5.getFloat("LAT"), rs5.getFloat("LONG")));
                                                m++;
                                            } else {
                                                polilineaDibujo = mMap.addPolyline(opcionesPolilineaDibujo);
                                                polilineaDibujo.setPoints(coordenadasOperacion);
                                                coordenadasOperacion.clear();
                                                partir = true;
                                                coordenadasOperacion.add(new LatLng(rs5.getFloat("LAT"), rs5.getFloat("LONG")));
                                                m = 0;
                                            }
                                        }
                                        if (!rs5.next()) {
                                            polilineaDibujo = mMap.addPolyline(opcionesPolilineaDibujo);
                                            polilineaDibujo.setPoints(coordenadasOperacion);
                                            coordenadasOperacion.clear();
                                            m = 0;
                                        }
                                        i++;
                                        partir = false;
                                    }
                                }
                            }

                            query = "SELECT * FROM OPERACION WHERE USUARIO='" + usuario.getCod() + "' AND PARCELA LIKE '%" + nomp + "%' AND FECHA BETWEEN '" + aweek + "' AND '" + atoday + "';";
                            Statement stmt9 = azure.getConexion().createStatement();
                            ResultSet rs9 = stmt9.executeQuery(query);
                            if (rs9 != null) {
                                while (rs9.next()) {
                                    String codop = rs9.getString("CODIGO");
                                    String nomop = rs9.getString("NOMBRE");
                                    int colorop = rs9.getInt("COLOR");
                                    int numTrozos = rs9.getInt("TROZOS");
                                    query = "SELECT * FROM COORDENADAS_OPERACION WHERE OPERACION='" + codop + "'";
                                    Statement stmt5 = azure.getConexion().createStatement();
                                    ResultSet rs5 = stmt5.executeQuery(query);
                                    if (rs5 != null) {
                                        opcionesPolilineaDibujo = new PolylineOptions()
                                                .visible(true)
                                                .color(colorop)
                                                .width(10);
                                        int i = 0, m = 0;;
                                        boolean partir = false;
                                        while (i < numTrozos) {
                                            while (rs5.next() && !partir) {
                                                int trozo = rs5.getInt("TROZO");
                                                if(m == 0) {
                                                    LatLng par = new LatLng(rs5.getFloat("LAT"), rs5.getFloat("LONG"));
                                                    mMap.addMarker(new MarkerOptions()
                                                            .position(par)
                                                            .title("Operación: " + nomop + " - Trozo: " + (trozo+1))
                                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                                    );
                                                }
                                                if (trozo == i) {
                                                    coordenadasOperacion.add(new LatLng(rs5.getFloat("LAT"), rs5.getFloat("LONG")));
                                                    m++;
                                                } else {
                                                    polilineaDibujo = mMap.addPolyline(opcionesPolilineaDibujo);
                                                    polilineaDibujo.setPoints(coordenadasOperacion);
                                                    coordenadasOperacion.clear();
                                                    partir = true;
                                                    coordenadasOperacion.add(new LatLng(rs5.getFloat("LAT"), rs5.getFloat("LONG")));
                                                    m = 0;
                                                }
                                            }
                                            if (!rs5.next()) {
                                                polilineaDibujo = mMap.addPolyline(opcionesPolilineaDibujo);
                                                polilineaDibujo.setPoints(coordenadasOperacion);
                                                coordenadasOperacion.clear();
                                                m = 0;
                                            }
                                            i++;
                                            partir = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        query = "SELECT * FROM TIPO_OPERACION";
        Statement stmt6 = azure.getConexion().createStatement();
        ResultSet rs6 = stmt6.executeQuery(query);
        if (rs6 != null) {
            while (rs6.next()) {
                nomOperaciones.add(rs6.getString("NOMBRE"));
            }
        }

        query = "SELECT * FROM ARTICULO";
        Statement stmt7 = azure.getConexion().createStatement();
        ResultSet rs7 = stmt7.executeQuery(query);
        if (rs7 != null && listCArticulos.size() == 0) {
            while (rs7.next()) {
                nomArticulos.add(rs7.getString("NOMBRE"));
                listCArticulos.add(new Articulo(rs7.getString("CODIGO"), rs7.getString("NOMBRE"), "", false));
            }
        }
        query = "SELECT * FROM VARIEDAD";
        Statement stmt8 = azure.getConexion().createStatement();
        ResultSet rs8 = stmt8.executeQuery(query);
        if (rs8 != null) {
            while (rs8.next()) {
                nomVariedades.add(rs8.getString("NOMBRE"));
            }
        }

        actualizarSpinners();
        azure.desconectar();
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

    public void obtenerArticulos2(String nombre) throws SQLException {
        azure.conectar();
        listCArticulos.clear();
        String query;
        if (nombre.equals("")) {
            query = "SELECT * FROM ARTICULO";
        } else query = "SELECT * FROM ARTICULO WHERE NOMBRE LIKE '" + nombre + "%'";
        Statement stmt = azure.getConexion().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            while (rs.next()) {
                Articulo a = new Articulo(rs.getString("CODIGO"), rs.getString("NOMBRE"), /*rs.getString("DESCRIPCION"),*/ false);
                if(listArticulos.contains(a.getNomArticulo())) {
                    a.setTildado(true);
                }
                listCArticulos.add(a);
            }
        }
        azure.desconectar();
    }

    public void obtenerOperaciones() throws SQLException {
        azure.conectar();
        listConsultaOperaciones.clear();
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
        String atoday = s.format(new Date(cal.getTimeInMillis()));
        cal.add(Calendar.MONTH, 0);
        while (cal.get(Calendar.DATE) > 1) {
            cal.add(Calendar.DATE, -1); // Substract 1 day until first day of month.
        }
        String aweek = s.format(new Date(cal.getTimeInMillis()));
        String query = "SELECT * FROM OPERACION WHERE USUARIO='" + usuario.getCod() + "' AND FECHA BETWEEN '" + aweek + "' AND '" + atoday + "';";
        Statement stmt = azure.getConexion().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            while (rs.next()) {
                listConsultaOperaciones.add(new Operacion(rs.getString("CODIGO"), rs.getString("NOMBRE"), rs.getString("FECHA")));
            }
        }
        azure.desconectar();
        operaciones.setAdapter(operacionesAdapter);
    }


    public List<List<LatLng>> obtenerParcelas(int posF, ArrayList<Integer> posP) throws SQLException {
        azure.conectar();
        String query;
        List<List<LatLng>> partes = new ArrayList<>();
        int i = 0, j = 0;
        while (i < posP.size()) {
            String codF = azure.obtenerCODFinca(listFincas.getItem(posF));
            String codP = azure.obtenerCODParcelaConFinca(listCParcelas.get(posP.get(i)).getNomParcela(), codF);
            query = "SELECT * FROM PARCELA WHERE FINCA='" + codF + "' AND CODIGO='" + codP + "'";
            Statement stmt = azure.getConexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs != null) {
                while (rs.next()) {
                    String codp = rs.getString("CODIGO");
                    String nombre = rs.getString("NOMBRE");
                    int colorf = azure.obtenerColorFinca(codF);
                    query = "SELECT * FROM COORDENADAS_PARCELA WHERE PARCELA='" + codp + "'";
                    Statement stmt2 = azure.getConexion().createStatement();
                    ResultSet rs2 = stmt2.executeQuery(query);
                    ArrayList<LatLng> coordenadasParcela = new ArrayList<>();
                    if (rs2 != null) {
                        while (rs2.next()) {
                            if(j == 0) {
                                LatLng par = new LatLng(rs2.getFloat("LAT"), rs2.getFloat("LONG"));
                                mMap.addMarker(new MarkerOptions()
                                        .position(par)
                                        .title("Parcela: " + nombre)
                                );
                            }
                            coordenadasParcela.add(new LatLng(rs2.getFloat("LAT"), rs2.getFloat("LONG")));
                            j++;
                        }
                        opcionesPolilineaDibujo = new PolylineOptions()
                                .color(colorf)
                                .visible(true)
                                .clickable(true)
                                .width(10);
                        polilinea = mMap.addPolyline(opcionesPolilineaDibujo);
                        polilinea.setPoints(coordenadasParcela);
                        partes.add(polilinea.getPoints());
                        j = 0;
                    }
                }
                actualizarSpinners();
            }
            i++;
        }

        azure.desconectar();
        return partes;
    }

    public List<LatLng> obtenerParcela(int posF, int posP) throws SQLException {
        azure.conectar();
        String query;
        String codF = azure.obtenerCODFinca(listFincas.getItem(posF));
        String codP = azure.obtenerCODParcela(listCParcelas.get(posP).getNomParcela());
        query = "SELECT * FROM PARCELA WHERE FINCA='" + codF + "' AND CODIGO='" + codP + "'";
        Statement stmt = azure.getConexion().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            while (rs.next()) {
                String codp = rs.getString("CODIGO");
                int colorf = azure.obtenerColorFinca(codF);
                query = "SELECT * FROM COORDENADAS_PARCELA WHERE PARCELA='" + codp + "'";
                Statement stmt2 = azure.getConexion().createStatement();
                ResultSet rs2 = stmt2.executeQuery(query);
                ArrayList<LatLng> coordenadasParcela = new ArrayList<>();
                if (rs2 != null) {
                    while (rs2.next()) {
                        coordenadasParcela.add(new LatLng(rs2.getFloat("LAT"), rs2.getFloat("LONG")));
                    }
                    opcionesPolilineaDibujo = new PolylineOptions()
                            .visible(true)
                            .width(10)
                            .color(colorf);
                    polilinea = mMap.addPolyline(opcionesPolilineaDibujo);
                    polilinea.setPoints(coordenadasParcela);
                }
            }
        }
        azure.desconectar();
        return polilinea.getPoints();
    }

    public void actualizarCheckedArticulos() {
        for(int i = 0; i < listArticulos.size(); i++) {
            for (int j = 0; j < listCArticulos.size(); j++)
            {
                if(listCArticulos.get(j).getNomArticulo().equals(listArticulos.get(i))) {
                    listCArticulos.get(j).setTildado(true);
                }
            }
        }
        articulosCAdapter = new CheckboxAdapterArticulos(getActivity(), R.layout.custom_checklist_view, listCArticulos);
        checkedArticulos.setAdapter(articulosCAdapter);
    }

    public void actualizarSpinners() {
        nombreFincas2 = nomFincas.toArray(new String[0]);
        listFincas = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, nombreFincas2);
        listFincas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFincas.setAdapter(listFincas);
        spinnerFincas.setSelection(spinnerPosFincas);

        checkedParcelas.setAdapter(parcelaAdapter);

        nombreOperaciones = nomOperaciones.toArray(new String[0]);
        listOperaciones = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, nombreOperaciones);
        listOperaciones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOperaciones.setAdapter(listOperaciones);
        spinnerOperaciones.setSelection(spinnerPosOperaciones);

        //listArticulos = new ArrayList<>();
        articulosAdapter = new ListAdapterArticulos(getActivity(), R.layout.custom_listarticulos_view, listArticulos, listCArticulos);
        articulos.setAdapter(articulosAdapter);

        for (int i = 0; i < listCArticulos.size(); i++) {
            listCArticulos.get(i).setTildado(false);
        }
        articulosCAdapter = new CheckboxAdapterArticulos(getActivity(), R.layout.custom_checklist_view, listCArticulos);
        checkedArticulos.setAdapter(articulosCAdapter);
    }

    public void actualizarSpinnersWhenFinca() {
        nombreFincas2 = nomFincas.toArray(new String[0]);
        listFincas = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, nombreFincas2);
        listFincas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFincas.setAdapter(listFincas);
        spinnerFincas.setSelection(spinnerPosFincas);

        checkedParcelas.setAdapter(parcelaAdapter);

        nombreOperaciones = nomOperaciones.toArray(new String[0]);
        listOperaciones = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, nombreOperaciones);
        listOperaciones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOperaciones.setAdapter(listOperaciones);
        spinnerOperaciones.setSelection(spinnerPosOperaciones);
    }

    public void actualizarDatosParcelas() throws SQLException {
        azure.conectar();
        String finca = spinnerFincas.getItemAtPosition(spinnerPosFincas).toString();
        String codfinca = azure.obtenerCODFinca(finca);
        String query = "SELECT * FROM PARCELA WHERE FINCA='" + codfinca + "'";
        ArrayList<LatLng> coordenadasParcela = new ArrayList<>();
        Statement stmt = azure.getConexion().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        listCParcelas.clear();
        if (rs != null) {
            while (rs.next()) {
                String codp = rs.getString("CODIGO");
                String nomp = rs.getString("NOMBRE");
                boolean conjunta = rs.getBoolean("CONJUNTA");
                query = "SELECT * FROM COORDENADAS_PARCELA WHERE PARCELA='" + codp + "'";
                Statement stmt3 = azure.getConexion().createStatement();
                ResultSet rs3 = stmt3.executeQuery(query);
                if (rs3 != null) {
                    while (rs3.next()) {
                        coordenadasParcela.add(new LatLng(rs3.getFloat("LAT"), rs3.getFloat("LONG")));
                    }
                    //polilinea.setPoints(coordenadasParcela);
                    //Polyline p = mMap.addPolyline();
                    //p.setPoints(coordenadasParcela);

                    listCParcelas.add(new Parcela(codp, nomp, (ArrayList<LatLng>) coordenadasParcela/*polilinea.getPoints()*/, conjunta));
                    coordenadasParcela.clear();
                }
            }
        }
        actualizarSpinnersWhenFinca();
        azure.desconectar();
    }

    public void actualizarDatosTipoOperacion() throws SQLException {
        azure.conectar();
        String tipo_op = spinnerOperaciones.getItemAtPosition(spinnerPosOperaciones).toString();
        String query = "SELECT * FROM TIPO_OPERACION WHERE NOMBRE='" + tipo_op + "'";
        Statement stmt = azure.getConexion().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs != null) {
            if (rs.next()) {
                if (rs.getString("TIENE_ARTICULOS").equals("1")) {
                    addArticulo.setEnabled(true);
                    addArticulo.setVisibility(View.VISIBLE);
                } else {
                    addArticulo.setEnabled(false);
                    addArticulo.setVisibility(View.INVISIBLE);
                }

                int intColor = rs.getInt("COLOR");
                String cadenaColor = "";
                switch(intColor) {
                    case -16711936:
                        cadenaColor = "VERDE";
                        break;
                    case -16776961:
                        cadenaColor = "AZUL";
                        break;
                    case -256:
                        cadenaColor = "AMARILLO";
                        break;
                    case -65536:
                        cadenaColor = "ROJO";
                        break;
                    case -65281:
                        cadenaColor = "MAGENTA";
                        break;
                    case -16711681:
                        cadenaColor = "CYAN";
                        break;
                    case -16777216:
                        cadenaColor = "BLANCO";
                        break;
                    case -1:
                        cadenaColor = "NEGRO";
                        break;
                    default: cadenaColor = "S/N";
                }

                coloresArray[0] = cadenaColor;
                colorDibujo.color(intColor);
                //{"Azul", "Verde", "Rojo", "Amarillo", "Magenta", "Cyan", "Negro", "Blanco"};

                Spinner SpinnerColorCrearOperacion = rootView.findViewById(R.id.SpinnerColorCrearOperacion);
                ArrayAdapter<String> listColores = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, coloresArray);
                listColores.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinnerColorCrearOperacion.setAdapter(listColores);
            }
        }
        azure.desconectar();
    }
}

