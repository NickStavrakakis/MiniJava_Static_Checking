import java.util.Hashtable;


public class SymbolTable {


	// explain this on readme
	private Hashtable<String, Class> stClasses = new Hashtable<String, Class>();
	private Hashtable<String, Method> stMethods = new Hashtable<String, Method>();

	public String className;
	public String[] classMethod;



	public Hashtable<String, Class> getClasses(){
		return stClasses;
	}

	public Hashtable<String, Method> getMethods(){
		return stMethods;
	}

	public void print(){
		System.out.println(stClasses);

	}

	public String getVarType(String idName, String varName) throws Exception{
		System.out.println("getVarType("+ idName +","+ varName+")");
		Method currMethod = stMethods.get(idName);
		System.out.println("yolo1");

		if (currMethod != null){
		System.out.println("yolo122");

			if (currMethod.parNames != null){
				int totalPars = currMethod.parNames.length;
					System.out.println("totalPars: " + totalPars);
				for (int currPar = 0; currPar < totalPars; currPar++){
					System.out.println("yolo2 " + currMethod.parNames[currPar]);
					if (currMethod.parNames[currPar].equals(varName))
						return currMethod.parTypes[currPar];
				}
			}
			if (currMethod.varNames != null){
				int totalVars = currMethod.varNames.length;
				for (int currVar = 0; currVar < totalVars; currVar++){
					if (currMethod.varNames[currVar].equals(varName))
						return currMethod.varTypes[currVar];
				}
			}
			Class currClass = stClasses.get(currMethod.nameClass);
			if (currClass.varNames != null){
				int totalVars = currClass.varNames.length;
				for (int currVar = 0; currVar < totalVars; currVar++){
					if (currClass.varNames[currVar].equals(varName))
						return currClass.varTypes[currVar];
				}
			}
			throw new Exception("\t(a) Variable does not exist: " + idName + "." + varName);
		}
		Class currClass = stClasses.get(idName);
		System.out.println("yolo551");

		if (currClass != null){
			System.out.println("yolo2");
			int totalVars = currClass.varNames.length;
			for (int currVar = 0; currVar < totalVars; currVar++){
				if (currClass.varNames[currVar].equals(varName))
					return currClass.varTypes[currVar];
			}
			throw new Exception("\t(b) Variable does not exist: " + idName + "." + varName);
		}
		return null;
	}

	public String[] getParentClassesNames(String className){

		String parentClassNamesList = "";
		Class currClass = stClasses.get(className);

		while (currClass.nameExtends != null){
			parentClassNamesList = parentClassNamesList + "|" + currClass.nameExtends;
			currClass = stClasses.get(currClass.nameExtends);
		}

		return parentClassNamesList.split("\\|");
	}

}
