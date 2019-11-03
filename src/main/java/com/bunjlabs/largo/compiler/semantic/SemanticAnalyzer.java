package com.bunjlabs.largo.compiler.semantic;

import com.bunjlabs.largo.compiler.SemanticException;
import com.bunjlabs.largo.compiler.parser.Parser;
import com.bunjlabs.largo.compiler.parser.nodes.Node;
import com.bunjlabs.largo.compiler.parser.nodes.NodeType;
import com.bunjlabs.largo.compiler.parser.nodes.OperatorType;
import com.bunjlabs.largo.compiler.semantic.tables.BlueprintTable;
import com.bunjlabs.largo.compiler.semantic.tables.ConstTable;
import com.bunjlabs.largo.compiler.semantic.tables.IdTable;
import com.bunjlabs.largo.types.LargoNumber;
import com.bunjlabs.largo.types.LargoString;

import java.util.ArrayList;
import java.util.List;

import static com.bunjlabs.largo.compiler.Utils.*;
import static com.bunjlabs.largo.compiler.Utils.checkNodeType;
import static com.bunjlabs.largo.compiler.parser.nodes.NodeType.*;
import static com.bunjlabs.largo.compiler.parser.nodes.OperatorType.*;

public class SemanticAnalyzer {

    private static final NodeType[] EXPRESSION_TYPES = new NodeType[]{
            ND_UNOP_EXPR, ND_BINOP_EXPR, ND_FUNC_DEF, ND_CALL,
            ND_INDEX_SEL, ND_INDEX_SET, ND_FIELD_SEL, ND_FIELD_SET,
            ND_ID_LOCAL, ND_ID_OUTER, ND_ARRAY, ND_OBJECT,
            ND_NUMBER, ND_BOOLEAN, ND_STRING, ND_NULL, ND_UNDEFINED};

    private static final NodeType[] STATEMENT_TYPES = new NodeType[]{
            ND_STMT_LIST, ND_VARINIT_STMT, ND_IMPORT, ND_IF_STMT,
            ND_IFELSE_STMT, ND_WHILE_STMT, ND_FOR_STMT, ND_EXPR_STMT,
            ND_BREAK, ND_CONTINUE, ND_RETURN};

    private static final OperatorType[] ASSIGN_OPERATOR_TYPES = new OperatorType[]{
            OP_ASSIGN, OP_PLUSEQ, OP_MINUSEQ, OP_MULTEQ,
            OP_DIVEQ, OP_MODEQ};

    private static final OperatorType[] NON_ASSIGN_OPERATOR_TYPES = new OperatorType[]{
            OP_PLUS, OP_MINUS, OP_MULT, OP_DIV,
            OP_MOD, OP_LAND, OP_LOR, OP_LNOT, OP_EQ,
            OP_NOTEQ, OP_GREAT, OP_GREATEQ, OP_LESS,
            OP_LESSEQ};

    private final ConstTable constTable;
    private final IdTable localIdTable;
    private final IdTable outerIdTable;
    private final BlueprintTable blueprintTable;
    private final int argumentsCount;
    private final Node root;
    private int nestLevel = 0;

    public SemanticAnalyzer(Node root) {
        this.root = root;
        this.constTable = new ConstTable();
        this.localIdTable = new IdTable();
        this.outerIdTable = new IdTable();
        this.blueprintTable = new BlueprintTable();
        this.argumentsCount = 0;
    }

    public SemanticAnalyzer(Node root, List<String> funcArguments, IdTable outerIdTable) {
        this.root = root;
        this.constTable = new ConstTable();
        this.localIdTable = new IdTable(funcArguments);
        this.outerIdTable = outerIdTable;
        this.blueprintTable = new BlueprintTable();
        this.argumentsCount = funcArguments.size();
    }

    private void analyzeId(Node node) throws SemanticException {
        int localId = localIdTable.getId(node.getString());
        if (localId >= 0) {
            node.setArgument(localId);
            return;
        }

        int outerId = outerIdTable.getId(node.getString());
        if (outerId >= 0) {
            node.setType(ND_ID_OUTER);
            node.setArgument(outerId);
            return;
        }

        throw new SemanticException(node, "Undefined variable: " + node.getString());
    }

