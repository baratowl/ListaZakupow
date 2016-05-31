package com.agh.pum.listazakupow;

import android.os.Bundle;

public class Trackuj extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Adding activity_trackuj layout to parent class frame layout.
         */
        getLayoutInflater().inflate(R.layout.activity_trackuj, frameLayout);

        /**
         * Setting title and itemChecked
         */
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);
    }
}