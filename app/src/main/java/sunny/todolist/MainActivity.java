package sunny.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements PickDateDialog.SaveDateListener {

    Task currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToggleButton();
        initSettingsButton();
        initListButton();
        initPickDateButton();
        initTextChangedEvents();
        initSaveButton();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            initTask(extras.getInt("taskID"));
        } else {
            currentTask = new Task();
        }
        setForEditing(false);
    }

    private void initTask(int taskID) {
        ListDataSource ds = new ListDataSource(this);

        try {
            ds.open();
            currentTask = ds.getSpecificTask(taskID);
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        EditText etSubject = findViewById(R.id.editTextSubject);
        EditText etDescription = findViewById(R.id.editTextDescription);
        TextView tvDueDate = findViewById(R.id.textViewDueDate);
        etSubject.setText(currentTask.getSubject());
        etDescription.setText(currentTask.getDescription());
        tvDueDate.setText(DateFormat.format("MM/dd/yyyy", currentTask.getDueDate()));

        String priority = currentTask.getPriority();
        RadioButton rbLow = findViewById(R.id.radioButtonLow);
        RadioButton rbMedium = findViewById(R.id.radioButtonMedium);
        RadioButton rbHigh = findViewById(R.id.radioButtonHigh);

        if (priority.equalsIgnoreCase("low")) {
            rbLow.setChecked(true);
        } else if (priority.equalsIgnoreCase("medium")) {
            rbMedium.setChecked(true);
        } else {
            rbHigh.setChecked(true);
        }
    }

    private void initToggleButton() {
        final ToggleButton editToggle = (ToggleButton)findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setForEditing(editToggle.isChecked());
            }
        });
    }

    private void setForEditing(boolean enabled) {
        EditText editSubject = findViewById(R.id.editTextSubject);
        EditText editDescription  = findViewById(R.id.editTextDescription);
        Button buttonChange = findViewById(R.id.buttonPickDate);
        Button buttonSave = findViewById(R.id.buttonSave);
        RadioButton radioLow = findViewById(R.id.radioButtonLow);
        RadioButton radioMedium = findViewById(R.id.radioButtonMedium);
        RadioButton radioHigh = findViewById(R.id.radioButtonHigh);

        editSubject.setEnabled(enabled);
        editDescription.setEnabled(enabled);
        buttonChange.setEnabled(enabled);
        buttonSave.setEnabled(enabled);
        radioLow.setEnabled(enabled);
        radioMedium.setEnabled(enabled);
        radioHigh.setEnabled(enabled);

        if(enabled) {
            editSubject.requestFocus();
        }
    }

    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ToDoSettings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ToDoList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    @Override
    public void didFinishPickingDateDialog(Calendar selectedTime) {
        TextView dueDate = findViewById(R.id.textViewDueDate);
        dueDate.setText(DateFormat.format("MM/dd/yyyy", selectedTime));
        currentTask.setDueDate(selectedTime);
    }

    private void initPickDateButton() {
        Button pickDate = findViewById(R.id.buttonPickDate);
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                PickDateDialog dialog = new PickDateDialog();
                dialog.show(fm, "PickDate");
            }
        });
    }

    private void initTextChangedEvents() {
        final EditText etTaskSubject = findViewById(R.id.editTextSubject);
        etTaskSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentTask.setSubject(etTaskSubject.getText().toString());
            }
        });

        final EditText etDescription = findViewById(R.id.editTextDescription);
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentTask.setDescription(etDescription.getText().toString());
            }
        });

        final RadioGroup etGroup = findViewById(R.id.rgPriority);
        etGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton etPriority = (RadioButton)group.findViewById(checkedId);
                boolean isChecked = etPriority.isChecked();
                if (isChecked)
                {
                    currentTask.setPriority(etPriority.getText().toString());
                }


            }
        });
    }

    private void initSaveButton() {
        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                boolean wasSuccessful = false;
                ListDataSource ds = new ListDataSource(MainActivity.this);
                try {
                    ds.open();

                    if (currentTask.getTaskID() == -1) {
                        wasSuccessful = ds.insertTask(currentTask);
                    }
                    if (wasSuccessful) {
                        int newId = ds.getLastTaskID();
                        currentTask.setTaskID(newId);
                    }
                    else {
                        wasSuccessful = ds.updateTask(currentTask);
                    }
                    ds.close();
                }
                catch (Exception e) {
                    wasSuccessful = false;
                }

                if (wasSuccessful) {
                    ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
                    editToggle.toggle();
                    setForEditing(false);
                } else {
                    Toast.makeText(MainActivity.this, "All fields must be populated before saving!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editSubject = findViewById(R.id.editTextSubject);
        imm.hideSoftInputFromWindow(editSubject.getWindowToken(), 0);
        EditText editDescription = findViewById(R.id.editTextDescription);
        imm.hideSoftInputFromWindow(editDescription.getWindowToken(), 0);
    }

}