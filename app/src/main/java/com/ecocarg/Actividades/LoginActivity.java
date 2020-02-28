package com.ecocarg.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import com.ecocarg.IntroActivity;
import com.ecocarg.R;
import com.loopj.android.http.*;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    Integer counter=1;
    private ProgressBar progressBar;
    EditText pasw_g,email_g;
    Button btn_ini,btn_vol,btn_contr;
    SharedPreferences preferences,preferencesleer;
    private AsyncHttpClient client;
    String pasword_llega;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    boolean acepto;
    boolean conexion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences=getSharedPreferences("primeravez", MODE_PRIVATE);
        /////////////////////////////////////////////////////////
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(10);
        counter = 1;
        acepto=false;
        ///////////////////////////////////////////////7


        client=new AsyncHttpClient();
        permiso();
        btn_ini=findViewById(R.id.btn_iniciar);
        btn_vol=findViewById(R.id.btn_irregistro);
        btn_contr=findViewById(R.id.btn_contraseña);
        pasw_g=findViewById(R.id.edit_pas);
        email_g=findViewById(R.id.edit_ema);
        btn_ini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           if(acepto) {
               if (validarEmail(email_g.getText().toString())) {
                   if (pasw_g.getText().toString().isEmpty()) {
                       Toast.makeText(getApplicationContext(), "Debe llenar el password", Toast.LENGTH_LONG).show();
                   } else {
                       progressBar.setVisibility(View.VISIBLE);
                       progressBar.setProgress(0);
                       btn_ini.setEnabled(false);
                       btn_vol.setEnabled(false);
                       btn_contr.setEnabled(false);
                       new MyAsyncTaskL().execute(10);

                   }

               } else {
                   email_g.setError("Email no valido");
                   email_g.setText("");

               }
           }
           else{
               permiso();
           }
        }
        });
        btn_contr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this, PedirCorreo.class);
                startActivity(intent);
               }
        });
        btn_vol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);

            }
        });
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    public void Cargar_WS(String valor)
    {
        String url = "http://ecotics.co/ws_inicio.php?email="+valor;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200){
                 Respuesta_web(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(),"No se comunico con el servidor",Toast.LENGTH_LONG).show();
            }
        });

    }
    public void Respuesta_web(String response){

        if(response.equals("2XXXXXXX2Q"))
        {
            Toast.makeText(getApplicationContext(),"Correo no registrado",Toast.LENGTH_LONG).show();
        }
        else
        {
            pasword_llega=response;
            if (pasword_llega.equals(pasw_g.getText().toString())){

                Intent intentmain= new Intent(LoginActivity.this,Main2Activity.class);
                Intent intentintro= new Intent(LoginActivity.this,IntroActivity.class);

                if(Entro())
                {
                    car_prefencias();
                    startActivity(intentmain);
                }
                else
                {
                    car_prefencias();
                    startActivity(intentintro);
                }
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Contraseña invalida",Toast.LENGTH_LONG).show();
            }
        }
    }
    public void car_prefencias(){
        preferencesleer = getSharedPreferences("primeravez", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencesleer.edit();
        editor.putString("registrado", "verdad");
        editor.putString("email", email_g.getText().toString());
        editor.putString("clave", pasw_g.getText().toString());
        editor.putString("sexo","masculino");
        editor.putString("nombre","");
        editor.putString("fecha","null");
        editor.putString("sesion","abierta");
        editor.putString("notificaciones","si");
        editor.apply();

    }
    private boolean Entro() {

        String comparar="verdad";
        String valor =preferences.getString("registrado", "no hay");
        return valor.equals(comparar);


    }

    public void permiso(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
            },REQUEST_CODE_ASK_PERMISSIONS);

        }
        else{
            acepto=true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case REQUEST_CODE_ASK_PERMISSIONS:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    acepto=true;
                }else {
                    acepto=false;
                    Toast.makeText(getApplicationContext(),"Disculpe debe permitir usar su ubicacion para el funcionamiento de la aplicacion",Toast.LENGTH_LONG).show();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
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
    class MyAsyncTaskL extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            conexion=Ping();
            publishProgress(counter);
            return "Tarea completa!. =)";
        }
        @Override
        protected void onPostExecute(String result) {

            btn_ini.setEnabled(true);
            btn_vol.setEnabled(true);
            btn_contr.setEnabled(true);
            progressBar.setVisibility(View.GONE);
            if(conexion)
                Cargar_WS(email_g.getText().toString());
            else
            {
                nopudo();
            }

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
    public void nopudo(){
        Toast.makeText(getApplicationContext(),"Sin conexion a internet. Intente más tarde",Toast.LENGTH_LONG).show();
    }

}
