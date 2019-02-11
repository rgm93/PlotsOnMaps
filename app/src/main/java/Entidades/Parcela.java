package Entidades;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;

public class Parcela {
    private ArrayList<Operacion> operaciones;
    private String codParcela;
    private String nomParcela;
    private ArrayList<LatLng> latlngParcela;
    private Polyline recintoParcela;
    private Variedad variedad;
    private boolean tildado;
    private boolean parcelaConjunta;

    public Parcela(String codParcela, String nomParcela, ArrayList<LatLng> latlngParcela, boolean parcelaConjunta) {
        this.operaciones = new ArrayList<>();
        this.codParcela = codParcela;
        this.nomParcela = nomParcela;
        this.latlngParcela = latlngParcela;
        this.parcelaConjunta = parcelaConjunta;
    }

    public ArrayList<Operacion> getOperaciones() { return operaciones; }

    public String getNomParcela() { return nomParcela; }

    public ArrayList<LatLng> getRecintoParcela() { return latlngParcela; }

    public boolean esTildado() {
        return tildado;
    }

    public void setTildado(boolean tildado) {
        this.tildado = tildado;
    }

    public boolean esParcelaConjunta() {
        return parcelaConjunta;
    }

    public void setParcelaConjunta(boolean parcelaConjunta) {
        this.parcelaConjunta = parcelaConjunta;
    }
}
