package Entidades;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;

public class Variedad {
    private String codVariedad;
    private String nomVariedad;

    public Variedad(String codVariedad, String nomVariedad) {
        this.codVariedad = codVariedad;
        this.nomVariedad = nomVariedad;

    }

    public String getCodVariedad() { return codVariedad; }
    public void setCodVariedad(String codVariedad) { this.codVariedad = codVariedad; }
    public String getNomVariedad() { return nomVariedad; }
    public void setNomVariedad(String nomVariedad) { this.nomVariedad = nomVariedad; }


}
