package com.desktop.telephone.telephonedesktop.desktop.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desktop.telephone.telephonedesktop.R;

public class ContactsListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_call_records, null);
        return view;
    }

    public static ContactsListFragment newInstance() {
//        Bundle argz = new Bundle();
        ContactsListFragment fragment = new ContactsListFragment();
//        fragment.setArguments(argz);
        return fragment;
    }
}
