package com.zengyicalvin.homework9;

import android.net.Uri;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity implements Tab3Venue.OnFragmentInteractionListener, MapFragment.OnFragmentInteractionListener{
    String jsonString;
    JSONObject json;
    String tab1;
    JSONObject tab2;
    String tab3;
    String tab4;
    Menu menuCtl;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        MyFavorite app = (MyFavorite) getApplication();
        Log.i("FavListTest", app.myfave.size()+"");

        Toolbar toolbar = (Toolbar) findViewById(R.id.detailToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        String title = getIntent().getStringExtra("EXTRA_EVENTNAME");
        if (title.length() > 22) {
            getSupportActionBar().setTitle(title.substring(0, 22) + "...");
        } else {
            getSupportActionBar().setTitle(title);
        }


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.detailContainer);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.detailTabs);
        tabLayout.getTabAt(0).setIcon(R.drawable.info_outline);
        tabLayout.getTabAt(1).setIcon(R.drawable.artist);
        tabLayout.getTabAt(2).setIcon(R.drawable.venue);
        tabLayout.getTabAt(3).setIcon(R.drawable.upcoming);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        jsonString = getIntent().getStringExtra("JSON_DATA");

        try {
            json = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            tab1 = json.getString("tab1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            tab2 = json.getJSONObject("tab2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            tab3 = json.getString("tab3");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            tab4 = json.getString("tab4");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendTwitter() {
        String tmpName = getIntent().getStringExtra("EXTRA_EVENTNAME");
        JSONObject tmpJson = null;
        try {
            tmpJson = new JSONObject(tab1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tmpUrl = null;
        try {
            tmpUrl = tmpJson.getString("url");
        }catch (JSONException e) {
            e.printStackTrace();
        }

        String venue = null;
        try {
            venue = tmpJson.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String q = tmpName.toUpperCase() + " located at " + venue + ". Website: " + tmpUrl;
            String intentUrl = "https://twitter.com/intent/tweet?text=Check Out " + URLEncoder.encode(q, "UTF-8");;
            Log.i("CheckURLCareful",intentUrl);

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(intentUrl));
            startActivity(i);
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void seeIfFave() {
        MyFavorite yourApplication = (MyFavorite) getApplicationContext();

        JSONObject tab1Json = getTab1JSON();
        String name = "";
        String id = "";
        String venue = "";
        String url = "";
        String type = "";
        Date date = null;
        String locale = "";

        try {
            type = tab1Json.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            name = tab1Json.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            id = tab1Json.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            venue = tab1Json.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String localDate = "";
        String localTime = "";
        try {
            localDate = tab1Json.getJSONObject("dates").getJSONObject("start").getString("localDate");
            SimpleDateFormat month_date = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(localDate);
            localTime =  tab1Json.getJSONObject("dates").getJSONObject("start").getString("localTime");
            locale = month_date.format(date) + " " + localTime;
        } catch (JSONException err) {
            SimpleDateFormat month_date = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            locale = month_date.format(date);
            Log.e("jsonparsingerror", "cant find locale");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            url = tab1Json.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (yourApplication.myfave.containsKey(id)) {
            menuCtl.getItem(0).setIcon(R.drawable.heart_fill_white);
            Toast.makeText(this, name + " was removed from favorites",
                    Toast.LENGTH_LONG).show();
            yourApplication.myfave.remove(id);
        } else {
            menuCtl.getItem(0).setIcon(R.drawable.heart_fill_red);
            yourApplication.myfave.put(id, new EventListItemModel(name, type, venue, locale, id, url, date));
            Toast.makeText(this, name + " was added to favorites",
                    Toast.LENGTH_LONG).show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.mybutton:
                sendTwitter();
                return true;
            case R.id.favMenueIcon:
                seeIfFave();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        menuCtl = menu;
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MyFavorite yourApplication = (MyFavorite) getApplicationContext();
        String id = "";
        try {
            id = getTab1JSON().getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (yourApplication.myfave.containsKey(id)) {
            menu.getItem(0).setIcon(R.drawable.heart_fill_red);
        } else {
            menu.getItem(0).setIcon(R.drawable.heart_fill_white);
        }
        return true;
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Tab1Event tab1 = new Tab1Event();
                    return tab1;
                case 1:
                    Tab2Artist tab2 = new Tab2Artist();
                    return tab2;
                case 2:
                    Tab3Venue tab3 = new Tab3Venue();
                    return tab3;
                case 3:
                    Tab4Upcoming tab4 = new Tab4Upcoming();
                    return tab4;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }
    @Override
    public void messageFromParentFragment(Uri uri) {
        Log.i("TAG", "received communication from parent fragment");
    }

    @Override
    public void messageFromChildFragment(Uri uri) {
        Log.i("TAG", "received communication from child fragment");
    }

    public JSONObject getTab1JSON() {
        JSONObject tab1JSON = null;
        try {
            tab1JSON = new JSONObject(tab1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tab1JSON;
    }

    public JSONObject getTab2JSON() {
        return tab2;
    }

    public JSONObject getTab3JSON() {
        JSONObject tab3JSON = null;
        try {
            tab3JSON = new JSONObject(tab3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tab3JSON;
    }
    public JSONObject getTab4JSON() {
        JSONObject tab4JSON = null;
        try {
            tab4JSON = new JSONObject(tab4);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tab4JSON;
    }

    public JSONObject getLatLng() {
        JSONObject locationJson = null;
        try {
            JSONObject jsonDataTab3 = new JSONObject(tab3);
            locationJson = jsonDataTab3.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("location");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return locationJson;
    }
}
