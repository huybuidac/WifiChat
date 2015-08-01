package huybdse02612.fpt.edu.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import huybdse02612.fpt.edu.Entity.TabManager;
import huybdse02612.fpt.edu.R;
import huybdse02612.fpt.edu.Service.ServerService;


public class MainActivity extends FragmentActivity {

    private final String TAG = this.getClass().getName();
    private ViewPager mViewPager;
    private TabHost mTabHost;
    private TabManager mTabBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViewFromLayout();
        mTabBar = new TabManager(mTabHost,mViewPager,this);
    }

    public void getViewFromLayout() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mViewPager = (ViewPager) findViewById(R.id.pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            stopService(new Intent(getApplicationContext(), ServerService.class));
            mViewPager.setTag(R.id.TAG_SERVICE_IS_START,false);
        } catch (Exception e) {
            Log.e(TAG,"MainACtivity exception");
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
