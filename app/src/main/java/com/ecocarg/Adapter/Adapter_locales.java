package com.ecocarg.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecocarg.Actividades.MainActivity;
import com.ecocarg.Dialog;
import com.ecocarg.Entidades.Locales;
import com.ecocarg.R;

import java.util.ArrayList;

public class Adapter_locales extends RecyclerView.Adapter<Adapter_locales.ViewHolderDatos>{
    ArrayList<Locales> listadelocales;
    int indice=0;

    public Adapter_locales(ArrayList<Locales> listadelocales) {
        this.listadelocales = listadelocales;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_locales,null,false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(parent.getContext(),listadelocales.get(parent.indexOfChild(v)+1).getNombre().toString(),Toast.LENGTH_LONG).show();
                indice=parent.indexOfChild(v)+1;
                PromptDialogFragment dialog = new PromptDialogFragment();
                dialog.setCancelable(true);
                dialog.mostrar(dialog);


            }
        });
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {

        holder.nombre.setText(listadelocales.get(position).getNombre().toString());
        holder.direccion.setText(listadelocales.get(position).getDireccion().toString());

      /*  if (listadelocales.get(position).getImagen()!=null)
             holder.imageView.setImageBitmap(listadelocales.get(position).getImagen());
        else
            holder.imageView.setImageResource(R.drawable.common_google_signin_btn_text_light_normal);*/
    }

    @Override
    public int getItemCount() {
        return listadelocales.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView nombre,direccion;
        ImageView imageView;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombre=itemView.findViewById(R.id.nombre_local);
            direccion=itemView.findViewById(R.id.direccion_local);
            imageView= itemView.findViewById(R.id.imagen_local);
        }
    }
    public static class PromptDialogFragment extends DialogFragment implements View.OnClickListener{
        ImageView imageView;
        Button btn_can,btn_ir;
        TextView text;
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_dialog,container,false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            imageView= view.findViewById(R.id.Imagen_Diag);
            btn_can=view.findViewById(R.id.btn_diag_can);
            btn_ir=view.findViewById(R.id.btn_diag_ir);
            text=view.findViewById(R.id.Text_Diag);

            btn_ir.setOnClickListener(this);
            btn_can.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


        }
        public void mostrar(PromptDialogFragment xss)
        {
            xss.show(getFragmentManager(),"tag");
        }
    }

}