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

import java.util.concurrent.ExecutionException;

public class Sms extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    EditText et1,et2,mes;
    Button go;
    Spinner spin;
    String[] name=new String[]{"Home","Demo","About"};
    ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        et1=findViewById(R.id.et1);
        et2=findViewById(R.id.et2);
        mes=findViewById(R.id.mes);
        go=findViewById(R.id.go);
        spin=findViewById(R.id.spin);
        adapter=new ArrayAdapter(getApplicationContext(),R.layout.apk,name);
        adapter.setDropDownViewResource(R.layout.apk);
        spin.setAdapter(adapter);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String to=et1.getText().toString();
                String from=et2.getText().toString();
                String message=mes.getText().toString();

                String url = "https://portal.smsbundles.com/sendsms_url.html?Username=03454014792&Password=bramerz792&From=" + from + "&To=" + to + "&Message=" + message;
                String response = null;

                try {
                    response = new sendMessage().execute(url).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (response.contains("Success"))
                {
                    et1.setText("");
                    et2.setText("");
                    mes.setText("");
                }
                //String res=sms.toString();
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                //readJSONFeed(url);





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
        getMenuInflater().inflate(R.menu.sms, menu);
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

        if (id == R.id.bulk) {
                            Intent intent=new Intent(Sms.this,Bulk_Message.class);
                            startActivity(intent);

        } else if (id == R.id.home) {
            Intent intent=new Intent(Sms.this,MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.sendmessage) {
            Toast.makeText(getApplicationContext(),"You are already on this Activity",Toast.LENGTH_SHORT).show();

        //} else if (id == R.id.nav_manage) {

        //} else if (id == R.id.nav_share) {

   //     } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
