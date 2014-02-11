
package nl.rgomiddelharnis.a6.po;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
     * De tabel om alle beschikbare tafels in op te slaan.
     */
    public static final String TABLE_TAFEL = "tafelnummer";
    /**
     * De tabel om alle producten in op te slaan.
     */
    public static final String TABLE_PRODUCTEN = "producten";
    
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
     * De key voor de status.
     */
    public static final String KEY_STATUS = "status";
    /**
     * De key om succes aan te tonen.
     */
    public static final String KEY_SUCCESS = "success";
    /**
     * De key om een error aan te tonen.
     */
    public static final String KEY_ERROR = "error";
    /**
     * De key voor categorienummer.
     */
    public static final String KEY_CATEGORIENR = "categorienummer";
    /**
     * De key voor gerecht.
     */
    public static final String KEY_GERECHT = "gerecht";
    /**
     * De key voor prijs als getal.
     */
    public static final String KEY_PRIJS = "prijs";
    /**
     * De key voor prijs als opgemaakte string.
     */
    public static final String KEY_PRIJS_FORMATTED = "prijs_format";
    /**
     * De key voor actief.
     */
    public static final String KEY_ACTIEF = "actief";
    
    Context mContext;
    
    /**
     * Maakt een nieuwe <code>DatabaseHandler</code> aan om verbindingen met de
     * database te beheren.
     * 
     * @param context De Context om te gebruiken.
     */
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
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
        String CREATE_TAFEL_TABLE = "CREATE TABLE " + TABLE_TAFEL + "(" + KEY_ID
                + " INTEGER PRIMARY KEY, " + KEY_STATUS + " INTEGER" + ")";
        String CREATE_PRODUCTEN_TABLE = "CREATE TABLE " + TABLE_PRODUCTEN + "(" + KEY_ID
                + " INTEGER PRIMARY KEY, " + KEY_CATEGORIENR + " INTEGER," + KEY_GERECHT + " TEXT,"
                + KEY_PRIJS + " DOUBLE," + KEY_ACTIEF + " INTEGER" + ")";

        // Maak tabellen aan
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_TAFEL_TABLE);
        db.execSQL(CREATE_PRODUCTEN_TABLE);
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
    
    /**
     * Haalt de URL van de site op van de gebruiker.
     * 
     * @return {@link String} De URL van de site.
     */
    public String getURL() {
        
        // Voer query uit
        String query = "SELECT " + KEY_SITE + " FROM " + TABLE_LOGIN;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        
        // Haal gegevens op
        cursor.moveToFirst();
        String site = cursor.getString(cursor.getColumnIndexOrThrow(KEY_SITE));
        
        cursor.close();
        db.close();
        
        return site;
    }
    
    /**
     * Haalt de gebruikersnaam op van de gebruiker.
     * 
     * @return {@link String} De gebruikersnaam.
     */
    public String getGebruikersnaam() {
     
        // Voer query uit
        String query = "SELECT " + KEY_GEBRUIKER + " FROM " + TABLE_LOGIN;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        
        // Haal gegevens op
        cursor.moveToFirst();
        String gebruikersnaam = cursor.getString(cursor.getColumnIndexOrThrow(KEY_GEBRUIKER));
        
        cursor.close();
        db.close();
        
        return gebruikersnaam;
    }
    
    /**
     * Haalt het wachtwoord op van de gebruiker.
     * 
     * @return {@link String} Het wachtwoord.
     */
    public String getWachtwoord() {
        
        // Voer query uit
        String query = "SELECT " + KEY_WACHTWOORD + " FROM " + TABLE_LOGIN;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        
        // Haal gegevens op
        cursor.moveToFirst();
        String wachtwoord = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WACHTWOORD));
        
        cursor.close();
        db.close();
        
        return wachtwoord;
    }
    
    /**
     * Voegt een nieuwe tafel toe.
     * 
     * @param id {@link Integer} Het nummer van de tafel.
     * @param status {@link Integer} De status van de tafel. 0 is vrij, 1 is bezet.
     * 
     * @return {@link Boolean} True als de tafel is toegevoegd
     */
    public boolean voegTafelToe(int id, int status) {
        
        // Waardes om toe te voegen
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_STATUS, status);
        
        // Voer query uit
        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_TAFEL, null, values);
        
        db.close();
        
        // Controleer of het toevoegen gelukt is
        if (result != -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verwijdert alle tafels uit de database.
     * 
     * @return {@link Boolean} True als de tafels verwijderd zijn.
     */
    public boolean leegTafels() {
        
        // Voer query uit
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(TABLE_TAFEL, "1", null);
        
        db.close();
        
        // Controleer of het verwijderen gelukt is
        if (result > 0) {
           return true; 
        } else {
            return false;
        }
        
    }
    
    /**
     * Haalt alle tafels op uit de database.
     * 
     * De status wordt weergegeven als localized string.
     * 
     * @return
     */
    public ArrayList<Map<String, Object>> getTafels() {
        ArrayList<Map<String, Object>> tafels = new ArrayList<Map<String, Object>>();
        
        // Voer query uit
        String query = "SELECT " + KEY_ID + ", " + KEY_STATUS + " FROM " + TABLE_TAFEL;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        
        // Haal gegevens op
        while (cursor.moveToNext()) {
            Map<String, Object> tafel = new HashMap<String, Object>();
            tafel.put(KEY_ID, Integer.toString(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))));
            if (cursor.getInt(cursor.getColumnIndexOrThrow(KEY_STATUS)) == 0) {
                tafel.put(KEY_STATUS, R.drawable.ic_tafel_vrij);
            } else {
                tafel.put(KEY_STATUS, R.drawable.ic_tafel_bezet);
            }
            tafels.add(tafel);
        }
        
        cursor.close();
        db.close();
        
        return tafels;
        
    }
    
    /**
     * Voegt een nieuw product toe.
     * 
     * @param id {@link Integer} Het nummer van het product.
     * @param categorienr {@link Integer} Het nummer van de categorie.
     * @param gerecht {@link String} De naam van het product.
     * @param prijs {@link Double} De prijs van het product.
     * @param actief {@link Integer} De status van het product, <code>true</code> is actief.
     * 
     * @return {@link Boolean} True als het product is toegevoegd.
     */
    public boolean voegProductToe(int id, int categorienr, String gerecht, double prijs, boolean actief) {
        
        // Waardes om toe te voegen
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_CATEGORIENR, categorienr);
        values.put(KEY_GERECHT, gerecht);
        values.put(KEY_PRIJS, prijs);
        values.put(KEY_ACTIEF, actief);
        
        // Voer query uit
        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_PRODUCTEN, null, values);
        
        db.close();
        
        // Controleer of het toevoegen gelukt is
        if (result != -1) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Verwijdert alle producten uit de database.
     * 
     * @return {@link Boolean} True als de producten verwijderd zijn.
     */
    public boolean leegProducten() {
        
        // Voer query uit
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(TABLE_PRODUCTEN, "1", null);
        
        db.close();
        
        // Controleer of het verwijderen gelukt is
        if (result > 0) {
           return true; 
        } else {
            return false;
        }
        
    }
    
    /**
     * Haalt alle producten op uit de database.
     * 
     * @return
     */
    public ArrayList<Map<String, Object>> getProducten() {
        ArrayList<Map<String, Object>> producten = new ArrayList<Map<String, Object>>();
        
        // Voer query uit
        String query = "SELECT " + KEY_ID + ", " + KEY_CATEGORIENR + ", " + KEY_GERECHT + ", "
                + KEY_PRIJS + ", " + KEY_ACTIEF + " FROM " + TABLE_PRODUCTEN + "ORDER BY " + KEY_GERECHT + " ASC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // Haal gegevens op
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ACTIEF)) == 1) {
                Map<String, Object> product = new HashMap<String, Object>();
                product.put(KEY_ID, cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                product.put(KEY_CATEGORIENR,
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CATEGORIENR)));
                product.put(KEY_GERECHT,
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_GERECHT)));
                product.put(KEY_PRIJS, cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRIJS)));
                product.put(
                        KEY_PRIJS_FORMATTED,
                        NumberFormat.getCurrencyInstance().format(
                                cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRIJS))));
                producten.add(product);
            }
        }
        
        cursor.close();
        db.close();
        
        return producten;
        
    }
    
    /**
     * Haalt alle producten op uit een bepaalde categorie uit de database.
     * 
     * @return
     */
    public ArrayList<Map<String, Object>> getProducten(int categorienr) {
        ArrayList<Map<String, Object>> producten = new ArrayList<Map<String, Object>>();
        
        // Voer query uit
        String query = "SELECT " + KEY_ID + ", " + KEY_CATEGORIENR + ", " + KEY_GERECHT + ", "
                + KEY_PRIJS + ", " + KEY_ACTIEF + " FROM " + TABLE_PRODUCTEN + " WHERE "
                + KEY_CATEGORIENR + "=? ORDER BY " + KEY_GERECHT + " ASC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[] {
            Integer.toString(categorienr)
        });

        // Haal gegevens op
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ACTIEF)) == 1) {
                Map<String, Object> product = new HashMap<String, Object>();
                product.put(KEY_ID, cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                product.put(KEY_CATEGORIENR,
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CATEGORIENR)));
                product.put(KEY_GERECHT,
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_GERECHT)));
                product.put(KEY_PRIJS, cursor
                        .getDouble(cursor.getColumnIndexOrThrow(KEY_PRIJS)));
                product.put(
                        KEY_PRIJS_FORMATTED,
                        NumberFormat.getCurrencyInstance().format(
                                cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRIJS))));
                producten.add(product);
            }
        }
        
        cursor.close();
        db.close();
        
        return producten;
        
    }
    
}
