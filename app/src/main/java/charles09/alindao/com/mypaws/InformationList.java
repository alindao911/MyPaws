package charles09.alindao.com.mypaws;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import charles09.alindao.com.mypaws.Registration.LoginActivity;

/**
 * Created by Pc-user on 02/02/2018.
 */

public class InformationList extends RecyclerView.Adapter<InformationList.InformationListViewHolder> {

    private ArrayList<InformationListDetails> list;
    FirebaseAuth mAuth;

    public InformationList() {
        list = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        populateList();
    }

    private void populateList() {
        InformationListDetails set1 = new InformationListDetails();
        set1.setSettingsName("Personal Info");
        list.add(set1);

        InformationListDetails set2 = new InformationListDetails();
        set2.setSettingsName("Policy and Billing Info");
        list.add(set2);

        InformationListDetails set3 = new InformationListDetails();
        set3.setSettingsName("Edit Payment Method");
        list.add(set3);

        InformationListDetails set4 = new InformationListDetails();
        set4.setSettingsName("Change Password");
        list.add(set4);

        InformationListDetails set5 = new InformationListDetails();
        set5.setSettingsName("Log out");
        list.add(set5);
    }

    @Override
    public InformationList.InformationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_account_model, parent, false);
        return new InformationListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InformationList.InformationListViewHolder holder, int position) {
        holder.settingsName.setText(list.get(position).getSettingsName());

        if(position == 0) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), PersonalInfoActivity.class);
                    view.getContext().startActivity(intent);
                }
            });
        }

        if(position == 1) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), PolicyBillingActivity.class);
                    view.getContext().startActivity(intent);
                }
            });
        }

        if(position == 4) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signOut();
                    Intent intent = new Intent(view.getContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class InformationListViewHolder extends RecyclerView.ViewHolder {
        TextView settingsName;
        CardView cardView;
        public InformationListViewHolder(View itemView) {
            super(itemView);
            settingsName = itemView.findViewById(R.id.textviewsettingname);
            cardView = itemView.findViewById(R.id.cardviewpets);
        }
    }
}
