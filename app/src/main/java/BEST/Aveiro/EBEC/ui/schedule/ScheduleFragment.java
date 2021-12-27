package best.Aveiro.EBEC.ui.schedule;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import best.Aveiro.EBEC.Objects.Day;
import best.Aveiro.EBEC.Objects.Event;
import best.Aveiro.EBEC.R;

public class ScheduleFragment extends Fragment {

    private ScheduleViewModel mViewModel;
    private RecyclerView events_recycler_view;
    private ScheduleListAdapter mAdapter;
    private TextView selected_day;
    TabLayout day_picker;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ScheduleViewModel.class);
        View v = inflater.inflate(R.layout.schedule_fragment, container, false);
        day_picker = v.findViewById(R.id.schedule_day_picker);
        selected_day = v.findViewById(R.id.selected_day);
        events_recycler_view = (RecyclerView)v.findViewById(R.id.events_recycler_view);


        mViewModel.getDays().observe(getViewLifecycleOwner(), days->{
            day_picker.removeAllTabs();
            for (Day d: days){

                day_picker.addTab(day_picker.newTab().setText(d.getDayDesignation()));
            }

        });
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference schedule = db.child("schedule");
        schedule.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Day> days = new ArrayList<>();
                        for (DataSnapshot day_snapshot: snapshot.getChildren()){
                            Day d = new Day();
                            d.setDayDesignation(day_snapshot.getKey());
                            d.setDate(day_snapshot.child("date").getValue(String.class));

                            ArrayList<Event> events_list = new ArrayList<>();
                            for (DataSnapshot event: day_snapshot.child("events").getChildren()){
                                Event e = new Event();

                                e.setOrder(event.child("order").getValue(Integer.class));

                                e.setDesignation(event.getKey());
                                e.setTime(event.child("time").getValue(String.class));
                                e.setDescription(event.child("description").getValue(String.class));
                                events_list.add(e);
                            }
                            d.setEvents(events_list);

                            mViewModel.addDay(d);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );




        day_picker.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                selected_day.setText(mViewModel.getDays().getValue().get(tab.getPosition()).getDate());
                ScheduleListAdapter new_adapter = new ScheduleListAdapter(getActivity().getApplicationContext(), mViewModel.getDays().getValue().get(tab.getPosition()).getEvents());
                events_recycler_view.setAdapter(new_adapter);
                events_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });

        return v;
    }



}