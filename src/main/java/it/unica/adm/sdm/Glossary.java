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

/**
 * Glossary class for strings, arrays and enums used in the webserver.
 *
 * @author anto
 */
public class Glossary {

    /**
     * WebServer parameters
     */
    public static final String RAW_ADDRESS = "192.168.1.120";
    public static final String RAW_PORT = "80";

    public static final String STORE = "/s";
    public static final String QUERY = "/q";
    public static final String IP_REGEX = "[0-9]*\\.[0-9]*\\.[0-9]*\\.[0-9]*";
    public static final String WEB_ROOT = "/";
    public static final int MAX_REQUESTS = 10;

    public static final String ENC = "UTF-8";

    public static final String TYPE = "content-type";
    public static final String CSS = "text/css";
    public static final String GIF = "image/gif";
    public static final String HTML = "text/html";
    public static final String ICO = "image/x-icon";
    public static final String JS = "application/x-javascript";
    public static final String JSON = "application/json";
    public static final String PNG = "image/png";
    public static final String TXT = "text/plain";
    public static final String SVG = "image/svg+xml";

    public static final String[] MIME = {"css", "gif", "html", "ico", "js", "png", "txt"};
    public static final String[] MIME_TYPE = {CSS, GIF, HTML, ICO, JS, PNG, TXT};

    public static final String BUCKET = "tempControl";
    public static final String TIMESTAMPKEY = "TempControl";
    public static final int PPERIOD = 120;
    public static final int PERIOD = 600;
    public static final int OPERIOD = 3600;
    public static final int NPERIOD = 60;

}
