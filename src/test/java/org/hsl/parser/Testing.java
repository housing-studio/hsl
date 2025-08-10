package org.hsl.parser;

import org.hsl.compiler.ast.Game;
import org.hsl.compiler.ast.Node;
import org.hsl.compiler.ast.impl.declaration.Constant;
import org.hsl.compiler.ast.impl.declaration.Method;
import org.hsl.compiler.parser.AstParser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class Testing {
    public static void main(String[] args) {
        String source =
            """
            const FOO = 1337
            const SPAWN_LOC = Location::Custom(FOO, 2.7, 3.0)

            fn foo() {
                stat player msg = "hello"
                stat global kills: int

                msg = "foo"
            }
            """;

        AstParser parser = Parsers.of(source);

        Game game = new Game();
        Node.game(game);
        assertDoesNotThrow(() -> parser.parseGame(game));

        for (Constant constant : game.constants().values())
            System.out.println(constant.print());

        for (Method method : game.methods().values())
            System.out.println(method.print());
    }
}
