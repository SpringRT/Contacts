package com.a11.contacts.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.a11.contacts.dialogs.RemoveDialog;
import com.a11.contacts.fragments.BaseFragment;
import com.a11.contacts.models.Contact;
import java.util.Iterator;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ContactHolder>{

    private Context context;
    private List<Contact> contacts;
    private BaseFragment fragment;

    public ListAdapter(BaseFragment fragment) {
        this.fragment = fragment;
        this.context = fragment.getMainActivity();
        contacts = fragment.getContactList();

        Iterator<Contact> iterator = contacts.iterator();
        while (iterator.hasNext()) {
            Contact contact = iterator.next();

            if(contact.isDeleted()) {
                iterator.remove();
            }
        }

    }

    static class ContactHolder extends RecyclerView.ViewHolder {
        private TextView name;

        ContactHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView;
        }

    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).
                inflate(android.R.layout.simple_list_item_1, null);

        return new ContactHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, final int position) {
        holder.name.setText(contacts.get(position).getName());
        holder.name.setTag(contacts.get(position).getId());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", contacts.get(position).getId());
                bundle.putInt("pos", position);
                fragment.sendAction(bundle);
            }
        });
        holder.name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                RemoveDialog dialog = new RemoveDialog();
                Bundle data = new Bundle();
                data.putString("name", contacts.get(position).getName());
                data.putInt("pos", position);
                data.putString("id", contacts.get(position).getId());
                dialog.setArguments(data);
                dialog.setFragment(fragment);
                dialog.setCancelable(true);

                dialog.show(fragment.getFragmentManager(), "");
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

}
