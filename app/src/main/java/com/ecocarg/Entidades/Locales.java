package com.ecocarg.Entidades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

public class Locales implements Parcelable {

    private String nombre;
    private String direccion;
    private String dato;
    private Bitmap imagen_b;
    private String coordena;
    private String descripcion;
    private String modelo;
    private String alternativo;



    public Locales(String nombre, String direccion, int imagen) {
        this.nombre = nombre;
        this.direccion = direccion;

    }
    public Locales()
    {

    }

    protected Locales(Parcel in) {
        nombre = in.readString();
        direccion = in.readString();
        dato = in.readString();
        imagen_b = in.readParcelable(Bitmap.class.getClassLoader());
        coordena = in.readString();
        descripcion = in.readString();
        modelo = in.readString();
        alternativo = in.readString();
    }

    public static final Creator<Locales> CREATOR = new Creator<Locales>() {
        @Override
        public Locales createFromParcel(Parcel in) {
            return new Locales(in);
        }

        @Override
        public Locales[] newArray(int size) {
            return new Locales[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public String getDato() {
        return dato;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAlternativo() {
        return alternativo;
    }

    public void setAlternativo(String alternativo) {
        this.alternativo = alternativo;
    }

    public String getCoordena() {
        return coordena;
    }

    public void setCoordena(String coordena) {
        this.coordena = coordena;
    }

    public void setDato(String dato) {
        this.dato = dato;
        try {
            byte[] byteCode= Base64.decode(dato,Base64.DEFAULT);
            int alto=100;
            int ancho=100;
            Bitmap foto=BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
           // this.imagen_b= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
            this.imagen_b=Bitmap.createScaledBitmap(foto,alto,ancho,true);
        }catch (Exception e){
           e.printStackTrace();
        }
    }

    public Bitmap getImagen_b() {
        return imagen_b;
    }

    public void setImagen_b(Bitmap imagen_b) {
        this.imagen_b = imagen_b;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(direccion);
        dest.writeString(dato);
        dest.writeParcelable(imagen_b, flags);
        dest.writeString(coordena);
        dest.writeString(descripcion);
        dest.writeString(modelo);
        dest.writeString(alternativo);
    }
}
