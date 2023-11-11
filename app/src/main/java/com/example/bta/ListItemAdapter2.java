package com.example.bta;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Filterable;
public class ListItemAdapter2 extends RecyclerView.Adapter<ListItemAdapter2.ViewHolder> {
    private List<User> itemList;

    private DatabaseReference rootRef;
    private DatabaseReference currentUserRef;
    private DatabaseReference favRef;

    private FirebaseUser user;

    // View holder is what holds the views
    public Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView name_tv;
        public TextView price_tv, status;
        RelativeLayout rl;
        public ImageView image_iv;
        public ToggleButton fav;
        public Button btBook;
        public String id;
        private DatabaseReference rootRef;
        private DatabaseReference databaseItems;
        private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth authRef = FirebaseAuth.getInstance();
        FirebaseUser user2 = authRef.getCurrentUser();
        String UID = user.getUid();
        DatabaseReference rootRef2 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference sellerRef = rootRef2.child("Users").child(UID);

        public ViewHolder(View v) {
            super(v);
            name_tv = (TextView) v.findViewById(R.id.list_item_name);
            price_tv = (TextView) v.findViewById(R.id.list_item_price);
            status = (TextView) v.findViewById(R.id.desc);
            image_iv=(ImageView) v.findViewById(R.id.list_item_photo);
           rl=(RelativeLayout) v.findViewById(R.id.item_list_info);
            btBook = (Button) v.findViewById(R.id.bBook);
            view = v;
            context = v.getContext();
        }
    }

    public ListItemAdapter2(List<User> itemList) {
        this.itemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookings2, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        rootRef = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        User listItem = itemList.get(position);
        int p=position;
if(p==0)
        {
            p=2;
        }
else if(p==2)
{
    p=0;
}

        if (listItem == null) {

            // holder.name_tv.setText("Name:\t"+
            holder.price_tv.setText("" + p);
             holder.status.setTextColor(Color.GREEN);
            holder.status.setText("Available");
            holder.btBook.setEnabled(true);
            // holder.cap.setText("");
            //holder.rem.setText("Empty Slots:\t"+listItem.getRseats());

            //holder.view.setTag(listItem.getID());

        } else if (listItem != null) {
            // holder.name_tv.setText("Name:\t"+listItem.getName());
            holder.price_tv.setText("" + p);
            if(p==0) {
                holder.status.setText("DRIVER");
            }
            else
            {
                holder.status.setText("BOOKED");
            }
            holder.rl.setEnabled(false);
            //holder.cap.setText("BOOKED");
            //holder.rem.setText("Empty Slots:\t"+listItem.getRseats());
            holder.btBook.setEnabled(false);
            //holder.view.setTag(listItem.getID());


        }
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p=position;
                if(p==0)
                {
                    p=2;
                }
                else if(p==2)
                {
                    p=0;
                }
                int pos =p;
                String UID = Helper.getUID();

                Vehicle veh = Helper.getVehicle();
                User user =Helper.getUser();
                // Toast.makeText(context,"ID="+UID +"\n VEHICLE \t"+ veh.getName()+" \n User \t"+user.getFirstName(),Toast.LENGTH_LONG).show();
                int n = (Integer.parseInt(veh.getRseats())) - 1;
                Vehicle veh2 = new Vehicle(veh.getID(), veh.getName(), veh.getRoute(), veh.getDeparture(), veh.getFare(), veh.getSeats(), String.valueOf(n));
                User user2 = new User(user.getFirstName(), user.getLastName(), user.getEmail(), String.valueOf(pos), veh.getName(), veh.getDeparture(), veh.getRoute());
                java.util.Date depa=new java.util.Date();
                String dep=depa.getDate()+"/"+depa.getMonth()+"/"+depa.getYear();
                User user3 = new User(user.getFirstName(), user.getLastName(), user.getEmail(), String.valueOf(pos),veh.getName(),veh.getDeparture(),veh.getRoute(),veh.getFare());
                Helper.setVehicle(veh2);
                Helper.setUser(user3);

                Intent mainIntent = new Intent(context, MpesaActivity.class);
                mainIntent.putExtra("messo", "Normal");
                context.startActivity(mainIntent);


            }
        });

        holder.image_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p=position;
                if(p==0)
                {
                    p=2;
                }
                else if(p==2)
                {
                    p=0;
                }
                int pos =p;
                String UID = Helper.getUID();

                Vehicle veh = Helper.getVehicle();
                User user =Helper.getUser();
                // Toast.makeText(context,"ID="+UID +"\n VEHICLE \t"+ veh.getName()+" \n User \t"+user.getFirstName(),Toast.LENGTH_LONG).show();
                int n = (Integer.parseInt(veh.getRseats())) - 1;
                Vehicle veh2 = new Vehicle(veh.getID(), veh.getName(), veh.getRoute(), veh.getDeparture(), veh.getFare(), veh.getSeats(), String.valueOf(n));
                User user2 = new User(user.getFirstName(), user.getLastName(), user.getEmail(), String.valueOf(pos), veh.getName(), veh.getDeparture(), veh.getRoute());
                java.util.Date depa=new java.util.Date();
                String dep=depa.getDate()+"/"+depa.getMonth()+"/"+depa.getYear();
                User user3 = new User(user.getFirstName(), user.getLastName(), user.getEmail(), String.valueOf(pos), veh.getName(),veh.getDeparture(),veh.getRoute(),veh.getFare());
                Helper.setVehicle(veh2);
                Helper.setUser(user3);

                Intent mainIntent = new Intent(context, MpesaActivity.class);
                mainIntent.putExtra("messo", "Normal");
                context.startActivity(mainIntent);


            }
        });



        holder.btBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p=position;
                if(p==0)
                {
                    p=2;
                }
                else if(p==2)
                {
                    p=0;
                }
                int pos =p;
                String UID = Helper.getUID();

                Vehicle veh = Helper.getVehicle();
                User user =Helper.getUser();
               // Toast.makeText(context,"ID="+UID +"\n VEHICLE \t"+ veh.getName()+" \n User \t"+user.getFirstName(),Toast.LENGTH_LONG).show();
               int n = (Integer.parseInt(veh.getRseats())) - 1;
                Vehicle veh2 = new Vehicle(veh.getID(), veh.getName(), veh.getRoute(), veh.getDeparture(), veh.getFare(), veh.getSeats(), String.valueOf(n));
                User user2 = new User(user.getFirstName(), user.getLastName(), user.getEmail(), String.valueOf(pos), veh.getName(), veh.getDeparture(), veh.getRoute());
                java.util.Date depa=new java.util.Date();
                String dep=depa.getDate()+"/"+depa.getMonth()+"/"+depa.getYear();
                User user3 = new User(user.getFirstName(), user.getLastName(), user.getEmail(), String.valueOf(pos), veh.getName(),veh.getDeparture(),veh.getRoute());
                Helper.setVehicle(veh2);
                Helper.setUser(user3);

                Intent mainIntent = new Intent(context, MpesaActivity.class);
                mainIntent.putExtra("messo", "Normal");
                context.startActivity(mainIntent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public User getListItem(int pos) {
        return itemList.get(pos);
    }

    public User getUserDetails() {
        User[] result = new User[1];
        FirebaseAuth authRef = FirebaseAuth.getInstance();
        FirebaseUser user = authRef.getCurrentUser();
        String UID = user.getUid();

       /* DatabaseReference myRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myUser = myRootRef.child("BTAUsers").child(UID);
        myUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String firstName = dataSnapshot.child("firstName").getValue(String.class);
                String lastName = dataSnapshot.child("lastName").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String password = dataSnapshot.child("password").getValue(String.class);
                String role = dataSnapshot.child("role").getValue(String.class);
                result[0] = new User(firstName, lastName, email, password, role);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                result[0] = new User("The", "Driver", "No Email", "", "Driver");
            }
        });
*/
        return result[0];
    }

}
