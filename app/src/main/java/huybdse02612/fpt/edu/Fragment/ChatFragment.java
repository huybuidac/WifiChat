package huybdse02612.fpt.edu.Fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import huybdse02612.fpt.edu.Entity.CommandMessage;
import huybdse02612.fpt.edu.Entity.CommandMessageType;
import huybdse02612.fpt.edu.Entity.User;
import huybdse02612.fpt.edu.R;
import huybdse02612.fpt.edu.Service.ProServerService;
import huybdse02612.fpt.edu.Util.ConstantValue;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private final String TAG = this.getClass().getName();
    private ViewGroup mContainer;
    private View mChatFrag;
    private User mCurUser;
    private TextView mTvChatArea;
    private EditText mEdtInput;
    private String mName;
    private Button mBtnSend;
    private ScrollView mScrollView;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Log.d(TAG,"GOTO broadcastReceiver onReceive");
                String myAction = intent.getStringExtra(ConstantValue.ACTION);
                if (myAction.equals(ConstantValue.ACTION_RECEIVE_MESSAGE)) {
                    CommandMessage cmd = (CommandMessage) intent.getSerializableExtra(ConstantValue.EXTRA_CMD);
                    if (mCurUser.getIpAddress().equals(cmd.getSenderAddress())) {
                        mTvChatArea.append(cmd.getmFromUser() + ": " + cmd.getContent() + "\r\n");

                        scrollToLastLine();
                    }
                }
                Log.d(TAG,"OUT broadcastReceiver onReceive");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void scrollToLastLine() {
        mScrollView.post(new Runnable() {
            public void run() {
                mScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"GOTO onCreateView");
        mChatFrag  = inflater.inflate(R.layout.fragment_chat, container, false);

        getViewFromLayout();
        setEvent();

        mContainer=container;
        Log.d(TAG,"OUT onCreateView");
        return mChatFrag;
    }

    private void setEvent() {
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mCurUser==null) return;
                    String mess =  mEdtInput.getText().toString();
                    mCurUser.addMessage("You: " +mess,false);
                    mTvChatArea.append("You: " +mess + "\r\n");
                    mEdtInput.setText("");
                    getActivity().startService(new Intent(getActivity(), ProServerService.class)
                            .setAction(ConstantValue.ACTION_SEND_MESSAGE)
                            .putExtra(ConstantValue.EXTRA_COMMAND_MESSAGE, new CommandMessage(CommandMessageType.MESSAGE, mName, mess, mCurUser.getIpAddress())));
                    scrollToLastLine();
                } catch (Exception e) {
                    Log.e(TAG,"mBtnSend Onclick");
                    e.printStackTrace();
                }
            }
        });
    }

    private void getViewFromLayout() {
        mBtnSend= (Button) mChatFrag.findViewById(R.id.btnSend);
        mTvChatArea= (TextView) mChatFrag.findViewById(R.id.tvChatArea);
        mEdtInput = (EditText) mChatFrag.findViewById(R.id.edtInput);
        mScrollView = (ScrollView) mChatFrag.findViewById(R.id.scrollView1);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"GOTO onDestroy");
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().stopService(new Intent(getActivity().getApplicationContext(), ProServerService.class));
        Log.d(TAG, "OUT onDestroy");
    }

    @Override
    public void onResume() {
        Log.d(TAG,"GOTO onResume");
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(ConstantValue.MY_BROADCAST));
        Log.d(TAG, "OUT onResume");
    }

    @Override
    public void setMenuVisibility(boolean menuVisible){
        Log.d(TAG,"GOTO setMenuVisibility");
        super.setMenuVisibility(menuVisible);
        try {
            if (menuVisible) {
                mCurUser = (User) mContainer.getTag(R.id.TAG_USER_SELECTED);
                if (mCurUser!=null) {
                    mTvChatArea.setText(mCurUser.getAllMessage());
                    scrollToLastLine();
                }
                mName = (String) mContainer.getTag(R.id.TAG_USER_NAME);
                if (mName==null) mName="Unknown";
            } else {
//                mContainer.setTag(R.id.TAG_USER_SELECTED,mCurUser);
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEdtInput.getWindowToken(), 0);
            }
        } catch (Exception e) {
            Log.e(TAG,"setMenuVisibility exception");
            e.printStackTrace();
        }
        Log.d(TAG,"OUT setMenuVisibility");
    }

    @Override
    public void onPause() {
        Log.d(TAG, "GOTO onPause");
        super.onPause();
        try {

        } catch (Exception e) {
            Log.e(TAG,"onPause");
            e.printStackTrace();
        }
        Log.d(TAG, "OUT onPause");
    }
}
