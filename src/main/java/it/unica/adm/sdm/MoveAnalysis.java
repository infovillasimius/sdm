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
import static it.unica.adm.sdm.Glossary.OPERIOD;
import static it.unica.adm.sdm.Glossary.PERIOD;
import static it.unica.adm.sdm.Glossary.PPERIOD;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author anto
 */
public class MoveAnalysis {

    private final ArrayList<MyPojo> before;
    private final ArrayList<MyPojo> after;
    private final MyPojo movement;
    private final double tempBefore;
    private final double tempAfter;
    private boolean significative;
    private final ArrayList<Double> tempsAfter;

    public MoveAnalysis(MyPojo movement) throws UnknownHostException, ExecutionException, InterruptedException {
        this.movement = movement;
        long time = Long.parseLong(movement.timestamp);

        before = Loader.find((time - PPERIOD) + "", time + "");
        
        after = Loader.find(time + "", (time + PERIOD) + "");
        
        tempBefore = Utilities.meanTemp(before);
        tempAfter = Utilities.meanTemp(after);
        //System.out.println("before = " + before.size()+ "temp=" + tempBefore);
        significative = (!before.isEmpty() && !after.isEmpty());
        //System.out.println(significative);
        tempsAfter = tempsAfterCalc();

    }

    public ArrayList<MyPojo> getBefore() {
        return before;
    }

    public ArrayList<MyPojo> getAfter() {
        return after;
    }

    public MyPojo getMovement() {
        return movement;
    }

    public double getTempBefore() {
        return tempBefore;
    }

    public double getTempAfter() {
        return tempAfter;
    }

    public boolean isSignificative() {
        return significative;
    }

    @Override
    public String toString() {
        return "MoveAnalysis{before = " + before.size() + ", after = " + after.size() + ", tempBefore=" + tempBefore + ", tempAfter=" + tempAfter + ", significative=" + significative + "}\n";
    }

    private ArrayList<Double> tempsAfterCalc() throws UnknownHostException, ExecutionException, InterruptedException {
        ArrayList<Double> list = new ArrayList<>();
        
        if(!significative){
            return list;
        }
        
        ArrayList<MyPojo> localAfter;
        ArrayList<MyPojo> after = Loader.find(movement.time + "", (movement.time + OPERIOD) + "");
        
        if(after.get(after.size()-1).time-movement.time<OPERIOD/2){
            this.significative=false;
            return list;
        }
        
        long time = movement.time;
        for (int x = 0; x < OPERIOD / NPERIOD; x++) {
            localAfter = new ArrayList<>();
            
            for (MyPojo m: after){
                if (m.time>(time + x * NPERIOD) && m.time<(time + (x + 1) * OPERIOD)){
                    localAfter.add(m);
                }
            }
            
            
            if (!localAfter.isEmpty()) {
                double temp = Utilities.meanTemp(localAfter);
                //System.out.println(x + " - " + temp);
                list.add(temp);
                if (temp <= tempBefore) {
                    return list;
                }
            } else {
                list.add(-273.25);
            }

        }
        /*if(list.size()>=OPERIOD / NPERIOD){
        this.significative=false;
        System.out.println("List size = "+list.size()+" - minuti osservati "+OPERIOD / NPERIOD);
        System.out.println("Temp before= "+tempBefore+" Observed temps "+list);
        
        }*/
        return list;
    }

    public ArrayList<Double> getTempsAfter() {
        return tempsAfter;
    }

}
