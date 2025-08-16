package org.housingstudio.hsl;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String command = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);

        switch (command) {
            case "new" -> newProject(args);
            case "export" -> export(args);
        }
    }

    private static void newProject(String[] args) {
    }

    private static void export(String[] args) {
    }
}
