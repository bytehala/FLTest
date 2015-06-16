package test.freelancer.com.fltest;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import test.freelancer.com.fltest.rest.ResultModel;
import test.freelancer.com.fltest.rest.TvProgram;
import test.freelancer.com.fltest.rest.TvgService;

/**
 * List that displays the TV Programmes
 */
public class ListFragment extends Fragment {

    List<TvProgram> results;

    @Inject
    TvgService service;

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

        results = new ArrayList<TvProgram>();
        final TvProgramAdapter adapter = new TvProgramAdapter(results);
        recyclerView.setAdapter(adapter);

        service.listTvPrograms(new Callback<ResultModel>() {
            @Override
            public void success(ResultModel resultModel, Response response) {
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




        return recyclerView;
    }

}
