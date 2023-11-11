package com.example.bta;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddBooking#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBooking extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner buses,routes,time;
    View view;
    private String[] bus;
    private String[] route;
    private String[] times;
    public AddBooking() {
        // Required empty public constructor
    }


    public static AddBooking newInstance(String param1, String param2) {
        AddBooking fragment = new AddBooking();
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
        view = inflater.inflate(R.layout.fragment_add_booking, container, false);
        buses = (Spinner) view.findViewById(R.id.buses);
        routes = (Spinner) view.findViewById(R.id.route);
        time = (Spinner) view.findViewById(R.id.time);
       bus = Helper.getBuses("Choose a Category.");
       ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,bus); //this, android.R.layout.simple_spinner_item, categoryOptions);

        buses.setAdapter(adapter);

        route = Helper.getRoutes("Choose a Category.");
        ArrayAdapter adapter2 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,route); //this, android.R.layout.simple_spinner_item, categoryOptions);

        routes.setAdapter(adapter);

        times = Helper.getTime("Choose a Category.");
        ArrayAdapter adapter3 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,times); //this, android.R.layout.simple_spinner_item, categoryOptions);

        time.setAdapter(adapter);
        return inflater.inflate(R.layout.fragment_add_booking, container, false);
    }

}