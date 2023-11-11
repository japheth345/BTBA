package com.example.bta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.UUID;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.AdapterView;
import android.app.AlertDialog;
public class AddBookingActivity extends AppCompatActivity {
Spinner bus,route,time;
EditText seats,fare;
Button add;
DatePicker dp;
String b,r,t,d,fn,ln,em;
public User [] bookings;
    private DatabaseReference rootRef;
    private DatabaseReference databaseItems;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth authRef = FirebaseAuth.getInstance();
    FirebaseUser user2 = authRef.getCurrentUser();
    String UID = user.getUid();
    DatabaseReference rootRef2 = FirebaseDatabase.getInstance().getReference();
    DatabaseReference sellerRef = rootRef2.child("Users").child(UID);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_booking);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        rootRef = FirebaseDatabase.getInstance().getReference();
        databaseItems = rootRef.child("BTABookings");
        bus = (Spinner) findViewById(R.id.buses);
        route= (Spinner) findViewById(R.id.route);
        time = (Spinner) findViewById(R.id.time);
        seats = (EditText) findViewById(R.id.slots);
        fare = (EditText) findViewById(R.id.fare);
        dp=(DatePicker) findViewById(R.id.dp);
        add=(Button) findViewById(R.id.add);
        bus.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int category, long id) {
                        b=bus.getSelectedItem().toString();


                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        return;
                    }
                });

        route.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int category, long id) {
                        r=route.getSelectedItem().toString();


                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        return;
                    }
                });
        time.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int category, long id) {
                        t=time.getSelectedItem().toString();


                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        return;
                    }
                });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getBaseContext());
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               // isItemPostable = false;
                                dialog.cancel();
                            }
                        });

                if (b.equals(null)) {
                    builder1.setMessage("Please select Bus");
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                  else  if (r.equals(null)) {
                        builder1.setMessage("Please select Route");
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
               else  if (t.equals(null)) {
                    builder1.setMessage("Please select Time");
                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
               else  if (seats.getText().equals(null)) {
                    builder1.setMessage("Kindly Enter Number of Slots");
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else  if (fare.getText().equals(null)) {
                        builder1.setMessage("Kindly Enter Fare");
                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                    }

                else {
                    int m=(new java.util.Date().getMonth() +1);

                  String d=""+dp.getDayOfMonth()+"/"+m+"/"+dp.getYear()+"\t,"+t;
                  String s=seats.getText().toString();
                  String f=fare.getText().toString();

                    sellerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            fn = dataSnapshot.child("firstName").getValue(String.class);
                            ln = dataSnapshot.child("lastName").getValue(String.class);
                            em = dataSnapshot.child("email").getValue(String.class);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError)
                        {

                        }
                    });
                    String uniqueItemID = UUID.randomUUID().toString();
                    Vehicle listingItem = new Vehicle(uniqueItemID,b,r,d,f,s,s);
                   User listingItem2 = new User("DRIVER","SEAT","No Email",String.valueOf(0),b,t,r);
                   int n=Integer.parseInt(s) +1;
                   bookings=new User[Integer.parseInt(s) +1];
                   bookings[0]=listingItem2;
                   databaseItems.child(b).setValue(listingItem);
                   databaseItems.child("DETAILS").child(b).child(String.valueOf(0)).setValue(bookings[0]);

                    Toast.makeText(getBaseContext(), "ROUTE ADDED SUCCESSFULLY ", Toast.LENGTH_LONG).show();
                    Intent setIntent = new Intent(getApplicationContext(), AdminDashboardActivity.class);
                    setIntent.addCategory(Intent.CATEGORY_HOME);
                    setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(setIntent);


                }
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
            Intent mainIntent = new Intent(this, AdminDashboardActivity.class);
            this.startActivity(mainIntent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode==KeyEvent.KEYCODE_BACK))
        {
            finish();
            Intent mainIntent = new Intent(this, AdminDashboardActivity.class);
            mainIntent.putExtra("Info","Success");
            this.startActivity(mainIntent);
        }

        return super.onKeyDown(keyCode, event);
    }

}