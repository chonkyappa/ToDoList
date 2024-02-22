package sunny.todolist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class PickDateDialog extends DialogFragment {
    
    Calendar selectedDate;
    
    public interface SaveDateListener {
        void didFinishPickingDateDialog(Calendar selectedDate); 
        
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.pick_date, container);
        
        getDialog().setTitle("Pick Date");
        selectedDate = Calendar.getInstance();
        
        final CalendarView cv = view.findViewById(R.id.calendarView);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate.set(year, month, dayOfMonth);
            }
        });

        Button saveDateButton = view.findViewById(R.id.buttonSaveDate);
        saveDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDate(selectedDate);
            }
        });

        Button cancelDateButton = view.findViewById(R.id.buttonCancelDate);
        cancelDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }

    private void saveDate(Calendar selectedTime) {
        SaveDateListener activity = (SaveDateListener)getActivity();
        activity.didFinishPickingDateDialog(selectedTime);
        getDialog().dismiss();
    }
}
