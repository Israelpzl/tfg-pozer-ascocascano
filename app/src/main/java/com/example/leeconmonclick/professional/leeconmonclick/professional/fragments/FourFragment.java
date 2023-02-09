package com.example.leeconmonclick.professional.leeconmonclick.professional.fragments;

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
 * Use the {@link FourFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FourFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private BarChart barChartfDifficulty, barChartCategory;
    private DatabaseReference databaseReference;

    public FourFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FourFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FourFragment newInstance(String param1, String param2) {
        FourFragment fragment = new FourFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_four, container, false);
        barChartCategory = view.findViewById(R.id.barChart1);
        barChartfDifficulty = view.findViewById(R.id.barChart2);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (getArguments() != null){


            databaseReference = FirebaseDatabase.getInstance().getReference();
            String name = getArguments().getString("namePatient").toLowerCase(Locale.ROOT);

            databaseReference.child("userPatient").child(name).child("stadistic").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    int jh = snapshot.child("joinWords").child("categories").child("Hogar").child("timesPlayed").getValue(Integer.class);
                    int ja = snapshot.child("joinWords").child("categories").child("Animales").child("timesPlayed").getValue(Integer.class);
                    int jc = snapshot.child("joinWords").child("categories").child("Comidas").child("timesPlayed").getValue(Integer.class);

                    int lh = snapshot.child("letters").child("categories").child("Hogar").child("timesPlayed").getValue(Integer.class);
                    int la = snapshot.child("letters").child("categories").child("Animales").child("timesPlayed").getValue(Integer.class);
                    int lc = snapshot.child("letters").child("categories").child("Comidas").child("timesPlayed").getValue(Integer.class);

                    int jf = snapshot.child("joinWords").child("difficulties").child("FÁCIL").child("timesPlayed").getValue(Integer.class);
                    int jn = snapshot.child("joinWords").child("difficulties").child("NORMAL").child("timesPlayed").getValue(Integer.class);
                    int jd = snapshot.child("joinWords").child("difficulties").child("DIFÍCIL").child("timesPlayed").getValue(Integer.class);
                    int jp = snapshot.child("joinWords").child("difficulties").child("PRÁCTICA").child("timesPlayed").getValue(Integer.class);


                    int lf = snapshot.child("letters").child("difficulties").child("FÁCIL").child("timesPlayed").getValue(Integer.class);
                    int ln = snapshot.child("letters").child("difficulties").child("NORMAL").child("timesPlayed").getValue(Integer.class);
                    int ld = snapshot.child("letters").child("difficulties").child("DIFÍCIL").child("timesPlayed").getValue(Integer.class);
                    int lp = snapshot.child("letters").child("difficulties").child("PRÁCTICA").child("timesPlayed").getValue(Integer.class);


                    //BarChart Category

                    ArrayList<BarEntry> entriesC = new ArrayList<>();

                    entriesC.add(new BarEntry(1,jh+lh));
                    entriesC.add(new BarEntry(2,jc+lc));
                    entriesC.add(new BarEntry(3,ja+la));
                    entriesC.add(new BarEntry(4,0));

                    BarDataSet barDataSetC = new BarDataSet(entriesC,"");
                    barDataSetC.setColors(ColorTemplate.COLORFUL_COLORS);

                    BarData barDataC = new BarData (barDataSetC);
                    barChartCategory.setData(barDataC);

                    String[] labelsC = new String[]{"","Hogar", "Comidas", "Animales","Personalizado"};
                    XAxis xAxisC = barChartCategory.getXAxis();
                    xAxisC.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxisC.setValueFormatter(new IndexAxisValueFormatter(labelsC));
                    xAxisC.setGranularity(1f);
                    xAxisC.setLabelCount(labelsC.length);
                    barChartCategory.getDescription().setEnabled(false);
                    barChartCategory.getLegend().setEnabled(false);


                    barChartCategory.invalidate();

                    //BarChartDifficulty
                    ArrayList<BarEntry> entriesD = new ArrayList<>();

                    entriesD.add(new BarEntry(1,jf+lf));
                    entriesD.add(new BarEntry(2,jn+ln));
                    entriesD.add(new BarEntry(3,jd+ld));
                    entriesD.add(new BarEntry(4,jp+lp));

                    BarDataSet barDataSetD = new BarDataSet(entriesD,"");
                    barDataSetD.setColors(ColorTemplate.COLORFUL_COLORS);

                    BarData barDataD = new BarData (barDataSetD);
                    barChartfDifficulty.setData(barDataD);

                    String[] labelsD = new String[]{"","FÁCIL", "NORMAL", "DIFÍCIL","PRÁCTICA"};
                    XAxis xAxisD= barChartfDifficulty.getXAxis();
                    xAxisD.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxisD.setValueFormatter(new IndexAxisValueFormatter(labelsD));
                    xAxisD.setGranularity(1f);
                    xAxisD.setLabelCount(labelsD.length);
                    barChartfDifficulty.getDescription().setEnabled(false);
                    barChartfDifficulty.getLegend().setEnabled(false);


                    barChartfDifficulty.invalidate();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        }

    }
}