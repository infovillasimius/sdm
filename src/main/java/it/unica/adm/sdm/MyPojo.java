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

import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author anto
 */
public class MyPojo {

    public String timestamp;
    public double temp;
    public boolean motionState;
    public String motionDuration;
    public String previousMotionDuration;
    public Long time;

    public MyPojo(String timestamp, double temp, boolean motionState, String motionDuration, String previousMotionDuration) {
        this.timestamp = timestamp;
        this.temp = temp;
        this.motionState = motionState;
        this.motionDuration = motionDuration;
        this.previousMotionDuration = previousMotionDuration;
        this.time=Long.parseLong(timestamp);
    }

    @Override
    public String toString() {
        JsonObject json = Json.createObjectBuilder()
                .add("timestamp", timestamp)
                .add("temp", temp)
                .add("motionState", motionState)
                .add("motionDuration", motionDuration)
                .add("previousMotionDuration", previousMotionDuration)
                .build();
        return json.toString();

        //return "MyPojo{" + "timestamp=" + timestamp + ", temp=" + temp + ", motionState=" + motionState + ", motionDuration=" + motionDuration + ", previousMotionDuration=" + previousMotionDuration + '}';
    }

}
