package com.example.wyb.work1.MainScreen.NewsAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wyb.work1.MainScreen.Home.MyImageView;
import com.example.wyb.work1.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import com.example.wyb.work1.MainScreen.News.NewsWithPhoto;
import com.example.wyb.work1.MainScreen.News.OnMyItemClickers;

/**
 * Created by wyb on 2018/4/17.
 */


//传入值，新闻标题，链接，时间，图片链接
public class WithPhotoApater extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    //******加载新闻所用********
    private ArrayList<NewsWithPhoto> NewsWithPhoto=new ArrayList<>();//用来存储新闻列表的，所有新闻内容
    private Context context;
    private OnMyItemClickers onMyItemClickers;  //点击时间

    //获取图片的线程判断，重写了textview已经弃用
    private Handler handler ;

    //******加载新闻所用********
    private String url;
    //******设置header和footer所用
    public static  final  int TYPE_HEADER=0;//说明带header的
    public static  final  int TYPE_FOOTER=1;//说明带footer的
    public static  final  int TYPE_NORMAL=2;//说明不带header和footer的
    private View mHeaderView;
    private View mFooterView;



    //初始化
    public WithPhotoApater(ArrayList<NewsWithPhoto> NewsWithPhoto)
    {
        this.NewsWithPhoto=NewsWithPhoto;
    }

    //header和footer的getset

    public View getmHeaderView() {
        return mHeaderView;
    }

    public void setmHeaderView(View mHeaderView) {
        this.mHeaderView = mHeaderView;
        notifyItemInserted(0);
    }

    public View getmFooterView() {
        return mFooterView;
    }

    public void setmFooterView(View mFooterView) {
        this.mFooterView = mFooterView;
        notifyItemInserted(getItemCount()-1);
    }


    //******设置header和footer所用




    //ViewPagerHolder的设置

    //********想舍弃哪一个复杂的holder,测试用，测试添加herder和footer
    class  ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MyImageView item_image2;
        TextView item_title2;
        OnMyItemClickers mlistener;

        public ListHolder(View itemView, OnMyItemClickers listener) {
            super(itemView);

            this.mlistener = listener;

            itemView.setOnClickListener(this);
            //如果是headerview或者footerview，直接返回
            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }

            item_image2 = (MyImageView) itemView.findViewById(R.id.news_photo);
            item_title2= (TextView) itemView.findViewById(R.id.news_photo_title);
        }

        @Override
        public void onClick(View v) {
            mlistener.onItemClick(v,getPosition());
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) {
            return new ListHolder(mHeaderView,onMyItemClickers);
        }
        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new ListHolder(mFooterView,onMyItemClickers);
            //返回mFooterView
        }
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_withpooto_news, parent, false);
        return new ListHolder(layout,onMyItemClickers);
    }

    public void setOnMyItemClickers (OnMyItemClickers onMyItemClickers)
    {
        this.onMyItemClickers=onMyItemClickers;
    }
        //返回大小，view中item的个数，个数应该是NewsWithPhotot的大小加上header和footer
    @Override
    public int getItemCount() {
        if(mHeaderView==null&&mFooterView==null)
        {
            return NewsWithPhoto.size();
        }
        else
            if(mHeaderView==null&&mFooterView!=null)
            {
                //情况特殊，因为有图片的这个不打算加header
                return NewsWithPhoto.size()+1;
            }
            else
                if(mHeaderView!=null&&mFooterView==null)
                {
                    return NewsWithPhoto.size()+1;
                }
                else
                {
                    return NewsWithPhoto.size()+2;
                }

       // return NewsWithPhoto.size();
    }
//判断不同的位置，加载不同的item
    @Override
    public int getItemViewType(int position) {
        if(mFooterView==null&&mHeaderView==null)
        {
            return TYPE_NORMAL;
        }

        //在position==0这个位置添加header
       // if(position==0)
        //{
         //   return  TYPE_HEADER;
        //}
        //同理
        if(position==getItemCount()-1)
        {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }


    @Override
    public void onBindViewHolder(/**final*/final RecyclerView.ViewHolder holder, int position) {

        //2018/04/18 修改原因  不打算加入header 所以把这个判断是不是正常数据加载的情况就按照position数目进项
        //加载数据，，原来是position-1,并把旧版判断header的代码注释掉

        if(getItemViewType(position) == TYPE_NORMAL ||getItemViewType(position) == TYPE_HEADER){
            if(holder instanceof ListHolder) {
                //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
                //2018/4/7  中午  先不用imageview加载网络图片，等适配器写好之后，先请求获取的url


                 url=NewsWithPhoto.get(position).getNew_photo_url();

                if(url=="")
                {
                    url="http://img2.chinadaily.com.cn/images/201804/12/5aced144a3105cdce0a26148.jpeg";
                }


                //**********************************************
                //问题：图片位置乱，加载速度慢，差评，想法，把图片缓存下来，存储，命名格式为新闻标题
                //新建线程加载图片信息，发送到消息队列中
               /* new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Bitmap bmp = getHttpBitmap(url);
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = bmp;
                        System.out.println("000");
                        handler.sendMessage(msg);
                    }
                }).start();*/
                /**Handelr里面来处理图片加载
                *
                 */
               /* handler=new Handler()
                {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case 0:
                                System.out.println("111");
                                Bitmap bmp=(Bitmap)msg.obj;
                                ((ListHolder) holder).item_image2.setImageBitmap(bmp);;
                                break;
                        }
                    }
                };*/
               ((ListHolder) holder).item_image2.setImageURL(url);

                //**********************************************
                String test=NewsWithPhoto.get(position).getNew_title();
                ((ListHolder) holder).item_title2.setText(test);

                return;
            }
            return;
        } //else if(getItemViewType(position) == TYPE_HEADER){


            //return;}
        else if(getItemViewType(position)==TYPE_FOOTER){
            return;
        }

    }


}
