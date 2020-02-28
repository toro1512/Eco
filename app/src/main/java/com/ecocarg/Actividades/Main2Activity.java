package com.ecocarg.Actividades;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Build;
import java.util.Calendar;
import java.util.HashMap;
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
import com.ecocarg.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.loopj.android.http.*;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import java.io.IOException;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class Main2Activity extends AppCompatActivity implements UnoFragment.OnFragmentInteractionListener, DosFragment.OnFragmentInteractionListener,
        TresFragment.OnFragmentInteractionListener,PresentacionFragment.OnFragmentInteractionListener, CuatroFragment.OnFragmentInteractionListener, ListarFragment.OnFragmentInteractionListener {
    ////////////////////////////////////////////////////////////////////////////////////////////
    private final static String TAG = MainActivity.class.getSimpleName();
    public HashMap<String, Integer> subProcess;
////////////////////////////////////////////////////////////////////////////////////////////7

    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;
/////////////////////////////////////////////////////////////////////////////////////////////////////
    TextView titulo, cargando;
    Integer counter = 1;
    int level=0;
    Double latMap,logMap;
    private ProgressBar progressBar;
    FloatingActionButton fb_perfil, fb_sobre, fb_contacto, fb_salir, fb_terminos, fb_mapa, fb_listar;
    FloatingActionsMenu fb_menu;
    boolean conexion,no_eco,no_conecto;
    ///////////////////////////////
    LocationManager ubicaccion,ubicar;
    private ArrayList<Locales> listaLocales;
    private SyncHttpClient client;
    public String lat, log, resul;
    /////////////////////////////////////////////////////7
    String correo_usu;
    SharedPreferences preferencesleer2, preferencesleer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //////////////////////////////////////////7SERVIVIO///////////////////////////////
               /////////////////////////////////////////
        preferencesleer = this.getSharedPreferences("primeravez", MODE_PRIVATE);
        correo_usu = preferencesleer.getString("email", "Sin correo");
        ///////////////////////////////////////////////////////////
        mostar(this);
        if (savedInstanceState == null) {
            Fragment newFragment = new PresentacionFragment();
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.content_fragment, newFragment).
                    commit();
        }
        /////////////////////////////////////////////////////////
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(10);
        counter = 1;
        no_eco=false;
        no_conecto=false;
        ///////////////////////////////////////////////7

        titulo = findViewById(R.id.mostar_lugar);
        fb_contacto = findViewById(R.id.FabContacto);
        fb_perfil = findViewById(R.id.FabPerfil);
        fb_sobre = findViewById(R.id.FabIntro);
        fb_salir = findViewById(R.id.FabSalir);
        fb_terminos = findViewById(R.id.Fabtermino);
        fb_menu = findViewById(R.id.grupofab);
        fb_mapa = findViewById(R.id.fab_mapa);
        fb_listar = findViewById(R.id.fab_listar);
        // cargando=findViewById(R.id.tx_argando);
        ///////////////////////////////////////////////////
        fb_menu.setEnabled(false);
        fb_listar.setEnabled(false);
        fb_mapa.setEnabled(false);
        //  cargando.setVisibility(View.VISIBLE);
        ////////////////////////////////////////////////////////////
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);

        /////////////////////////////////////////////////////
        listaLocales = new ArrayList<Locales>();
        client = new SyncHttpClient();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            new MyAsyncTask().execute(10);
        }

//////////////////////////////////////////////////////////////////////////////////////////////
        fb_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titulo.setText("Perfil");
                fb_menu.collapse();
                cargar_perfil();
            }
        });
//////////////////////////////////////////////////////////////////////////////////////////////
        fb_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titulo.setText("Mapa");
                fb_menu.collapse();
                if(conexion)
                    cargar_mapa();
                else
                {
                    fb_menu.setEnabled(false);
                    fb_listar.setEnabled(false);
                    fb_mapa.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                        new MyAsyncTask().execute(10);
                    }
                    cargar_mapa();
                }
            }
        });

//////////////////////////////////////////////////////////////////////////////////////////////
        fb_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fb_menu.collapse();
                Cerrar_aplicacion();
            }
        });