    private void analyzeArray(Node node) throws SemanticException {
        if (!checkNodeChildsForNull(node, 1)) {
            throw semanticException(node, "ArrayExpression must have one child for ExpressionList, Expression or Empty");
        }

        Node expr = node.getChild(0);
        if (!checkNodeType(expr, ND_EXPR_LIST) && !checkNodeType(expr, EXPRESSION_TYPES) && !checkNodeType(expr, ND_EMPTY)) {
            throw semanticException(node, "ArrayExpression first child must be ExpressionList, Expression or Empty");
        }

        if (expr.getType() != ND_EMPTY) {
            analyzeExpressionList(expr);
        }
    }

    private void analyzeObject(Node node) throws SemanticException {
        // nothing here
    }

    private void analyzeExpressionList(Node node) throws SemanticException {
        if (node.getType() != ND_EXPR_LIST) {
            analyzeExpression(node);
            return;
        }

        if (!checkNodeChildsForNull(node, 2)) {
            throw semanticException(node, "ExpressionList must have two childs for Expression and ExpressionList or one child for Expression");
        }

        Node curr = node;
        while (curr.getType() != ND_EMPTY) {
            Node expr = curr.getChild(0);

            if (!checkNodeType(expr, EXPRESSION_TYPES)) {
                throw semanticException(node, "First child in ExpressionList must be Expression");
            }

            analyzeExpression(expr);

            curr = curr.getChild(1);
        }
    }

    private void analyzeUnaryOperatorExpression(Node node) throws SemanticException {
        if (!checkNodeChildsForNull(node, 1)) {
            throw semanticException(node, "UnaryOperatorExpression must have one child for Expression");
        }

        Node expr = node.getChild(0);

        if (checkNodeOperator(node, OP_DPREPLUS, OP_DPREMINUS, OP_DPOSTPLUS, OP_DPOSTMINUS)) {
            if (!checkNodeType(expr, ND_ID_LOCAL)) {
                throw semanticException(node, "pre increment operator must have local identificator at right side");
            }

            analyzeId(expr);
        } else if (checkNodeOperator(node, OP_LNOT, OP_MINUS, OP_PLUS)) {
            analyzeExpression(expr);
        } else {
            throw semanticException(node, "Unknown unary operator");
        }
    }

    private void analyzeBinaryOperatorExpression(Node node) throws SemanticException {
        if (!checkNodeChildsForNull(node, 2)) {
            throw semanticException(node, "BinaryOperatorExpression must have two childs for left and right Expression");
        }

        Node leftExpr = node.getChild(0);
        Node rightExpr = node.getChild(1);

        if (checkNodeOperator(node, ASSIGN_OPERATOR_TYPES)) {
            if (!checkNodeType(leftExpr, ND_ID_LOCAL)) {
                throw semanticException(node, "assign operator must have local identificator at left side");
            }

            analyzeId(leftExpr);

            if (!checkNodeType(rightExpr, EXPRESSION_TYPES)) {
                throw semanticException(node, "Second operator must be an Expression");
            }

            analyzeExpression(rightExpr);

        } else if (checkNodeOperator(node, NON_ASSIGN_OPERATOR_TYPES)) {
            if (!checkNodeType(leftExpr, EXPRESSION_TYPES)) {
                throw semanticException(node, "Second operator must be an Expression");
            }
            if (!checkNodeType(rightExpr, EXPRESSION_TYPES)) {
                throw semanticException(node, "Second operator must be an Expression");
            }

            analyzeExpression(leftExpr);
            analyzeExpression(rightExpr);
        } else {
            throw semanticException(node, "Unknown binary operator");
        }
    }

