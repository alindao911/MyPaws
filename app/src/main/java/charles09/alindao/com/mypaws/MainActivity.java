package charles09.alindao.com.mypaws;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //widgets declaration
    private ImageView imgViewNewClaim;
    private ImageView imgViewViewClaim;
    private ImageView imgViewHelpFaq;
    private ImageView imgViewMyAccount;
    private ImageView imgViewTheScoop;
    private ImageView imgViewRefer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //cast widgets method
        castWidgets();

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

    }

}
