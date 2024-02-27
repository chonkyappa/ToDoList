package sunny.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ListDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mylist.db";
    private static final int DATABASE_VERSION = 2;

    //Database creation of sql statement
    private static final String CREATE_TABLE_LIST =
            "create table list (taskID integer primary key autoincrement, "
                    + "subject text not null, description text, "
                    + "dueDate text, priority text, isCompleted boolean default false);";

    public ListDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        Log.w(ContactDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                + newVersion +! ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(db);
         */
        try {
            db.execSQL("ALTER TABLE contact ADD COLUMN contactphoto blob");
        }
        catch (Exception e) {
            //do nothing
        }
    }

}
