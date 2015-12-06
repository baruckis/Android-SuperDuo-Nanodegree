package it.jaschke.alexandria;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import it.jaschke.alexandria.api.Callback;


public class MainActivity extends AppCompatActivity implements Callback {

    private Toolbar mToolbar;
    private CharSequence mTitle;

    private BroadcastReceiver messageReciever;

    public static final String MESSAGE_EVENT = "MESSAGE_EVENT";
    public static final String MESSAGE_KEY = "MESSAGE_EXTRA";


    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;

    private boolean mUserLearnedDrawer;
    private int mCurrentSelectedPosition = R.id.navigate_books;

    private Integer mNavigationViewCheckedItemId;

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private static final String TITLE = "title";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        setToolbar();

        messageReciever = new MessageReciever();
        IntentFilter filter = new IntentFilter(MESSAGE_EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReciever, filter);

        setupNavigationDrawer(savedInstanceState);
        setupBackStackChange();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Select navigation drawer view item at startup, also set title.

        if (getSupportFragmentManager().findFragmentById(R.id.container) == null) {
            mNavigationView.getMenu().performIdentifierAction(mCurrentSelectedPosition, 0);
            mNavigationViewCheckedItemId = mCurrentSelectedPosition;
            mNavigationView.setCheckedItem(mNavigationViewCheckedItemId);
            setTitle(mTitle);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = getString(R.string.app_name) + getString(R.string.separator) + title;
        mToolbar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                if (mDrawerToggle.isDrawerIndicatorEnabled()) {
                    return mDrawerToggle.onOptionsItemSelected(item);
                }
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReciever);
        super.onDestroy();
    }

    @Override
    public void onItemSelected(String ean) {

        Bundle args = new Bundle();
        args.putString(BookDetail.EAN_KEY, ean);

        BookDetail bookDetailFragment = new BookDetail();
        bookDetailFragment.setArguments(args);

        if (findViewById(R.id.right_container) != null) {
            changeFragment(bookDetailFragment, false, R.id.right_container, true);
        } else {
            changeFragment(bookDetailFragment, false, R.id.container, true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
        outState.putString(TITLE, String.valueOf(mTitle));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getCharSequence(TITLE);
        }
    }

    private class MessageReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(MESSAGE_KEY) != null) {
                Toast.makeText(MainActivity.this, intent.getStringExtra(MESSAGE_KEY), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() < 2) {
                finish();
            }
            super.onBackPressed();
        }
    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void setupNavigationDrawer(Bundle savedInstanceState) {

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean fromSavedInstanceState = false;

        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            fromSavedInstanceState = true;
        } else {
            mTitle = getString(R.string.app_name) + getString(R.string.separator) + getString(R.string.books);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        if (mNavigationView != null) {

            mNavigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {

                            if (mNavigationViewCheckedItemId != null && menuItem.getItemId() == mNavigationViewCheckedItemId) return false;

                            mDrawerLayout.closeDrawers();

                            switch (menuItem.getItemId()) {
                                default:
                                case R.id.navigate_books:
                                    changeFragment(new ListOfBooks());
                                    break;
                                case R.id.navigate_scan:
                                    changeFragment(new AddBook());
                                    break;
                                case R.id.navigate_about:
                                    changeFragment(new About());
                                    break;
                            }

                            return true;
                        }
                    });
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(getString(R.string.app_name));
                invalidateOptionsMenu();
                Utils.hideKeyboard(getParent());
                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !fromSavedInstanceState) {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    private void changeFragment(Fragment nextFragment) {
        changeFragment(nextFragment, false, R.id.container, true);
    }

    private void changeFragment(Fragment nextFragment, boolean tryPopBackStack, int containerViewId, boolean addToBackStack) {

        String backStateName = nextFragment.getClass().getName();
        FragmentManager fragmentManager = getSupportFragmentManager();

        boolean isFragmentPopped = false;
        if (tryPopBackStack) {
            isFragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
        }

        if (!isFragmentPopped) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            fragmentTransaction.replace(containerViewId, nextFragment, backStateName);

            if (addToBackStack) {
                fragmentTransaction.addToBackStack(backStateName);
            }

            // Commit the transaction
            fragmentTransaction.commit();

        } else {
            nextFragment = fragmentManager.findFragmentByTag(backStateName);
        }
    }

    private void setupBackStackChange() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
                if (fragment != null && fragment instanceof INavigationFragment) {
                    INavigationFragment navigationFragment = (INavigationFragment) fragment;
                    if (mNavigationView == null) return;
                    // check or uncheck navigation drawer view menu item
                    if (navigationFragment.getNavigationViewItemId() != null) {
                        mNavigationViewCheckedItemId = navigationFragment.getNavigationViewItemId();
                        mNavigationView.getMenu().findItem(mNavigationViewCheckedItemId).setChecked(true);
                    } else if (mNavigationViewCheckedItemId != null) {
                        mNavigationView.getMenu().findItem(mNavigationViewCheckedItemId).setChecked(false);
                        mNavigationViewCheckedItemId = null;
                    }
                }
            }
        });
    }
}