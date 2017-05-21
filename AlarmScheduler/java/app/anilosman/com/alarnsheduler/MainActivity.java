package app.anilosman.com.alarnsheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import data.CustomListViewAdapter;
import data.DataBaseHandler;
import model.Alarm;

public class MainActivity extends AppCompatActivity {

    private DataBaseHandler dba;
    private ArrayList<Alarm> dbAlarms = new ArrayList<>();
    private CustomListViewAdapter alarmAdapter;
    private ListView listView;
    private AlarmReceiver alarmRec = new AlarmReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, AddAlarmActivity.class);
                startActivity(i);

                Snackbar.make(view, "Moving to adding layout", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        listView = (ListView) findViewById(R.id.alarmList);

        refreshData();

    }

    // data refresher helper function
    private void refreshData() {
        dbAlarms.clear();
        dba = new DataBaseHandler(getApplicationContext());
        ArrayList<Alarm> alarmsFromDB = dba.getAlarms();

        for (int i = 0; i < alarmsFromDB.size(); i++){
            String name = alarmsFromDB.get(i).getName();
            int hour = alarmsFromDB.get(i).getHour();
            int min = alarmsFromDB.get(i).getMinunte();
            String date = alarmsFromDB.get(i).getDate();
            int mID = alarmsFromDB.get(i).getItemID();

            Alarm alarm = new Alarm();
            alarm.setName(name);
            alarm.setHour(hour);
            alarm.setMinunte(min);
            alarm.setDate(date);
            alarm.setItemID(mID);

            dbAlarms.add(alarm);
        }

        dba.close();

        //setup adapter
        alarmAdapter = new CustomListViewAdapter(MainActivity.this, R.layout.alarm_row, dbAlarms);
        listView.setAdapter(alarmAdapter);
        alarmAdapter.notifyDataSetChanged();

    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
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
        if (id == R.id.action_add) {
            Intent i = new Intent(MainActivity.this, AddAlarmActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
