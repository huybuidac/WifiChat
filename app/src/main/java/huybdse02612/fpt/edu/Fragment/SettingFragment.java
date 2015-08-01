package huybdse02612.fpt.edu.Fragment;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import huybdse02612.fpt.edu.Entity.CommandMessage;
import huybdse02612.fpt.edu.Entity.CommandMessageType;
import huybdse02612.fpt.edu.R;
import huybdse02612.fpt.edu.Service.ServerService;
import huybdse02612.fpt.edu.Util.ConstantValue;
import huybdse02612.fpt.edu.Util.PreferencesLib;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    private final String TAG = this.getClass().getName();
    private Switch swtEnableLanChat;
    private EditText mEdtName;
    private View mSettingView;
    private ViewGroup mContainer;
    private Button mBtnSetname;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "GOTO broadcastReceiver onReceive");
            try {
                if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    Log.d("NetworkCheckReceiver", "NetworkCheckReceiver invoked...");

                    boolean noConnectivity = intent.getBooleanExtra(
                            ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

                    if (noConnectivity) {
                        if (swtEnableLanChat!=null && swtEnableLanChat.isChecked()) {
                            context.stopService(new Intent(context, ServerService.class));
                            mContainer.setTag(R.id.TAG_SERVICE_IS_START, false);
                            swtEnableLanChat.setChecked(false);
                            Toast.makeText(context,"Lost wifi connection!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            } catch (Exception e) {
                Log.e(TAG, "broadcastReceiver onReceive exception");
                e.printStackTrace();
            }
            Log.d(TAG, "OUT broadcastReceiver onReceive");
        }
    };

    private void setEventForView() {
        Log.d(TAG, "GOTO setEventForView");
        mBtnSetname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContainer.setTag(R.id.TAG_USER_NAME, mEdtName.getText().toString());
                PreferencesLib.writeString(getActivity(),PreferencesLib.NAME,mEdtName.getText().toString());
            }
        });
        swtEnableLanChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked) {
                        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        if (!mWifi.isConnected()) {
                            swtEnableLanChat.setChecked(false);
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Info")
                                    .setMessage("Wifi is not available!")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else {
                            mContainer.setTag(R.id.TAG_USER_NAME, mEdtName.getText().toString());
                            PreferencesLib.writeString(getActivity(), PreferencesLib.NAME, mEdtName.getText().toString());
                            getActivity().startService(new Intent(getActivity(), ServerService.class)
                                    .setAction(ConstantValue.ACTION_CONNECT)
                                    .putExtra(ConstantValue.EXTRA_COMMAND_MESSAGE,
                                            new CommandMessage(CommandMessageType.CONNECT,
                                                    ((EditText) getActivity().findViewById(R.id.edtName)).getText().toString(),
                                                    "", "255.255.255.255")));
                            mContainer.setTag(R.id.TAG_SERVICE_IS_START, true);
                        }
                    } else {
                        getActivity().stopService(new Intent(getActivity(), ServerService.class));
                        mContainer.setTag(R.id.TAG_SERVICE_IS_START,false);
                    }
                } catch (Exception e) {
                    Log.e(TAG,"swtEnableLanChat.setOnCheckedChangeListener");
                    e.getStackTrace();
                }
            }
        });
        Log.d(TAG, "OUT setEventForView");
    }

    private void getViewFromLayout() {
        Log.d(TAG, "GOTO getViewFromLayout");
        swtEnableLanChat = (Switch) mSettingView.findViewById(R.id.swtEnableLanChat);
        mEdtName = (EditText) mSettingView.findViewById(R.id.edtName);
        mBtnSetname = (Button) mSettingView.findViewById(R.id.btnSetname);
        Log.d(TAG, "OUT getViewFromLayout");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "GOTO onCreateView");
        mContainer = container;
        mSettingView = inflater.inflate(R.layout.fragment_setting, container, false);
        getViewFromLayout();
        setEventForView();
        IntentFilter filterWifiState = new IntentFilter();
        filterWifiState.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(broadcastReceiver, filterWifiState);
        String name = PreferencesLib.readString(getActivity(),PreferencesLib.NAME,"name");
        mEdtName.setText(name);
        Log.d(TAG, "OUT onCreateView");
        return mSettingView;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        Log.d(TAG, "GOTO setMenuVisibility");
        super.setMenuVisibility(menuVisible);
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEdtName.getWindowToken(), 0);
        } catch (Exception e) {
            Log.e(TAG, "setMenuVisibility exception");
            e.printStackTrace();
        }
        Log.d(TAG, "OUT setMenuVisibility");

    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "GOTO onDestroyView");
        super.onDestroyView();
        Log.d(TAG, "OUT onDestroyView");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "GOTO onDestroy");
        try {
            super.onDestroy();
            getActivity().stopService(new Intent(getActivity(), ServerService.class));
            mContainer.setTag(R.id.TAG_SERVICE_IS_START,false);
        } catch (Exception e) {
            Log.e(TAG,"onDestroy");
            e.printStackTrace();
        }
        Log.d(TAG, "OUT onDestroy");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "GOTO onResume");
        super.onResume();

        Log.d(TAG, "OUT onResume");
    }

    //    @Override
//    public void onResume() {
//        Log.d(TAG, "GOTO onResume");
//
//        Log.d(TAG, "OUT onResume");
//    }
}
