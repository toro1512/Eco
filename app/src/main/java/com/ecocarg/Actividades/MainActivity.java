package com.ecocarg.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.ProgressDialog;
import android.widget.ProgressBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.ecocarg.Entidades.Locales;
import com.ecocarg.Fragmentos.BannerFragment;
import com.ecocarg.Fragmentos.CuatroFragment;
import com.ecocarg.Fragmentos.DosFragment;
import com.ecocarg.Fragmentos.ListarFragment;
import com.ecocarg.Fragmentos.MapsFragment;
import com.ecocarg.Fragmentos.PerfilFragment;
import com.ecocarg.Fragmentos.PresentacionFragment;
import com.ecocarg.Fragmentos.TresFragment;
import com.ecocarg.Fragmentos.UnoFragment;
import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;
import com.ecocarg.R;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements UnoFragment.OnFragmentInteractionListener, DosFragment.OnFragmentInteractionListener,
        TresFragment.OnFragmentInteractionListener,PresentacionFragment.OnFragmentInteractionListener, CuatroFragment.OnFragmentInteractionListener, ListarFragment.OnFragmentInteractionListener{

    public DrawerLayout drawable;
    public NavigationView navigationView;
    LocationManager ubicar;
    LocationManager ubicaccion;
    private ArrayList<Locales> listaLocales;
    private AsyncHttpClient client;
    public String lat,log,resul;

    boolean primera_vez=false;
    public Toolbar toolbar;
    String correo_usu;
    SharedPreferences preferencesleer;
    SharedPreferences preferencesleer2;
    




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setToolbar();
        preferencesleer=this.getSharedPreferences("primeravez",MODE_PRIVATE);
        correo_usu=preferencesleer.getString("email","Sin correo");
        listaLocales=new ArrayList<Locales>();
        client=new AsyncHttpClient();
        drawable = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navview);

        if(savedInstanceState==null)
        {primera_vez=true;}
        else
        {primera_vez=false;}


        if(!EstadoGPS())
        {ActivaGPS();}
        else {
           iniciar();
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if(!EstadoGPS())
        {ActivaGPS();}
        else
        {if(!primera_vez)
            iniciar();}

    }


     private void setToolbar() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void Cargar_fragmentInicial() {
        Fragment fragment=null;

try {
         Bundle bundle = new Bundle();

         bundle.putSerializable("lislocales", listaLocales);
         fragment=new PresentacionFragment();
         fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.content_fragment,fragment).
                commit();
        MenuItem menuItem = navigationView.getMenu().getItem(0);
        menuItem.setChecked(true);
        getSupportActionBar().setTitle(menuItem.getTitle());
}
catch (NullPointerException s){

    s.printStackTrace();
}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_mapa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Fragment fragment=null;
        Bundle bundle = new Bundle();
        MenuItem menuItem = navigationView.getMenu().getItem(0);
        switch (item.getItemId()) {
            case android.R.id.home:
                drawable.openDrawer(GravityCompat.START);
                break;
            case R.id.menu_verLista:


                bundle.putSerializable("lislocales", listaLocales);
                fragment=new ListarFragment();
                fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.content_fragment, fragment).
                            commit();
                    menuItem = navigationView.getMenu().getItem(0);
                    menuItem.setChecked(true);
                    getSupportActionBar().setTitle(menuItem.getTitle());

                break;
            case R.id.menu_verMapa:

                bundle.putSerializable("lislocales", listaLocales);
                fragment=new MapsFragment();
                fragment.setArguments(bundle);
                  getSupportFragmentManager().beginTransaction().
                            replace(R.id.content_fragment, fragment).
                            commit();
                    menuItem.setChecked(true);
                    getSupportActionBar().setTitle(menuItem.getTitle());

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean Cerrar_aplicacion() {
        boolean var = false;

        new AlertDialog.Builder(this).setTitle("Salir").
                setMessage("¿Desea salir de la aplicación?").
                setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      cargara();

                        System.exit(0);
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean var1 = false;
            }
        }).show();


        return var;
    }
