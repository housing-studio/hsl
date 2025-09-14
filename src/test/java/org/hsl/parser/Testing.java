package org.hsl.parser;

import com.google.gson.GsonBuilder;
import org.housingstudio.hsl.compiler.ast.Game;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.codegen.hierarchy.NodeVisitor;
import org.housingstudio.hsl.compiler.ast.impl.action.BuiltinActions;
import org.housingstudio.hsl.compiler.ast.impl.action.BuiltinConditions;
import org.housingstudio.hsl.compiler.ast.impl.declaration.ConstantDeclare;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Method;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.codegen.Exporter;
import org.housingstudio.hsl.compiler.codegen.impl.house.House;
import org.housingstudio.hsl.compiler.codegen.impl.house.Metadata;

import static org.junit.jupiter.api.Assertions.*;

public class Testing {
    public static void main(String[] args) {
        String source =
            "fn enterArea() {\n" +
            "    if (!hasPermission(Permission::Build)) {\n" +
            "        return\n" +
            "    }\n" +
            "    chat(\"hi\")\n" +
            "}\n";

        BuiltinActions.init();
        BuiltinConditions.init();
        AstParser parser = Parsers.of(source);

        Game game = new Game();
        Node.game(game);

        assertDoesNotThrow(() -> parser.parseGame(game));
        NodeVisitor.initHierarchy();
        NodeVisitor.initLifecycle();

        for (ConstantDeclare constant : game.constants().values())
            System.out.println(constant.print());

        for (Method method : game.functions().values())
            System.out.println(method.print());

        House export = Exporter.export(new Metadata(), game);
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(export);
        System.out.println(Format.WHITE);
        System.out.println(json);
    }
}
