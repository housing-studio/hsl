package org.hsl.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hsl.compiler.ast.Game;
import org.hsl.compiler.ast.Node;
import org.hsl.compiler.ast.hierarchy.NodeVisitor;
import org.hsl.compiler.ast.impl.declaration.ConstantDeclare;
import org.hsl.compiler.ast.impl.declaration.Method;
import org.hsl.compiler.debug.Format;
import org.hsl.compiler.parser.AstParser;
import org.hsl.export.Exporter;
import org.hsl.export.House;
import org.hsl.export.action.Action;
import org.hsl.export.generic.Function;

import static org.junit.jupiter.api.Assertions.*;

public class Testing {
    public static void main(String[] args) {
        String source =
            """
            fn foo() {
                stat player msg = "hello"
                msg = "foo"

                stat global asd: string = "Hello, World!"
            }
            """;

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

        Method method = game.methods().values().stream().findFirst().orElseThrow();
        Function build = method.build();

        for (Action action : build.actions())
            System.out.println(action);

        House export = Exporter.export(game);
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(export);
        System.out.println(Format.WHITE);
        System.out.println(json);
    }
}
