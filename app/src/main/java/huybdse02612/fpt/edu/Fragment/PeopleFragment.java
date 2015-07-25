package huybdse02612.fpt.edu.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import huybdse02612.fpt.edu.Entity.CommandMessage;
import huybdse02612.fpt.edu.Entity.ListUsers;
import huybdse02612.fpt.edu.Entity.User;
import huybdse02612.fpt.edu.R;
import huybdse02612.fpt.edu.Util.ConstantVariance;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends Fragment {

    private View mPeopleView;
    private ListView mLvPeople;
    private ArrayAdapter<String> mAdaptorPeople;
    private ListUsers mLstUsers;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String myAction = intent.getStringExtra(ConstantVariance.ACTION);
                if (myAction.equals(ConstantVariance.ACTION_ADD_USER)) {
                    User user = (User) intent.getSerializableExtra(ConstantVariance.EXTRA_USER);
                    mLstUsers.addUser(user);
                    mAdaptorPeople.clear();
                    mAdaptorPeople.addAll(mLstUsers.getListUserName());
                    mAdaptorPeople.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private ViewGroup mContainer;

    private void getViewFromLayout() {
        mLvPeople = (ListView) mPeopleView.findViewById(R.id.listView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContainer = container;
        mPeopleView = inflater.inflate(R.layout.fragment_people, container, false);

        getViewFromLayout();
        initData();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(ConstantVariance.MY_BROADCAST));
        return mPeopleView;
    }

    private void initData() {

        mLstUsers = new ListUsers();

        mAdaptorPeople = new ArrayAdapter<String>
                (mPeopleView.getContext(), android.R.layout.simple_list_item_1, mLstUsers.getListUserName());

        mLvPeople.setAdapter(mAdaptorPeople);

        mLvPeople.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((ViewPager) getActivity().findViewById(R.id.pager)).setCurrentItem(1);
                mContainer.setTag(ConstantVariance.TAG_USER_SELECTED, mLstUsers.getUserByIndex(position));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(ConstantVariance.MY_BROADCAST));
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}
