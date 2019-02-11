package Extra;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import Entidades.Mensaje;
import com.example.ruben.Paginas.R;

import java.util.List;

public class ListaMensajesAdapter extends BaseAdapter {

    private Context context;
    private List<Mensaje> listaMensajes;

    public ListaMensajesAdapter(Context context, List<Mensaje> listaMensajes) {
        this.context = context;
        this.listaMensajes = listaMensajes;
    }

    @Override
    public int getCount() {
        return listaMensajes.size();
    }

    @Override
    public Object getItem(int position) {
        return listaMensajes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View v = View.inflate(context, R.layout.item_mensaje, null);
        TextView titulo = v.findViewById(R.id.mensaje_titulo);
        TextView descripcion = v.findViewById(R.id.mensaje_descripcion);

        titulo.setText(listaMensajes.get(position).getTitulo());
        descripcion.setText(listaMensajes.get(position).getDescripcion());

        v.setTag(listaMensajes.get(position).getId());
        return v;
    }
}
