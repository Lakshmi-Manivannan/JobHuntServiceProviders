package com.lakshmimanivannan.jobhuntappserviceproviders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServicesViewHolder> {
    ArrayList<ServiceClass> allServices;
    private Context context;

    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public ServicesAdapter(ArrayList<ServiceClass> allServices,Context context) {
        this.allServices = allServices;
        this.context = context;

    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_request_lists,parent,false);
        ServicesViewHolder servicesViewHolder = new ServicesViewHolder(view,mListener);
        return servicesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {

        ServiceClass serviceClass = allServices.get(position);

        Glide.with(context)
                .load(serviceClass.getImage())
                .into(holder.img);
        holder.u_name.setText(serviceClass.getU_name());
        holder.timestamp.setText(serviceClass.getTimestamp());
        holder.category.setText(serviceClass.getCategory());
        holder.location.setText(serviceClass.getLocation());
    }
    @Override
    public int getItemCount() {
        return allServices.size();
    }

    public static class ServicesViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView u_name,timestamp,category,location;
        Button accept_request;

        public ServicesViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            img = itemView.findViewById(R.id.service_img);
            u_name = itemView.findViewById(R.id.service_name);
            timestamp = itemView.findViewById(R.id.service_timestamp);
            category = itemView.findViewById(R.id.service_category);
            location = itemView.findViewById(R.id.service_location);
            accept_request = itemView.findViewById(R.id.accept_service);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
