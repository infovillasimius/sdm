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

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static it.unica.adm.sdm.Glossary.*;


/**
 *
 * @author anto
 */
public class SensorDataManager {

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String rawAddress = Glossary.RAW_ADDRESS;
        String rawPort = Glossary.RAW_PORT;

        if (args != null && args.length > 1 && !args[0].isEmpty() && !args[1].isEmpty()) {
            System.out.println(args[0]);
            System.out.println(args[1]);
            rawAddress = args[0];
            rawPort = args[1];
        }
        
        int port = Integer.parseInt(rawPort);

        InetAddress address;
        try {
            byte[] addr = getAddress(rawAddress);
            address = InetAddress.getByAddress(addr);

            System.out.println("Il server opera all'indirizzo: " + address + " porta: " + port);

            InetSocketAddress addrs = new InetSocketAddress(address, port);
            try {

                HttpServer server = HttpServer.create();

                server.bind(addrs, Glossary.MAX_REQUESTS);

                server.createContext(STORE, new Handler());
                
                server.createContext(QUERY, new QueryHandler());

                server.createContext(WEB_ROOT, new RootHandler());

                server.start();
                
            } catch (IOException ex) {
                Logger.getLogger(SensorDataManager.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (UnknownHostException ex) {
            Logger.getLogger(SensorDataManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    static private byte[] getAddress(String raw) {

        byte[] addr = {0, 0, 0, 0};

        if (raw.matches(Glossary.IP_REGEX)) {
            for (int i = 0; i < 3; i++) {
                addr[i] = (byte) Integer.parseInt(raw.substring(0, raw.indexOf('.')));
                raw = raw.substring(raw.indexOf('.') + 1);
            }
            addr[3] = (byte) Integer.parseInt(raw);
            return addr;
        }

        return null;
    }
    
}
