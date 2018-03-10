package charles09.alindao.com.mypaws.ViewClaim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import charles09.alindao.com.mypaws.Model.Claim;
import charles09.alindao.com.mypaws.Model.Pet;
import charles09.alindao.com.mypaws.NewClaim.NewClaim;
import charles09.alindao.com.mypaws.R;
import charles09.alindao.com.mypaws.Utils.UniversalImageLoader;

/**
 * Created by sharls on 3/8/2018.
 */

public class ViewClaimActivity extends AppCompatActivity {
    //widgets
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private RecyclerView recyclerView;

    //Firebase
    private DatabaseReference myRef;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseRecyclerAdapter<Claim, ShowClaim> firebaseRecyclerAdapter;
    private DatabaseReference recyclerRef;
    private FirebaseAuth mAuth;

    //Variables
    private String uID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_claim);
        castWidgets();
        setupToolbar();
        initImageLoader();
        setupFirebase();
        setupRecyclerView();
        populateRecyclerView();
    }



    private void populateRecyclerView() {
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Claim, ShowClaim>
                (Claim.class, R.layout.layout_model_pet, ShowClaim.class, recyclerRef) {
            @Override
            protected void populateViewHolder(ShowClaim viewHolder, final Claim model, int position) {
                if(model.getClaimOwnerCode().equalsIgnoreCase(uID)) {
                    setUpPet(model, viewHolder);
                } else {
                    viewHolder.cardView.setVisibility(View.GONE);
                }
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), ClaimDetailsActivity.class);
                        intent.putExtra("code", model.getClaimCode());
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void setUpPet(final Claim claim, final ShowClaim viewHolder) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Pets");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.getValue(Pet.class).getPetCode().equalsIgnoreCase(claim.getClaimPetCode())) {
                        setupWidgets(snapshot.getValue(Pet.class), viewHolder);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

    private void setupWidgets(Pet pet, ShowClaim viewHolder) {
        viewHolder.petName.setText("View " + pet.getPetName() + "'s claim");
        UniversalImageLoader.setImage(pet.getPetPhoto(), viewHolder.petPic, null, "");
    }

    private void setupRecyclerView() {
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(1, GridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void castWidgets() {
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.viewclaimsrecycler);
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

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        uID = mAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        recyclerRef = firebaseDatabase.getReference("Claims");
    }

    private static class ShowClaim extends RecyclerView.ViewHolder {
        private final ImageView petPic;
        private final TextView petName;
        private final CardView cardView;

        public ShowClaim(View itemView) {
            super(itemView);
            petPic = itemView.findViewById(R.id.imgviewpetpic);
            petName = itemView.findViewById(R.id.textviewpetname);
            cardView = itemView.findViewById(R.id.cardviewpets);
        }
        private void setPetName(String text) { petName.setText(text); }
        private void setPetPic(String text) {
            UniversalImageLoader.setImage(text, petPic, null, "");
        }
    }
}
