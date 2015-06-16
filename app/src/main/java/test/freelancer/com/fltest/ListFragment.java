package test.freelancer.com.fltest;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
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
import java.util.List;

import javax.inject.Inject;

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

    @Inject
    TvgService service;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView view = (ListView) inflater.inflate(R.layout.fragment_list, container, false);
        ((MainActivity) getActivity()).getApplicationComponent().inject(this);

        service.listTvPrograms(new Callback<ResultModel>() {
            @Override
            public void success(ResultModel resultModel, Response response) {
                Toast.makeText(getActivity(), "Num: " + resultModel.getCount(), Toast.LENGTH_SHORT).show();
                List<TvProgram> results = resultModel.getResults();
                saveTvPrograms(results);
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
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

}
