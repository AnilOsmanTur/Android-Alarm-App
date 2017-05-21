package app.anilosman.com.alarnsheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import data.DataBaseHandler;

public class AlarmDetailActivity extends AppCompatActivity {

    private TextView name, date, time;
    private Button deleteButton;
    private int alarmID;
    private AlarmReceiver alarmRec = new AlarmReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_detail);

        name = (TextView) findViewById(R.id.alarmNameDetail);
        time = (TextView) findViewById(R.id.alarmTimeDetail);
        date = (TextView) findViewById(R.id.alarmDateDetail);

        deleteButton = (Button) findViewById(R.id.deleteButton);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            name.setText(extras.getString("name"));
            date.setText(extras.getString("date"));

            int hour = extras.getInt("hour");
            int min = extras.getInt("min");
            time.setText( hour + ":" + min );

            alarmID = extras.getInt("id");

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteAlarm();
                }
            });

        }

        alarmRec.setAlarms(getApplicationContext());

    }

    private void deleteAlarm() {
        DataBaseHandler dba = new DataBaseHandler(getApplicationContext());
        dba.deleteAlarm(alarmID);

        // deactivating alarm
        alarmRec.cancelAlarms(getApplicationContext());

        Toast.makeText(getApplicationContext(), "Alarm Deleted!", Toast.LENGTH_SHORT).show();

        dba.close();
        AlarmDetailActivity.this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            deleteAlarm();
            return true;
        }else if (id == R.id.action_setAlams) {
            alarmRec.setAlarms(getApplicationContext());
            return true;
        }

            return super.onOptionsItemSelected(item);
    }


}
