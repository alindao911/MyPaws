package charles09.alindao.com.mypaws;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import charles09.alindao.com.mypaws.NewClaim.SubmitClaim;

/**
 * Created by Pc-user on 02/02/2018.
 */

public class PetList extends RecyclerView.Adapter<PetList.PetListViewHolder> {
    private ArrayList<PetListDetails> pet;

    public PetList() {
        pet = new ArrayList<>();
        populateList();
    }

    private void populateList() {
        PetListDetails pet1 = new PetListDetails();
        pet1.setName("Doggo");
        pet1.setImage(R.drawable.images);
        pet.add(pet1);
    }

    @Override
    public PetList.PetListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_model_pet, parent, false);
        return new PetListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PetList.PetListViewHolder holder, final int position) {
        holder.txtPetName.setText(pet.get(position).getName());
        holder.imgViewPetPic.setImageResource(Integer.parseInt("" + pet.get(position).getImage()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SubmitClaim.class);
                intent.putExtra("petname", pet.get(position).getName());
                intent.putExtra("petphoto", pet.get(position).getImage());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pet.size();
    }

    protected class PetListViewHolder extends RecyclerView.ViewHolder {
        TextView txtPetName;
        ImageView imgViewPetPic;
        CardView cardView;
        public PetListViewHolder(View itemView) {
            super(itemView);
            txtPetName = itemView.findViewById(R.id.textviewpetname);
            imgViewPetPic = itemView.findViewById(R.id.imgviewpetpic);
            cardView = itemView.findViewById(R.id.cardviewpets);
        }
    }
}
