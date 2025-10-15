package org.housingstudio.hsl.cli;

import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.Main;
import org.housingstudio.hsl.compiler.Compiler;
import org.housingstudio.hsl.compiler.codegen.interop.htsl.HtslExporter;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.codegen.impl.house.House;
import org.housingstudio.hsl.compiler.codegen.impl.house.Metadata;
import org.housingstudio.hsl.compiler.error.ErrorMode;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.jar.Manifest;

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
            case "diagnostics":
                diagnostics(args);
                break;
            case "version":
                version(args);
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
        System.err.println("    -v               - toggle verbose mode");
        System.err.println("    -htsl            - transpile project to HTSL representation");
        System.err.println("  diagnostics        - compile the project and print compiler diagnostics");
        System.err.println("  version            - print debug information about the compiler");
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

    private void diagnostics(String[] args) {
        File workDir = new File(System.getProperty("user.dir"));
        File buildFile = new File(workDir, "build.toml");

        if (!buildFile.exists()) {
            System.err.println("Run hsl export from a valid hsl project directory.");
            System.exit(1);
            return;
        }

        Metadata metadata = Metadata.read(buildFile);
        Compiler compiler = Compiler.create(metadata, workDir, ErrorMode.JSON);

        try {
            compiler.init();
            compiler.compileSources();
            compiler.export();
        } catch (Exception e) {
            // do not emit compiler stack trace, as errors and warnings are already captured by diagnostics
        } finally {
            System.out.println(compiler.diagnostics().export());
        }
    }

    private void export(String[] args) {
        boolean verbose = Arrays.asList(args).contains("-v");
        boolean htsl = Arrays.asList(args).contains("-htsl");
        boolean raw = Arrays.asList(args).contains("-raw");

        if (raw) {
            Format.setEnabled(false);
        }

        File workDir = new File(System.getProperty("user.dir"));
        File buildFile = new File(workDir, "build.toml");

        if (!buildFile.exists()) {
            System.err.println("Run hsl export from a valid hsl project directory.");
            return;
        }

        try {
            Metadata metadata = Metadata.read(buildFile);

            if (verbose)
                System.out.println(
                    Format.MAGENTA + "" + Format.BOLD + "HSL: " + Format.DEFAULT +
                    Format.LIGHT_GRAY + "Compiling and exporting project: " + Format.WHITE + metadata.id() +
                    Format.DEFAULT
                );

            Compiler compiler = Compiler.create(metadata, workDir, ErrorMode.PRETTY_PRINT);
            compiler.init();
            compiler.compileSources();

            if (verbose)
                System.out.println(
                    Format.MAGENTA + "" + Format.BOLD + "HSL: " + Format.DEFAULT +
                    Format.GREEN + "Applied " + Format.WHITE + compiler.optimizations() + Format.GREEN +
                    " optimization steps" + Format.DEFAULT
                );

            compiler.invokeMain();

            File targetDir = new File(workDir, "target");
            if (!targetDir.exists())
                targetDir.mkdirs();

            File exportFile;

            House export = compiler.export();
            if (htsl) {
                String header = loadResource("htsl.header")
                    .replace("%project.name%", metadata.id())
                    .replace("%project.author%", metadata.author() != null ? metadata.author() : "N/A")
                    .replace("%build.timestamp%", DateTimeFormatter.ISO_INSTANT.format(Instant.now()));
                String htslData = HtslExporter.export(export);

                exportFile = new File(targetDir, metadata.id() + ".htsl");
                writeFile(exportFile, header + htslData);
            } else {
                String json = new GsonBuilder().setPrettyPrinting().create().toJson(export);
                exportFile = new File(targetDir, metadata.id() + ".json");
                writeFile(exportFile, json);
            }

            if (verbose)
                System.out.println(
                    Format.MAGENTA + "" + Format.BOLD + "HSL: " + Format.DEFAULT +
                    Format.GREEN + "Compiled and exported project to " + Format.WHITE + exportFile.getName() +
                    Format.DEFAULT
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

    private void version(String[] args) {
        String ver = Main.class.getPackage().getImplementationVersion();
        if (ver == null)
            ver = "DEV BUILD";

        String commit = getManifestAttribute("Git-Commit");
        String timestamp = getManifestAttribute("Build-Timestamp");

        System.out.printf("hsl %s (%s %s)\n", ver, commit, timestamp);

        System.out.printf("host: %s %s (%s)\n",
            System.getProperty("os.name"),
            System.getProperty("os.version"),
            System.getProperty("os.arch")
        );

        System.out.printf("vendor: %s (%s)%n",
            System.getProperty("java.vendor"),
            System.getProperty("java.version")
        );

        System.out.printf("runtime: %s (%s)%n",
            System.getProperty("java.runtime.name"),
            System.getProperty("java.runtime.version")
        );

        System.out.printf("vm: %s (%s)%n",
            System.getProperty("java.vm.name"),
            System.getProperty("java.vm.version")
        );
    }

    private @NotNull String getManifestAttribute(String name) {
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF")) {
            if (is == null)
                return "unknown";

            Manifest manifest = new Manifest(is);
            String value = manifest.getMainAttributes().getValue(name);
            return value != null ? value : "unknown";
        } catch (IOException e) {
            return "unknown";
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
