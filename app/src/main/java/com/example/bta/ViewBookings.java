package com.example.bta;

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
import android.widget.Button;
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

public class ViewBookings extends Fragment {
    //I chose to use an unfilteredlist to base filters off of. This way, the database is only called when something is changed in the database.
    //Otherwise, every time the filter is changed, we would have to get the items from the database again.
    private ArrayList<Vehicle> unfilteredList = new ArrayList<>();
    private ArrayList<Vehicle> listingsList = new ArrayList<>();
    private ListItemAdapter4 mAdapter;
    private boolean isViewFiltered;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference itemsRef = rootRef.child("BTABookings");
    private ProgressBar mProgressBar;
    TextView tv;
    User userF=null;
    Button bt;
    public ViewBookings() {
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
      //  menuInflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_bookings, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mProgressBar = view.findViewById(R.id.myDataLoaderProgressBar);
        tv=view.findViewById(R.id.textView);
        mProgressBar.setVisibility(View.VISIBLE);
        bt=view.findViewById(R.id.su);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        //recyclerView.setNestedScrollingEnabled(false);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
         mLayoutManager.setReverseLayout(true);
        // mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        DatabaseReference myRootRef = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth authRef = FirebaseAuth.getInstance();
        FirebaseUser user = authRef.getCurrentUser();
        String UID = user.getUid();
        DatabaseReference myUser = myRootRef.child("BTAUsers").child(UID);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddBooking();
            }

            ;
        });
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


        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            //OnDataChange gets the full database every time something is changed inside of it.
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear the listingslist so we can add the items again (with changes)
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    unfilteredList.clear();
                    listingsList.clear();
                  //  listingsList.removeAll(listingsList);
                    //itemsMap is a map of every item in the 'Items' database
                    Map<String, Object> itemsMap = (HashMap<String, Object>) dataSnapshot.getValue();

                    for (String key : itemsMap.keySet()) {

                        Object itemMap = itemsMap.get(key);

                        if (itemMap instanceof Map && itemMap != null) {
                            Map<String, Object> itemObj = (Map<String, Object>) itemMap;
                            String id = (String) itemObj.get("id");
                            String name = (String) itemObj.get("name");
                            String route = (String) itemObj.get("route");
                            String departure = (String) itemObj.get("departure");
                            String fare = (String) itemObj.get("fare");
                            String seats = (String) itemObj.get("seats");
                            String rseats = (String) itemObj.get("rseats");
                            Vehicle item = new Vehicle(id, name, route, departure, fare, seats, rseats);

                            unfilteredList.add(item);


                        }
                    }
                    for(int i=0;i<unfilteredList.size();i++)
                    {
                        if(unfilteredList.get(i) != null && unfilteredList.get(i).getName() != null)
                        {
                            listingsList.add(unfilteredList.get(i));
                        }
                    }
    mAdapter = new ListItemAdapter4(listingsList);
    recyclerView.setAdapter(mAdapter);
    recyclerView.setLayoutManager(mLayoutManager);
    tv.setText("Available Booking Records");
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
    public void AddBooking()
    {
        Intent intent=new Intent(getContext(),AddBookingActivity.class);
                getContext().startActivity(intent);

    }




}
