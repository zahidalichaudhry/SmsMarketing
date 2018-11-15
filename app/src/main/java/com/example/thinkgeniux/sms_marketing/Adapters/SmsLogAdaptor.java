package com.example.thinkgeniux.sms_marketing.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thinkgeniux.sms_marketing.PojoClass.Sms_Log_Pojo;
import com.example.thinkgeniux.sms_marketing.R;

import java.util.ArrayList;


/**
 * Created by ThinkGeniux on 13/11/2018.
 */

public class SmsLogAdaptor extends RecyclerView.Adapter<SmsLogAdaptor.ProgrammingViewHolder> {
    ArrayList<Sms_Log_Pojo> arrayList=new ArrayList<>();
    Activity activity;
    public SmsLogAdaptor(ArrayList<Sms_Log_Pojo> arrayList, Context context){

    this.arrayList=arrayList;
        activity = (Activity) context;
    }



    @Override
    public ProgrammingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_log_item,parent,false);
        return new ProgrammingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProgrammingViewHolder holder, final int position)
    {
        holder.to.setText(arrayList.get(position).getTo());
        holder.from.setText(arrayList.get(position).getFrom());
        holder.message.setText(arrayList.get(position).getMessage());
        holder.time.setText(arrayList.get(position).getTime());
        holder.number.setText(arrayList.get(position).getId());

    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ProgrammingViewHolder extends RecyclerView.ViewHolder{
        TextView to,from,message,time,number;
        public ProgrammingViewHolder(View itemView) {

            super(itemView);

            to =(TextView) itemView.findViewById(R.id.to);
            from =(TextView) itemView.findViewById(R.id.from);
            message =(TextView) itemView.findViewById(R.id.message);
            time =(TextView) itemView.findViewById(R.id.time);
            number =(TextView) itemView.findViewById(R.id.number);

        }
    }
}
