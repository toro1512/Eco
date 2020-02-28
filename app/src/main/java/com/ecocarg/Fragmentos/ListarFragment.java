package com.ecocarg.Fragmentos;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.ecocarg.Actividades.DetalleItem;
import com.ecocarg.Adapter.Adapter_local;
import com.ecocarg.Entidades.Locales;
import com.ecocarg.R;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View view;
    private ListView lvItem;
    boolean coneee;
    private Adapter_local adaptador;
    ArrayList<Locales> listaLocales,listaLocales_dos;




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ListarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListarFragment newInstance(String param1, String param2) {
        ListarFragment fragment = new ListarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listaLocales_dos=new ArrayList<Locales>();
        // Inflate the layout for this fragment
        try {
            listaLocales = (ArrayList<Locales>) getArguments().getSerializable("lislocales");
            coneee=getArguments().getBoolean("cone");

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        view = inflater.inflate(R.layout.fragment_listar, container, false);
        lvItem= (ListView) view.findViewById(R.id.lvItems);

        if(listaLocales.size()==0)
        {
            Toast.makeText(getContext(),"Estas trabajando modo offline",Toast.LENGTH_LONG).show();
            }
        else
        {
        adaptador=new Adapter_local(getContext(),listaLocales);
        lvItem.setAdapter(adaptador);
        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent= new Intent(getActivity(),DetalleItem.class);
                intent.putExtra("objetoData",listaLocales.get(position));
                startActivity(intent);


            }
        });}
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

