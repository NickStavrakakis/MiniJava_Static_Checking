package types;
import java.util.Hashtable;
import types.Class;
import types.Method;

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
		///*flg*/System.out.println("getVarType("+ idName +","+ varName+")");
		Method currMethod = stMethods.get(idName);
		if (currMethod != null){
			if (currMethod.parNames != null){
				int totalPars = currMethod.parNames.length;
				for (int currPar = 0; currPar < totalPars; currPar++){
					if (currMethod.parNames[currPar].equals(varName))
						return currMethod.parTypes[currPar];
				}
			}
			if (currMethod.varNames != null){
				int totalVars = currMethod.varNames.length;
				for (int currVar = 0; currVar < totalVars; currVar++){
					if (currMethod.varNames[currVar].equals(varName)){
						return currMethod.varTypes[currVar];
					}
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
			if (idName.contains(".")){
				String[] parts = idName.split("\\.");
				String[] parentClassNames = getParentClassesNames(parts[0]);
				for (int currParentClasseNamePos = 0; currParentClasseNamePos < parentClassNames.length; currParentClasseNamePos++){
					return getVarType(parentClassNames[currParentClasseNamePos], varName);
				}
			}

			throw new Exception("\t(a) Variable does not exist: " + idName + "." + varName);
		}

		Class currClass = stClasses.get(idName);
		if (currClass != null){
			int totalVars = currClass.varNames.length;
			for (int currVar = 0; currVar < totalVars; currVar++){
				if (currClass.varNames[currVar].equals(varName))
					return currClass.varTypes[currVar];
			}
			throw new Exception("\t(b) Variable does not exist: " + idName + "." + varName);
		}
		throw new Exception("\t(c) Variable does not exist: " + idName + "." + varName);
	}

	public String[] getParentClassesNames(String className){
		///*flg*/System.out.println("getParentClassesNames("+ className+")");
		String parentClassNamesList = "";
		Class currClass = stClasses.get(className);
		if(currClass != null){
			while (currClass.nameExtends != null){
				parentClassNamesList = currClass.nameExtends + "|" + parentClassNamesList;
				currClass = stClasses.get(currClass.nameExtends);
				if(currClass == null)
					break;
			}
		}
		///*flg*/System.out.println("will return " + parentClassNamesList);
		return parentClassNamesList.split("\\|");
	}

}
