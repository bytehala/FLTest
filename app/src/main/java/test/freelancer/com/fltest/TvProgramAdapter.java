package test.freelancer.com.fltest;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import test.freelancer.com.fltest.rest.TvProgram;

/**
 * Created by Lem on 6/16/2015.
 */
public class TvProgramAdapter extends RecyclerView.Adapter<TvProgramAdapter.ViewHolder> {

    List<TvProgram> programList;

    public TvProgramAdapter(List<TvProgram> programList) {
        this.programList = programList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_tv_program, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        TvProgram tvProgram = programList.get(i);

        viewHolder.name.setText(tvProgram.getName());
        viewHolder.start_time.setText(tvProgram.getStartTime());
        viewHolder.end_time.setText(tvProgram.getEndTime());
        viewHolder.rating.setText(tvProgram.getRating());
        viewHolder.channel.setText(tvProgram.getChannel());
    }

    @Override
    public int getItemCount() {
        return programList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView rating;
        public TextView start_time;
        public TextView end_time;
        public TextView channel;

        public ViewHolder(View cardView) {
            super(cardView);
            name = (TextView) cardView.findViewById(R.id.name);
            rating = (TextView) cardView.findViewById(R.id.rating);
            start_time = (TextView) cardView.findViewById(R.id.start_time);
            end_time = (TextView) cardView.findViewById(R.id.end_time);
            channel = (TextView) cardView.findViewById(R.id.channel);

        }
    }
}
