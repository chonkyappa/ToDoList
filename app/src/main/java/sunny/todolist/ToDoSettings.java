package sunny.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class ToDoSettings extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initListButton();
        initSortByClick();
        initSortOrderClick();
        initSettings();
    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToDoSettings.this, ToDoList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void initSortByClick() {
        RadioGroup rgSortBy = findViewById(R.id.rgSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbSubject = findViewById(R.id.rbSubject);
                RadioButton rbDate = findViewById(R.id.rbDate);

                if (rbSubject.isChecked()) {
                    getSharedPreferences("ToDoListPreferences", Context.MODE_PRIVATE).edit().putString("sortfield", "subject").apply();
                } else if (rbDate.isChecked()) {
                    getSharedPreferences("ToDoListPreferences", Context.MODE_PRIVATE).edit().putString("sortfield", "date").apply();
                } else {
                    getSharedPreferences("ToDoListPreferences", Context.MODE_PRIVATE).edit().putString("sortfield", "priority").apply();
                }
            }
        });
    }

    private void initSortOrderClick() {
        RadioGroup rgSortOrder = findViewById(R.id.rgSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbAscending = findViewById(R.id.rbAsc);

                if (rbAscending.isChecked()) {
                    getSharedPreferences("ToDoListPreferences", Context.MODE_PRIVATE).edit().putString("sortorder", "ASC").apply();
                } else {
                    getSharedPreferences("ToDoListPreferences", Context.MODE_PRIVATE).edit().putString("sortorder", "DESC").apply();
                }
            }
        });
    }

    private void initSettings() {
        // get the settings
        String sortBy = getSharedPreferences("ToDoListPreferences", Context.MODE_PRIVATE).getString("sortfield", "subject");
        String sortOrder = getSharedPreferences("ToDoListPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");

        // get the buttons for sort by
        RadioButton rbSubject = findViewById(R.id.rbSubject);
        RadioButton rbDate = findViewById(R.id.rbDate);
        RadioButton rbPriority = findViewById(R.id.rbPriority);

        // display the checked button for sort by
        if (sortBy.equalsIgnoreCase("subject")) {
            rbSubject.setChecked(true);
        } else if (sortBy.equalsIgnoreCase("date")) {
            rbDate.setChecked(true);
        } else {
            rbPriority.setChecked(true);
        }

        // get the buttons for sort order
        RadioButton rbAscending = findViewById(R.id.rbAsc);
        RadioButton rbDescending = findViewById(R.id.rbDesc);

        // display the checked option for sort order
        if (sortOrder.equalsIgnoreCase("ASC")) {
            rbAscending.setChecked(true);
        } else {
            rbDescending.setChecked(true);
        }
    }

}
