package charles09.alindao.com.mypaws;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import charles09.alindao.com.mypaws.Model.User;
import charles09.alindao.com.mypaws.Utils.UniversalImageLoader;

public class AccountActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private RecyclerView recyclerViewInformation;
    private RecyclerView recyclerViewPolicies;
    private TextView textFullname;
    private TextView textAddress;
    private TextView textEmail;
    private ImageView imgViewProfilePic;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        castWidgets();
        setupToolbar();
        setupRecyclerView();
        setupFirebase();
        retrieveData();
    }
    private void retrieveData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id = mAuth.getCurrentUser().getUid();
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
    private void setupProfile(User user) {
        textAddress.setText(user.getUserAddress());
        textFullname.setText(user.getUserFirstName() + " " + user.getUserLastName());
        textEmail.setText(user.getUserEmail());
        UniversalImageLoader.setImage(user.getUserPhoto(), imgViewProfilePic, null, "");
    }
    private void castWidgets() {
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        recyclerViewInformation = findViewById(R.id.recyclerinformation);
        recyclerViewPolicies = findViewById(R.id.recyclerpolicy);
        textFullname = findViewById(R.id.textfullname);
        textEmail = findViewById(R.id.textemail);
        textAddress = findViewById(R.id.textaddress);
        imgViewProfilePic = findViewById(R.id.imgviewprofpic);
    }
    private void setupFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        mAuth = FirebaseAuth.getInstance();
    }
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false);
        ab.setHomeAsUpIndicator(R.drawable.ic_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Account");
    }
    private void setupRecyclerView() {
        recyclerViewInformation.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewInformation.setAdapter(new InformationList());

        recyclerViewPolicies.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPolicies.setAdapter(new PolicyList());
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
