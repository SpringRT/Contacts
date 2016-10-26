package com.a11.contacts.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.a11.contacts.adapters.ListAdapter;
import com.a11.contacts.R;

public class FragmentList extends BaseFragment{

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView contacts = (RecyclerView) view.findViewById(R.id.rv_contacts);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        contacts.setLayoutManager(manager);
        ListAdapter adapter = new ListAdapter(this);
        contacts.setAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, null);
    }
}
