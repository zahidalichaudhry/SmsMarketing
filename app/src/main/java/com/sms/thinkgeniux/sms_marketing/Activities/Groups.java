package com.sms.thinkgeniux.sms_marketing.Activities;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.sms.thinkgeniux.sms_marketing.AdaptersHolderItemToch.GroupBrandsAdapter;
import com.sms.thinkgeniux.sms_marketing.AdaptersHolderItemToch.RecyclerItemTouch;
import com.sms.thinkgeniux.sms_marketing.Constants;
import com.sms.thinkgeniux.sms_marketing.PojoClass.GroupItem;
import com.sms.thinkgeniux.sms_marketing.DataBase.DbHelper;
import com.sms.thinkgeniux.sms_marketing.R;

import java.util.ArrayList;

public class Groups extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerItemTouch.OnItemClickListener  {
    RecyclerView GroupRecyclerView;
    GroupBrandsAdapter BrandsRecylcerAdapter;

    Button addGroup,submit;
    EditText name;
    ArrayList<GroupItem> arrayList=new ArrayList<>();
    DbHelper SQLite = new DbHelper(this);
    String GroupName;
    ConstraintLayout parent;


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
        parent=(ConstraintLayout)findViewById(R.id.parent);
        parent.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });
        initAnimation();
        getWindow().setAllowEnterTransitionOverlap(false);
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.groups, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.home) {


            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Groups.this);
            Intent i = new Intent(Groups.this, MainActivity.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());


//        } else if (id == R.id.sendmessage) {
//            Intent intent=new Intent(MainActivity.this,Sms.class);
//            startActivity(intent);

        } else if (id == R.id.bulk) {

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Groups.this);
            Intent i = new Intent(Groups.this, Bulk_Message.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

        } else if (id == R.id.message) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Groups.this);
            Intent i = new Intent(Groups.this, Sms_Log.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

        } else if (id == R.id.groups) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Groups.this);
            Intent i = new Intent(Groups.this, Groups.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());
            finish();

            //   } else if (id == R.id.nav_send) {

        }
        else if (id == R.id.brands) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Groups.this);
            Intent i = new Intent(Groups.this, Brands.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

            //   } else if (id == R.id.nav_send) {

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

//        GroupRecylcerAdapter =new GroupItemAdaptor(arrayList,Groups.this);
//        GroupRecyclerView.setAdapter(GroupRecylcerAdapter);
//        GroupRecylcerAdapter.notifyDataSetChanged();

        BrandsRecylcerAdapter =new GroupBrandsAdapter(arrayList,this);
        GroupRecyclerView.setAdapter(BrandsRecylcerAdapter);

        BrandsRecylcerAdapter.notifyDataSetChanged();
//        BrandsRecylcerAdapter.setLayoutManager(new GridLayoutManager(this, 2));
//        BrandsRecyclerView.setAdapter(new GroupBrandsAdapter(arrayList, this));

        GroupRecyclerView.addOnItemTouchListener(new RecyclerItemTouch(Groups.this, GroupRecyclerView, this));
    }

    @Override
    public void onItemClick(View view, int position)
    {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Groups.this);
        Intent in = new Intent(Groups.this, Group_Details.class);
        in.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
        in.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
        in.putExtra("id",arrayList.get(position).getGroup_Id());
        in.putExtra("name",arrayList.get(position).getGroup_Name());
        startActivity(in, options.toBundle());

    }

    @Override
    public void onLongItemClick(View view, final int position)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Groups.this);
        alertDialogBuilder.setMessage("Are you Sure You want to Delete");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

//                        arrayList.remove(position);
//                        notifyDataSetChanged();
                        SQLite.deleteGroup(Integer.parseInt(arrayList.get(position).getGroup_Id()));
//                        Intent intent= new Intent(Groups.this, Groups.class);
//                        startActivity(intent);
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Groups.this);
                        Intent in = new Intent(Groups.this, Groups.class);
                        in.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
                        in.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
//                        in.putExtra("id",arrayList.get(position).getGroup_Id());
//                        in.putExtra("name",arrayList.get(position).getGroup_Name());
                        startActivity(in, options.toBundle());

                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    private  void initAnimation()
    {

        Slide enterTransition = new Slide();
        enterTransition.setSlideEdge(Gravity.TOP);
        enterTransition.setDuration(1000);
        enterTransition.setInterpolator(new AnticipateOvershootInterpolator());
        getWindow().setEnterTransition(enterTransition);
    }

}
