
package nl.rgomiddelharnis.a6.po.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockListFragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import nl.rgomiddelharnis.a6.po.DatabaseHandler;
import nl.rgomiddelharnis.a6.po.R;
import nl.rgomiddelharnis.a6.po.activity.BeheerTafelActivity;
import nl.rgomiddelharnis.a6.po.task.BestelTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Fragment met een lijst van alle bestellingen voor de geselecteerde tafel.
 * 
 * @author Sven Dubbeld <sven.dubbeld1@gmail.com>
 */
public class BestellijstFragment extends SherlockListFragment {

    /**
     * Tag voor in logs.
     * 
     * @see android.util.Log
     */
    @SuppressWarnings("unused")
    private static final String TAG = "BestellijstFragment";

    Context mContext;
    DatabaseHandler mDb;

    int mTafel;
    
    public BestellijstFragment() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Laat alle bestellingen zien.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        
        mContext = getSherlockActivity();
        mDb = new DatabaseHandler(mContext);

        mTafel = ((BeheerTafelActivity) mContext).getTafel();
        
        // Verkrijg data van database
        ArrayList<Map<String, Object>> data = mDb.getBestellingen(((BeheerTafelActivity) getSherlockActivity()).getTafel());

        // Gegevens om te gebruiken...
        String[] from = {
                DatabaseHandler.KEY_GERECHT, DatabaseHandler.KEY_PRIJS_FORMATTED, DatabaseHandler.KEY_AANTAL
        };

        // ... en toe te voegen aan deze velden.
        int[] to = {
                R.id.lbl_gerecht, R.id.lbl_prijs, R.id.lbl_aantal
        };

        // Maak een SimpleAdapter aan voor het verwerken van de gegevens.
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, R.layout.list_item_bestelling,
                from, to);

        // Pas de adapter toe op de lijst.
        setListAdapter(adapter);

        return inflater.inflate(R.layout.fragment_bestellingen_list, container, false);
        
    }
    
    /**
     * Past een bestelling aan zodra deze gekozen wordt.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // Haal het opgegeven item op
        Map<String, Object> selected = (Map<String, Object>) getListAdapter().getItem(position);

        pasBestellingAanDialog(selected);
    }
    
    /**
     * Laat een {@link Dialog} zien om een bestelling aan te passen.
     * 
     * @param selected {@link Map}<{@link String}, {@link Object}> Het gekozen item.
     */
    private void pasBestellingAanDialog(final Map<String, Object> selected) {
        // Maak een builder aan
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        // Maak layout aan
        LayoutInflater inflater = ((BeheerTafelActivity) mContext).getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_bestel, null);

        // Stel het dialog in
        builder.setTitle(selected.get(DatabaseHandler.KEY_GERECHT).toString());
        builder.setView(layout);

        // Maak EditText aan
        final EditText txtAantal = (EditText) layout.findViewById(R.id.txt_aantal);
        txtAantal.setText(selected.get(DatabaseHandler.KEY_AANTAL).toString());
        final EditText txtOpmerking = (EditText) layout.findViewById(R.id.txt_opmerking);
        txtOpmerking.setText(selected.get(DatabaseHandler.KEY_OPMERKING).toString());
        

        // Maak buttons aan
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            /**
             * Maakt een <code>BestelTask</code> aan om de bestelling aan te
             * passen.
             */
            @SuppressWarnings("unchecked")
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Bereid de verbindingsparameters voor
                List<NameValuePair> params = new ArrayList<NameValuePair>(6);
                params.add(new BasicNameValuePair("tag", "pas_bestelling_aan"));
                params.add(new BasicNameValuePair("gebruikersnaam", mDb.getGebruikersnaam()));
                params.add(new BasicNameValuePair("wachtwoord", mDb.getWachtwoord()));
                params.add(new BasicNameValuePair("bestellingnummer", selected.get(DatabaseHandler.KEY_BESTELNR).toString()));
                params.add(new BasicNameValuePair("aantal", txtAantal.getText().toString()));
                params.add(new BasicNameValuePair("opmerking", txtOpmerking.getText().toString()));

                new BestelTask(mContext).execute(params);

                // Sluit dialog
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

            /**
             * Annuleert het dialog.
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Laat dialog zien
        builder.show();
    }
    
}
