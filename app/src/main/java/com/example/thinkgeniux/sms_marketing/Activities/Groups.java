package com.example.thinkgeniux.sms_marketing.Activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.thinkgeniux.sms_marketing.Adapters.GroupItemAdaptor;
import com.example.thinkgeniux.sms_marketing.PojoClass.GroupItem;
import com.example.thinkgeniux.sms_marketing.DataBase.DbHelper;
import com.example.thinkgeniux.sms_marketing.R;

import java.util.ArrayList;

public class Groups extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView GroupRecyclerView;
    GroupItemAdaptor GroupRecylcerAdapter;

    Button addGroup,submit;
    EditText name;
    ArrayList<GroupItem> arrayList=new ArrayList<>();
    DbHelper SQLite = new DbHelper(this);
    String GroupName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GroupRecyclerView =findViewById(R.id.rec);
        GroupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        name=(EditText)findViewById(R.id.name);
        name.setVisibility(View.GONE);
        addGroup=(Button)findViewById(R.id.addGroup);
        submit=(Button)findViewById(R.id.submit);
        submit.setVisibility(View.GONE);
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                name.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          GroupName=name.getText().toString();
                if (GroupName.length()==0) {
                    name.requestFocus();
                    name.setError(Html.fromHtml("<font color='red'>Please Enter Valid Name</font>"));
                }
                else if (v == submit) {
                    //RegisterOrder();
                    AddGroup();
                }
            }
        });

//        String[]countries=new String[]{"Saudi -Arabia","Pakistan"};

//        GroupRecyclerView.setAdapter(new GroupItemAdaptor(countries));


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getAllDataGroup();
    }

    private void AddGroup()
    {
        SQLite.insertGroup(GroupName.trim());
        blank();

    }

    private void blank()
    {
        submit.setVisibility(View.GONE);
        name.setText(null);
        name.setVisibility(View.GONE);
        getAllDataGroup();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.groups, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void getAllDataGroup() {
       arrayList = SQLite.getAllDataGroup();

//        for (int i = 0; i < row.size(); i++) {
////            String id = row.get(i).get(TAG_ID);
////            String poster = row.get(i).get(TAG_NAME);
////            String title = row.get(i).get(TAG_ADDRESS);
////
////            Data data = new Data();
////
////            data.setId(id);
////            data.setName(poster);
////            data.setAddress(title);
//            arrayList.add(new GroupItem(r))
//
//            itemList.add(data);
//        }

//        adaptor.notifyDataSetChanged();
        GroupRecylcerAdapter =new GroupItemAdaptor(arrayList,Groups.this);
        GroupRecyclerView.setAdapter(GroupRecylcerAdapter);
        GroupRecylcerAdapter.notifyDataSetChanged();
    }
}
