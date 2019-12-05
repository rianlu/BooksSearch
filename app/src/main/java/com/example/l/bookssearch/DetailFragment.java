package com.example.l.bookssearch;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.l.bookssearch.databinding.FragmentDetailBinding;
import com.example.l.bookssearch.model.Book;
import com.example.l.bookssearch.utils.JsoupUtils;
import com.example.l.bookssearch.viewmodel.BookViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private BookViewModel viewModel;
    private FragmentDetailBinding binding;
    private JsoupUtils jsoupUtils;
    private String TAG = "DetailFragment";

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        viewModel = ViewModelProviders.of(requireActivity()).get(BookViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(requireActivity());
        jsoupUtils = JsoupUtils.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new ImageAsyncTask().execute();
    }

    class ImageAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            return jsoupUtils.getBookImageFromBaiduBike(viewModel.getDetailBook().getValue().getTitle());
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(TAG, "onPostExecute: " + s);
            Glide.with(requireActivity())
                    .load(s)
                    .into(binding.imageView);
        }
    }
}
