package huybdse02612.fpt.edu.Entity;

import android.content.Context;
import android.view.View;
import android.widget.TabHost;

/**
 * Created by hoang anh tuan on 7/22/2015.
 */
public class TabFactory implements TabHost.TabContentFactory {

    private final Context mContext;
    public TabFactory(Context context) {
        mContext = context;
    }

    public View createTabContent(String tag) {
        View v = new View(mContext);
        v.setMinimumWidth(0);
        v.setMinimumHeight(0);
        return v;
    }

}