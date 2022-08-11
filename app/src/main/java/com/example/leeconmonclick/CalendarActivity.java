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

import com.google.android.gms.tasks.OnSuccessListener;
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

    private Bundle data;
    private TextView  taskDate, taskTime;
    private EditText taskDescription,taskTittle;
    private String date,time,userCollection;

    private DatabaseReference databaseReference;
    private FirebaseAuth db = FirebaseAuth.getInstance();

    private int contador;

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
        taskTittle = (EditText) findViewById(R.id.textView12);
        taskDescription = (EditText) findViewById(R.id.descriptionTaskId);


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

        data = getIntent().getExtras();
        if (data.getBoolean("modeEdit")){
            modeEditOn(calendarView);
        }


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

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseUser user = db.getCurrentUser();
        userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();




        databaseReference.child(userCollection).child("taskList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                /*
                databaseReference.child(userCollection).child("taskList").removeValue();
                contador = 0;
                for(DataSnapshot objDataSnapshot : snapshot.getChildren()){

                    databaseReference.child(userCollection).child("taskList").child(contador+"").setValue(objDataSnapshot);
                    contador +=1;

                }
                */


                if (data.getBoolean("modeEdit")){
                    databaseReference.child(userCollection).child("taskList").child(data.getInt("id")+"").child("tittle").setValue(taskTittle.getText().toString());
                    databaseReference.child(userCollection).child("taskList").child(data.getInt("id")+"").child("description").setValue(taskDescription.getText().toString());
                    databaseReference.child(userCollection).child("taskList").child(data.getInt("id")+"").child("date").setValue(date);
                    databaseReference.child(userCollection).child("taskList").child(data.getInt("id")+"").child("time").setValue(time);
                    Toast.makeText(getApplicationContext(),"Tarea editada correctamente",Toast.LENGTH_LONG).show();

                }else{
                    int randomNum = (int) (Math.random() *1000);
                    Task task = new Task(randomNum,taskTittle.getText().toString(),date,time,taskDescription.getText().toString());

                    databaseReference.child(userCollection).child("taskList").child(randomNum+"").setValue(task).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Tarea guardada correctamente",Toast.LENGTH_LONG).show();
                        }
                    });
                }
                taskDate.setText(date);
                taskTime.setText(time);
                taskTittle.setText("");
                taskDescription.setText("");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        Intent taskIntent = new Intent(this, TaskList.class);
        startActivity(taskIntent);


    }

    @Override
    public int compare(Task t1, Task t2) {
        return t1.getDate().compareTo(t2.getDate());
    }

    private void modeEditOn(CalendarView calendarView){

        taskTittle.setText(data.getString("tittle"));
        taskDescription.setText(data.getString("description"));
        taskDate.setText(data.getString("date"));
        taskTime.setText(data.getString("time"));
       // calendarView.setDate(Long.parseLong(data.getString("date")));

    }
}