package huybdse02612.fpt.edu.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import huybdse02612.fpt.edu.Entity.CommandMessage;
import huybdse02612.fpt.edu.Entity.CommandMessageType;
import huybdse02612.fpt.edu.Entity.ProClient;
import huybdse02612.fpt.edu.R;
import huybdse02612.fpt.edu.Service.ProServerService;
import huybdse02612.fpt.edu.Util.ConstantVariance;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    private Switch swtEnableLanChat;
    private Button btnGotoChat;
    private View mSettingView;

    public SettingFragment() {
        // Required empty public constructor
    }

    private void setEventForView() {
        btnGotoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        swtEnableLanChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnGotoChat.setVisibility(isChecked==true?View.VISIBLE:View.INVISIBLE);
            }
        });
    }

    private void getViewFromLayout() {
        swtEnableLanChat = (Switch) mSettingView.findViewById(R.id.swtEnableLanChat);
        btnGotoChat= (Button) mSettingView.findViewById(R.id.btnGotoChat);
        ((Button)mSettingView.findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startService(new Intent(getActivity(), ProServerService.class)
                        .setAction(ConstantVariance.ACTION_SEND)
                        .putExtra(ConstantVariance.EXTRA_COMMAND_MESSAGE,new CommandMessage(CommandMessageType.CONNECT,"DaicaHuy","HEHEHE","255.255.255.255")));
//                new ProClient("10.20.6.253").Start();
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mSettingView = inflater.inflate(R.layout.fragment_setting, container, false);
        getViewFromLayout();
        setEventForView();
        return mSettingView;
    }

}
