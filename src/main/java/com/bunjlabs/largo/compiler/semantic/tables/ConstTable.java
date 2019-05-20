package com.bunjlabs.largo.compiler.semantic.tables;

import com.bunjlabs.largo.types.LargoValue;

import java.util.HashMap;
import java.util.Map;

public class ConstTable {

    private final Map<LargoValue, Integer> table = new HashMap<>();

    private int lastId = 0;

    public int getId(LargoValue value) {
        if (table.containsKey(value)) {
            return table.get(value);
        } else {
            int id = lastId++;
            table.put(value, id);
            return id;
        }
    }


    public LargoValue[] buildConstPool() {
        LargoValue[] constPool = new LargoValue[table.size()];
        table.forEach((value, index) -> constPool[index] = value);
        return constPool;
    }

}
