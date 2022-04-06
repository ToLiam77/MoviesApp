package com.example.movies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultHolder> {
    Context context;
    List<ParseObject> list;
    boolean areAllChecked = false;

    //We need to have this arrayList to keep the checked items
    public ArrayList<String> arrayListChecked = new ArrayList<String>();
    public ArrayList<String> arrayListUnchecked = new ArrayList<String>();

    public ResultAdapter (Context context, List<ParseObject> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.result_cell, parent, false);
        return new ResultHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultHolder holder, @SuppressLint("RecyclerView") int position) {
        //Select All Boxes if Required
        holder.cb.setChecked(areAllChecked);

        //Get the ParseObject from the list
        ParseObject object = list.get (position);
        //Get the name of the author and display in the textView
        if (object.getString("title") != null){
            holder.name.setText(object.getString("title"));
        }else{
            holder.name.setText("null");
        }

        //Handle the checkbox
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (areAllChecked){
                    if(!holder.cb.isChecked()){
                        System.out.println(list.get(position).getString("title"));
                        arrayListUnchecked.add(list.get(position).getString("title"));
                    }else{
                        arrayListUnchecked.remove(list.get(position).getString("title"));
                    }
                }else{
                    if(holder.cb.isChecked()){
                        System.out.println(list.get(position).getString("title"));
                        arrayListChecked.add(list.get(position).getString("title"));
                    }else{
                        arrayListChecked.remove(list.get(position).getString("title"));
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<String> getListChecked(){
        return arrayListChecked;
    }

    public ArrayList<String> getListUnchecked(){
        return arrayListUnchecked;
    }

    public void setAllBoxesChecked(){
        areAllChecked = true;
        notifyDataSetChanged();
    }

    public void clearList(){
        list = new ArrayList<>();
        notifyDataSetChanged();
    }
}
