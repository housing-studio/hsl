package org.hsl.parser;

import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.codegen.hierarchy.NodeVisitor;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Method;
import org.housingstudio.hsl.compiler.ast.impl.local.LocalDeclare;
import org.housingstudio.hsl.compiler.ast.impl.local.LocalDeclareAssign;
import org.housingstudio.hsl.compiler.ast.impl.type.BaseType;
import org.housingstudio.hsl.compiler.ast.impl.value.ConstantLiteral;
import org.housingstudio.hsl.compiler.parser.AstParser;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MethodParseTest {
    @Test
    public void testDeclare() {
        String source =
            "fn foo() {\n" +
            "    stat player kills: int\n" +
            "}\n";

        AstParser parser = Parsers.of(source);
        Method method = assertDoesNotThrow(parser::nextMethod);

        assertEquals(1, method.scope().statements().size());
        Node node = method.scope().statements().get(0);

        LocalDeclare local = assertInstanceOf(LocalDeclare.class, node);
        assertEquals("kills", local.name());

        assertEquals(BaseType.INT, local.type());
    }

    @Test
    public void testInfer() {
        String source =
            "fn foo() {\n" +
            "    stat player msg = \"hello\"\n" +
            "}\n";

        AstParser parser = Parsers.of(source);
        Method method = assertDoesNotThrow(parser::nextMethod);

        NodeVisitor.initHierarchy();
        NodeVisitor.initLifecycle();

        assertEquals(1, method.scope().statements().size());
        Node node = method.scope().statements().get(0);

        LocalDeclareAssign local = assertInstanceOf(LocalDeclareAssign.class, node);
        assertEquals("msg", local.name());

        assertEquals(BaseType.STRING, local.type());
        ConstantLiteral literal = assertInstanceOf(ConstantLiteral.class, local.value());

        assertEquals("hello", literal.token().value());
    }

    @Test
    public void testDeclareAssign() {
        String source =
            "fn foo() {\n" +
            "    stat player health: int = 123\n" +
            "}\n";

        AstParser parser = Parsers.of(source);
        Method method = assertDoesNotThrow(parser::nextMethod);

        NodeVisitor.initHierarchy();
        NodeVisitor.initLifecycle();

        assertEquals(1, method.scope().statements().size());
        Node node = method.scope().statements().get(0);

        LocalDeclareAssign local = assertInstanceOf(LocalDeclareAssign.class, node);
        assertEquals("health", local.name());

        assertEquals(BaseType.INT, local.type());
        ConstantLiteral literal = assertInstanceOf(ConstantLiteral.class, local.value());

        assertEquals("123", literal.token().value());
    }
}
