package com.example.ruben.Paginas;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import Entidades.Usuario;

public class BottomActivity extends AppCompatActivity {

    private GoogleMap mMap = null;
    private GoogleApiClient cliente = null;
    private Usuario usuario = null;
    private MainFragmentOperador m2 = new MainFragmentOperador(mMap, cliente, usuario);
    private Object o;
    private FragmentTransaction ft;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Bundle b = new Bundle();

            switch (item.getItemId()) {
                case R.id.modoZona:
                    if (usuario.getTipo().equals("TECNICO")) o = m2;
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.contenido, (Fragment) o, "Zonas");
                    ft.commit();
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);

        Bundle b = getIntent().getExtras();

        if (b != null) {
            usuario = (Usuario) b.getSerializable("usuario");
            m2 = new MainFragmentOperador(mMap, cliente, usuario);
            m2.setRetainInstance(true);
            FragmentTransaction ft = null;
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contenido, m2, "Zonas");
            ft.addToBackStack("2");
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
