package com.example.leeconmonclick;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.WorkManager;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Switch;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

import es.leerconmonclick.util.Task;
import es.leerconmonclick.util.WorkManagerNoti;

public class CalendarActivity extends AppCompatActivity implements Comparator<Task> {

    private Bundle data;
    private TextView  taskDate, taskTime;
    private EditText taskDescription,taskTittle;
    private String date,time,userCollection;
    private  Calendar calendar,c;

    private Switch noty;
    private int contador;

    private DatabaseReference databaseReference;
    private FirebaseAuth db = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        c  = Calendar.getInstance();
        calendar = Calendar.getInstance();

        int year =  c.get(Calendar.YEAR);
        int month =  c.get(Calendar.MONTH) + 1;
        int day =  c.get(Calendar.DAY_OF_MONTH);
        int hourDay = c.get(Calendar.HOUR_OF_DAY);
        int minuteDay = c.get(Calendar.MINUTE);

        taskDate = (TextView) findViewById(R.id.dateTaskId);
        taskTime = (TextView) findViewById(R.id.timeTaskId);
        taskTittle = (EditText) findViewById(R.id.textView12);
        taskDescription = (EditText) findViewById(R.id.descriptionTaskId);
        noty =(Switch) findViewById(R.id.switch1);


        date = day + "/" + month + "/" + year;
        if (Integer.toString(minuteDay).length() == 1){
            time =hourDay + ":" + "0" + minuteDay;
        }else{
            time =hourDay + ":" + minuteDay;
        }

        taskDate.setText(date);
        taskTime.setText(time);


        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
           public void onSelectedDayChange(@NonNull CalendarView calendarView, int year1, int month1, int day1) {
               date =  day1+ "/" + (month1 + 1)  + "/" + year1;
               calendar.set(Calendar.DAY_OF_MONTH,day1);
               calendar.set(Calendar.MONTH,month1);
               calendar.set(Calendar.YEAR,year1);
               taskDate.setText(date);
           }
       });

        data = getIntent().getExtras();
        if (data.getBoolean("modeEdit")){
            try {
                modeEditOn(calendarView);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    public void openTime(View v){

        Calendar c  = Calendar.getInstance();
        int hourDay = c.get(Calendar.HOUR_OF_DAY);
        int minuteDay = c.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog( this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {



                if (Integer.toString(minute).length() == 1){
                    time =hour + ":" + "0" + minute;
                }else{
                    time =hour + ":" + minute;
                }
                calendar.set(Calendar.HOUR_OF_DAY,hour);
                calendar.set(Calendar.MINUTE,minute);
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (data.getBoolean("modeEdit")){
                    deleteNotify(data.getString("tagNoty"));
                    String tag = generateKey();

                    databaseReference.child(userCollection).child("taskList").child(data.getInt("id")+"").child("tittle").setValue(taskTittle.getText().toString());
                    databaseReference.child(userCollection).child("taskList").child(data.getInt("id")+"").child("description").setValue(taskDescription.getText().toString());
                    databaseReference.child(userCollection).child("taskList").child(data.getInt("id")+"").child("date").setValue(date);
                    databaseReference.child(userCollection).child("taskList").child(data.getInt("id")+"").child("time").setValue(time);
                    databaseReference.child(userCollection).child("taskList").child(data.getInt("id")+"").child("tagNoty").setValue(tag);

                    Long alertTime = calendar.getTimeInMillis() - System.currentTimeMillis() - 30000;
                    int random = (int) (Math.random() * 50 + 1);
                    Data data = saveData("Tarea: " + taskTittle.getText().toString() ,"Te toca", random);
                    WorkManagerNoti.saveNoti(alertTime,data,tag);

                    if(!noty.isChecked()){
                        deleteNotify(tag);
                    }

                    Toast.makeText(getApplicationContext(),"Tarea editada correctamente",Toast.LENGTH_LONG).show();

                }else{
                    databaseReference.child(userCollection).child("taskList").removeValue();
                    contador = 0;
                    for(DataSnapshot objDataSnapshot : snapshot.getChildren()){
                        String tittle = (String) objDataSnapshot.child("tittle").getValue();
                        String date = (String) objDataSnapshot.child("date").getValue();
                        String time = (String) objDataSnapshot.child("time").getValue();
                        String description = (String) objDataSnapshot.child("description").getValue();
                        String tag = (String) objDataSnapshot.child("tagNoty").getValue();
                        Task t =  new Task(contador,tittle,date,time,description,tag);
                        databaseReference.child(userCollection).child("taskList").child(contador+"").setValue(t);
                        contador +=1;

                    }
                    String tag = generateKey();
                    Task task = new Task(contador,taskTittle.getText().toString(),date,time,taskDescription.getText().toString(),tag);


                    databaseReference.child(userCollection).child("taskList").child(contador+"").setValue(task).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Long alertTime = calendar.getTimeInMillis() - System.currentTimeMillis() - 30000;
                            int random = (int) (Math.random() * 50 + 1);
                            Data data = saveData("Tarea: " + taskTittle.getText().toString(),"Te toca", random);
                            WorkManagerNoti.saveNoti(alertTime,data,tag);

                            if(!noty.isChecked()){
                                deleteNotify(tag);
                            }

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

        Intent taskIntent = new Intent(this, TaskListActivity.class);
        startActivity(taskIntent);


    }

    @Override
    public int compare(Task t1, Task t2) {
        return t1.getDate().compareTo(t2.getDate());
    }

    private void modeEditOn(CalendarView calendarView) throws ParseException {

        taskTittle.setText(data.getString("tittle"));
        taskDescription.setText(data.getString("description"));
        taskDate.setText(data.getString("date"));
        taskTime.setText(data.getString("time"));
        TextView editTittle = (TextView) findViewById(R.id.textView10);
        editTittle.setText("Editar Tarea");
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        Date d = f.parse(data.getString("date"));
        calendarView.setDate(d.getTime());

    }

    private void deleteNotify (String tag){
        WorkManager.getInstance(this).cancelAllWorkByTag(tag);
        Toast.makeText(getApplicationContext(),"Notificación Eliminada",Toast.LENGTH_LONG).show();
    }

    private String generateKey(){
        return UUID.randomUUID().toString();
    }

    private Data saveData(String tittle,String description,int idNoty ){
        return new Data.Builder()
                .putString("tittle",tittle)
                .putString("description",description)
                .putInt("idNoty",idNoty).build();
    }

    public void goBack(View view){finish();}

    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }
}