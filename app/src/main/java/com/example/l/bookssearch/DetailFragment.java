package com.example.l.bookssearch;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.l.bookssearch.data.Book;
import com.example.l.bookssearch.databinding.FragmentDetailBinding;
import com.example.l.bookssearch.utils.JsoupUtil;
import com.example.l.bookssearch.viewmodel.BookViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private BookViewModel viewModel;
    private FragmentDetailBinding binding;
    private JsoupUtil jsoupUtil;
    private String TAG = DetailFragment.class.getName();
    private String detailUrl = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        viewModel = ViewModelProviders.of(requireActivity()).get(BookViewModel.class);
        jsoupUtil = JsoupUtil.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new ImageAsyncTask().execute();

        Book detailBook = viewModel.getDetailBook().getValue();
        binding.tvTitle.setText(detailBook.getTitle());
        binding.tvAuthor.setText(detailBook.getAuthor());
        binding.tvPublish.setText(detailBook.getPublish());
        binding.tvLocation.setText(detailBook.getLocation());
        binding.tvIsbn.setText(detailBook.getIsbn());
        binding.tvDescription.setText(detailBook.getDescription());
        Bundle bundle = getArguments();
        detailUrl = bundle.getString("detailUrl");
        if (bundle != null) {
            Book book = bundle.getParcelable("currentBook");
            if (book != null) {
                 detailBook.setNum(book.getNum());
            }
        }
        binding.tvNum.setText(detailBook.getNum());
    }

    class ImageAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            return jsoupUtil.getBookImageFromBaiduBike(viewModel.getDetailBook().getValue().getTitle());
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(TAG, "onPostExecute: " + s);
            if (s != null && !"".equals(s)) {
                Glide.with(requireActivity())
                        .load(s)
                        .into(binding.imageView);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_with_brower:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(detailUrl));
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
