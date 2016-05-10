package info.androidhive.materialtabs.fragments;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import info.androidhive.materialtabs.R;


public class TwoFragment extends Fragment{
    public static final String MyPREFERENCES = "TokenStore" ;

    private Timer autoUpdate;
    static int temperature=0,calorie=0,step=0,min=0;
    static int mile=0;
    static int progressCal=56,progressStep=76,progressMin=34,progressMile=45;
    private ProgressBar progressBarMin;
    private ProgressBar progressBarMile;
    private ProgressBar progressBarCal;
    private ProgressBar progressBarStep;
    private TextView steps;
    private TextView miles;
    private TextView calories;
    private TextView mins;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    public TwoFragment() {
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
        return inflater.inflate(R.layout.fragment_two, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        steps = (TextView)getView().findViewById(R.id.steps);
        calories = (TextView)getView().findViewById(R.id.calories);
        mins = (TextView)getView().findViewById(R.id.minutes);
        miles = (TextView)getView().findViewById(R.id.miles);

       // steps.setText(""+step);
      //  calories.setText(""+calorie);
     //   mins.setText(""+min);
      //  miles.setText(""+mile);


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
        }, 0, 20000);//timer for 3 min




    }

    class getHeartRate extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String code="a";
            Context context = getActivity();
            try {
                SharedPreferences sp = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                code = sp.getString("access_token", "");
            }
            catch (Exception e)
            {

            }


            DateFormat df = new SimpleDateFormat("HH:mm");
            String currentTime = df.format(Calendar.getInstance().getTime());

            System.out.println(currentTime);

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://api.fitbit.com/1/user/-/activities/date/2016-05-02.json");

// Add Headers
            httpGet.addHeader("Authorization", code);
            //  httpGet.addHeader("key2", "value2");

            try {

                HttpResponse response = httpclient.execute(httpGet);

                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                System.out.println(responseString);

                JSONObject activities = new JSONObject(responseString);

                JSONObject Summary = activities.getJSONObject("summary");

                String calories = Summary.getString("caloriesOut") ;

                calorie=Integer.parseInt(calories);

                System.out.println("TotalCalories" + calorie);

                String sedentaryMinutes= Summary.getString("sedentaryMinutes");

                int dormant=Integer.parseInt(sedentaryMinutes);

                min=1440-dormant;

                System.out.println("sedentaryMinutes" + min);

                String steps = Summary.getString("steps");

                step = Integer.parseInt(steps);

                System.out.println("steps" + steps);

                JSONArray distance = Summary.getJSONArray("distances");

                JSONObject TotalDistance = distance.getJSONObject(0);

                String distance1 = TotalDistance.getString("distance");

                System.out.println("distance"+distance1);

                mile =(int) Float.parseFloat(distance1);

//                JSONObject goals = activities.getJSONObject("goals");
//
//                String TCalories=goals.getString("caloriesOut");
//
//                int cal= Integer.parseInt(TCalories);
//
//                progressCal=69;
//
//                System.out.println("progcal"+progressCal);
//
//
//                String TactiveMinutes =goals.getString("activeMinutes");
//
//                int min1= Integer.parseInt(TactiveMinutes);
//
//               progressMin=23;
//
//                System.out.println("progmin"+progressMin);
//
//                String Tdistance = goals.getString("distance");
//
//                int distan1= Integer.parseInt(Tdistance);
//
//               progressMile= 56;
//
//                System.out.println("progMil"+progressMile);
//
//                String TSteps = goals.getString("steps");
//
//                int step1= Integer.parseInt(TSteps);
//
//                progressStep= 78;
//
//                System.out.println("progstep"+progressStep);
//
//
//


            } catch (Exception e) {
// TODO Auto-generated catch block
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void v) {

            ValueAnimator animator = new ValueAnimator();
            animator.setObjectValues(0,step);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    steps.setText(String.valueOf(animation.getAnimatedValue()));
                }
            });
            animator.setEvaluator(new TypeEvaluator<Integer>() {
                public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                    return Math.round(startValue + (endValue - startValue) * fraction);
                }
            });
            animator.setDuration(3000);
            animator.start();

            ValueAnimator animator1 = new ValueAnimator();
            animator1.setObjectValues(0,calorie);
            animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    calories.setText(String.valueOf(animation.getAnimatedValue()));
                }
            });
            animator1.setEvaluator(new TypeEvaluator<Integer>() {
                public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                    return Math.round(startValue + (endValue - startValue) * fraction);
                }
            });
            animator1.setDuration(3000);
            animator1.start();

            ValueAnimator animator2 = new ValueAnimator();
            animator2.setObjectValues(0,min);
            animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    mins.setText(String.valueOf(animation.getAnimatedValue()));
                }
            });
            animator2.setEvaluator(new TypeEvaluator<Integer>() {
                public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                    return Math.round(startValue + (endValue - startValue) * fraction);
                }
            });
            animator2.setDuration(3000);
            animator2.start();

            ValueAnimator animator3 = new ValueAnimator();
            animator3.setObjectValues(0,mile);
            animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    miles.setText(String.valueOf(animation.getAnimatedValue()));
                }
            });
            animator3.setEvaluator(new TypeEvaluator<Integer>() {
                public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                    return Math.round(startValue + (endValue - startValue) * fraction);
                }
            });
            animator3.setDuration(3000);
            animator3.start();


            progressBarMile = (ProgressBar) getView().findViewById(R.id.mileProgress);
            progressBarMin = (ProgressBar) getView().findViewById(R.id.progressBar4);
            progressBarStep = (ProgressBar) getView().findViewById(R.id.StepProgress);
            progressBarCal = (ProgressBar) getView().findViewById(R.id.progressBar3);


            // Start long running operation in a background thread
            new Thread(new Runnable() {
                public void run() {
                    while (progressStatus < progressMile) {
                        progressStatus += 1;
                        handler.post(new Runnable() {
                            public void run() {
                                progressBarMile.setProgress(progressStatus);

                            }
                        });
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            new Thread(new Runnable() {
                public void run() {
                    while (progressStatus < progressMin) {
                        progressStatus += 1;
                        handler.post(new Runnable() {
                            public void run() {
                                progressBarMin.setProgress(progressStatus);

                            }
                        });
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            new Thread(new Runnable() {
                public void run() {
                    while (progressStatus < progressCal) {
                        progressStatus += 1;
                        handler.post(new Runnable() {
                            public void run() {
                                progressBarCal.setProgress(progressStatus);

                            }
                        });
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            new Thread(new Runnable() {
                public void run() {
                    while (progressStatus < progressStep) {
                        progressStatus += 1;
                        handler.post(new Runnable() {
                            public void run() {
                                progressBarStep.setProgress(progressStatus);

                            }
                        });
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        }
    }
}
