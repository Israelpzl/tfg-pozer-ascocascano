package es.leerconmonclick.util;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import com.example.leeconmonclick.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListAdapterTask extends RecyclerView.Adapter<ListAdapterTask.ViewHolder> {


    private List<Task> mDataTask;
    private LayoutInflater mInflater;
    private Context context;

    private String userCollection;

    private DatabaseReference databaseReference;
    private FirebaseAuth db = FirebaseAuth.getInstance();

    final ListAdapterTask.OnItemClickListener listener;




    public interface OnItemClickListener{
        void onItemClick(Task item);
    }


    public ListAdapterTask(List<Task> itemList, Context context,ListAdapterTask.OnItemClickListener listener){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mDataTask = itemList;
        this.listener = listener;

    }


    @Override
    public int getItemCount(){return mDataTask.size();}

    @Override
    public ListAdapterTask.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.list_task_element, null);
        return new ListAdapterTask.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final ListAdapterTask.ViewHolder holder, final int position){
        try {
            holder.bindData(mDataTask.get(position));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setItems (List<Task> tasks){ mDataTask = tasks;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tittleTask;
        private ImageButton deleteBtn;
        private ImageView exclamation;

        ViewHolder(View itemView){
            super(itemView);
            tittleTask = itemView.findViewById(R.id.descriptionTask);
            deleteBtn = (ImageButton) itemView.findViewById(R.id.cancelBtn);
            exclamation = (ImageView) itemView.findViewById(R.id.exclamationImg);
        }

        void bindData(final Task task) throws ParseException {

            databaseReference = FirebaseDatabase.getInstance().getReference();
            FirebaseUser user = db.getCurrentUser();
            userCollection = user.getEmail();
            String[] parts = userCollection.split("@");
            userCollection = parts[0];
            userCollection = userCollection.toLowerCase();
            String taskId = task.getId()+"";
            String tag = task.getTagNoty();

            SimpleDateFormat formatDay = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = Calendar.getInstance();

            Date dateToday = calendar.getTime();
            String formattedDate = formatDay.format(dateToday);
            dateToday = formatDay.parse(formattedDate);
            Date dateTask =formatDay.parse(task.getDate());

            int hourDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minuteDay = calendar.get(Calendar.MINUTE);
            String time =hourDay + ":" + minuteDay;
            SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

            Date timeToday = formatTime.parse(time);
            Date timeTask = formatTime.parse(task.getTime());


            if  (dateTask.before(dateToday)){
                exclamation.setVisibility(View.VISIBLE);

            }else{
                exclamation.setVisibility(View.GONE);

            }

            tittleTask.setText(task.getTittle());

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setMessage("¿Quieres borrar la tarea?");
                    builder.setTitle("Borrado");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteNotify(tag);
                            databaseReference.child("Users").child(userCollection).child("taskList").child(taskId).removeValue();
                            Toast.makeText(context, "Tarea borrada con éxito", Toast.LENGTH_LONG).show();
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
            });


            itemView.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    listener.onItemClick(task);
                }
            });

        }


    }

    private void deleteNotify (String tag){
        WorkManager.getInstance(context).cancelAllWorkByTag(tag);
        Toast.makeText(context,"Notificación Eliminada",Toast.LENGTH_LONG).show();
    }


}
