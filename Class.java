public class Class {

	public String className;
	public String classExtendsName;
	public String[] classMethod;
	public String[] varTypes;
	public String[] varNames;

	public void addClass(String cName, String cExtendsName, String[] cMethod, String[] cVarTypes, String[] cVarNames){
		className = cName;
		classExtendsName = cExtendsName;
		if (cMethod != null)
			classMethod = cMethod.clone();
		if (cVarTypes != null)
			varTypes = cVarTypes.clone();
		if (cVarNames != null)
			varNames = cVarNames.clone();
	}

}
