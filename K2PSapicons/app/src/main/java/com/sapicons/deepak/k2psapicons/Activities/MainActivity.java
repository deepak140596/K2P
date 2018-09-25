package com.sapicons.deepak.k2psapicons.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sapicons.deepak.k2psapicons.Fragments.FavoFragment;
import com.sapicons.deepak.k2psapicons.Fragments.HomeFragment;
import com.sapicons.deepak.k2psapicons.Fragments.MessageFragment;
import com.sapicons.deepak.k2psapicons.Fragments.PostFragment;
import com.sapicons.deepak.k2psapicons.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private CheckBox check1;
    private BottomNavigationView mMainNav;
    private HomeFragment mhomeFragment;
    private PostFragment mpostFragment;
    private FavoFragment mfavoFragment;
    private MessageFragment msgFragment;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    CircleImageView m;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        mAuth = FirebaseAuth.getInstance();


        Toolbar mToolbar = findViewById(R.id.main_app_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("K2P");

        check1 = findViewById(R.id.remember_me);
        FrameLayout mFrame = findViewById(R.id.main_frame);
        mMainNav = findViewById(R.id.main_nav);

        mDrawer = findViewById(R.id.drawer_main);
        mToggle = new ActionBarDrawerToggle(this,mDrawer,R.string.open,R.string.close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.drawer_context_navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.con_setting_user:
                        startActivity(new Intent(MainActivity.this,SettingActivity.class));
                        mDrawer.closeDrawers();
                        break;
                    case R.id.con_specific:
                        startActivity(new Intent(MainActivity.this,SpecificActivity.class));
                        mDrawer.closeDrawers();
                        break;
                    case R.id.con_log_out:
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Log Out!!")
                                .setMessage("Are you sure you want to Log-out?")
                                .setNegativeButton(android.R.string.no, null)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0, int arg1) {
                                        FirebaseAuth.getInstance().signOut();
                                        sendToStart();
                                    }
                                }).create().show();
                        mDrawer.closeDrawers();
                        break;




                }


                return false;
            }
        });

        mhomeFragment = new HomeFragment();
        mpostFragment = new PostFragment();
        mfavoFragment = new FavoFragment();
        msgFragment = new MessageFragment();

        setFragment(mhomeFragment);

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(mhomeFragment);
                        return true;

                    case R.id.nav_post:
                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(mpostFragment);
                        return true;

                    case R.id.nav_favourites:
                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(mfavoFragment);
                        return true;
                    case R.id.nav_messages:
                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(msgFragment);
                        return true;



                        default:
                            return false;

                }
            }

            private void setFragment(Fragment fragment) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                        .beginTransaction();
                fragmentTransaction.replace(R.id.main_frame, fragment);
                fragmentTransaction.commit();


            }
        });
        }

    private void setFragment(HomeFragment mhomeFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, mhomeFragment);
        fragmentTransaction.commit();


    }


    @Override
    public void onStart() {
        try {
            super.onStart();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){

            sendToStart();

        }
        }

    private void sendToStart() {

        Intent startActivity = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(startActivity);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }


        if (item.getItemId()==R.id.all_user_single_post){
             startActivity(new Intent(MainActivity.this,SpecificActivity.class));
        }


        return true;
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                    }
                }).create().show();


    }
}
