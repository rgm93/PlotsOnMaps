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

public class ListAdapterConsulta extends ArrayAdapter<String>{

    private Activity context;
    private int id;
    private ArrayList<String> consultas;

    public ListAdapterConsulta(Activity context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.id = resource;
        this.consultas = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(id, null);
        }

        final String s = consultas.get(position);
        TextView tvNombre = convertView.findViewById(R.id.nombreArticulo);

        tvNombre.setText(s);
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
