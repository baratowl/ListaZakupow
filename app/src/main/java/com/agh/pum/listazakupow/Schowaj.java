package com.agh.pum.listazakupow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Schowaj extends BaseActivity {

    protected TextView tVstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Adding activity_schowaj layout to parent class frame layout.
         */
        getLayoutInflater().inflate(R.layout.activity_schowaj, frameLayout);

        /**
         * Setting title and itemChecked
         */
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);
        TrackService trackService = new TrackService();
        tVstatus = (TextView) findViewById(R.id.tvStatus);

        final Intent theIntent = new Intent(this, trackService.getClass());
        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(theIntent);
                tVstatus.setText(getString(R.string.action_start));
                //Toast.makeText(Schowaj.this, getString(R.string.action_start), Toast.LENGTH_LONG).show();
            }
        });
        Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(theIntent);
                tVstatus.setText(getString(R.string.action_stop));
                //Toast.makeText(Schowaj.this, getString(R.string.action_stop), Toast.LENGTH_LONG).show();
            }
        });
    }
}
