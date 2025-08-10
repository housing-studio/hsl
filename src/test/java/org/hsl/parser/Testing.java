package org.hsl.parser;

import org.hsl.compiler.ast.Game;
import org.hsl.compiler.ast.Node;
import org.hsl.compiler.ast.impl.declaration.ConstantDeclare;
import org.hsl.compiler.ast.impl.declaration.Method;
import org.hsl.compiler.parser.AstParser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class Testing {
    public static void main(String[] args) {
        String source =
            """
            const X = 100
            const Y = 200
            const Z = 300

            const ERROR = 10

            const SPAWN_LOC = Location::Custom(X + ERROR, Y - ERROR, Z - ERROR)

            fn foo() {
                stat player msg = "hello"
                stat global kills: int

                msg = "foo"
                foo(123, param1=abc, param2=1)
            }
            """;

        AstParser parser = Parsers.of(source);

        Game game = new Game();
        Node.game(game);
        assertDoesNotThrow(() -> parser.parseGame(game));

        for (ConstantDeclare constant : game.constants().values())
            System.out.println(constant.print());

        for (Method method : game.methods().values())
            System.out.println(method.print());

        String spawnLoc = game.constants().get("SPAWN_LOC").value().asConstantValue();
        System.out.println(spawnLoc);
    }
}
