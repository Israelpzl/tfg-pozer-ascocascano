package com.example.leeconmonclick.professional.leeconmonclick.professional.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leeconmonclick.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private BarChart barChart;
    private DatabaseReference databaseReference;

    public ThirdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThirdFragment newInstance(String param1, String param2) {
        ThirdFragment fragment = new ThirdFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third, container, false);
        barChart = view.findViewById(R.id.barChart);
        return view ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

/*      Description description = new Description();
        description.setText("Nº VECES JUGADAS");
        barChart.setDescription(description);*/

        if (getArguments() != null){

            databaseReference = FirebaseDatabase.getInstance().getReference();
            String name = getArguments().getString("namePatient").toLowerCase(Locale.ROOT);

            databaseReference.child("userPatient").child(name).child("stadistic").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    ArrayList<BarEntry> entries = new ArrayList<>();

                    entries.add(new BarEntry(1,snapshot.child("joinWords").child("timesPlayed").getValue(Integer.class)));
                    entries.add(new BarEntry(2,snapshot.child("letters").child("timesPlayed").getValue(Integer.class)));
                    entries.add(new BarEntry(3,snapshot.child("syllables").child("timesPlayed").getValue(Integer.class)));

                    BarDataSet barDataSet = new BarDataSet(entries,"");
                    barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                    BarData barData = new BarData (barDataSet);
                    barChart.setData(barData);

                    String[] labels = new String[]{"","Juego 1", "Juego 2", "Juego 3"};
                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                    xAxis.setGranularity(1f);
                    xAxis.setLabelCount(labels.length);
                    barChart.getDescription().setEnabled(false);
                    barChart.getLegend().setEnabled(false);


                    barChart.invalidate();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }



    }
}