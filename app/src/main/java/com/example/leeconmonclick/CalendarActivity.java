package com.example.leeconmonclick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.leerconmonclick.util.Task;

public class CalendarActivity extends AppCompatActivity {

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private TextView  taskDate, taskTime;
    private EditText taskDescription;
    private Button newTaskBtn, cancelTaskBtn, openPopUpTaskBtn, addTimeTaskBtn;
    private String date,time;

    private int cont = 0;

    private DatabaseReference databaseReference;
    private FirebaseAuth db = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);



        Calendar c  = Calendar.getInstance();
        int year =  c.get(Calendar.YEAR);
        int month =  c.get(Calendar.MONTH);
        int day =  c.get(Calendar.DAY_OF_MONTH);
        int hourDay = c.get(Calendar.HOUR_OF_DAY);
        int minuteDay = c.get(Calendar.MINUTE);

        taskDate = (TextView) findViewById(R.id.dateCalendarId);
        taskTime = (TextView) findViewById(R.id.timeCalendarId);


        date = day + "/" + month + "/" + year;
        time =hourDay + ":" + minuteDay;
        taskDate.setText(date);
        taskTime.setText(time);

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
           public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
               date =  i2+ "/" + (i1 + 1)  + "/" + i;
               taskDate.setText(date);
           }
       });


    }

    /*
    public void createNewTask(View v){


        alertDialogBuilder = new AlertDialog.Builder(this);
        final View taskPopUpView = getLayoutInflater().inflate(R.layout.taskpopup,null);


        taskDescription = (EditText) taskPopUpView.findViewById(R.id.descriptionCalendarPopupId);
        taskDate = (EditText) taskPopUpView.findViewById(R.id.dateCalendarPopupId);

        taskDate.setText(date);

        alertDialogBuilder.setView(taskPopUpView);
        alertDialog =  alertDialogBuilder.create();
        alertDialog.show();




    }
*/
    public void openTime(View v){

        Calendar c  = Calendar.getInstance();
        int hourDay = c.get(Calendar.HOUR_OF_DAY);
        int minuteDay = c.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog( this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                time =hour + ":" + minute;
                taskTime.setText(time);
            }
        }, hourDay, minuteDay, true);


        tpd.show();

    }

    public void saveTask (View v){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = db.getCurrentUser();
        String userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];

        taskDescription = (EditText) findViewById(R.id.descriptionCalendarId);

        Task task = new Task(date,taskDescription.getText().toString(),time);

        String numTask = cont+"";


        databaseReference.child("Users").child(userCollection).child("taskList").child(numTask).setValue(task);
        cont +=1;
        //databaseReference.child("Users").child(userCollection).child("task").(task);

        Toast.makeText(getApplicationContext(),"Tarea guardada correctamente",Toast.LENGTH_LONG).show();

    }
}