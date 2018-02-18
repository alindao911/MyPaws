package charles09.alindao.com.mypaws;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pc-user on 02/02/2018.
 */

public class PolicyList extends RecyclerView.Adapter<PolicyList.InformationListViewHolder> {

    private ArrayList<InformationListDetails> list;

    public PolicyList() {
        list = new ArrayList<>();
        populateList();
    }

    private void populateList() {
        InformationListDetails set1 = new InformationListDetails();
        set1.setSettingsName("Doggo's Policy");
        list.add(set1);

        InformationListDetails set2 = new InformationListDetails();
        set2.setSettingsName("Catto's Policy");
        list.add(set2);
    }

    @Override
    public PolicyList.InformationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_account_model, parent, false);
        return new PolicyList.InformationListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PolicyList.InformationListViewHolder holder, int position) {
        holder.settingsName.setText(list.get(position).getSettingsName());
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

