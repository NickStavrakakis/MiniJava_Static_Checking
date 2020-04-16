import syntaxtree.*;
import visitor.GJDepthFirst;

public class LocateVisitor extends GJDepthFirst<Object, Object>{


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
		if (n.f14 != null){
			int totalVars = n.f14.size();
			varTypes = new String[totalVars];
			varNames = new String[totalVars];
			for (int currVarPos = 0; currVarPos < totalVars; currVarPos++){
				String[] currVar = (n.f14.elementAt(currVarPos).accept(this, argu).toString()).split(" ");
				varTypes[currVarPos] = currVar[0];
				varNames[currVarPos] = currVar[1];
			}
		}

		Classes currClass = new Classes();
		String[] mainMethod = new String[1];
		mainMethod[0] = "main";
		currClass.addClass(className, mainMethod, null, null);

		Methods currMethod = new Methods();
		currMethod.addMethod(null, "main", className, methodIdTypes, methodIdNames, varTypes, varNames);
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
    public Object visit(ClassDeclaration n, Object argu) {

		String className = n.f1.accept(this, argu).toString();
		// maybe we should check for multiple declarations

		String[] varTypes = null;
		String[] varNames = null;
		if (n.f3 != null){

			int totalVars = n.f3.size();
			varTypes = new String[totalVars];
			varNames = new String[totalVars];

			for (int currVarPos = 0; currVarPos < totalVars; currVarPos++){
				String[] currVar = (n.f3.elementAt(currVarPos).accept(this, argu).toString()).split(" ");
				varTypes[currVarPos] = currVar[0];
				varNames[currVarPos] = currVar[1];
			}
		}
		// maybe we should check for multiple definitions

		String[] methods = null;
		if (n.f4 != null){

			int totalMethods = n.f4.size();
			methods = new String[totalMethods];
			for (int currMethodPos = 0; currMethodPos < totalMethods; currMethodPos++){
			   methods[currMethodPos] =  n.f4.elementAt(currMethodPos).accept(this, className).toString();
			}
		}
		Classes currClass = new Classes();
		currClass.addClass(className, methods, varTypes, varNames);
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
		String classExtendsName = n.f3.accept(this, argu).toString();

		String[] varTypes = null;
		String[] varNames = null;
		if (n.f5 != null){

			int totalVars = n.f5.size();
			varTypes = new String[totalVars];
			varNames = new String[totalVars];

			for (int currVarPos = 0; currVarPos < totalVars; currVarPos++){
				String[] currVar = (n.f5.elementAt(currVarPos).accept(this, argu).toString()).split(" ");
				varTypes[currVarPos] = currVar[0];
				varNames[currVarPos] = currVar[1];
			}
		}
		// maybe we should check for multiple definitions

		String[] methods = null;
		if (n.f6 != null){

			int totalMethods = n.f6.size();
			methods = new String[totalMethods];

			for (int currMethodPos = 0; currMethodPos < totalMethods; currMethodPos++){
			   //later replace className with "this"
			   methods[currMethodPos] =  n.f6.elementAt(currMethodPos).accept(this, className).toString();
			}
		}
		Classes currClass = new Classes();
		currClass.addClass(className, methods, varTypes, varNames);

		return null;
    }

	/**
	* f0 -> <IDENTIFIER>
	*/
	public Object visit(Identifier n, Object argu) {
		return n.f0.toString();
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
     * f0 -> ArrayType()
     *       | BooleanType()
     *       | IntegerType()
     *       | Identifier()
     */
    public Object visit(Type n, Object argu) {
   		return n.f0.accept(this, argu);
    }

    /**
     * f0 -> BooleanArrayType()
     *       | IntegerArrayType()
     */
    public Object visit(ArrayType n, Object argu) {
       return n.f0.accept(this, argu);
    }

    /**
     * f0 -> "boolean"
     * f1 -> "["
     * f2 -> "]"
     */
    public Object visit(BooleanArrayType n, Object argu) {
		return n.f0.toString()+n.f1.toString()+n.f1.toString();
    }

    /**
     * f0 -> "int"
     * f1 -> "["
     * f2 -> "]"
     */
    public Object visit(IntegerArrayType n, Object argu) {
		return n.f0.toString()+n.f1.toString()+n.f1.toString();
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

		String methodType = n.f1.accept(this, argu).toString();
		String methodName = n.f2.accept(this, argu).toString();
		String[] parTypes = null;
		String[] parNames = null;

		Object parameterList = n.f4.accept(this, argu);
		if(parameterList != null){

			String[] parameters = parameterList.toString().split(", ");
			int totalPars = parameters.length;
			parTypes = new String[totalPars];
			parNames = new String[totalPars];

			for (int currParPos = 0; currParPos < totalPars; currParPos++){
				String[] currPar = parameters[currParPos].split(" ");
				parTypes[currParPos] = currPar[0];
				parNames[currParPos] = currPar[1];
			}
		}

		n.f7.accept(this, argu);

		String[] varTypes = null;
		String[] varNames = null;
		if (n.f7 != null){

			int totalVars = n.f7.size();
			varTypes = new String[totalVars];
			varNames = new String[totalVars];

			for (int currVarPos = 0; currVarPos < totalVars; currVarPos++){
				String[] currVar = (n.f7.elementAt(currVarPos).accept(this, argu).toString()).split(" ");
				varTypes[currVarPos] = currVar[0];
				varNames[currVarPos] = currVar[1];
			}
		}
		// maybe we should check for multiple definitions

		Methods currMethod = new Methods();
		currMethod.addMethod(methodType, methodName, argu.toString(), parTypes, parNames, varTypes, varNames);



		n.f8.accept(this, argu);
		n.f10.accept(this, argu);


		return methodName;
	}

    /**
     * f0 -> FormalParameter()
     * f1 -> FormalParameterTail()
     */
    public Object visit(FormalParameterList n, Object argu) {

		Object tail = n.f1.accept(this, argu);
		if (tail != null){
			return n.f0.accept(this, argu) + "" + tail;
		}
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
     * f0 -> ( FormalParameterTerm() )*
     */
    public Object visit(FormalParameterTail n, Object argu) {
       return n.f0.accept(this, argu);
    }

    /**
     * f0 -> ","
     * f1 -> FormalParameter()
     */
    public Object visit(FormalParameterTerm n, Object argu) {
		return n.f0 + " " + n.f1.accept(this, argu);
    }

}
