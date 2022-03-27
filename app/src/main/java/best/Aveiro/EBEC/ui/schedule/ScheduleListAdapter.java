package best.Aveiro.EBEC.ui.schedule;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import best.Aveiro.EBEC.Objects.Day;
import best.Aveiro.EBEC.Objects.Event;
import best.Aveiro.EBEC.R;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder> {
    private ArrayList<Event> mEvents;
    private LayoutInflater mInflater;
    private Context mContext;

    public ScheduleListAdapter(Context context, ArrayList<Event> events) {
        mInflater = LayoutInflater.from(context);
        this.mEvents = events;
        this.mContext = context;
        Collections.sort(this.mEvents, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return Integer.compare(e1.order, e2.order);
            }
        });

    }


    @Override
    public ScheduleListAdapter.ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.event_item, parent, false);

        return new ScheduleViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Event mCurrent = mEvents.get(position);
        holder.event_designation.setText(mCurrent.getDesignation());
        holder.event_time.setText(mCurrent.getTime());
        holder.event_description.setText(mCurrent.getDescription());
        holder.event_description_card.setVisibility(View.GONE);
        holder.event_designation_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.event_description_card.getVisibility() == View.GONE)
                    holder.event_description_card.setVisibility(View.VISIBLE);
                else
                    holder.event_description_card.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.mEvents.size();
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {
        public ScheduleListAdapter mAdapter;
        public TextView event_designation, event_time, event_description;
        public CardView event_description_card, event_designation_card;


        public ScheduleViewHolder(@NonNull View itemView, ScheduleListAdapter adapter) {
            super(itemView);

            event_designation = itemView.findViewById(R.id.event_designation);
            event_time = itemView.findViewById(R.id.event_time);
            event_description = itemView.findViewById(R.id.event_description);
            event_description_card = itemView.findViewById(R.id.event_description_card);
            event_designation_card = itemView.findViewById(R.id.event_designation_card);
            this.mAdapter = adapter;
        }
    }
}
