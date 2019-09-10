package com.whywhom.soft.huarongdao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.whywhom.soft.huarongdao.R;
import com.whywhom.soft.huarongdao.ui.home.HomeFragment;

import java.util.ArrayList;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.ViewHolder>{

    private MutableLiveData<ArrayList<String>> multiLevelData;
    private ArrayList<String> levelData;
    private Context context;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public LevelAdapter(Context context, OnItemClickListener listener, MutableLiveData<ArrayList<String>> data) {
        multiLevelData = data;
        levelData = multiLevelData.getValue();
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LevelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelAdapter.ViewHolder holder, int position) {
        String str = levelData.get(position);
        holder.btnName.setText(str);
        holder.btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return levelData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        Button btnName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnName = itemView.findViewById(R.id.btn_name);
        }
    }
}
