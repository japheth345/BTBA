package com.example.bta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class UserBookActivity extends AppCompatActivity {
    private ArrayList<User> unfilteredList = new ArrayList<>();
    private ArrayList<User> listingsList = new ArrayList<>();

    ArrayList<User> data=new ArrayList<>();
    ArrayList<User> data2=new ArrayList<>();
    private ListItemAdapter2 mAdapter;
    private boolean isViewFiltered;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference itemsRef ;
    private ProgressBar mProgressBar;
    TextView tv;
    int n=0;
    String veh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        mProgressBar =findViewById(R.id.myDataLoaderProgressBar);
        tv=findViewById(R.id.textView);
        Vehicle vehicle=Helper.getVehicle();
        if(vehicle==null)
        {
            Intent mainIntent = new Intent(this, CustomerDashboardActivity.class);
            this.startActivity(mainIntent);
        }
        else {
            n = (Integer.parseInt(vehicle.getSeats())) + 1;
            veh = vehicle.getName();
            if(vehicle==null || veh.equals(null) || veh.isEmpty() || veh.length() <=0 || n <=0)
            {
                Intent mainIntent = new Intent(this, CustomerDashboardActivity.class);
                this.startActivity(mainIntent);
            }
        }
        mProgressBar.setVisibility(View.VISIBLE);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        itemsRef = rootRef.child("BTABookings").child("DETAILS").child(veh);
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            //OnDataChange gets the full database every time something is changed inside of it.
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(int i=0;i<n ;i++)
                {
                    User user2=null;
                    data.add(user2);
                }

                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {

                    for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    String fn=Snapshot.child("firstName").getValue(String.class);
                    String ln=Snapshot.child("lastName").getValue(String.class);
                    String em=Snapshot.child("email").getValue(String.class);
                    String se=Snapshot.child("seat").getValue(String.class);
                    String ve=Snapshot.child("vehicle").getValue(String.class);
                    String de=Snapshot.child("depa").getValue(String.class);
                    String ro=Snapshot.child("route").getValue(String.class);

                        User user=new User(fn,ln,em,se,ve,de,ro);
                        int r=Integer.parseInt(user.getSeat());
                        if(r==0)
                        {
                            r=2;

                           data.set(r,user);
                        }
                        else if(r==2){
                           r=0;
                            data.set(r,user);
                            continue;
                        }
                        else {
                            data.set(r, user);
                            continue;
                        }   // data.add(user);


                    }


                        //Toast.makeText(getApplicationContext(), "SEATS \t \t"+data.size()+"\n "+data.get(2).getFirstName(), Toast.LENGTH_LONG).show();
                       mAdapter = new ListItemAdapter2(data);
                        GridLayoutManager mLayoutManager = new GridLayoutManager(UserBookActivity.this,3,GridLayoutManager.HORIZONTAL,true);
                        mLayoutManager.setReverseLayout(true);
                    mLayoutManager.setReverseLayout(true);
                    //LayoutManager.setStackFromEnd(true);

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
            finish();
            Intent mainIntent = new Intent(this, CustomerDashboardActivity.class);
            this.startActivity(mainIntent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode==KeyEvent.KEYCODE_BACK))
        {
            finish();
            Intent mainIntent = new Intent(this, CustomerDashboardActivity.class);
            mainIntent.putExtra("Info","Success");
            this.startActivity(mainIntent);
        }

        return super.onKeyDown(keyCode, event);
    }

}