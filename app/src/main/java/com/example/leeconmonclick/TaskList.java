package com.example.leeconmonclick;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import es.leerconmonclick.util.ListAdapterTask;
import es.leerconmonclick.util.Task;

public class TaskList extends AppCompatActivity implements Comparator<Task> {

    private List<Task> taskItems =  taskItems = new ArrayList<>();

    private DatabaseReference databaseReference;
    private FirebaseAuth db = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);


        getListTask();
    }

    public void goCalendar (View v){
        Intent calendarIntent = new Intent(this, CalendarActivity.class);
        calendarIntent.putExtra("modeEdit",false);
        startActivity(calendarIntent);
    }

    public void getListTask(){


        databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = db.getCurrentUser();
        String userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();

        databaseReference.child("Users").child(userCollection).child("taskList").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                taskItems.clear();

                for(DataSnapshot objDataSnapshot : snapshot.getChildren()){
                   // Task t =  objDataSnapshot.getValue(Task.class);
                    Long id = (Long) objDataSnapshot.child("id").getValue();
                    String tittle = (String) objDataSnapshot.child("tittle").getValue();
                    String date = (String) objDataSnapshot.child("date").getValue();
                    String time = (String) objDataSnapshot.child("time").getValue();
                    String description = (String) objDataSnapshot.child("description").getValue();
                    int i = Math.toIntExact(id);
                    Task t =  new Task(i,tittle,date,time,description);

                    taskItems.add(t);

                    Collections.sort(taskItems, new TaskList());

                    ListAdapterTask listAdapterTask = new ListAdapterTask(taskItems, TaskList.this);
                    RecyclerView recyclerView = findViewById(R.id.listTaskRecycleView);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(TaskList.this));
                    recyclerView.setAdapter(listAdapterTask);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



    @Override
    public int compare(Task t1, Task t2) {
        return t1.getDate().compareTo(t2.getDate());
    }
}