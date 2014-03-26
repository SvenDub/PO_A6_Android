
package nl.rgomiddelharnis.a6.po;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    public static final int DATABASE_VERSION = 2;

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
     * De tabel om alle bestellingen in op te slaan.
     */
    public static final String TABLE_BESTELLINGEN = "bestellingen";

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
     * De key voor registratie id.
     */
    public static final String KEY_REG_ID = "registration_id";
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
     * De key voor productnummer.
     */
    public static final String KEY_PRODUCTNR = "productnummer";
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
    /**
     * De key voor tafelnummer.
     */
    public static final String KEY_TAFELNR = "tafelnummer";
    /**
     * De key voor bestelnummer.
     */
    public static final String KEY_BESTELNR = "bestelnummer";
    /**
     * De key voor aantal.
     */
    public static final String KEY_AANTAL = "aantal";
    /**
     * De key voor opmerking.
     */
    public static final String KEY_OPMERKING = "opmerking";
    /**
     * De key voor datum.
     */
    public static final String KEY_DATUM = "datum";

    /**
     * De query voor het aanmaken van de {@link #TABLE_LOGIN}.
     */
    private static final String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "(" + KEY_ID
            + " INTEGER PRIMARY KEY, " + KEY_GEBRUIKER + " TEXT UNIQUE, " + KEY_WACHTWOORD
            + " TEXT," + KEY_SITE + " TEXT," + KEY_REG_ID + " TEXT" + ")";
    /**
     * De query voor het aanmaken van de {@link #TABLE_TAFEL}.
     */
    private static final String CREATE_TAFEL_TABLE = "CREATE TABLE " + TABLE_TAFEL + "(" + KEY_TAFELNR
            + " INTEGER PRIMARY KEY, " + KEY_STATUS + " INTEGER" + ")";
    /**
     * De query voor het aanmaken van de {@link #TABLE_PRODUCTEN}.
     */
    private static final String CREATE_PRODUCTEN_TABLE = "CREATE TABLE " + TABLE_PRODUCTEN + "(" + KEY_PRODUCTNR
            + " INTEGER PRIMARY KEY, " + KEY_CATEGORIENR + " INTEGER," + KEY_GERECHT + " TEXT,"
            + KEY_PRIJS + " DOUBLE," + KEY_ACTIEF + " INTEGER" + ")";
    /**
     * De query voor het aanmaken van de {@link #TABLE_BESTELLINGEN}.
     */
    private static final String CREATE_BESTELLINGEN_TABLE = "CREATE TABLE " + TABLE_BESTELLINGEN + "("
            + KEY_BESTELNR + " INTEGER PRIMARY KEY, " + KEY_TAFELNR + " INTEGER, " + KEY_ID
            + " INTEGER, " + KEY_PRODUCTNR + " INTEGER, " + KEY_AANTAL + " INTEGER, "
            + KEY_OPMERKING + " TEXT, " + KEY_DATUM + " TEXT, " + KEY_STATUS + " INTEGER" + ")";

    /**
     * De query voor het verwijderen van de {@link #TABLE_LOGIN}.
     */
    private static final String REMOVE_LOGIN_TABLE = "DROP TABLE " + TABLE_LOGIN;
    /**
     * De query voor het verwijderen van de {@link #TABLE_TAFEL}.
     */
    private static final String REMOVE_TAFEL_TABLE = "DROP TABLE " + TABLE_TAFEL;
    /**
     * De query voor het verwijderen van de {@link #TABLE_PRODUCTEN}.
     */
    private static final String REMOVE_PRODUCTEN_TABLE = "DROP TABLE " + TABLE_PRODUCTEN;
    /**
     * De query voor het verwijderen van de {@link #TABLE_BESTELLINGEN}.
     */
    private static final String REMOVE_BESTELLINGEN_TABLE = "DROP TABLE " + TABLE_BESTELLINGEN;

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
        // Maak tabellen aan
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_TAFEL_TABLE);
        db.execSQL(CREATE_PRODUCTEN_TABLE);
        db.execSQL(CREATE_BESTELLINGEN_TABLE);
    }

    /**
     * Werkt de upgrade naar een nieuwere versie.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Voer een actie uit afhankelijk van de vorige versie
        if (oldVersion == 1) {
            // Maak login table opnieuw aan
            db.execSQL(REMOVE_LOGIN_TABLE);
            db.execSQL(CREATE_LOGIN_TABLE);
        }
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
     * @return {@link Boolean} True als er ingelogd is.
     */
    public boolean login(int id, String gebruikersnaam, String wachtwoord, String site, String regid) {

        // Log de vorige gebruiker uit
        logout();

        // Waardes om toe te voegen
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_GEBRUIKER, gebruikersnaam);
        values.put(KEY_WACHTWOORD, wachtwoord);
        values.put(KEY_SITE, site);
        values.put(KEY_REG_ID, regid);

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
     * Haalt het registratie id op van de gebruiker.
     * 
     * @return {@link String} Het wachtwoord.
     */
    public String getRegistratieId() {

        // Voer query uit
        String query = "SELECT " + KEY_REG_ID + " FROM " + TABLE_LOGIN;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // Haal gegevens op
        cursor.moveToFirst();
        String regid = cursor.getString(cursor.getColumnIndexOrThrow(KEY_REG_ID));

        cursor.close();
        db.close();

        return regid;
    }

    /**
     * Voegt een nieuwe tafel toe.
     * 
     * @param tafelnummer {@link Integer} Het nummer van de tafel.
     * @param status {@link Integer} De status van de tafel. 0 is vrij, 1 is
     *            bezet.
     * @return {@link Boolean} True als de tafel is toegevoegd
     */
    public boolean voegTafelToe(int tafelnummer, int status) {

        // Waardes om toe te voegen
        ContentValues values = new ContentValues();
        values.put(KEY_TAFELNR, tafelnummer);
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
     * Haalt alle tafels op uit de database. De status wordt weergegeven als
     * icon.
     * 
     * @return
     */
    public ArrayList<Map<String, Object>> getTafels() {
        ArrayList<Map<String, Object>> tafels = new ArrayList<Map<String, Object>>();

        // Voer query uit
        String query = "SELECT " + KEY_TAFELNR + ", " + KEY_STATUS + " FROM " + TABLE_TAFEL;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // Haal gegevens op
        while (cursor.moveToNext()) {
            Map<String, Object> tafel = new HashMap<String, Object>();
            tafel.put(KEY_TAFELNR,
                    Integer.toString(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_TAFELNR))));
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
     * Haalt de status van een tafel op.
     * 
     * @param tafelnummer {@link Integer} Het numer van de tafel.
     * @return boolean De status van de tafel.
     */
    public boolean isTafelBezet(int tafelnummer) {
        boolean result = false;

        // Voer query uit
        String query = "SELECT " + KEY_STATUS + " FROM " + TABLE_TAFEL + " WHERE " + KEY_TAFELNR
                + "=?";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[] {
                Integer.toString(tafelnummer)
        });

        // Haal gegevens op
        cursor.moveToFirst();
        int status = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_STATUS));

        result = (status == 1) ? true : false;

        cursor.close();
        db.close();

        return result;

    }

    /**
     * Voegt een nieuw product toe.
     * 
     * @param productnummer {@link Integer} Het nummer van het product.
     * @param categorienr {@link Integer} Het nummer van de categorie.
     * @param gerecht {@link String} De naam van het product.
     * @param prijs {@link Double} De prijs van het product.
     * @param actief {@link Integer} De status van het product,
     *            <code>true</code> is actief.
     * @return {@link Boolean} True als het product is toegevoegd.
     */
    public boolean voegProductToe(int productnummer, int categorienr, String gerecht, double prijs,
            boolean actief) {

        // Waardes om toe te voegen
        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCTNR, productnummer);
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

        // Haal valuta op
        NumberFormat numberFormat = Functions.getNumberFormat(mContext);

        // Voer query uit
        String query = "SELECT " + KEY_PRODUCTNR + ", " + KEY_CATEGORIENR + ", " + KEY_GERECHT
                + ", "
                + KEY_PRIJS + ", " + KEY_ACTIEF + " FROM " + TABLE_PRODUCTEN + "ORDER BY "
                + KEY_GERECHT + " ASC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // Haal gegevens op
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ACTIEF)) == 1) {
                Map<String, Object> product = new HashMap<String, Object>();
                product.put(KEY_PRODUCTNR,
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PRODUCTNR)));
                product.put(KEY_CATEGORIENR,
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CATEGORIENR)));
                product.put(KEY_GERECHT,
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_GERECHT)));
                product.put(KEY_PRIJS, cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRIJS)));
                product.put(
                        KEY_PRIJS_FORMATTED,
                        numberFormat.format(
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
     * @param {@link Integer} Het nummer van de categorie
     * @return
     */
    public ArrayList<Map<String, Object>> getProducten(int categorienr) {
        ArrayList<Map<String, Object>> producten = new ArrayList<Map<String, Object>>();

        // Haal valuta op
        NumberFormat numberFormat = Functions.getNumberFormat(mContext);

        // Voer query uit
        String query = "SELECT " + KEY_PRODUCTNR + ", " + KEY_CATEGORIENR + ", " + KEY_GERECHT
                + ", "
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
                product.put(KEY_PRODUCTNR,
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PRODUCTNR)));
                product.put(KEY_CATEGORIENR,
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CATEGORIENR)));
                product.put(KEY_GERECHT,
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_GERECHT)));
                product.put(KEY_PRIJS, cursor
                        .getDouble(cursor.getColumnIndexOrThrow(KEY_PRIJS)));
                product.put(
                        KEY_PRIJS_FORMATTED,
                        numberFormat.format(
                                cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRIJS))));
                producten.add(product);
            }
        }

        cursor.close();
        db.close();

        return producten;

    }

    /**
     * Haalt de prijs van een product op.
     * 
     * @param {@link Integer} Het nummer van het product.
     * @param {@link Integer} Het aantal producten.
     * @return {@link String} De prijs van het product.
     */
    public String getProductPrijs(int productnr, int aantal) {

        // Haal valuta op
        NumberFormat numberFormat = Functions.getNumberFormat(mContext);

        // Voer query uit
        String query = "SELECT " + KEY_PRIJS + " FROM " + TABLE_PRODUCTEN + " WHERE "
                + KEY_PRODUCTNR + "=?";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[] {
                Integer.toString(productnr)
        });

        // Haal gegevens op
        cursor.moveToFirst();
        double prijs = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRIJS));
        prijs *= aantal;

        cursor.close();
        db.close();

        return numberFormat.format(prijs);
    }

    /**
     * Haalt de naam van een product op.
     * 
     * @param {@link Integer} Het nummer van het product.
     * @return {@link String} De naam van het product.
     */
    public String getProductNaam(int productnr) {

        // Voer query uit
        String query = "SELECT " + KEY_GERECHT + " FROM " + TABLE_PRODUCTEN + " WHERE "
                + KEY_PRODUCTNR + "=?";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[] {
                Integer.toString(productnr)
        });

        // Haal gegevens op
        cursor.moveToFirst();
        String naam = cursor.getString(cursor.getColumnIndexOrThrow(KEY_GERECHT));

        cursor.close();
        db.close();

        return naam;
    }

    /**
     * Voegt een nieuwe bestelling toe.
     * 
     * @param bestelnummer {@link Integer} Het nummer van de bestelling.
     * @param tafelnummer {@link Integer} Het nummer van de tafel.
     * @param inlog_id {@link Integer} Het id van de ober die de bestelling
     *            heeft toegevoegd.
     * @param product {@link Integer} Het nummer van het bestelde product.
     * @param aantal_besteld {@link Integer} Het aantal bestelde producten.
     * @param opmerking {@link String} Een eventuele opmerking.
     * @param datum {@link String} De datum dat de bestelling is toegevoegd.
     * @param status {@link Integer} De status van de bestelling.
     * @return {@link Boolean} True als de bestelling is toegevoegd.
     */
    public boolean voegBestellingToe(int bestelnummer, int tafelnummer, int id, int product,
            int aantal_besteld, String opmerking, String datum, int status) {

        // Waardes om toe te voegen
        ContentValues values = new ContentValues();
        values.put(KEY_BESTELNR, bestelnummer);
        values.put(KEY_TAFELNR, tafelnummer);
        values.put(KEY_ID, id);
        values.put(KEY_PRODUCTNR, product);
        values.put(KEY_AANTAL, aantal_besteld);
        values.put(KEY_OPMERKING, opmerking);
        values.put(KEY_DATUM, datum);
        values.put(KEY_STATUS, status);

        // Voer query uit
        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_BESTELLINGEN, null, values);

        db.close();

        // Controleer of het toevoegen gelukt is
        if (result != -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verwijdert alle bestellingen uit de database.
     * 
     * @return {@link Boolean} True als de bestellingen verwijderd zijn.
     */
    public boolean leegBestellingen() {

        // Voer query uit
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(TABLE_BESTELLINGEN, "1", null);

        db.close();

        // Controleer of het verwijderen gelukt is
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Haalt alle bestellingen van een bepaalde tafel op uit de database.
     * 
     * @return
     */
    public ArrayList<Map<String, Object>> getBestellingen(int tafelnr) {
        ArrayList<Map<String, Object>> bestellingen = new ArrayList<Map<String, Object>>();

        // Voer query uit
        String query = "SELECT " + KEY_BESTELNR + ", " + KEY_TAFELNR + ", " + KEY_ID
                + ", " + KEY_PRODUCTNR + ", " + KEY_AANTAL + ", " + KEY_OPMERKING + ", "
                + KEY_DATUM + ", " + KEY_STATUS + " FROM " + TABLE_BESTELLINGEN + " WHERE "
                + KEY_TAFELNR + "=? ORDER BY " + KEY_BESTELNR + " ASC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[] {
                Integer.toString(tafelnr)
        });

        // Haal gegevens op
        while (cursor.moveToNext()) {
            Map<String, Object> bestelling = new HashMap<String, Object>();
            int productnr = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PRODUCTNR));
            bestelling.put(KEY_BESTELNR,
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BESTELNR)));
            bestelling.put(KEY_TAFELNR,
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_TAFELNR)));
            bestelling.put(KEY_ID, cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
            bestelling.put(KEY_PRODUCTNR, productnr);
            bestelling.put(KEY_AANTAL,
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_AANTAL)));
            bestelling.put(KEY_OPMERKING,
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_OPMERKING)));
            bestelling.put(KEY_DATUM,
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATUM)));
            bestelling.put(KEY_STATUS,
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_STATUS)));
            bestelling.put(
                    KEY_PRIJS_FORMATTED,
                    getProductPrijs(productnr,
                            cursor.getInt(cursor.getColumnIndexOrThrow(KEY_AANTAL))));
            bestelling.put(KEY_GERECHT, getProductNaam(productnr));
            bestellingen.add(bestelling);
        }

        cursor.close();
        db.close();

        return bestellingen;

    }

    /**
     * Haalt de totale prijs van een openstaande bestelling op van een tafel.
     * 
     * @param tafelnr {@link Integer} Het nummer van de tafel.
     * @return {@link Double} De prijs van de bestelling.
     */
    public double getBestellingPrijs(int tafelnr) {

        // Voer query uit
        String query = "SELECT COALESCE(SUM(B." + KEY_AANTAL + " * P." + KEY_PRIJS + " ), 0) FROM "
                + TABLE_BESTELLINGEN + " B, " + TABLE_PRODUCTEN + " P WHERE "
                + KEY_TAFELNR + "=? AND B." + KEY_PRODUCTNR + "= P." + KEY_PRODUCTNR;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[] {
                Integer.toString(tafelnr)
        });

        // Haal gegevens op
        cursor.moveToFirst();
        double prijs = cursor.getDouble(0);

        cursor.close();
        db.close();

        return prijs;
    }

    /**
     * Haalt de totale prijs van een openstaande bestelling op van een tafel als
     * opgemaakte String.
     * 
     * @param tafelnr {@link Integer} Het nummer van de tafel.
     * @return {@link String} De prijs van de bestelling als String met valuta.
     */
    public String getBestellingPrijsFormatted(int tafelnr) {

        // Haal valuta op
        NumberFormat numberFormat = Functions.getNumberFormat(mContext);

        // Haal prijs op
        double prijs = getBestellingPrijs(tafelnr);

        return numberFormat.format(prijs);
    }
}
