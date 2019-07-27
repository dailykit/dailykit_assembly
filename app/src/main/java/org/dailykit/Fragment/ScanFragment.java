package org.dailykit.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.dailykit.Activity.ScanActivity;
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
