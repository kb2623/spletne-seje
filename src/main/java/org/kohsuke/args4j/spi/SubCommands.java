package org.kohsuke.args4j.spi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Collection of {@link SubCommand}s that define possible sub-commands.
 *
 * @author Kohsuke Kawaguchi
 * @see SubCommandHandler
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommands {
	SubCommand[] value();
}
