public class Classes {

	public String className;
	public String[] classMethod;


	public void Classes(){
		System.out.println("Class Created");
	}

	public void addClass(String cName, String[] cMethod, String[] cVarTypes, String[] cVarNames){
		className = cName;
		classMethod = cMethod;
	}

}
