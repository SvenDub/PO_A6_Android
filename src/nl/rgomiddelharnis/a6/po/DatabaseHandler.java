
package nl.rgomiddelharnis.a6.po;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Beheert alle verbindingen met de database.
 * 
 * @author Sven Dubbeld <sven.dubbeld1@gmail.com>
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    /**
     * De naam van de database.
     */
    public static final String DATABASE_NAME = "po_kassa";

    /**
     * De huidige versie van de database.
     */
    public static final int DATABASE_VERSION = 1;
    
    public static final String TABLE_LOGIN = "login";
    
    public static final String KEY_ID = "id";
    public static final String KEY_GEBRUIKER = "gebruikersnaam";
    public static final String KEY_WACHTWOORD = "wachtwoord";
    public static final String KEY_SITE = "site";

    /**
     * Maakt een nieuwe <code>DatabaseHandler</code> aan om verbindingen met de
     * database te beheren.
     * 
     * @param context De Context om te gebruiken.
     */
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Maakt alle tabellen aan.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "(" + KEY_ID
                + " INTEGER PRIMARY KEY, " + KEY_GEBRUIKER + " TEXT UNIQUE, " + KEY_WACHTWOORD
                + " TEXT," + KEY_SITE + " TEXT" + ")";
    }

    /**
     * Werkt de upgrade naar een nieuwere versie.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