//////////////////////////////////////////////////////////////////////////////////////////////
        fb_listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titulo.setText("Listar");
                fb_menu.collapse();
                if(conexion)
                cargar_lista();
                else
                {
                    fb_menu.setEnabled(false);
                    fb_listar.setEnabled(false);
                    fb_mapa.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                        new MyAsyncTask().execute(10);
                    }

                    cargar_lista();
                }
            }
        });
//////////////////////////////////////////////////////////////////////////////////////////////
        fb_contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titulo.setText("Contacto");
                fb_menu.collapse();
                cargar_contacto();
            }
        });
//////////////////////////////////////////////////////////////////////////////////////////////
        fb_terminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titulo.setText("Terminos");
                fb_menu.collapse();
                cargar_terminos();
            }
        });
//////////////////////////////////////////////////////////////////////////////////////////////
        fb_sobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titulo.setText("SobreEcoTICS");
                fb_menu.collapse();
                cargar_eco();
            }
        });
        Intent monitorIntent = new Intent(this, BatteryLevelReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 123456, monitorIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().get(Calendar.MILLISECOND), 1200, pendingIntent);

    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    class MyAsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            conexion = Ping();
            publishProgress(counter);
            if (conexion) {
                localizame();
            } else {
                Log.d("TAG", "doInBackground: No tiene");
                return "No tiene conexion";
            }
            return "Tarea completa!. =)";
        }

        @Override
        protected void onPostExecute(String result) {

            fb_menu.setEnabled(true);
            fb_listar.setEnabled(true);
            fb_mapa.setEnabled(true);
            //       cargando.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.GONE);
            if(no_eco)
            Toast.makeText(getApplicationContext(),"No tenemos ECOCargadores cerca de tu ubicación",Toast.LENGTH_LONG).show();
            if(!conexion) {
                Toast.makeText(getApplicationContext(), "No tiene conexión a internet, se activa modo: offline", Toast.LENGTH_LONG).show();
                if(!sin_cone()){
                        latMap=7.8863847;
                        logMap=-72.5041025;}
            }
            if(no_conecto)
                Toast.makeText(getApplicationContext(),"No conecto, con el servidor ",Toast.LENGTH_LONG).show();

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            progressBar.setProgress(values[0]);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean Ping() {
        String IP = "ecotics.co";
        Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 " + IP);
            int mExitValue = mIpAddrProcess.waitFor();
            if (mExitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7
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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7
    public void cargara() {

        preferencesleer2 = getSharedPreferences("primeravez", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencesleer2.edit();
        editor.putString("sesion", "cerrada");
        editor.commit();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7
    public boolean sin_cone() {
        boolean conee=false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1000);
        }
        ubicaccion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ubicaccion != null) {
            Location loc = ubicaccion.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc == null) {
                loc = ubicaccion.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (loc == null) {
                    loc = ubicaccion.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                }


            }
            if (loc != null) {
                latMap = loc.getLatitude();
                logMap = loc.getLongitude();
                conee=true;

            }

        }
        return conee;
    }
   //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
                latMap=loc.getLatitude();
                log=String.valueOf(loc.getLongitude());
                logMap=loc.getLongitude();
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
                        no_conecto=true;


                    }
                });
            }

            else {
                    //No lo consiguio por ningun metodo
                latMap=7.8863847;
                logMap=-72.5041025;
            }
        }
        else
        {
            //El provider no esta activo... caso raro
            latMap=7.8863847;
            logMap=-72.5041025;

        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7
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
                    locales.setDescripcion(jsonArray.getJSONObject(i).getString("DESCRIPCION"));

                    listaLocales.add(locales);

                }

            } catch (Exception e) {
                e.printStackTrace();
                //       Toast.makeText(getApplicationContext(),"Problemas en la consulta",Toast.LENGTH_LONG).show();

            }
        }
        else{
            no_eco=true;
        }
      }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7

    public void cargar_perfil(){
        Fragment fragment = null;
        fragment= new PerfilFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("verificar", conexion);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.content_fragment, fragment).
                commit();

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7

    public void cargar_eco(){
        Fragment fragment = null;
        fragment= new BannerFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.content_fragment, fragment).
                commit();

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7


    public void cargar_contacto(){
        String to = "info@ecotics.co";

        String mailTo = "mailto:" + to;
        Intent emailIntent = new Intent(Intent.ACTION_VIEW);
        emailIntent.setData(Uri.parse(mailTo));
        startActivity(emailIntent);

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7
    public void cargar_terminos(){
        Uri uri = Uri.parse("http://www.ecotics.co/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7

    public void cargar_mapa(){
        Fragment fragment = null;
        fragment= new MapsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("lislocales", listaLocales);
        bundle.putBoolean("verificar",conexion);
        bundle.putDouble("latitu",latMap);
        bundle.putDouble("longi",logMap);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.content_fragment, fragment).
                commit();

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void cargar_lista(){
        Fragment fragment = null;
        fragment= new ListarFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("lislocales", listaLocales);
        bundle.putBoolean("cone",conexion);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.content_fragment, fragment).
                commit();

    }
    /////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {

    }
    ////////////////////////////////////////////////////////////////////////////////////////////
    public void log(String msg) {
        Log.d(TAG, msg);
    }
///////////////////////////////////////////////////////////////////////////////////////////
    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                log(serviceClass.getName() + " is running");
                subProcess.put(serviceClass.getName(), service.pid);
                return true;
            }
        }
        log(serviceClass.getName() + " is not running");
        return false;
    }
    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("my-event"));

    }

    @Override
    protected void onPause() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            level = intent.getIntExtra("message", 0);

        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        FragmentManager fragmentManager = getSupportFragmentManager(); //Desde Activity
        Fragment fragmento = fragmentManager.findFragmentById(R.id.content_fragment);
       if(!EstadoGPS()) {
            Toast.makeText(getApplicationContext(),"Debe tener alta presion del GPS",Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivaGPS();
                }
            },2000);
         }
        else {
           if (fragmento instanceof MapsFragment){
               fb_menu.setEnabled(false);
               fb_listar.setEnabled(false);
               fb_mapa.setEnabled(false);
               progressBar.setVisibility(View.VISIBLE);
               progressBar.setProgress(0);
               new MyAsyncTask().execute(10);
               cargar_mapa();


           }else {
               if (fragmento instanceof ListarFragment){
                   fb_menu.setEnabled(false);
                   fb_listar.setEnabled(false);
                   fb_mapa.setEnabled(false);
                   progressBar.setVisibility(View.VISIBLE);
                   progressBar.setProgress(0);
                   new MyAsyncTask().execute(10);
                   cargar_lista();}
               else{

                   fb_menu.setEnabled(false);
                   fb_listar.setEnabled(false);
                   fb_mapa.setEnabled(false);
                   progressBar.setVisibility(View.VISIBLE);
                   progressBar.setProgress(0);
                   new MyAsyncTask().execute(10);
               }


           }
       }




    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean EstadoGPS() {
        boolean valor;
        ubicar = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {

            if (!ubicar.isProviderEnabled(LocationManager.GPS_PROVIDER)|| !ubicar.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                valor = false;
            else
                valor = true;
        } catch (Exception e) {
            e.printStackTrace();
            valor = false;
        }
        return valor;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private void mostar(Context context) {
    setPendingIntent(context);
    CreateNotificacionChannel(context);
    crearNotificacion(context);
}

    private  void setPendingIntent(Context context) {
        Intent intent = new Intent(context, Main2Activity.class);
        pendingIntent= PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

    }
    private void CreateNotificacionChannel(Context context) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name="Notitificacion";
            NotificationChannel notificationChannel= new NotificationChannel(CHANNEL_ID,name, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager= (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void crearNotificacion(Context context) {
        NotificationCompat.Builder builder= new NotificationCompat.Builder(context.getApplicationContext(),CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ff_sobre);
        builder.setContentTitle("Bienvenido a EcoCargador");
        builder.setContentText("Ayudemos al ambiente");
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setContentIntent(pendingIntent);
        //  builder.setFullScreenIntent(pendingIntent, true);
        NotificationManagerCompat notificationManagerCompat =NotificationManagerCompat.from(context.getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID,builder.build());
    }
}




