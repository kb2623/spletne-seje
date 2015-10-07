package org.kohsuke.args4j;

import org.kohsuke.args4j.spi.Setters;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.OptionHandler;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.util.*;

public class XmlCmdParser extends CmdLineParser {

	@Option(name = "-xml", usage = "Path to configuration file", metaVar = "<path>")
	private File xmlFile = null;
	private OptionHandler currentOptionHandler = null;

	public XmlCmdParser(Object bean) {
		super(bean);
		try {
			Field f = this.getClass().getDeclaredField("xmlFile");
			Option o = f.getAnnotation(Option.class);
			addOption(Setters.create(f, this), o);
		} catch (NoSuchFieldException ignore) {}
	}

	private String getOptionName() {
		return currentOptionHandler.option.toString();
	}

	@Override
	public void parseArgument(String... args) throws CmdLineException {
		Set<OptionHandler> present = parseArgumentCmd(args);
		boolean helpSet = false;
		for (OptionHandler handler : getOptions()) {
			if(handler.option.help() && present.contains(handler)) {
				helpSet = true;
			}
		}
		if (!helpSet) {
			checkRequiredOptionsAndArguments(present);
		}
	}

	private Set<OptionHandler> parseArgumentXml(Set<OptionHandler> present) throws CmdLineException {
		if (xmlFile != null) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(xmlFile);
				if (document.getFirstChild().getNodeName().equals("spletneseje-configuration")) {
					processNode(present, document.getFirstChild().getChildNodes(), "");
				} else {
					throw new CmdLineException(this, "Bad xml file!!!");
				}
				return present;
			} catch (ParserConfigurationException e) {
				throw new CmdLineException(this, e.getMessage());
			} catch (SAXException e) {
				throw new CmdLineException(this, e.getMessage());
			} catch (IOException e) {
				throw new CmdLineException(this, e.getMessage());
			}
		} else {
			return present;
		}
	}

	private void processNode(Set<OptionHandler> present, NodeList list, String prop) throws CmdLineException {
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if ("property".equals(node.getNodeName())) {
				String value = node.getAttributes().getNamedItem("name").getNodeValue();
				currentOptionHandler = findOptionByAliasName(prop + (prop.isEmpty() ? "" : ".") + value);
				if (!present.contains(currentOptionHandler)) {
					CmdLineImpl line = new CmdLineImpl(node.getTextContent());
					currentOptionHandler.parseArguments(line);
					present.add(currentOptionHandler);
				}
			} else if (node.getChildNodes().getLength() > 1) {
				processNode(present, node.getChildNodes(), prop + (prop.isEmpty() ? "" : ".") + node.getNodeName());
			} else if (node.getChildNodes().getLength() == 1) {
				currentOptionHandler = findOptionByAliasName(prop + (prop.isEmpty() ? "" : ".") + node.getNodeName());
				if (!present.contains(currentOptionHandler)) {
					CmdLineImpl line = new CmdLineImpl(node.getTextContent());
					currentOptionHandler.parseArguments(line);
					present.add(currentOptionHandler);
				}
			}
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
		while(cmdLine.hasMore()) {
			String arg = cmdLine.getCurrentToken();
			if(isOption(arg)) {
				// '=' is for historical compatibility fallback
				boolean isKeyValuePair = arg.contains(getProperties().getOptionValueDelimiter()) || arg.indexOf('=') != -1;
				// parse this as an option.
				currentOptionHandler = isKeyValuePair ? findOptionHandler(arg) : findOptionByName(arg);
				if(currentOptionHandler == null) {
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
				if (currentOptionHandler == null) // this is a programmer error. arg index should be continuous
					throw new IllegalStateException("@Argument with index=" + argIndex + " is undefined");
				if (!currentOptionHandler.option.isMultiValued())
					argIndex++;
			}
			int diff = currentOptionHandler.parseArguments(cmdLine);
			cmdLine.proceed(diff);
			present.add(currentOptionHandler);
		}
		parseArgumentXml(present);
		return present;
	}

	protected OptionHandler findOptionByAliasName(String name) {
		for (OptionHandler h : getOptions()) {
			NamedOptionDef option = (NamedOptionDef)h.option;
			for (String alias : option.aliases()) {
				if (name.equals(alias)) {
					return h;
				}
			}
		}
		return null;
	}
}
