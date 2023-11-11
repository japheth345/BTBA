package com.example.bta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.core.view.View;

import androidx.appcompat.app.AppCompatActivity;
public class AdminDashboardActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
            finish();
            Intent mainIntent = new Intent(this, HomeActivity.class);
            this.startActivity(mainIntent);
        }
        return super.onOptionsItemSelected(item);
    }

        private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ViewBookings(), "VIEW BOOKINGS");
        adapter.addFrag(new ViewCustomers(), "VIEW CUSTOMERS");


        viewPager.setAdapter(adapter);
    }

    public void Logout(android.view.View view)
    {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.signOut();
        Intent mainIntent = new Intent(this,HomeActivity.class);
        this.startActivity(mainIntent);
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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
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
    public void Logout(View view)
    {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.signOut();
        finish();
        Intent mainIntent = new Intent(this,HomeActivity.class);
        this.startActivity(mainIntent);
    }
}