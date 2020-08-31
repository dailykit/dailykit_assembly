package org.dailykit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;

import org.dailykit.R;
import org.dailykit.viewmodel.RealmViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RealmActivity extends CustomAppCompatActivity {

    @BindView(R.id.realm_name)
    TextInputEditText realmName;
    @BindView(R.id.client_id)
    TextInputEditText clientId;
    @BindView(R.id.continue_button)
    Button continueButton;
    private RealmViewModel realmViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm);
        ButterKnife.bind(this);
        realmViewModel = ViewModelProviders.of(this).get(RealmViewModel.class);
    }

    @OnClick(R.id.continue_button)
    public void onViewClicked() {
        if(realmName.getText().toString().isEmpty() || clientId.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter Realm Name and Client ID to continue", Toast.LENGTH_SHORT).show();
        }
        else{
            realmViewModel.setRealm(realmName.getText().toString());
            realmViewModel.setClientId(clientId.getText().toString());
            startActivity(new Intent(RealmActivity.this,LoginActivity.class));
            finish();
        }
    }
}
