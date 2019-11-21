package com.example.l.bookssearch;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.l.bookssearch.databinding.FragmentHomeBinding;
import com.example.l.bookssearch.utils.JsoupUtils;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private int typeId;
    private MyViewModel viewModel;
    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        viewModel = ViewModelProviders.of(requireActivity()).get(MyViewModel.class);

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d(TAG, "onCheckedChanged: i=" + i);
                switch (i) {
                    case R.id.book_name:
                        typeId = 1;
                        break;
                    case R.id.author:
                        typeId = 2;
                        break;
                    case R.id.isbn:
                        typeId = 4;
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
        return binding.getRoot();
    }

    public void startSearch(String key) {
        if (TextUtils.isEmpty(key)) {
            Toast.makeText(getActivity(), "请输入关键词！", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = JsoupUtils.getSearchUrl(key, typeId);
        viewModel.setUrl(url);
        NavController controller = Navigation.findNavController(getView());
        controller.navigate(R.id.action_homeFragment_to_searchFragment);
    }
}
