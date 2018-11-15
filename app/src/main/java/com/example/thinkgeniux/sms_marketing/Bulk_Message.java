package com.example.thinkgeniux.sms_marketing;

import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.thinkgeniux.sms_marketing.DataBase.DbHelper;
import com.example.thinkgeniux.sms_marketing.PojoClass.ContactItem;
import com.example.thinkgeniux.sms_marketing.PojoClass.GroupItem;
import com.example.thinkgeniux.sms_marketing.PojoClass.Spinner_Pojo;
import com.example.thinkgeniux.sms_marketing.VolleySMS.SMS_Send;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Bulk_Message extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
//    EditText et1,et2,mes;
    EditText message;
    Button sendSms;
    Spinner to,from;
    String toName="",fromName="";
    String tId="",fromId="",messagestring;
    private ProgressDialog loading;
    int GroupIdInt;
//    String color_name="",size_name="";
//    String[] names=new String[]{"Home","Demo","List"};
    ArrayAdapter<String> adapter;
    ArrayList<ContactItem> arrayListContacts=new ArrayList<>();
    ArrayList<Spinner_Pojo> arrayListTo= new ArrayList<>();
    ArrayList<Spinner_Pojo> arrayListFrom= new ArrayList<>();
    DbHelper SQLite = new DbHelper(this);
    ArrayList<GroupItem> arrayListGroups=new ArrayList<>();
    ArrayList<GroupItem> arrayListBrands=new ArrayList<>();

    SMS_Send send_SMS;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulk__message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



//
//        et1=findViewById(R.id.et1);
//     //   et2=findViewById(R.id.et2);
//        mes=findViewById(R.id.mes);
        sendSms=findViewById(R.id.go);
        message=(EditText)findViewById(R.id.message) ;
        send_SMS=new SMS_Send();
        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loading = ProgressDialog.show(Bulk_Message.this,"Sending...","Please wait...",false,false);
                getAllDataGroupContactsAndSending();


            }
        });
//        spin1=findViewById(R.id.from);

        to = (Spinner) findViewById(R.id.to);
        from = (Spinner) findViewById(R.id.from);
        to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                Spinner_Pojo group = (Spinner_Pojo) parent.getSelectedItem();
                toName = group.getName().toString();
                tId = group.getId().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner_Pojo brand = (Spinner_Pojo) parent.getSelectedItem();
                fromName = brand.getName().toString();
                fromId = brand.getId().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        adapter=new ArrayAdapter<String>(this,R.layout.apk,names);
//        adapter.setDropDownViewResource(R.layout.apk);
////        spin1.setAdapter(adapter);

//      go.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View view) {
//            //  String from=et2.getText().toString();
//              String message=mes.getText().toString();
//          }
//      });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSenderList();
        getGroupsList();
    }

    private void getAllDataGroupContactsAndSending()
    {

        GroupIdInt = Integer.parseInt(tId);
        arrayListContacts = SQLite.getAllDataContacts(GroupIdInt);
        messagestring=message.getText().toString().trim();
        for (int i=0;i<arrayListContacts.size();i++)
        {
            String sendingNumber="0"+arrayListContacts.get(i).getContact_Name();
            send_SMS.CallAPi(Bulk_Message.this,fromName,sendingNumber,messagestring);
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
            String date = df.format(Calendar.getInstance().getTime());
            SQLite.insertSmsLog(sendingNumber,fromName,messagestring,date);
//            Toast.makeText(Bulk_Message.this,sendingNumber,Toast.LENGTH_LONG).show();
        }

    }

    private void getGroupsList()
    {
        arrayListGroups = SQLite.getAllDataGroup();
        for (int i=0;i<arrayListGroups.size();i++)
        {
            arrayListTo.add(new Spinner_Pojo(arrayListGroups.get(i).getGroup_Id(),arrayListGroups.get(i).getGroup_Name()));
        }
        ArrayAdapter<Spinner_Pojo> adapter = new
                ArrayAdapter<Spinner_Pojo>(Bulk_Message.this,
                android.R.layout.simple_spinner_dropdown_item, arrayListTo);
        to.setPrompt("Sending Group");
        to.setAdapter(adapter);
    }

    private void getSenderList()
    {
        arrayListBrands = SQLite.getAllDataBrands();
        for (int i=0;i<arrayListBrands.size();i++)
        {
            arrayListFrom.add(new Spinner_Pojo(arrayListBrands.get(i).getGroup_Id(),arrayListBrands.get(i).getGroup_Name()));
        }
        ArrayAdapter<Spinner_Pojo> adapter = new
                ArrayAdapter<Spinner_Pojo>(Bulk_Message.this,
                android.R.layout.simple_spinner_dropdown_item, arrayListFrom);
        from.setPrompt("Brand From");
        from.setAdapter(adapter);
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
