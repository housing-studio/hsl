package org.hsl.parser;

import org.hsl.compiler.ast.Node;
import org.hsl.compiler.ast.impl.declaration.Method;
import org.hsl.compiler.ast.impl.local.LocalDeclare;
import org.hsl.compiler.ast.impl.local.LocalDeclareAssign;
import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.ast.impl.value.ConstantLiteral;
import org.hsl.compiler.parser.AstParser;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MethodParseTest {
    @Test
    public void testDeclare() {
        String source =
            """
            fn foo() {
                stat player kills: int
            }
            """;

        AstParser parser = Parsers.of(source);
        Method method = assertDoesNotThrow(parser::nextMethod);

        assertEquals(1, method.scope().statements().size());
        Node node = method.scope().statements().getFirst();

        LocalDeclare local = assertInstanceOf(LocalDeclare.class, node);
        assertEquals("kills", local.name());

        assertEquals(Type.INT, local.type());
    }

    @Test
    public void testInfer() {
        String source =
            """
            fn foo() {
                stat player msg = "hello"
            }
            """;

        AstParser parser = Parsers.of(source);
        Method method = assertDoesNotThrow(parser::nextMethod);

        assertEquals(1, method.scope().statements().size());
        Node node = method.scope().statements().getFirst();

        LocalDeclareAssign local = assertInstanceOf(LocalDeclareAssign.class, node);
        assertEquals("msg", local.name());

        assertEquals(Type.STRING, local.type());
        ConstantLiteral literal = assertInstanceOf(ConstantLiteral.class, local.value());

        assertEquals("hello", literal.value().value());
    }

    @Test
    public void testDeclareAssign() {
        String source =
            """
            fn foo() {
                stat player health: int = 123
            }
            """;

        AstParser parser = Parsers.of(source);
        Method method = assertDoesNotThrow(parser::nextMethod);

        assertEquals(1, method.scope().statements().size());
        Node node = method.scope().statements().getFirst();

        LocalDeclareAssign local = assertInstanceOf(LocalDeclareAssign.class, node);
        assertEquals("health", local.name());

        assertEquals(Type.INT, local.type());
        ConstantLiteral literal = assertInstanceOf(ConstantLiteral.class, local.value());

        assertEquals("123", literal.value().value());
    }
}
