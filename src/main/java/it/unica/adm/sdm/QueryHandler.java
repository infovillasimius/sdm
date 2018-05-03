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

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import static it.unica.adm.sdm.Glossary.*;
import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * Handler for REST Storing service
 *
 * @author anto
 */
public class QueryHandler implements HttpHandler {

    public QueryHandler() {

    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        ArrayList<MyPojo> list = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("####0.00");
        String response = "";

        String request = URLDecoder.decode(he.getRequestURI().toASCIIString(), ENC);

        String par = request;
        String timestamp1, timestamp2;

        try {
            timestamp1 = par.substring(3, 13);
            timestamp2 = par.substring(14, 24);

            list = Loader.find(timestamp1, timestamp2);

            ArrayList<MyPojo> movesList = Utilities.movesList(list);
            ArrayList<MoveAnalysis> moveAnalysisList = new ArrayList<>();
            for (MyPojo m : movesList) {
                MoveAnalysis moveAnalysis = new MoveAnalysis(m);
                if (moveAnalysis.isSignificative()) {
                    moveAnalysisList.add(moveAnalysis);
                    //System.out.println(moveAnalysisList);
                }

            }
            ArrayList<Double> meanMoves = Utilities.meanMoves(moveAnalysisList);
            double meanDelta = 0, meanAfter = 0, meanBefore = 0, min=0, time=0;
            if (!meanMoves.isEmpty()) {
                meanBefore = meanMoves.get(0);
                meanDelta = meanMoves.get(1);
                meanAfter = meanMoves.get(2);
                min=meanMoves.get(3);
                time=meanMoves.get(4);
            }
            
            int count=Utilities.count(Utilities.minuteMeans(list));

            JsonObject json = Json.createObjectBuilder()
                    .add("Average temperature", Utilities.meanTemp(list))
                    .add("Movements", movesList.size())
                    .add("Average minutes between moves", time)
                    .add("Average Temp before moves", meanBefore)
                    .add("Average Delta T", meanDelta)
                    .add("Average Temp after moves", meanAfter)
                    .add("Average minutes to reach the previous temperature", min)
                    .add("Minutes of working", count)
                    .add("Total Energy", 0.140*count/60)
                    .add("Observation Period (s)", OPERIOD)
                    .add("n", list.size())
                    .build();

            response = json.toString();

            Headers responseHeaders = he.getResponseHeaders();
            responseHeaders.set(TYPE, JSON);

            he.sendResponseHeaders(200, response.length());
            try (OutputStream os = he.getResponseBody()) {
                os.write(response.getBytes());
            }

        } catch (IOException | ExecutionException | InterruptedException e) {
            Logger.getLogger(QueryHandler.class.getName()).log(Level.SEVERE, null, e);
            response = "500 - Server Error";
            Headers responseHeaders = he.getResponseHeaders();
            responseHeaders.set(TYPE, TXT);
            he.sendResponseHeaders(500, response.length());
            try (OutputStream os = he.getResponseBody()) {
                os.write(response.getBytes());
            }
        }

    }
}
