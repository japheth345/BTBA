package com.example.bta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PrintReceipt extends AppCompatActivity {
    public TextView tv1,tv2;
    private StorageReference mStorage;
    User user2;
    private List<User> itemList;

    private DatabaseReference rootRef;
    private DatabaseReference currentUserRef;
    private DatabaseReference favRef;

    private FirebaseUser user;

    // View holder is what holds the views
    public Context context;
    int pageHeight = 1120;
    int pagewidth = 1200;
    Bitmap bmp, scaledbmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_receipt);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        tv1=(TextView) findViewById(R.id.tv1);
        tv2=(TextView) findViewById(R.id.tv2);
        mStorage = FirebaseStorage.getInstance().getReference();

    }


}