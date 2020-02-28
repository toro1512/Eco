package com.ecocarg.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ecocarg.Actividades.MainActivity;
import com.ecocarg.Fragmentos.CincoFragment;
import com.ecocarg.Fragmentos.CuatroFragment;
import com.ecocarg.Fragmentos.DosFragment;
import com.ecocarg.Fragmentos.TresFragment;
import com.ecocarg.Fragmentos.UnoFragment;
import com.ecocarg.IntroActivity;
import com.ecocarg.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;


    public static Fragment newInstance(int index) {
        Fragment fragment=null;

        switch (index) {
            case 1: fragment=new UnoFragment(); break;
            case 2: fragment=new DosFragment(); break;
            case 3: fragment=new TresFragment(); break;
            case 4: fragment=new CuatroFragment(); break;
            case 5: fragment=new CincoFragment(); break;
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        Log.d("tag","crea la viats on create");
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d("tag","crea la vista");
        View root = inflater.inflate(R.layout.fragment_intro, container, false);

        return root;
    }
}