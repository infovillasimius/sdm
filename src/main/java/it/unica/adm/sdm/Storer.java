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
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author anto
 */
public class Storer {

    static class Pojo {

        public String timestamp;
        public double temp;
        public String motionState;
        public String motionDuration;
        public String previousMotionDuration;
    }

    public static void store(MyPojo data) throws UnknownHostException, ExecutionException, InterruptedException {

        RiakClient client = RiakClient.newClient(); 

        Location location = new Location(new Namespace("tempControl"), data.timestamp);
        Pojo obj = new Pojo();
        obj.timestamp = data.timestamp;
        obj.temp = data.temp;
        obj.motionState = data.motionState+"";
        obj.motionDuration = data.motionDuration;
        obj.previousMotionDuration = data.previousMotionDuration;

        StoreValue sv = new StoreValue.Builder(obj).withLocation(location).build();
        StoreValue.Response svResponse = client.execute(sv);
        client.shutdown();
    }

}
