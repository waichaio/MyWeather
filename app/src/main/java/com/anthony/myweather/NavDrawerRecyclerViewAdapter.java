package com.anthony.myweather;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by anthonyo on 27/5/2015.
 */
public class NavDrawerRecyclerViewAdapter extends RecyclerView.Adapter<NavDrawerRecyclerViewAdapter.ViewHolder> {
    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;
    private String[] city;
    private static MyClickListener myClickListener;
    private View defaultSelection;
    private Resources resources;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView city;
        int Holderid;

        public ViewHolder(View itemView,int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);
            if (ViewType==TYPE_ITEM){
                city=(TextView)itemView.findViewById(R.id.textView);
                Holderid=1;
                itemView.setOnClickListener(this);
            }else{
                Holderid=0;
            }
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }


    public NavDrawerRecyclerViewAdapter(String[] city, Resources resources){
        this.city=city;
        this.resources=resources;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public NavDrawerRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_list_item,parent,false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(NavDrawerRecyclerViewAdapter.ViewHolder holder, int position) {
        if(holder.Holderid ==1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.city.setText(city[position - 1]); // Setting the Text with the array of our Titles
            //holder.imageView.setImageResource(mIcons[position -1]);// Settimg the image with array of our icons
            if(position-1==0){
                defaultSelection=(View)holder.city;
                holder.city.setBackgroundColor(R.color.drawer_selected);
            }

        }
        else{

            //holder.profile.setImageResource(profile);           // Similarly we set the resources for header view
            //holder.Name.setText(name);
            //holder.email.setText(email);
        }
    }

    public View getDefaultSelection(){
        return defaultSelection;
    }

    @Override
    public int getItemCount() {
        return city.length+1;
    }


    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
