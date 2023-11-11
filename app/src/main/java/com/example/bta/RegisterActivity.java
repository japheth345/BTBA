package com.example.bta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import java.util.regex.Pattern;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class RegisterActivity extends AppCompatActivity {
    ProgressBar progressBar;

    TextInputEditText fn, ln, em, pw1, pw2;
    private FirebaseAuth mAuth;
    private Button b1;
    private DatabaseReference myRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myUser = myRootRef.child("BTAUsers");
    private DatabaseReference myUID;
    String role="User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        fn = (TextInputEditText) findViewById(R.id.fn);
        ln = (TextInputEditText) findViewById(R.id.ln);
        em = (TextInputEditText) findViewById(R.id.em);
        pw1 = (TextInputEditText) findViewById(R.id.pw1);
        pw2 = (TextInputEditText) findViewById(R.id.pw2);
        b1 = (Button) findViewById(R.id.su);

        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }

            ;
        });

    }

    private void registerUser() {
        String fname = fn.getText().toString().trim();
        String lname = ln.getText().toString().trim();
        String email = em.getText().toString().trim();
        String password = pw1.getText().toString().trim();
        String password2 = pw2.getText().toString().trim();

        if (fname.isEmpty()) {
            fn.setError("First Name is required");
            fn.requestFocus();
            return;
        }
        if (lname.isEmpty()) {
            ln.setError("Last Name is required");
            ln.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            em.setError("Email is required");
            em.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            em.setError("Please enter a valid email");
            em.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            pw1.setError("Password is required");
            pw1.requestFocus();
            return;
        }
        if (password2.isEmpty()) {
            pw2.setError("Re-Enter Password");
            pw2.requestFocus();
            return;
        }
        if (password.length() < 6 || password.length() > 15) {
            pw1.setError("Password should be of 6-15 characters");
            pw1.requestFocus();
            return;
        }
        if (password.equals(password2)) {

            String upperCaseChars = "(.*[A-Z].*)";
            if (!password.matches(upperCaseChars)) {
                //editTextPassword.setError("Password should contain atleast one upper case alphabet");
                pw1.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
                pw1.requestFocus();
                return;
            }

            String lowerCaseChars = "(.*[a-z].*)";
            if (!password.matches(lowerCaseChars)) {
                pw1.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
                pw1.requestFocus();
                return;
            }

            String numbers = "(.*[0-9].*)";
            if (!password.matches(numbers)) {
                pw1.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
                pw1.requestFocus();
                return;
            }

            String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
            if (!password.matches(specialChars)) {
                pw1.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
                pw1.requestFocus();
                return;
            }

            //progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {

                        FirebaseAuth authRef = FirebaseAuth.getInstance();
                        FirebaseUser user = authRef.getCurrentUser();
                        String UID = user.getUid();
                        User newUser = new User(fname, lname, email, password,role);
                        myUser.child(UID).setValue(newUser);
                       // Toast.makeText(getApplicationContext(),"User Registered Successfully",Toast.LENGTH_SHORT).show();
                        showDialog("Registration Suceessful", "Check your Email for Verification",0);
                        sendEmailVerification();

                        finish();

                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                            em.setText("");
                            pw1.setText("");
                            pw2.setText("");
                            showDialog("Error", "You are already registered",1);
                            //Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                        } else {
                            em.setText("");
                            pw1.setText("");
                            pw2.setText("");
                            showDialog("Error", task.getException().getMessage(),1);
                           // Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
        else
        {
            pw1.setError("Password does  not match");
            pw1.requestFocus();
            pw2.setError("Password does  not match");
            pw2.requestFocus();
            return;


        }
    }

    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Check your Email for Verification", Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                        fn.setText("");
                        ln.setText("");
                        em.setText("");
                        pw1.setText("");
                        pw2.setText("");

                    } else {
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
    public void GoToLogin(View view)
    {
        Intent intent=new Intent(RegisterActivity.this,HomeActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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
                        finish();
                        Intent mainIntent = new Intent(RegisterActivity.this,HomeActivity.class);
                        RegisterActivity.this.startActivity(mainIntent);

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
        tv.setText("");
        dialog.show();
    }

}
