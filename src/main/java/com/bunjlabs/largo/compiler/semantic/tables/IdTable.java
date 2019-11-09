package com.bunjlabs.largo.compiler.semantic.tables;

import java.util.*;

public class IdTable {

    private final Deque<VariableScope> scopeStack = new ArrayDeque<>();
    private int autoIncrementId = 0;

    public IdTable() {
        push();
    }

    public ResolvedId getId(String name) {
        for (VariableScope scope : scopeStack) {
            if (scope.variableExists(name)) {
                return new ResolvedId(name, scope.getId(name));
            }
        }

        return null;
    }

    public ResolvedId createId(String name) {
        if (variableExists(name)) {
            return null;
        }

        int id = autoIncrementId++;
        scopeStack.peek().putId(name, id);
        return new ResolvedId(name, id);
    }


    public void push() {
        scopeStack.push(new VariableScope());
    }

    public void pop() {
        scopeStack.pop();
    }

    public int getVariableCount() {
        return autoIncrementId;
    }

    private boolean variableExists(String name) {
        for (VariableScope scope : scopeStack) {
            if (scope.variableExists(name)) {
                return true;
            }
        }
        return false;
    }

    public static class ResolvedId {
        public final String name;
        public final int id;

        ResolvedId(String name, int id) {
            this.name = name;
            this.id = id;
        }
    }

    private static class VariableScope {
        private final Map<String, Integer> map = new HashMap<>();

        int getId(String name) {
            return map.get(name);
        }

        void putId(String name, int id) {
            map.put(name, id);
        }

        boolean variableExists(String name) {
            return map.containsKey(name);
        }
    }
}
