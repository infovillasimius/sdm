/*
 * Copyright (C) 2018 anto
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.unica.adm.sdm;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.commands.kv.FetchValue;
import com.basho.riak.client.api.commands.timeseries.Query;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.client.core.query.timeseries.Cell;
import com.basho.riak.client.core.query.timeseries.QueryResult;
import com.basho.riak.client.core.query.timeseries.Row;
import static it.unica.adm.sdm.Glossary.TIMESTAMPKEY;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Used to get desired data from Riak
 *
 * @author anto
 */
public class Loader {

    static class Pojo {

        public String timestamp;
        public double temp;
        public String motionState;
        public String motionDuration;
        public String previousMotionDuration;
    }

    public static MyPojo load(String key) throws UnknownHostException, ExecutionException, InterruptedException {

        RiakClient client = RiakClient.newClient();
        Location location = new Location(new Namespace("tempControl"), key);

        FetchValue fv = new FetchValue.Builder(location).build();
        FetchValue.Response response = client.execute(fv);

        // Fetch object as String
        //String value = response.getValue(String.class);
        Pojo p = response.getValue(Pojo.class);

        client.shutdown();

        if (p != null) {
            return new MyPojo(p.timestamp, p.temp, p.motionState.equalsIgnoreCase("true"), p.motionDuration, p.previousMotionDuration);
        }
        return null;

    }

    public static ArrayList<MyPojo> load(String key, String key2) throws UnknownHostException, ExecutionException, InterruptedException {
        ArrayList<MyPojo> list = new ArrayList<>();

        RiakClient client = RiakClient.newClient();

        long t1 = Long.parseLong(key);

        long t2 = Long.parseLong(key2);

        for (long l = t1; l <= t2; l++) {

            Location location = new Location(new Namespace("tempControl"), l + "");
            FetchValue fv = new FetchValue.Builder(location).build();
            FetchValue.Response response = client.execute(fv);
            Pojo p = response.getValue(Pojo.class);
            if (p != null) {
                MyPojo myPojo = new MyPojo(p.timestamp, p.temp, p.motionState.equalsIgnoreCase("true"), p.motionDuration, p.previousMotionDuration);
                list.add(myPojo);
                //l+=60;
            }

        }

        client.shutdown();

        return list;

    }

    public static ArrayList<MyPojo> find(String key, String key2) throws UnknownHostException, ExecutionException, InterruptedException {
        ArrayList<MyPojo> list = new ArrayList<>();

        RiakClient client = RiakClient.newClient();

        long t1 = Long.parseLong(key + "000");
        long t2 = Long.parseLong(key2 + "000");

        if (t1 == t2) {
            t1 = t1 - 500;
            t2 = t2 + 500;
        }

        String queryText = "select timeStampKey, motionDuration, previousMotionDuration, time, motionState, temperature  "
                + " from " + TIMESTAMPKEY
                + " where timeStampKey = '" + TIMESTAMPKEY + "' and time > " + t1 + " and time < " + t2;

        //System.out.println(queryText);

        Query query = new Query.Builder(queryText).build();
        QueryResult queryResult = client.execute(query);

        if (queryResult.getRowsCount() > 0) {

            for (Row r : queryResult) {
                List<Cell> l = r.getCellsCopy();
                //System.out.println(l.toString());

                String tstamp = l.get(3).getTimestamp() / 1000 + "";
                //System.out.println(tstamp);
                list.add(new MyPojo(tstamp, l.get(5).getDouble(), l.get(4).getBoolean(), l.get(1).getVarcharAsUTF8String(), l.get(2).getVarcharAsUTF8String()));

            }
        }
        client.shutdown();

        return list;
    }

}
