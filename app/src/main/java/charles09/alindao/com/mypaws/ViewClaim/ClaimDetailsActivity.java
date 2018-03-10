package charles09.alindao.com.mypaws.ViewClaim;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import charles09.alindao.com.mypaws.Model.Claim;
import charles09.alindao.com.mypaws.Model.Pet;
import charles09.alindao.com.mypaws.Model.User;
import charles09.alindao.com.mypaws.R;
import charles09.alindao.com.mypaws.Utils.UniversalImageLoader;

public class ClaimDetailsActivity extends AppCompatActivity {
    //Widgets
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ImageView petPhoto;
    private ImageView claimPhotoOne;
    private ImageView claimPhotoTwo;
    private TextView petOwnerName;
    private TextView petOwnerAddress;
    private TextView petOwnerEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_details);
        castWidgets();
        initImageLoader();
        setupToolbar();
        Bundle bundle = getIntent().getExtras();
        String code = bundle.getString("code");
        setupWidgets(code);
    }
    private void setupWidgets(final String code) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Claims");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.getValue(Claim.class).getClaimCode().equalsIgnoreCase(code)) {
                        Claim claim = snapshot.getValue(Claim.class);
                        UniversalImageLoader.setImage(claim.getClaimImageOne(), claimPhotoOne, null, "");
                        UniversalImageLoader.setImage(claim.getClaimImageTwo(), claimPhotoTwo, null, "");
                        setUpPet(claim);
                        setUpOwner();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpOwner() {
        final String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.getValue(User.class).getUserCode().equalsIgnoreCase(uID)) {
                        User user = snapshot.getValue(User.class);
                        petOwnerName.setText(user.getUserFirstName() + " " + user.getUserLastName());
                        petOwnerAddress.setText(user.getUserAddress());
                        petOwnerEmail.setText(user.getUserEmail());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpPet(final Claim claim) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Pets");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.getValue(Pet.class).getPetCode().equalsIgnoreCase(claim.getClaimPetCode())) {
                        UniversalImageLoader.setImage(snapshot.getValue(Pet.class).getPetPhoto(), petPhoto, null, "");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false);
        ab.setHomeAsUpIndicator(R.drawable.ic_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("View Claim");
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
    private void castWidgets() {
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        petPhoto = findViewById(R.id.petphoto);
        claimPhotoOne = findViewById(R.id.imgview1);
        claimPhotoTwo = findViewById(R.id.imgview2);
        petOwnerName = findViewById(R.id.textfullname);
        petOwnerEmail = findViewById(R.id.textemail);
        petOwnerAddress = findViewById(R.id.textaddress);
    }
}
