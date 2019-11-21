package com.example.l.bookssearch.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.l.bookssearch.model.Book;
import com.example.l.bookssearch.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Book> list;
    public MyAdapter(List<Book> list){
        this.list = list;
    }
    private OnItemCLickListener itemCLickListener;

    public void setItemCLickListener(OnItemCLickListener itemCLickListener) {
        this.itemCLickListener = itemCLickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Book book = list.get(i);
        viewHolder.getRvTitle().setText(book.getTitle());
        viewHolder.getRvPublish().setText(book.getPublish());
        viewHolder.getRvCount().setText(book.getCount());
        viewHolder.getRvNum().setText(book.getNum());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView rvTitle;
        TextView rvPublish;
        TextView rvCount;
        TextView rvNum;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rvTitle = itemView.findViewById(R.id.rv_title);
            rvPublish = itemView.findViewById(R.id.rv_publish);
            rvCount = itemView.findViewById(R.id.rv_count);
            rvNum = itemView.findViewById(R.id.rv_num);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    itemCLickListener.onItemClick(view, position);
                }
            });
        }

        TextView getRvTitle() {
            return rvTitle;
        }

        TextView getRvPublish() {
            return rvPublish;
        }

        TextView getRvCount() {
            return rvCount;
        }

        TextView getRvNum() {
            return rvNum;
        }
    }

    public interface OnItemCLickListener {
        void onItemClick(View view, int position);
    }
}
