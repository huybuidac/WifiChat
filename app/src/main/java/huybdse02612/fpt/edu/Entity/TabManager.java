package huybdse02612.fpt.edu.Entity;

import android.support.v4.app.Fragment;

import android.support.v4.view.ViewPager;
import android.widget.TabHost;

import java.util.List;
import java.util.Vector;

import huybdse02612.fpt.edu.Activity.MainActivity;
import huybdse02612.fpt.edu.Adaptor.MyPagerAdaptor;
import huybdse02612.fpt.edu.Fragment.ChatFragment;
import huybdse02612.fpt.edu.Fragment.PeopleFragment;
import huybdse02612.fpt.edu.Fragment.SettingFragment;

/**
 * Created by hoang anh tuan on 7/22/2015.
 */
public class TabManager implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private TabHost mTabHost;
    private MainActivity mMainAtivity;
    private MyPagerAdaptor mPagerAdapter;

    public TabManager(TabHost tabHost, ViewPager viewPager, MainActivity activity) {
            this.mTabHost = tabHost;
            this.mViewPager = viewPager;
            this.mMainAtivity = activity;
            setupTabHost();
            setupViewPager();
    }

    private void setupTabHost() {
        mTabHost.setup();
        TabFactory tf = new TabFactory(mMainAtivity);
        mTabHost.addTab(this.mTabHost.newTabSpec("People").setIndicator("People").setContent(tf));
        mTabHost.addTab(this.mTabHost.newTabSpec("Chat").setIndicator("Chat").setContent(tf));
        mTabHost.addTab(this.mTabHost.newTabSpec("Setting").setIndicator("Setting").setContent(tf));
        mTabHost.setOnTabChangedListener(this);
    }

    private void setupViewPager() {
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(mMainAtivity.getApplicationContext(), PeopleFragment.class.getName()));
        fragments.add(Fragment.instantiate(mMainAtivity.getApplicationContext(), ChatFragment.class.getName()));
        fragments.add(Fragment.instantiate(mMainAtivity.getApplicationContext(), SettingFragment.class.getName()));
        this.mPagerAdapter = new MyPagerAdaptor(mMainAtivity.getSupportFragmentManager(), fragments);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onTabChanged(String tabId) {
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }

    @Override
    public void onPageSelected(int position) {
        this.mTabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

}
