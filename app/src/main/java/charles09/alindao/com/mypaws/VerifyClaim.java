package charles09.alindao.com.mypaws;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class VerifyClaim extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ImageView imgViewPetPic;
    private TextView txtViewPetName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_claim);
        castWidgets();
        setupToolbar();
        setupWidgets();
    }
    private void castWidgets() {
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        imgViewPetPic = findViewById(R.id.imgviewpetpic);
        txtViewPetName = findViewById(R.id.textviewclaimfor);
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
        int petImage = bundle.getInt("petimage");

        imgViewPetPic.setImageResource(petImage);
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
}
