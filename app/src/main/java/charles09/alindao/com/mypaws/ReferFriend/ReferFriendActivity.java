package charles09.alindao.com.mypaws.ReferFriend;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import charles09.alindao.com.mypaws.MainActivity;
import charles09.alindao.com.mypaws.Model.Pet;
import charles09.alindao.com.mypaws.NewClaim.SubmitClaim;
import charles09.alindao.com.mypaws.R;
import charles09.alindao.com.mypaws.Utils.UniversalImageLoader;
import charles09.alindao.com.mypaws.Utils.Utility;

public class ReferFriendActivity extends AppCompatActivity {
    //widgets
    private EditText petName;
    private ImageButton petPic;
    private Button btnSubmit;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ProgressDialog progressDialog;

    //Firebase
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    //Declaring variables
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0;
    private int SELECT_FILE = 1;
    private String holderUri;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_friend);
        castWidgets();
        setupToolbar();
        setupFirebase();
        initImageLoader();
        init();
    }

    private void init() {
        petPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPet();
            }
        });
    }

    private void addPet(){
        final String petname = petName.getText().toString();
        final String uID = mUser.getUid();
        if(TextUtils.isEmpty(petname) || !hasImage(petPic)) {
            Toast.makeText(this, "Please input necessary fields", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.setMessage("Registering user...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            StorageReference myRef = storageReference.child(uID).child(uri.getLastPathSegment());
            myRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String downloadPath = taskSnapshot.getDownloadUrl().toString();
                            String petCode = databaseReference.push().getKey();
                            Pet pet = new Pet(petname, downloadPath, uID, petCode, "AC");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(ReferFriendActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                            databaseReference.child(petCode).setValue(pet);
                            progressDialog.hide();
                        }
                    });
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ReferFriendActivity.this);
        builder.setTitle("Choose Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ReferFriendActivity.this);

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
        UniversalImageLoader.setImage(holderUri, petPic, null,"");

    }

    private void onSelectFromGalleryResult(Intent data) {
        uri = data.getData();
        holderUri = uri.toString();
        UniversalImageLoader.setImage(holderUri, petPic, null,"");
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

    private boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable!=null);

        if(hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }
        return hasImage;
    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void setupFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Pets");
        storageReference = FirebaseStorage.getInstance().getReference("PetPhotos");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    private void castWidgets() {
        petName = findViewById(R.id.etpetname);
        petPic = findViewById(R.id.imgAddPic);
        btnSubmit = findViewById(R.id.btnSubmit);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(this);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false);
        ab.setHomeAsUpIndicator(R.drawable.ic_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Refer A Friend");
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        else {
            return true;
        }
    }
}
