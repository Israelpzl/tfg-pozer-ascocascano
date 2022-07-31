package es.leerconmonclick.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.leeconmonclick.R;

import java.util.List;

public class ListAdapterTask extends RecyclerView.Adapter<ListAdapterTask.ViewHolder> {


    private List<Task> mDataTask;
    private LayoutInflater mInflater;
    private Context context;


    public ListAdapterTask(List<Task> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mDataTask = itemList;

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
        holder.bindData(mDataTask.get(position));
    }

    public void setItems (List<Task> tasks){ mDataTask = tasks;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView descriptionTask;
        Button editBtn, cancelBtn;

        ViewHolder(View itemView){
            super(itemView);
            descriptionTask = itemView.findViewById(R.id.descriptionTask);
            //editBtn = itemView.findViewById(R.id.editBtn);
            //cancelBtn = itemView.findViewById(R.id.cancelBtn);
        }

        void bindData(final Task task){
            descriptionTask.setText(task.getDescription());
        }

    }

}
