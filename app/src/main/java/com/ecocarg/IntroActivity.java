package com.ecocarg;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.ecocarg.Actividades.Main2Activity;
import com.ecocarg.Actividades.MainActivity;
import com.ecocarg.Fragmentos.CincoFragment;
import com.ecocarg.Fragmentos.CuatroFragment;
import com.ecocarg.Fragmentos.DosFragment;
import com.ecocarg.Fragmentos.TresFragment;
import com.ecocarg.Fragmentos.UnoFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecocarg.ui.main.SectionsPagerAdapter;

public class IntroActivity extends AppCompatActivity implements UnoFragment.OnFragmentInteractionListener, DosFragment.OnFragmentInteractionListener,
        TresFragment.OnFragmentInteractionListener, CuatroFragment.OnFragmentInteractionListener, CincoFragment.OnFragmentInteractionListener
{

    private LinearLayout linearPunto;
    private TextView[] puntosSlide;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        linearPunto= findViewById(R.id.LinearPtos);
        agregarIndicadoresPts(0);
        viewPager.addOnPageChangeListener(viewListener);

    }

    private void agregarIndicadoresPts(int pos) {

        puntosSlide = new TextView[4];
        linearPunto.removeAllViews();
        for(int i=0; i<puntosSlide.length;i++)
        {

            puntosSlide[i]=new TextView(this);
            puntosSlide[i].setText(Html.fromHtml("&#8226;"));
            puntosSlide[i].setTextSize(35);
            puntosSlide[i].setTextColor(getResources().getColor(R.color.colorBlancoTraparente));
            linearPunto.addView(puntosSlide[i]);

        }
        if(puntosSlide.length>0)
            if(pos<4)
            puntosSlide[pos].setTextColor(getResources().getColor(R.color.colorBlanco));
    }


   ViewPager.OnPageChangeListener viewListener= new ViewPager.OnPageChangeListener() {
       @Override
       public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

       @Override
       public void onPageSelected(int position) {
           agregarIndicadoresPts(position);
           if(position==4)
           {
               Intent intent= new Intent(IntroActivity.this, Main2Activity.class);
               startActivity(intent);
               finish();
           }
               }

       @Override
       public void onPageScrollStateChanged(int state) {
          }
   };
    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}