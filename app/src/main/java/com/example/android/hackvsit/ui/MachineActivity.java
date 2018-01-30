package com.example.android.hackvsit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.hackvsit.R;
import com.example.android.hackvsit.model.Machine;

import timber.log.Timber;

public class MachineActivity extends AppCompatActivity {

    private Machine mMachine;
    private String MACHINE = "machine";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);
        Intent startingIntent = getIntent();
        mMachine = startingIntent.getParcelableExtra(MACHINE);
        Timber.d(mMachine.getmVendorId());

    }


}
