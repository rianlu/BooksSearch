package com.example.l.bookssearch;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.l.bookssearch.databinding.FragmentHomeBinding;
import com.example.l.bookssearch.utils.JsoupUtils;

import java.util.ArrayList;
import java.util.List;


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
        viewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);

        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = binding.searchKey.getText().toString().trim();
                if (TextUtils.isEmpty(key)) {
                    Toast.makeText(getActivity(), "请输入关键词！", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = JsoupUtils.getSearchUrl(key, typeId);
                viewModel.setUrl(url);
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_homeFragment_to_searchFragment);
            }
        });

        initSpinner();

        return binding.getRoot();
    }

    //初始化Spinner
    public void initSpinner(){

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, viewModel.getTypes());
        binding.spType.setAdapter(adapter);
        binding.spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                typeId = 0;
            }
        });
    }
}
