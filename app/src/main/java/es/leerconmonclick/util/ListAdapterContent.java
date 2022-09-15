package es.leerconmonclick.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.AddContentActivity;
import com.example.leeconmonclick.CalendarActivity;
import com.example.leeconmonclick.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.w3c.dom.Text;

import java.util.List;

public class ListAdapterContent extends RecyclerView.Adapter<ListAdapterContent.ViewHolder> {


    private List<Content> mDataTask;
    private LayoutInflater mInflater;
    private Context context;

    private String userCollection;

    private DatabaseReference databaseReference;
    private FirebaseAuth db = FirebaseAuth.getInstance();




    public ListAdapterContent(List<Content> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mDataTask = itemList;
    }

    @Override
    public int getItemCount() {return mDataTask.size();}


    @Override
    public ListAdapterContent.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.content_element, null);
        return new ListAdapterContent.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ListAdapterContent.ViewHolder holder, int position) {
            holder.bindData(mDataTask.get(position));
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView word;
        private ImageView imgView;
        private ImageButton editBtn, deleteBtn;



        ViewHolder(View itemView) {
            super(itemView);

            word = itemView.findViewById(R.id.wordContentElementId);
            imgView = itemView.findViewById(R.id.imgContentElementId);
            editBtn = itemView.findViewById(R.id.btnEditContentElementId);
            deleteBtn = itemView.findViewById(R.id.btnDeleteContentElementId);

        }

        void bindData(final Content content){

            databaseReference = FirebaseDatabase.getInstance().getReference();
            word.setText( content.getWord());
            Glide.with(context).load(content.getImg()).into(imgView);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteContent( content.getWord());
                }
            });

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editContent(content);
                }
            });

        }
    }

    private void deleteContent(String w){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("¿Quieres borrar el contenido?");
        builder.setTitle("Borrado");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseReference.child("content").child(w).removeValue();
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


    private void editContent(Content content){
        Intent addIntent = new Intent(context, AddContentActivity.class);
        addIntent.putExtra("word", content.getWord());
        addIntent.putExtra("image", content.getImg());
        addIntent.putExtra("determinant", content.getDeterminant());
        //addIntent.putExtra("syllables", content.getSyllables());
        addIntent.putExtra("modeEdit", true);
        context.startActivity(addIntent);
    }

}
