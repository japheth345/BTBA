package com.example.bta;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
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
public class ListItemAdapter3 extends RecyclerView.Adapter<ListItemAdapter3.ViewHolder> {
    private List<User> itemList;

    private DatabaseReference rootRef;
    private DatabaseReference currentUserRef;
    private DatabaseReference favRef;
    private StorageReference mStorage;
    private FirebaseUser user;

    // View holder is what holds the views
    public Context context;
    int pageHeight = 1120;
    int pagewidth = 1200;
    Bitmap bmp, scaledbmp;
    private final String filePath = "ENACOACH";
    private int STORAGE_PERMISSION_CODE = 1;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView date1,date2,name1,name2,seat1,seat2,fare1,fare2,tv1,tv2;
        public Button btPrint;
        public String id;
        private DatabaseReference rootRef;
        private DatabaseReference databaseItems;
        private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth authRef = FirebaseAuth.getInstance();
        FirebaseUser user2 = authRef.getCurrentUser();
        String UID = user.getUid();
        DatabaseReference rootRef2 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference sellerRef = rootRef2.child("BTAUsers").child(UID);


        public ViewHolder(View v) {
            super(v);
            date1 = (TextView) v.findViewById(R.id.date1);
            date2 = (TextView) v.findViewById(R.id.date2);
            name1=(TextView) v.findViewById(R.id.name1);
            name2 = (TextView) v.findViewById(R.id.name2);
            tv1=(TextView) v.findViewById(R.id.tv1);
            tv2=(TextView) v.findViewById(R.id.tv2);
            seat1= (TextView) v.findViewById(R.id.seat1);
             seat2= (TextView) v.findViewById(R.id.seat2);
            fare1 = (TextView) v.findViewById(R.id.fare1);
            fare2 = (TextView) v.findViewById(R.id.fare2);
            btPrint = (Button) v.findViewById(R.id.bPrint);
            mStorage = FirebaseStorage.getInstance().getReference();
            view = v;
            context = v.getContext();
        }
    }

    public ListItemAdapter3(List<User> itemList) {
        this.itemList = itemList;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookings3, parent, false);
        return new ListItemAdapter3.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemAdapter3.ViewHolder holder, int position) {
        rootRef = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        User listItem = itemList.get(position);

       /* if (Integer.parseInt(listItem.getRseats()) <=0) {
            holder.btBook.setVisibility(View.INVISIBLE);
        }

        */
        //holder.id = listItem.getID();
        if (listItem == null) {
            holder.date1.setText("OOPS!");
            holder.date2.setText("No records");
            holder.date1.setTextColor(Color.RED);
            holder.date2.setTextColor(Color.RED);
           holder.name1.setVisibility(View.INVISIBLE);
            holder.name2.setVisibility(View.INVISIBLE);
            holder.seat1.setVisibility(View.INVISIBLE);
            holder.seat2.setVisibility(View.INVISIBLE);
            holder.fare1.setVisibility(View.INVISIBLE);
            holder.fare2.setVisibility(View.INVISIBLE);
        }
        else if (listItem != null)
        {
   holder.date2.setText(listItem.getDeparture());
   holder.name2.setText(listItem.getVehicle());
   holder.seat2.setText(listItem.getSeat());
   holder.fare2.setText(listItem.getFare());

        }
        holder.btPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               generatePDF(listItem);


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
    public static File commonDocumentDirPath(String FolderName)
    {
        File dir = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + FolderName);
        }
        else
        {
            dir = new File(Environment.getExternalStorageDirectory() + "/" + FolderName);
        }

        // Make sure the path directory exists.
        if (!dir.exists())
        {
            // Make it, if it doesn't exit
            boolean success = dir.mkdirs();
            if (!success)
            {
                dir = null;
            }
        }
        return dir;
    }
    private void generatePDF(User user) {

        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.pic);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 500, 300, false);
        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();
        Paint title = new Paint();
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();


        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        Canvas canvas = myPage.getCanvas();

        canvas.drawBitmap(scaledbmp, 240, 20, paint);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        title.setTextSize(40);

        title.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("EnaCoach Shuttle", 300, 370, title);
        canvas.drawText("Customer Receipt", 300, 400, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        title.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        title.setTextSize(25);
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Date:\t" + user.getDeparture(), 300, 450, title);
        canvas.drawText(".................................................................", 250, 470, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(context, R.color.black));
        title.setTextSize(30);
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(user.getFirstName() + "\t" + user.getLastName(), 340, 500, title);
        canvas.drawText(user.getEmail(), 370, 550, title);
        canvas.drawText("VName:\t" + user.getVehicle(), 370, 600, title);
        canvas.drawText("Destination:\t" + user.getRoute(), 340, 650, title);
        canvas.drawText("Seat:\t" + user.getSeat(), 300, 700, title);
        canvas.drawText("Amount(Kshs):\t" + user.getFare(), 340, 750, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        title.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        title.setTextSize(50);
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Thank you welcome again", 350, 800, title);

        pdfDocument.finishPage(myPage);
        File myExternalFile = new File(context.getExternalFilesDir(filePath), new java.util.Date().toString()+".pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(myExternalFile));
            Toast.makeText(context, "Receipt generated successfully and stored at \n" + myExternalFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            Uri fileURL = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", myExternalFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileURL, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
            Toast.makeText(context, "File saved!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            //If file is not saved, print stack trace, clear edittext, and display toast message
            e.printStackTrace();
            //myEditText.setText("");
            Toast.makeText(context, "File not saved... Possible permissions error", Toast.LENGTH_LONG).show();
        }
         //PdfDocument.close();
    }






}
