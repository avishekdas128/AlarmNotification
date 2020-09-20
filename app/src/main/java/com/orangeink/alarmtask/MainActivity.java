package com.orangeink.alarmtask;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.orangeink.alarmtask.databinding.ActivityMainBinding;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private Calendar calSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.timePicker.setOnClickListener(v -> openTimePickerDialog());
        binding.setAlarm.setOnClickListener(v -> setAlarm(calSet));
    }

    private void openTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, (view, hourOfDay, minute) -> {
            Calendar calNow = Calendar.getInstance();
            calSet = (Calendar) calNow.clone();
            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            if (calSet.compareTo(calNow) <= 0) {
                calSet.add(Calendar.DATE, 1);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.setTitle("Set Alarm Time");
        timePickerDialog.show();
    }

    private void setAlarm(Calendar calSet) {
        if(calSet == null || binding.editText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Time not set or text empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Alarm is set for " + calSet.getTime(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra("TEXT", binding.editText.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), (int) calSet.getTimeInMillis(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
        } else
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
        binding.editText.setText("");
    }
}