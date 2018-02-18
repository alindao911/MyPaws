package charles09.alindao.com.mypaws;

import android.app.Activity;
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
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

public class SubmitClaim extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ImageView imgViewPetPic;
    private TextView txtViewPetName;
    private CardView submitClaim;
    private LinearLayout btnCamera;
    private ImageView imgViewOne;
    private ImageView imgViewTwo;

    private String petName;
    private int petImage;
    //Declaring variables
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0;
    private int SELECT_FILE = 1;
    private String holderUri;
    private int holder = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_claim);
        castWidgets();
        setupToolbar();
        setupWidgets();
        initImageLoader();
        init();
    }
    private void castWidgets() {
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        imgViewPetPic = findViewById(R.id.imgviewpetpic);
        txtViewPetName = findViewById(R.id.textviewclaimfor);
        submitClaim = findViewById(R.id.submitclaim);
        btnCamera = findViewById(R.id.camerabtn);
        imgViewOne = findViewById(R.id.imgview1);
        imgViewTwo = findViewById(R.id.imgview2);
    }
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false);
        ab.setHomeAsUpIndicator(R.drawable.ic_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("New Claim");
    }
    private void setupWidgets() {
        Bundle bundle = getIntent().getExtras();
        petName = bundle.getString("petname");
        petImage = bundle.getInt("petphoto");

        imgViewPetPic.setImageResource(petImage);
        txtViewPetName.setText("Claim for " + petName);

    }
    private void init() {
        submitClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VerifyClaim.class);
                intent.putExtra("petname", petName);
                intent.putExtra("petimage", petImage);
                startActivity(intent);
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SubmitClaim.this);
        builder.setTitle("Choose Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(SubmitClaim.this);

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
                holder++;
            } else if(requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
                holder++;
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Uri uri = data.getData();
        holderUri = uri.toString();

        if(hasImage(imgViewOne) && !hasImage(imgViewTwo)) {
            UniversalImageLoader.setImage(holderUri, imgViewTwo, null, "");
        }
        else if(!hasImage(imgViewOne) && hasImage(imgViewTwo)) {
            UniversalImageLoader.setImage(holderUri, imgViewOne, null, "");
        }
        else if(!hasImage(imgViewOne) && !hasImage(imgViewTwo)) {
            UniversalImageLoader.setImage(holderUri, imgViewOne, null, "");
        }
        else if(hasImage(imgViewOne) && hasImage(imgViewTwo) && holder%2==0) {
            UniversalImageLoader.setImage(holderUri, imgViewOne, null, "");
        }
        else {
            UniversalImageLoader.setImage(holderUri, imgViewTwo, null, "");
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Uri uri = data.getData();
        holderUri = uri.toString();
        if(hasImage(imgViewOne) && !hasImage(imgViewTwo)) {
            UniversalImageLoader.setImage(holderUri, imgViewTwo, null, "");
        }
        else if(!hasImage(imgViewOne) && hasImage(imgViewTwo)) {
            UniversalImageLoader.setImage(holderUri, imgViewOne, null, "");
        }
        else if(!hasImage(imgViewOne) && !hasImage(imgViewTwo)) {
            UniversalImageLoader.setImage(holderUri, imgViewOne, null, "");
        }
        else if(hasImage(imgViewOne) && hasImage(imgViewTwo) && holder%2==0) {
            UniversalImageLoader.setImage(holderUri, imgViewOne, null, "");
        }
        else {
            UniversalImageLoader.setImage(holderUri, imgViewTwo, null, "");
        }
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

}
