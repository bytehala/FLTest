package test.freelancer.com.fltest.rest;

import com.orm.SugarRecord;

/**
 * Created by Lem on 6/16/2015.
 */
public class TvProgram extends SugarRecord<TvProgram> {

    String result;
    String start_time;
    String end_time;
    String channel;
    String rating;

    // Sugar
    public TvProgram() {

    }
}
