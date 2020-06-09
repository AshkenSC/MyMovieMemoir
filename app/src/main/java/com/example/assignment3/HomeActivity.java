package com.example.assignment3;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.assignment3.localDB.database.WatchlistEntryDatabase;
import com.example.assignment3.networkconnection.NetworkConnection;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.ExecutionException;

public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener{
    NetworkConnection networkConnection = null;

    // declare watchlist Room db instance
    public static WatchlistEntryDatabase db = null;

    // username which will be used to get user information
    protected String username = "";
    protected String firstName = "";
    public int userId = -1;

    // declared for navigation drawer
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_nav_drawer);
        // initialize network connection
        networkConnection = new NetworkConnection();
        // initialize Watchlist db
        db = WatchlistEntryDatabase.getInstance(this);

        /* get logged in username */
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        username = bundle.getString("username");

        // get user's first name
        GetUserFirstName getUserFirstName = new GetUserFirstName();
        getUserFirstName.execute(username);
        try {
            firstName = getUserFirstName.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // get user's id
        GetUserId getUserId = new GetUserId();
        getUserId.execute(username);
        try {
            userId = getUserId.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /* display sidebar */
        //adding the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nv);
        toggle = new ActionBarDrawerToggle(this,
                drawerLayout,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //these two lines of code show the navicon drawer icon top left hand side
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
        replaceFragment(new HomeFragment(userId, firstName));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_return_home:
                replaceFragment(new HomeFragment(userId, firstName));
                break;
            case R.id.menu_movie_search:
                replaceFragment(new MovieSearchFragment(userId, firstName));
                break;
            case R.id.menu_movie_memoir:
                replaceFragment(new MovieMemoirFragment(userId));
                break;
            case R.id.menu_watchlist:
                replaceFragment(new WatchlistFragment(userId));
                break;
            case R.id.menu_report:
                replaceFragment(new ReportFragment(userId));
                break;
            case R.id.menu_map:
                replaceFragment(new MapFragment(userId, firstName));
                break;
        }
        //this code closes the drawer after you selected an item from the menu,
        //otherwise stay open
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(Fragment nextFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nextFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private class GetUserFirstName extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... usernames) {
            return networkConnection.getFirstName(usernames[0]);
        }

        @Override
        protected void onPostExecute(String userFirstName) {
            firstName = userFirstName;
        }
    }

    private class GetUserId extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... usernames) {
            return networkConnection.getUserId(usernames[0]);
        }

        @Override
        protected void onPostExecute(Integer fetchedUserId) {
            userId = fetchedUserId;
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            this.getSupportFragmentManager().popBackStack();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}