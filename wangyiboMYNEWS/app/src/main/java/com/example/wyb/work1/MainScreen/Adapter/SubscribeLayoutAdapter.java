package com.example.wyb.work1.MainScreen.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wyb.work1.MainScreen.News.OnMyItemClickers;
import com.example.wyb.work1.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by wyb on 2018/4/13.
 */


//适配加载订阅界面下方的订阅内容
public class SubscribeLayoutAdapter extends RecyclerView.Adapter<SubscribeLayoutAdapter.SubscribeHolder> {
    private  List<String> list;
    private  RecyclerView recyclerView;
    private  Context context;
    private  OnMyItemClickers onMyItemClickers;

    //先不写点击事件的构造函数
    public SubscribeLayoutAdapter(List<String> data, Context context)
    {
        this.list=data;
        this.context=context;

    }
    public void setData(List<String> data)
    {
        this.list=data;
    }

    @Override
    public SubscribeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.items_subscribe,parent,false);
        return new SubscribeHolder(view,onMyItemClickers);
    }

    @Override
    public void onBindViewHolder(SubscribeHolder holder, int position) {
        holder.textView.setText(list.get(position));
        switch (holder.textView.getText().toString())
        {
            case "Industries":
                holder.icon.setImageResource(R.mipmap.image_industyr1);
                break;
            case "Companies":
                holder.icon.setImageResource(R.mipmap.image_companies);
                break;
            case "Health":
                holder.icon.setImageResource(R.mipmap.image_health);
                break;
            case "Books":
                holder.icon.setImageResource(R.mipmap.image_book);
                break;
            case "Basketball":
                holder.icon.setImageResource(R.mipmap.image_basketball);
                break;
            case "Art":
                holder.icon.setImageResource(R.mipmap.image_military);
                break;
            case "China.Military":
                holder.icon.setImageResource(R.mipmap.image_art);
                break;
            case "Finance":
                holder.icon.setImageResource(R.mipmap.image_finance);
                break;
            case "Fashion":
                holder.icon.setImageResource(R.mipmap.image_industry);
                break;
            default:

        }

    }

    /*

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context=parent.getContext();
            View view= LayoutInflater.from(context).inflate(R.layout.items_subscribe,parent,false);
            return new SubscribeHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }
    */
    @Override
    public int getItemCount() {
        return list.size();
    }


    //初始化点击事件
    public void setOnMyClickListener( OnMyItemClickers onMyClickListener)
    {
        this.onMyItemClickers=onMyClickListener;
    }

    //先不写点击事件的适配器

    //加一手点击事件
    public class SubscribeHolder extends  RecyclerView.ViewHolder implements View.OnClickListener
    {

        public RelativeLayout relativeLayout;
        public TextView textView;
        public ImageView del,icon;
        public LinearLayout linearLayout;

        OnMyItemClickers onMyItemClickers;

        public SubscribeHolder(View itemview,OnMyItemClickers listener )
        {

            super(itemview);
            this.onMyItemClickers=listener;
            itemview.setOnClickListener(this);

            relativeLayout=(RelativeLayout)itemview.findViewById(R.id.subscribe_item);
            textView=(TextView)itemview.findViewById(R.id.Sub_text);
            del = (ImageView) itemView.findViewById(R.id.del);
            icon=(ImageView)itemView.findViewById(R.id.icon);

        }

        @Override
        public void onClick(View v) {
            onMyItemClickers.onItemClick(v,getPosition());
        }
    }
}
