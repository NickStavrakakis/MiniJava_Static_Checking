import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.Hashtable;

public class LocateVisitor extends GJDepthFirst<Object, Object>{

	/* Creating our Symbol Table */
	public SymbolTable st;

	public LocateVisitor(){
		st = new SymbolTable();
	}

	/**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> "public"
     * f4 -> "static"
     * f5 -> "void"
     * f6 -> "main"
     * f7 -> "("
     * f8 -> "String"
     * f9 -> "["
     * f10 -> "]"
     * f11 -> Identifier()
     * f12 -> ")"
     * f13 -> "{"
     * f14 -> ( VarDeclaration() )*
     * f15 -> ( Statement() )*
     * f16 -> "}"
     * f17 -> "}"
     */
    public Object visit(MainClass n, Object argu) {


		String className = n.f1.accept(this, argu).toString();


		String[] methodIdTypes = new String[1];
		String[] methodIdNames = new String[1];
		methodIdTypes[0] = "String[]";
		methodIdNames[0] = n.f11.accept(this, argu).toString();


		String[] varTypes = null;
		String[] varNames = null;
		if (n.f14.present()){
			int totalVars = n.f14.size();
			varTypes = new String[totalVars];
			varNames = new String[totalVars];
			for (int currVarPos = 0; currVarPos < totalVars; currVarPos++){
				String[] currVar = (n.f14.elementAt(currVarPos).accept(this, argu).toString()).split(" ");
				varTypes[currVarPos] = currVar[0];
				varNames[currVarPos] = currVar[1];
			}
			/* Checking also if the variables are already defined */
			for (int i = 0; i < totalVars; i++){
				for (int j = 0; j < totalVars; j++){
					if ((i != j) && (varNames[i].equals(varNames[j]))){
			 			System.out.println("\tMultiple Definition: " + className + "." + varNames[i]);
						System.exit(1);
					}
				}
			}
		}


		/* ---------CLASSES--------- */
		/* Creating the information class for the current Class */
		Class currClass = new Class();
		String[] mainMethod = new String[1];
		mainMethod[0] = "main";
		currClass.addClass(className, null, mainMethod, null, null);
		/* Inserting this class in our Class' Hashtable */
		Hashtable<String, Class> stClasses = st.getClasses();
		stClasses.put(className, currClass);


		/* ---------METHODS--------- */
		/* Creating the information class for the main method */
		Method currMethod = new Method();
		currMethod.addMethod(null, mainMethod[0], className, methodIdTypes, methodIdNames, varTypes, varNames);
		/* Inserting this class in our Methods' Hashtable */
		Hashtable<String, Method> stMethods = st.getMethods();
		stMethods.put(className + ":" + mainMethod[0], currMethod);


		return null;
    }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
	public Object visit(ClassDeclaration n, Object argu){


 	   String className = n.f1.accept(this, argu).toString();
 	   /* Checking also if the class is already defined */
 	   Hashtable<String, Class> stClasses = st.getClasses();
 	   if (stClasses.get(className) != null){
 		   System.out.println("\tMultiple Definition: " + className);
 		   System.exit(1);
 	   }


 	   String[] varTypes = null;
 	   String[] varNames = null;
 	   if (n.f3.present()){
 	   int totalVars = n.f3.size();
 		   varTypes = new String[totalVars];
 		   varNames = new String[totalVars];
 		   for (int currVarPos = 0; currVarPos < totalVars; currVarPos++){
 			   String[] currVar = (n.f3.elementAt(currVarPos).accept(this, argu).toString()).split(" ");
 			   varTypes[currVarPos] = currVar[0];
 			   varNames[currVarPos] = currVar[1];
 		   }
 		   /* Checking also if the variables are already defined */
 		   for (int i = 0; i < totalVars; i++){
 			   for (int j = 0; j < totalVars; j++){
 				   if ((i != j) && (varNames[i].equals(varNames[j]))){
 					   System.out.println("\tMultiple Definition: " + className + "." + varNames[i]);
 					   System.exit(1);
 				   }
 			   }
 		   }
 	   }


 	   String[] methods = null;
 	   if (n.f4.present()){
 		   int totalMethods = n.f4.size();
 		   methods = new String[totalMethods];
 		   for (int currMethodPos = 0; currMethodPos < totalMethods; currMethodPos++){
 			   methods[currMethodPos] =  n.f4.elementAt(currMethodPos).accept(this, className).toString();
 		   }
 	   }


 	   /* ---------CLASSES--------- */
 	   /* Creating the information class for the current Class */
 	   Class currClass = new Class();
 	   currClass.addClass(className, null, methods, varTypes, varNames);
 	   /* Inserting this class in our Class' Hashtable */
 	   // Hashtable<String, Class> stClasses = st.getClasses();
 	   stClasses.put(className, currClass);


 	   return null;
    }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
	public Object visit(ClassExtendsDeclaration n, Object argu) {


  	  String className = n.f1.accept(this, argu).toString();
  	  /* Checking also if the class is already defined */
  	  Hashtable<String, Class> stClasses = st.getClasses();
  	  if (stClasses.get(className) != null){
  		  System.out.println("\tMultiple Definition: " + className);
  		  System.exit(1);
  	  }


  	  String classExtendsName = n.f3.accept(this, argu).toString();
  	  /* Checking also if the extend class has been defined */
  	  // Hashtable<String, Class> stClasses = st.getClasses();
  	  if (stClasses.get(classExtendsName) == null){
  		  System.out.println("\tMissing Declaration: " + classExtendsName);
  		  System.exit(1);
  	  }


  	  String[] varTypes = null;
  	  String[] varNames = null;
  	  if (n.f5.present()){
  	  int totalVars = n.f5.size();
  		  varTypes = new String[totalVars];
  		  varNames = new String[totalVars];
  		  for (int currVarPos = 0; currVarPos < totalVars; currVarPos++){
  			  String[] currVar = (n.f5.elementAt(currVarPos).accept(this, argu).toString()).split(" ");
  			  varTypes[currVarPos] = currVar[0];
  			  varNames[currVarPos] = currVar[1];
  		  }
  		  /* Checking also if the variables are already defined */
  		  for (int i = 0; i < totalVars; i++){
  			  for (int j = 0; j < totalVars; j++){
  				  if ((i != j) && (varNames[i].equals(varNames[j]))){
  					  System.out.println("\tMultiple Definition: " + className + "." + varNames[i]);
  					  System.exit(1);
  				  }
  			  }
  		  }
  	  }


  	  String[] methods = null;
  	  if (n.f6.present()){
  		  int totalMethods = n.f6.size();
  		  methods = new String[totalMethods];
  		  for (int currMethodPos = 0; currMethodPos < totalMethods; currMethodPos++){
  			 methods[currMethodPos] =  n.f6.elementAt(currMethodPos).accept(this, className + "<" + classExtendsName).toString();
  		  }
  	  }


  	  /* ---------CLASSES--------- */
  	  /* Creating the information class for the current Class */
  	  Class currClass = new Class();
  	  currClass.addClass(className, classExtendsName, methods, varTypes, varNames);
  	  /* Inserting this class in our Class' Hashtable */
  	  // Hashtable<String, Class> stClasses = st.getClasses();
  	  stClasses.put(className, currClass);


  	  return null;
    }

