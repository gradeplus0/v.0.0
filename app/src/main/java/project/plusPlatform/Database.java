package project.plusPlatform;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;

public class Database extends SQLiteOpenHelper implements Serializable {

    private static final String DB_NAME = "Gradeplus";
    private static final int DB_VERSION = 8;
    private enum userTypes {
        ADMIN, STUDENT, LECTURER
    }
    public Database(Context context){
        super(context,DB_NAME,null,DB_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        makeDatabase(db,1,DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       makeDatabase(db,oldVersion,newVersion);
    }

    public void makeDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion <2){
            String commentTable = "CREATE TABLE Comment (" +
                    "work_id INTEGER," +
                    "comment_value TEXT)";
            String feedbackTable = "CREATE TABLE Feedback (" +
                    "feedback_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "feedback_value TEXT);";

            String resultTable = "CREATE TABLE Result (" +
                    "result_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "result_mark INTEGER," +
                    "feedback_id INTEGER);";

            String assessedworkTable = "CREATE TABLE AssessedWork (" +
                    "work_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "work_name TEXT," +
                    "result_id INTEGER)";

            String moduleWorkProxy = "CREATE TABLE ModuleWorkProxy (" +
                    "work_id INTEGER," +
                    "module_id INTEGER);";

            String moduleTable = "CREATE TABLE Module (" +
                    "module_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "module_name TEXT);";
            String userModuleProxy = "CREATE TABLE UserModuleProxy (" +
                    "module_id INTEGER," +
                    "user_id INTEGER);";
            String userTable = "CREATE TABLE User (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_name TEXT," +
                    "user_email TEXT," +
                    "user_password TEXT NOT NULL," +
                    "user_type TEXT);";


            String[] queries = {feedbackTable,resultTable,assessedworkTable,moduleTable,moduleWorkProxy,userModuleProxy,userTable};
            for(String query : queries){
                db.execSQL(query);
            }
            System.out.println("Database is created");
        }

        if(oldVersion <3){
            db.execSQL("ALTER TABLE Result ADD user_id INTEGER");
            System.out.println("Table is altered");
        }

        if(oldVersion <4){
            db.execSQL("ALTER TABLE Result ADD work_id INTEGER");
        }

        if(oldVersion <5){
            db.execSQL("ALTER TABLE AssessedWork ADD result_uploaded TEXT");
            ContentValues values = new ContentValues();
            values.put("result_uploaded","false");
            db.update("AssessedWork",values,null,null);
        }
        if(oldVersion <6){
            ContentValues values = new ContentValues();
            values.put("user_email","kmalik@AD.qmul.ac.uk");
            values.put("user_password","111222");
            db.insert("User",null,values);
        }
        if(newVersion ==8){
            db.execSQL("DELETE FROM User where user_email = 'mehdi@ST.qmul.ac.uk';");
            System.out.println("User deleted");
        }
    }
}
