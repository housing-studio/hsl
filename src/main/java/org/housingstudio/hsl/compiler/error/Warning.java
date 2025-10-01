package org.housingstudio.hsl.compiler.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum Warning {
    /**
     * `INVALID_NAMING_CONVENTION` indicates, that the target identifier does not match the preferred naming convention
     * of HSL.
     */
    INVALID_NAMING_CONVENTION(100),

    /**
     * `TYPE_CONVERSION_TO_ITSELF` indicates, that the type conversion converts to itself.
     */
    TYPE_CONVERSION_TO_ITSELF(101);

    /**
     * The error code of the token error.
     */
    private final int code;
}
