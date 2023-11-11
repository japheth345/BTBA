package com.example.bta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;
import com.bdhobare.mpesa.Mode;
import com.bdhobare.mpesa.Mpesa;
import com.bdhobare.mpesa.interfaces.AuthListener;
import com.bdhobare.mpesa.interfaces.MpesaListener;
import com.bdhobare.mpesa.models.STKPush;
import com.bdhobare.mpesa.models.STKPush.Builder;
import com.bdhobare.mpesa.utils.Pair;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MpesaActivity extends AppCompatActivity implements AuthListener, MpesaListener{
    public static final String BUSINESS_SHORT_CODE = "174379";
    public static final String PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
    public static final String CONSUMER_KEY = "im5vzVmG6yTAFFbqi0SOPQzTtLbWoMZ6";
    public static final String CONSUMER_SECRET = "ZL4AsyBvMRo4v4Tg";
    public static final String CALLBACK_URL = "https://console.firebase.google.com/u/2/project/commercial-c81be/database/commercial-c81be-default-rtdb/data";


    public static final String  NOTIFICATION = "PushNotification";
    public static final String SHARED_PREFERENCES = "com.example.bta";

    Button pay;
    ProgressDialog dialog;
    EditText phone;



    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public String id;
    private DatabaseReference rootRef;
    private DatabaseReference databaseItems;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth authRef = FirebaseAuth.getInstance();
    FirebaseUser user2 = authRef.getCurrentUser();
    String UID = user.getUid();
    DatabaseReference rootRef2 = FirebaseDatabase.getInstance().getReference();
    DatabaseReference sellerRef = rootRef2.child("BTAUsers").child(UID);
    String str=null;
    TextView tv1,tv4;
    public Context context;
    int pageHeight = 1120;
    int pagewidth = 1200;
    Bitmap bmp, scaledbmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa);
        pay = (Button)findViewById(R.id.pay);
        phone = (EditText)findViewById(R.id.phone);
      //  amount = (EditText)findViewById(R.id.amount);

        FillBookingData();
          context=getApplicationContext();

        Vehicle veh=Helper.getVehicle();

        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        Mpesa.with(this, CONSUMER_KEY, CONSUMER_SECRET);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Processing");
        dialog.setIndeterminate(true);
         str =getIntent().getStringExtra("messo");
         if(str.equals("Normal"))
         {
                 str="BOOKING SUCCESSFUL";
         }
         else
         {
             pay.setVisibility(View.INVISIBLE);
             phone.setVisibility(View.INVISIBLE);
            // amount.setVisibility(View.INVISIBLE);

             //tv2.setVisibility(View.INVISIBLE);


             //FillBookingData();

         }
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String p = phone.getText().toString();
                int a = 1;
                if (p.isEmpty()){
                    phone.setError("Enter phone.");
                    return;
                }
              //  FillBookingData();
                pay(p, a);
            }
        });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(NOTIFICATION)) {
                    String title = intent.getStringExtra("title");
                    String message = intent.getStringExtra("message");
                    int code = intent.getIntExtra("code", 0);
                    showDialog(title, message, code);

                }
            }
        };
    }

    @Override
    public void onAuthError(Pair<Integer, String> result) {
        Log.e("Error", result.message);
    }

    @Override
    public void onAuthSuccess() {

        //TODO make payment
        pay.setEnabled(true);
    }
    private void pay(String phone, int amount){
        dialog.show();
        STKPush.Builder builder = new Builder(BUSINESS_SHORT_CODE, PASSKEY, amount,BUSINESS_SHORT_CODE, phone);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String token = sharedPreferences.getString("InstanceID", "");

        builder.setFirebaseRegID(token);
        STKPush push = builder.build();



        Mpesa.getInstance().pay(this, push);

    }
    

    @Override
    public void onMpesaError(Pair<Integer, String> result) {
        dialog.hide();
        Intent mainIntent = new Intent(MpesaActivity.this,CustomerDashboardActivity.class);
        MpesaActivity.this.startActivity(mainIntent);
        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMpesaSuccess(String MerchantRequestID, String CheckoutRequestID, String CustomerMessage) {
        dialog.hide();
        Intent mainIntent = new Intent(MpesaActivity.this,CustomerDashboardActivity.class);
        MpesaActivity.this.startActivity(mainIntent);
        //Toast.makeText(this, CustomerMessage, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(NOTIFICATION));

    }
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
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
    public void FillBookingData()
    {
        DatabaseReference rootRef2 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference sellerRef = rootRef2.child("BTAUsers").child(UID);
        rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseItems = rootRef.child("BTABookings");
        Vehicle veh2=Helper.getVehicle();
        Vehicle veh=veh2;
        User user3=Helper.getUser();
        if(veh2==null || veh==null || user3==null)
        {  finish();
            Intent mainIntent = new Intent(this, CustomerDashboardActivity.class);
            this.startActivity(mainIntent);
        }
        else {
       // String fn=user3.getFirstName()+"#"+veh2.getFare();
       // user3.setFirstName(fn);
       // Toast.makeText(this,"ID="+UID +"\n VEHICLE \t"+ veh.getName()+" \n User \t"+user3.getFirstName(),Toast.LENGTH_LONG).show();
// Toast.makeText(this,"Date "+user3.getDeparture(),Toast.LENGTH_LONG).show();
            databaseItems.child(veh2.getName()).setValue(veh2);
            databaseItems.child("DETAILS").child(veh2.getName()).child(user3.getSeat()).setValue(user3);
            sellerRef.child("BOOKINGS").child(new java.util.Date().toString()).setValue(user3);
           Toast.makeText(MpesaActivity.this, "Congratulations you have booked a seat now its time to pay for it", Toast.LENGTH_LONG).show();
          //  Intent mainIntent = new Intent(this, CustomerDashboardActivity.class);
           // mainIntent.putExtra("Info","Success");
           // this.startActivity(mainIntent);
             // generatePDF(user3);
            //showDialog("SUCCESS", str,0);

    }}

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
                        finish();
                         Intent mainIntent = new Intent(MpesaActivity.this,CustomerDashboardActivity.class);
                         MpesaActivity.this.startActivity(mainIntent);

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
           /* Uri fileURL = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileURL,"application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
            */


        } catch (IOException e) {
            Toast.makeText(context, "Receipt not printed because \n"+e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
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