package in.jiyofit.the_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import java.util.concurrent.ScheduledExecutorService;

public class FirstActivity extends BaseActivity implements NavRecyclerAdapter.NavItemClickListener {
    DataHelper dh;
    RecyclerView navRecyclerView;
    ScheduledExecutorService newSocialExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_first, contentFrameLayout);

        navRecyclerView = (RecyclerView) findViewById(R.id.afirst_rv_navigation);
        NavRecyclerAdapter navRecyclerAdapter = new NavRecyclerAdapter(this);
        //tells that the adapter implements the listener
        navRecyclerAdapter.setNavItemClickListener(this);
        navRecyclerView.setAdapter(navRecyclerAdapter);
        navRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dh = new DataHelper();
        DBAdapter dbHelper = new DBAdapter(this);
    }

    // to minimize app when back is pressed here
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //minimize application
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void NavItemClick(View view, String activityName) {
            try {
                Class<?> nextActivity = Class.forName(String.valueOf(getPackageName()) + "." + activityName);
                startActivity(new Intent(this, nextActivity));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

    }
}


