package org.kohsuke.args4j;

public class EnumAttribute {

	@Option(name = "-animal", usage = "Give your favorite animal.")
	Animal myAnimal;

	enum Animal {HORSE, DUCK}

}
