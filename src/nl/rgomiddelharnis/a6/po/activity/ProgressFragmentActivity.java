
package nl.rgomiddelharnis.a6.po.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import nl.rgomiddelharnis.a6.po.R;

public abstract class ProgressFragmentActivity extends SherlockFragmentActivity {

    private ProgressBar mProgressBar;

    @Override
    public void setContentView(View view) {
        init().addView(view);
    }

    @Override
    public void setContentView(int layoutResID) {
        getLayoutInflater().inflate(layoutResID, init(), true);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        init().addView(view, params);
    }

    private ViewGroup init() {
        super.setContentView(R.layout.activity_progress_fragment);
        mProgressBar = (ProgressBar) findViewById(R.id.activity_bar);
        hideProgressBar();
        return (ViewGroup) findViewById(R.id.activity_frame);
    }

    protected ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }
    
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }
    
    public void setIndeterminate(boolean indeterminate) {
        mProgressBar.setIndeterminate(indeterminate);
    }

}
