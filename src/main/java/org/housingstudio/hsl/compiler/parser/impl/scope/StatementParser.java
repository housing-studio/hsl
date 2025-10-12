package org.housingstudio.hsl.compiler.parser.impl.scope;

import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.control.Random;
import org.housingstudio.hsl.compiler.ast.impl.control.Return;
import org.housingstudio.hsl.compiler.ast.impl.control.ReturnValue;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.scope.Statement;
import org.housingstudio.hsl.compiler.ast.impl.value.Argument;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.ast.impl.value.MethodCall;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a parser algorithm that parses a statement node from the token stream.
 *
 * @see Statement
 */
public class StatementParser extends ParserAlgorithm<Node> {
    /**
     * Parse the next {@link Statement} node from the token stream.
     *
     * @param parser  the AST node parser
     * @param context the token parser context
     * @return the next {@link Statement} node from the token stream
     */
    @Override
    public @NotNull Node parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        // handle local variable declaration
        if (peek().is(TokenType.EXPRESSION, "stat"))
            return (Node) parser.nextLocalDeclaration();

        // handle variable assignment
        // foo = 123
        //     ^ the `=` symbol after an identifier indicates, that a variable is assigned
        //      ^ the `=` symbol must not follow another `=`, as that would be a comparing binary operator
        // ^^^ the identifier must be followed by a `=`
        else if (
            peek().is(TokenType.IDENTIFIER) && at(cursor() + 1).is(TokenType.OPERATOR, "=") &&
            !at(cursor() + 2).is(TokenType.OPERATOR, "=")
        )
            return parser.nextLocalAssignment();

        // handle prefix operators

        // handle postfix operators
        // foo++
        // ^^^ target operand
        //    ^^ postfix operator
        else if (
            peek().is(TokenType.IDENTIFIER) && (
                (at(cursor() + 1).is(TokenType.OPERATOR, "+") && at(cursor() + 2).is(TokenType.OPERATOR, "+")) ||
                (at(cursor() + 1).is(TokenType.OPERATOR, "-") && at(cursor() + 2).is(TokenType.OPERATOR, "-"))
            )
        )
            return parser.nextPostfixOperator();

        // handle operator assignment
        // foo+=2
        // ^^^ lhs target operand
        //    ^^ operator
        //      ^ rhs operand value
        else if (
            peek().is(TokenType.IDENTIFIER) && (
                isAssigmentOperator(at(cursor() + 1)) && at(cursor() + 2).is(TokenType.OPERATOR, "=")
            )
        )
            return parser.nextAssignmentOperator();

        // handle method call
        // chat("Hello, World!")
        //     ^ the parentheses after an identifier indicates, that a method is being called
        else if (peek().is(TokenType.IDENTIFIER) && at(cursor() + 1).is(TokenType.LPAREN)) {
            Token method = get();
            List<Argument> arguments = parser.nextArgumentList();
            return new MethodCall(method, arguments);
        }

        // handle macro call
        // println!("Hello, World!")
        else if (peek().is(TokenType.IDENTIFIER) && at(cursor() + 1).is(TokenType.OPERATOR, "!"))
            return parser.nextMacroCall();

        // handle conditional
        else if (peek().is(TokenType.EXPRESSION, "if"))
            return parser.nextConditional();

        // handle return
        else if (peek().is(TokenType.EXPRESSION, "return")) {
            get();
            if (peek().is(TokenType.SEMICOLON)) {
                get();
                return new Return();
            }
            Value value = parser.nextValue();
            if (peek().is(TokenType.SEMICOLON))
                get();
            return new ReturnValue(value);
        }

        // handle random action
        else if (peek().is(TokenType.EXPRESSION, "random")) {
            get(TokenType.EXPRESSION, "random");
            Scope scope = parser.nextScope();
            return new Random(scope);
        }

        // handle for loop
        else if (peek().is(TokenType.EXPRESSION, "for"))
            return parser.nextForLoop();

        // handle while loop
        else if (peek().is(TokenType.EXPRESSION, "while"))
            return parser.nextWhileLoop();

        context.errorPrinter().print(
            Notification.error(Errno.UNEXPECTED_TOKEN, "unexpected statement")
                .error("token " + peek().print() + " is not a statement", peek())
        );
        throw new UnsupportedOperationException("Not implemented statement: " + peek());
    }

    private boolean isAssigmentOperator(@NotNull Token token) {
        if (!token.is(TokenType.OPERATOR))
            return false;

        switch (token.value()) {
            case "+":
            case "-":
            case "*":
            case "/":
                return true;
            default:
                return false;
        }
    }
}