    private void analyzeFunctionDefinitionIds(Node node) throws SemanticException {
        if (checkNodeType(node, ND_ID_LOCAL, ND_EMPTY)) {
            return;
        }

        if (!checkNodeChildsForNull(node, 2)) {
            throw semanticException(node, "FuncDefIds must have two childs for Id and FuncDefIds");
        }

        Node curr = node;
        while (curr.getType() != ND_EMPTY) {
            Node expr = curr.getChild(0);

            if (!checkNodeType(expr, ND_ID_LOCAL)) {
                throw semanticException(node, "First child in FuncDefIds must be Id");
            }

            curr = curr.getChild(1);
        }
    }

    private void analyzeFunctionDefinition(Node node) throws SemanticException {
        if (!checkNodeChildsForNull(node, 2)) {
            throw semanticException(node, "FunctionDefinition must have two childs for Identificators and Statement");
        }

        Node args = node.getChild(0);
        Node body = node.getChild(1);

        if (!checkNodeType(args, ND_EXPR_LIST, ND_ID_LOCAL, ND_EMPTY)) {
            throw semanticException(node, "FunctionDefinition first child must be ExpressionList, Id or Empty");
        }

        if (!checkNodeType(body, STATEMENT_TYPES)) {
            throw semanticException(node, "FunctionDefinition body must be an Statement");
        }

        analyzeFunctionDefinitionIds(args);

        List<String> funcArguments = new ArrayList<>();

        if (args.getType() == ND_ID_LOCAL) {
            funcArguments.add(args.getString());
        } else {
            Node curArg = args;
            while (curArg.getType() != ND_EMPTY) {
                funcArguments.add(curArg.getChild(0).getString());
                curArg = curArg.getChild(1);
            }
        }

        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(body, funcArguments, localIdTable);
        SemanticInfo semanticInfo = semanticAnalyzer.analyze();

        node.setArgument(blueprintTable.getId(semanticInfo));
    }

    private void analyzeFunctionCall(Node node) throws SemanticException {
        if (!checkNodeChildsForNull(node, 2)) {
            throw semanticException(node, "FuncCall statement must have two childs for Expression and FuncArg");
        }

        Node expr = node.getChild(0);
        Node args = node.getChild(1);

        if (!checkNodeType(expr, EXPRESSION_TYPES)) {
            throw semanticException(node, "FuncCall id must be Id");
        }
        if (!checkNodeType(args, EXPRESSION_TYPES) && !checkNodeType(args, ND_EXPR_LIST, ND_EMPTY)) {
            throw semanticException(node, "FuncCall args must be Expression, ExpressionList or Empty");
        }

        analyzeExpression(expr);
        if (args.getType() != ND_EMPTY) {
            analyzeExpressionList(args);
        }
    }

    private void analyzeIndexSelect(Node node) throws SemanticException {
        if (!checkNodeChildsForNull(node, 2)) {
            throw semanticException(node, "FieldSelect must have two childs for left expression and index expression");
        }

        Node expr = node.getChild(0);
        Node index = node.getChild(1);

        if (!checkNodeType(expr, EXPRESSION_TYPES)) {
            throw semanticException(node, "FieldSelect first child must be Expression");
        }

        if (!checkNodeType(index, EXPRESSION_TYPES)) {
            throw semanticException(node, "FieldSelect second child must be Expression");
        }

        analyzeExpression(expr);
        analyzeExpression(index);
    }

    private void analyzeIndexSet(Node node) throws SemanticException {
        if (!checkNodeChildsForNull(node, 3)) {
            throw semanticException(node, "IndexSet must have three childs for left expression, index expression and right expression");
        }

        Node leftExpr = node.getChild(0);
        Node index = node.getChild(1);
        Node rightExpr = node.getChild(2);

        if (!checkNodeType(leftExpr, EXPRESSION_TYPES)) {
            throw semanticException(node, "IndexSet first child must be Expression");
        }

        if (!checkNodeType(index, EXPRESSION_TYPES)) {
            throw semanticException(node, "IndexSet second child must be Expression");
        }

        if (!checkNodeType(rightExpr, EXPRESSION_TYPES)) {
            throw semanticException(node, "IndexSes third child must be Expression");
        }

        analyzeExpression(leftExpr);
        analyzeExpression(index);
        analyzeExpression(rightExpr);
    }

