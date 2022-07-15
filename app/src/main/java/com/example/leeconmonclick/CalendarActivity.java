package com.example.leeconmonclick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity {

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private EditText taskDescription, taskDate, taskTime;
    private Button newTaskBtn, cancelTaskBtn, openPopUpTaskBtn;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

       CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
       TextView myDate = (TextView) findViewById(R.id.textDate);

       calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
           @Override
           public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
               date = (i1 + 1) + "/" + i2 + "/" + i;
               myDate.setText(date);

               openPopUpTaskBtn = (Button) findViewById(R.id.openPopupTaskCalendarBtn);


           }
       });
    }

    public void createNewTask(View v){
        alertDialogBuilder = new AlertDialog.Builder(this);
        final View taskPopUpView = getLayoutInflater().inflate(R.layout.taskpopup,null);



        taskDescription = (EditText) taskPopUpView.findViewById(R.id.descriptionCalendarPopupId);
        taskDate = (EditText) taskPopUpView.findViewById(R.id.dateCalendarPopupId);

        taskDate.setText(date);

        taskTime = (EditText) taskPopUpView.findViewById(R.id.timeCalendarPopupId);

        newTaskBtn = (Button) taskPopUpView.findViewById(R.id.addCalendarPopupBtnId);
        cancelTaskBtn = (Button) taskPopUpView.findViewById(R.id.cancelCalendarPopupBtnId);

        alertDialogBuilder.setView(taskPopUpView);
        alertDialog =  alertDialogBuilder.create();
        alertDialog.show();


    }
}