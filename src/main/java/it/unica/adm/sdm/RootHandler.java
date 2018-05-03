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

/**
 * Handle static requests to webserver
 *
 * @author anto
 */
public class RootHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {

        //Decodifica url passato al server e lo rende come stringa
        String request = URLDecoder.decode(he.getRequestURI().toASCIIString(), "UTF-8");

        //ottiene l'header per la risposta (modificabile)
        Headers responseHeaders = he.getResponseHeaders();

        //request = request.substring(1);

        String response = "404 - Page Not Found";

        responseHeaders.set(TYPE, TXT);
        he.sendResponseHeaders(404, response.length());
        try (OutputStream os = he.getResponseBody()) {
            os.write(response.getBytes());
        }

    }

}
