package charles09.alindao.com.mypaws.NewClaim;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nostra13.universalimageloader.core.ImageLoader;

import charles09.alindao.com.mypaws.Model.Pet;
import charles09.alindao.com.mypaws.PetList;
import charles09.alindao.com.mypaws.R;
import charles09.alindao.com.mypaws.Utils.UniversalImageLoader;

public class NewClaim extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private RecyclerView recyclerView;

    //Firebase Database
    private DatabaseReference myRef;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseRecyclerAdapter<Pet, ShowViewHolder> firebaseRecyclerAdapter;
    private DatabaseReference recyclerRef;
    private FirebaseAuth mAuth;

    //vars
    private String uID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_claim);
        castWidgets();
        setupToolbar();
        initImageLoader();
        setupFirebase();
        setupRecyclerView();
        populateRecyclerView();
    }
    private void populateRecyclerView() {
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Pet, ShowViewHolder>
                (Pet.class, R.layout.layout_model_pet, ShowViewHolder.class, recyclerRef) {
            @Override
            protected void populateViewHolder(ShowViewHolder viewHolder, final Pet model, int position) {
                if(model.getPetOwnerCode().equalsIgnoreCase(uID)) {
                    viewHolder.setPetName(model.getPetName());
                    viewHolder.setPetPic(model.getPetPhoto());
                } else {
                    viewHolder.cardView.setVisibility(View.GONE);
                }
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), SubmitClaim.class);
                        intent.putExtra("petname", model.getPetName());
                        intent.putExtra("petphoto", model.getPetPhoto());
                        intent.putExtra("petcode", model.getPetCode());
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        uID = mAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        recyclerRef = firebaseDatabase.getReference("Pets");
    }

    private void castWidgets() {
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview);
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

    private void setupRecyclerView() {
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(1, GridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(mLayoutManager);
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

    private static class ShowViewHolder extends RecyclerView.ViewHolder {
        private final ImageView petPic;
        private final TextView petName;
        private final CardView cardView;

        public ShowViewHolder(View itemView) {
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
