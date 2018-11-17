package com.example.thinkgeniux.sms_marketing.Activities;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thinkgeniux.sms_marketing.Constants;
import com.example.thinkgeniux.sms_marketing.DataBase.DbHelper;
import com.example.thinkgeniux.sms_marketing.PojoClass.ContactItem;
import com.example.thinkgeniux.sms_marketing.PojoClass.GroupItem;
import com.example.thinkgeniux.sms_marketing.PojoClass.Spinner_Pojo;
import com.example.thinkgeniux.sms_marketing.R;
import com.example.thinkgeniux.sms_marketing.VolleySMS.SMS_Send;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Bulk_Message extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
//    EditText et1,et2,mes;
    EditText message;
    Button sendSms;
    Spinner tospinner,fromspinner;
    String toName="",fromName="";
    boolean thread =true;
    String tId="",fromId="",messagestring,totalnumberString,donnumberString;

    private ProgressDialog loading;
    ConstraintLayout parent;
    int GroupIdInt,totalnumberInteger,donenumberInteger=0;
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
//        spin1=findViewById(R.id.from);

        tospinner = (Spinner)findViewById(R.id.tospinner);
        fromspinner = (Spinner)findViewById(R.id.from);
        // Initializing an ArrayAdapter

        tospinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(position > 0){
                    // Notify the selected item text
                    Spinner_Pojo group = (Spinner_Pojo) parent.getSelectedItem();
                    toName = group.getName().toString();
                    tId = group.getId().toString();
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fromspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        initAnimation();
        getWindow().setAllowEnterTransitionOverlap(false);
    }

    private void getAllDataGroupContactsAndSending()
    {

        GroupIdInt = Integer.parseInt(tId);
        arrayListContacts = SQLite.getAllDataContacts(GroupIdInt);
        messagestring=message.getText().toString().trim();
        ;
        for (int i=0;i<arrayListContacts.size();i++)
        {
            String sendingNumber="0"+arrayListContacts.get(i).getContact_Name();
//            send_SMS.CallAPi(Bulk_Message.this,fromName,sendingNumber,messagestring);
            CallSMSAPi(sendingNumber);
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
            String date = df.format(Calendar.getInstance().getTime());
            SQLite.insertSmsLog(sendingNumber,fromName,messagestring,date);
//            Toast.makeText(Bulk_Message.this,sendingNumber,Toast.LENGTH_LONG).show();
        }
        loading.dismiss();
        TrackingMessages();
    }

    private void CallSMSAPi(final String sendingNumberString )
    {
        StringRequest request = new StringRequest(Request.Method.POST, SMS_Send.BASE_SMS_URl, new com.android.volley.Response.Listener<String>()
        {

            @Override
            public void onResponse(String response) {


                Toast.makeText(Bulk_Message.this, response , Toast.LENGTH_SHORT).show();
            donenumberInteger=donenumberInteger+1;
//
//            {
////                loading.dismiss();
////                JSONObject abc = new JSONObject(response);
////                int j=abc.length();
////                for(int i=j; i>= 1; i--) {
////                    String num = String.valueOf(i);
////                    JSONObject data = abc.getJSONObject(num);
////                    arrayList.add(new Products_pojo(data.getString("product_id"), data.getString("pro_name")
////                            , data.getString("img_url").replace("localhost", Config.ip),data.getString("sku")
////                            ,data.getString("product_quantity"),data.getString("price").replace(".0000","Rs")));
////
////                }
////                adapter=new All_Products_Adapter(arrayList,All_Products.this);
////                recyclerView.setAdapter(adapter);
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
////                Toast.makeText(All_Products.this,"Nothing is Available For Time Being",Toast.LENGTH_LONG).show();
//////                loading.dismiss();
//////                onBackPressed();
//            }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Log.e("Error",error.printStackTrace());
                Toast.makeText(Bulk_Message.this, "Volley Error"+error , Toast.LENGTH_SHORT).show();
//            onBackPressed();

            }
        }

        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Username", SMS_Send.Username);
                params.put("Password", SMS_Send.Password);
                params.put("From", fromName);
                params.put("To", sendingNumberString);
                params.put("Message", messagestring);
                return params;
            }
        };    request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Bulk_Message.this.getApplicationContext());
        requestQueue.add(request);

    }

    private void getGroupsList()
    {
        arrayListGroups = SQLite.getAllDataGroup();
        arrayListTo.add(new Spinner_Pojo("0","ADD PHONE BOOK"));
        for (int i=0;i<arrayListGroups.size();i++)
        {
            arrayListTo.add(new Spinner_Pojo(arrayListGroups.get(i).getGroup_Id(),arrayListGroups.get(i).getGroup_Name()));
        }
//        ArrayAdapter<Spinner_Pojo> adapter = new
//                ArrayAdapter<Spinner_Pojo>(Bulk_Message.this,
//                android.R.layout.simple_spinner_dropdown_item, arrayListTo)
        final ArrayAdapter<Spinner_Pojo> spinnerArrayAdapter = new ArrayAdapter<Spinner_Pojo>(
                this,R.layout.spinner_item,arrayListTo){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        tospinner.setPrompt("Sending Group");
        tospinner.setAdapter(spinnerArrayAdapter);
    }

    private void getSenderList()
    {
        arrayListBrands = SQLite.getAllDataBrands();
        arrayListFrom.add(new Spinner_Pojo("0","BRAND NAME"));
        for (int i=0;i<arrayListBrands.size();i++)
        {
            arrayListFrom.add(new Spinner_Pojo(arrayListBrands.get(i).getGroup_Id(),arrayListBrands.get(i).getGroup_Name()));
        }
//        ArrayAdapter<Spinner_Pojo> adapter = new
//                ArrayAdapter<Spinner_Pojo>(Bulk_Message.this,
//                android.R.layout.simple_spinner_dropdown_item, arrayListFrom);

        final ArrayAdapter<Spinner_Pojo> spinnerArrayAdapter = new ArrayAdapter<Spinner_Pojo>(
                this,R.layout.spinner_item,arrayListFrom){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        fromspinner.setPrompt("Brand From");
        fromspinner.setAdapter(spinnerArrayAdapter);
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
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.bulk__message, menu);
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
//
//            Intent intent=new Intent(Bulk_Message.this,MainActivity.class);
//            startActivity(intent);

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Bulk_Message.this);
            Intent i = new Intent(Bulk_Message.this, MainActivity.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

//        } else if (id == R.id.sendmessage) {
//            Intent intent=new Intent(MainActivity.this,Sms.class);
//            startActivity(intent);

        } else if (id == R.id.bulk) {

//            Intent intent=new Intent(Bulk_Message.this,Bulk_Message.class);
//            startActivity(intent);
//            finish();
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Bulk_Message.this);
            Intent i = new Intent(Bulk_Message.this, Bulk_Message.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

        } else if (id == R.id.message) {
//            Intent intent=new Intent(Bulk_Message.this,Sms_Log.class);
//            startActivity(intent);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Bulk_Message.this);
            Intent i = new Intent(Bulk_Message.this, Sms_Log.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

        } else if (id == R.id.groups) {
//            Intent intent=new Intent(Bulk_Message.this,Groups.class);
//            startActivity(intent);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Bulk_Message.this);
            Intent i = new Intent(Bulk_Message.this, Groups.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

            //   } else if (id == R.id.nav_send) {

        }
        else if (id == R.id.brands) {
//            Intent intent=new Intent(Bulk_Message.this,Brands.class);
//            startActivity(intent);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Bulk_Message.this);
            Intent i = new Intent(Bulk_Message.this, Brands.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

            //   } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void TrackingMessages()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Bulk_Message.this);
        LayoutInflater inflater = Bulk_Message.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.tranking_message_dialog, null);
        builder.setView(dialogView);
////        Button shop,chek;
//        shop=(Button)dialogView.findViewById(R.id.shop);
//        chek=(Button)dialogView.findViewById(R.id.chek);
        final TextView totalnumber,donenumber;
        totalnumber=(TextView)dialogView.findViewById(R.id.total);
        donenumber=(TextView)dialogView.findViewById(R.id.done);
        builder.setTitle("Message Sending");
        totalnumber.setText(String.valueOf(arrayListContacts.size()));

        if (thread){
        final Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                donenumber.setText(String.valueOf(donenumberInteger));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();}
//        shop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent =new Intent(Product_Details.this,Home_Categories.class);
//                startActivity(intent);
//            }
//        });
//        chek.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent =new Intent(Product_Details.this,My_Cart.class);
//                startActivity(intent);
//            }
//        });

        builder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (donenumberInteger>=arrayListContacts.size())
                {
                    dialogInterface.dismiss();
                    thread=false;
//                    Intent intent=new Intent(Bulk_Message.this,MainActivity.class);
//                    startActivity(intent);

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Bulk_Message.this);
                    Intent in = new Intent(Bulk_Message.this, MainActivity.class);
                    in.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
                    in.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
                    startActivity(in, options.toBundle());


            }else
                {
                    Toast.makeText(Bulk_Message.this, "Some Messages are Left" , Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
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
