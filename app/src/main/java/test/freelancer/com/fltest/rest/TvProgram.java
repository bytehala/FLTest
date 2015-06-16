package test.freelancer.com.fltest.rest;

import com.orm.SugarRecord;

/**
 * Created by Lem on 6/16/2015.
 */
public class TvProgram extends SugarRecord<TvProgram> {

    String name;
    String start_time;
    String end_time;
    String channel;
    String rating;

    // Sugar
    public TvProgram() {

    }

    public String getName() {
        return name;
    }

    public String getStartTime() {
        return start_time;
    }

    public String getEndTime() {
        return end_time;
    }

    public String getRating() {
        return rating;
    }

    public String getChannel() {
        return channel;
    }
}
