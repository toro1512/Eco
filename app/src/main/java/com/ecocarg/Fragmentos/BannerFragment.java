package com.ecocarg.Fragmentos;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecocarg.Actividades.MainActivity;
import com.ecocarg.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BannerFragment extends Fragment implements UnoFragment.OnFragmentInteractionListener, DosFragment.OnFragmentInteractionListener,
        TresFragment.OnFragmentInteractionListener, CuatroFragment.OnFragmentInteractionListener {

    private ViewPager viewPager;
    private View view;
    Activity actividad;

    private LinearLayout linearPunto;
    private TextView[] puntosSlide;


    public BannerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_banner, container, false);

        viewPager = view.findViewById(R.id.pager);
        linearPunto = view.findViewById(R.id.LinearPtos);
        agregarIndicadoresPts(0);
        viewPager.addOnPageChangeListener(viewListener);


        AdapterViewPager pagerAdapter = new AdapterViewPager(getFragmentManager(), 0);
        viewPager.setAdapter(pagerAdapter);

        return view;
    }

    private void agregarIndicadoresPts(int pos) {

        puntosSlide = new TextView[4];
        linearPunto.removeAllViews();
        for (int i = 0; i < puntosSlide.length; i++) {

            puntosSlide[i] = new TextView(actividad);
            puntosSlide[i].setText(Html.fromHtml("&#8226;"));
            puntosSlide[i].setTextSize(35);
            puntosSlide[i].setTextColor(getResources().getColor(R.color.colorBlancoTraparente));
            linearPunto.addView(puntosSlide[i]);

        }
        if (puntosSlide.length > 0)
            puntosSlide[pos].setTextColor(getResources().getColor(R.color.colorBlanco));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            actividad = (Activity) context;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            agregarIndicadoresPts(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public class AdapterViewPager extends FragmentStatePagerAdapter {

        public AdapterViewPager(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new UnoFragment();
                case 1:
                    return new DosFragment();
                case 2:
                    return new TresFragment();
                case 3:
                    return new CuatroFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}