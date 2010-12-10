package edu.stanford.math.plex4.generation;

import java.util.List;
import java.util.Vector;

import edu.stanford.math.primitivelib.generation.ClassSpecifier;
import edu.stanford.math.primitivelib.generation.JavaCodeGenerator;

/**
 * This class contains functions for creating auto-generated code. In particular it generates the homology algorithms
 * for different underlying types (int, boolean, object) from template files in the templates folder.
 * 
 * @author Andrew Tausz
 *
 */
public class GeneratorDriver {
	private static List<ClassSpecifier> classSpecifiers = new Vector<ClassSpecifier>();
	private static String basePackageName = "edu.stanford.math.plex4.autogen";
	private static String baseSourceDirectory = "src/java";
	private static String templateDirectory = "templates/";

	public static void main(String[] args) {
		addHomologyClasses();
		generateClasses();
	}

	private static void addHomologyClasses() {
		Vector<String> types = new Vector<String>();
		types.add("int");
		types.add("boolean");
		types.add("F");
		String[] classes = new String[]{"PersistenceAlgorithm", "PersistentHomology", "AbsoluteHomology", "RelativeHomology", "ClassicalHomology"};
		String packageId = "homology";

		for (String type: types) {
			Vector<String> templateTypes = new Vector<String>();
			templateTypes.add(type);
			for (String classTag: classes) {
				ClassSpecifier specifier = new ClassSpecifier(packageId, classTag, templateTypes);
				specifier.addGenericType("U");
				classSpecifiers.add(specifier);
			}
		}
	}

	private static void generateClasses() {
		JavaCodeGenerator generator = new JavaCodeGenerator(basePackageName, templateDirectory, baseSourceDirectory);
		generator.generateClasses(classSpecifiers);
	}
}
