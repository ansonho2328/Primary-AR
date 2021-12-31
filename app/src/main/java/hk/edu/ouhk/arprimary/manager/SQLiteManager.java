package hk.edu.ouhk.arprimary.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteManager {

    private SQLiteHelper sqLiteHelper;

    // remember to invoke this method first
    public void initSQLIte(Context context){
        this.sqLiteHelper = new SQLiteHelper(context);
    }

    public SQLiteDatabase getDatabase(){
        this.validateNotNull();
        return sqLiteHelper.getWritableDatabase();
    }

    public void createTables(){
        validateNotNull();
        this.sqLiteHelper.onCreate(sqLiteHelper.getWritableDatabase());
    }

    public void dropTables(){
        validateNotNull();
        this.sqLiteHelper.reset(sqLiteHelper.getWritableDatabase());
    }


    // self throw exception
    private void validateNotNull() {
        if (sqLiteHelper == null) throw new IllegalArgumentException("SQLiteHelper Not initialized.");
    }


}
