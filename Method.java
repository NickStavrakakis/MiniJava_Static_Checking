public class Method {

	public String methodType;
	public String methodName;
	public String className;
	public String[] methodIdTypes;
	public String[] methodIdNames;
	public String[] methodVarTypes;
	public String[] methodVarNames;

	public void addMethod(String mType, String mName, String cName, String[] mIdTypes, String[] mIdNames, String[] mVarTypes, String[] mVarNames){
		methodType = mType;
		methodName = mName;
		className = cName;
		if (mIdTypes != null)
			methodIdTypes = mIdTypes.clone();
		if (mIdNames != null)
			methodIdNames = mIdNames.clone();
		if (mVarTypes != null)
			methodVarTypes = mVarTypes.clone();
		if (mVarNames != null)
			methodVarNames = mVarNames.clone();
		//System.out.println("Added " + className + ": " + methodName + "()");
	}

}
