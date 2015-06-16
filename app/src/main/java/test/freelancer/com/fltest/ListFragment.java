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

        // eurgh, damn android.os.NeworkOnMainThreadException - so pesky!
        // stackoverflow told me to do this:
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // download the program guide
        String JsonResponse = connect("http://whatsbeef.net/wabz/guide.php?start=0");
        try {
            JSONObject json = new JSONObject(JsonResponse);
            view.setAdapter(new ListAdapter(json.getJSONArray("results")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    public static String connect(String url) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);
                instream.close();
                return result;
            }
        } catch (IOException e) {
        }
        return null;
    }

    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public class ListAdapter extends BaseAdapter {

        JSONArray array;

        public ListAdapter(JSONArray response) {
            array = response;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public JSONObject getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);

            try {
                TextView name = new TextView(getActivity());
                name.setText(array.getJSONObject(position).getString("name"));

                layout.addView(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return layout;
        }
    }
}
