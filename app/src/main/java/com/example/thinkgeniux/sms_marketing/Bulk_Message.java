package com.example.thinkgeniux.sms_marketing;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Bulk_Message extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    EditText et1,et2,mes;
    Button go;
    Spinner spin1;
    String[] names=new String[]{"Home","Demo","List"};
    ArrayAdapter<String> adapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulk__message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        et1=findViewById(R.id.et1);
     //   et2=findViewById(R.id.et2);
        mes=findViewById(R.id.mes);
        go=findViewById(R.id.go);
        spin1=findViewById(R.id.spin1);


        adapter=new ArrayAdapter<String>(this,R.layout.apk,names);
        adapter.setDropDownViewResource(R.layout.apk);
        spin1.setAdapter(adapter);

      go.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            //  String from=et2.getText().toString();
              String message=mes.getText().toString();
          }
      });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.bulk__message, menu);
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

        if (id == R.id.home) {
            Intent intent=new Intent(Bulk_Message.this,MainActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Home ",Toast.LENGTH_SHORT).show();
            // Handle the camera action
        } else if (id == R.id.bulk) {

            Toast.makeText(getApplicationContext(),"You are already on this Activity",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.message) {
            Intent intent=new Intent(Bulk_Message.this,Sms.class);
            startActivity(intent);
            //Toast.makeText(getApplicationContext(),"Message",Toast.LENGTH_SHORT).show();


      //  } else if (id == R.id.nav_manage) {

        //} else if (id == R.id.nav_share) {

        //} else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
