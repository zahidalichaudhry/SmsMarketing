package com.example.thinkgeniux.sms_marketing.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thinkgeniux.sms_marketing.Activities.Group_Details;
import com.example.thinkgeniux.sms_marketing.PojoClass.GroupItem;
import com.example.thinkgeniux.sms_marketing.R;

import java.util.ArrayList;


/**
 * Created by ThinkGeniux on 13/11/2018.
 */

public class GroupItemAdaptor extends RecyclerView.Adapter<GroupItemAdaptor.ProgrammingViewHolder> {
    ArrayList<GroupItem> arrayList=new ArrayList<>();
    Activity activity;
    public GroupItemAdaptor(ArrayList<GroupItem> arrayList, Context context){

    this.arrayList=arrayList;
        activity = (Activity) context;
    }



    @Override
    public ProgrammingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.group_names_item,parent,false);
        return new ProgrammingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProgrammingViewHolder holder, final int position) {
        holder.gtv.setText(arrayList.get(position).getGroup_Name());
        holder.gtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Group_Details.class);
                intent.putExtra("id",arrayList.get(position).getGroup_Id());
                intent.putExtra("name",arrayList.get(position).getGroup_Name());
                activity.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ProgrammingViewHolder extends RecyclerView.ViewHolder{
        TextView gtv;
        public ProgrammingViewHolder(View itemView) {

            super(itemView);

           gtv =(TextView) itemView.findViewById(R.id.gtv);


        }
    }
}
