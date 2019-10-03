package com.example.l.bookssearch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.l.bookssearch.adapter.MyAdapter;
import com.example.l.bookssearch.utils.JsoupUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class SearchResultFragment extends Fragment implements View.OnClickListener {

    private TextView tvCount;
    private RecyclerView recyclerView;
    private MaterialButton previousBtn;
    private MaterialButton nextBtn;
    private MyHandler handler;
    private Document doc = null;
    private String url = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            url = bundle.getString("URL");
        }
        handler = new MyHandler(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_result, container, false);
        tvCount = root.findViewById(R.id.show_count);
        previousBtn = root.findViewById(R.id.previous_btn);
        nextBtn = root.findViewById(R.id.next_btn);
        previousBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView = root.findViewById(R.id.rv);
        recyclerView.setLayoutManager(manager);
        refreshView();
        return root;
    }

    public void refreshView(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    doc = Jsoup.connect(url).get();
                    if (doc != null){
                        // 获得图书的基本信息
                        List<Book> bookList = JsoupUtils.getTotalBooks(doc);
                        handler.obtainMessage(1, bookList).sendToTarget();
                        String pagCountInfo = "当前在第" + (Integer.parseInt(JsoupUtils.getNextUrl(doc).substring(JsoupUtils.getNextUrl(doc).length()-1))-1) + "页";
                        handler.obtainMessage(2, JsoupUtils.getTotalCount(doc) + pagCountInfo).sendToTarget();
                    }
                } catch (IOException e) {
                    Toast.makeText(getActivity(), "请求超时", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.previous_btn){
            url = JsoupUtils.getPreviousUrl(doc);
            if (!TextUtils.isEmpty(url)){
                refreshView();
            }else {
                Toast.makeText(getActivity(), "已经是第一页了", Toast.LENGTH_SHORT).show();
            }
        }else{
            url = JsoupUtils.getNextUrl(doc);
            if (!TextUtils.isEmpty(url)){
                refreshView();
            }else {
                Toast.makeText(getActivity(), "已经是最后一页了", Toast.LENGTH_SHORT).show();
            }
        }
    }

    static class MyHandler extends Handler {

        WeakReference<SearchResultFragment> weakReference;
        MyHandler(SearchResultFragment activity){
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SearchResultFragment activity = weakReference.get();
            if (msg.what == 1){
                if (activity != null){
                    MyAdapter adapter = new MyAdapter((List<Book>) msg.obj);
                    activity.recyclerView.setAdapter(adapter);
                }
            }
            if (msg.what == 2){
                if (activity != null){
                    activity.tvCount.setText((String)msg.obj);
                }
            }
        }
    }
}
