package nhom7.clothnest.localDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import nhom7.clothnest.models.User;

public class UserInfo_Sqlite extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME = "LocalDatabase.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "userinfo";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    public static String EMAIL = "";
    public static String PASSWORD = "";

    public UserInfo_Sqlite(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        " (" +  COLUMN_EMAIL + " TEXT PRIMARY KEY , " +
                        COLUMN_PASSWORD + " TEXT);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void initAccount(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_EMAIL, email);
        cv.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_NAME, null, cv);
    }

    public void setInfoUser() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cusor = null;
        if(db!=null)    {
            cusor = db.rawQuery(query, null);
        }

        cusor.moveToFirst();
        EMAIL = cusor.getString(0);
        PASSWORD = cusor.getString(1);
    }

    public void deleteTableAcc()   {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME;
        db.execSQL(query);
    }

    public void updateTableAcc(String newPassword)    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PASSWORD, newPassword);

        db.update(TABLE_NAME, cv, "email = ?", new String[]{EMAIL});


    }
}
