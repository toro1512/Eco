package com.ecocarg.Fragmentos;


import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ecocarg.Adapter.CustomInfoVentanaAdapter;
import com.ecocarg.Entidades.Locales;
import com.ecocarg.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private View rootView;
    private MapView mapView;
    double latMapa,logMapa;
    boolean primera=false;
    private GoogleMap gMap;
    ArrayList<Locales> listaLocales;
    boolean puede;
    float zoomm;





    public MapsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       try {
              listaLocales = (ArrayList<Locales>) getArguments().getSerializable("lislocales");
              puede= getArguments().getBoolean("verificar");
              latMapa=getArguments().getDouble("latitu");
              logMapa=getArguments().getDouble("longi");
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
         rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = rootView.findViewById(R.id.mapa);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng aqui_estoy;
        String lat_b,lon_b;

        aqui_estoy = new LatLng(latMapa,logMapa);
        gMap.addMarker(new MarkerOptions().position(aqui_estoy).title("UBICACION"));

        if(listaLocales.size()>0)
        {
            gMap.setInfoWindowAdapter(new CustomInfoVentanaAdapter(this.getContext(), LayoutInflater.from(getActivity()), listaLocales));
            for (int i = 0; i < listaLocales.size() ; i++) {
                lon_b = listaLocales.get(i).getCoordena().substring(6, listaLocales.get(i).getCoordena().indexOf(" "));
                lat_b = listaLocales.get(i).getCoordena().substring(listaLocales.get(i).getCoordena().indexOf(" "), listaLocales.get(i).getCoordena().length() - 1);
                LatLng unisimon = new LatLng(Double.parseDouble(lat_b), Double.parseDouble(lon_b));
                gMap.addMarker(new MarkerOptions().position(unisimon).title(listaLocales.get(i).getNombre()).
                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
            }

        }
        else
        {
            Toast.makeText(getContext(),"Estas trabajando modo offline",Toast.LENGTH_LONG).show();
            Toast.makeText(getContext(),"No tenemos ecocargadores cerca",Toast.LENGTH_LONG).show();
           }
        zoomm=14;
       CameraPosition camara = new CameraPosition.Builder()
                .zoom(zoomm)
                .target(aqui_estoy)
                .tilt(30)
                .bearing(90)
                .build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(camara));

    }

}
