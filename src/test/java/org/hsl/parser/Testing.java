package org.hsl.parser;

import org.hsl.compiler.ast.impl.declaration.Constant;
import org.hsl.compiler.ast.impl.declaration.Method;
import org.hsl.compiler.parser.AstParser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class Testing {
    public static void main(String[] args) {
        String source =
            """
            const TEST_VALUE = 123

            fn foo() {
                stat player msg = "hello"
                stat global kills: int

                msg = "foo"
            }
            """;

        AstParser parser = Parsers.of(source);

        Constant constant = assertDoesNotThrow(parser::nextConstant);
        System.out.println(constant.print());

        Method method = assertDoesNotThrow(parser::nextMethod);
        System.out.println(method.print());
    }
}
