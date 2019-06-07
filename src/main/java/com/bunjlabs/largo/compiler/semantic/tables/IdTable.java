package com.bunjlabs.largo.compiler.semantic.tables;

import java.util.*;

public class IdTable {

    private final Deque<VariableScope> scopeStack = new ArrayDeque<>();

    private int autoIncrementId = 0;

    public IdTable() {
        push();
    }

    public IdTable(List<String> funcArguments) {
        push();
        for (String arg : funcArguments) createId(arg);
    }

    public int getId(String name) {
        for (VariableScope scope : scopeStack) {
            if (scope.variableExists(name)) {
                return scope.getId(name);
            }
        }

        return -1;
    }

    public int createId(String name) {
        if (variableExists(name)) {
            return -1;
        }

        int id = autoIncrementId++;
        scopeStack.peek().putId(name, id);
        return id;
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
