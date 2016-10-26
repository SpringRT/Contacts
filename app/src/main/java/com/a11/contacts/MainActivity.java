package com.a11.contacts;

import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.a11.contacts.fragments.BaseFragment;
import com.a11.contacts.fragments.FragmentData;
import com.a11.contacts.fragments.FragmentList;
import com.a11.contacts.models.Contact;

public class MainActivity extends AppCompatActivity {

    private boolean isPortrait = true;
    private BaseFragment currentFragment;
    private List<Contact> contactList;
    private Set<String> removed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPortrait = getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        getRemoved();
        getContacts();
        setContentView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && isPortrait) {
            isPortrait = false;
            setContentView();
        }
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && !isPortrait) {
            isPortrait = true;
            setContentView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveRemoved();
    }

    @Override
    public void onBackPressed() {
        if(isPortrait && currentFragment instanceof FragmentList) {
            super.onBackPressed();
        }
        else {
            currentFragment = new FragmentList();
            setFragment();
        }
    }

    private void saveRemoved() {
        SharedPreferences prefs = getSharedPreferences("contacts", MODE_PRIVATE);
        prefs.edit().remove("removed").apply();
        prefs.edit().putStringSet("removed", removed).apply();
    }

    private void getRemoved() {
        SharedPreferences prefs = getSharedPreferences("contacts", MODE_PRIVATE);
        removed = prefs.getStringSet("removed", new HashSet<String>());
    }

    private void getContacts() {
        contactList = new ArrayList<>();

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur != null && cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));

                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(

                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    if(pCur != null) {
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));

                            Contact contact = new Contact(id, name, phoneNo);
                            boolean b = false;
                            if(removed.contains(contact.getId())) {
                                contact.setDeleted(true);
                            }

                            for(Contact c : contactList) {
                                if(c.getId().equals(id)) {
                                    b = true;
                                    break;
                                }
                            }

                            if(!b) {
                                contactList.add(contact);
                            }
                        }

                        pCur.close();
                    }
                }
            }

            cur.close();
        }
    }

    public void setDeleted(int pos, String id) {
        if(contactList.get(pos).getId().equals(id)) {
            removed.add(id);
            for(Contact c : contactList) {
                if(c.getId().equals(id)) {
                    c.setDeleted(true);
                }
            }

            if(isPortrait) {
                currentFragment = new FragmentList();
                setFragment();
            }
            else {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.contacts_frame, new FragmentList()).commit();
                fm.beginTransaction().replace(R.id.data_frame, new FragmentData()).commit();
            }
            Toast.makeText(this, contactList.get(pos).getName()
                    +  " удален!", Toast.LENGTH_LONG).show();
        }
    }

    private void setContentView() {
        setContentView(R.layout.activity_main);

        if(isPortrait) {
            currentFragment = new FragmentList();
            setFragment();
        }
        else {
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.contacts_frame, new FragmentList()).commit();
            fm.beginTransaction().replace(R.id.data_frame, new FragmentData()).commit();
        }
    }

    private void setFragment() {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.main_frame, currentFragment).commit();
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void sendMessage(String number) {
        Intent sms = new Intent();
        sms.setAction(Intent.ACTION_VIEW);
        sms.setData(Uri.fromParts("sms", number, null));

        startActivity(sms);
    }

    public void sendData(Bundle data) {
        int pos = data.getInt("pos", -1);
        String id = data.getString("id", "");

        if(pos != -1 && !id.isEmpty()) {
            Bundle fData = new Bundle();
            fData.putString("id", contactList.get(pos).getId());
            fData.putString("name", contactList.get(pos).getName());
            fData.putString("phone", contactList.get(pos).getPhoneNumber());

            if(isPortrait) {
                currentFragment = new FragmentData();
                currentFragment.setArguments(fData);
                setFragment();
            }
            else {
                FragmentData fragment = new FragmentData();
                fragment.setArguments(fData);
                getFragmentManager().beginTransaction().
                        replace(R.id.data_frame, fragment).commit();
            }
        }
    }

}
