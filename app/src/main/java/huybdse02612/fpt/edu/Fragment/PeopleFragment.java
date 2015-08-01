package huybdse02612.fpt.edu.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import huybdse02612.fpt.edu.Entity.BadgeView;
import huybdse02612.fpt.edu.Entity.CommandMessage;
import huybdse02612.fpt.edu.Entity.CommandMessageType;
import huybdse02612.fpt.edu.Entity.ListUsers;
import huybdse02612.fpt.edu.Entity.User;
import huybdse02612.fpt.edu.R;
import huybdse02612.fpt.edu.Service.ServerService;
import huybdse02612.fpt.edu.Util.ConstantValue;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends Fragment {

    private static final int UPDATE_LISTVIEW = 666;
    private final String TAG = this.getClass().getName();
    private View mPeopleView;
    private ListView mLvPeople;
    private MyPeopleAdaptor mAdaptorPeople;

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_LISTVIEW:
                    mAdaptorPeople.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
    private ListUsers mLstUsers;
    private ViewGroup mContainer;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "GOTO broadcastReceiver onReceive");
            try {
                String myAction = intent.getStringExtra(ConstantValue.ACTION);
                if (myAction.equals(ConstantValue.ACTION_ADD_USER)) {
                    final User user = (User) intent.getSerializableExtra(ConstantValue.EXTRA_USER);
                    final boolean needRespond = intent.getBooleanExtra(ConstantValue.EXTRA_NEED_RESPOND, false);
                    mLstUsers.addUser(user);
                    if (needRespond) {
                        getActivity().startService(new Intent(getActivity(), ServerService.class)
                                .setAction(ConstantValue.ACTION_CONNECT)
                                .putExtra(ConstantValue.EXTRA_COMMAND_MESSAGE,
                                        new CommandMessage(CommandMessageType.CONNECT_RESPOND,
                                                mContainer.getTag(R.id.TAG_USER_NAME).toString(),
                                                "", user.getIpAddress())));
                    }
                }
                if (myAction.equals(ConstantValue.ACTION_RECEIVE_MESSAGE)) {
                    CommandMessage cmd = (CommandMessage) intent.getSerializableExtra(ConstantValue.EXTRA_CMD);
                    int curFrag = ((ViewPager) getActivity().findViewById(R.id.pager)).getCurrentItem();

                    User user = (User) mContainer.getTag(R.id.TAG_USER_SELECTED);
                    if (curFrag == 0 || user == null || !user.getIpAddress().equals(cmd.getSenderAddress())) {
                        mLstUsers.getUserByIP(cmd.getSenderAddress()).addMessage(cmd.getmFromUser() + ": " + cmd.getContent(), true);
                    } else {
                        mLstUsers.getUserByIP(cmd.getSenderAddress()).addMessage(cmd.getmFromUser() + ": " + cmd.getContent(), false);
                    }
                }
                if (myAction.equals(ConstantValue.ACTION_STOP_SERVICE)){
                    mLstUsers.clear();
                    mLvPeople.invalidate();
                }
                runUpdateThread();
            } catch (Exception e) {
                Log.e(TAG, "BroadcastReceive onReceive exception");
                e.printStackTrace();
            }
            Log.d(TAG, "OUT broadcastReceiver onReceive");
        }
    };

    private void getViewFromLayout() {
        Log.d(TAG, "GOTO getViewFromLayout");
        mLvPeople = (ListView) mPeopleView.findViewById(R.id.listView);
        Log.d(TAG, "OUT getViewFromLayout");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "GOTO onCreateView");
        mContainer = container;
        mPeopleView = inflater.inflate(R.layout.fragment_people, container, false);

        getViewFromLayout();
        initData();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(ConstantValue.MY_BROADCAST));
        Log.d(TAG, "OUT onCreateView");
        return mPeopleView;
    }

    private void initData() {
        Log.d(TAG, "GOTO initData");
        mLstUsers = new ListUsers();

        mAdaptorPeople = new MyPeopleAdaptor(getActivity().getApplicationContext());

        mLvPeople.setAdapter(mAdaptorPeople);

        mLvPeople.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mContainer.setTag(R.id.TAG_USER_SELECTED, mLstUsers.getUserByIndex(position));
                ((ViewPager) getActivity().findViewById(R.id.pager)).setCurrentItem(1);
                mLstUsers.getUserByIndex(position).setCount(0);
                mLvPeople.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdaptorPeople.notifyDataSetChanged();
                    }
                });
            }
        });

        registerForContextMenu(mLvPeople);
        Log.d(TAG, "OUT initData");
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.listView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle("TEST");
            menu.add("gege");
            menu.add("gege");
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        
        return true;
    }
    @Override
    public void onResume() {
        Log.d(TAG, "GOTO onResume");
        super.onResume();
        Log.d(TAG, "OUT onResume");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "GOTO onDestroy");
        try {
            getActivity().unregisterReceiver(broadcastReceiver);
            getActivity().stopService(new Intent(getActivity().getApplicationContext(), ServerService.class));
            mContainer.setTag(R.id.TAG_SERVICE_IS_START,false);
        } catch (Exception e) {
            Log.e(TAG,"onDestroy");
            e.printStackTrace();
        }
        Log.d(TAG, "OUT onDestroy");
    }

    private synchronized void runUpdateThread() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                            myHandler.obtainMessage(UPDATE_LISTVIEW)
                                    .sendToTarget();
                        } catch (Exception e) {
                            Log.d(TAG, "sleep failure");
                        }

                    }
                }).start();
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        try {
            if (menuVisible){
                runUpdateThread();
            }
        } catch (Exception e) {
            Log.e(TAG,"setMenuVisibility");
            e.printStackTrace();
        }
    }

    private class MyPeopleAdaptor extends BaseAdapter {
        private final int droidGreen = Color.parseColor("#A4C639");
        private LayoutInflater mInflater;
        private Context mContext;

        public MyPeopleAdaptor(Context context) {
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        public int getCount() {
            return mLstUsers.getCount();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(android.R.layout.simple_list_item_2, null);
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(android.R.id.text1);
                holder.text.setTextColor(Color.BLACK);
                holder.badge = new BadgeView(mContext, holder.text);
                holder.badge.setBadgeBackgroundColor(droidGreen);
                holder.badge.setTextColor(Color.BLACK);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            User user = mLstUsers.getUserByIndex(position);
            holder.text.setText(user.getSender());

            if (user.getCount() != 0) {
                holder.badge.setText(String.valueOf(user.getCount()));
                holder.badge.show();
            } else {
                holder.badge.hide();
            }


            return convertView;
        }

        class ViewHolder {
            TextView text;
            BadgeView badge;
        }
    }

}
