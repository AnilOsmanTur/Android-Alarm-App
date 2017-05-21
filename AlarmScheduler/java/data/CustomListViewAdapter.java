package data;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.text.DateFormat;
import java.util.ArrayList;

import app.anilosman.com.alarnsheduler.AlarmDetailActivity;
import app.anilosman.com.alarnsheduler.R;
import model.Alarm;

/**
 * Created by anilosman on 13.05.2017.
 */

public class CustomListViewAdapter extends ArrayAdapter<Alarm> {

    private int layoutResource;
    private Activity activity;
    private ArrayList<Alarm> todoList = new ArrayList<>();

    public CustomListViewAdapter(Activity act, int resource, ArrayList<Alarm> data) {
        super(act, resource, data);
        this.layoutResource = resource;
        this.activity = act;
        this.todoList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return todoList.size();
    }

    @Nullable
    @Override
    public Alarm getItem(int position) {
        return todoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;

        if ( row == null || (row.getTag()) == null) {
            LayoutInflater inflater = LayoutInflater.from(activity);

            row = inflater.inflate(layoutResource, null);
            holder = new ViewHolder();

            holder.mName = (TextView) row.findViewById(R.id.alarmName);
            holder.mDate = (TextView) row.findViewById(R.id.alarmDate);
            holder.mTime = (TextView) row.findViewById(R.id.alarmTime);

            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.alarm = getItem(position);

        holder.mName.setText(holder.alarm.getName());
        holder.mDate.setText(holder.alarm.getDate());
        holder.mTime.setText(( holder.alarm.getHour() + ":" + holder.alarm.getMinunte() ));

        final ViewHolder finalHolder = holder;
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = finalHolder.alarm.getName().toString();
                int hour = finalHolder.alarm.getHour();
                int min = finalHolder.alarm.getMinunte();
                String date = finalHolder.alarm.getDate().toString();

                int mid = finalHolder.alarm.getItemID();

                Intent i = new Intent(activity, AlarmDetailActivity.class);
                i.putExtra("name", name);
                i.putExtra("hour", hour);
                i.putExtra("min", min);
                i.putExtra("date", date);
                i.putExtra("id", mid);

                activity.startActivity(i);

            }
        });

        return row;
    }

    class ViewHolder {
        Alarm alarm;
        TextView mName;
        TextView mTime;
        TextView mDate;
    }

}

