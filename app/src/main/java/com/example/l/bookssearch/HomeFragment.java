package com.example.l.bookssearch;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.l.bookssearch.databinding.FragmentHomeBinding;
import com.example.l.bookssearch.utils.JsoupUtils;
import com.example.l.bookssearch.viewmodel.BookViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private int typeId;
    private BookViewModel viewModel;
    private FragmentHomeBinding binding;
    private String TAG = "HomeFragment";
    private JsoupUtils jsoupUtils;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        viewModel = ViewModelProviders.of(requireActivity()).get(BookViewModel.class);
        jsoupUtils = JsoupUtils.getInstance();
        initView();
        return binding.getRoot();
    }

    private void startSearch(String key) {
        if (TextUtils.isEmpty(key)) {
            Toast.makeText(getActivity(), "请输入关键词！", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = jsoupUtils.getSearchUrl(key, typeId);
        Log.d(TAG, "startSearch: " + typeId);
        viewModel.setUrl(url);
        Log.d(TAG, "startSearch: " + viewModel.getUrl());
        NavController controller = Navigation.findNavController(getView());
        controller.navigate(R.id.action_homeFragment_to_searchFragment);
    }

    private void initView() {
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d(TAG, "onCheckedChanged: i=" + i);
                switch (i) {
                    case R.id.book_name:
                        typeId = 0;
                        break;
                    case R.id.author:
                        typeId = 1;
                        break;
                    case R.id.isbn:
                        typeId = 3;
                        break;
                }
                Log.d(TAG, "onCheckedChanged: typeId" + typeId);
            }
        });
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                startSearch(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearch(String.valueOf(binding.searchView.getQuery()));
            }
        });
    }
}
