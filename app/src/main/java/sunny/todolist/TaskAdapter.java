package sunny.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter {

    private ArrayList<Task> taskData;
    private View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    private Context parentContext;

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView taskSubject;
        public TextView taskDueDate;
        public TextView taskPriority;
        public Button deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            taskSubject = itemView.findViewById(R.id.textSubject);
            taskDueDate = itemView.findViewById(R.id.textDueDate);
            taskPriority = itemView.findViewById(R.id.textPriority);
            deleteButton = itemView.findViewById(R.id.buttonDeleteTask);

            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }

        public TextView getTaskSubject() {
            return taskSubject;
        }

        public TextView getTaskDueDate() {
            return taskDueDate;
        }

        public TextView getTaskPriority() {
            return taskPriority;
        }

        public Button getDeleteButton() {
            return deleteButton;
        }
    }

    public TaskAdapter(ArrayList<Task> taskData, Context context) {
        this.taskData = taskData;
        this.parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TaskViewHolder tvh = (TaskViewHolder) holder;
        tvh.getTaskSubject().setText(taskData.get(position).getSubject());
        tvh.getTaskDueDate().setText(taskData.get(position).getDueDate().toString());
        tvh.getTaskPriority().setText(taskData.get(position).getPriority());

        if (isDeleting) {
            tvh.getDeleteButton().setVisibility(View.VISIBLE);
            tvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position);
                }
            });
        } else {
            tvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }

    private void deleteItem(int position) {
        Task task = taskData.get(position);
        ListDataSource ds = new ListDataSource(parentContext);

        try {
            ds.open();
            boolean didDelete = ds.deleteTask(task.getTaskID());
            ds.close();

            if (didDelete) {
                taskData.remove(position);
                notifyDataSetChanged();
            } else {
                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
        }

    }

    public void setDelete(boolean b) {
        isDeleting = b;
    }
    @Override
    public int getItemCount() {
        return taskData.size();
    }
}
