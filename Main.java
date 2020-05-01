import syntaxtree.*;
import visitor.*;
import java.util.*;
import java.io.*;
import visitors.*;
import types.ClassInfo;
import types.MethodInfo;
import types.SymbolTable;

class Main{
	public static void main (String [] args){

		SymbolTable st = new SymbolTable();

		if (args.length < 1){
			System.err.println("No File Given");
			System.exit(1);
		}

		FileInputStream fis_A = null;
		FileInputStream fis_B = null;

		for(int arg = 0; arg<args.length; arg++){

			try{

				String currFileName = args[arg];

				fis_A = new FileInputStream(currFileName);
				MiniJavaParser parser_A = new MiniJavaParser(fis_A);
				System.out.println("\n" + currFileName + " parsed successfully.");

				/* ---------EXECUTING VISITOR A: DefCollectVisitor--------- */
				DefCollectVisitor visitor_A = new DefCollectVisitor();
				Goal root_A = parser_A.Goal();
				root_A.accept(visitor_A, null);
				st = visitor_A.st;
				System.out.println("\tVisitor A ended successfully");

				fis_B = new FileInputStream(currFileName);
				MiniJavaParser parser_B = new MiniJavaParser(fis_B);

				/* ---------EXECUTING VISITOR B: TypeCheckVisitor---------- */
				TypeCheckVisitor visitor_B = new TypeCheckVisitor(st);
				Goal root_B = parser_B.Goal();
				root_B.accept(visitor_B, null);
				System.out.println("\tVisitor B ended successfully");
				System.out.println();


				/* --------------------PRINTING OFFSETS-------------------- */
				/* creating hashtables to store the offsets for every class */
				Hashtable<String, Integer> varOffsets = new Hashtable<String, Integer>();
				Hashtable<String, Integer> methOffsets = new Hashtable<String, Integer>();

				Hashtable<String, ClassInfo> stClasses = st.getClasses();
				Hashtable<String, MethodInfo> stMethods = st.getMethods();

				List<String> alreadyVisited = new ArrayList<>();

				/* Calculating the offsets until every class has been visited = Symbol Table of classes is Empty */
				while (!stClasses.isEmpty()){

					/* Removing from the Symbol Table the already printed/visited classes */
					for (String className: alreadyVisited){
						stClasses.remove(className);
					}

					/* We copy our hash table to a set so we can visit every element */
					Set<String> keys = stClasses.keySet();
					boolean skipped_main = false;
					for (String key: keys){

						ClassInfo currClass = stClasses.get(key);

						/* We don't print/calculate the main's class offsets, so we added to the two offset tables and then we skip it */
						if (skipped_main != true){
							if (currFileName.endsWith("/" + currClass.name + ".java")){
								skipped_main = true;
								varOffsets.put(currClass.name, 0);
								methOffsets.put(currClass.name, 0);
								alreadyVisited.add(currClass.name);
								continue;
							}
						}


						int varOffsetCounter;
						if (currClass.nameExtends != null){
							/* if its superclass is not on the offset table, that means that we have not calculated and printed the offsets of that class
						 	so we skip this class until the above happens, else we take its offset count */
							if (varOffsets.get(currClass.nameExtends) != null)
								varOffsetCounter = varOffsets.get(currClass.nameExtends);
							else
								continue;
						}
						else
							varOffsetCounter = 0;

						System.out.println("-----------Class " + currClass.name + " -----------");


						/* ---------VARIABLES--------- */
						System.out.println("--Variables---");

						if (currClass.varNames != null){
							int totalVars = currClass.varNames.length;
							for (int currVarPos = 0; currVarPos < totalVars; currVarPos++){
								System.out.println(currClass.name + "." + currClass.varNames[currVarPos] + " : " + varOffsetCounter);
								switch(currClass.varTypes[currVarPos]){
									case "int":
										varOffsetCounter = varOffsetCounter + 4;
										break;
									case "boolean":
										varOffsetCounter = varOffsetCounter + 1;
										break;
									default:
										varOffsetCounter = varOffsetCounter + 8;
								}
							}
						}
						varOffsets.put(currClass.name, varOffsetCounter);


						/* ----------METHODS---------- */
						System.out.println("---Methods---");

						/* if it's missing, add current class to the offset Hashtable */
						if (methOffsets.get(currClass.name) == null)
							methOffsets.put(currClass.name, 0);

						/* if has a super class, take its offset count */
						int methOffsetCounter;
						if (currClass.nameExtends != null){
							if (methOffsets.get(currClass.nameExtends) != null)
								methOffsetCounter = methOffsets.get(currClass.nameExtends);
							else /* will not happen, but we keep it for safety */
								continue;
						}
						else
							methOffsetCounter = 0;

						if (currClass.methods != null){
							for (String method: currClass.methods){
								String[] parts = method.split("\\.");
								String[] parentClassNames = st.getParentClassesNames(parts[0]);
								boolean inheritanceExist = false;
								for (int currParentClasseNamePos = 0; currParentClasseNamePos < parentClassNames.length; currParentClasseNamePos++){
									if (stMethods.get(parentClassNames[currParentClasseNamePos] + "." + parts[1]) != null){
										inheritanceExist = true;
										break;
									}
								}
								if (inheritanceExist != true){
									System.out.println(method + " : " + methOffsetCounter);
									methOffsetCounter = methOffsetCounter + 8;
								}
							}
						}
						methOffsets.put(currClass.name, methOffsetCounter);

						/* add the current class to the visited list, to remove it before the next big loop */
						alreadyVisited.add(currClass.name);
						System.out.println();
					}
				}
				System.out.println();
			}
			catch(ParseException ex){
				System.out.println(ex.getMessage());
			}
			catch(FileNotFoundException ex){
				System.err.println(ex.getMessage());
			}
			catch(Exception ex){
				System.err.println(ex.getMessage());
				System.out.println();
			}
			finally{
				try{
					if (fis_A != null) fis_A.close();
					if (fis_B != null) fis_B.close();
				}
				catch(IOException ex){
					System.err.println(ex.getMessage());
				}
			}
		}
	}
}
