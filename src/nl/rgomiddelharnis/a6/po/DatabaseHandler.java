
package nl.rgomiddelharnis.a6.po;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    /**
     * De tabel om te gebruiken voor het inloggen.
     */
    public static final String TABLE_LOGIN = "login";

    /**
     * De key voor id.
     */
    public static final String KEY_ID = "id";
    /**
     * De key voor gebruikersnaam.
     */
    public static final String KEY_GEBRUIKER = "gebruikersnaam";
    /**
     * De key voor wachtwoord.
     */
    public static final String KEY_WACHTWOORD = "wachtwoord";
    /**
     * De key voor site.
     */
    public static final String KEY_SITE = "site";
    
    /**
     * De key om succes aan te tonen.
     */
    public static final String KEY_SUCCESS = "success";

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
        // Query's
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "(" + KEY_ID
                + " INTEGER PRIMARY KEY, " + KEY_GEBRUIKER + " TEXT UNIQUE, " + KEY_WACHTWOORD
                + " TEXT," + KEY_SITE + " TEXT" + ")";

        // Maak tabellen aan
        db.execSQL(CREATE_LOGIN_TABLE);
        db.close();
    }

    /**
     * Werkt de upgrade naar een nieuwere versie.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    /**
     * Controleert of er een gebruiker ingelogd is.
     * 
     * @return {@link Boolean} True als er ingelogd is.
     */
    public boolean isGebruikerIngelogd() {

        // Voor qeury uit
        String query = "SELECT * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // Tel het aantal rijen
        int rows = cursor.getCount();

        cursor.close();
        db.close();
        
        if (rows > 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Logt een nieuwe gebruiker in.
     * 
     * @param id {@link Integer} De ID van de gebruiker.
     * @param gebruikersnaam {@link String} De gebruikersnaam.
     * @param wachtwoord {@link String} Het wachtwoord.
     * @param site {@link String} De url van de site om te gebruiken.
     * 
     * @return {@link Boolean} True als er ingelogd is.
     */
    public boolean login(int id, String gebruikersnaam, String wachtwoord, String site) {

        // Log de vorige gebruiker uit
        logout();
        
        // Waardes om toe te voegen
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_GEBRUIKER, gebruikersnaam);
        values.put(KEY_WACHTWOORD, wachtwoord);
        values.put(KEY_SITE, site);
        
        // Voer query uit
        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_LOGIN, null, values);
        
        db.close();
        
        // Controleer of toevoegen gelukt is
        if (result != -1) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Logt een gebruiker uit.
     * 
     * @return {@link Boolean} True als er uitgelogd is.
     */
    public boolean logout() {
        
        // Voer query uit
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(TABLE_LOGIN, "1", null);
        
        db.close();
        
        // Controleer of uitloggen gelukt is
        if (result > 0) {
           return true; 
        } else {
            return false;
        }
        
    }

}
