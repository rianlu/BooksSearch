package com.example.l.bookssearch;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.l.bookssearch.adapter.MyAdapter;
import com.example.l.bookssearch.databinding.FragmentSearchBinding;
import com.example.l.bookssearch.utils.JsoupUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private String TAG = "Search";
    private Document doc;
    private String url;
    private MyViewModel viewModel;
    private FragmentSearchBinding binding;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        url = viewModel.getUrl();
        initUI();
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getActivity());

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        refreshView();
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
                        viewModel.getBookList().postValue(bookList);
                        String pagInfo = "当前在第" + url.substring(url.length() - 1) + "页";
                        viewModel.getPageInfo().postValue(pagInfo);
                    }
                } catch (IOException e) {
                    Log.d(TAG, "run: " + e.getMessage());
                    Looper.prepare();
                    Toast.makeText(getActivity(), "请求超时", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void initUI() {

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        binding.rv.setLayoutManager(manager);
        binding.rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 16;
                outRect.right = 16;
                outRect.bottom = 16;
                outRect.left = 16;
            }
        });

        viewModel.getBookList().observe(getActivity(), new Observer<List<Book>>() {
            @Override
            public void onChanged(@Nullable List<Book> books) {
                MyAdapter adapter = new MyAdapter((viewModel.getBookList().getValue()));
                binding.rv.setAdapter(adapter);
            }
        });

        binding.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.previous_btn){
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
        });
    }
}
