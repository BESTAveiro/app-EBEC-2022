package best.Aveiro.EBEC.ui.team_details;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;

import best.Aveiro.EBEC.Objects.Prova;
import best.Aveiro.EBEC.Objects.Team;
import best.Aveiro.EBEC.R;


public class TeamProvasAdapter extends RecyclerView.Adapter<TeamProvasAdapter.ProvasViewHolder> {
    private ArrayList<Prova> mProvas;
    private LayoutInflater mInflater;
    private String mTeamName;
    private Context mContext;

    public TeamProvasAdapter(Context context, ArrayList<Prova> provas) {
        mInflater = LayoutInflater.from(context);
        this.mProvas = provas;
        this.mContext = context;

    }


    @Override
    public ProvasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView;
        mItemView = mInflater.inflate(R.layout.prova_item, parent, false);

        return new ProvasViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProvasViewHolder holder, int position) {
        Prova currentProva = this.mProvas.get(position);
        holder.mProvaName.setText(currentProva.getName());
        holder.mScore.setText(currentProva.getScore());

    }



    @Override
    public int getItemCount() {
        return this.mProvas.size();
    }

    public class ProvasViewHolder extends RecyclerView.ViewHolder {
        public TeamProvasAdapter mAdapter;
        private TextView mProvaName, mScore;
        public ProvasViewHolder(@NonNull View itemView, TeamProvasAdapter adapter) {
            super(itemView);
            this.mAdapter = adapter;
            mProvaName = itemView.findViewById(R.id.prova_name);
            mScore = itemView.findViewById(R.id.score_text);

        }
    }
}
