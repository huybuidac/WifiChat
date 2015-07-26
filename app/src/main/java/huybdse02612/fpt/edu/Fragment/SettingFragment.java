package huybdse02612.fpt.edu.Fragment;


import android.content.Intent;
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

import huybdse02612.fpt.edu.Entity.CommandMessage;
import huybdse02612.fpt.edu.Entity.CommandMessageType;
import huybdse02612.fpt.edu.Entity.User;
import huybdse02612.fpt.edu.R;
import huybdse02612.fpt.edu.Service.ProServerService;
import huybdse02612.fpt.edu.Util.ConstantValue;

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


    private void setEventForView() {
        Log.d(TAG, "GOTO setEventForView");
        mBtnSetname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContainer.setTag(R.id.TAG_USER_NAME, mEdtName.getText().toString());
            }
        });
        swtEnableLanChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked) {
                        mContainer.setTag(R.id.TAG_USER_NAME, mEdtName.getText().toString());
                        getActivity().startService(new Intent(getActivity(), ProServerService.class)
                                .setAction(ConstantValue.ACTION_CONNECT)
                                .putExtra(ConstantValue.EXTRA_COMMAND_MESSAGE,
                                        new CommandMessage(CommandMessageType.CONNECT,
                                                ((EditText) getActivity().findViewById(R.id.edtName)).getText().toString(),
                                                "", "255.255.255.255")));
                    } else {
                        getActivity().stopService(new Intent(getActivity(), ProServerService.class));
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
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
        Log.d(TAG, "OUT onCreateView");
        return mSettingView;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "GOTO onDestroy");
        super.onDestroy();
        getActivity().stopService(new Intent(getActivity(), ProServerService.class));
        Log.d(TAG, "OUT onDestroy");
    }

    @Override
    public void onPause() {
        Log.d(TAG, "GOTO onPause");
        super.onPause();
        try {

        } catch (Exception e) {
            Log.e(TAG, "onPause");
            e.printStackTrace();
        }
        Log.d(TAG, "OUT onPause");
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        Log.d(TAG,"GOTO setMenuVisibility");
        super.setMenuVisibility(menuVisible);
        try {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEdtName.getWindowToken(), 0);
        } catch (Exception e) {
            Log.e(TAG,"setMenuVisibility exception");
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
}
