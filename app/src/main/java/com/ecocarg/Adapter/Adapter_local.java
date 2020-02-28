package com.ecocarg.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecocarg.Entidades.Locales;
import com.ecocarg.R;

import java.io.Serializable;
import java.util.ArrayList;

public class Adapter_local extends BaseAdapter {

    private Context context;
    private ArrayList<Locales> listItem;

    public Adapter_local(Context context, ArrayList<Locales> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Locales Item= (Locales) getItem(position);

        convertView= LayoutInflater.from(context).inflate(R.layout.list_locales,null);
        ImageView imgFoto= convertView.findViewById(R.id.imagen_local);
        TextView tvTitulo= convertView.findViewById(R.id.nombre_local);
        TextView tvContenido= convertView.findViewById(R.id.direccion_local);
        imgFoto.setImageBitmap(Item.getImagen_b());
        tvContenido.setText(Item.getDireccion());
        tvTitulo.setText(Item.getNombre());

        return convertView;
    }
}