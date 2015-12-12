package org.kohsuke.args4j;

import org.kohsuke.args4j.spi.OptionHandler;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Argument of the command line.
 * <p>
 * This works mostly like {@link Option} except the following differences.
 * <p>
 * <ol>
 * <li>Arguments have an index about their relative position on the command line.
 * </ol>
 *
 * @author Kohsuke Kawaguchi
 * @author Mark Sinke
 */
@Retention(RUNTIME)
@Target({FIELD, METHOD, PARAMETER})
public @interface Argument {
	/**
	 * See {@link Option#usage()}.
	 */
	String usage() default "";

	/**
	 * See {@link Option#metaVar()}.
	 */
	String metaVar() default "";

	/**
	 * See {@link Option#required()}.
	 */
	boolean required() default false;

	/**
	 * See {@link Option#hidden()}.
	 */
	boolean hidden() default false;

	/**
	 * See {@link Option#handler()}.
	 */
	Class<? extends OptionHandler> handler() default OptionHandler.class;

	/**
	 * Position of the argument.
	 * <p>
	 * <p>
	 * If you define multiple single value properties to bind to arguments,
	 * they should have {@code index=0, index=1, index=2}, ... and so on.
	 * <p>
	 * <p>
	 * Multi value properties bound to arguments must be always the last entry.
	 */
	int index() default 0;

	/**
	 *
	 */
	boolean multiValued() default false;
}