    private void analyzeFieldSelect(Node node) throws SemanticException {
        if (!checkNodeChildsForNull(node, 2)) {
            throw semanticException(node, "FieldSelect must have two childs for Expression and Id");
        }

        Node expr = node.getChild(0);
        Node id = node.getChild(1);

        if (!checkNodeType(expr, EXPRESSION_TYPES)) {
            throw semanticException(node, "FieldSelect first child must be Expression");
        }

        if (!checkNodeType(id, ND_ID_LOCAL)) {
            throw semanticException(node, "FieldSelect second child must be Id");
        }

        analyzeExpression(expr);
        id.setArgument(constTable.getId(LargoString.from(id.getString())));
    }

    private void analyzeFieldSet(Node node) throws SemanticException {
        if (!checkNodeChildsForNull(node, 3)) {
            throw semanticException(node, "FieldSet must have three childs for left expression, index expression and right expression");
        }

        Node leftExpr = node.getChild(0);
        Node id = node.getChild(1);
        Node rightExpr = node.getChild(2);

        if (!checkNodeType(leftExpr, EXPRESSION_TYPES)) {
            throw semanticException(node, "FieldSet first child must be Expression");
        }

        if (!checkNodeType(id, ND_ID_LOCAL)) {
            throw semanticException(node, "FieldSet second child must be Id");
        }

        if (!checkNodeType(rightExpr, EXPRESSION_TYPES)) {
            throw semanticException(node, "FieldSet third child must be Expression");
        }

        analyzeExpression(leftExpr);
        id.setArgument(constTable.getId(LargoString.from(id.getString())));
        analyzeExpression(rightExpr);
    }

    private void analyzeExpression(Node node) throws SemanticException {
        switch (node.getType()) {
            case ND_UNOP_EXPR:
                analyzeUnaryOperatorExpression(node);
                return;
            case ND_BINOP_EXPR:
                analyzeBinaryOperatorExpression(node);
                return;
            case ND_FUNC_DEF:
                analyzeFunctionDefinition(node);
                return;
            case ND_CALL:
                analyzeFunctionCall(node);
                return;
            case ND_INDEX_SEL:
                analyzeIndexSelect(node);
                return;
            case ND_INDEX_SET:
                analyzeIndexSet(node);
                return;
            case ND_FIELD_SEL:
                analyzeFieldSelect(node);
                return;
            case ND_FIELD_SET:
                analyzeFieldSet(node);
                return;
            case ND_ID_LOCAL:
                analyzeId(node);
                return;
            case ND_ARRAY:
                analyzeArray(node);
                return;
            case ND_OBJECT:
                analyzeObject(node);
                return;
            case ND_NUMBER:
                LargoNumber numberValue = LargoNumber.from(Double.parseDouble(node.getString()));
                node.setArgument(constTable.getId(numberValue));
                return;
            case ND_STRING:
                LargoString stringValue = LargoString.from(node.getString());
                node.setArgument(constTable.getId(stringValue));
                return;
            case ND_BOOLEAN:
                node.setArgument(node.getString().equals("true") ? 1 : 0);
                return;
            case ND_NULL:
            case ND_UNDEFINED:
                return;
            default:
                throw unexpectedNodeException(node);
        }
    }

    private void analyzeExpressionStatement(Node node) throws SemanticException {
        if (!checkNodeChildsForNull(node, 2)) {
            throw semanticException(node, "ExpressionStatement must have two childs for Expression and ExpressionStatement");
        }

        Node curr = node;

        while (curr.getType() != ND_EMPTY) {
            Node expr = curr.getChild(0);

            if (!checkNodeType(expr, EXPRESSION_TYPES)) {
                throw semanticException(node, "First child in ExpressionList must be Expression");
            }

            analyzeExpression(expr);

            curr = curr.getChild(1);
        }
    }