	/**
	 * f0 -> Type()
	 * f1 -> Identifier()
	 * f2 -> ";"
	 */
	public Object visit(VarDeclaration n, Object argu) {
		return n.f0.accept(this, argu) + " " + n.f1.accept(this, argu);
	}

   /**
    * f0 -> "public"
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
	public Object visit(MethodDeclaration n, Object argu) {
 	   /* argu = Method's Class Name + < + Method's Class Extender Name if exists*/


 	   String[] parts = argu.toString().split("<");
 	   String className = parts[0];
 	   String classExtendsName = null;
 	   if (parts.length == 2)
 		   classExtendsName = parts[1];


 	   String methodType = n.f1.accept(this, className).toString();


 	   String methodName = n.f2.accept(this, className).toString();
 	   /* Checking also if the method is already defined */
 	   Hashtable<String, Method> stMethods = st.getMethods();
 	   if (stMethods.get(className + "." + methodName) != null){
 		   System.out.println("\tMultiple Definition: " + className + "." + methodName);
 		   System.exit(1);
 	   }


 	   String[] parTypes = null;
 	   String[] parNames = null;
 	   int totalPars = 0;
 	   if (n.f4.present()){
 		   Object parameterList = n.f4.accept(this, className);
 		   String[] parameters = parameterList.toString().split(", ");
 		   totalPars = parameters.length;
 		   parTypes = new String[totalPars];
 		   parNames = new String[totalPars];
 		   for (int currParPos = 0; currParPos < totalPars; currParPos++){
 			   String[] currPar = parameters[currParPos].split(" ");
 			   parTypes[currParPos] = currPar[0];
 			   parNames[currParPos] = currPar[1];
 		   }
 		   /* Checking also if the parameters are already defined */
 		   for (int i = 0; i < totalPars; i++){
 			   for (int j = 0; j < totalPars; j++){
 				   if ((i != j) && (parNames[i].equals(parNames[j]))){
 					   System.out.println("\tMultiple Definition: " + className + "." + methodName + "." + parNames[i]);
 					   System.exit(1);
 				   }
 			   }
 		   }
 	   }


