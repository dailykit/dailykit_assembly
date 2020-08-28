package org.dailykit.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import org.dailykit.R;
import org.dailykit.util.SoftwareConfig;

import timber.log.Timber;

public class MenuFragment extends Fragment {

    private SoftwareConfig softwareConfig;
    private Switch switchPartialPacking,switchRapidScanning;
    private static final String TAG = "MenuFragment";

    public MenuFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        switchPartialPacking=view.findViewById(R.id.switch_partial_packing);
        switchRapidScanning=view.findViewById(R.id.switch_rapid_scanning);
        softwareConfig = new SoftwareConfig(getActivity());
        switchPartialPacking.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                softwareConfig.enablePartialPacking(true);
                Toast.makeText(getActivity(), "Partial Packing Enabled", Toast.LENGTH_SHORT).show();
            } else {
                softwareConfig.enablePartialPacking(false);
                Toast.makeText(getActivity(), "Partial Packing Disabled", Toast.LENGTH_SHORT).show();
            }
            Timber.e("isPartialPackingEnabled : "+ String.valueOf(softwareConfig.isPartialPackingEnabled()));
        });

        if (softwareConfig.isPartialPackingEnabled()) {
            switchPartialPacking.setChecked(true);
        }



        switchRapidScanning.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                softwareConfig.enableRapidScanning(true);
                Toast.makeText(getActivity(), "Rapid Scanning Enabled", Toast.LENGTH_SHORT).show();
            } else {
                softwareConfig.enableRapidScanning(false);
                Toast.makeText(getActivity(), "Rapid Scanning Disabled", Toast.LENGTH_SHORT).show();
            }
            Timber.e("isRapidScanningEnabled : "+ String.valueOf(softwareConfig.isRapidScanningEnabled()));
        });

        if (softwareConfig.isRapidScanningEnabled()) {
            switchRapidScanning.setChecked(true);
        }

        return view;
    }

}
