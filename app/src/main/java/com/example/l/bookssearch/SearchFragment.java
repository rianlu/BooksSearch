package com.example.l.bookssearch;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.l.bookssearch.adapter.MyAdapter;
import com.example.l.bookssearch.databinding.FragmentSearchBinding;
import com.example.l.bookssearch.model.Book;
import com.example.l.bookssearch.utils.JsoupUtils;
import com.example.l.bookssearch.viewmodel.BookViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private String TAG = "SearchFragment";
    private BookViewModel viewModel;
    private FragmentSearchBinding binding;
    private JsoupUtils jsoupUtils;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        viewModel = ViewModelProviders.of(requireActivity()).get(BookViewModel.class);
        jsoupUtils = JsoupUtils.getInstance();

        initView();

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(requireActivity());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        hideSoftKeyboard(getView());
        refreshView();
    }

    private void initView() {

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

        viewModel.getBookList().observe(requireActivity(), new Observer<List<Book>>() {
            @Override
            public void onChanged(@Nullable List<Book> books) {
                MyAdapter adapter = new MyAdapter((viewModel.getBookList().getValue()));
                adapter.setItemCLickListener(new MyAdapter.OnItemCLickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String detailUrl = jsoupUtils.getBookDetailUrl(position);
                                if (jsoupUtils.checkBookFormat(detailUrl, requireActivity())) {
                                    requireActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            NavController controller = Navigation.findNavController(view);
                                            controller.navigate(R.id.action_searchFragment_to_detailFragment);
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "当前图书不符合查询格式，将使用浏览器打开", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(detailUrl));
                                    startActivity(intent);
                                }
                            }
                        }).start();
                    }
                });
                binding.rv.setAdapter(adapter);
            }
        });

        binding.setClickListener(view -> {
            if (view.getId() == R.id.previous_btn){
                String url = jsoupUtils.getPreviousUrl(viewModel.getUrl());
                if (!TextUtils.isEmpty(url)){
                    // 把上一页的url存放到viewModel
                    viewModel.setUrl(url);
                    refreshView();
                }else {
                    Toast.makeText(getActivity(), "已经是第一页了", Toast.LENGTH_SHORT).show();
                }
            }else{
                String url = jsoupUtils.getNextUrl(viewModel.getUrl());
                if (!TextUtils.isEmpty(url)){
                    // 把下一页的url存放到viewModel
                    viewModel.setUrl(url);
                    refreshView();
                }else {
                    Toast.makeText(getActivity(), "已经是最后一页了", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void refreshView() {
        new BookListAsyncTask().execute();
    }


    class BookListAsyncTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog = ProgressDialog.show(requireActivity(), "提示", "加载中...");

        @Override
        protected Void doInBackground(Void... voids) {
            // 获得图书的基本信息
            List<Book> bookList = jsoupUtils.getTotalBooks(viewModel.getUrl());
            Log.d(TAG, "doInBackground: " + bookList.toString());
            viewModel.getBookList().postValue(bookList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
        }
    }

    private void hideSoftKeyboard(View view)
    {
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
