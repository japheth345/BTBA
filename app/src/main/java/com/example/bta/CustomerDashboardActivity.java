package com.example.bta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;


import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CustomerDashboardActivity extends AppCompatActivity {
String str;
    public Context context;
    int pageHeight = 1120;
    int pagewidth = 1200;
    Bitmap bmp, scaledbmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        context=getApplicationContext();
        str =getIntent().getStringExtra("messo");


        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
       BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
      UserBookingFragment book= new UserBookingFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, book,"");
        transaction.commit();

       // BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.bookings:

                                UserBookingFragment book= new UserBookingFragment();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_layout, book,"");
                                transaction.commit();
                                return true;

                            case R.id.receipts:

                               UserHistoryFragment receipts= new UserHistoryFragment();
                                FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                                transaction2.replace(R.id.frame_layout,receipts,"");
                                transaction2.commit();

                        }
                     return  false;
                    }
                });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
            finish();

            Intent mainIntent = new Intent(this, HomeActivity.class);
            this.startActivity(mainIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    public void Logout(android.view.View view)
    {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.signOut();
        Intent mainIntent = new Intent(this,HomeActivity.class);
        this.startActivity(mainIntent);
    }
        private void showDialog(String title, String message,int code){
            MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .title(title)
                    .titleGravity(GravityEnum.CENTER)
                    .customView(R.layout.success_dialog, true)
                    .positiveText("OK")
                    .cancelable(false)
                    .widgetColorRes(R.color.colorPrimary)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            dialog.dismiss();
                            //  Intent mainIntent = new Intent(MpesaActivity.this,CustomerDashboardActivity.class);
                            //MpesaActivity.this.startActivity(mainIntent);

                        }
                    })
                    .build();
            android.view.View view=dialog.getCustomView();
            TextView messageText = (TextView)view.findViewById(R.id.message);
            TextView tv = (TextView)view.findViewById(R.id.tv);
            ImageView imageView = (ImageView)view.findViewById(R.id.success);
            if (code != 0){
                imageView.setVisibility(View.GONE);
            }
            messageText.setText(message);
            tv.setText("SEAT BOOKED SUCCESSFULLY");
            dialog.show();
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
        canvas.drawText("Date:\t"+user.getDeparture(), 300, 450, title);
        canvas.drawText(".................................................................", 250, 470, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(context, R.color.black));
        title.setTextSize(30);
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(user.getFirstName() +"\t"+user.getLastName(), 340, 500, title);
        canvas.drawText(user.getEmail(), 370, 550, title);
        canvas.drawText("VName:\t"+user.getVehicle(), 370, 600, title);
        canvas.drawText("Destination:\t"+user.getRoute(),340 , 650, title);
        canvas.drawText("Seat:\t"+user.getSeat(), 300, 700, title);
        canvas.drawText("Amount(Kshs):\t"+user.getFare(), 340, 750, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        title.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        title.setTextSize(50);
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Thank you welcome again", 350, 800, title);

        pdfDocument.finishPage(myPage);

        File file = new File(Environment.getExternalStorageDirectory(), "EnaCoach.pdf");

        try {

            pdfDocument.writeTo(new FileOutputStream(file));

            Toast.makeText(context, "Receipt generated successfully and stored at \n"+file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            Uri fileURL = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileURL,"application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);

        } catch (IOException e) {
            Toast.makeText(context, "Receipt not printed because \n"+e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }
}


