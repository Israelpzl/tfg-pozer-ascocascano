package com.example.leeconmonclick.professional.leeconmonclick.professional;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leeconmonclick.ErrorActivity;
import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.R;
import com.example.leeconmonclick.patient.CategorySelecctionActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import es.leerconmonclick.util.AudioPlay;
import es.leerconmonclick.util.adapters.ListAdapterTask;
import es.leerconmonclick.util.utils.Task;

public class TaskListActivity extends AppCompatActivity implements Comparator<Task> {

    private List<Task> taskItems;

    private DatabaseReference databaseReference;

    private RecyclerView recyclerView;
    private ListAdapterTask listAdapterTask;
    private String userCollection;

    private AlertDialog alertDialog;
    private TextView descriptionTaskPopUp, tittleTaskPopUp, dateTaskPopUp, timeTaskPopUp,titlePage;
    private TextView title;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findElements();
        getSettings();

        readData();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(TaskListActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });
    }

    private void readData(){


        databaseReference.child("Users").child(userCollection).child("taskList").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                taskItems.clear();
                for(DataSnapshot objDataSnapshot : snapshot.getChildren()){
                    Long id = (Long) objDataSnapshot.child("id").getValue();
                    String tittle = (String) objDataSnapshot.child("tittle").getValue();
                    String date = (String) objDataSnapshot.child("date").getValue();
                    String time = (String) objDataSnapshot.child("time").getValue();
                    String description = (String) objDataSnapshot.child("description").getValue();
                    String tag = (String) objDataSnapshot.child("tagNoty").getValue();
                    int i = Math.toIntExact(id);
                    Task t =  new Task(i,tittle,date,time,description,tag);
                    taskItems.add(t);
                }
                listAdapterTask = new ListAdapterTask(taskItems, TaskListActivity.this, new ListAdapterTask.OnItemClickListener() {
                    @Override
                    public void onItemClick(Task item) {
                        popUpDescriptionTask(item);
                    }
                });

                recyclerView.setAdapter(listAdapterTask);
                listAdapterTask.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void popUpDescriptionTask(Task task) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final View taskPopUpView = getLayoutInflater().inflate(R.layout.taskpopup,null);

        descriptionTaskPopUp = (TextView) taskPopUpView.findViewById(R.id.descriptionPopUpId);
        tittleTaskPopUp = (TextView) taskPopUpView.findViewById(R.id.tittlePopUpId);
        dateTaskPopUp = (TextView) taskPopUpView.findViewById(R.id.datePopUpId);
        timeTaskPopUp = (TextView) taskPopUpView.findViewById(R.id.timePopUpId);
        ImageButton editBtn = (ImageButton) taskPopUpView.findViewById(R.id.editBtnPopUpId);
        ImageButton deleteBtn = (ImageButton) taskPopUpView.findViewById(R.id.deleteBtnId);

        databaseReference.child("Users").child(userCollection).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String size = snapshot.child("sett").child("0").getValue().toString();
                switch (size) {
                    case "grande":
                        descriptionTaskPopUp.setTextSize(30);
                        tittleTaskPopUp.setTextSize(30);
                        timeTaskPopUp.setTextSize(30);
                        break;
                    case "normal":
                        descriptionTaskPopUp.setTextSize(20);
                        tittleTaskPopUp.setTextSize(20);
                        dateTaskPopUp.setTextSize(20);
                        timeTaskPopUp.setTextSize(20);
                        break;
                    case "peque":
                        descriptionTaskPopUp.setTextSize(10);
                        tittleTaskPopUp.setTextSize(10);
                        dateTaskPopUp.setTextSize(10);
                        timeTaskPopUp.setTextSize(10);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goEdit(task);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTask(task.getTagNoty(),task.getId()+"");
                alertDialog.dismiss();
            }
        });

        descriptionTaskPopUp.setText(task.getDescription());
        tittleTaskPopUp.setText(task.getTittle());
        dateTaskPopUp.setText(task.getDate());
        timeTaskPopUp.setText(task.getTime());

        alertDialogBuilder.setView(taskPopUpView);
        alertDialog =  alertDialogBuilder.create();
        alertDialog.show();

    }

    private void goEdit(Task task){
        Intent calendarIntent = new Intent(this, AddTaskActivity.class);
        calendarIntent.putExtra("id", task.getId());
        calendarIntent.putExtra("tittle", task.getTittle());
        calendarIntent.putExtra("description", task.getDescription());
        calendarIntent.putExtra("date", task.getDate());
        calendarIntent.putExtra("time", task.getTime());
        calendarIntent.putExtra("tagNoty", task.getTagNoty());
        calendarIntent.putExtra("modeEdit", true);
        this.startActivity(calendarIntent);
    }

    private void deleteTask(String tag, String taskId){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("¿Quieres borrar la tarea?");
        builder.setTitle("Borrado");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteNotify(tag);
                databaseReference.child("Users").child(userCollection).child("taskList").child(taskId).removeValue();
                Toast.makeText(getApplicationContext(), "Tarea borrada con éxito", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void deleteNotify (String tag){
        WorkManager.getInstance(this).cancelAllWorkByTag(tag);
    }

    private void getSettings(){
        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.taskList);

        databaseReference.child("Users").child(userCollection).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String size = snapshot.child("sett").child("0").getValue().toString();
                switch (size) {
                    case "grande":
                        titlePage.setTextSize(30);
                        break;
                    case "normal":
                        titlePage.setTextSize(20);
                        break;
                    case "peque":
                        titlePage.setTextSize(10);
                        break;
                }

                String dalto = snapshot.child("sett").child("1").getValue().toString();
                if(dalto.equals("tritanopia")){
                    constraintLayout.setBackgroundResource(R.color.background_tritano);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                setContentView(R.layout.activity_error2);
            }
        });

    }

    private void findElements(){

        taskItems = new ArrayList<>();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        titlePage = findViewById(R.id.titleTask);

        recyclerView = findViewById(R.id.listTaskRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TaskListActivity.this));

        FirebaseUser user = firebaseAuth.getCurrentUser();
        userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();

    }


    public void goAddTask(View v){
        Intent calendarIntent = new Intent(this, AddTaskActivity.class);
        calendarIntent.putExtra("modeEdit",false);
        startActivity(calendarIntent);
    }


    public void goBack(View view){finish();}

    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }



    @Override
    public int compare(Task t1, Task t2) {
        return t1.getDate().compareTo(t2.getDate());
    }

    @Override
    protected void onPause() {
        boolean valor = AudioPlay.isIsplayingAudio();
        if(valor){
            AudioPlay.stopAudio();
        }
        super.onPause();
    }
}