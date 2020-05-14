package org.dailykit.fragment;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.dailykit.activity.ScanActivity;
import org.dailykit.R;

public class ScanFragment extends Fragment {

    Button scanNow;


    public ScanFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        scanNow=view.findViewById(R.id.scan_now);
        scanNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ScanActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
