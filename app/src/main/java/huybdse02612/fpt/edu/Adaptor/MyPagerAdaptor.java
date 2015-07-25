package huybdse02612.fpt.edu.Adaptor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by hoang anh tuan on 7/22/2015.
 */
public class MyPagerAdaptor extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    public MyPagerAdaptor(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }
    @Override
    public Fragment getItem(int position) {
        return this.mFragments.get(position);
    }

    @Override
    public int getCount() {
        return this.mFragments.size();
    }
}
