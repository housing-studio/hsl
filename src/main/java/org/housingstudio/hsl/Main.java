package org.housingstudio.hsl;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.cli.CLI;

@UtilityClass
public class Main {
    public void main(String[] args) {
        CLI.parse(args);
    }
}
