package charles09.alindao.com.mypaws.NewClaim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;

import charles09.alindao.com.mypaws.MainActivity;
import charles09.alindao.com.mypaws.Model.Claim;
import charles09.alindao.com.mypaws.R;
import charles09.alindao.com.mypaws.ReferFriend.ReferFriendActivity;
import charles09.alindao.com.mypaws.Utils.UniversalImageLoader;

public class VerifyClaim extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ImageView imgViewPetPic;
    private TextView txtViewPetName;
    private Button btnDisagree;
    private Button btnAgree;
    private ProgressDialog progressDialog;

    //vars
    private String petCode;
    private Bitmap imageOne;
    private Bitmap imageTwo;
    private String uID;

    //Firebase
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_claim);
        castWidgets();
        initImageLoader();
        setupFirebase();
        setupToolbar();
        setupWidgets();
        init();
    }

    private void init() {
        btnDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(VerifyClaim.this, "Cannot proceed new claim, need to agree with the terms", Toast.LENGTH_SHORT).show();
            }
        });
        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewClaim();
            }
        });
    }

    private void setupFirebase() {
        mStorage = FirebaseStorage.getInstance().getReference("ClaimPhotos");
        mDatabase = FirebaseDatabase.getInstance().getReference("Claims");
    }

    private void addNewClaim() {
        progressDialog.setMessage("Adding new claim...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageOne.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference myRef = mStorage.child(uID).child("ImageOne"+uID);
        myRef.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final String downloadPath = taskSnapshot.getDownloadUrl().toString();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imageTwo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        StorageReference myRef1 = mStorage.child(uID).child("ImageOne"+uID);
                        myRef1.putBytes(data)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        String downloadPath2 = taskSnapshot.getDownloadUrl().toString();
                                        String claimCode = mDatabase.push().getKey();
                                        Claim claim = new Claim(claimCode, petCode, downloadPath, downloadPath2, "AC", uID);
                                        mDatabase.child(claimCode).setValue(claim);
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        Toast.makeText(VerifyClaim.this, "Successfully added", Toast.LENGTH_SHORT).show();
                                        progressDialog.hide();

                                    }
                                });
                    }
                });
    }

    private void castWidgets() {
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        imgViewPetPic = findViewById(R.id.imgviewpetpic);
        txtViewPetName = findViewById(R.id.textviewclaimfor);
        btnAgree = findViewById(R.id.agreeBtn);
        btnDisagree = findViewById(R.id.disagreeBtn);
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
        toolbarTitle.setText("Verify");
    }
    private void setupWidgets() {
        Bundle bundle = getIntent().getExtras();
        String petName = bundle.getString("petname");
        String petImage = bundle.getString("petimage");
        imageOne = bundle.getParcelable("ImageOne");
        imageTwo = bundle.getParcelable("ImageTwo");
        petCode = bundle.getString("petcode");
        uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        UniversalImageLoader.setImage(petImage, imgViewPetPic, null, "");
        txtViewPetName.setText("Claim for " + petName);
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
    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }
}
