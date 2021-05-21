package com.example.my_coach.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.my_coach.BuildConfig;
import com.example.my_coach.R;
import com.example.my_coach.Sitting_Activity;
import com.example.my_coach.ui.Splash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class ProfileFragment extends Fragment {

    private TextView UserName;
    private CircleImageView Profile_image;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private ProgressBar progressBar;
    private String NameDB,ImageDB,EmaileDB,PhoneDB;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserName = view.findViewById(R.id.user_name);
        Profile_image = view.findViewById(R.id.profile_image);
        progressBar = view.findViewById(R.id.ProgressBarProfil);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        GetUserData();


        //intant to settings activety
        view.findViewById(R.id.btn_sitting).setOnClickListener(v ->
                {
                    Intent intent=new Intent(getActivity(), Sitting_Activity.class);

                    intent.putExtra("name",NameDB);
                    intent.putExtra("image",ImageDB);
                    intent.putExtra("email",EmaileDB);
                    intent.putExtra("phone",PhoneDB);

                    startActivity(intent);
                }


        );
        //onclick to send email
        view.findViewById(R.id.btn_contact).setOnClickListener(v -> sendmail());
        //onclick to open privecy
        view.findViewById(R.id.btn_privacy).setOnClickListener(v -> openprivacyy());
//onclick to share app
        view.findViewById(R.id.btn_Share).setOnClickListener(v -> shareApp());


        //onclick to logout
        view.findViewById(R.id.btn_log_out).setOnClickListener(v -> logout());


    }


    private void logout() {
        if (getActivity() != null)
            new AlertDialog.Builder(getActivity())
                    .setTitle("LogOut")
                    .setMessage("are you sure that you want to logout")
                    .setPositiveButton("yes", (dialog, which) -> {
                        getActivity().getSharedPreferences("Login", MODE_PRIVATE)
                                .edit()
                                .clear()
                                .apply();
                        getActivity().getSharedPreferences("user", MODE_PRIVATE)
                                .edit()
                                .clear()
                                .apply();
                        firebaseAuth.signOut();
                        startActivity(new Intent(getActivity(), Splash.class));
                        getActivity().finish();
                    })
                    .setNegativeButton("no", null)
                    .create().show();

    }

    private void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void openprivacyy() {
        Uri uri = Uri.parse("https://my-coach-0.flycricket.io/privacy.html"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void sendmail() {
        String[] TO = {"mf255579@gmail.com"};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My Coach Trainee Version");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));


        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(),
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void GetUserData() {
        if (getActivity() != null) {

            if (firebaseAuth.getCurrentUser()!= null){



            }


            progressBar.setVisibility(View.VISIBLE);
            String UID = firebaseAuth.getCurrentUser().getUid();
            DocumentReference DB = firestore.collection("users")
                    .document(UID);
            DB.get().addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    assert snapshot != null;
                     NameDB = snapshot.getString("name");
                     ImageDB = snapshot.getString("image");
                     EmaileDB = snapshot.getString("email");
                     PhoneDB = snapshot.getString("phone");

                    UserName.setText("Welcom :" + NameDB);

                    assert ImageDB != null;
                    if (!ImageDB.equals("null")) {

                        Glide.with(getActivity())
                                .load(ImageDB)
                                .placeholder(R.drawable.graduation_project_logo)
                                .into(Profile_image);


                    }

                    progressBar.setVisibility(View.GONE);

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Error in Task" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            });


        }
    }
}