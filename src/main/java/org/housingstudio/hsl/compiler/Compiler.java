package org.housingstudio.hsl.compiler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Game;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Macro;
import org.housingstudio.hsl.compiler.ast.impl.placeholder.PlaceholderRegistry;
import org.housingstudio.hsl.runtime.natives.NativeDefinitions;
import org.housingstudio.hsl.compiler.codegen.hierarchy.NodeVisitor;
import org.housingstudio.hsl.compiler.ast.impl.action.BuiltinActions;
import org.housingstudio.hsl.compiler.ast.impl.action.BuiltinConditions;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.error.ErrorMode;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenTransformer;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.housingstudio.hsl.compiler.token.Tokenizer;
import org.housingstudio.hsl.compiler.codegen.Exporter;
import org.housingstudio.hsl.compiler.codegen.impl.house.House;
import org.housingstudio.hsl.compiler.codegen.impl.house.Metadata;
import org.housingstudio.hsl.compiler.optimization.Optimizer;
import org.housingstudio.hsl.lsp.Diagnostics;
import org.housingstudio.hsl.runtime.vm.Frame;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class Compiler {
    private final @NotNull Diagnostics diagnostics = new Diagnostics();

    private final @NotNull Metadata metadata;
    private final @NotNull List<File> sourceFiles;
    private final @NotNull Game game;
    private final @NotNull ErrorMode mode;

    private int optimizations = 0;

    public void init() {
        if (mode == ErrorMode.JSON)
            Format.setEnabled(false);

        BuiltinActions.init();
        BuiltinConditions.init();
        NativeDefinitions.init();
        PlaceholderRegistry.init();
    }

    public void compileSources() {
        List<AstParser> parsers = new ArrayList<>();

        for (File sourceFile : sourceFiles) {
            String content = readFile(sourceFile);
            List<Token> tokens = tokenizeFile(sourceFile, content);
            parsers.add(createParser(tokens, sourceFile, content));
        }

        NativeDefinitions.apply(game);

        Node.game(game);
        for (AstParser parser : parsers)
            parser.parseGame(game);

        NodeVisitor.initHierarchy();
        NodeVisitor.initLifecycle();

        optimizeAst();
    }

    public void invokeMain() {
        Macro macro = game.macros().get("main");
        if (macro == null)
            return;

        Frame heap = new Frame(null, "main", 0, 0, 0, null, Frame.Kind.MACRO);
        macro.invoke(heap);
    }

    public @NotNull House export() {
        return Exporter.export(metadata, game);
    }

    private void optimizeAst() {
        Optimizer optimizer = new Optimizer();
        optimizations = optimizer.optimize(game);

        /*
        // Log optimization results if in debug mode
        if (mode != ErrorMode.JSON && optimizations > 0) {
            System.out.println("Applied " + optimizations + " optimizations");
        }
         */
    }

    private @NotNull List<Token> tokenizeFile(@NotNull File file, @NotNull String content) {
        Tokenizer tokenizer = new Tokenizer(file, content, diagnostics, mode);
        List<Token> tokens = new ArrayList<>();
        Token token;

        do {
            tokens.add(token = tokenizer.next());
            if (token.is(TokenType.UNEXPECTED))
                throw new RuntimeException(token.value());
        } while (token.hasNext());

        return new TokenTransformer(tokens).transform();
    }

    private @NotNull AstParser createParser(@NotNull List<Token> tokens, @NotNull File file, @NotNull String content) {
        ParserContext context = new ParserContext(tokens, file, content, diagnostics, mode);
        Node.context(context);

        return new AstParser(context);
    }

    private @NotNull String readFile(@NotNull File file) {
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            return sb.toString();
        } catch (IOException e) {
            System.err.println("Unable to read source file: " + file.getAbsoluteFile());
            throw new RuntimeException(e);
        }
    }

    public static @NotNull Compiler create(
        @NotNull Metadata metadata, @NotNull File projectDir, @NotNull ErrorMode mode
    ) {
        try {
            List<File> files = Files.walk(projectDir.toPath())
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".hsl"))
                .map(Path::toFile)
                .collect(Collectors.toList());

            return new Compiler(metadata, files, new Game(), mode);
        } catch (IOException e) {
            System.err.println("Unable to collect source files");
            throw new RuntimeException(e);
        }
    }
}
