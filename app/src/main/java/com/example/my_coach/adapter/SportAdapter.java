package com.example.my_coach.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_coach.Model.SportModel;
import com.example.my_coach.R;
import com.example.my_coach.sport.SportsProperties.SportsSpecificationsActivity;

import java.util.List;

public class SportAdapter extends RecyclerView.Adapter<SportAdapter.ViewHolder> {

    private Context context;
    private List<SportModel> list;
    public static String UID;
    public SportAdapter(Context context, List<SportModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SportAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.format_sport, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SportAdapter.ViewHolder holder, int position) {
        SportModel model = list.get(position);

        holder.setSportsName(list.get(position).getName());
        holder.setSportsImage(list.get(position).getImage());

        holder.layout.setOnClickListener(v -> {
            UID = model.getId();
            Intent intent = new Intent(context, SportsSpecificationsActivity.class);
            intent.putExtra("id",model.getId());
            context.startActivity(intent);
          
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView SportsName;
        ImageView SportsImage;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.btn_sports);
            SportsName = itemView.findViewById(R.id.categories_Sports_name);
            SportsImage = itemView.findViewById(R.id.categories_Sports_image);
        }
        void setSportsName(String name) {
            SportsName.setText(name);
        }

        void setSportsImage(String url) {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.graduation_project_logo)
                    .into(SportsImage);
        }
    }
}
