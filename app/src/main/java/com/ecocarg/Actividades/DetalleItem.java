package com.ecocarg.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecocarg.Entidades.Locales;
import com.ecocarg.R;

public class DetalleItem extends AppCompatActivity {

    Locales Item;
    TextView titulo,descripcion,volver,eco,valor_des;
    ImageView imgFoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_item);


        titulo= findViewById(R.id.item_nombre);
        descripcion= findViewById(R.id.item_ubicacion);
        volver=findViewById(R.id.item_volver);
        eco=findViewById(R.id.item_info);
        imgFoto=findViewById(R.id.imgFoto);
        valor_des=findViewById(R.id.val_descrip);
        Item=(Locales) getIntent().getParcelableExtra("objetoData");
        titulo.setText(Item.getNombre());
        eco.setText(Item.getModelo());
        valor_des.setText(Item.getDescripcion());
        descripcion.setText(Item.getDireccion());
        imgFoto.setImageBitmap(Item.getImagen_b());
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        }
}
