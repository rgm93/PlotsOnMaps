package Extra;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ruben.Paginas.R;

import java.util.ArrayList;

import Entidades.Articulo;

public class ListAdapterArticulos extends ArrayAdapter<String>{

    private Activity context;
    private int id;
    private ArrayList<String> articulos;
    private ArrayList<Articulo> articulosChecked;

    public ListAdapterArticulos(Activity context, int resource, ArrayList<String> objects, ArrayList<Articulo> articulosChecked) {
        super(context, resource, objects);
        this.context = context;
        this.id = resource;
        this.articulos = objects;
        this.articulosChecked = articulosChecked;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(id, null);
        }

        final String s = articulos.get(position);
        TextView tvNombre = convertView.findViewById(R.id.nombreArticulo);
        final ImageButton b = convertView.findViewById(R.id.botonBorrarArticulo);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0, j = 0;
                boolean encontrado = false;
                while (j < articulosChecked.size() && !encontrado) {
                    if (articulos.get(position).equals(articulosChecked.get(j).getNomArticulo())) {
                        encontrado = true;
                        articulosChecked.get(j).setTildado(false);
                    }
                    else j++;
                }

                articulos.remove(position);
                notifyDataSetChanged();
            }
        });

        tvNombre.setText(s);
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
