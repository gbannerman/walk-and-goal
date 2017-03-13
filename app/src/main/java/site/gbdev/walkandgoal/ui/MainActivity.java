package site.gbdev.walkandgoal.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import site.gbdev.walkandgoal.R;
import site.gbdev.walkandgoal.ui.history.HistoryFragment;
import site.gbdev.walkandgoal.ui.home.HomeFragment;
import site.gbdev.walkandgoal.ui.settings.PreferencesActivity;
import site.gbdev.walkandgoal.ui.statistics.StatisticsFragment;
import site.gbdev.walkandgoal.ui.test.TestFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_main);
                if (currentFragment.getClass() == HomeFragment.class){

                    Intent intent = new Intent(getBaseContext(), AddProgressActivity.class);
                    startActivity(intent);

                } else if (currentFragment.getClass() == TestFragment.class){
                    Intent intent = new Intent(getBaseContext(), AddTestProgressActivity.class);
                    startActivity(intent);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);

        currentFragment = new HomeFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main, currentFragment, "home")
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment newFragment = new HomeFragment();
        String tag = "home";

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean testMode = sharedPreferences.getBoolean("pref_test_mode", false);

        if (id == R.id.nav_home) {
            if (testMode){
                tag = "test";
                newFragment = new TestFragment();
            }
            fab.show();
        } else if (id == R.id.nav_history) {
            tag = "history";
            fab.hide();
            newFragment = new HistoryFragment();
        } else if (id == R.id.nav_statistics) {
            tag = "statistics";
            fab.hide();
            newFragment = new StatisticsFragment();
        } else if (id == R.id.nav_settings) {
            Intent i = new Intent(this, PreferencesActivity.class);
            startActivity(i);
        }

        currentFragment = newFragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main, currentFragment, tag)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
