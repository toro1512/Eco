package com.ecocarg.Fragmentos;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.ecocarg.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

import java.io.IOException;
import java.util.Calendar;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment  {



    View view;
     Spinner sexo;
    TextView correo_pre,ter_y_con;
    Switch permiso;
    EditText nombre;
    Button guardar;
    private int Year,Mes,Dia;
    private AsyncHttpClient client,client_dos;
    TextView fecha;
    SharedPreferences preferences,preferencesleer;
    Location location;
    boolean puede;



    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        puede= getArguments().getBoolean("verificar");
        view=inflater.inflate(R.layout.fragment_perfil, container, false);

        client=new AsyncHttpClient();
        client_dos=new AsyncHttpClient();
        sexo= view.findViewById(R.id.spinner);
        correo_pre=view.findViewById(R.id.ema_per);
        ter_y_con= view.findViewById(R.id.ter_con_per);
        fecha=view.findViewById(R.id.fec_per);
        guardar=view.findViewById(R.id.gur_per);
        nombre=view.findViewById(R.id.nom_per);
        permiso=view.findViewById(R.id.switch1_per);
        preferencesleer=this.getActivity().getSharedPreferences("primeravez",MODE_PRIVATE);
        correo_pre.setText(preferencesleer.getString("email","Sin correo"));
        nombre.setText(preferencesleer.getString("nombre",""));
        if(!preferencesleer.getString("fecha","").equals("null"))
        {
          fecha.setText(preferencesleer.getString("fecha",""));
        }
        if(preferencesleer.getString("notificaciones","Sin seleccion").equals("si"))
        {permiso.setChecked(true);}
        else{permiso.setChecked(false);}
        if(preferencesleer.getString("sexo","Sin seleccion").equals("Masculino")) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.masculino, android.R.layout.simple_spinner_item);
            sexo.setAdapter(adapter);
            sexo.setSelection(0);
        }
        else{
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.masculino, android.R.layout.simple_spinner_item);
            sexo.setAdapter(adapter);
            sexo.setSelection(1);
        }
        if(puede)
        {
            client_dos.get("http://ecotics.co/ws_datos.php?email=" + preferencesleer.getString("email", "Sin correo"), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                          llenar_campos(new String(responseBody));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }

        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  Calendar C= Calendar.getInstance();
                Mes = C.get(Calendar.MONTH);
                Dia = C.get(Calendar.DAY_OF_MONTH);
                Year= C.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fecha.setText(dayOfMonth + "-" + (month +1) +"-" + year);
                    }
                },Year,Mes,Dia);
                datePickerDialog.show();
            }
        });
        ter_y_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.ecotics.co/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Ping()){
                  cargar_ws();
                }
                else
                {
                    Toast.makeText(getActivity(),"Lo sentimos no tiene acceso a internet, intente mas tarde",Toast.LENGTH_LONG).show();

                }
            }
        });

        return view;
    }
    public void cargar_ws() {
        String sex;
        if (sexo.getSelectedItem().toString().equals("Masculino"))
            sex = "M";
        else
            sex = "F";

        String url = "http://ecotics.co/ws_actualizar.php?nombre=" + nombre.getText().toString() + "&sexo=" + sex +
                "&fecha=" + fecha.getText().toString() + "&email=" + correo_pre.getText().toString();
        url.replace(" ", "%20");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {

                    String x = new String(responseBody);
                    x=x.substring(3);
                    if (x.equalsIgnoreCase("SI")) {
                         try {
                             Toast.makeText(getActivity(),"Cambios realizados", Toast.LENGTH_LONG).show();
                         }
                        catch ( Exception e){
                            e.printStackTrace();
                        }
                        cargar_pre();

                    }
                    else{
                        try {
                            Toast.makeText(getActivity(),"Cambios no realizados, no afecto ningun valor", Toast.LENGTH_LONG).show();
                        }
                        catch ( Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                else{
                    try {
                        Toast.makeText(getActivity(),"codigo erroneo", Toast.LENGTH_LONG).show();
                    }
                    catch ( Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                try {
                    Toast.makeText(getActivity(), "Fallo conexion con el servidor, intente mas tarde", Toast.LENGTH_LONG).show();
                }
                catch ( Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
    public void cargar_pre(){
        String valor;
        boolean a =permiso.isChecked();
        if(a){valor ="si";}
        else{valor="no";}
        preferences=this.getActivity().getSharedPreferences("primeravez",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("notificaciones",valor);
        editor.putString("nombre",nombre.getText().toString());
        editor.putString("fecha",fecha.getText().toString());
        editor.putString("sexo",sexo.getSelectedItem().toString());
        editor.commit();


    }

public void llenar_campos(String response){
    if(response.length()>3) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {

                if(jsonArray.getJSONObject(i).getString("FECHA_NAC").equals("0000-00-00"))
                    fecha.setText("DD-MM-YYYY");
                 else
                  fecha.setText(jsonArray.getJSONObject(i).getString("FECHA_NAC"));
                if(jsonArray.getJSONObject(i).getString("SEXO").equals("M")) {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                            R.array.masculino, android.R.layout.simple_spinner_item);
                    sexo.setAdapter(adapter);
                    sexo.setSelection(0);
                }
                else{
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                            R.array.masculino, android.R.layout.simple_spinner_item);
                    sexo.setAdapter(adapter);
                    sexo.setSelection(1);
                }
                if(jsonArray.getJSONObject(i).getString("NOMBRE").equals("0"))
                    fecha.setText("Nombre");
                else
                    nombre.setText(jsonArray.getJSONObject(i).getString("NOMBRE"));
            }

        } catch (Exception e) {
            e.printStackTrace();
             }
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

}
