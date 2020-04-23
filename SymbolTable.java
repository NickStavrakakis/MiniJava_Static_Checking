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

	public String getVarType(String idName, String varName){
		System.out.println("getVarType("+ idName +", "+ varName+")");
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
			System.out.println("\tVariable does2 not exist: " + idName + "." + varName);
			System.exit(1);
		}
		Class currClass = stClasses.get(idName);
		if (currClass != null){
			int totalVars = currClass.varNames.length;
			for (int currVar = 0; currVar < totalVars; currVar++){
				if (currClass.varNames[currVar].equals(varName))
					return currClass.varTypes[currVar];
			}
			System.out.println("\tVariable does1 not exist: " + idName + "." + varName);
			System.exit(1);
		}
		return null;
	}


}
