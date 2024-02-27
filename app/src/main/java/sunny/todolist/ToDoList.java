package sunny.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ToDoList extends AppCompatActivity {

    ArrayList<Task> tasks;
    TaskAdapter adapter;
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            int taskID = tasks.get(position).getTaskID();
            Intent intent = new Intent(ToDoList.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("taskID", taskID);
            startActivity(intent);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initSettingsButton();
        initAddButton();
        initDeleteToggle();
    }
    @Override
    public void onResume() {
        super.onResume();
        // get the shared preferences for sort order and sort by
        String sortBy = getSharedPreferences("ToDoListPreferences", Context.MODE_PRIVATE).getString("sortfield", "subject");
        String sortOrder = getSharedPreferences("ToDoListPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");
        ListDataSource ds = new ListDataSource(ToDoList.this);

        try {
            ds.open();
            tasks = ds.getTasks(sortBy, sortOrder);
            ds.close();

            if (tasks.size() > 0) {
                RecyclerView taskList = findViewById(R.id.rvTasks);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

                taskList.setLayoutManager(layoutManager);
                adapter = new TaskAdapter(tasks, this);
                adapter.setOnItemClickListener(onItemClickListener);
                taskList.setAdapter(adapter);
            } else {
                Intent intent = new Intent(ToDoList.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error retrieving tasks!", Toast.LENGTH_LONG).show();
        }
    }

    private void initDeleteToggle() {
        Switch deleteToggle = findViewById(R.id.switchDelete);
        deleteToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.setDelete(isChecked);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initAddButton() {
        Button addTaskButton = findViewById(R.id.buttonAdd);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToDoList.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }



    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToDoList.this, ToDoSettings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
}
