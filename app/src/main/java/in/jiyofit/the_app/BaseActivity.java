package in.jiyofit.the_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener {
    protected DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    protected ListView navList;
    protected FrameLayout contentFrameLayout;
    String[] activityNameArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        drawerLayout = (DrawerLayout) findViewById(R.id.abase_drawer_layout);
        contentFrameLayout = (FrameLayout)findViewById(R.id.abase_content_frame);
        navList = (ListView) findViewById(R.id.abase_lv);
        NavAdapter navAdapter = new NavAdapter(this);
        navList.setAdapter(navAdapter);
        navList.setOnItemClickListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.opendrawer, R.string.closedrawer);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityNameArray = getResources().getStringArray(R.array.activity_names);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.first_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // this makes the hamburger icon into a button. This button now opens and closes the drawer
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return actionBarDrawerToggle.onOptionsItemSelected(item);
        }
        //without break, if itemID==R.id.action_settings then it will execute both
        switch (item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_feedback:
                Toast.makeText(this, "Feedback from the user", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_logout:
                SharedPreferences profilePrefs = getSharedPreferences("PData", MODE_PRIVATE);
                SharedPreferences.Editor profileEditor = profilePrefs.edit();
                profileEditor.putBoolean("profileIsSet", false);
                profileEditor.commit();
                SharedPreferences loginPrefs = getSharedPreferences("LData", MODE_PRIVATE);
                SharedPreferences.Editor loginEditor = loginPrefs.edit();
                loginEditor.putBoolean("isItSaved", false);
                loginEditor.commit();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        //need to do the return because break is used
        return super.onOptionsItemSelected(item);
    }

    //this takes care of orientation change during drawer opening and closing
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    //when a user switches between portrait and landscape sometimes a new activity is created.
    // savedInstance takes back up of whatever the user is doing and puts it back to
    // the switched view. So user loses no data
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        navList.setItemChecked(position, true);
        drawerLayout.closeDrawers();
        String currentActivityName = this.getClass().getSimpleName();
        String clickedActivityName = activityNameArray[position];
        if(!clickedActivityName.equals(currentActivityName)){
            Class<?> nextActivity = null;
            try {
                nextActivity = Class.forName(String.valueOf(getPackageName()) + "." + clickedActivityName);
                if(clickedActivityName.equals("WorkoutActivity")){
                    nextActivity = Class.forName(String.valueOf(getPackageName()) + ".FirstActivity");
                }
                startActivity(new Intent(this, nextActivity));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