 	   String[] varTypes = null;
 	   String[] varNames = null;
 	   if (n.f7.present()){
 		   int totalVars = n.f7.size();
 		   varTypes = new String[totalVars];
 		   varNames = new String[totalVars];
 		   for (int currVarPos = 0; currVarPos < totalVars; currVarPos++){
 			   String[] currVar = (n.f7.elementAt(currVarPos).accept(this, className).toString()).split(" ");
 			   varTypes[currVarPos] = currVar[0];
 			   varNames[currVarPos] = currVar[1];
 		   }
 		   /* Checking also if the parameters are already defined */
 		   for (int i = 0; i < totalVars; i++){
 			   for (int j = 0; j < totalPars; j++){
 				   if (varNames[i].equals(parNames[j])){
 					   System.out.println("\tMultiple Definition: " + className + "." + methodName + "." + varNames[i]);
 					   System.exit(1);
 				   }
 			   }
 			   for (int j = 0; j < totalVars; j++){
 				   if ((i != j) && (varNames[i].equals(varNames[j]))){
 					   System.out.println("\tMultiple Definition: " + className + "." + methodName + "." + varNames[i]);
 					   System.exit(1);
 				   }
 			   }
 		   }
 	   }


 	   if (classExtendsName != null){
 		   Method extenderCLassMethod = stMethods.get(classExtendsName + "." + methodName);
 		   if (extenderCLassMethod != null){
 			   if (extenderCLassMethod.type != methodType){
 				   System.out.println("\tDifferent Method Type from superclass: " + className + "." + methodName);
 				   System.exit(1);
 			   }
 			   else{
 				   for (int i = 0; i < totalPars; i++){
 					   for (int j = 0; j < extenderCLassMethod.parNames.length; j++){
 						   if (!parTypes[i].equals(extenderCLassMethod.parTypes[j])){
 							   System.out.println("\tDifferent Variable Type from superclass: " + className + "." + methodName + "." + parTypes[i] + "." + parNames[i]);
 							   System.exit(1);
 						   }
 						   if (!parNames[i].equals(extenderCLassMethod.parNames[j])){
 							   System.out.println("\tDifferent Variable Name from superclass: " + className + "." + methodName + "." + parTypes[i] + "." + parNames[i]);
 							   System.exit(1);
 						   }
 					   }
 				   }
 			   }
 		   }
 	   }


 	   /* ---------METHODS--------- */
 	   /* Creating the information class for the main method */
 	   Method currMethod = new Method();
 	   currMethod.addMethod(methodType, methodName, argu.toString(), parTypes, parNames, varTypes, varNames);
 	   /* Inserting this class in our Methods' Hashtable */
 	   //Hashtable<String, Method> stMethods = st.getMethods();
 	   stMethods.put(argu + "." + methodName, currMethod);


 	   return methodName;
    }

   /**
    * f0 -> FormalParameter()
    * f1 -> FormalParameterTail()
    */
	public Object visit(FormalParameterList n, Object argu) {
 	   Object tail = n.f1.accept(this, argu);
 	   if (tail != null)
 		   return n.f0.accept(this, argu) + "" + tail;
 	   return n.f0.accept(this, argu);
    }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
	public Object visit(FormalParameter n, Object argu) {
		return n.f0.accept(this, argu).toString() + " " + n.f1.accept(this, argu).toString();
    }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
	public Object visit(FormalParameterTerm n, Object argu) {
		return n.f0 + " " + n.f1.accept(this, argu);
    }

	/**
 	* f0 -> BooleanArrayType()
 	*       | IntegerArrayType()
 	*/
    public Object visit(ArrayType n, Object argu) {
 	   return n.f0.accept(this, argu).toString();
    }

	/**
     * f0 -> "boolean"
     * f1 -> "["
     * f2 -> "]"
     */
    public Object visit(BooleanArrayType n, Object argu) {
		return n.f0.toString() + n.f1.toString() + n.f2.toString();
    }

	/**
     * f0 -> "int"
     * f1 -> "["
     * f2 -> "]"
     */
    public Object visit(IntegerArrayType n, Object argu) {
		return n.f0.toString() + n.f1.toString() + n.f2.toString();
    }

	/**
	* f0 -> "boolean"
	*/
	public Object visit(BooleanType n, Object argu) {
		return n.f0.toString();
	}

	/**
     * f0 -> "int"
     */
 	public Object visit(IntegerType n, Object argu) {
       return n.f0.toString();
    }

	/**
	* f0 -> <IDENTIFIER>
	*/
	public Object visit(Identifier n, Object argu) {
		return n.f0.toString();
	}

}
