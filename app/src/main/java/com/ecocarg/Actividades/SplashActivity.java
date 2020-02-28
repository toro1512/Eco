package com.ecocarg.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ecocarg.R;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "valor de la var";
    SharedPreferences preferencessplash;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    LocationManager ubicar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preferencessplash=getSharedPreferences("primeravez", MODE_PRIVATE);
        permiso();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!EstadoGPS()) {
            Toast.makeText(getApplicationContext(),"Debe tener alta presion del GPS",Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivaGPS();
                }
            },2000);
        }
        else
            llamando();
    }

    private String Entro(){return preferencessplash.getString("registrado","");}
    private boolean Sesion() {

        String comparar="abierta";
        String valor =preferencessplash.getString("sesion", "no hay");
        return valor.equals(comparar);



    }

    public void permiso(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
            },REQUEST_CODE_ASK_PERMISSIONS);

        }
        else{
            slpppp();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case REQUEST_CODE_ASK_PERMISSIONS:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    slpppp();
                }else {
                    Toast.makeText(getApplicationContext(),"Disculpe debe permitir usar su ubicación para el funcionamiento de la aplicación",Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            permiso();
                           }
                    },2000);

                   }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public void slpppp(){
        if(!EstadoGPS())
            ActivaGPS();
        else
            llamando();

    }
    public void llamando(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                final Intent intentmain= new Intent(SplashActivity.this,Main2Activity.class);
                final Intent intentregis= new Intent(SplashActivity.this,RegistroActivity.class);
                final Intent intentlogin=new Intent(SplashActivity.this,LoginActivity.class);
                if(!TextUtils.isEmpty(Entro()))
                {

                    if(Sesion()) {
                        startActivity(intentmain);
                    }else{
                        startActivity(intentlogin);}
                }
                else
                {
                    startActivity(intentregis);
                }
                finish();

            }
        },3000);
    }
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
}
