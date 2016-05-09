package info.androidhive.materialtabs.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.LayoutInflater;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.codec.binary.Base64;

import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.fragments.OneFragment;
import info.androidhive.materialtabs.fragments.ThreeFragment;
import info.androidhive.materialtabs.fragments.TwoFragment;

public class CustomViewIconTextTabsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Intent myIntent;
    String redirectURL;
    SharedPreferences sp;
    public static Context contextOfApplication;
    public static final String MyPREFERENCES = "TokenStore" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view_icon_text_tabs);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        try {
            myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.fitbit.com/oauth2/authorize?client_id=227LGH&response_type=code&redirect_uri=myapp://prabhu&scope=activity%20heartrate"));
            startActivity(myIntent);
        }
        catch (Exception e)
        {

        }
        Intent intent = getIntent();

// check if this intent is started via custom scheme link
        try {
            redirectURL = intent.getData().toString();
            redirectURL=redirectURL.substring(20,60);
        }
        catch (Exception e)
        {

        }

        System.out.println(redirectURL);

        basicAuth();

        new initAuth().execute();



    }

    /**
     * Adding custom view to tab
     */
    class initAuth extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            String code =null;
            String code1=null;


            SharedPreferences sp = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            code=sp.getString("Authorization","");
            code1=sp.getString("RedirectCode","");

            String url ="https://api.fitbit.com/oauth2/token?client_id=227LGH&grant_type=authorization_code&redirect_uri=myapp://prabhu&code="+code1;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

// Add Headers
            httpPost.addHeader("Authorization",code);
            httpPost.addHeader("Content-Type","application/x-www-form-urlencoded");
            //  httpGet.addHeader("key2", "value2");

            try {

                HttpResponse response = httpclient.execute(httpPost);

                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                System.out.println("auth token"+responseString);

                JSONObject token = new JSONObject(responseString);

                SharedPreferences sp1 = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sp1.edit();

                editor.putString("access_token","Bearer "+token.getString("access_token"));
                editor.commit();


            } catch (Exception e) {
// TODO Auto-generated catch block
            }

            return null;
        }

    }
    public void basicAuth()
    {
        sp=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String token="227LTD:22cde08e86ded651c7b2d9fb2e5c30a6";//client ID+client secret

        byte[] bytesEncoded = Base64.encodeBase64(token.getBytes());

        String encodedValue= new String(bytesEncoded);

        String AuthToken="Basic "+encodedValue;
        System.out.println("ecncoded value is " + AuthToken);

        SharedPreferences.Editor editor = sp.edit();

        editor.putString("Authorization",AuthToken);
        editor.putString("RedirectCode", redirectURL);
        editor.commit();

    }


    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("ONE");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_favourite, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);


        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("TWO");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_call, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("THREE");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_contacts, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    /**
     * Adding fragments to ViewPager
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OneFragment(), "ONE");
        adapter.addFrag(new TwoFragment(), "TWO");
        adapter.addFrag(new ThreeFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }














}
