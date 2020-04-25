import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.Hashtable;

public class Visitor2 extends GJDepthFirst<Object, Object>{

	/* Creating our Symbol Table */
	public SymbolTable st;
	String currClassName;
	String currMethodName;

	public Visitor2(SymbolTable visitor_A_st) {
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
	public Object visit(MainClass n, Object argu) {
		System.out.println("MainClass");

		String className = n.f1.accept(this, null).toString();
		currClassName = className;


		n.f11.accept(this, null);


		if (n.f14.present()){
			int totalVars = n.f14.size();
			for (int currVarPos = 0; currVarPos < totalVars; currVarPos++){
				n.f14.elementAt(currVarPos).accept(this, className);
			}
		}


		n.f15.accept(this, argu);


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
		System.out.println("ClassDeclaration");

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
	public Object visit(ClassExtendsDeclaration n, Object argu) {
		System.out.println("ClassExtendsDeclaration");

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
	public Object visit(VarDeclaration n, Object argu){
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
	public Object visit(MethodDeclaration n, Object argu) {
		/* argu = Method's Class Name + < + Method's Class Extender Name if exists*/
		System.out.println("MethodDeclaration");

		String[] parts = argu.toString().split("<");
		String className = parts[0];
		String classExtendsName = null;
		if (parts.length == 2)
			classExtendsName = parts[1];

		String methodType = n.f1.accept(this, null).toString();


		String methodName = n.f2.accept(this, null).toString();
		currMethodName = className + "." + methodName;


		n.f4.accept(this, null);


		if (n.f7.present()){
			int totalVars = n.f7.size();
			for (int currVarPos = 0; currVarPos < totalVars; currVarPos++){
				n.f7.elementAt(currVarPos).accept(this, className);
			}
		}


		if (n.f8.present()){
			int totalStatements = n.f8.size();
			for (int currStatementPos = 0; currStatementPos < totalStatements; currStatementPos++){
				n.f8.elementAt(currStatementPos).accept(this, className + "." + methodName);
			}
		}


		String retType = n.f10.accept(this, className + "." + methodName).toString();
		parts = retType.split("\\|");
		if (parts.length == 2)
			retType = parts[1];
		else if (retType.equals("this")){
			retType = currClassName;
		}
		if (!retType.equals(methodType)){
			System.out.println("Expected " + methodType + " type @MethodDeclaration");
			System.exit(1);
		}

		return null;
	}

	/**
     * f0 -> FormalParameter()
     * f1 -> FormalParameterTail()
     */
    public Object visit(FormalParameterList n, Object argu) {

		Object currPar = n.f0.accept(this, argu);
		Object currParTail = n.f1.accept(this, argu);
		if (currPar != null){
			if(currParTail!=null)
				return currPar.toString() + " " + currParTail.toString();
			return currPar.toString();
    	}
    	return null;
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

		int totalPars = n.f0.size();
		String currParTail = "";
		for (int currParPos = 0; currParPos < totalPars; currParPos++)
			currParTail = currParTail + ", " + n.f0.elementAt(currParPos).accept(this, argu).toString();
		if(currParTail != null)
			return currParTail;

		return null;
	}

	/**
	 * f0 -> ","
	 * f1 -> FormalParameter()
	 */
	public Object visit(FormalParameterTerm n, Object argu) {
		return n.f1.accept(this, argu);
	}

	/**
	 * f0 -> BooleanArrayType()
	 *		 | IntegerArrayType()
	 */
	public Object visit(ArrayType n, Object argu) {
		System.out.println("ArrayType");
		// Can we remove this block?
		return n.f0.accept(this, argu).toString();
	}

	/**
	 * f0 -> "boolean"
	 * f1 -> "["
	 * f2 -> "]"
	 */
	public Object visit(BooleanArrayType n, Object argu) {
		System.out.println("BooleanArrayType");

		return n.f0.toString() + n.f1.toString() + n.f2.toString();
	}

	/**
	 * f0 -> "int"
	 * f1 -> "["
	 * f2 -> "]"
	 */
	public Object visit(IntegerArrayType n, Object argu) {
		System.out.println("IntegerArrayType");

		return n.f0.toString() + n.f1.toString() + n.f2.toString();
	}

	/**
	 * f0 -> "boolean"
	 */
	public Object visit(BooleanType n, Object argu) {
		System.out.println("BooleanType");

		return n.f0.toString();
	}

	/**
	 * f0 -> "int"
	 */
	public Object visit(IntegerType n, Object argu) {
		System.out.println("IntegerType");

		return n.f0.toString();
	}

	/**
	 * f0 -> "{"
	 * f1 -> ( Statement() )*
	 * f2 -> "}"
	 */
	public Object visit(Block n, Object argu) {
		System.out.println("Block");

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
	public Object visit(AssignmentStatement n, Object argu) {
		System.out.println("AssignmentStatement");

		// Remove this, it should never be null
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
			System.out.println(exTypeLeft + "!=" + exTypeRight);
			System.out.println("\tIncompatible Type @AssignmentStatement");
			System.exit(1);
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
	public Object visit(ArrayAssignmentStatement n, Object argu) {
		System.out.println("ArrayAssignmentStatement");


		String exType = n.f0.accept(this, argu).toString();
		String[] parts = exType.split("\\|");
		if (parts.length == 2)
			exType = parts[1];
		else if (exType.equals("this")){
			exType = currClassName;
		}
		if (!exType.equals("int[]")){
			System.out.println("Expected int[] type @ArrayAssignmentStatement");
			System.exit(1);
		}


		exType = n.f2.accept(this, argu).toString();
		parts = exType.split("\\|");
		if (parts.length == 2)
			exType = parts[1];
		else if (exType.equals("this")){
			exType = currClassName;
		}
		if (!exType.equals("int")){
			System.out.println("Expected int type @ArrayAssignmentStatement");
			System.exit(1);
		}


		exType = n.f5.accept(this, argu).toString();
		parts = exType.split("\\|");
		if (parts.length == 2)
			exType = parts[1];
		else if (exType.equals("this")){
			exType = currClassName;
		}
		if (!exType.equals("int")){
			System.out.println("Expected int type @ArrayAssignmentStatement");
			System.exit(1);
		}

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
	public Object visit(IfStatement n, Object argu) {
		System.out.println("IfStatement");

		// Remove this, it should never be null
		if (n.f2.accept(this, argu)== null)
			return null;

		String exType = n.f2.accept(this, argu).toString();
		String[] parts = exType.split("\\|");
		if (parts.length == 2)
			exType = parts[1];
		else if (exType.equals("this")){
			exType = currClassName;
		}

		if (!exType.equals("boolean")){
			System.out.println("Expected boolean type @IfStatement");
			System.exit(1);
		}


		return null;
	}

	/**
	 * f0 -> "while"
	 * f1 -> "("
	 * f2 -> Expression()
	 * f3 -> ")"
	 * f4 -> Statement()
	 */
	public Object visit(WhileStatement n, Object argu) {
		System.out.println("WhileStatement");

		// Remove this, it shoul never be null
		if (n.f2.accept(this, argu)== null)
			return null;

		String exType = n.f2.accept(this, argu).toString();
		String[] parts = exType.split("\\|");
		if (parts.length == 2)
			exType = parts[1];
		else if (exType.equals("this")){
			exType = currClassName;
		}

		if (!exType.equals("boolean")){
			System.out.println("Expected boolean type @WhileStatement");
			System.exit(1);
		}

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
	public Object visit(PrintStatement n, Object argu) {
		System.out.println("PrintStatement");

		// if (!n.f2.accept(this, argu).toString().equals("int") ||
		// 	!n.f2.accept(this, argu).toString().equals("boolean")){
		// 	System.out.println("Expected int/boolean type @PrintStatement");
		// 	System.exit(1);
		// }


		return null;
	}

	/**
	 * f0 -> Clause()
	 * f1 -> "&&"
	 * f2 -> Clause()
	 */
	public Object visit(AndExpression n, Object argu) {
		System.out.println("AndExpression");

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


		if (!clauseTypeLeft.equals("boolean") || !clauseTypeRight.equals("boolean")){
			System.out.println("Expected boolean type @AndExpression");
			System.exit(1);
		}


		return "boolean";
	}

	/**
	 * f0 -> PrimaryExpression()
	 * f1 -> "<"
	 * f2 -> PrimaryExpression()
	 */
	public Object visit(CompareExpression n, Object argu) {
		System.out.println("CompareExpression");

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


		if (!exTypeLeft.equals("int") || !exTypeRight.equals("int")){
			System.out.println("Expected int type @CompareExpression");
			System.exit(1);
		}


		return "boolean";
	}

	/**
	 * f0 -> PrimaryExpression()
	 * f1 -> "+"
	 * f2 -> PrimaryExpression()
	 */
	public Object visit(PlusExpression n, Object argu) {
		System.out.println("PlusExpression");

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


		if (!exTypeLeft.equals("int") || !exTypeRight.equals("int")){
			System.out.println("Expected int type @PlusExpression");
			System.exit(1);
		}


		return "int";
	}

	/**
	 * f0 -> PrimaryExpression()
	 * f1 -> "-"
	 * f2 -> PrimaryExpression()
	 */
	public Object visit(MinusExpression n, Object argu) {
		System.out.println("MinusExpression");


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


		if (!exTypeLeft.equals("int") || !exTypeRight.equals("int")){
			System.out.println("Expected int type @MinusExpression, got " + n.f0.accept(this, argu).toString());
			System.out.println("Expected int type @MinusExpression, got " + n.f2.accept(this, argu).toString());
			System.exit(1);
		}


		return "int";
	}

	/**
	 * f0 -> PrimaryExpression()
	 * f1 -> "*"
	 * f2 -> PrimaryExpression()
	 */
	public Object visit(TimesExpression n, Object argu) {
		System.out.println("TimesExpression");


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

		if (!exTypeLeft.equals("int") || !exTypeRight.equals("int")){
			System.out.println("Expected int type @TimesExpression");
			System.exit(1);
		}
		return "int";
	}

	/**
	 * f0 -> PrimaryExpression()
	 * f1 -> "["
	 * f2 -> PrimaryExpression()
	 * f3 -> "]"
	 */
	public Object visit(ArrayLookup n, Object argu) {
		System.out.println("ArrayLookup");

		String exTypeLeft = n.f0.accept(this, argu).toString();
		String exTypeRight = n.f2.accept(this, argu).toString();
		String[] partsLeft = exTypeLeft.split("\\|");
		String[] partsRight = exTypeRight.split("\\|");
		if (partsLeft.length == 2){
			exTypeLeft = partsLeft[1];
			System.out.println(partsLeft[1] + " " + partsLeft[0]);

		}
		else if (exTypeLeft.equals("this")){
			exTypeLeft = currClassName;
		}
		if (partsRight.length == 2){
			exTypeRight = partsRight[1];
			System.out.println(partsRight[1] + " " + partsRight[0]);
		}
		else if (exTypeRight.equals("this")){
			exTypeRight = currClassName;
		}

		if (!exTypeLeft.equals("int[]")){
			System.out.println("Expected int[] type @ArrayLookup");
			System.exit(1);
		}


		if (!exTypeRight.toString().equals("int")){
			System.out.println("Expected int type @ArrayLookup");
			System.exit(1);
		}


		return "int";
	}

	/**
	 * f0 -> PrimaryExpression()
	 * f1 -> "."
	 * f2 -> "length"
	 */
	public Object visit(ArrayLength n, Object argu) {
		System.out.println("ArrayLength");

		String exType = n.f0.accept(this, argu).toString();
		String[] parts = exType.split("\\|");
		if (parts.length == 2)
			exType = parts[1];
		else if (exType.equals("this")){
			exType = currClassName;
		}


		if (!n.f0.accept(this, argu).toString().equals("int[]")){
			System.out.println("Expected int[] type @ArrayLength");
			System.exit(1);
		}


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
	public Object visit(MessageSend n, Object argu) {
		System.out.println("MessageSend");

		// Fix this block
		n.f2.accept(this, null);
		return null;
	}

	/**
	 * f0 -> Expression()
	 * f1 -> ExpressionTail()
	 */
	public Object visit(ExpressionList n, Object argu) {
		System.out.println("ExpressionList");
		Object tail = n.f1.accept(this, argu);
		if (tail != null)
	 		return n.f0.accept(this, argu) + "" + tail;
		return n.f0.accept(this, argu);
	}

	/**
	 * f0 -> ","
	 * f1 -> Expression()
	 */
	public Object visit(ExpressionTerm n, Object argu) {
		System.out.println("ExpressionTerm");
		return n.f0 + " " + n.f1.accept(this, argu);
	}

	/**
	 * f0 -> <INTEGER_LITERAL>
	 */
	public Object visit(IntegerLiteral n, Object argu) {
		System.out.println("IntegerLiteral");
		return "int";
	}

	/**
	 * f0 -> "true"
	 */
	public Object visit(TrueLiteral n, Object argu) {
		return "boolean";
	}

	/**
	 * f0 -> "false"
	 */
	public Object visit(FalseLiteral n, Object argu) {
		return "boolean";
	}

	/**
	 * f0 -> <IDENTIFIER>
	 */
	public String visit(Identifier n, Object argu) {
		System.out.println("Identifier (" + n.f0.toString() +")");

		if (argu != null){
			return n.f0.toString() + "|" + st.getVarType(argu.toString(), n.f0.toString());
		}


		return n.f0.toString();
	}

	/**
	 * f0 -> "this"
	 */
	public Object visit(ThisExpression n, Object argu) {
		return n.f0.toString();
	}

	/**
	 * f0 -> "new"
	 * f1 -> "boolean"
	 * f2 -> "["
	 * f3 -> Expression()
	 * f4 -> "]"
	 */
	public Object visit(BooleanArrayAllocationExpression n, Object argu) {
		System.out.println("BooleanArrayAllocationExpression");


		if (!n.f3.accept(this, argu).toString().equals("boolean")){
			System.out.println("Expected int type @BooleanArrayAllocationExpression");
			System.exit(1);
		}


		return null;
	}

	/**
	 * f0 -> "new"
	 * f1 -> "int"
	 * f2 -> "["
	 * f3 -> Expression()
	 * f4 -> "]"
	 */
	public Object visit(IntegerArrayAllocationExpression n, Object argu) {
		System.out.println("IntegerArrayAllocationExpression");


		String exType = n.f3.accept(this, argu).toString();
		String[] parts = exType.split("\\|");
		if (parts.length == 2)
			exType = parts[1];
		else if (exType.equals("this")){
			exType = currClassName;
		}

		if (!exType.equals("int")){
			System.out.println("Expected int type @IntegerArrayAllocationExpression, got "+ exType);
			System.exit(1);
		}


		return null;
	}

	/**
	 * f0 -> "new"
	 * f1 -> Identifier()
	 * f2 -> "("
	 * f3 -> ")"
	 */
	public Object visit(AllocationExpression n, Object argu) {
		System.out.println("AllocationExpression");

		String idName = n.f1.accept(this, null).toString();
		Hashtable<String, Class> stClasses = st.getClasses();
		Class currClass = stClasses.get(idName);
		if (currClass == null){
			System.out.println("\tMissing Declaration @AllocationExpression: " + idName);
			System.exit(1);
		}
		return idName;
	}

	/**
	 * f0 -> "!"
	 * f1 -> Clause()
	 */
	public Object visit(NotExpression n, Object argu) {
		System.out.println("NotExpression");

		// Remove this, it should never be null
		if (n.f1.accept(this, argu)== null)
			return null;

		String clauseType = n.f1.accept(this, argu).toString();
		String[] parts = clauseType.split("\\|");
		if (parts.length == 2)
			clauseType = parts[1];
		else if (clauseType.equals("this")){
			clauseType = currClassName;
		}

		if (!clauseType.equals("boolean")){
			System.out.println("Expected boolean type @NotExpression");
			System.exit(1);
		}


		return "boolean";
	}

	/**
	 * f0 -> "("
	 * f1 -> Expression()
	 * f2 -> ")"
	 */
	public Object visit(BracketExpression n, Object argu) {
		System.out.println("BracketExpression");

		return n.f1.accept(this, argu);
	}
}
