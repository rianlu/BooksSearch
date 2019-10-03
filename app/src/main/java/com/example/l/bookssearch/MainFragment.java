package com.example.l.bookssearch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.l.bookssearch.utils.JsoupUtils;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private EditText etKey;
    private MaterialButton searchBtn;
    private String url = null;
    private Spinner spType;
    private int typeId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);
        etKey = root.findViewById(R.id.search_key);
        searchBtn = root.findViewById(R.id.search_btn);
        spType = root.findViewById(R.id.sp_type);
        initSpinner();
        initSearch();
        return root;
    }

    //初始化Spinner
    public void initSpinner(){
        List<String> typeList = new ArrayList<>();
        typeList.add("题名");
        typeList.add("作者");
        typeList.add("主题词");
        typeList.add("ISBN");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, typeList);
        spType.setAdapter(adapter);
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    //初始化搜索
    public void initSearch(){
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = etKey.getText().toString().trim();
                url = JsoupUtils.getSearchUrl(key, typeId);
                if (url != null) {
                    SearchResultFragment searchResultFragment = new SearchResultFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", url);
                    searchResultFragment.setArguments(bundle);
                    getFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.container, searchResultFragment)
                            .commit();
                }
            }
        });
    }
}
