package charles09.alindao.com.mypaws;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import charles09.alindao.com.mypaws.Model.Pet;
import charles09.alindao.com.mypaws.Model.User;
import charles09.alindao.com.mypaws.NewClaim.NewClaim;
import charles09.alindao.com.mypaws.ReferFriend.ReferFriendActivity;
import charles09.alindao.com.mypaws.Utils.UniversalImageLoader;
import charles09.alindao.com.mypaws.Utils.ViewPagerAdapter;
import charles09.alindao.com.mypaws.ViewClaim.ViewClaimActivity;

public class MainActivity extends AppCompatActivity {
    //widgets declaration
    private ImageView imgViewNewClaim;
    private ImageView imgViewViewClaim;
    private ImageView imgViewHelpFaq;
    private ImageView imgViewMyAccount;
    private ImageView imgViewTheScoop;
    private ImageView imgViewRefer;
    private TextView textName;

    //Firebase
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    //Variables
    private String uID;
    private User user;
    private ViewPager adsPager;
    private StringBuilder stringBuilder;
    private StringBuilder stringBuilderTwo;
    private int dotscount;
    private ImageView[] dots;
    private LinearLayout sliderDotsPanel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //cast widgets method
        castWidgets();
        setupFirebase();
        setupViewPager();
        initImageLoader();

        imgViewNewClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("NEW CLAIM", "INTENT");
                Intent intent = new Intent(getApplicationContext(), NewClaim.class);
                startActivity(intent);
            }
        });

        imgViewViewClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("VIEW CLAIM","INTENT");
                Intent intent = new Intent(getApplicationContext(), ViewClaimActivity.class);
                startActivity(intent);
            }
        });

        imgViewHelpFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HELP & FAQ","INTENT");
            }
        });

        imgViewMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MY ACCOUNT", "INTENT");
                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent);
            }
        });

        imgViewTheScoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("THE SCOOP", "INTENT");
                Intent intent = new Intent(getApplicationContext(), ScoopActivity.class);
                startActivity(intent);
            }
        });

        imgViewRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("REFER A FRIEND","INTENT");
                Intent intent = new Intent(getApplicationContext(), ReferFriendActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupViewPager() {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        Query query = dbref.child("Pets");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getValue(Pet.class).getPetOwnerCode().equalsIgnoreCase(id)) {
                        stringBuilder.append(snapshot.getValue(Pet.class).getPetPhoto());
                        stringBuilder.append(",");
                        stringBuilderTwo.append(snapshot.getValue(Pet.class).getPetName());
                        stringBuilderTwo.append(",");
                    }
                }
                String[] name = stringBuilderTwo.toString().split(",");
                String[] array = stringBuilder.toString().split(",");
                ViewPagerAdapter adsPagerAdapter = new ViewPagerAdapter(getApplicationContext(), array, name);
                adsPager.setAdapter(adsPagerAdapter);
                dotscount = adsPagerAdapter.getCount();
                dots = new ImageView[dotscount];

                for(int k = 0; k < dotscount; k++) {
                    dots[k] = new ImageView(getApplicationContext());
                    dots[k].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 0, 8, 0);
                    sliderDotsPanel.addView(dots[k], params);
                }

                dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                adsPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        for(int i = 0; i < dotscount; i++) {
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                        }
                        dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void castWidgets() {
        imgViewNewClaim = findViewById(R.id.imgviewnewclaim);
        imgViewViewClaim = findViewById(R.id.imgviewviewclaim);
        imgViewHelpFaq = findViewById(R.id.imgviewhelpandfaq);
        imgViewMyAccount = findViewById(R.id.imgviewmyaccount);
        imgViewTheScoop = findViewById(R.id.imgviewthescoop);
        imgViewRefer = findViewById(R.id.imgviewrefer);
        textName = findViewById(R.id.textname);
        adsPager = findViewById(R.id.ads_pager);
        sliderDotsPanel = findViewById(R.id.sliderdots);
        stringBuilder = new StringBuilder();
        stringBuilderTwo = new StringBuilder();

    }

    private void setupFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        uID = FirebaseAuth.getInstance().getCurrentUser().toString();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id = FirebaseAuth.getInstance().getCurrentUser().toString();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.getValue(User.class).getUserCode().equalsIgnoreCase(id)) {
                        user = snapshot.getValue(User.class);
                        setUserName(user);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void setUserName(User user) {
        textName.setText("Welcome " + user.getUserFirstName());
    }

}
