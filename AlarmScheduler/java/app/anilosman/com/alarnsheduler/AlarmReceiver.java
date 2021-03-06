package app.anilosman.com.alarnsheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.ArrayList;
import java.util.Calendar;

import data.DataBaseHandler;
import model.Alarm;

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent
 * and then starts the IntentService {@code SampleSchedulingService} to do some work.
 */
public class AlarmReceiver extends BroadcastReceiver {
    // alarm database
    private DataBaseHandler dba;
    private ArrayList<Alarm> dbAlarms = new ArrayList<>();
    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intents that is triggered when the alarm fires.
    private ArrayList<PendingIntent> alarmIntents = new ArrayList<>();
    private int hour, min, id;
    private Ringtone ring;

    @Override
    public void onReceive(final Context context, Intent intent) {
        // BEGIN_INCLUDE(alarm_onreceive)

        // END_INCLUDE(alarm_onreceive)

    }


    public void setAlarms(Context context) {
        dbAlarms.clear();
        dba = new DataBaseHandler(context);
        dbAlarms = dba.getAlarms();
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        for (int i = 0; i < dbAlarms.size(); i++){

            hour = dbAlarms.get(i).getHour();
            min = dbAlarms.get(i).getMinunte();
            id = dbAlarms.get(i).getItemID();

            setAlarm(context);
        }

        dba.close();

        // Enable {@code SampleBootReceiver} to automatically restart the alarm when the
        // device is rebooted.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

    }

    // BEGIN_INCLUDE(set_alarm)
    /**
     * Sets a repeating alarm that runs once a day at desired time. When the
     * alarm fires, the app broadcasts an Intent to this WakefulBroadcastReceiver.
     * @param context
     */
    public void setAlarm(Context context) {
        if (alarmMgr == null) {
            alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        }
        Intent intent = new Intent(context, AlarmActivity.class);
        PendingIntent alarmIntent = PendingIntent.getActivity(context, id, intent, 0);
        alarmIntents.add( alarmIntent );

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // Set the alarm's trigger time to desired time.
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        /*
         * If you don't have precise time requirements, use an inexact repeating alarm
         * the minimize the drain on the device battery.
         *
         * The call below specifies the alarm type, the trigger time, the interval at
         * which the alarm is fired, and the alarm's associated PendingIntent.
         * It uses the alarm type RTC_WAKEUP ("Real Time Clock" wake up), which wakes up
         * the device and triggers the alarm according to the time of the device's clock.
         *
         * Alternatively, you can use the alarm type ELAPSED_REALTIME_WAKEUP to trigger
         * an alarm based on how much time has elapsed since the device was booted. This
         * is the preferred choice if your alarm is based on elapsed time--for example, if
         * you simply want your alarm to fire every 60 minutes. You only need to use
         * RTC_WAKEUP if you want your alarm to fire at a particular date/time. Remember
         * that clock-based time may not translate well to other locales, and that your
         * app's behavior could be affected by the user changing the device's time setting.
         *
         * Here are some examples of ELAPSED_REALTIME_WAKEUP:
         *
         * // Wake up the device to fire a one-time alarm in one minute.
         * alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
         *         SystemClock.elapsedRealtime() +
         *         60*1000, alarmIntent);
         *
         * // Wake up the device to fire the alarm in 30 minutes, and every 30 minutes
         * // after that.
         * alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
         *         AlarmManager.INTERVAL_HALF_HOUR,
         *         AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
         */

        // Set the alarm to fire at approximately desired time, according to the device's
        // clock, and to repeat once a day.
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                     AlarmManager.INTERVAL_DAY, alarmIntent);

    }
    // END_INCLUDE(set_alarm)

    /**
     * Cancels the alarm.
     * @param context
     */
    // BEGIN_INCLUDE(cancel_alarm)
    public void cancelAlarms(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmMgr!= null) {
            for (int i = 0; i < alarmIntents.size(); i++) {
                cancelAlarmFromArray( i );
            }
        }

        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
    // END_INCLUDE(cancel_alarm)

    public void cancelAlarmFromArray( int id ) {

        PendingIntent alarmIntent = alarmIntents.get(id);
        alarmMgr.cancel(alarmIntent);

    }

    public void cancelAlarm( int id ) {

        PendingIntent alarmIntent = alarmIntents.get(id);
        alarmMgr.cancel(alarmIntent);

    }

}
