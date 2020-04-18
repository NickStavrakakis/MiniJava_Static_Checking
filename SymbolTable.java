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

	public void addClass(String cName, String[] cMethod, String[] cVarTypes, String[] cVarNames){
		className = cName;
		classMethod = cMethod;
	}

	public Hashtable<String, Method> getMethods(){
		return stMethods;
	}

	public void print(){
		System.out.println(stClasses);
	}

}
