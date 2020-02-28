package com.ecocarg.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ecocarg.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cz.msebera.android.httpclient.Header;

public class PedirCorreo extends AppCompatActivity {
    Integer counter=1;
    private ProgressBar progressBar;
    Button enviar,registrar;
    boolean conexion;
    EditText email_g;
    private AsyncHttpClient client;
    String pasword_llega;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedir_correo);
        enviar=findViewById(R.id.btn_envia);
        registrar=findViewById(R.id.btn_registrar_p);
        email_g=findViewById(R.id.email_pe);
        client= new AsyncHttpClient();
        /////////////////////////////////////////////////////////
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(10);
        counter = 1;
        ///////////////////////////////////////////////7


        DisplayMetrics medidaventana=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(medidaventana);
        int alto=medidaventana.heightPixels;
        int ancho=medidaventana.widthPixels;
        getWindow().setLayout((int)(ancho*0.80),(int)(alto*0.60));
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email_g.getText().toString().isEmpty()) {
                   if (validarEmail(email_g.getText().toString())) {
                       enviar.setEnabled(false);
                       registrar.setEnabled(false);
                       progressBar.setVisibility(View.VISIBLE);
                       progressBar.setProgress(0);
                       new MyAsyncTaskP().execute(10);

                    }
                    else {
                        email_g.setError("Email no valido ");
                        email_g.setText("");
                       }
                 }
                else
                   {
                    Toast.makeText(getApplicationContext(),"Ingrese su correo",Toast.LENGTH_LONG).show();
                   }

            }
        });
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(PedirCorreo.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

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
                Toast.makeText(getApplicationContext(),"No se comunico con el Servidor",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void Respuesta_web(String response) {
        if(response.equals("2XXXXXXX2Q"))
        {
            Toast.makeText(getApplicationContext(),"Correo no registrado, por favor registrese",Toast.LENGTH_LONG).show();
        }
        else
        {
           pasword_llega=response;
           enviar_correo();

        }

    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    private void enviar_correo() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "ecotics.co");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("info@ecotics.co", "3c0t1cs2019");
            }
        });

        SenderAsyncTask task = new SenderAsyncTask(session, "info@ecotics.co", email_g.getText().toString(), "Tu solicitud de recuperación de contraseña","Estimado(a) usuario: ante todo reciba un coordial saludo. Ha realizado una solicitud de recuperación de contraseña a través de la aplicación EcoTICS, su contraseña es: "+pasword_llega);
        task.execute();
    }
    private class SenderAsyncTask extends AsyncTask<String, String, String> {

        private String from, to, subject, message;
        private Session session;

        public SenderAsyncTask(Session session, String from, String to, String subject, String message) {
            this.session = session;
            this.from = from;
            this.to = to;
            this.subject = subject;
            this.message = message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Message mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress(from));
                mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                mimeMessage.setSubject(subject);
                mimeMessage.setContent(message, "text/html; charset=utf-8");
                Transport.send(mimeMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Correo enviado", Toast.LENGTH_LONG).show();
            Intent intent= new Intent(PedirCorreo.this, LoginActivity.class);
            startActivity(intent);

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
   class MyAsyncTaskP extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            conexion=Ping();
            publishProgress(counter);
            return "Tarea completa!. =)";
        }
        @Override
        protected void onPostExecute(String result) {

           enviar.setEnabled(true);
            registrar.setEnabled(true);


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
