package in.jiyofit.the_app;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PerformanceActivity extends FragmentActivity {
    String dateString;
    DBAdapter dbHelper;
    Bundle bundle;
    DayDataFragment dayDataFragment;
    FrameLayout overlayContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);

        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        caldroidFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.aperformance_frl_calcontainer, caldroidFragment).commit();

        overlayContainer = (FrameLayout) findViewById(R.id.overlaycontainer);

        dbHelper = new DBAdapter(this);
        ColorDrawable greenLight = new ColorDrawable(getResources().getColor(R.color.greenLight));
        ColorDrawable redLight = new ColorDrawable(getResources().getColor(R.color.redLight));
        ColorDrawable yellowLight = new ColorDrawable(getResources().getColor(R.color.yellowLight));

        for (int i = 1; i < 27; i++) {
            if (i < 10) {
                dateString = "0" + i + "-02-2016";
            } else {
                dateString = i + "-02-2016";
            }
            if (dbHelper.getLoadPercentage(dateString) > 150) {
                caldroidFragment.setBackgroundDrawableForDate(redLight, ParseDate(dateString));
            } else if (dbHelper.getLoadPercentage(dateString) < 80) {
                caldroidFragment.setBackgroundDrawableForDate(yellowLight, ParseDate(dateString));
            } else {
                caldroidFragment.setBackgroundDrawableForDate(greenLight, ParseDate(dateString));
            }
            caldroidFragment.refreshView();
        }
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                String clickedDate = DateToStr(date);
                ArrayList dayData = dbHelper.getDayData(clickedDate);
                bundle = new Bundle();
                bundle.putStringArrayList("dayData", dayData);
                dayDataFragment = new DayDataFragment();
                dayDataFragment.setArguments(bundle);
                overlayContainer.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().replace(R.id.overlaycontainer, dayDataFragment).commit();

            }
        };
        caldroidFragment.setCaldroidListener(listener);
        caldroidFragment.refreshView();

    }

    public Date ParseDate(String date_str) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date dateStr = null;
        try {
            dateStr = formatter.parse(date_str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public String DateToStr (Date date) {
        String intMonth = (String) android.text.format.DateFormat.format("MM", date); //06
        String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013
        String day = (String) android.text.format.DateFormat.format("dd", date); //20
        return day+"-"+intMonth+"-"+year;
    }

}
