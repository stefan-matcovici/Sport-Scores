package ro.uaic.info.tppa.sportscores;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ro.uaic.info.tppa.sportscores.adapters.EventListAdapter;
import ro.uaic.info.tppa.sportscores.models.SportEvent;

public class LiveEventsActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mBarDrawerToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                        channelName, NotificationManager.IMPORTANCE_HIGH));
            }
        }

        // Drawer specific code
        mToolbar = findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setItemIconTintList(null);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.drawer_menu_settings_option:
                        Intent preferencesIntent = new Intent(LiveEventsActivity.this, PreferencesActivity.class);
                        startActivity(preferencesIntent);
                }
                mNavigationView.setCheckedItem(menuItem.getItemId());
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        mDrawerLayout.addDrawerListener(mBarDrawerToggle);
        mBarDrawerToggle.syncState();

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        setHeaderImage();

        // List
        mListView = findViewById(R.id.events_list);

        final ArrayList<SportEvent> sportEvents = new ArrayList<SportEvent>();
        sportEvents.add(new SportEvent("league1", "homeTeam1", "awayTeam1", "1", "2", "1", "location1"));
        sportEvents.add(new SportEvent("league2", "homeTeam2", "awayTeam2", "1", "2", "1",  "location2"));
        sportEvents.add(new SportEvent("league3", "homeTeam3", "awayTeam3", "1", "2", "1", "location3"));
        sportEvents.add(new SportEvent("league4", "homeTeam4", "awayTeam4", "1", "2", "1",  "location4"));
        sportEvents.add(new SportEvent("league5", "homeTeam5", "awayTeam5", "1", "2", "1",  "location5"));


        final ArrayAdapter<SportEvent> eventArrayAdapter = new EventListAdapter(this, sportEvents);
        mListView.setAdapter(eventArrayAdapter);


    }

    private void setHeaderImage() {
        Drawable drawable = null;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String sport = prefs.getString("default_sport", "");

        System.out.println("default-sport=" + sport);

        switch (sport) {
            case "football":
                drawable = getResources().getDrawable(R.drawable.header_football);
                break;
            case "basketball":
                drawable = getResources().getDrawable(R.drawable.header_basketball);
                break;
            case "tennis":
                drawable = getResources().getDrawable(R.drawable.header_tennis);
                break;
        }

        mNavigationView.getHeaderView(0).setBackground(drawable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return mBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    @Override
    public void onResume() {
        super.onResume();

        setHeaderImage();
    }
}
