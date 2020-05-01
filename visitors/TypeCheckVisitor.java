package visitors;
import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.Hashtable;
import types.ClassInfo;
import types.MethodInfo;
import types.SymbolTable;

public class TypeCheckVisitor extends GJDepthFirst<Object, Object>{

	/* Creating our Symbol Table */
	public SymbolTable st;
	String currClassName;
	String currMethodName;


	public TypeCheckVisitor(SymbolTable visitor_A_st){
		st = visitor_A_st;
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

		String className = n.f1.accept(this, null).toString();
		currClassName = className;
		String fullMethodName = className + ".main";
		currMethodName = fullMethodName;

		n.f11.accept(this, null);

		if (n.f14.present()){
			int totalVars = n.f14.size();
			for (int currVarPos = 0; currVarPos < totalVars; currVarPos++){
				n.f14.elementAt(currVarPos).accept(this, fullMethodName);
			}
		}

		if (n.f15.present()){
			int totalStatements = n.f15.size();
			for (int currStatementPos = 0; currStatementPos < totalStatements; currStatementPos++){
				n.f15.elementAt(currStatementPos).accept(this, fullMethodName);
			}
		}

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

		String className = n.f1.accept(this, null).toString();
		currClassName = className;

		if (n.f3.present()){
			int totalVars = n.f3.size();
			for (int currVarPos = 0; currVarPos < totalVars; currVarPos++){
				n.f3.elementAt(currVarPos).accept(this, className);
			}
		}

		if (n.f4.present()){
			int totalMethods = n.f4.size();
			for (int currMethodPos = 0; currMethodPos < totalMethods; currMethodPos++){
				n.f4.elementAt(currMethodPos).accept(this, className);
			}
		}

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

		String className = n.f1.accept(this, null).toString();
		currClassName = className;

		String classExtendsName = n.f3.accept(this, null).toString();

		if (n.f5.present()){
			int totalVars = n.f5.size();
			for (int currVarPos = 0; currVarPos < totalVars; currVarPos++){
				n.f5.elementAt(currVarPos).accept(this, className);
			}
		}

		if (n.f6.present()){
			int totalMethods = n.f6.size();
			for (int currMethodPos = 0; currMethodPos < totalMethods; currMethodPos++){
				n.f6.elementAt(currMethodPos).accept(this, className + "<" + classExtendsName).toString();
			}
		}

		return null;
	}


