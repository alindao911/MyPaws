package charles09.alindao.com.mypaws;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import charles09.alindao.com.mypaws.Model.User;

public class RegistrationActivity extends AppCompatActivity {
    private ImageView imgViewProfile;
    private ImageView imgBtnProfile;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextAddress;
    private EditText editTextContact;
    private EditText editTextAge;
    private RadioButton femaleButton;
    private RadioButton maleButton;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private Button registerButton;
    private ProgressDialog progressDialog;

    //Declaring variables
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0;
    private int SELECT_FILE = 1;
    private String holderUri;
    private Uri uri;
    private String userEmail;
    private String userPass;
    private String userFName;
    private String userLName;
    private String userAddress;
    private String userContact;
    private String userAge;
    private String userGender = "";

    //Firebase
    private FirebaseAuth mAuth;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        editTextEmail = findViewById(R.id.etRegEmail);
        editTextPassword = findViewById(R.id.etRegPassword);
        editTextFirstName = findViewById(R.id.etFirstName);
        editTextLastName = findViewById(R.id.etLastName);
        editTextAddress = findViewById(R.id.etRegAddress);
        editTextContact = findViewById(R.id.etRegContactNumber);
        femaleButton = findViewById(R.id.femaleRB);
        maleButton = findViewById(R.id.maleRB);
        editTextAge = findViewById(R.id.etAge);
        registerButton = findViewById(R.id.registerBtn);
        progressDialog = new ProgressDialog(this);
        imgViewProfile = findViewById(R.id.imgViewProfile);
        imgBtnProfile = findViewById(R.id.btnCamera);

        userEmail = editTextEmail.getText().toString();
        userPass = editTextPassword.getText().toString();
        userFName = editTextFirstName.getText().toString();
        userLName = editTextLastName.getText().toString();
        userAddress = editTextAddress.getText().toString();
        userContact = editTextContact.getText().toString();
        userAge = editTextAge.getText().toString();
        userGender = "";

        setupToolbar();
        setupFirebase();
        initImageLoader();
        setImage();
        init();
    }

    private void init() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail = editTextEmail.getText().toString();
                userPass = editTextPassword.getText().toString();
                userFName = editTextFirstName.getText().toString();
                userLName = editTextLastName.getText().toString();
                userAddress = editTextAddress.getText().toString();
                userContact = editTextContact.getText().toString();
                userAge = editTextAge.getText().toString();
                userGender = "";
                if(!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPass) && !TextUtils.isEmpty(userFName)
                        && !TextUtils.isEmpty(userLName) && !TextUtils.isEmpty(userAddress) && !TextUtils.isEmpty(userContact)) {
                    if(hasImage(imgViewProfile)) {
                        registerAccount();
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Pick a photo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegistrationActivity.this, "Input all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerAccount() {
        if(maleButton.isChecked()) {
            userGender = "Male";
        } else if (femaleButton.isChecked()) {
            userGender = "Female";
        }
        progressDialog.setTitle("Register");
        progressDialog.setMessage("Registering user...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        final String finalUserGender = userGender;
        mAuth.createUserWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "Registration Fails", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        else {
                            FirebaseUser user = mAuth.getCurrentUser();
                            final String id = user.getUid();
                            StorageReference myRef = mStorageReference.child(id).child(uri.getLastPathSegment());
                            myRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Uri profile = taskSnapshot.getDownloadUrl();
                                    String profileDownloadUrl = profile.toString();
                                    User user = new User(userEmail, userPass, userFName, userLName, userAddress, userContact, Integer.parseInt(userAge), finalUserGender, profileDownloadUrl);
                                    mDatabaseReference.child(id).setValue(user);
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }
                });
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("User");
        mStorageReference = FirebaseStorage.getInstance().getReference("Photos");
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false);
        ab.setHomeAsUpIndicator(R.drawable.ic_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Register Account");
    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void setImage() {
        imgBtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setTitle("Choose Profile Picture");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(RegistrationActivity.this);

                if(items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if(result) {
                        cameraIntent();
                    }
                }
                else if(items[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Gallery";
                    if(result) {
                        galleryIntent();
                    }
                } else if(items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    //PERMISSION FOR CAMERA AND GALLERY
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo")) {
                        cameraIntent();
                    } else if(userChoosenTask.equals("Choose from Gallery")) {
                        galleryIntent();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if(requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        uri = data.getData();
        holderUri = uri.toString();
        UniversalImageLoader.setImage(holderUri, imgViewProfile, null, "");
        imgViewProfile.setBackgroundResource(0);

    }

    private void onSelectFromGalleryResult(Intent data) {
        uri = data.getData();
        holderUri = uri.toString();
        Log.d("REGISTER ACTIVITY", "onSelectFromGalleryResult" + holderUri);
        UniversalImageLoader.setImage(holderUri, imgViewProfile, null, "");
        imgViewProfile.setBackgroundResource(0);
    }

    private boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable!=null);

        if(hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }
        return hasImage;
    }
}
