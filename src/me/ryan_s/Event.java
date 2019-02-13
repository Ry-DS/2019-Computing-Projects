package me.ryan_s;

import java.text.ParseException;

/**
 * Created by SimplyBallistic on 13/02/2019
 *
 * @author SimplyBallistic
 **/
public class Event {
    private String name;
    private Unit unit;

    public Event(String name, Unit unit) {
        this.name = name;
        this.unit = unit;
    }
    public static Event parse(String string){
        try{
            String[] split=string.split(",");
            return new Event(split[0],Unit.valueOf(split[1].toUpperCase()));
        }catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to parse String "+string+" into event");
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public Unit getUnit() {
        return unit;
    }
}
