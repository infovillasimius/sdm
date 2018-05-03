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
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handler for REST Storing service
 *
 * @author anto
 */
public class Handler implements HttpHandler {

    public Handler() {

    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        String response = "";

        String request = URLDecoder.decode(he.getRequestURI().toASCIIString(), ENC);

        String par = request;
        String timestamp;
        int index, index2;
        double temp;
        boolean motionState;
        String motionDuration;
        String previousMotionDuration;

        try {
            timestamp = par.substring(3, 13);

            index = par.indexOf("temperature=") + 12;
            index2 = par.indexOf(",", index);

            temp = Double.parseDouble(par.substring(index, index2));

            index = par.indexOf("motionState=") + 12;
            index2 = par.indexOf(",", index);
            if (index2 < 0) {
                index2 = par.length();
            }
            motionState = par.substring(index, index2).equalsIgnoreCase("true");

            index = par.indexOf("motionDuration=") + 15;
            index2 = par.indexOf(",", index);
            if (index2 < 0) {
                index2 = par.length();
            }
            motionDuration = par.substring(index, index2);

            index = par.indexOf("previousMotionDuration=") + 23;
            index2 = par.indexOf(",", index);
            if (index2 < 0) {
                index2 = par.length();
            }
            previousMotionDuration = par.substring(index, index2);
            System.out.println("time = " + timestamp);
            System.out.println("temp = " + temp);
            System.out.println("motionState = " + motionState);
            System.out.println("motionDuration = " + motionDuration);
            System.out.println("previousMotionDuration = " + previousMotionDuration);
            MyPojo obj = new MyPojo(timestamp, temp, motionState, motionDuration, previousMotionDuration);
            try {
                //Storer.store(obj);
                Writer.store(obj);
            } catch (UnknownHostException | ExecutionException | InterruptedException ex) {
                Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
            }

            response = obj.toString();

            Headers responseHeaders = he.getResponseHeaders();
            responseHeaders.set(TYPE, TXT);

            he.sendResponseHeaders(200, response.length());
            try (OutputStream os = he.getResponseBody()) {
                os.write(response.getBytes());
            }

        } catch (IOException | NumberFormatException e) {
            Logger.getLogger(QueryHandler.class.getName()).log(Level.SEVERE, null, e);
            response="500 - Server Error";
            Headers responseHeaders = he.getResponseHeaders();
            responseHeaders.set(TYPE, TXT);
            he.sendResponseHeaders(500, response.length());
            try (OutputStream os = he.getResponseBody()) {
                os.write(response.getBytes());
            }
        }

    }

}
