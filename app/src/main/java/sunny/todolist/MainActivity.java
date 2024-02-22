package sunny.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements PickDateDialog.SaveDateListener {

    Task currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentTask = new Task();
        initToggleButton();
        setForEditing(false);
        initSettingsButton();
        initListButton();
        initPickDateButton();
        initTextChangedEvents();

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

}