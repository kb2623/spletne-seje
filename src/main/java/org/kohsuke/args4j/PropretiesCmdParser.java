package org.kohsuke.args4j;

import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Setters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class PropretiesCmdParser extends CmdLineParser {

	@Option(name = "-props", usage = "Path to configuration file", metaVar = "<path>")
	private File popsFile = null;
	private OptionHandler currentOptionHandler = null;

	public PropretiesCmdParser(Object bean) {
		super(bean);
		try {
			Field f = this.getClass().getDeclaredField("popsFile");
			Option o = f.getAnnotation(Option.class);
			addOption(Setters.create(f, this), o);
		} catch (NoSuchFieldException ignore) {
		}
	}

	private String getOptionName() {
		return currentOptionHandler.option.toString();
	}

	@Override
	public void parseArgument(String... args) throws CmdLineException {
		Set<OptionHandler> present = parseArgumentCmd(args);
		boolean helpSet = false;
		for (OptionHandler handler : getOptions()) {
			if (handler.option.help() && present.contains(handler)) {
				helpSet = true;
			}
		}
		if (!helpSet) {
			checkRequiredOptionsAndArguments(present);
		}
	}

	private Set<OptionHandler> parseArgumentProps(Set<OptionHandler> present) throws CmdLineException {
		if (popsFile != null) {
			Properties properties = new Properties();
			try {
				properties.load(new FileInputStream(popsFile));
				for (String s : properties.stringPropertyNames()) {
					currentOptionHandler = findOptionByAliasName(s);
					if (currentOptionHandler == null) {
						System.err.println("Ignoring " + s + " = " + properties.getProperty(s));
					} else if (!present.contains(currentOptionHandler)) {
						CmdLineImpl line = new CmdLineImpl(properties.getProperty(s));
						currentOptionHandler.parseArguments(line);
						present.add(currentOptionHandler);
					}
				}
			} catch (IOException e) {
				throw new CmdLineException(this, "Error reading propreties");
			}
			return present;
		} else {
			return present;
		}
	}

	private Set<OptionHandler> parseArgumentCmd(String... args) throws CmdLineException {
		Utilities.checkNonNull(args, "args");
		String expandedArgs[] = args;
		if (super.getProperties().getAtSyntax()) {
			expandedArgs = expandAtFiles(args);
		}
		CmdLineImpl cmdLine = new CmdLineImpl(expandedArgs);
		Set<OptionHandler> present = new HashSet<>();
		int argIndex = 0;
		while (cmdLine.hasMore()) {
			String arg = cmdLine.getCurrentToken();
			if (isOption(arg)) {
				// '=' is for historical compatibility fallback
				boolean isKeyValuePair = arg.contains(getProperties().getOptionValueDelimiter()) || arg.indexOf('=') != -1;
				// parse this as an option.
				currentOptionHandler = isKeyValuePair ? findOptionHandler(arg) : findOptionByName(arg);
				if (currentOptionHandler == null) {
					throw new CmdLineException(this, Messages.UNDEFINED_OPTION, arg);
				}
				// known option; skip its name
				if (isKeyValuePair) {
					cmdLine.splitToken();
				} else {
					cmdLine.proceed(1);
				}
			} else {
				if (argIndex >= getArguments().size()) {
					Messages msg = getArguments().size() == 0 ? Messages.NO_ARGUMENT_ALLOWED : Messages.TOO_MANY_ARGUMENTS;
					throw new CmdLineException(this, msg, arg);
				}
				// known argument
				currentOptionHandler = getArguments().get(argIndex);
				if (currentOptionHandler == null) { // this is a programmer error. arg index should be continuous
					throw new IllegalStateException("@Argument with index=" + argIndex + " is undefined");
				}
				if (!currentOptionHandler.option.isMultiValued()) {
					argIndex++;
				}
			}
			int diff = currentOptionHandler.parseArguments(cmdLine);
			cmdLine.proceed(diff);
			present.add(currentOptionHandler);
		}
		parseArgumentProps(present);
		return present;
	}

	protected OptionHandler findOptionByAliasName(String name) {
		for (OptionHandler h : getOptions()) {
			NamedOptionDef option = (NamedOptionDef) h.option;
			for (String alias : option.aliases()) {
				if (name.equals(alias)) {
					return h;
				}
			}
		}
		return null;
	}
}
