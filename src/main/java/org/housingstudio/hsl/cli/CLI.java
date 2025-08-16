package org.housingstudio.hsl.cli;

import com.moandjiezana.toml.Toml;
import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.Main;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
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
            case "new" -> newProject(args);
            case "export" -> export(args);
            default -> sendHelp();
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

        String build = loadResource("build.toml").replace("{project.name}", projectName);
        writeFile(new File(projectDir, "build.toml"), build);

        System.out.println("Created new project: " + projectName);
    }

    private void export(String[] args) {
        File workDir = new File(System.getProperty("user.dir"));
        File buildFile = new File(workDir, "build.toml");

        if (!buildFile.exists()) {
            System.err.println("Run hsl export from a valid hsl project directory.");
            return;
        }

        Toml toml = new Toml().read(buildFile);
        String name = toml.getString("name");

        System.out.println("Compiling and exporting project: " + name);
    }

    private void writeFile(@NotNull File path, @NotNull String content) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write file: " + path.getName(), e);
        }
    }

    private @NotNull String readFile(@NotNull File path) {
        try {
            return Files.readString(path.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Unable to read file: " + path.getName(), e);
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
