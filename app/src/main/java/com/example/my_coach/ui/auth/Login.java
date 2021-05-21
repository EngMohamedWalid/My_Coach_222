package com.example.my_coach.ui.auth;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.my_coach.MainActivity;
import com.example.my_coach.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;


public class Login extends AppCompatActivity {


    //view
    private EditText _email, _pass;
    private CheckBox lremember;
    private ProgressBar lprogressBar;


    //firebase
    private FirebaseFirestore db;
    private FirebaseAuth lfirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //findview
        _email = findViewById(R.id.l_email);
        _pass = findViewById(R.id.l_pass);
        lremember = findViewById(R.id.remember);
        lprogressBar = findViewById(R.id.l_progres);

        //firebase
        lfirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //Tntent to Registration activity
        findViewById(R.id.creat).setOnClickListener(v -> startActivity(new Intent(Login.this, Regestration.class)));

        //Tntent to ForgetPasseord activity
        findViewById(R.id.forget).setOnClickListener(v -> startActivity(new Intent(Login.this, ForgetPasswordActivity.class)));

        //on click to login
        findViewById(R.id.l_login).setOnClickListener(v1 -> {
            validationData();

        });
    }

    private void validationData() {
        String email = _email.getText().toString().trim();
        String pass = _pass.getText().toString().trim();

        if (email.isEmpty()) {
            _email.requestFocus();
            showAlert("Email is required");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _email.requestFocus();
            showAlert("Invalid Email address\n Email must be yourName@company.com");
            return;
        }
        if (pass.isEmpty()) {
            _pass.requestFocus();
            showAlert("Password is required");
            return;
        }
        if (pass.length() < 6) {
            _pass.requestFocus();
            showAlert("Password should have 6 digit");
            return;
        }
        signInWithFirebase(email, pass);

    }

    private void signInWithFirebase(String email, String pass) {
        lprogressBar.setVisibility(View.VISIBLE);
        lfirebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        db.collection("users")
                                .whereEqualTo("id", task.getResult().getUser().getUid())
                                .addSnapshotListener((value, error) -> {
                                    if (error == null) {
                                        if (value == null) {
                                            lprogressBar.setVisibility(View.GONE);
                                            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show();
                                        } else {

                                            for (DocumentChange documentChange : value.getDocumentChanges()) {
                                                boolean isCoach = documentChange.getDocument().getBoolean("coach");

                                                if (lremember.isChecked()) {
                                                    getSharedPreferences("Login", MODE_PRIVATE)
                                                            .edit()
                                                            .putBoolean("islogin", true)
                                                            .apply();
                                                }
                                                getSharedPreferences("user", MODE_PRIVATE)
                                                        .edit()
                                                        .putBoolean("isCoach",isCoach)
                                                        .putString("user_id", task.getResult().getUser().getUid())
                                                        .apply();
                                                gotMain();
                                            }
                                            lprogressBar.setVisibility(View.GONE);
                                        }

                                    } else {
                                        lprogressBar.setVisibility(View.GONE);
                                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }


                                });


                    } else {
                        lprogressBar.setVisibility(View.GONE);

                        showAlert(task.getException().getMessage());
                    }


                });

    }


    void showAlert(String msg) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(msg).
                setPositiveButton("ok", null)
                .create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        boolean islogin = getSharedPreferences("Login", MODE_PRIVATE).getBoolean("islogin", false);
        if (islogin) {
            gotMain();
        }
    }

    void gotMain() {
        startActivity(new Intent(Login.this, MainActivity.class));
        finish();
    }
}

