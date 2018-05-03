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

import static it.unica.adm.sdm.Glossary.NPERIOD;
import static it.unica.adm.sdm.Glossary.PPERIOD;
import java.util.ArrayList;

/**
 *
 * @author anto
 */
public class Utilities {

    public static double meanTemp(ArrayList<MyPojo> list) {
        if (list == null || list.isEmpty()) {
            return -273.25;
        } else if (list.size() == 1) {
            //System.out.println(list.get(0).temp);
            return list.get(0).temp;
        }

        double mean = 0;
        long tp = 0;
        long totalTime = Long.parseLong(list.get(list.size() - 1).timestamp) - Long.parseLong(list.get(0).timestamp);
        for (MyPojo m : list) {
            long t = Long.parseLong(m.timestamp);
            if (tp != 0) {
                mean += (m.temp * (t - tp));
                tp = t;

            } else {
                tp = t;
                mean = m.temp;
            }

        }

        return mean / totalTime;

    }

    public static ArrayList<MyPojo> movesList(ArrayList<MyPojo> list) {
        ArrayList<MyPojo> moves = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            return moves;
        }
        String previous = list.get(0).previousMotionDuration;
        Long previousTime = list.get(0).time;
        for (MyPojo m : list) {
            if (!m.previousMotionDuration.equalsIgnoreCase(previous)) {
                previous = m.previousMotionDuration;

                if (m.time - previousTime >= PPERIOD) {
                    previousTime = m.time;
                    moves.add(m);
                }

            }
        }
        return moves;
    }

    public static ArrayList<Double> meanMoves(ArrayList<MoveAnalysis> list) {
        ArrayList<Double> meanMoves = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            return meanMoves;
        }
        double meanDelta = 0, meanAfter = 0, meanBefore = 0, min = 0;
        Long time = Long.parseLong("0"), tp = list.get(0).getMovement().time;

        for (MoveAnalysis m : list) {
            meanDelta += (m.getTempAfter() - m.getTempBefore());
            meanBefore += m.getTempBefore();
            meanAfter += m.getTempAfter();
            min += m.getTempsAfter().size();

            time += m.getMovement().time - tp;
            //System.out.println(m.getMovement().time - tp);
            tp = m.getMovement().time;

        }

        meanMoves.add(meanBefore / list.size());
        meanMoves.add(meanDelta / list.size());
        meanMoves.add(meanAfter / list.size());
        meanMoves.add(min / list.size());
        meanMoves.add(time.doubleValue() / list.size() / 60);
        //System.out.println(time.doubleValue() / list.size() / 60);
        return meanMoves;
    }

    public static ArrayList<Double> minuteMeans(ArrayList<MyPojo> list) {
        
        Long tp = list.get(0).time;
        ArrayList<MyPojo> local = new ArrayList<>();
        ArrayList<Double> temps = new ArrayList<>();
        for (MyPojo m : list) {
            if (m.time - tp > NPERIOD) {
                tp = m.time;
                if (!local.isEmpty()) {
                    temps.add(meanTemp(local));
                }
                local = new ArrayList<>();
            } else {
                local.add(m);
            }
        }

        return temps;
    }

    public static int count(ArrayList<Double> temps) {
        int count = 0;
        double p = temps.get(0);
        for (Double d : temps) {
            if (d < p) {
                count++;
            }
            p = d;
        }

        return count;

    }

}
