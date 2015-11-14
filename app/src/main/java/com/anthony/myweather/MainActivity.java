package com.anthony.myweather;


import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private static String LOG_TAG = MainActivity.class.getSimpleName();
    private String[] mCityTitles;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavDrawerRecyclerViewAdapter adapter;
    private ArrayList<View> oldView;
    private ArrayList previousPosition;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview);

        previousPosition=new ArrayList();
        oldView=new ArrayList<View>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCityTitles = getResources().getStringArray(R.array.cities_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (RecyclerView) findViewById(R.id.RecyclerView);
        mDrawerList.setHasFixedSize(true);

        mTitle=getSupportActionBar().getTitle();
        adapter = new NavDrawerRecyclerViewAdapter(mCityTitles,getResources());
        adapter.setOnItemClickListener(new NavDrawerRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if((int)previousPosition.get(previousPosition.size()-1)!=position-1) {
                    previousPosition.add(position - 1);
                    selectItem(position - 1);
                    v.setBackgroundColor(getResources().getColor(R.color.drawer_selected));
                    v.setSelected(true);
                    if (oldView.size() > 0) {
                        setToUnSelect(oldView.size() - 1);
                    }
                    if(position-1==0){
                        oldView.add(oldView.get(0));
                        oldView.get(0).setBackgroundColor(getResources().getColor(R.color.drawer_selected));
                    }else {

                        oldView.add(v);
                    }
                }
                //adapter.setPreviousSelected(v);
            }

        });
        // Set the adapter for the list view
        mDrawerList.setAdapter(adapter);
        // Set the list's click listener
        mDrawerList.setLayoutManager(new LinearLayoutManager(this));



// enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP|ActionBar.DISPLAY_SHOW_TITLE);
        //getSupportActionBar().setIcon(R.drawable.ic_drawer);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                if(oldView.size()==0) {
                    oldView.add(adapter.getDefaultSelection());
                    if(oldView.size()==1) {
                        oldView.get(0).setBackgroundColor(getResources().getColor(R.color.drawer_selected));
                        //adapter.getDefaultSelection().setBackgroundColor(getResources().getColor(R.color.drawer_selected));
                    }
                }
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Cities");

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        selectItem(0);
        previousPosition.add(0);

        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);
    }


    private void setToUnSelect(int position){
        if(oldView.size()>0&&oldView.get(position)!=null)
            oldView.get(position).setBackgroundColor(getResources().getColor(R.color.drawer_unselected));
    }

    private void setToSelect(int position){
        if(oldView.size()>0&&oldView.get(position)!=null)
            oldView.get(position).setBackgroundColor(getResources().getColor(R.color.drawer_selected));
    }



    private void selectItem(int position){
        //if(previousPosition.get(previousPosition.size()-1)!=position)

        //setToSelect(position);
        Fragment fragment=WeatherFragment.newInstance(mCityTitles[position],"");
        //Bundle args=new Bundle();
//        switch (position) {
//            case 0:
//                fragment = WeatherFragment.newInstance("Auckland", "");
//                break;
//            case 1:
//                fragment = WeatherFragment.newInstance("Wellington", "");
//                break;
//            case 2:
//                fragment = WeatherFragment.newInstance("Christchurch", "");
//                break;
//            case 3:
//                fragment = WeatherFragment.newInstance("Dunedin", "");
//                break;
//            default:
//                break;
//        }
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            // update selected item and title, then close the drawer
           // mDrawerList.setItemChecked(position, true);
            //mDrawerList.setSelection(position);
            setTitle(mCityTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
                // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }


    }

    @Override
    public void onBackPressed(){
        if(previousPosition!=null&&previousPosition.size()>0){
            previousPosition.remove(previousPosition.size()-1);
        }
        if(oldView!=null&&oldView.size()>0){
            setToUnSelect(oldView.size()-1);
            oldView.remove(oldView.size()-1);
        }
        if(mDrawerLayout.isDrawerOpen(mDrawerList)){ //replace this with actual function which returns if the drawer is open
            mDrawerLayout.closeDrawer(mDrawerList);     // replace this with actual function which closes drawer
        }else if(previousPosition.size()>0){
            selectItem((int) previousPosition.get(previousPosition.size() - 1));
            setToSelect(oldView.size()-1);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

//   @Override
//    protected void onResume() {
//        super.onResume();
//
//        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter.MyClickListener() {
//            @Override
//            public void onItemClick(int position, View v) {
//                Log.i(LOG_TAG, " Clicked on Item " + position);
//                //showDetails(v);
//
//            }
//        });
//    }

    public void showDetails(View v){
       // TransitionManager.beginDelayedTransition(mRootView, new Fade());
        Intent intent = new Intent(this, DetailActivity.class);


        String transitionName = "weatherMain";
        ActivityOptions options =
                ActivityOptions.makeSceneTransitionAnimation(this, v, transitionName);
        startActivity(intent, options.toBundle());
        //ActivityCompat.startActivity(this, intent, options.toBundle());
    }

}
