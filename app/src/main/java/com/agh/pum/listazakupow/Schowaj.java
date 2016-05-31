package com.agh.pum.listazakupow;

import android.os.Bundle;

public class Schowaj extends BaseActivity {

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
    }
}
