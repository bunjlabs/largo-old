package com.bunjlabs.largo.compiler.parser.nodes;

import java.util.Arrays;

public class Node extends BaseNode {
    public static final int MAX_CHILDS = 4;
    private Node[] childs;
    private OperatorType op;

    private int argument = -1;
    private String string;

    private int lineNumber;
    private int columnNumber;

    public Node(NodeType type, Node... childs) {
        super(type);
        this.childs = Arrays.copyOf(childs, MAX_CHILDS);
    }

    public Node(NodeType type, OperatorType op, Node... childs) {
        super(type);
        this.op = op;
        this.childs = Arrays.copyOf(childs, MAX_CHILDS);
    }

    public int getChildCount() {
        return childs.length;
    }

    public Node getChild(int index) {
        if (index >= 0 && index < childs.length) {
            return childs[index];
        } else {
            return null;
        }
    }

    public void setChild(int index, Node child) {
        if (index >= 0 && index < childs.length) {
            childs[index] = child;
        }
    }

    public OperatorType getOperator() {
        return op;
    }

    public int getArgument() {
        return argument;
    }

    public void setArgument(int argument) {
        this.argument = argument;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
