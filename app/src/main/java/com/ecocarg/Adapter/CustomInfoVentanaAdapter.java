package com.ecocarg.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecocarg.Entidades.Locales;
import com.ecocarg.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class CustomInfoVentanaAdapter  implements GoogleMap.InfoWindowAdapter {
    private static final String TAG = "CustomInfoWindowAdapter";
    private LayoutInflater inflater;
    private Context mContext;
    ArrayList<Locales> listalocales;
    ImageView foto;
    TextView Nombre,Direccion;

    public CustomInfoVentanaAdapter(Context context, LayoutInflater inflater, ArrayList<Locales> lista){
        this.inflater = inflater;
        listalocales=lista;
        mContext=context;
    }

    @Override
    public View getInfoContents(final Marker m) {
        //Carga layout personalizado.

        View v = inflater.inflate(R.layout.fragment_infoventana_layout, null);
        foto= v.findViewById(R.id.info_window_imagen);
        Nombre= v.findViewById(R.id.info_window_nombre);
        Direccion= v.findViewById(R.id.info_window_direccion);
        for(int i=0;i<listalocales.size();i++) {
            if(listalocales.get(i).getNombre().equals(m.getTitle()))
            {
                if(listalocales.get(i).getNombre().equals("UBICACION"))
                {
                    Nombre.setVisibility(View.INVISIBLE);
                    Direccion.setText("Aqui estoy");
                    foto.setImageResource(R.drawable.persona);

                }
                else {
                    Nombre.setVisibility(View.VISIBLE);
                    Direccion.setVisibility(View.VISIBLE);
                    foto.setVisibility(View.VISIBLE);
                    Direccion.setText(listalocales.get(i).getDireccion());
                    Nombre.setText(listalocales.get(i).getNombre());
                    foto.setImageBitmap(listalocales.get(i).getImagen_b());
                }
                break;
            }
        }
        //  Toast.makeText(mContext,listalocales.get(0).getNombre() ,Toast.LENGTH_LONG).show();

       //AQUI SE LLENA LOS VECTORES Y SE HACE UN SWITCH DE LO Q VOY A MOSTRAR
        return v;
    }

    @Override
    public View getInfoWindow(Marker m) {


        return null;
    }


}
