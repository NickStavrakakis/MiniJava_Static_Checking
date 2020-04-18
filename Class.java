public class Class {

	public String name;
	public String nameExtends;
	public String[] methods;
	public String[] varTypes;
	public String[] varNames;

	public void addClass(String cName, String cExtendsName, String[] cMethod, String[] cVarTypes, String[] cVarNames){
		name = cName;
		nameExtends = cExtendsName;
		if (cMethod != null)
			methods = cMethod.clone();
		if (cVarTypes != null)
			varTypes = cVarTypes.clone();
		if (cVarNames != null)
			varNames = cVarNames.clone();
	}

}
