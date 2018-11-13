package com.example.thinkgeniux.sms_marketing;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by ThinkGeniux on 13/11/2018.
 */

public class recAdaptor extends RecyclerView.Adapter<recAdaptor.ProgrammingViewHolder> {
    private String[] data;
    public recAdaptor(String[] data){

    this.data=data;
    }



    @Override
    public ProgrammingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.group_names,parent,false);
        return new ProgrammingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProgrammingViewHolder holder, int position) {
        holder.gtv.setText(data[position]);

    }



    @Override
    public int getItemCount() {
        return data.length;
    }

    public class ProgrammingViewHolder extends RecyclerView.ViewHolder{
        TextView gtv;
        public ProgrammingViewHolder(View itemView) {

            super(itemView);

           gtv =(TextView) itemView.findViewById(R.id.gtv);


        }
    }
}
