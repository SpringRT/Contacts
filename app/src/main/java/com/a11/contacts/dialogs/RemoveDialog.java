package com.a11.contacts.dialogs;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.a11.contacts.R;
import com.a11.contacts.fragments.BaseFragment;

public class RemoveDialog extends DialogFragment {

    private BaseFragment fragment;
    public void setFragment(BaseFragment fragment){
        this.fragment = fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_remove, container, false);
        TextView tv = (TextView) view.findViewById(R.id.tv_remove);

        final String name = getArguments().getString("name", "");
        final int pos = getArguments().getInt("pos", -1);
        final String id = getArguments().getString("id", "");
        String text = tv.getText().toString();
        tv.setText(text.replace("%s", name));

        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveDialog.this.dismiss();
            }
        });
        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.setDeleted(pos, id);
                RemoveDialog.this.dismiss();
            }
        });

        return view;
    }
}