public void cargara(){

    preferencesleer2 = getSharedPreferences("primeravez", MODE_PRIVATE);
    SharedPreferences.Editor editor = preferencesleer2.edit();
    editor.putString("sesion","cerrada");
    editor.commit();
}
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void localizame() {

          if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(this, new String[]{
                  Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
          },1000);
        }
        ubicaccion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(ubicaccion != null) {
            Location loc = ubicaccion.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc == null) {
                loc = ubicaccion.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (loc == null) {
                    loc = ubicaccion.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                }
            }
            if(loc!=null){
                lat=String.valueOf(loc.getLatitude());
                log=String.valueOf(loc.getLongitude());
                resul=log+"%20"+lat;//ojo
                String url = "http://ecotics.co/prueba_json_HOY.php?var="+resul+"&uso="+correo_usu;
                client.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        if(statusCode==200){
                            listarEco(new String(responseBody));
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(getApplicationContext(),"No se comunico con el Servidor",Toast.LENGTH_LONG).show();

                    }
                });
            }

            else {
                Toast.makeText(this, "no pudo ubicarme por GPS", Toast.LENGTH_LONG).show();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Locales locales = new Locales();
                locales.setNombre("UBICACION");
                locales.setDireccion("MI DIRECCION");
                locales.setDato("SIN IMAGEN");
                locales.setCoordena("0.00000"+" "+"0.000000");
                listaLocales.add(locales);

            }
            }
        else
        {
            Locales locales = new Locales();
            locales.setNombre("UBICACION");
            locales.setDireccion("MI DIRECCION");
            locales.setDato("SIN IMAGEN");
            locales.setCoordena("0.00000"+" "+"0.000000");
            listaLocales.add(locales);
        }



    }
    private void listarEco(String response){

        if(response.length()>3) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    Locales locales = new Locales();
                    locales.setNombre(jsonArray.getJSONObject(i).getString("NOMBRE"));
                    locales.setDireccion(jsonArray.getJSONObject(i).getString("DIRECCION"));
                    locales.setDato(jsonArray.getJSONObject(i).getString("IMAGEN"));
                    locales.setCoordena(jsonArray.getJSONObject(i).getString("COORDENADA"));
                    locales.setModelo(jsonArray.getJSONObject(i).getString("MODELO"));

                    listaLocales.add(locales);

                }

            } catch (Exception e) {
                e.printStackTrace();
         //       Toast.makeText(getApplicationContext(),"Problemas en la consulta",Toast.LENGTH_LONG).show();

            }
        }
        else{
            Toast.makeText(getApplicationContext(),"No tenemos ECOCargadores cerca de tu ubicacion",Toast.LENGTH_LONG).show();
        }
        Locales locales = new Locales();
        locales.setNombre("UBICACION");
        locales.setDireccion("MI DIRECCION");
        locales.setDato("SIN IMAGEN");
        locales.setCoordena(lat+" "+log);
        listaLocales.add(locales);

    }
/*
            Adapter_locales adapter = new Adapter_locales(listaLocales);
            recycler.setAdapter(adapter);


    }*/

    private boolean EstadoGPS() {
        boolean valor;
        ubicar = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {

            if (!ubicar.isProviderEnabled(LocationManager.GPS_PROVIDER))
                valor = false;
            else
                valor = true;
        } catch (Exception e) {
            e.printStackTrace();
            valor = false;
        }
        return valor;
    }

    private void ActivaGPS() {
        new AlertDialog.Builder(this).setTitle("Señal GPS").
                setMessage("El GPS esta Desactivado.. Para ingresar a la app debe activarlo, Desea activarlo ahora?").
                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).show();
    }
    public void iniciar(){

            localizame();
            if(primera_vez) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Cargar_fragmentInicial();
                    }
                }, 1000);

            }
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                boolean fragmentTransaction = false;
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.menu_lugar:
                        fragment = new MapsFragment();
                        fragmentTransaction = true;
                        break;
                    case R.id.menu_perfil:
                        fragment = new PerfilFragment();
                        fragmentTransaction = true;
                        break;
                    case R.id.menu_sobre:
                        fragment = new BannerFragment();
                        fragmentTransaction = true;
                        break;
                    case R.id.menu_cerrar:
                        if (Cerrar_aplicacion())
                            fragmentTransaction = false;
                        break;
                    case R.id.menu_contacto:
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@ecotics.co"});
                        startActivity(emailIntent);
                        break;
                    case R.id.menu_terminos:
                        Uri uri = Uri.parse("http://www.ecotics.co/");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        break;
                }
                if (fragmentTransaction) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("lislocales", listaLocales);
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.content_fragment, fragment).
                            commit();
                    menuItem.setChecked(true);
                    getSupportActionBar().setTitle(menuItem.getTitle());
                    drawable.closeDrawers();
                }

                return false;
            }
        });



    }

    @Override
    public void onBackPressed() {

    }


}
