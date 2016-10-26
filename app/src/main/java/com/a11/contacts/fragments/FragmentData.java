package com.a11.contacts.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.a11.contacts.R;

public class FragmentData extends BaseFragment {

    private String id;
    private String name;
    private String phone;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if(view != null) {
            ((TextView) view.findViewById(R.id.tv_contact_name)).setText(id);
            ((EditText) view.findViewById(R.id.et_name)).setText(name);
            ((EditText) view.findViewById(R.id.et_phone)).setText(phone);
            if(phone.isEmpty()) {
                view.findViewById(R.id.btn_sms).setEnabled(false);
                view.findViewById(R.id.btn_sms).setVisibility(View.INVISIBLE);
            }
            else {
                view.findViewById(R.id.btn_sms).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendMessage(phone);
                    }
                });
            }
        }
    }

    private boolean readData() {
        Bundle data = getArguments();

        if(data != null) {
            id = data.getString("id", "");
            name = data.getString("name", "");
            phone = data.getString("phone", "");

            return true;
        }

        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(readData()) {
            return inflater.inflate(R.layout.fragment_contact_data, null);
        }
        else {
            return null;
        }
    }
}
