package com.example.bta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class BookingHistoryActivity extends AppCompatActivity {
    private ArrayList<User> unfilteredList = new ArrayList<>();
    private ArrayList<User> listingsList = new ArrayList<>();

    ArrayList<User> data=new ArrayList<>();
    ArrayList<User> data2=new ArrayList<>();
    private ListItemAdapter5 mAdapter;
    private boolean isViewFiltered;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference itemsRef ;
    private ProgressBar mProgressBar;
    TextView tv;
    int n;
    String veh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        mProgressBar =findViewById(R.id.myDataLoaderProgressBar);
        tv=findViewById(R.id.textView);
        Vehicle vehicle=Helper.getVehicle();
        n=(Integer.parseInt(vehicle.getSeats()))+1;
        veh=vehicle.getName();
        mProgressBar.setVisibility(View.VISIBLE);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        itemsRef = rootRef.child("BTABookings").child("DETAILS").child(veh);
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
                    //Toast.makeText(getApplicationContext(), "SEATS 11\t \t"+data.size()+"\n "+data.get(0).getFirstName(), Toast.LENGTH_LONG).show();

                    for(int i=0;i<n ;i++)
                    {
                        User user2=null;
                        data2.add(user2);
                    }
                    for(int i=0;i<data.size();i++)
                    {
                        User user2=data.get(i);
                        data2.set(Integer.parseInt(user2.getSeat()),user2);
                    }
                    data=data2;
                    //Toast.makeText(getApplicationContext(), "SEATS \t \t"+data.size()+"\n "+data.get(0).getFirstName(), Toast.LENGTH_LONG).show();
                    mAdapter = new ListItemAdapter5(data);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    mLayoutManager.setReverseLayout(true);
                    mLayoutManager.setStackFromEnd(true);

                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                    mProgressBar.setVisibility(View.INVISIBLE);


                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Data not Exist", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.addbookingmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {

            Intent mainIntent = new Intent(this, AdminDashboardActivity.class);
            this.startActivity(mainIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}