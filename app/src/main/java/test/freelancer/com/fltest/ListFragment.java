package test.freelancer.com.fltest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import test.freelancer.com.fltest.recycler.EndlessRecyclerOnScrollListener;
import test.freelancer.com.fltest.recycler.SpacesItemDecoration;
import test.freelancer.com.fltest.rest.ResultModel;
import test.freelancer.com.fltest.rest.TvProgram;
import test.freelancer.com.fltest.rest.TvgService;

/**
 * List that displays the TV Programmes
 */
public class ListFragment extends Fragment {

    public static final int START_INCREMENT = 10;
    List<TvProgram> results;
    @Inject
    TvgService service;
    int start = 0;
    private TvProgramAdapter adapter;
    int resultCount = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_list, container, false);
        ((MainActivity) getActivity()).getApplicationComponent().inject(this);
        recyclerView.addItemDecoration(new SpacesItemDecoration(20));

        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        results = new ArrayList<>();
        adapter = new TvProgramAdapter(results);
        recyclerView.setAdapter(adapter);

        loadTvPrograms(start);

        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                start += START_INCREMENT;
                if(start < resultCount)
                    loadTvPrograms(start);
            }
        });

        return recyclerView;
    }

    private void loadTvPrograms(int start) {
        service.listTvPrograms(start, new Callback<ResultModel>() {
            @Override
            public void success(ResultModel resultModel, Response response) {
                if (resultModel.getResults() == null)
                    return;
                resultCount = resultModel.getCount();
                results.addAll(resultModel.getResults());
                saveTvPrograms(results);
                adapter.notifyDataSetChanged();
            }

            private void saveTvPrograms(List<TvProgram> results) {
                TvProgram.deleteAll(TvProgram.class);
                for (TvProgram program : results) {
                    program.save();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("MainActivity", error.getMessage());
                Toast.makeText(getActivity(), "This is cached data", Toast.LENGTH_SHORT).show();

                results.addAll(TvProgram.find(TvProgram.class, null, null));
                adapter.notifyDataSetChanged();
            }
        });
    }

}
