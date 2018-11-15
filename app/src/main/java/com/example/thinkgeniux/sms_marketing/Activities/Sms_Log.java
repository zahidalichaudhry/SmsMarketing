package com.example.thinkgeniux.sms_marketing.Activities;

import android.app.ProgressDialog;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.thinkgeniux.sms_marketing.Adapters.ContactsItemAdaptor;
import com.example.thinkgeniux.sms_marketing.Adapters.SmsLogAdaptor;
import com.example.thinkgeniux.sms_marketing.DataBase.DbHelper;
import com.example.thinkgeniux.sms_marketing.PojoClass.ContactItem;
import com.example.thinkgeniux.sms_marketing.PojoClass.Sms_Log_Pojo;
import com.example.thinkgeniux.sms_marketing.R;

import java.util.ArrayList;

public class Sms_Log extends AppCompatActivity {
    ArrayList<Sms_Log_Pojo> arrayListsmslog=new ArrayList<>();
    RecyclerView logRecyclerView;
    SmsLogAdaptor logRecylcerAdapter;
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
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                To=to.getText().toString().trim();
                From=to.getText().toString().trim();
                Message=to.getText().toString().trim();
                Time=to.getText().toString().trim();
                SQLite.insertSmsLog(To,To,To,To);
                getAllDataLogSms();
            }
        });
        logRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getAllDataLogSms();
    }

    private void getAllDataLogSms()
    {
        arrayListsmslog = SQLite.getAllDataSmsLog();
        logRecylcerAdapter =new SmsLogAdaptor(arrayListsmslog,Sms_Log.this);
        logRecyclerView.setAdapter(logRecylcerAdapter);
        logRecylcerAdapter.notifyDataSetChanged();
    }
}
