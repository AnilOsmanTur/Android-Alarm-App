package app.anilosman.com.alarnsheduler;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AlarmActivity extends AppCompatActivity {

    //private MediaPlayer alarmPlayer;
    private Ringtone ring;
    private Button stopButton, snoozeButton;

    private AlarmReceiver alarmRec = new AlarmReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ring = RingtoneManager.getRingtone(getApplicationContext(), uri);
        ring.play();

/*
        alarmPlayer = new MediaPlayer();
        alarmPlayer = MediaPlayer.create(getApplicationContext(), R.raw.analog_alarm_clock);

        alarmPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                alarmPlayer.start();
            }
        });
*/

        stopButton = (Button) findViewById(R.id.stop_button);
        snoozeButton = (Button) findViewById(R.id.snooze_button);

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ring.stop();
                deleteAlarm();
                AlarmActivity.this.finish();
            }
        });

        snoozeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nothing for now
            }
        });

    }

    private void deleteAlarm() {
        // deactivating alarm
        alarmRec.cancelAlarms(getApplicationContext());
    }
}
