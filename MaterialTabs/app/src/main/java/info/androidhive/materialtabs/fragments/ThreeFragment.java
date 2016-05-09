package info.androidhive.materialtabs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import info.androidhive.materialtabs.R;


public class ThreeFragment extends Fragment{

    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_three, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ValueLineChart mCubicValueLineChart = (ValueLineChart) getView().findViewById(R.id.cubiclinechart);

        ValueLineSeries series = new ValueLineSeries();
        series.setColor(0xFF56B7F1);


        series.addPoint(new ValueLinePoint("1", 64f));
        series.addPoint(new ValueLinePoint("2", 65f));
        series.addPoint(new ValueLinePoint("3", 54f));
        series.addPoint(new ValueLinePoint("4", 64f));
        series.addPoint(new ValueLinePoint("5", 62f));
        series.addPoint(new ValueLinePoint("6", 66f));
        series.addPoint(new ValueLinePoint("7", 68f));
        series.addPoint(new ValueLinePoint("8", 69f));
        series.addPoint(new ValueLinePoint("9", 65f));
        series.addPoint(new ValueLinePoint("10", 64f));
        series.addPoint(new ValueLinePoint("11", 63f));
        series.addPoint(new ValueLinePoint("12", 51f));
        series.addPoint(new ValueLinePoint("13", 71f));
        series.addPoint(new ValueLinePoint("14", 41f));
        series.addPoint(new ValueLinePoint("15", 67f));
        series.addPoint(new ValueLinePoint("16", 64f));
        series.addPoint(new ValueLinePoint("17", 66f));
        series.addPoint(new ValueLinePoint("18", 64f));
        series.addPoint(new ValueLinePoint("19", 66f));
        series.addPoint(new ValueLinePoint("20", 62f));
        series.addPoint(new ValueLinePoint("21", 69f));
        series.addPoint(new ValueLinePoint("22", 66f));
        series.addPoint(new ValueLinePoint("23", 67f));
        series.addPoint(new ValueLinePoint("24", 64f));
        series.addPoint(new ValueLinePoint("25", 66f));
        series.addPoint(new ValueLinePoint("26", 65f));
        series.addPoint(new ValueLinePoint("27", 62f));
        series.addPoint(new ValueLinePoint("28", 65f));
        series.addPoint(new ValueLinePoint("29", 67f));
        series.addPoint(new ValueLinePoint("30", 65f));

        mCubicValueLineChart.setHorizontalScrollBarEnabled(true);
        mCubicValueLineChart.addSeries(series);
        mCubicValueLineChart.startAnimation();

    }



}
