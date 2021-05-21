package com.example.my_coach.ui.auth;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.my_coach.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class  Regestration extends AppCompatActivity {

    //view
    private EditText tedName, tedPhone, tedEmail, tedPassword, tedDate;
    private ProgressBar tprogressBar;
    private RadioButton tedCoch,tedTraine;
    boolean isCoach;

    //firebase
    private FirebaseAuth tfirebaseAuth;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regestration);


        //findview
        tedName = findViewById(R.id.rt_name);
        tedPhone = findViewById(R.id.rt_phone);
        tedEmail = findViewById(R.id.rt_email);
        tedPassword =  findViewById(R.id.rt_pass);
        tedDate = findViewById(R.id.rt_dat);
        tprogressBar=findViewById(R.id.tr_progres);
        tedCoch=findViewById(R.id.rb_Coach);
        tedTraine=findViewById(R.id.rb_Trainee);

        //firebase
        tfirebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        //on click to regest traine
        findViewById(R.id.rt_regest).setOnClickListener(v -> {


            validationdata();

        });
        //on click to finesh this fragment
        findViewById(R.id.rt_back).setOnClickListener(v -> {
            startActivity(new Intent(Regestration.this, Login.class));

        });
    }

    private void validationdata() {
        String tname = tedName.getText().toString().trim();
        String tphone = tedPhone.getText().toString().trim();
        String temail = tedEmail.getText().toString().trim();
        String tpass = tedPassword.getText().toString().trim();
        String tdat = tedDate.getText().toString().trim();




        if (tname.isEmpty()) {
            tedName.requestFocus();
            showAlert("Name is required");
            return;
        }
        if (tphone.isEmpty()) {
            tedPhone.requestFocus();
            showAlert("Phone is required");
            return;
        }
        if (tphone.length() < 11) {
            tedPhone.requestFocus();
            showAlert("Invalid Phone Number");
            return;
        }
        if (temail.isEmpty()) {
            tedEmail.requestFocus();
            showAlert("Email is required");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(temail).matches()) {
            tedEmail.requestFocus();
            showAlert("Invalid Email address\n Email must be yourName@company.com");
            return;
        }
        if (tpass.isEmpty()) {
            tedPassword.requestFocus();
            showAlert("Password is required");
            return;
        }
        if (tpass.length() < 6) {
            tedPassword.requestFocus();
            showAlert("Password should have 6 digit");
            return;
        }

        if (tdat.isEmpty()) {
            tedDate.requestFocus();
            showAlert("Date_of_birth is required");
            return;
        }
        signupWithFirebase(temail,tpass);


    }

    private void signupWithFirebase(String temail, String tpass) {
        tprogressBar.setVisibility(View.VISIBLE);
        tfirebaseAuth.createUserWithEmailAndPassword(temail,tpass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        saveUserData();

                    }else {
                        tprogressBar.setVisibility(View.GONE);
                        showAlert(task.getException().getMessage());
                    }


                });
    }

    private void saveUserData() {
        if (tedCoch.isChecked()){
            isCoach=true;
        }
        if (tedTraine.isChecked()){
            isCoach=false;

        }

        if (tfirebaseAuth.getCurrentUser() != null){

            String userID=tfirebaseAuth.getCurrentUser().getUid();

            Map<String, Object> user = new HashMap<>();
            user.put("id",userID);
            user.put("name",tedName.getText().toString().trim());
            user.put("phone",tedPhone.getText().toString().trim());
            user.put("email",tedEmail.getText().toString().trim());
            user.put("coach",isCoach);
            user.put("password",tedPassword.getText().toString().trim());
            user.put("Date",tedDate.getText().toString().trim());
            user.put("image","null");

            firestore.collection("users")
                    .document(userID)
                    .set(user)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()){
                            tprogressBar.setVisibility(View.GONE);
                            new AlertDialog.Builder(Regestration.this)
                                    .setTitle("Congratulations")
                                    .setMessage("Account Created Successfuly")
                                    .setCancelable(false)
                                    .setPositiveButton("ok", (dialog, which) -> {

                                        startActivity(new Intent(Regestration.this,Login.class));

                                    })
                                    .create().show();

                        }else {
                            tprogressBar.setVisibility(View.GONE);
                            showAlert("Error \n"+ task.getException().getMessage());
                        }
                    });

        }



    }

    void showAlert(String msg ){
        new AlertDialog.Builder(Regestration.this)
                .setTitle("Error")
                .setMessage(msg).
                setPositiveButton("ok",null)
                .create().show();
    }
}
