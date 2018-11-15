package com.example.thinkgeniux.sms_marketing.Activities;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.thinkgeniux.sms_marketing.Adapters.BrandsItemAdaptor;
import com.example.thinkgeniux.sms_marketing.Adapters.GroupItemAdaptor;
import com.example.thinkgeniux.sms_marketing.DataBase.DbHelper;
import com.example.thinkgeniux.sms_marketing.PojoClass.GroupItem;
import com.example.thinkgeniux.sms_marketing.R;

import java.util.ArrayList;

public class Brands extends AppCompatActivity {
    RecyclerView  BrandsRecyclerView;
    BrandsItemAdaptor  BrandsRecylcerAdapter;

    Button addGroup,submit;
    EditText name;
    ArrayList<GroupItem> arrayList=new ArrayList<>();
    DbHelper SQLite = new DbHelper(this);
    String GroupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brands);


        BrandsRecyclerView =findViewById(R.id.rec);
        BrandsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        name=(EditText)findViewById(R.id.name);
        name.setVisibility(View.GONE);
        addGroup=(Button)findViewById(R.id.addBrand);
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



        getAllDataBrands();
    }

    private void getAllDataBrands()
    {
        arrayList = SQLite.getAllDataBrands();

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
        BrandsRecylcerAdapter =new BrandsItemAdaptor(arrayList,Brands.this);
        BrandsRecyclerView.setAdapter(BrandsRecylcerAdapter);
        BrandsRecylcerAdapter.notifyDataSetChanged();

    }

    private void AddGroup()
    {
        SQLite.insertBrands(GroupName.trim());
        blank();

    }

    private void blank()
    {
        submit.setVisibility(View.GONE);
        name.setText(null);
        name.setVisibility(View.GONE);
        getAllDataBrands();
    }

}
