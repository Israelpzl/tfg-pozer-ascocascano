package com.example.leeconmonclick.professional.leeconmonclick.professional;

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

import java.util.ArrayList;

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


        //BarChart Category

        ArrayList<BarEntry> entriesC = new ArrayList<>();

        entriesC.add(new BarEntry(1,26));
        entriesC.add(new BarEntry(2,9));
        entriesC.add(new BarEntry(3,43));
        entriesC.add(new BarEntry(4,100));

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

        entriesD.add(new BarEntry(1,5));
        entriesD.add(new BarEntry(2,12));
        entriesD.add(new BarEntry(3,45));
        entriesD.add(new BarEntry(4,10));

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
}