package com.agh.pum.listazakupow;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

public class BaseActivity extends Activity {

    /**
     * @authors: Lukasz Baratowicz, Ireneusz Kolcon, Michal Kryszkiewicz, Przemyslaw Lach
     *
     * This activity vill add Navigation Drawer for our application and all the code related to navigation drawer.
     * We are going to extend all our other activites from this BaseActivity so that every activity will have Navigation Drawer in it.
     * This activity layout contain frame layouts in which we will add our child activity layout.
     */
    protected FrameLayout frameLayout;

    /**
     * ListView to add navigation drawer item in it.
     * We have made it protected to access it in child class. We will just use it in child class to make item selected according to activity opened.
     */

    protected ListView mDrawerList;

    /**
     * List item array for navigation drawer items.
     * */
    protected String[] listArray = new String[5];

    /**
     * Static variable for selected item position. Which can be used in child activity to know which item is selected from the list.
     * */
    protected static int position;

    /**
     *  This flag is used just to check that launcher activity is called first time
     *  so that we can open appropriate Activity on launch and make list item position selected accordingly.
     * */
    private static boolean isLaunch = true;

    /**
     *  Base layout node of this Activity.
     * */
    private DrawerLayout mDrawerLayout;

    /**
     * Drawer listner class for drawer open, close etc.
     */
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_base_layout);

        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        listArray[0] = getString(R.string.lista_zakupow_label);
        listArray[1] = getString(R.string.lista_sklepow_label);
        listArray[2] = getString(R.string.trackuj_label);
        listArray[3] = getString(R.string.schowaj_label);
        listArray[4] = getString(R.string.info_label);

        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item_layout, listArray));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                openActivity(position);
            }
        });

        ActionBar bar = getActionBar();
        if (bar != null) {

            // enable ActionBar app icon to behave as action to toggle nav drawer
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeButtonEnabled(true);

            // ActionBarDrawerToggle ties together the the proper interactions between the sliding drawer and the action bar app icon
            actionBarDrawerToggle = new ActionBarDrawerToggle(
                    this,						/* host Activity */
                    mDrawerLayout, 				/* DrawerLayout object */
                    R.string.open_drawer,       /* "open drawer" description for accessibility */
                    R.string.close_drawer)      /* "close drawer" description for accessibility */
            {
                @Override
                public void onDrawerClosed(View drawerView) {
                    getActionBar().setTitle(listArray[position]);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    getActionBar().setTitle(getString(R.string.app_name));
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    super.onDrawerSlide(drawerView, slideOffset);
                }

                @Override
                public void onDrawerStateChanged(int newState) {
                    super.onDrawerStateChanged(newState);
                }
            };

            mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();

            /**
             * As we are calling BaseActivity from manifest file and this base activity is intended just to add navigation drawer in our app.
             * We have to open some activity with layout on launch. So we are checking if this BaseActivity is called first time then we are opening our first activity.
             * */
            if (isLaunch) {
                /**
                 *Setting this flag false so that next time it will not open our first activity.
                 *We have to use this flag because we are using this BaseActivity as parent activity to our other activity.
                 *In this case this base activity will always be call when any child activity will launch.
                 */
                isLaunch = false;
                openActivity(0);
            }
        }
    }

    /**
     * @param position
     *
     * Launching activity when any list item is clicked.
     */
    protected void openActivity(int position) {

        /**
         * We can set title & itemChecked here but as this BaseActivity is parent for other activity,
         * So whenever any activity is going to launch this BaseActivity is also going to be called and
         * it will reset this value because of initialization in onCreate method.
         * So that we are setting this in child activity.
         */
		mDrawerList.setItemChecked(position, true);
		setTitle(listArray[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        BaseActivity.position = position; //Setting currently selected position in this field so that it will be available in our child activities.

        Intent intent = null;

        switch (position) {
            case 0:
                intent = new Intent(this, ListaZakupow.class);
                break;
            case 1:
                intent = new Intent(this, ListaSklepow.class);
                break;
            case 2:
                intent = new Intent(this, Trackuj.class);
                break;
            case 3:
                intent = new Intent(this, Schowaj.class);
                break;
            case 4:
                intent = new Intent(this, Info.class);
                break;
            default:
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /* We can override onBackPressed method to toggle navigation drawer*/
    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(mDrawerList)){
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            mDrawerLayout.openDrawer(mDrawerList);
        }
    }
}

