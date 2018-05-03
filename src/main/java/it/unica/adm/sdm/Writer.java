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

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.commands.timeseries.Store;
import com.basho.riak.client.core.query.timeseries.*;
import static it.unica.adm.sdm.Glossary.TIMESTAMPKEY;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author anto
 */
public class Writer {

    public static void store(ArrayList<MyPojo> list) throws ExecutionException, InterruptedException, UnknownHostException {

        RiakClient client = RiakClient.newClient();
        List<Row> rows = new ArrayList();
        for (MyPojo m : list) {

            rows.add(new Row(
                    new Cell(TIMESTAMPKEY),
                    new Cell(m.motionDuration),
                    new Cell(m.previousMotionDuration),
                    Cell.newTimestamp(Long.parseLong(m.timestamp + "000")),
                    new Cell(m.motionState),
                    new Cell(m.temp)
            ));

            Store storeCmd = new Store.Builder(TIMESTAMPKEY).withRows(rows).build();
            client.execute(storeCmd);
        }
        client.shutdown();
    }

    public static void store(MyPojo m) throws ExecutionException, InterruptedException, UnknownHostException {

        RiakClient client = RiakClient.newClient();

        List<Row> rows = Arrays.asList(
                new Row(
                        new Cell(TIMESTAMPKEY),
                        new Cell(m.motionDuration),
                        new Cell(m.previousMotionDuration),
                        Cell.newTimestamp(Long.parseLong(m.timestamp + "000")),
                        new Cell(m.motionState),
                        new Cell(m.temp)
                )
        );

        Store storeCmd = new Store.Builder(TIMESTAMPKEY).withRows(rows).build();
        client.execute(storeCmd);

        client.shutdown();
    }
}
