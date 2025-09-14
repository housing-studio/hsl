package org.housingstudio.hsl.cli;

import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.Main;
import org.housingstudio.hsl.compiler.Compiler;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.codegen.impl.house.House;
import org.housingstudio.hsl.compiler.codegen.impl.house.Metadata;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Arrays;

@UtilityClass
public class CLI {
    public void parse(String[] args) {
        if (args.length == 0) {
            sendHelp();
            return;
        }

        String command = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);

        switch (command) {
            case "new":
                newProject(args);
                break;
            case "export":
                export(args);
                break;
            default:
                sendHelp();
        }
    }

    private void sendHelp() {
        System.err.println("usage: hsl new,export [...]");
        System.err.println(" ");
        System.err.println("  new <project name> - create a new project");
        System.err.println("  export             - compile and export the project");
    }

    private void newProject(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: hsl new <project name>");
            return;
        }

        String projectName = args[0];

        File workDir = new File(System.getProperty("user.dir"));
        File projectDir = new File(workDir, projectName);

        if (projectDir.exists()) {
            System.err.println("Project directory already exists: " + projectName);
            return;
        }

        if (!projectDir.mkdirs()) {
            System.err.println("Failed to create project directory: " + projectName);
            return;
        }

        writeFile(new File(projectDir, "example.hsl"), loadResource("example.hsl"));

        String build = loadResource("build.toml").replace("{project.id}", projectName);
        writeFile(new File(projectDir, "build.toml"), build);

        System.out.println("Created new project: " + projectName);
    }

    private void export(String[] args) {
        boolean verbose = Arrays.asList(args).contains("-v");

        File workDir = new File(System.getProperty("user.dir"));
        File buildFile = new File(workDir, "build.toml");

        if (!buildFile.exists()) {
            System.err.println("Run hsl export from a valid hsl project directory.");
            return;
        }

        try {
            Metadata metadata = Metadata.read(buildFile);
            System.out.println(
                Format.MAGENTA + "" + Format.BOLD + "HSL: " + Format.DEFAULT +
                Format.LIGHT_GRAY + "Compiling and exporting project: " + Format.WHITE + metadata.id()
            );

            Compiler compiler = Compiler.create(metadata, workDir);
            compiler.init();
            compiler.compileSources();

            House export = compiler.export();
            String json = new GsonBuilder().setPrettyPrinting().create().toJson(export);

            File targetDir = new File(workDir, "target");
            if (!targetDir.exists())
                targetDir.mkdirs();

            File exportFile = new File(targetDir, metadata.id() + ".json");
            writeFile(exportFile, json);

            System.out.println(
                Format.MAGENTA + "" + Format.BOLD + "HSL: " + Format.DEFAULT +
                Format.GREEN + "Compiled and exported project to " + Format.WHITE + exportFile.getName()
            );
        } catch (Exception e) {
            System.out.println(
                Format.MAGENTA + "" + Format.BOLD + "HSL: " + Format.DEFAULT +
                Format.RED + "Failed to compile project"
            );
            if (verbose) {
                System.out.println("Compiler error:");
                e.printStackTrace(System.err);
            }
            else
                System.out.println(
                    Format.LIGHT_YELLOW + "" + Format.BOLD + "TIP: " + Format.DEFAULT +
                    Format.LIGHT_GRAY + "Use " + Format.WHITE + "`hsl export -v`" + Format.LIGHT_GRAY +
                    " to get more information"
                );
            System.exit(1);
        }
    }

    private void writeFile(@NotNull File path, @NotNull String content) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write file: " + path.getName(), e);
        }
    }

    private @NotNull String loadResource(@NotNull String path) {
        InputStream stream = Main.class.getClassLoader().getResourceAsStream(path);
        if (stream == null)
            throw new RuntimeException("Unable to load file: " + path);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }

            return builder.toString();
        } catch (IOException e) {
            throw new RuntimeException("Unable to load file: " + path, e);
        }
    }
}
