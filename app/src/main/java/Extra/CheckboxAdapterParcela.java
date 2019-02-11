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
import Entidades.Parcela;

public class CheckboxAdapterParcela extends ArrayAdapter<Parcela>{

    private Activity context;
    private int id;
    private ArrayList<Parcela> parcelas;

    public CheckboxAdapterParcela(Activity context, int resource, ArrayList<Parcela> objects) {
        super(context, resource, objects);
        this.context = context;
        this.id = resource;
        this.parcelas = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(id, null);
        }

        final Parcela p = parcelas.get(position);
        TextView tvNombre = convertView.findViewById(R.id.textoCheck);
        CheckBox checkb = convertView.findViewById(R.id.checkbutton);
        checkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                p.setTildado(b);
            }
        });

        String nombre_conjunto = p.getNomParcela();
        tvNombre.setText(nombre_conjunto);
        checkb.setChecked(p.esTildado());
        return convertView;
    }

    public ArrayList<Integer> getParcelaPos() {
        ArrayList<Integer> posiciones = new ArrayList<>();
        for(int i = 0; i < parcelas.size(); i++) {
            if(parcelas.get(i).esTildado()) {
                posiciones.add(i);
            }
        }
        return posiciones;
    }
}
