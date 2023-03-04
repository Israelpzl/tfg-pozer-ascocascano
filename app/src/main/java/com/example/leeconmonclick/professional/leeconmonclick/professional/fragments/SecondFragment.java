package com.example.leeconmonclick.professional.leeconmonclick.professional.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.leeconmonclick.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private PieChart pieChart1,pieChart2,pieChart3;
    private DatabaseReference databaseReference;
    private TextView percentage1,percentage2,percentage3;
    private  int percentageSucces =0;
    private String userCollection;

    public SecondFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_second, container, false);
        pieChart1 = view.findViewById(R.id.radarChart1);
        pieChart2 = view.findViewById(R.id.radarChart2);
        pieChart3 = view.findViewById(R.id.radarChart3);
        percentage1 = view.findViewById(R.id.percentage1);
        percentage2 = view.findViewById(R.id.percentage2);
        percentage3 = view.findViewById(R.id.percentage3);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();

        databaseReference.child("Users").child(userCollection).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String size = snapshot.child("sett").child("0").getValue().toString();
                switch (size) {
                    case "grande":
                        percentage1.setTextSize(30);
                        percentage2.setTextSize(30);
                        percentage3.setTextSize(30);
                        break;
                    case "normal":
                        percentage1.setTextSize(20);
                        percentage2.setTextSize(20);
                        percentage3.setTextSize(20);
                        break;
                    case "peque":
                        percentage1.setTextSize(10);
                        percentage2.setTextSize(10);
                        percentage3.setTextSize(10);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void pieChartFunction(String game, PieChart pieChart,TextView percentage,String gameText){
        databaseReference.child("userPatient").child(getArguments().getString("namePatient").toLowerCase(Locale.ROOT)).child("stadistic").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Integer> colors = new ArrayList<>();
                colors.add(ContextCompat.getColor(getContext(), R.color.button_green_check));
                colors.add(ContextCompat.getColor(getContext(), R.color.button_red_cancel));

                ArrayList<PieEntry> entries = new ArrayList<>();
                int auxS;
                int auxF;

                if (game.equals("joinWords") || game.equals("letters")) {

                    auxS = snapshot.child(game).child("difficulties").child("FÁCIL").child("succes").getValue(Integer.class)
                            + snapshot.child(game).child("difficulties").child("NORMAL").child("succes").getValue(Integer.class)
                            + snapshot.child(game).child("difficulties").child("DIFÍCIL").child("succes").getValue(Integer.class);
                    auxF = snapshot.child(game).child("difficulties").child("FÁCIL").child("failed").getValue(Integer.class)
                            + snapshot.child(game).child("difficulties").child("NORMAL").child("failed").getValue(Integer.class)
                            + snapshot.child(game).child("difficulties").child("DIFÍCIL").child("failed").getValue(Integer.class);

                }else{

                    auxS = snapshot.child(game).child("succes").getValue(Integer.class);
                    auxF = snapshot.child(game).child("failed").getValue(Integer.class);

                }

                if (snapshot.child(game).child("timesPlayed").getValue(Integer.class) != 0) {
                    percentageSucces = (auxS * 100) / (auxS + auxF);
                } else {
                    percentageSucces = 0;
                }

                percentage.setText(percentageSucces + "" + "%");

                entries.add(new PieEntry(auxS, "Acertado"));
                entries.add(new PieEntry(auxF, "Fallado"));

                PieDataSet pieDataSet = new PieDataSet(entries,gameText);
                pieDataSet.setColors(colors);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(14f);

                PieData pieData = new PieData(pieDataSet);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText(gameText);
                pieChart.getLegend().setEnabled(false);
                pieChart.setBackgroundColor(Color.TRANSPARENT);
                pieChart.animate();

                pieChart.setData(pieData);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null){

            databaseReference = FirebaseDatabase.getInstance().getReference();

            pieChartFunction("joinWords",pieChart1,percentage1,"Unir Palabras");
            pieChartFunction("letters",pieChart2,percentage2,"Letras");
            pieChartFunction("syllables",pieChart3,percentage3,"Sílabas");

        }
    }
}