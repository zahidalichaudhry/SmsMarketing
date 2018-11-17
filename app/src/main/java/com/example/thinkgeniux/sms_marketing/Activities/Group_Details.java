package com.example.thinkgeniux.sms_marketing.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.example.thinkgeniux.sms_marketing.AdaptersHolderItemToch.ContactAdapter;
import com.example.thinkgeniux.sms_marketing.AdaptersHolderItemToch.GroupBrandsAdapter;
import com.example.thinkgeniux.sms_marketing.AdaptersHolderItemToch.RecyclerItemTouch;
import com.example.thinkgeniux.sms_marketing.Constants;
import com.example.thinkgeniux.sms_marketing.PojoClass.CSVPojo;
import com.example.thinkgeniux.sms_marketing.PojoClass.ContactItem;
import com.example.thinkgeniux.sms_marketing.DataBase.DbHelper;
import com.example.thinkgeniux.sms_marketing.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Group_Details extends AppCompatActivity implements RecyclerItemTouch.OnItemClickListener  {
    String GroupId,GroupName;
    ArrayList<CSVPojo> arrayListCSV=new ArrayList<>();
    ArrayList<CSVPojo> arrayListToAdd=new ArrayList<>();
    ArrayList<ContactItem> arrayListContacts=new ArrayList<>();
    Button getCSV,saveCSV;
    TextView statusCSV;
    DbHelper SQLite = new DbHelper(this);
    boolean exist;
    ConstraintLayout coordinatorLayout;

    RecyclerView  BrandsRecyclerView;
    ContactAdapter BrandsRecylcerAdapter;
    private ProgressDialog loading;

    int GroupIdInt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group__details);
        final Intent intent = getIntent();
        GroupId = intent.getStringExtra("id");
        GroupName = intent.getStringExtra("name");
        getCSV=(Button)findViewById(R.id.getCSV);
        statusCSV=(TextView)findViewById(R.id.statuscsv);
        saveCSV=(Button)findViewById(R.id.saveCSV);
        exist=false;
        coordinatorLayout = (ConstraintLayout) findViewById(R.id
                .coordinatorLayout);
        GroupIdInt = Integer.parseInt(GroupId);
        BrandsRecyclerView =findViewById(R.id.recycler_all_contacts);
        BrandsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getPermissions();
        getAllDataGroupContacts();
        blank();
        getCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCSVFile();
            }
        });
        saveCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveCSVFILETOSQLLITE();
            }
        });
        initAnimation();
        getWindow().setAllowEnterTransitionOverlap(false);

    }

    private void blank()
    {
        saveCSV.setVisibility(View.GONE);
        statusCSV.setVisibility(View.GONE);
    }

    private void SaveCSVFILETOSQLLITE()
    {
        loading = ProgressDialog.show(Group_Details.this,"Saving...","Please wait...",false,false);
        int j=arrayListCSV.size();
        for (int i=0;i<j;i++)
        {
            for (int k=0;k<arrayListContacts.size();k++)
            {
                if (arrayListCSV.get(i).getNumber().equals(arrayListContacts.get(k).getContact_Name()));
                {
                   exist=true;
                }

            }
            if (!exist)
            {
            SQLite.insertContacts(arrayListCSV.get(i).getNumber(),GroupName,GroupIdInt);
            exist=false;
            }
        }
        loading.dismiss();
        getAllDataGroupContacts();
        blank();
    }

    private void getAllDataGroupContacts()
    {
        arrayListContacts = SQLite.getAllDataContacts(GroupIdInt);
//        ContactRecylcerAdapter =new ContactsItemAdaptor(arrayListContacts,Group_Details.this);
//        ContactRecyclerView.setAdapter(ContactRecylcerAdapter);
//        ContactRecylcerAdapter.notifyDataSetChanged();

        BrandsRecylcerAdapter =new ContactAdapter(arrayListContacts,this);
        BrandsRecyclerView.setAdapter(BrandsRecylcerAdapter);

        BrandsRecylcerAdapter.notifyDataSetChanged();
//        BrandsRecylcerAdapter.setLayoutManager(new GridLayoutManager(this, 2));
//        BrandsRecyclerView.setAdapter(new GroupBrandsAdapter(arrayList, this));

        BrandsRecyclerView.addOnItemTouchListener(new RecyclerItemTouch(Group_Details.this, BrandsRecyclerView, this));

    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new
                            String[]{android.Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }
    private void selectCSVFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("*/*");
        Intent i = Intent.createChooser(intent, "File");
//        startActivityForResult(Intent.createChooser(intent, "Open CSV"), 1);
        startActivityForResult(i, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode== Activity.RESULT_OK)
            try {
                final Uri imageUri=data.getData();
                File initialFile = new File(data.getData().getPath());
//                InputStream targetStream = new FileInputStream(initialFile);
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                proImportCSV(inputStream);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
    }
    private void proImportCSV(InputStream from) {
//        try {
//            // Delete everything above here since we're reading from the File we already have
//            ContentValues cv = new ContentValues();
//            // reading CSV and writing table
//            CSVReader dataRead = new CSVReader(new FileReader(from)); // <--- This line is key, and why it was reading the wrong file
//
//            SQLiteDatabase db = mHelper.getWritableDatabase(); // LEt's just put this here since you'll probably be using it a lot more than once
//            String[] vv = null;
//            while((vv = dataRead.readNext())!=null) {
//                cv.clear();
//                SimpleDateFormat currFormater  = new SimpleDateFormat("dd-MM-yyyy");
//                SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd");
//
//                String eDDte;
//                try {
//                    Date nDate = currFormater.parse(vv[0]);
//                    eDDte = postFormater.format(nDate);
//                    cv.put(Table.DATA,eDDte);
//                }
//                catch (Exception e) {
//                }
//                cv.put(Table.C,vv[1]);
//                cv.put(Table.E,vv[2]);
//                cv.put(Table.U,vv[3]);
//                cv.put(Table.C,vv[4]);
//                db.insert(Table.TABLE_NAME,null,cv);
//            } dataRead.close();
//
//        } catch (Exception e) { Log.e("TAG",e.toString());
//
//        }

        // Get input stream and Buffered Reader for our data file.

//        File inputF = new File(inputFilePath);
//        InputStream inputFS = new FileInputStream(from);
        BufferedReader reader = new BufferedReader(new InputStreamReader(from, Charset.forName("UTF-8")));
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputFS));
        String line="";
        try{
            //Read each line
            while ((line = reader.readLine()) != null) {

                //Split to separate the name from the capital
//            String[] RowData = line.split(",");

                //Create a State object for this row's data.
                arrayListCSV.add(new CSVPojo(line));
                statusCSV.setVisibility(View.VISIBLE);
                saveCSV.setVisibility(View.VISIBLE);

//            cur.setCapital(RowData[1]);

                //Add the State object to the ArrayList (in this case we are the ArrayList)
            }
        }catch (IOException e) {
            Log.v("Main Activity", "Error Reading File on Line " + line, e);
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onLongItemClick(View view, final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Group_Details.this);
        alertDialogBuilder.setMessage("Are you Sure You want to Delete");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

//                        SQLite.deleteContact(Integer.parseInt(arrayListContacts.get(position).getContact_Id()));
//                        Intent intent= new Intent(Group_Details.this,Group_Details.class);
//                        intent.putExtra("id",GroupId);
//                        intent.putExtra("name",GroupName);
//                        startActivity(intent);
//                        finish();

                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Group_Details.this);
                        Intent in = new Intent(Group_Details.this, MainActivity.class);
                        in.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
                        in.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
                        in.putExtra("id",GroupId);
                        in.putExtra("name",GroupName);
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
