package com.example.leeconmonclick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.leerconmonclick.util.Task;

public class CalendarActivity extends AppCompatActivity implements Comparator<Task> {

    private List<Task> taskItems =  taskItems = new ArrayList<>();

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private TextView  taskDate, taskTime;
    private EditText taskDescription,taskTittle;
    private Button newTaskBtn, cancelTaskBtn, openPopUpTaskBtn, addTimeTaskBtn;
    private String date,time,userCollection;

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

        taskDate = (TextView) findViewById(R.id.dateTaskId);
        taskTime = (TextView) findViewById(R.id.timeTaskId);


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
        userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();

        taskTittle = (EditText) findViewById(R.id.textView12);
        taskDescription = (EditText) findViewById(R.id.descriptionTaskId);


        databaseReference.child("Users").child(userCollection).child("taskList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                //Collections.sort(taskItems, new TaskList());
                //databaseReference.child("Users").child(userCollection).child("taskList").removeValue();



                int randomNum = (int) (Math.random() *1000);


                Task task = new Task(randomNum,taskTittle.getText().toString(),date,time,taskDescription.getText().toString());
               // Task task2 = new Task(randomNum,taskItems.size()+"",date,time,"Tarea generada");
               // taskItems.add(task);
                //taskItems.add(task2);



               // cont=  0;
                databaseReference.child("Users").child(userCollection).child("taskList").child(randomNum+"").setValue(task);
                /*


               taskItems.clear();

                for(DataSnapshot objDataSnapshot : snapshot.getChildren()) {
                    Task t = objDataSnapshot.getValue(Task.class);
                    taskItems.add(t);
                }


                for (Task t : taskItems){
                    databaseReference.child("Users").child(userCollection).child("taskList").child(cont+"").setValue(t);
                }
                 */




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Toast.makeText(getApplicationContext(),"Tarea guardada correctamente",Toast.LENGTH_LONG).show();
        Intent taskIntent = new Intent(this, TaskList.class);
        startActivity(taskIntent);

    }

    @Override
    public int compare(Task t1, Task t2) {
        return t1.getDate().compareTo(t2.getDate());
    }
}