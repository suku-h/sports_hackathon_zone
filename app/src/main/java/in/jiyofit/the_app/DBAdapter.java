package in.jiyofit.the_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class DBAdapter extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "WorkoutData.sqlite";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;

    public DBAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    public Integer getLoadPercentage(String dateValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        String col[] = {"percentage_avg_load"};
        //never forget inverted commas for datevalue in query
        Cursor cursor = db.query("Table_Performance_Data", col, "date = '"+dateValue+"'", null, null, null, null);
        Integer result = null;
        if (cursor.moveToFirst()) {
            do {
                result = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public ArrayList<String> getDayData(String dateValue){
        ArrayList<String> day_data = new ArrayList<String>();
        String cols[] = {"date", "load_values", "hours_slept", "sleep_quality", "exercise_done",
                        "calories_burnt", "percentage_avg_load"};
        Cursor cursor = db.query("Table_Performance_Data", cols, "date = '" + dateValue + "'", null, null, null, null);
        while (cursor.moveToNext()) {
            for (int i = 0; i < cols.length; i++) {
                day_data.add(i, cursor.getString(cursor.getColumnIndex(cols[i])));
            }
        }
        cursor.close();
        return day_data;
    }
}