    private void analyzeFor(Node node) throws SemanticException {
        if (!checkNodeChildsForNull(node, 4)) {
            throw semanticException(node, "While statement must have two childs for condition and body");
        }

        Node preExpr = node.getChild(0);
        Node cond = node.getChild(1);
        Node postExpre = node.getChild(2);
        Node body = node.getChild(3);

        if (!checkNodeType(preExpr, ND_EXPR_STMT, ND_VARINIT_STMT, ND_EMPTY)) {
            throw semanticException(node, "For pre expression must be ExpressionStatement, VarInit or Empty");
        }

        if (!checkNodeType(cond, EXPRESSION_TYPES) && !checkNodeType(cond, ND_EMPTY)) {
            throw semanticException(node, "For condition expression must be Expression or Empty");
        }

        if (!checkNodeType(postExpre, ND_EXPR_STMT) && !checkNodeType(postExpre, ND_EMPTY)) {
            throw semanticException(node, "For post expression must be ExpressionStatement or Empty");
        }

        if (!checkNodeType(body, STATEMENT_TYPES)) {
            throw semanticException(node, "For body must be an Statement or StatementLise");
        }

        if (checkNodeType(preExpr, ND_VARINIT_STMT)) {
            analyzeVarInit(preExpr);
        } else if (!checkNodeType(preExpr, ND_EMPTY)) {
            analyzeExpressionStatement(preExpr);
        }

        if (cond.getType() != ND_EMPTY) analyzeExpression(cond);
        if (postExpre.getType() != ND_EMPTY) analyzeExpressionStatement(postExpre);

        analyzeStatement(body);
    }

    private void analyzeWhile(Node node) throws SemanticException {
        if (!checkNodeChildsForNull(node, 2)) {
            throw semanticException(node, "While statement must have two childs for condition and body");
        }

        Node expr = node.getChild(0);
        Node body = node.getChild(1);

        if (!checkNodeType(expr, EXPRESSION_TYPES)) {
            throw semanticException(node, "While condition must be an Expression");
        }

        if (!checkNodeType(body, STATEMENT_TYPES)) {
            throw semanticException(node, "While body must be an Statement or StatementLise");
        }

        analyzeExpression(expr);
        analyzeStatement(body);
    }

    private void analyzeIf(Node node) throws SemanticException {
        if (!checkNodeChildsForNull(node, 2)) {
            throw semanticException(node, "If statement must have two childs for condition and body");
        }
        Node expr = node.getChild(0);
        Node body = node.getChild(1);

        if (!checkNodeType(expr, EXPRESSION_TYPES)) {
            throw semanticException(node, "If condition must be an Expression");
        }

        if (!checkNodeType(body, STATEMENT_TYPES)) {
            throw semanticException(node, "If body must be an Statement or StatementLise");
        }

        analyzeExpression(expr);
        analyzeStatement(body);
    }

    private void analyzeIfElse(Node node) throws SemanticException {
        if (!checkNodeChildsForNull(node, 3)) {
            throw semanticException(node, "IfElse statement must have three childs for condition, body and else body");
        }

        Node expr = node.getChild(0);
        Node body = node.getChild(1);
        Node bodyElse = node.getChild(2);

        if (!checkNodeType(expr, EXPRESSION_TYPES)) {
            throw semanticException(node, "IfElse condition must be an Expression");
        }

        if (!checkNodeType(body, STATEMENT_TYPES)) {
            throw semanticException(node, "IfElse body must be an Statement or StatementLise");
        }

        if (!checkNodeType(bodyElse, STATEMENT_TYPES)) {
            throw semanticException(node, "IfElse else body must be an Statement or StatementLise");
        }

        analyzeExpression(expr);
        analyzeStatement(body);
        analyzeStatement(bodyElse);
    }

