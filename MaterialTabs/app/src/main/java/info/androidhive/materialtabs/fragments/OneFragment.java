package info.androidhive.materialtabs.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import info.androidhive.materialtabs.R;


public class OneFragment extends Fragment{

    public static final String MyPREFERENCES = "TokenStore" ;

    static int temperature=0;

    public OneFragment() {
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

        View view = inflater.inflate(R.layout.fragment_one, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        new getHeartRate().execute();
System.out.println("temp"+temperature);
        PieChart mPieChart = (PieChart)getView().findViewById(R.id.piechart);
        if(temperature<70) {
            mPieChart.addPieSlice(new PieModel("HeartRate", temperature, Color.parseColor("#FE6DA8")));
            mPieChart.addPieSlice(new PieModel("HeartRate", 50, Color.parseColor("#56B7F1")));
        }
        else
        {
            mPieChart.addPieSlice(new PieModel("HeartRate", temperature, Color.parseColor("#d66515")));

        }
        mPieChart.startAnimation();
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
            HttpGet httpGet = new HttpGet("https://api.fitbit.com/1/user/-/activities/heart/date/2016-05-02/1d/1min/time/" + currentTime + "/" + currentTime + ".json");

// Add Headers
            httpGet.addHeader("Authorization", code);
            //  httpGet.addHeader("key2", "value2");

            try {
                // Add your data
                //  List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                // nameValuePairs.add(new BasicNameValuePair("content", ""));
                // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpGet);

                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                System.out.println(responseString);

                JSONObject HeartRate = new JSONObject(responseString);

                JSONObject IntraRate = HeartRate.getJSONObject("activities-heart-intraday");

                JSONArray data = IntraRate.getJSONArray("dataset");

                JSONObject value = data.getJSONObject(0);

                System.out.println("value" + value.getString("value"));

                Context context1 = getActivity();
                SharedPreferences sp1 = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sp1.edit();

                editor.putString("HeartRate", "Bearer " + value.getString("value"));
                editor.commit();

                temperature=Integer.parseInt(value.getString("value"));



            } catch (Exception e) {
// TODO Auto-generated catch block
            }

            return null;
        }
    }
}
