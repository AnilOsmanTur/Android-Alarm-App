package app.anilosman.com.alarnsheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import data.DataBaseHandler;
import model.Alarm;

public class AddAlarmActivity extends AppCompatActivity {

    private EditText addName;
    private TimePicker setTime;
    private Button addButton;
    private DataBaseHandler dba;
    private int hour, min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        dba = new DataBaseHandler(getApplicationContext());

        addName = (EditText) findViewById(R.id.editAlarmName);
        setTime = (TimePicker) findViewById(R.id.timePicker);
        addButton = (Button) findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // database adding helper
                addToDB();
            }
        });

    }

    private void addToDB() {
        Alarm alarm = new Alarm();

        hour = setTime.getCurrentHour();
        min = setTime.getCurrentMinute();

        alarm.setName(addName.getText().toString().trim());
        alarm.setHour(hour);
        alarm.setMinunte(min);

        dba.addAlarm(alarm);
        dba.close();
        AddAlarmActivity.this.finish();
    }
}
