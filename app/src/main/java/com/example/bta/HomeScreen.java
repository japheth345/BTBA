package com.example.bta;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import androidx.annotation.NonNull;
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

import android.content.Context;
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
public class HomeScreen extends AppCompatActivity {

    private TextView myEditText;
    private Button saveButton;
    private Button viewButton;
    private String fileName = "";
    private String currentDate;
    private final String filePath = "ENACOACH RECEIPTS";
    private int STORAGE_PERMISSION_CODE = 1;
    private boolean isSelected;
    public Context context;
    int pageHeight = 1120;
    int pagewidth = 1200;
    Bitmap bmp, scaledbmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        checkPermissions();
        setContentView(R.layout.activity_mainbb);
        myEditText = findViewById(R.id.editText);
        saveButton = findViewById(R.id.button);
        viewButton = findViewById(R.id.viewFiles);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get time and date
                currentDate = new SimpleDateFormat("yyyy-MM-dd__HH:mm:ss").format(new Date());
                //Append date and tinme to filename
                fileName = "/File_Name__" + currentDate + ".pdf";
                //Call the createPDF method on the user input
                User user = Helper.getUser();
                generatePDF(user);
            }

            ;
        });
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openFileViewer = new Intent(HomeScreen.this, HomeActivity.class);
                startActivity(openFileViewer);
                finish();

            }
        });
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(HomeScreen.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //Do nothing, we granted the permission already
        } else {
            requestStoragePermission();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createMyPDF(String myString,User user){

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

        File myExternalFile = new File(getExternalFilesDir(filePath), fileName);
        try {
            pdfDocument.writeTo(new FileOutputStream(myExternalFile));
            Toast.makeText(HomeScreen.this,"File saved!", Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            //If file is not saved, print stack trace, clear edittext, and display toast message
            e.printStackTrace();
            myEditText.setText("");
            Toast.makeText(HomeScreen.this,"File not saved... Possible permissions error", Toast.LENGTH_LONG).show();
        }
         //PdfDocument.close();
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
        File myExternalFile = new File(getExternalFilesDir(filePath), fileName);
        try {
            pdfDocument.writeTo(new FileOutputStream(myExternalFile));
            Toast.makeText(HomeScreen.this, "File saved!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            //If file is not saved, print stack trace, clear edittext, and display toast message
            e.printStackTrace();
            myEditText.setText("");
            Toast.makeText(HomeScreen.this, "File not saved... Possible permissions error", Toast.LENGTH_LONG).show();
        }
       // PdfDocument.close();
    }
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(HomeScreen.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}