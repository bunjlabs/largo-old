package com.bunjlabs.largo.compiler.semantic.tables;

import com.bunjlabs.largo.compiler.semantic.SemanticInfo;

import java.util.ArrayList;
import java.util.List;

public class BlueprintTable {

    private final List<SemanticInfo> table = new ArrayList<>();

    private int lastId = 0;

    public int getId(SemanticInfo semanticInfo) {
        int id = lastId++;
        table.add(semanticInfo);
        return id;
    }

    public SemanticInfo[] buildFunctionsTable() {
        return table.toArray(new SemanticInfo[0]);
    }

}
