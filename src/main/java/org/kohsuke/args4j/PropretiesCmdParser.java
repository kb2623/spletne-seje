package org.kohsuke.args4j;

import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setters;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

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

	/**
	 * Reads all lines of a file with the platform encoding.
	 */
	private static List<String> readAllLines(File f) throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(f));
		try {
			List<String> result = new ArrayList<String>();
			String line;
			while ((line = r.readLine()) != null) {
				result.add(line);
			}
			return result;
		} finally {
			r.close();
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

	private void checkRequiredOptionsAndArguments(Set<OptionHandler> present) throws CmdLineException {
		// make sure that all mandatory options are present
		for (OptionHandler handler : getOptions()) {
			if (handler.option.required() && !present.contains(handler)) {
				throw new CmdLineException(this, Messages.REQUIRED_OPTION_MISSING, handler.option.toString());
			}
		}
		// make sure that all mandatory arguments are present
		for (OptionHandler handler : getArguments()) {
			if (handler.option.required() && !present.contains(handler)) {
				throw new CmdLineException(this, Messages.REQUIRED_ARGUMENT_MISSING, handler.option.toString());
			}
		}
		//make sure that all requires arguments are present
		for (OptionHandler handler : present) {
			if (handler.option instanceof NamedOptionDef && !isHandlerHasHisOptions((NamedOptionDef) handler.option, present)) {
				throw new CmdLineException(this, Messages.REQUIRES_OPTION_MISSING,
						handler.option.toString(), Arrays.toString(((NamedOptionDef) handler.option).depends()));
			}
		}
		//make sure that all forbids arguments are not present
		for (OptionHandler handler : present) {
			if (handler.option instanceof NamedOptionDef && !isHandlerAllowOtherOptions((NamedOptionDef) handler.option, present)) {
				throw new CmdLineException(this, Messages.FORBIDDEN_OPTION_PRESENT,
						handler.option.toString(), Arrays.toString(((NamedOptionDef) handler.option).forbids()));
			}
		}
	}

	/**
	 * @return {@code true} if all options required by {@code option} are present, {@code false} otherwise
	 */
	private boolean isHandlerHasHisOptions(NamedOptionDef option, Set<OptionHandler> present) {
		for (String depend : option.depends()) {
			if (!present.contains(findOptionHandler(depend))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return {@code true} if all options forbid by {@code option} are not present, {@code false} otherwise
	 */
	private boolean isHandlerAllowOtherOptions(NamedOptionDef option, Set<OptionHandler> present) {
		for (String forbid : option.forbids()) {
			if (present.contains(findOptionHandler(forbid)))
				return false;
		}
		return true;
	}

	private OptionHandler findOptionHandler(String name) {
		// Look for key/value pair first.
		int pos = name.indexOf(getProperties().getOptionValueDelimiter());
		if (pos < 0) {
			pos = name.indexOf('=');    // historical compatibility fallback
		}
		if (pos > 0) {
			name = name.substring(0, pos);
		}
		return findOptionByName(name);
	}

	/**
	 * Finds a registered {@code OptionHandler} by its name or its alias.
	 *
	 * @param name name
	 * @return the {@code OptionHandler} or {@code null}
	 */
	private OptionHandler findOptionByName(String name) {
		for (OptionHandler h : getOptions()) {
			NamedOptionDef option = (NamedOptionDef) h.option;
			if (name.equals(option.name())) {
				return h;
			}
			for (String alias : option.aliases()) {
				if (name.equals(alias)) {
					return h;
				}
			}
		}
		return null;
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

	/**
	 * Expands every entry prefixed with the AT sign by
	 * reading the file. The AT sign is used to reference
	 * another file that contains command line options separated
	 * by line breaks.
	 *
	 * @param args the command line arguments to be preprocessed.
	 * @return args with the @ sequences replaced by the text files referenced
	 * by the @ sequences, split around the line breaks.
	 * @throws CmdLineException
	 */
	private String[] expandAtFiles(String args[]) throws CmdLineException {
		List<String> result = new ArrayList<String>();
		for (String arg : args) {
			if (arg.startsWith("@")) {
				File file = new File(arg.substring(1));
				if (!file.exists())
					throw new CmdLineException(this, Messages.NO_SUCH_FILE, file.getPath());
				try {
					result.addAll(readAllLines(file));
				} catch (IOException ex) {
					throw new CmdLineException(this, "Failed to parse "+file,ex);
				}
			} else {
				result.add(arg);
			}
		}
		return result.toArray(new String[result.size()]);
	}

	/**
	 * Essentially a pointer over a {@link String} array.
	 * Can move forward; can look ahead.
	 */
	private class CmdLineImpl implements Parameters {
		private final String[] args;
		private int pos;

		CmdLineImpl(String[] args) {
			this.args = args;
			pos = 0;
		}

		CmdLineImpl(String s) {
			this.args = new String[]{s};
			pos = 0;
		}

		protected boolean hasMore() {
			return pos < args.length;
		}

		protected String getCurrentToken() {
			return args[pos];
		}

		private void proceed(int n) {
			pos += n;
		}

		public String getParameter(int idx) throws CmdLineException {
			if (pos + idx >= args.length || pos + idx < 0)
				throw new CmdLineException(PropretiesCmdParser.this, Messages.MISSING_OPERAND, getOptionName());
			return args[pos + idx];
		}

		public int size() {
			return args.length - pos;
		}

		/**
		 * Used when the current token is of the form "-option=value",
		 * to replace the current token by "value", as if this was given as two tokens "-option value"
		 */
		void splitToken() {
			if (pos < args.length && pos >= 0) {
				int idx = args[pos].indexOf("=");
				if (idx > 0) {
					args[pos] = args[pos].substring(idx + 1);
				}
			}
		}
	}
}
