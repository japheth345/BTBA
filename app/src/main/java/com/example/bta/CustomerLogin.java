package com.example.bta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText editTextEmail;
    TextInputEditText editTextPassword;
    Button si;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        editTextEmail = (TextInputEditText) findViewById(R.id.em);
        editTextPassword = (TextInputEditText) findViewById(R.id.pw1);
        si = (Button) findViewById(R.id.su);
        mAuth=FirebaseAuth.getInstance();
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }

            ;
        });

    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6 || password.length() > 15) {
            editTextPassword.setError("Password should be of 6-15 characters");
            editTextPassword.requestFocus();
            return;
        }

        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars)) {
            //editTextPassword.setError("Password should contain atleast one upper case alphabet");
            editTextPassword.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
            editTextPassword.requestFocus();
            return;
        }

        String lowerCaseChars = "(.*[a-z].*)";
        if (!password.matches(lowerCaseChars)) {
            editTextPassword.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
            editTextPassword.requestFocus();
            return;
        }

        String numbers = "(.*[0-9].*)";
        if (!password.matches(numbers)) {
            editTextPassword.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
            editTextPassword.requestFocus();
            return;
        }

        String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
        if (!password.matches(specialChars)) {
            editTextPassword.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
            editTextPassword.requestFocus();
            return;
        }

        //progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //  progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    //
                    //if (mAuth.getCurrentUser().isEmailVerified()) {
                        finish();
            User user=getUserDetails();
            //Helper.user=user;

            Helper.setUser(user);
                        FirebaseAuth authRef = FirebaseAuth.getInstance();
                        FirebaseUser user2 = authRef.getCurrentUser();
                        String UID = user2.getUid();
                        Helper.setUID(UID);

                        Intent intent = new Intent(CustomerLogin.this,CustomerDashboardActivity.class);
                        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                /*else {
                        editTextPassword.setText("");
                        Toast.makeText(CustomerLogin.this, "Verify your email to login", Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                    */

                //}
                else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void openRegister(View view)
    {
        Intent mainIntent = new Intent(CustomerLogin.this,RegisterActivity.class);
        CustomerLogin.this.startActivity(mainIntent);
       CustomerLogin.this.finish();
    }
    public void openResetPassword(View view)
    {
        Intent mainIntent = new Intent(CustomerLogin.this,ResetPasswordActivity.class);
        CustomerLogin.this.startActivity(mainIntent);
        CustomerLogin.this.finish();
    }
    public User getUserDetails()
    {
        User[] result = {null};
        FirebaseAuth authRef = FirebaseAuth.getInstance();
        FirebaseUser user = authRef.getCurrentUser();
        String UID = user.getUid();
         DatabaseReference myRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myUser = myRootRef.child("BTAUsers");
        DatabaseReference myUID;
        myUID = myUser.child(authRef.getCurrentUser().getUid());
        myUID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String password = dataSnapshot.child("password").getValue(String.class);
                    String role = dataSnapshot.child("role").getValue(String.class);
                    result[0] = new User(firstName, lastName, email, password, role);

                } else {
                    result[0] = new User("The", "Driver", "No Email", "", "Driver");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return result[0];

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

            Intent mainIntent = new Intent(this, HomeActivity.class);
            this.startActivity(mainIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}