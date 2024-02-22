package sunny.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class ListDataSource {
    private SQLiteDatabase database;
    private ListDBHelper dbHelper;

    public ListDataSource(Context context) {
        dbHelper = new ListDBHelper(context);
    }

    public Task getSpecificTask(int taskID) {
        Task task = new Task();
        String query = "SELECT * FROM contact WHERE taskID =" + taskID;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            task.setTaskID(cursor.getInt(0));
            task.setSubject(cursor.getString(1));
            task.setDescription(cursor.getString(2));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(cursor.getString(3)));
            task.setDueDate(calendar);
            task.setPriority(cursor.getString(4));
            if(cursor.getString(5) == "false")
                task.setCompleted(false);
            else
                task.setCompleted(true);

            cursor.close();
        }
        return task;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertTask(Task t) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put("subject", t.getSubject());
            initialValues.put("description", t.getDescription());
            initialValues.put("dueDate", String.valueOf(t.getDueDate().getTimeInMillis()));
            initialValues.put("priority", t.getPriority());
            if (t.isCompleted() == false)
                initialValues.put("isCompleted", "false");
            else
                initialValues.put("isCompleted", true);
            didSucceed = database.insert("list", null, initialValues) > 0;
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateTask(Task t) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) t.getTaskID();
            ContentValues updateValues = new ContentValues();
            updateValues.put("subject", t.getSubject());
            updateValues.put("description", t.getDescription());
            updateValues.put("dueDate", String.valueOf(t.getDueDate().getTimeInMillis()));
            updateValues.put("priority", t.getPriority());
            if (t.isCompleted() == false)
                updateValues.put("isCompleted", "false");
            else
                updateValues.put("isCompleted", true);
            didSucceed = database.update("list", updateValues, "taskID=" + rowId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public int getLastTaskID() {
        int lastId;
        try {
            String query = "Select MAX(taskID) from list";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }

    public ArrayList<String> getListSubject() {
        ArrayList<String> listSubjects = new ArrayList<>();
        try {
            String query = "Select subject from list";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                listSubjects.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            listSubjects = new ArrayList<String>();
        }
        return listSubjects;
    }

    public ArrayList<Task> getTasks(String sortField, String sortOrder) {
        ArrayList<Task> tasks = new ArrayList<Task>();
        String query;
        try {
            if (sortField.equals("priority")) {
                query = "SELECT * FROM list ORDER BY CASE priority WHEN 'Low' THEN 1 WHEN 'Medium' THEN 2 WHEN 'High' THEN 3 ELSE 4 END " + sortOrder;
            }
            else {
                query = "Select * FROM list ORDER BY " + sortField + " " + sortOrder;
            }
            Cursor cursor = database.rawQuery(query, null);

            Task newTask;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newTask = new Task();
                newTask.setTaskID(cursor.getInt(0));
                newTask.setSubject(cursor.getString(1));
                newTask.setDescription(cursor.getString(2));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(3)));
                newTask.setDueDate(calendar);
                newTask.setPriority(cursor.getString(4));
                if(cursor.getString(5) == "false")
                    newTask.setCompleted(false);
                else
                    newTask.setCompleted(true);
                tasks.add(newTask);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            tasks = new ArrayList<Task>();
        }
        return tasks;
    }

    public boolean deleteTask(int taskID) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("list","taskID=" + taskID, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -return value already set to false
        }
        return didDelete;
    }

}
