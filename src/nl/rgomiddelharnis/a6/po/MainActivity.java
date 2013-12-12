
package nl.rgomiddelharnis.a6.po;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;

public class MainActivity extends SherlockFragmentActivity {
    
    ActionBar mActionBar;
    public static final boolean LOCAL_LOGD = true;
    public static final boolean LOCAL_LOGV = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mActionBar = getSupportActionBar();
        
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
