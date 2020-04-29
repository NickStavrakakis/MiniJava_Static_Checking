package types;

public class Method {

	public String type;
	public String name;
	public String nameClass;
	public String[] parTypes;
	public String[] parNames;
	public String[] varTypes;
	public String[] varNames;

	public void addMethod(String mType, String mName, String cName, String[] mIdTypes, String[] mIdNames, String[] mVarTypes, String[] mVarNames){

		type = mType;
		name = mName;
		nameClass = cName;

		if (mIdTypes != null)
			parTypes = mIdTypes.clone();
		if (mIdNames != null)
			parNames = mIdNames.clone();
		if (mVarTypes != null)
			varTypes = mVarTypes.clone();
		if (mVarNames != null)
			varNames = mVarNames.clone();

	}

}
