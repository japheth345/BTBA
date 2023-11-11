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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Filterable;
public class ListItemAdapter5 extends RecyclerView.Adapter<ListItemAdapter5.ViewHolder> {
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
        public TextView price_tv, status, cap, rem;
        public ImageView image_iv;
        public ToggleButton fav;

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
            cap = (TextView) v.findViewById(R.id.cap);
            rem = (TextView) v.findViewById(R.id.rem);

            view = v;
            context = v.getContext();
        }
    }

    public ListItemAdapter5(List<User> itemList) {
        this.itemList = itemList;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookings4, parent, false);
        return new ListItemAdapter5.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemAdapter5.ViewHolder holder, int position) {
        rootRef = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        User listItem = itemList.get(position);

       /* if (Integer.parseInt(listItem.getRseats()) <=0) {
            holder.btBook.setVisibility(View.INVISIBLE);
        }

        */
        //holder.id = listItem.getID();
        if (listItem == null) {
            holder.name_tv.setText("Seat:"+String.valueOf(position));
            holder.price_tv.setText("SEAT NOT BOOKED");

            holder.status.setText("");
            holder.cap.setText("");
            holder.rem.setText("");

        } else if (listItem != null) {
            holder.name_tv.setText("Seat:\t"+listItem.getSeat());
            holder.price_tv.setText("Name:\t"+listItem.getFirstName() +"\t "+listItem.getLastName());
            holder.status.setText("Email:\t"+listItem.getEmail());
            holder.cap.setText("Route:\t"+ listItem.getRoute());
            holder.rem.setText("Travel cool");


        }


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public User getListItem(int pos) {
        return itemList.get(pos);
    }


}
