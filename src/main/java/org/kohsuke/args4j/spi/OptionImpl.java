package org.kohsuke.args4j.spi;

import org.kohsuke.args4j.Option;

/**
 * Implementation of @Option so we can instantiate it.
 *
 * @author Jan Materne
 */
public class OptionImpl extends AnnotationImpl implements Option {
	public String name;
	public String[] depends;
	public String[] forbids;

	public OptionImpl(ConfigElement ce) throws ClassNotFoundException {
		super(Option.class, ce);
		name = ce.name;
	}

	public String name() {
		return name;
	}

	public String[] depends() {
		return depends;
	}

	public String[] forbids() {
		return depends;
	}
}