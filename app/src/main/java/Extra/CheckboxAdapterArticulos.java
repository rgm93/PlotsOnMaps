package Extra;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.ruben.Paginas.R;

import java.util.ArrayList;

import Entidades.Articulo;
import Entidades.Parcela;

public class CheckboxAdapterArticulos extends ArrayAdapter<Articulo>{

    private Activity context;
    private int id;
    private ArrayList<Articulo> articulos;

    public CheckboxAdapterArticulos(Activity context, int resource, ArrayList<Articulo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.id = resource;
        this.articulos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(id, null);
        }

        final Articulo a = articulos.get(position);
        TextView tvNombre = convertView.findViewById(R.id.textoCheck);
        CheckBox checkb = convertView.findViewById(R.id.checkbutton);
        checkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                a.setTildado(b);
            }
        });

        tvNombre.setText(a.getNomArticulo());
        checkb.setChecked(a.esTildado());
        return convertView;
    }

    public ArrayList<Integer> getArticuloPos() {
        ArrayList<Integer> posiciones = new ArrayList<>();
        for(int i = 0; i < articulos.size(); i++) {
            if(articulos.get(i).esTildado()) {
                posiciones.add(i);
            }
        }
        return posiciones;
    }

    public int getNumArticulos() {return articulos.size(); }
}
