package com.a11.contacts.fragments;


import android.app.Fragment;
import android.os.Bundle;
import com.a11.contacts.MainActivity;
import com.a11.contacts.models.Contact;
import java.util.List;

public class BaseFragment extends Fragment {

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    public void sendAction(Bundle bundle) {
        if(bundle == null || bundle.isEmpty()) {
            return ;
        }

        getMainActivity().sendData(bundle);
    }

    public List<Contact> getContactList() {
        return getMainActivity().getContactList();
    }

    public void setDeleted(int pos, String id) {
        getMainActivity().setDeleted(pos, id);
    }

    public void sendMessage(String number) {
        getMainActivity().sendMessage(number);
    }
}