    private void analyzeVarInit(Node node) throws SemanticException {
        int id = localIdTable.createId(node.getString());

        if (id < 0) {
            throw new SemanticException(node, "Variable already defined: " + node.getString());
        }

        node.setArgument(id);

        if (!checkNodeChildsForNull(node, 2)) {
            throw semanticException(node, "VariableInit statement must have two childs as Expression and VarInit");
        }

        Node expr = node.getChild(0);
        Node nextInit = node.getChild(1);

        if (!checkNodeType(expr, EXPRESSION_TYPES) && !checkNodeType(expr, ND_EMPTY)) {
            throw semanticException(node, "VariableInit first child must be an Expression or Empty");
        }

        if (!checkNodeType(nextInit, ND_VARINIT_STMT, ND_EMPTY)) {
            throw semanticException(node, "VarInit second child must be an VarInit or Empty");
        }

        if (expr.getType() != ND_EMPTY) analyzeExpression(expr);
        if (nextInit.getType() != ND_EMPTY) analyzeVarInit(nextInit);
    }

    private void analyzeReturn(Node node) throws SemanticException {
        if (!checkNodeChildsForNull(node, 1)) {
            throw semanticException(node, "Import statement must have one child for Exression or Empty");
        }

        Node expr = node.getChild(0);

        if (!checkNodeType(expr, EXPRESSION_TYPES) && checkNodeType(expr, ND_EMPTY)) {
            throw semanticException(node, "VariableInit first child must be an Expression or Empty");
        }

        if (expr.getType() != ND_EMPTY) analyzeExpression(expr);
    }

    private void analyzeImport(Node node) throws SemanticException {
        if (!checkNodeChildsForNull(node, 1)) {
            throw semanticException(node, "Import statement must have one child for Id");
        }

        Node id = node.getChild(0);

        if (!checkNodeType(id, ND_ID_LOCAL)) {
            throw semanticException(node, "Import child must be an Id");
        }

        node.setArgument(constTable.getId(LargoString.from(node.getString())));
        id.setArgument(localIdTable.createId(id.getString()));
    }

    private void analyzeStatementList(Node node) throws SemanticException {
        nestLevel++;
        localIdTable.push();
        Node curr = node;
        while (!checkNodeType(curr, ND_EMPTY)) {
            Node leftStmt = curr.getChild(0);
            if (!checkNodeType(leftStmt, STATEMENT_TYPES)) {
                throw semanticException(node, "First child of StatementList must be an Statement");
            }

            analyzeStatement(leftStmt);

            Node rightStmt = curr.getChild(1);
            if (!checkNodeType(rightStmt, ND_STMT_LIST, ND_EMPTY)) {
                throw semanticException(node, "Second child of StatementList must be an StatementList or Empty");
            }

            curr = rightStmt;
        }
        nestLevel--;
        localIdTable.pop();
    }

    private void analyzeStatement(Node node) throws SemanticException {
        switch (node.getType()) {
            case ND_STMT_LIST:
                analyzeStatementList(node);
                return;
            case ND_EMPTY:
            case ND_BREAK:
            case ND_CONTINUE:
                return;
            case ND_IMPORT:
                analyzeImport(node);
                return;
            case ND_VARINIT_STMT:
                analyzeVarInit(node);
                return;
            case ND_IF_STMT:
                analyzeIf(node);
                return;
            case ND_IFELSE_STMT:
                analyzeIfElse(node);
                return;
            case ND_WHILE_STMT:
                analyzeWhile(node);
                return;
            case ND_FOR_STMT:
                analyzeFor(node);
                return;
            case ND_CALL:
                analyzeFunctionCall(node);
                return;
            case ND_RETURN:
                analyzeReturn(node);
                return;
            case ND_EXPR_STMT:
                analyzeExpressionStatement(node);
                return;
            default:
                throw unexpectedNodeException(node);
        }
    }


    public SemanticInfo analyze() throws SemanticException {
        if (!checkNodeType(root, STATEMENT_TYPES)) {
            throw semanticException(root, "Root must be Statement");
        }

        analyzeStatement(root);

        return new SemanticInfo(root, constTable.buildConstPool(), blueprintTable.buildFunctionsTable(), argumentsCount, localIdTable.getVariableCount());
    }
}
