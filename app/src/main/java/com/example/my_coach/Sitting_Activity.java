package com.example.my_coach;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.my_coach.ui.Splash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Sitting_Activity extends AppCompatActivity {

    //view
    private CircleImageView UserImage;
    private TextView UserName;
    private TextView UserEmail;
    private TextView UserPhone;
    private TextView selectedLang;
    private String lang;
    private ImageView Update_user_image;

    //update image
    private Uri imageUri = null;
    private static String ImageURL = null;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private String userID;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitting_);

        //find view
        UserImage=findViewById(R.id.User_image_Sitting);
        UserName=findViewById(R.id.User_Name_Siting);
        UserEmail=findViewById(R.id.User_Email_Sitting);
        UserPhone=findViewById(R.id.User_Phone_Sitting);
        selectedLang=findViewById(R.id.selectedLang);
        Update_user_image=findViewById(R.id.Update_user_image);

        //firestor
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        String CurrentLang = Locale.getDefault().getDisplayLanguage();
         lang = getSharedPreferences("language",MODE_PRIVATE)
                .getString("lang",CurrentLang);

        if (lang.equals("ar"))
        selectedLang.setText("عربي");
        else
            selectedLang.setText("English");

        //intent to get back to profile

        findViewById(R.id.back_Sitting).setOnClickListener(v -> finish());

        GetIntentData();
        findViewById(R.id.btn_Language).setOnClickListener(v -> {

            showAlertLanguage();

        });
        Update_user_image.setOnClickListener(v -> {
      //updatUserImage
            checkPermission();
        });
    }
    private void showAlertLanguage() {
        AlertDialog alert=new AlertDialog.Builder(this).create();
        alert.setCancelable(false);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_lanuage,null);
        alert.setView(view);
         ImageView  cancel_lang=view.findViewById(R.id.exit_lang);
        cancel_lang.setOnClickListener(v -> {
            alert.dismiss();
        });
        RadioButton radio_ar= view.findViewById(R.id.rb_arabic);
        RadioButton radio_en= view.findViewById(R.id.rb_english);

        if (lang.equals("ar")) {
            radio_ar.setChecked(true);
            radio_en.setChecked(false);

        } else{
            radio_en.setChecked(false);
            radio_en.setChecked(true);
            }
        view.findViewById(R.id.save_lang).setOnClickListener(v -> {
            if (radio_en.isChecked())
                saveLanguage("en");
            else if (radio_ar.isChecked())
                saveLanguage("ar");
        });

        alert.show();
    }
    void saveLanguage(String lang){

        getSharedPreferences("language",MODE_PRIVATE)
                .edit()
                .putString("lang",lang)
                .apply();
        //language
        Locale l =new Locale(lang);
        Locale.setDefault(l);
        Configuration configuration=new Configuration();
        configuration.locale=l;
        getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());

        Intent intent = new Intent(Sitting_Activity.this, Splash.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        Update_user_image.setOnClickListener(v -> {
            //UplodImage


        });
    }

    private void GetIntentData(){

        Intent intent= getIntent();

        String name=intent.getStringExtra("name");
        String image=intent.getStringExtra("image");
        String email=intent.getStringExtra("email");
        String phone=intent.getStringExtra("phone");

        assert image != null;
        SetuserData(name,image,email,phone);
    }

    private void SetuserData(String name, String image, String email, String phone) {

        UserName.setText(name);
        UserEmail.setText(email);
        UserPhone.setText(phone);
        if (!image.equals("null")) {

            Glide.with(this)
                    .load(image)
                    .placeholder(R.drawable.graduation_project_logo)
                    .into(UserImage);


        }

    }

    private void checkPermission() {

        //use permission to READ_EXTERNAL_STORAGE For Device >= Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


                // to ask user to reade external storage
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            } else {
                OpenGalleryImagePicker();
            }

            //implement code for device < Marshmallow
        } else {

            OpenGalleryImagePicker();
        }
    }
    private void OpenGalleryImagePicker() {
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                imageUri = result.getUri();

                // set image user in ImageView ;
                UserImage.setImageURI(imageUri);

                uploadImage();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(this, "Error : " + error, Toast.LENGTH_LONG).show();

            }
        }
    }
    private void uploadImage() {

        if (firebaseAuth.getCurrentUser() != null) {

            // chick if user image is null or not
            if (imageUri != null) {

                userID = firebaseAuth.getCurrentUser().getUid();

                // mack progress bar dialog
                 progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("UpLoading...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                // mack collection in fireStorage
                final StorageReference ref = storageReference.child("profile_image_user").child(userID + ".jpg");

                // get image user and give to imageUserPath
                ref.putFile(imageUri).addOnProgressListener(taskSnapshot -> {

                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("upload " + (int) progress + "%");

                }).continueWithTask(task -> {
                    if (!task.isSuccessful()) {

                        throw task.getException();

                    }
                    return ref.getDownloadUrl();

                }).addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        progressDialog.dismiss();
                        Uri downloadUri = task.getResult();

                        assert downloadUri != null;
                        ImageURL = downloadUri.toString();
                        saveChange();

                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(this, " Error in addOnCompleteListener " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
    }

    private void saveChange() {

        if (firebaseAuth.getCurrentUser() != null){

            String userID=firebaseAuth.getCurrentUser().getUid();

            Map<String, Object> user = new HashMap<>();
            user.put("image",ImageURL);
            firestore.collection("users")
                    .document(userID)
                    .update(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(Sitting_Activity.this, "image upload", Toast.LENGTH_SHORT).show();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(Sitting_Activity.this, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}