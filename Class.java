public class Class {

	public String name;
	public String nameExtends;
	public String[] methods;
	public String[] varTypes;
	public String[] varNames;

	public void addClass(String cName, String cExtendsName, String[] cMethod, String[] cvarTypes, String[] cvarNames){

		name = cName;
		nameExtends = cExtendsName;
		if (cMethod != null)
			methods = cMethod.clone();
		if (cvarTypes != null)
			varTypes = cvarTypes.clone();
		if (cvarNames != null)
			varNames = cvarNames.clone();
			
	}

}
