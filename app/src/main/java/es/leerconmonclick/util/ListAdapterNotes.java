package es.leerconmonclick.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leeconmonclick.AddContentActivity;
import com.example.leeconmonclick.AddNoteActivity;
import com.example.leeconmonclick.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;

public class ListAdapterNotes extends RecyclerView.Adapter<ListAdapterNotes.MyViewHolder>{

    private Context context;
    private ArrayList<Note> mDataNote;
    private LayoutInflater mInflater;

    private String userCollection;
    private DatabaseReference databaseReference;
    private FirebaseAuth db = FirebaseAuth.getInstance();


    public ListAdapterNotes(Context context, ArrayList<Note> mDataNote) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mDataNote = mDataNote;
    }

    @Override
    public ListAdapterNotes.MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.note_view, null);
        return new ListAdapterNotes.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {
            holder.bindData(mDataNote.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataNote.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private TextView date;

        private ImageButton editBtn, deleteBtn;

         MyViewHolder( View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tittleNoteId);
            description = itemView.findViewById(R.id.descriptionNoteId);
            editBtn = itemView.findViewById(R.id.btnEditNoteElementId);
            deleteBtn = itemView.findViewById(R.id.btnDeleteNoteElementId);
            date = itemView.findViewById(R.id.timeNoteId);
        }

        void bindData(final Note note)  {

            databaseReference = FirebaseDatabase.getInstance().getReference();


            title.setText(note.getTitle());
            description.setText(note.getDescription());

            FirebaseUser user = db.getCurrentUser();
            userCollection = user.getEmail();
            String[] parts = userCollection.split("@");
            userCollection = parts[0];
            userCollection = userCollection.toLowerCase();

            databaseReference.child("Users").child(userCollection).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String size = snapshot.child("sett").child("0").getValue().toString();
                    if(size.equals("grande")){
                        title.setTextSize(30);
                        description.setTextSize(30);
                        date.setTextSize(30);
                    }else if(size.equals("normal")){
                        title.setTextSize(20);
                        description.setTextSize(20);
                        date.setTextSize(20);
                    }else if(size.equals("peque")){
                        title.setTextSize(10);
                        description.setTextSize(10);
                        date.setTextSize(10);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            date.setText(DateFormat.getDateTimeInstance().format(note.getTime()));

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteNote(note);
                }
            });

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editNote(note);
                }
            });

        }
    }

    private void deleteNote(Note note){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("¿Quieres borrar el contenido?");
        builder.setTitle("Borrado");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseReference.child("Users").child(userCollection).child("notas").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    @SuppressLint("NotifyDataSetChanged")
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        for(DataSnapshot objDataSnapshot : dataSnapshot.getChildren()){
                            Long time = (Long) objDataSnapshot.child("time").getValue();
                            if(time == note.getTime()){
                                objDataSnapshot.getRef().removeValue();
                            }
                        }
                    }
                });
                Toast.makeText(context, "Contenido borrado con éxito", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void editNote(Note note){
        Intent addIntent = new Intent(context, AddNoteActivity.class);
        addIntent.putExtra("tittle", note.getTitle());
        addIntent.putExtra("description", note.getDescription());
        addIntent.putExtra("modeEdit", true);
        context.startActivity(addIntent);
    }
}
