package com.raedghazal.thegametask.models;

import java.util.ArrayList;
import java.util.HashMap;

public class SnakeLadder {
    public enum Type {
        SNAKE, LADDER
    }

    private int from;
    private int to;
    private Type type;

    public SnakeLadder(int from, int to, Type type) {
        this.from = from;
        this.to = to;
        this.type = type;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public Type getType() {
        return type;
    }

    public static HashMap<Integer,Integer> toHashMap(ArrayList<SnakeLadder> snakeLadders)
    {
        HashMap<Integer,Integer> hashMap = new HashMap<>();
        for(SnakeLadder snakeLadder : snakeLadders)
        {
            hashMap.put(snakeLadder.getFrom(),snakeLadder.getTo());
        }
        return hashMap;
    }
}
