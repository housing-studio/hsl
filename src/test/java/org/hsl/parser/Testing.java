package org.hsl.parser;

import com.google.gson.GsonBuilder;
import org.hsl.compiler.ast.Game;
import org.hsl.compiler.ast.Node;
import org.hsl.compiler.ast.hierarchy.NodeVisitor;
import org.hsl.compiler.ast.impl.action.BuiltinActions;
import org.hsl.compiler.ast.impl.declaration.ConstantDeclare;
import org.hsl.compiler.ast.impl.declaration.Method;
import org.hsl.compiler.debug.Format;
import org.hsl.compiler.parser.AstParser;
import org.hsl.export.Exporter;
import org.hsl.export.House;

import static org.junit.jupiter.api.Assertions.*;

public class Testing {
    public static void main(String[] args) {
        String source =
            """
            @description("hello world")
            @loop(1s)
            @icon(Material::Stone)
            fn foo() {
                changeVariable(Namespace::Player, "kills", Mode::Increment)
                chat("hello")
            }
            """;

        BuiltinActions.init();
        AstParser parser = Parsers.of(source);

        Game game = new Game();
        Node.game(game);

        assertDoesNotThrow(() -> parser.parseGame(game));
        NodeVisitor.initHierarchy();
        NodeVisitor.initLifecycle();

        for (ConstantDeclare constant : game.constants().values())
            System.out.println(constant.print());

        for (Method method : game.methods().values())
            System.out.println(method.print());

        House export = Exporter.export(game);
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(export);
        System.out.println(Format.WHITE);
        System.out.println(json);
    }
}
