package com.sms.thinkgeniux.sms_marketing.Activities;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;
import com.sms.thinkgeniux.sms_marketing.AdaptersHolderItemToch.RecyclerItemTouch;
import com.sms.thinkgeniux.sms_marketing.AdaptersHolderItemToch.Sms_LogAdapter;
import com.sms.thinkgeniux.sms_marketing.Constants;
import com.sms.thinkgeniux.sms_marketing.DataBase.DbHelper;
import com.sms.thinkgeniux.sms_marketing.PojoClass.ContactItem;
import com.sms.thinkgeniux.sms_marketing.PojoClass.Sms_Log_Pojo;
import com.sms.thinkgeniux.sms_marketing.R;

import java.util.ArrayList;

public class Sms_Log extends AppCompatActivity implements RecyclerItemTouch.OnItemClickListener {
    ArrayList<Sms_Log_Pojo> arrayListsmslog=new ArrayList<>();
    RecyclerView logRecyclerView;
    Sms_LogAdapter logRecylcerAdapter;
    TextView to,form,message,time;
    Button add;
    String To,From,Message,Time;
    DbHelper SQLite = new DbHelper(this);
    private ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms__log);
        logRecyclerView =findViewById(R.id.logsms);
        to=(TextView)findViewById(R.id.to);
        form=(TextView)findViewById(R.id.from);
        message=(TextView)findViewById(R.id.messag);
        time=(TextView)findViewById(R.id.time);
        add=(Button)findViewById(R.id.submit);
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                To=to.getText().toString().trim();
//                From=to.getText().toString().trim();
//                Message=to.getText().toString().trim();
//                Time=to.getText().toString().trim();
//                SQLite.insertSmsLog(To,To,To,To);
//                getAllDataLogSms();
//            }
//        });
        logRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getAllDataLogSms();
        initAnimation();
    }

    private void getAllDataLogSms()
    {
        arrayListsmslog = SQLite.getAllDataSmsLog();
        logRecylcerAdapter =new Sms_LogAdapter(arrayListsmslog,this);
        logRecyclerView.setAdapter(logRecylcerAdapter);

        logRecylcerAdapter.notifyDataSetChanged();
//        BrandsRecylcerAdapter.setLayoutManager(new GridLayoutManager(this, 2));
//        BrandsRecyclerView.setAdapter(new GroupBrandsAdapter(arrayList, this));

        logRecyclerView.addOnItemTouchListener(new RecyclerItemTouch(Sms_Log.this, logRecyclerView, this));

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onLongItemClick(View view, final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Sms_Log.this);
        alertDialogBuilder.setMessage("Are you Sure You want to Delete");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        SQLite.deleteLog(Integer.parseInt(arrayListsmslog.get(position).getId()));
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Sms_Log.this);
                        Intent i = new Intent(Sms_Log.this, Sms_Log.class);
                        i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
                        i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
                        startActivity(i, options.toBundle());
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
