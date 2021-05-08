package com.lakshmimanivannan.jobhuntappserviceproviders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AcceptedRequestAdapter extends RecyclerView.Adapter<AcceptedRequestAdapter.AcceptedRequestViewHolder> {
    ArrayList<AcceptedRequestHelperClass> acceptedRequest;
    private Context context;

    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public AcceptedRequestAdapter(ArrayList<AcceptedRequestHelperClass> acceptedRequest,Context context) {
        this.acceptedRequest = acceptedRequest;
        this.context = context;

    }

    @NonNull
    @Override
    public AcceptedRequestAdapter.AcceptedRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_request_lists,parent,false);
        AcceptedRequestAdapter.AcceptedRequestViewHolder acceptedRequestViewHolder = new AcceptedRequestAdapter.AcceptedRequestViewHolder(view,mListener);
        return acceptedRequestViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedRequestAdapter.AcceptedRequestViewHolder  holder, int position) {

        AcceptedRequestHelperClass acceptedRequestHelperClass = acceptedRequest.get(position);

        Glide.with(context)
                .load(acceptedRequestHelperClass.getUser_img())
                .into(holder.img);
        holder.u_name.setText(acceptedRequestHelperClass.getUsername());
        holder.timestamp.setText(acceptedRequestHelperClass.getTimestamp());
        holder.category.setText(acceptedRequestHelperClass.getCategory());
        holder.location.setText(acceptedRequestHelperClass.getLocation());
    }
    @Override
    public int getItemCount() {
        return acceptedRequest.size();
    }

    public static class AcceptedRequestViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView u_name,timestamp,category,location;

        public AcceptedRequestViewHolder(@NonNull View itemView,final AcceptedRequestAdapter.OnItemClickListener listener) {
            super(itemView);
            img = itemView.findViewById(R.id.service_img);
            u_name = itemView.findViewById(R.id.service_name);
            timestamp = itemView.findViewById(R.id.service_timestamp);
            category = itemView.findViewById(R.id.service_category);
            location = itemView.findViewById(R.id.service_location);

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

