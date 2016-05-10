package info.androidhive.materialtabs.fragments;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.activity.CustomViewIconTextTabsActivity;


public class OneFragment extends Fragment{
    HttpClient httpclient = new DefaultHttpClient();
    public static final String MyPREFERENCES = "TokenStore" ;
    TextView statusActivity;
    public Vibrator vibrator;
    private Timer autoUpdate;

    static int temperature=0,cal=0,stat=0;

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
    public void onResume()  {
        super.onResume();

        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        new getHeartRate().execute();
                    }
                });
            }
                    },0,60000*2);




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


            HttpGet httpGet = new HttpGet("https://api.fitbit.com/1/user/-/activities/heart/date/2016-05-02/1d/1min/time/" + currentTime + "/" + currentTime + ".json");

// Add Headers
            httpGet.addHeader("Authorization", code);


            try {

                HttpResponse response = httpclient.execute(httpGet);


                HttpEntity entity = response.getEntity();

                String responseString = EntityUtils.toString(entity, "UTF-8");

                System.out.println("rrr"+responseString);


                JSONObject HeartRate = new JSONObject(responseString);


                JSONObject IntraRate = HeartRate.getJSONObject("activities-heart-intraday");


                JSONArray data = IntraRate.getJSONArray("dataset");


                JSONObject value = data.getJSONObject(0);


                System.out.println("valueHeart" + value.getString("value"));


                Context context1 = getActivity();
                SharedPreferences sp1 = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sp1.edit();

                editor.putString("HeartRate", "Bearer " + value.getString("value"));
                editor.commit();

                temperature=Integer.parseInt(value.getString("value"));

//get calorie
                HttpGet httpGet1 = new HttpGet("https://api.fitbit.com/1/user/-/activities/calories/date/2016-05-02/1d/1min/time/" + currentTime + "/" + currentTime + ".json");
                 httpGet1.addHeader("Authorization", code);
                 // httpGet.addHeader("key2", "value2");
                   HttpResponse response1 = httpclient.execute(httpGet1);
                  HttpEntity entity1 = response1.getEntity();
                 String responseString1 = EntityUtils.toString(entity1, "UTF-8");
                  System.out.println(responseString1);
                   JSONObject calorieRate = new JSONObject(responseString1);
                  JSONObject IntraRateCal = calorieRate.getJSONObject("activities-calories-intraday");
                 JSONArray data1 = IntraRateCal.getJSONArray("dataset");
                 JSONObject valueCal = data1.getJSONObject(0);
                 System.out.println("valueCal" + (int)Float.parseFloat(valueCal.getString("value")));
                   cal=(int)Float.parseFloat(valueCal.getString("value"));


    //post to remote db



            } catch (Exception e) {
// TODO Auto-generated catch block
            }

            return null;
        }

       @Override
        protected void onPostExecute(Void v) {

            statusActivity=(TextView)getView().findViewById(R.id.activity);
            if(cal>12)
            {
                statusActivity.setText("Running");
                stat=1;
            }
            else
            {
                statusActivity.setText("Resting");
                stat=0;
            }



           try
           {



               HttpPost post = new HttpPost("http://ec2-54-153-12-179.us-west-1.compute.amazonaws.com:3001/heartrate/getThreshold");

               // add header


               List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
               urlParameters.add(new BasicNameValuePair("id", "1"));


               post.setEntity(new UrlEncodedFormEntity(urlParameters));

               HttpResponse response = httpclient.execute(post);

               HttpEntity entity1 = response.getEntity();
               String responseString1 = EntityUtils.toString(entity1, "UTF-8");

               System.out.println("responsekkkkk"+responseString1);

           }catch (Exception e)
           {

           }




            System.out.println("temp"+temperature);
            PieChart mPieChart = (PieChart)getView().findViewById(R.id.piechart);
            if(temperature<70) {
                mPieChart.addPieSlice(new PieModel("HeartRate", temperature, Color.parseColor("#FE6DA8")));
                mPieChart.addPieSlice(new PieModel("HeartRate", 50, Color.parseColor("#56B7F1")));
            }
            else {

                mPieChart.addPieSlice(new PieModel("HeartRate", temperature, Color.parseColor("#d66515")));
                mPieChart.startAnimation();
            }

            if(temperature>70)
            {

                try {
                    SmsManager sms = SmsManager.getDefault();  // using android SmsManager
                    sms.sendTextMessage("6504305478", null, "Heart Rate exceeded", null, null);
                }
                catch (Exception e)
                {

                }

                long pattern[] = { 0, 100, 200, 300, 400 };
                vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(pattern, 0);

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getActivity())
                                .setContentTitle("My notification")
                                .setContentText("Heart Limit exceeded");


                NotificationManager mNotifyMgr =
                        (NotificationManager)getActivity().getSystemService(getContext().NOTIFICATION_SERVICE);
// Builds the notification and issues it.
                mNotifyMgr.notify(001, mBuilder.build());

            }


        }
    }

}
