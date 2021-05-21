package com.example.my_coach.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_coach.Model.AbouteSportModel;
import com.example.my_coach.R;

import java.util.List;

public class AbouteSportAdapter extends RecyclerView.Adapter<AbouteSportAdapter.ViewHolder>{

    public static String UID;
    public Context context;
    private List<AbouteSportModel> list;

    public AbouteSportAdapter(Context context, List<AbouteSportModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AbouteSportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.formate_sport_brief,parent,false ));
    }

    @Override
    public void onBindViewHolder(@NonNull AbouteSportAdapter.ViewHolder holder, int position) {
        holder.setSport_brief(list.get(position).getSport_brief());
        holder.setSport_Image(list.get(position).getSport_image());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView Sport_brief;
        private ImageView Sport_Image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Sport_Image=itemView.findViewById(R.id.SportBrief_Image);
            Sport_brief=itemView.findViewById(R.id.SportBrief_Brief);
        }

        void setSport_brief(String brief) {
            Sport_brief.setText(brief);
        }

        void setSport_Image(String url) {
            if (!url.equals("nul")) {
                Glide.with(context)
                        .load(url)
                        .placeholder(R.drawable.graduation_project_logo)
                        .into(Sport_Image);
            }
        }

    }

}


