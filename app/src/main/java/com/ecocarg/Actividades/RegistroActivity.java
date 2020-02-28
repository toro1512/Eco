package com.ecocarg.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ecocarg.IntroActivity;
import com.ecocarg.R;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener{
    Integer counter=1;
    private ProgressBar progressBar;

    EditText pasw_g,usu_g,email_g;
    Button btn_reg;
    Button tex_salir,iniciar_s;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    boolean acepto,conexion;


    SharedPreferences preferences;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        acepto=false;
        /////////////////////////////////////////////////////////
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(10);
        counter = 1;
        ///////////////////////////////////////////////7


        pasw_g=findViewById(R.id.edit_pas);
        iniciar_s=findViewById(R.id.text_inicio);
        usu_g=findViewById(R.id.edit_usu);
        email_g=findViewById(R.id.edit_ema);
        btn_reg=findViewById(R.id.btn_reg);
        tex_salir=findViewById(R.id.text_salir);

        request=Volley.newRequestQueue(this);
        permiso();

        tex_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
                finish();
            }
        });
        iniciar_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentmain= new Intent(RegistroActivity.this,LoginActivity.class);
                startActivity(intentmain);

            }
        });

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validarEmail(email_g.getText().toString()))
                {
                     if(acepto){
                         if(usu_g.getText().toString().isEmpty())
                         {
                             Toast.makeText(getApplicationContext(),"Debe llenar usuario",Toast.LENGTH_LONG).show();
                         }
                       else{

                             if(pasw_g.getText().toString().isEmpty())
                             {
                                 Toast.makeText(getApplicationContext(),"Debe llenar el password",Toast.LENGTH_LONG).show();
                             }
                            else
                                {
                                    btn_reg.setEnabled(false);
                                    tex_salir.setEnabled(false);
                                    iniciar_s.setEnabled(false);
                                    progressBar.setVisibility(View.VISIBLE);
                                    progressBar.setProgress(0);
                                    new MyAsyncTaskR().execute(10);

                                }
                       }

                   }
                   else
                   {
                     permiso();
                   }

                }
                else
                {
                    email_g.setError("Email no valido");
                    email_g.setText("");

                }

            }
        });

    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    private void Cargar_WS() {

        String url = "http://ecotics.co/ws_registrar.php?usu="+usu_g.getText().toString()+
                "&password="+pasw_g.getText().toString()+"&email="+email_g.getText().toString();
        url.replace(" ","%20");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);


    }


    @Override
    public void onResponse(JSONObject response) {
        String respuesta=response.toString();
        respuesta=respuesta.substring(24,35);
        if(respuesta.equals("NO REGISTRA")){
            Toast.makeText(this,"El usuario ya existe",Toast.LENGTH_LONG).show();


        }
        else
        {
            if(respuesta.equals("SI REGISTRA")){
                Toast.makeText(this,"BIENVENIDO",Toast.LENGTH_LONG).show();
                preferences = getSharedPreferences("primeravez", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("registrado", "verdad");
                editor.putString("sesion","abierta");
                editor.putString("email", email_g.getText().toString());
                editor.putString("clave", pasw_g.getText().toString());
                editor.putString("sexo","masculino");
                editor.putString("nombre","");
                editor.putString("fecha","null");
                editor.putString("notificaciones","si");
                editor.commit();
                Intent intentmain= new Intent(RegistroActivity.this,IntroActivity.class);
                startActivity(intentmain);
                finish();

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Tenemos problemas con el servidor.. intenta en unos minutos",Toast.LENGTH_LONG).show();

            }
        }


    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"no se pudo registrar"+error.toString(),Toast.LENGTH_LONG).show();

    }
    public void permiso(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
            },REQUEST_CODE_ASK_PERMISSIONS);

        }
        else
            acepto=true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

          switch (requestCode){
            case REQUEST_CODE_ASK_PERMISSIONS:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    acepto=true;
                }else {
                    acepto=false;
                    Toast.makeText(getApplicationContext(),"Disculpe debe permitir usar su Ubicacion para el funcionamiento de la aplicacion",Toast.LENGTH_LONG).show();
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
  class MyAsyncTaskR extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            conexion=Ping();
            publishProgress(counter);
            return "Tarea completa!. =)";
        }
        @Override
        protected void onPostExecute(String result) {

            btn_reg.setEnabled(true);
            tex_salir.setEnabled(true);
            iniciar_s.setEnabled(true);
            progressBar.setVisibility(View.GONE);
            if(conexion)
            Cargar_WS();
            else
                nopudo();


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
