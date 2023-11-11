package com.example.bta;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.Filterable;
public class ListItemAdapter4 extends RecyclerView.Adapter<ListItemAdapter4.ViewHolder> {
    private List<Vehicle> itemList;

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
        public Button btBook;
        public String id;
        CardView cv;
        LinearLayout ll;
        public ViewHolder(View v) {
            super(v);
            cv=(CardView) v.findViewById(R.id.cv);
            ll=(LinearLayout) v.findViewById(R.id.ll);
            name_tv = (TextView) v.findViewById(R.id.list_item_name);
            price_tv = (TextView) v.findViewById(R.id.list_item_price);
            status = (TextView) v.findViewById(R.id.desc);
            cap = (TextView) v.findViewById(R.id.cap);
            rem = (TextView) v.findViewById(R.id.rem);
            btBook = (Button) v.findViewById(R.id.bBook);
            btBook.setText("VIEW");
            view = v;
            context = v.getContext();
        }
    }
    public ListItemAdapter4(List<Vehicle> itemList) {
        this.itemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookings, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        rootRef = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Vehicle listItem = itemList.get(position);

        if (listItem==null) {
            holder.cv.removeAllViews();
           holder.cv.setVisibility(View.INVISIBLE);
           holder.ll.removeAllViews();
           holder.ll.setVisibility(View.INVISIBLE);
        }



        holder.id = listItem.getID();
        holder.name_tv.setText("Name: \t"+listItem.getName());
        holder.price_tv.setText("Route: \t"+ listItem.getRoute());
        holder.status.setText("Departure: \t "+listItem.getDeparture());
        holder.cap.setText("Capacity: \t"+listItem.getSeats());
        holder.rem.setText("Empty Slots:\t "+listItem.getRseats());

        holder.view.setTag(listItem.getID());

        holder.btBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.view.setTag(listItem.getID());
                Vehicle listItem = itemList.get(position);
                Helper.setVehicle(listItem );
                ArrayList<String> results=new ArrayList<>();

                Intent mainIntent = new Intent(context,BookingHistoryActivity.class);
                context.startActivity(mainIntent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public Vehicle getListItem(int pos) {
        return itemList.get(pos);
    }
}
