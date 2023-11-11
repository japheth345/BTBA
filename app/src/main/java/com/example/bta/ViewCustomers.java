package com.example.bta;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewCustomers extends Fragment {
    //I chose to use an unfilteredlist to base filters off of. This way, the database is only called when something is changed in the database.
    //Otherwise, every time the filter is changed, we would have to get the items from the database again.
    ArrayList<User> data=new ArrayList<>();
    ArrayList<User> data2=new ArrayList<>();
    private ListItemAdapter6 mAdapter;
    private boolean isViewFiltered;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference itemsRef = rootRef.child("BTABookings");
    private ProgressBar mProgressBar;
    TextView tv;
    User userF=null;
    public ViewCustomers() {
        // Required empty public constructor
    }

    public static UserBookingFragment newInstance() {
        return new UserBookingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Toast.makeText(get, "USER NAME \t"+user.getFirstName(), Toast.LENGTH_LONG).show();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_customers, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mProgressBar = view.findViewById(R.id.myDataLoaderProgressBar);
        tv=view.findViewById(R.id.textView);
        mProgressBar.setVisibility(View.VISIBLE);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new ListItemAdapter6(data);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(false);
        mLayoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
       // recyclerView.setHasFixedSize(true);
        DatabaseReference myRootRef = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth authRef = FirebaseAuth.getInstance();
        FirebaseUser user = authRef.getCurrentUser();
        String UID = user.getUid();
        DatabaseReference myUser = myRootRef.child("BTAUsers").child(UID);
        myUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String firstName = dataSnapshot.child("firstName").getValue(String.class);
                String lastName = dataSnapshot.child("lastName").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String password = dataSnapshot.child("password").getValue(String.class);
                String role = dataSnapshot.child("role").getValue(String.class);
                userF = new User(firstName, lastName, email, password, role);

                Helper.setUser(userF);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                userF = new User("The", "Driver", "No Email", "", "Driver");
                Helper.setUser(userF);
            }
        });

        DatabaseReference itemsRef = rootRef.child("BTAUsers");
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            //OnDataChange gets the full database every time something is changed inside of it.
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear the listingslist so we can add the items again (with changes)
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {

                    for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                        User user=Snapshot.getValue(User.class);

                        data.add(Snapshot.getValue(User.class));


                    }

                    data=data;


                    mAdapter.notifyDataSetChanged();
                    tv.setText("Registered Records");
                    mProgressBar.setVisibility(View.INVISIBLE);
                }   }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        return view;
    }



    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



}
