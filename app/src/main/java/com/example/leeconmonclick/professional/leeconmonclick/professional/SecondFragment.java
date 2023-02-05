package com.example.leeconmonclick.professional.leeconmonclick.professional;

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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (getArguments() != null){

            databaseReference = FirebaseDatabase.getInstance().getReference();

            databaseReference.child("userPatient").child(getArguments().getString("namePatient")).child("stadistic").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {




                    //GET COLORS
                    List<Integer> colors = new ArrayList<>();
                    colors.add(ContextCompat.getColor(getContext(), R.color.button_green_check));
                    colors.add(ContextCompat.getColor(getContext(), R.color.button_red_cancel));

                    //GAME 1

                    int sf = snapshot.child("joinWords").child("difficulties").child("FÁCIL").child("succes").getValue(Integer.class);
                    int ff = snapshot.child("joinWords").child("difficulties").child("FÁCIL").child("failed").getValue(Integer.class);
                    int sn = snapshot.child("joinWords").child("difficulties").child("NORMAL").child("succes").getValue(Integer.class);
                    int fn = snapshot.child("joinWords").child("difficulties").child("NORMAL").child("failed").getValue(Integer.class);
                    int sd = snapshot.child("joinWords").child("difficulties").child("DIFÍCIL").child("succes").getValue(Integer.class);
                    int fd = snapshot.child("joinWords").child("difficulties").child("DIFÍCIL").child("failed").getValue(Integer.class);

                    int auxS = sf + sn + sd;
                    int auxF = ff + fn + fd;



                    if ( snapshot.child("joinWords").child("timesPlayed").getValue(Integer.class) != 0){
                        percentageSucces =(auxS * 100) / (auxS + auxF);
                    }else{
                        percentageSucces = 0;
                    }

                    percentage1.setText(percentageSucces+""+"%");


                    ArrayList<PieEntry> entries1 = new ArrayList<>();
                    entries1.add(new PieEntry(auxS,"Acertado"));
                    entries1.add(new PieEntry(auxF,"Fallado"));


                    PieDataSet pieDataSet1 = new PieDataSet(entries1,"Unir Palabras");
                    pieDataSet1.setColors(colors);
                    pieDataSet1.setValueTextColor(Color.BLACK);
                    pieDataSet1.setValueTextSize(14f);

                    PieData pieData1 = new PieData(pieDataSet1);
                    pieChart1.getDescription().setEnabled(false);
                    pieChart1.setCenterText("Unir Palabras");
                    pieChart1.setBackgroundColor(Color.TRANSPARENT);
                    pieChart1.animate();

                    pieChart1.setData(pieData1);

                    //GAME 2

                    sf = snapshot.child("letters").child("difficulties").child("FÁCIL").child("succes").getValue(Integer.class);
                    ff = snapshot.child("letters").child("difficulties").child("FÁCIL").child("failed").getValue(Integer.class);
                    sn = snapshot.child("letters").child("difficulties").child("NORMAL").child("succes").getValue(Integer.class);
                    fn = snapshot.child("letters").child("difficulties").child("NORMAL").child("failed").getValue(Integer.class);
                    sd = snapshot.child("letters").child("difficulties").child("DIFÍCIL").child("succes").getValue(Integer.class);
                    fd = snapshot.child("letters").child("difficulties").child("DIFÍCIL").child("failed").getValue(Integer.class);

                    auxS = sf + sn + sd;
                    auxF = ff + fn + fd;

                    if ( snapshot.child("letters").child("timesPlayed").getValue(Integer.class) != 0){
                        percentageSucces =(auxS * 100) / (auxS + auxF);
                    }else{
                        percentageSucces = 0;
                    }

                    percentage2.setText(percentageSucces+""+"%");

                    ArrayList<PieEntry> entries2 = new ArrayList<>();
                    entries2.add(new PieEntry(auxS,"Acertado"));
                    entries2.add(new PieEntry(auxF,"Fallado"));


                    PieDataSet pieDataSet2 = new PieDataSet(entries2,"Letras");
                    pieDataSet2.setColors(colors);
                    pieDataSet2.setValueTextColor(Color.BLACK);
                    pieDataSet2.setValueTextSize(14f);

                    PieData pieData2 = new PieData(pieDataSet2);
                    pieChart2.getDescription().setEnabled(false);
                    pieChart2.setCenterText("Letras");
                    pieChart2.setBackgroundColor(Color.TRANSPARENT);
                    pieChart2.getLegend().setEnabled(false);
                    pieChart2.animate();

                    pieChart2.setData(pieData2);

                    //GAME 3
                    int s = snapshot.child("syllables").child("succes").getValue(Integer.class);
                    int f = snapshot.child("syllables").child("failed").getValue(Integer.class);

                    if ( snapshot.child("syllables").child("timesPlayed").getValue(Integer.class) != 0){
                        percentageSucces =(s * 100) / (s + f);
                    }else{
                        percentageSucces = 0;
                    }

                    percentage3.setText(percentageSucces+""+"%");

                    ArrayList<PieEntry> entries3 = new ArrayList<>();
                    entries3.add(new PieEntry(s,"Acertado"));
                    entries3.add(new PieEntry(f,"Fallado"));


                    PieDataSet pieDataSet3 = new PieDataSet(entries3,"Sílabas");
                    pieDataSet3.setColors(colors);
                    pieDataSet3.setValueTextColor(Color.BLACK);
                    pieDataSet3.setValueTextSize(14f);

                    PieData pieData3 = new PieData(pieDataSet3);
                    pieChart3.getDescription().setEnabled(false);
                    pieChart3.setCenterText("Sílabas");
                    pieChart3.getLegend().setEnabled(false);
                    pieChart3.setBackgroundColor(Color.TRANSPARENT);
                    pieChart3.animate();

                    pieChart3.setData(pieData3);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }



    }
}