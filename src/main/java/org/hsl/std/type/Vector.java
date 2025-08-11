package org.hsl.std.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class Vector {
    private double x, y, z;
}
