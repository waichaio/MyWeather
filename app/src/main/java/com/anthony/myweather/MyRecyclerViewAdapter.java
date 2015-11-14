package com.anthony.myweather;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by anthonyo on 22/5/2015.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.viewHolder>  {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<Weather> mDataset;
    private Context mContext;
    private static MyClickListener myClickListener;
    private Utilities utilities;


    public static class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView dateView;
        TextView dayView;
        TextView temp;
        LinearLayout container;
        ImageView imageView;


        public viewHolder(View rootView) {
            super(rootView);
            container = (LinearLayout)rootView.findViewById(R.id.card_view_layout);
            dateView= (TextView)rootView.findViewById(R.id.textView_Date);
            dayView= (TextView)rootView.findViewById(R.id.textView_Day);
            temp = (TextView)rootView.findViewById(R.id.textView_temp);
            imageView = (ImageView)rootView.findViewById(R.id.imageView_weatherIcon);
            //TextView tempView = (TextView) rootView.findViewById(R.id.textView_temp);
            //TextView minMaxView= (TextView)rootView.findViewById(R.id.textView_minMax);
            //TextView locationName= (TextView) rootView.findViewById(R.id.textView_locationName);
            //TextView desc = (TextView)rootView.findViewById(R.id.textView_desc);
            itemView.setOnClickListener(this);

        }



        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    private void setAnimation(View view){
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.abc_popup_enter);
        view.startAnimation(anim);
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(Context context, ArrayList<Weather> myDataset) {
        mContext=context;
        mDataset = myDataset;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        utilities =new Utilities();
        holder.dayView.setText(mDataset.get(position).getDay());
        holder.dateView.setText(mDataset.get(position).getDate());
        holder.temp.setText(mDataset.get(position).getDay_Temp() + "\u00B0");
        holder.imageView.setImageResource(utilities.getIconImgSrc(mDataset.get(position).getIcon()));
        setAnimation(holder.container);

        final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if(lp instanceof StaggeredGridLayoutManager.LayoutParams){
            StaggeredGridLayoutManager.LayoutParams sglp= (StaggeredGridLayoutManager.LayoutParams)lp;
            sglp.setFullSpan(position==0);
            holder.itemView.setLayoutParams(sglp);
        }
    }

    public void clear(){

        notifyItemRangeRemoved(0, getItemCount());
        mDataset.clear();
    }

    public void add(Weather data) {
        mDataset.add(data);
        notifyItemInserted(getItemCount() - 1);
    }

//    public void addItem(Weather dataObj, int index) {
//        mDataset.add(index, dataObj);
//        notifyItemInserted(index);
//    }
//
//    public void deleteItem(int index) {
//        mDataset.remove(index);
//        notifyItemRemoved(index);
//    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
