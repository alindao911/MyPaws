package charles09.alindao.com.mypaws;

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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import charles09.alindao.com.mypaws.Model.Pet;
import charles09.alindao.com.mypaws.Model.User;
import charles09.alindao.com.mypaws.NewClaim.NewClaim;
import charles09.alindao.com.mypaws.NewClaim.SubmitClaim;
import charles09.alindao.com.mypaws.Utils.UniversalImageLoader;

public class PersonalInfoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private RecyclerView recyclerView;
    private TextView textName;
    private TextView textAddress;
    private TextView textEmail;
    private TextView textContact;
    private TextView textUsername;
    private ImageView imgViewProfile;

    //
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Pet, ShowViewHolder> firebaseRecyclerAdapter;
    private DatabaseReference recyclerRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        castWidgets();
        initImageLoader();
        setupToolbar();
        setupRecyclerView();
        setupFirebase();
        populateRecyclerView();
    }
    private void setupFirebase() {
        recyclerRef = FirebaseDatabase.getInstance().getReference("Pets");
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.getValue(User.class).getUserCode().equalsIgnoreCase(id)) {
                        User user = snapshot.getValue(User.class);
                        setupProfile(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setupProfile(User user)
    {
        textName.setText(user.getUserFirstName() + " " + user.getUserLastName());
        textAddress.setText(user.getUserAddress());
        textUsername.setText(user.getUserFirstName());
        textContact.setText(user.getUserContact());
        textEmail.setText(user.getUserEmail());
        UniversalImageLoader.setImage(user.getUserPhoto(), imgViewProfile, null, "");
    }
    private void castWidgets()
    {
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview);
        textName = findViewById(R.id.textFullname);
        textAddress = findViewById(R.id.textAddress);
        textEmail = findViewById(R.id.textEmail);
        textContact = findViewById(R.id.textContact);
        textUsername = findViewById(R.id.textUsername);
        imgViewProfile = findViewById(R.id.imgviewpic);
    }
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false);
        ab.setHomeAsUpIndicator(R.drawable.ic_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Personal Information");
    }
    private void populateRecyclerView() {
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Pet, ShowViewHolder>
                (Pet.class, R.layout.layout_model_pet, ShowViewHolder.class, recyclerRef) {
            @Override
            protected void populateViewHolder(ShowViewHolder viewHolder, final Pet model, int position) {
                String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
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

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
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
