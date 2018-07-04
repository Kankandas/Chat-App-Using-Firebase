package com.example.kankan.timepass;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

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
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference myRef;
    private ViewPager mViewPager;
    private Profile myProfile;
    private List<String> idList;

    private String name,relation,work,school,college,email,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        idList=new ArrayList<>();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        myProfile=new Profile();
        myRef= FirebaseDatabase.getInstance().getReference();


        if(user==null)
        {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
        //setOnlineStatus();
        //myRef.child("OnLine").child(user.getUid()).child("isOnline").setValue("Yes");





    }

    @Override
    protected void onStart() {

        super.onStart();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myRef.child("Payal").child(user.getUid()).child("isOnline").setValue("Yes");

            }
        },2000);
        myRef.child("Payal").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myRef.child("Payal").child(user.getUid()).child("isOnline").onDisconnect().setValue(ServerValue.TIMESTAMP);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    protected void onStop() {
        super.onStop();
        myRef.child("Payal").child(user.getUid()).child("isOnline").setValue(ServerValue.TIMESTAMP);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_All_User) {

            startActivity(new Intent(MainActivity.this,AllUsersActivity.class));
        }
        if(id==R.id.action_logout)
        {
            Calendar calendar=Calendar.getInstance();
            SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
            String currentTime=format.format(calendar.getTime());
            String str="";
            int mainHour=0;
            String timeType="";
            for(int i=0;i<currentTime.length();i++)
            {
                str=str+currentTime.charAt(i);
                if(currentTime.charAt(i+1)==':')
                {
                    int hour=Integer.parseInt(str);
                    if(hour>12)
                    {
                        mainHour=hour-12;
                        timeType="PM";
                    }
                    else if(hour==12)
                    {
                        mainHour=12;
                        timeType="PM";
                    }
                    else
                    {
                        mainHour=hour;
                        timeType="AM";
                    }
                    break;
                }
            }
            StringTokenizer tokenizer=new StringTokenizer(currentTime,":");
            String []strings=new String[3];
            int i=0;
            while(tokenizer.hasMoreTokens())
            {
                strings[i]=tokenizer.nextToken();
                i++;
            }
            strings[0]=String.valueOf(mainHour);
            String finalTime="";
            for(int j=0;j<strings.length;j++)
            {
                if(j==2)
                {
                    finalTime=finalTime+strings[j]+" "+timeType;
                }
                else {
                    finalTime = finalTime + strings[j] + ":";
                }
            }
            myRef.child("OnLine").child(user.getUid()).child("isOnline").setValue(finalTime);
            auth.signOut();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_account, container, false);
            return rootView;
        }
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
            switch (position)
            {
                case 0:

                     AccountFragment accountFragment=new AccountFragment();
                     return accountFragment;

                case 1:
                    AllUsersFragment allUsersFragment=new AllUsersFragment();
                    return allUsersFragment;

                case 2:
                    TimeLineFragment timeLineFragment=new TimeLineFragment();
                    return timeLineFragment;


            }
            return null;

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

    }


}
