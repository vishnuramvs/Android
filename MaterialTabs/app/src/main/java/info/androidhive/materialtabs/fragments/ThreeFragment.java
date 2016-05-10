package info.androidhive.materialtabs.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import info.androidhive.materialtabs.R;


public class ThreeFragment extends Fragment{
    public static final String MyPREFERENCES = "TokenStore" ;

    int [] rate = new int[30];
    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new getHeartRate().execute();
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


        series.addPoint(new ValueLinePoint("1", 64));
        series.addPoint(new ValueLinePoint("2", 65));
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


    class getHeartRate extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Context context = getActivity();
            SharedPreferences sp = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            String code = sp.getString("access_token", "");

            DateFormat df = new SimpleDateFormat("HH:mm");
            String currentTime = df.format(Calendar.getInstance().getTime());

            System.out.println(currentTime);

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://api.fitbit.com/1/user/-/activities/heart/date/2016-03-01/2016-03-30.json");

// Add Headers
            httpGet.addHeader("Authorization", code);
            //  httpGet.addHeader("key2", "value2");

            try {

                HttpResponse response = httpclient.execute(httpGet);

                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
               // System.out.println(responseString);

                JSONObject HeartRate = new JSONObject(responseString);

                JSONArray RateArray = HeartRate.getJSONArray("activities-heart");

                for(int i=0;i<31;i++) {
                    rate[i] = Integer.parseInt(RateArray.getJSONObject(i).getJSONObject("value").getString("restingHeartRate"));

                }

                System.out.println("30 day " + rate);



            } catch (Exception e) {
// TODO Auto-generated catch block
            }

            return null;
        }
    }


}
