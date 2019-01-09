package com.test.backgroundsocket.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.test.backgroundsocket.R;
import com.test.backgroundsocket.constants.IntentConstants;
import com.test.backgroundsocket.services.JobSimpleService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonStartClick(View view) {
        Intent intent = new Intent(this, JobSimpleService.class);
        intent.putExtra(IntentConstants.RECEIVER, "Test");
        JobSimpleService.enqueueWork(this, intent);

    }

    public void onButtonStopClick(View view) {
    }
}
