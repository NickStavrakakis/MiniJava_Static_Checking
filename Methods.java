public class Methods {

	public String methodType;
	public String methodName;
	public String className;
	public String[] methodIdTypes;
	public String[] methodIdNames;
	public String[] methodVarTypes;
	public String[] methodVarNames;

	public void createMethod(){
		System.out.println("createMethod Created");
	}

	public void addMethod(String mType, String mName, String cName, String[] mIdTypes, String[] mIdNames, String[] mVarTypes, String[] mVarNames){
		methodType = mType;
		methodName = mName;
		className = cName;
		if (mIdTypes != null)
			methodIdTypes = mIdTypes.clone();
		if (mIdTypes != null)
			methodIdNames = mIdNames.clone();
		if (mIdTypes != null)
			methodVarTypes = mVarTypes.clone();
		if (mIdTypes != null)
			methodVarNames = mVarNames.clone();
		System.out.println("Added " + className + ": " + methodName + "()");
	}

}
