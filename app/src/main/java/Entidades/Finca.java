package Entidades;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;

import java.util.ArrayList;

public class Finca {
    private ArrayList<Parcela> parcelas;
    private String codFinca;
    private String nomFinca;
    private LatLng latlngFinca;
    private int color;
    private Polygon recintoFinca;

    public Finca(String codFinca, String nomFinca, int color) {
        this.parcelas = new ArrayList<>();
        this.codFinca = codFinca;
        this.nomFinca = nomFinca;
        this.color = color;
    }

    public Finca(String codFinca, String nomFinca, LatLng latlngFinca) {
        this.parcelas = new ArrayList<>();
        this.codFinca = codFinca;
        this.nomFinca = nomFinca;
        this.latlngFinca = latlngFinca;
    }

    public Finca(String codFinca, String nomFinca) {
        this.parcelas = new ArrayList<>();
        this.codFinca = codFinca;
        this.nomFinca = nomFinca;
    }

    public ArrayList<Parcela> getParcelas() { return parcelas; }
    public void setParcelas(ArrayList<Parcela> parcelas) { this.parcelas = parcelas; }
    public String getCodFinca() { return codFinca; }
    public void setCodFinca(String codFinca) { this.codFinca = codFinca; }
    public String getNomFinca() { return nomFinca; }
    public void setNomFinca(String nomFinca) { this.nomFinca = nomFinca; }
    public LatLng getLatlngFinca() { return latlngFinca; }
    public void setLatlngFinca(LatLng latlngFinca) { this.latlngFinca = latlngFinca; }
    public Polygon getRecintoFinca() { return recintoFinca; }
}
