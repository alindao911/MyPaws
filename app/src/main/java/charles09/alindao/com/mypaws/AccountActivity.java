package charles09.alindao.com.mypaws;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class AccountActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private RecyclerView recyclerViewInformation;
    private RecyclerView recyclerViewPolicies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        castWidgets();
        setupToolbar();
        setupRecyclerView();
    }
    private void castWidgets() {
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        recyclerViewInformation = findViewById(R.id.recyclerinformation);
        recyclerViewPolicies = findViewById(R.id.recyclerpolicy);
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
