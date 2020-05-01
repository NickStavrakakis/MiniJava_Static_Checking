package visitors;
import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.Hashtable;
import types.ClassInfo;
import types.MethodInfo;
import types.SymbolTable;

public class DefCollectVisitor extends GJDepthFirst<Object, Object>{

	/* Creating our Symbol Table */
	public SymbolTable st;


	public DefCollectVisitor(){
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
	public Object visit(MainClass n, Object argu) throws Exception{

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
					if ((i != j) && (varNames[i].equals(varNames[j])))
						throw new Exception("\terror: variable " +  varNames[i] + " is already defined in class " + className);
				}
			}
		}

		/* ---------CLASSES--------- */
		/* Creating the information class for the current Class */
		ClassInfo currClass = new ClassInfo();
		String[] mainMethod = new String[1];
		mainMethod[0] = "main";
		currClass.addClass(className, null, mainMethod, null, null);
		/* Inserting this class in our class' Hashtable */
		Hashtable<String, ClassInfo> stClasses = st.getClasses();
		stClasses.put(className, currClass);

		/* ---------METHODS--------- */
		/* Creating the information class for the main method */
		MethodInfo currMethod = new MethodInfo();
		currMethod.addMethod(null, className + "." + mainMethod[0], className, methodIdTypes, methodIdNames, varTypes, varNames);
		/* Inserting this class in our Methods' Hashtable */
		Hashtable<String, MethodInfo> stMethods = st.getMethods();
		stMethods.put(className + "." + mainMethod[0], currMethod);

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
	public Object visit(ClassDeclaration n, Object argu) throws Exception{

		String className = n.f1.accept(this, argu).toString();
		/* Checking also if the class is already defined */
		Hashtable<String, ClassInfo> stClasses = st.getClasses();
		if (stClasses.get(className) != null)
			throw new Exception("\terror: duplicate class " + className);

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
					if ((i != j) && (varNames[i].equals(varNames[j])))
						throw new Exception("\terror: variable " +  varNames[i] + " is already defined in class " + className);
				}
			}
		}

		String[] methods = null;
		if (n.f4.present()){
			int totalMethods = n.f4.size();
			methods = new String[totalMethods];
			for (int currMethodPos = 0; currMethodPos < totalMethods; currMethodPos++){
				methods[currMethodPos] =	n.f4.elementAt(currMethodPos).accept(this, className).toString();
			}
		}

		/* ---------CLASSES--------- */
		/* Creating the information class for the current Class */
		ClassInfo currClass = new ClassInfo();
		currClass.addClass(className, null, methods, varTypes, varNames);
		/* Inserting this class in our Class' Hashtable */
		// Hashtable<String, ClassInfo> stClasses = st.getClasses();
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
	public Object visit(ClassExtendsDeclaration n, Object argu) throws Exception{

		String className = n.f1.accept(this, argu).toString();
		/* Checking also if the class is already defined */
		Hashtable<String, ClassInfo> stClasses = st.getClasses();
		if (stClasses.get(className) != null)
			throw new Exception("\terror: duplicate class " + className);

		String classExtendsName = n.f3.accept(this, argu).toString();
		/* Checking also if the extend class has been defined */
		// Hashtable<String, ClassInfo> stClasses = st.getClasses();
		if (stClasses.get(classExtendsName) == null)
			throw new Exception("\terror: cannot find symbol " + classExtendsName);

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
					if ((i != j) && (varNames[i].equals(varNames[j])))
						throw new Exception("\terror: variable " +  varNames[i] + " is already defined in class " + className);
				}
			}
		}

		String[] methods = null;
		if (n.f6.present()){
			int totalMethods = n.f6.size();
			methods = new String[totalMethods];
			for (int currMethodPos = 0; currMethodPos < totalMethods; currMethodPos++){
			methods[currMethodPos] =	n.f6.elementAt(currMethodPos).accept(this, className + "<" + classExtendsName).toString();
			}
		}

		/* ---------CLASSES--------- */
		/* Creating the information class for the current Class */
		ClassInfo currClass = new ClassInfo();
		currClass.addClass(className, classExtendsName, methods, varTypes, varNames);
		/* Inserting this class in our Class' Hashtable */
		// Hashtable<String, ClassInfo> stClasses = st.getClasses();
		stClasses.put(className, currClass);

		return null;
	}

	/**
	* f0 -> Type()
	* f1 -> Identifier()
	* f2 -> ";"
	*/
	public Object visit(VarDeclaration n, Object argu) throws Exception{
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
	public Object visit(MethodDeclaration n, Object argu) throws Exception{
		/* argu = Method's Class Name + < + Method's Class Extender Name if exists*/

		String[] parts = argu.toString().split("<");
		String className = parts[0];
		String classExtendsName = null;
		if (parts.length == 2)
			classExtendsName = parts[1];

		String methodType = n.f1.accept(this, className).toString();

		String methodName = n.f2.accept(this, className).toString();
		String fullMethodName = className + "." + methodName;
		/* Checking also if the method is already defined */
		Hashtable<String, MethodInfo> stMethods = st.getMethods();
		if (stMethods.get(fullMethodName) != null)
			throw new Exception("\terror: method " + methodName + " is already defined");

		String[] parTypes = null;
		String[] parNames = null;
		int totalPars = 0;
		if (n.f4.present()){
			Object parameterList = n.f4.accept(this, className);
			String[] parameters = parameterList.toString().split(",");

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
					if ((i != j) && (parNames[i].equals(parNames[j])))
						throw new Exception("\terror: variable " +  parNames[i] + " is already defined in method " + methodName);
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
			/* Checking also if the variables are already defined */
			for (int i = 0; i < totalVars; i++){
				for (int j = 0; j < totalPars; j++){
					if (varNames[i].equals(parNames[j]))
						throw new Exception("\terror: variable " +  varNames[i] + " is already defined in method " + methodName);
				}
				for (int j = 0; j < totalVars; j++){
					if ((i != j) && (varNames[i].equals(varNames[j])))
						throw new Exception("\terror: variable " +  varNames[i] + " is already defined in method " + methodName);
				}
			}
		}
		if (classExtendsName != null){
			MethodInfo extenderClassMethod = stMethods.get(classExtendsName + "." + methodName);
			if (extenderClassMethod != null){
				if (extenderClassMethod.type != methodType)
					throw new Exception("\terror: " + methodName + " in " + className + " cannot override " + methodName + " in classExtendsName");

				if (extenderClassMethod.parNames == null){
					if (totalPars != 0)
						throw new Exception("\terror: " + methodName + " in " + className + " cannot override " + methodName + " in classExtendsName");
				}
				else{
					if (totalPars != extenderClassMethod.parNames.length)
						throw new Exception("\terror: " + methodName + " in " + className + " cannot override " + methodName + " in classExtendsName");
				}
				for (int i = 0; i < totalPars; i++){
					for (int j = 0; j < extenderClassMethod.parNames.length; j++){
						if (!parTypes[i].equals(extenderClassMethod.parTypes[j]) || !parNames[i].equals(extenderClassMethod.parNames[j]))
							throw new Exception("\terror: " + methodName + " in " + className + " cannot override " + methodName + " in classExtendsName");
					}
				}
			}
		}

		/* ---------METHODS--------- */
		/* Creating the information class for the main method */
		MethodInfo currMethod = new MethodInfo();
		currMethod.addMethod(methodType, fullMethodName, className, parTypes, parNames, varTypes, varNames);
		/* Inserting this class in our Methods' Hashtable */
		// Hashtable<String, MethodInfo> stMethods = st.getMethods();
		stMethods.put(fullMethodName, currMethod);

		return fullMethodName;
	}


	/**
	* f0 -> FormalParameter()
	* f1 -> FormalParameterTail()
	*/
	public Object visit(FormalParameterList n, Object argu) throws Exception{

		Object currPar = n.f0.accept(this, argu);
		Object currParTail = n.f1.accept(this, argu);
		if (currPar != null){
			if (currParTail!=null)
				return currPar.toString() + currParTail.toString();
			return currPar.toString();
		}
		return null;
	}


	/**
	* f0 -> Type()
	* f1 -> Identifier()
	*/
	public Object visit(FormalParameter n, Object argu) throws Exception{
		return n.f0.accept(this, argu).toString() + " " + n.f1.accept(this, argu).toString();
	}


	/**
	* f0 -> ( FormalParameterTerm() )*
	*/
	public Object visit(FormalParameterTail n, Object argu) throws Exception{

		int totalPars = n.f0.size();
		String currParTail = "";
		for (int currParPos = 0; currParPos < totalPars; currParPos++)
			currParTail = currParTail + "," + n.f0.elementAt(currParPos).accept(this, argu).toString();
		if (currParTail != null)
			return currParTail;

		return null;
	}


	/**
	* f0 -> ","
	* f1 -> FormalParameter()
	*/
	public Object visit(FormalParameterTerm n, Object argu) throws Exception{
		return n.f1.accept(this, argu);
	}


	/**
	* f0 -> BooleanArrayType()
	*		| IntegerArrayType()
	*/
	public Object visit(ArrayType n, Object argu) throws Exception{
		return n.f0.accept(this, argu).toString();
	}

	/**
	* f0 -> "boolean"
	* f1 -> "["
	* f2 -> "]"
	*/
	public Object visit(BooleanArrayType n, Object argu) throws Exception{
		return n.f0.toString() + n.f1.toString() + n.f2.toString();
	}


	/**
	* f0 -> "int"
	* f1 -> "["
	* f2 -> "]"
	*/
	public Object visit(IntegerArrayType n, Object argu) throws Exception{
		return n.f0.toString() + n.f1.toString() + n.f2.toString();
	}


	/**
	* f0 -> "boolean"
	*/
	public Object visit(BooleanType n, Object argu) throws Exception{
		return n.f0.toString();
	}


	/**
	* f0 -> "int"
	*/
	public Object visit(IntegerType n, Object argu) throws Exception{
		return n.f0.toString();
	}


	/**
	* f0 -> <IDENTIFIER>
	*/
	public Object visit(Identifier n, Object argu) throws Exception{
		return n.f0.toString();
	}

}