	/**
	* f0 -> Type()
	* f1 -> Identifier()
	* f2 -> ";"
	*/
	public Object visit(VarDeclaration n, Object argu) throws Exception{
		n.f2.accept(this, null);
		return null;
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

		String methodType = n.f1.accept(this, null).toString();

		String methodName = n.f2.accept(this, null).toString();
		String fullMethodName = className + "." + methodName;
		currMethodName = fullMethodName;

		n.f4.accept(this, null);

		if (n.f7.present()){
			int totalVars = n.f7.size();
			for (int currVarPos = 0; currVarPos < totalVars; currVarPos++){
				n.f7.elementAt(currVarPos).accept(this, fullMethodName);
			}
		}

		if (n.f8.present()){
			int totalStatements = n.f8.size();
			for (int currStatementPos = 0; currStatementPos < totalStatements; currStatementPos++){
				n.f8.elementAt(currStatementPos).accept(this,fullMethodName);
			}
		}

		String retType = n.f10.accept(this, fullMethodName).toString();
		parts = retType.split("\\|");
		if (parts.length == 2)
			retType = parts[1];
		else if (retType.equals("this"))
			retType = currClassName;
		if (!retType.equals(methodType))
			throw new Exception("\terror: incompatible types: " + retType + " cannot be converted to " + methodType);
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
		return n.f0.accept(this, argu).toString() + " " + n.f1.accept(this, null).toString();
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
	*       | IntegerArrayType()
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
	* f0 -> "{"
	* f1 -> ( Statement() )*
	* f2 -> "}"
	*/
	public Object visit(Block n, Object argu) throws Exception{

		if (n.f1.present()){
			int totalStatements = n.f1.size();
			for (int currStatementPos = 0; currStatementPos < totalStatements; currStatementPos++){
				n.f1.elementAt(currStatementPos).accept(this, argu);
			}
		}
		return null;
	}


	/**
	* f0 -> Identifier()
	* f1 -> "="
	* f2 -> Expression()
	* f3 -> ";"
	*/
	public Object visit(AssignmentStatement n, Object argu) throws Exception{

		if (n.f2.accept(this, argu)== null)
			return null;

		String exTypeLeft = n.f0.accept(this, argu).toString();
		String exTypeRight = n.f2.accept(this, argu).toString();

		String[] partsLeft = exTypeLeft.split("\\|");
		String[] partsRight = exTypeRight.split("\\|");
		if (partsLeft.length == 2)
			exTypeLeft = partsLeft[1];
		else if (exTypeLeft.equals("this")){
			exTypeLeft = currClassName;
		}
		if (partsRight.length == 2)
			exTypeRight = partsRight[1];
		else if (exTypeRight.equals("this")){
			exTypeRight = currClassName;
		}

		if (!exTypeLeft.equals(exTypeRight)){
			String[] parentClassNames = st.getParentClassesNames(exTypeLeft);
			boolean flag = true;
			for (int currParentClassNamePos = 0; currParentClassNamePos < parentClassNames.length; currParentClassNamePos++){
				if (parentClassNames[currParentClassNamePos].equals(exTypeRight)){
					flag = false;
					break;
				}
			}
			if (flag){
				parentClassNames = st.getParentClassesNames(exTypeRight);
				for (int currParentClassNamePos = 0; currParentClassNamePos < parentClassNames.length; currParentClassNamePos++){
					if (parentClassNames[currParentClassNamePos].equals(exTypeLeft)){
						flag = false;
						break;
					}
				}
				if (flag)
					throw new Exception("\terror: incompatible types: " + exTypeRight + " cannot be converted to " + exTypeLeft);
			}
		}

		return null;
	}


	/**
	* f0 -> Identifier()
	* f1 -> "["
	* f2 -> Expression()
	* f3 -> "]"
	* f4 -> "="
	* f5 -> Expression()
	* f6 -> ";"
	*/
	public Object visit(ArrayAssignmentStatement n, Object argu) throws Exception{

		String exType = n.f0.accept(this, argu).toString();
		String[] parts = exType.split("\\|");
		if (parts.length == 2)
			exType = parts[1];
		else if (exType.equals("this")){
			exType = currClassName;
		}

		if (exType.equals("int[]")){
			exType = n.f5.accept(this, argu).toString();
			parts = exType.split("\\|");
			if (parts.length == 2)
				exType = parts[1];
			else if (exType.equals("this")){
				exType = currClassName;
			}
			if (!exType.equals("int"))
				throw new Exception("\terror: incompatible types: " + exType + " cannot be converted to int");
		}
		else if (exType.equals("boolean[]")){
			exType = n.f5.accept(this, argu).toString();
			parts = exType.split("\\|");
			if (parts.length == 2)
				exType = parts[1];
			else if (exType.equals("this")){
				exType = currClassName;
			}
			if (!exType.equals("boolean"))
				throw new Exception("\terror: incompatible types: " + exType + " cannot be converted to boolean");
		}
		else
			throw new Exception("\terror: cannot find symbol " + exType);

		exType = n.f2.accept(this, argu).toString();
		parts = exType.split("\\|");
		if (parts.length == 2)
			exType = parts[1];
		else if (exType.equals("this")){
			exType = currClassName;
		}
		if (!exType.equals("int"))
			throw new Exception("\terror: incompatible types: " + exType + " cannot be converted to int");

		return null;
	}


	/**
	* f0 -> "if"
	* f1 -> "("
	* f2 -> Expression()
	* f3 -> ")"
	* f4 -> Statement()
	* f5 -> "else"
	* f6 -> Statement()
	*/
	public Object visit(IfStatement n, Object argu) throws Exception{

		if (n.f2.accept(this, argu)== null)
			return null;

		String exType = n.f2.accept(this, argu).toString();
		String[] parts = exType.split("\\|");
		if (parts.length == 2)
			exType = parts[1];
		else if (exType.equals("this")){
			exType = currClassName;
		}
		if (!exType.equals("boolean"))
			throw new Exception("\terror: incompatible types: " + exType + " cannot be converted to boolean");

		n.f4.accept(this, argu);

		n.f6.accept(this, argu);

		return null;
	}


	/**
	* f0 -> "while"
	* f1 -> "("
	* f2 -> Expression()
	* f3 -> ")"
	* f4 -> Statement()
	*/
	public Object visit(WhileStatement n, Object argu) throws Exception{

		if (n.f2.accept(this, argu)== null)
			return null;

		String exType = n.f2.accept(this, argu).toString();
		String[] parts = exType.split("\\|");
		if (parts.length == 2)
			exType = parts[1];
		else if (exType.equals("this")){
			exType = currClassName;
		}
		if (!exType.equals("boolean"))
			throw new Exception("\terror: incompatible types: " + exType + " cannot be converted to boolean");

		n.f4.accept(this, argu);

		return null;
	}


	/**
	* f0 -> "System.out.println"
	* f1 -> "("
	* f2 -> Expression()
	* f3 -> ")"
	* f4 -> ";"
	*/
	public Object visit(PrintStatement n, Object argu) throws Exception{

		String exType = n.f2.accept(this, argu).toString();
		String[] parts = exType.split("\\|");
		if (parts.length == 2)
			exType = parts[1];
		else if (exType.equals("this")){
			exType = currClassName;
		}
		if (!exType.equals("int"))
			throw new Exception("\terror: incompatible types: " + exType + " cannot be converted to int");
		return null;
	}


	/**
	* f0 -> Clause()
	* f1 -> "&&"
	* f2 -> Clause()
	*/
	public Object visit(AndExpression n, Object argu) throws Exception{

		String clauseTypeLeft = n.f0.accept(this, argu).toString();
		String clauseTypeRight = n.f2.accept(this, argu).toString();

		String[] partsLeft = clauseTypeLeft.split("\\|");
		String[] partsRight = clauseTypeRight.split("\\|");
		if (partsLeft.length == 2)
			clauseTypeLeft = partsLeft[1];
		else if (clauseTypeLeft.equals("this")){
			clauseTypeLeft = currClassName;
		}
		if (partsRight.length == 2)
			clauseTypeRight = partsRight[1];
		else if (clauseTypeRight.equals("this")){
			clauseTypeRight = currClassName;
		}

		if (!clauseTypeLeft.equals("boolean"))
			throw new Exception("\terror: incompatible types: " + clauseTypeLeft + " cannot be converted to boolean");

		if (!clauseTypeRight.equals("boolean"))
			throw new Exception("\terror: incompatible types: " + clauseTypeRight + " cannot be converted to boolean");

		return "boolean";
	}


	/**
	* f0 -> PrimaryExpression()
	* f1 -> "<"
	* f2 -> PrimaryExpression()
	*/
	public Object visit(CompareExpression n, Object argu) throws Exception{

		String exTypeLeft = n.f0.accept(this, argu).toString();
		String exTypeRight = n.f2.accept(this, argu).toString();

		String[] partsLeft = exTypeLeft.split("\\|");
		String[] partsRight = exTypeRight.split("\\|");
		if (partsLeft.length == 2)
			exTypeLeft = partsLeft[1];
		else if (exTypeLeft.equals("this")){
			exTypeLeft = currClassName;
		}
		if (partsRight.length == 2)
			exTypeRight = partsRight[1];
		else if (exTypeRight.equals("this")){
			exTypeRight = currClassName;
		}

		if (!exTypeLeft.equals("int"))
			throw new Exception("\terror: incompatible types: " + exTypeLeft + " cannot be converted to int");

		if (!exTypeRight.equals("int"))
			throw new Exception("\terror: incompatible types: " + exTypeRight + " cannot be converted to int");

		return "boolean";
	}


	/**
	* f0 -> PrimaryExpression()
	* f1 -> "+"
	* f2 -> PrimaryExpression()
	*/
	public Object visit(PlusExpression n, Object argu) throws Exception{

		String exTypeLeft = n.f0.accept(this, argu).toString();
		String exTypeRight = n.f2.accept(this, argu).toString();

		String[] partsLeft = exTypeLeft.split("\\|");
		String[] partsRight = exTypeRight.split("\\|");
		if (partsLeft.length == 2)
			exTypeLeft = partsLeft[1];
		else if (exTypeLeft.equals("this")){
			exTypeLeft = currClassName;
		}
		if (partsRight.length == 2)
			exTypeRight = partsRight[1];
		else if (exTypeRight.equals("this")){
			exTypeRight = currClassName;
		}

		if (!exTypeLeft.equals("int"))
			throw new Exception("\terror: incompatible types: " + exTypeLeft + " cannot be converted to int");

		if (!exTypeRight.equals("int"))
			throw new Exception("\terror: incompatible types: " + exTypeRight + " cannot be converted to int");

		return "int";
	}


	/**
	* f0 -> PrimaryExpression()
	* f1 -> "-"
	* f2 -> PrimaryExpression()
	*/
	public Object visit(MinusExpression n, Object argu) throws Exception{

		String exTypeLeft = n.f0.accept(this, argu).toString();
		String exTypeRight = n.f2.accept(this, argu).toString();

		String[] partsLeft = exTypeLeft.split("\\|");
		String[] partsRight = exTypeRight.split("\\|");
		if (partsLeft.length == 2)
			exTypeLeft = partsLeft[1];
		else if (exTypeLeft.equals("this")){
			exTypeLeft = currClassName;
		}
		if (partsRight.length == 2)
			exTypeRight = partsRight[1];
		else if (exTypeRight.equals("this")){
			exTypeRight = currClassName;
		}

		if (!exTypeLeft.equals("int"))
			throw new Exception("\terror: incompatible types: " + exTypeLeft + " cannot be converted to int");

		if (!exTypeRight.equals("int"))
			throw new Exception("\terror: incompatible types: " + exTypeRight + " cannot be converted to int");

		return "int";
	}


	/**
	* f0 -> PrimaryExpression()
	* f1 -> "*"
	* f2 -> PrimaryExpression()
	*/
	public Object visit(TimesExpression n, Object argu) throws Exception{

		String exTypeLeft = n.f0.accept(this, argu).toString();
		String exTypeRight = n.f2.accept(this, argu).toString();

		String[] partsLeft = exTypeLeft.split("\\|");
		String[] partsRight = exTypeRight.split("\\|");
		if (partsLeft.length == 2)
			exTypeLeft = partsLeft[1];
		else if (exTypeLeft.equals("this")){
			exTypeLeft = currClassName;
		}
		if (partsRight.length == 2)
			exTypeRight = partsRight[1];
		else if (exTypeRight.equals("this")){
			exTypeRight = currClassName;
		}

		if (!exTypeLeft.equals("int"))
			throw new Exception("\terror: incompatible types: " + exTypeLeft + " cannot be converted to int");

		if (!exTypeRight.equals("int"))
			throw new Exception("\terror: incompatible types: " + exTypeRight + " cannot be converted to int");

		return "int";
	}


	/**
	* f0 -> PrimaryExpression()
	* f1 -> "["
	* f2 -> PrimaryExpression()
	* f3 -> "]"
	*/
	public Object visit(ArrayLookup n, Object argu) throws Exception{

		String exTypeLeft = n.f0.accept(this, argu).toString();
		String exTypeRight = n.f2.accept(this, argu).toString();

		String[] partsLeft = exTypeLeft.split("\\|");
		String[] partsRight = exTypeRight.split("\\|");
		if (partsLeft.length == 2){
			exTypeLeft = partsLeft[1];
		}
		else if (exTypeLeft.equals("this")){
			exTypeLeft = currClassName;
		}
		if (partsRight.length == 2){
			exTypeRight = partsRight[1];
		}
		else if (exTypeRight.equals("this")){
			exTypeRight = currClassName;
		}

		if (!exTypeLeft.equals("int[]"))
			throw new Exception("\terror: incompatible types: " + exTypeLeft + " cannot be converted to int[]");

		if (!exTypeRight.equals("int"))
			throw new Exception("\terror: incompatible types: " + exTypeRight + " cannot be converted to int");

		return "int";
	}


	/**
	* f0 -> PrimaryExpression()
	* f1 -> "."
	* f2 -> "length"
	*/
	public Object visit(ArrayLength n, Object argu) throws Exception{

		String exType = n.f0.accept(this, argu).toString();
		String[] parts = exType.split("\\|");
		if (parts.length == 2)
			exType = parts[1];
		else if (exType.equals("this")){
			exType = currClassName;
		}
		if ( !exType.equals("int[]") || !exType.equals("boolean[]"))
			throw new Exception("\terror: incompatible types: " + exType + " cannot be converted to int[] or boolean[]");
		return "int";
	}


	/**
	* f0 -> PrimaryExpression()
	* f1 -> "."
	* f2 -> Identifier()
	* f3 -> "("
	* f4 -> ( ExpressionList() )?
	* f5 -> ")"
	*/
	public Object visit(MessageSend n, Object argu) throws Exception{

		String exType = n.f0.accept(this, argu).toString();
		String[] parts = exType.split("\\|");
		if (parts.length == 2)
			exType = parts[1];
		else if (exType.equals("this")){
			exType = currClassName;
		}

		String idName = n.f2.accept(this, null).toString();
		if (exType.equals("int") || exType.equals("int[]") || exType.equals("boolean") || exType.equals("boolean[]"))
			throw new Exception("\terror: class expected");

		Hashtable<String, ClassInfo> stClasses = st.getClasses();
		if (stClasses.get(exType) == null)
			throw new Exception("\terror: cannot find symbol " + idName);

		Hashtable<String, MethodInfo> stMethods = st.getMethods();
		MethodInfo currMethod = stMethods.get(exType + "." + idName);
		if (currMethod == null){
			String[] parentClassNames = st.getParentClassesNames(exType);
			boolean flag = true;
			for (int currParentClassNamePos = 0; currParentClassNamePos < parentClassNames.length; currParentClassNamePos++){
				currMethod = stMethods.get(parentClassNames[currParentClassNamePos] + "." + idName);
				if (currMethod != null){
					flag = false;
					break;
				}
			}
			if (flag)
				throw new Exception("\terror: cannot find symbol " + idName);
		}

		if (n.f4.present()){
			String exList = n.f4.accept(this, argu).toString();
			parts = exList.split(",");
			int totalPars = currMethod.parNames.length;
			if (currMethod.parTypes == null){
				if (parts != null)
					throw new Exception("\terror: method " + idName + " in class " + exType + " cannot be applied to given types");
			}
			else{
				if ( (parts == null) || (parts.length != currMethod.parTypes.length))
					throw new Exception("\terror: method " + idName + " in class " + exType + " cannot be applied to given types");
			}

			for(int currExPos = 0; currExPos < parts.length; currExPos++){
				String currExType = parts[currExPos];
				String[] currParts = currExType.split("\\|");
				if (currParts.length == 2)
					currExType = currParts[1];
				else if (currExType.equals("this")){
					currExType = currClassName;
				}
				if (!currExType.equals(currMethod.parTypes[currExPos])){
					/* Then we should check if the current type has same types from inheritance */
					String[] parentClassNames = st.getParentClassesNames(currExType);
					boolean flag = true;
					for (int currParentClassNamePos = 0; currParentClassNamePos < parentClassNames.length; currParentClassNamePos++){
						if (parentClassNames[currParentClassNamePos].equals(currMethod.parTypes[currExPos]))
							flag = false;
					}
					if (flag)
						throw new Exception("\terror: incompatible types: " + currExType + " cannot be converted to " + currMethod.parTypes[currExPos]);
				}
			}
		}

		if (currMethod.type == null)
			throw new Exception("\terror: incompatible types: unexpected return value");

		return currMethod.type;
	}


	/**
	* f0 -> Expression()
	* f1 -> ExpressionTail()
	*/
	public Object visit(ExpressionList n, Object argu) throws Exception{

		Object currEx = n.f0.accept(this, argu);
		Object currExTail = n.f1.accept(this, argu);
		if (currEx != null){
			if (currExTail!=null)
				return currEx.toString() + currExTail.toString();
			return currEx.toString();
    	}
    	return null;
	}


	/**
	* f0 -> ( ExpressionTerm() )*
	*/
    public Object visit(ExpressionTail n, Object argu) throws Exception{

		int totalExprs = n.f0.size();
		String currExTail = "";
		for (int currExPos = 0; currExPos < totalExprs; currExPos++)
			currExTail = currExTail + "," + n.f0.elementAt(currExPos).accept(this, argu).toString();
		if (currExTail != null)
			return currExTail;
		return null;
   }


	/**
	* f0 -> ","
	* f1 -> Expression()
	*/
	public Object visit(ExpressionTerm n, Object argu) throws Exception{
		return n.f1.accept(this, argu);
	}


	/**
	* f0 -> <INTEGER_LITERAL>
	*/
	public Object visit(IntegerLiteral n, Object argu) throws Exception{
		return "int";
	}


	/**
	* f0 -> "true"
	*/
	public Object visit(TrueLiteral n, Object argu) throws Exception{
		return "boolean";
	}


	/**
	* f0 -> "false"
	*/
	public Object visit(FalseLiteral n, Object argu) throws Exception{
		return "boolean";
	}


	/**
	* f0 -> <IDENTIFIER>
	*/
	public String visit(Identifier n, Object argu) throws Exception{

		if (argu != null)
			return n.f0.toString() + "|" + st.getVarType(argu.toString(), n.f0.toString());
		return n.f0.toString();
	}


	/**
	* f0 -> "this"
	*/
	public Object visit(ThisExpression n, Object argu) throws Exception{
		return n.f0.toString();
	}


	/**
	* f0 -> "new"
	* f1 -> "boolean"
	* f2 -> "["
	* f3 -> Expression()
	* f4 -> "]"
	*/
	public Object visit(BooleanArrayAllocationExpression n, Object argu) throws Exception{

		String exType = n.f3.accept(this, argu).toString();
		String[] parts = exType.split("\\|");
		if (parts.length == 2)
			exType = parts[1];
		else if (exType.equals("this")){
			exType = currClassName;
		}
		if (!exType.equals("int"))
			throw new Exception("\terror: incompatible types: " + exType + " cannot be converted to int");
		return null;
	}


	/**
	* f0 -> "new"
	* f1 -> "int"
	* f2 -> "["
	* f3 -> Expression()
	* f4 -> "]"
	*/
	public Object visit(IntegerArrayAllocationExpression n, Object argu) throws Exception{

		String exType = n.f3.accept(this, argu).toString();
		String[] parts = exType.split("\\|");
		if (parts.length == 2)
			exType = parts[1];
		else if (exType.equals("this")){
			exType = currClassName;
		}
		if (!exType.equals("int"))
			throw new Exception("\terror: incompatible types: " + exType + " cannot be converted to int");
		return null;
	}


	/**
	* f0 -> "new"
	* f1 -> Identifier()
	* f2 -> "("
	* f3 -> ")"
	*/
	public Object visit(AllocationExpression n, Object argu) throws Exception{

		String idName = n.f1.accept(this, null).toString();
		Hashtable<String, ClassInfo> stClasses = st.getClasses();
		ClassInfo currClass = stClasses.get(idName);
		if (currClass == null)
			throw new Exception("\terror: cannot find symbol " + idName);

		return idName;
	}


	/**
	* f0 -> "!"
	* f1 -> Clause()
	*/
	public Object visit(NotExpression n, Object argu) throws Exception{

		if (n.f1.accept(this, argu)== null)
			return null;

		String clauseType = n.f1.accept(this, argu).toString();
		String[] parts = clauseType.split("\\|");
		if (parts.length == 2)
			clauseType = parts[1];
		else if (clauseType.equals("this")){
			clauseType = currClassName;
		}
		if (!clauseType.equals("boolean"))
			throw new Exception("\terror: incompatible types: " + clauseType + " cannot be converted to boolean");
		return "boolean";
	}


	/**
	* f0 -> "("
	* f1 -> Expression()
	* f2 -> ")"
	*/
	public Object visit(BracketExpression n, Object argu) throws Exception{
		return n.f1.accept(this, argu);
	}
}
