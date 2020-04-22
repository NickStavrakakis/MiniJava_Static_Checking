import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.Hashtable;

public class Visitor2 extends GJDepthFirst<Object, Object>{

	/* Creating our Symbol Table */
	public SymbolTable st;

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
	 //done
	public Object visit(MainClass n, Object argu) {

		//System.out.println("MainClass (argu = " + argu + ")");
		String className = n.f1.accept(this, argu).toString();
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

		//System.out.println("ClassDeclaration (argu = " + argu + ")");

		String className = n.f1.accept(this, argu).toString();

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

		//System.out.println("ClassExtendsDeclaration (argu = " + argu + ")");

		String className = n.f1.accept(this, argu).toString();
		String classExtendsName = n.f3.accept(this, argu).toString();

	    if (n.f6.present()){
	  	  int totalMethods = n.f6.size();
	  	  for (int currMethodPos = 0; currMethodPos < totalMethods; currMethodPos++){
	  		 n.f6.elementAt(currMethodPos).accept(this, className + "<" + classExtendsName).toString();
	  	  }
	    }
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

		//System.out.println("MethodDeclaration (argu = " + argu + ")");
		/* argu = Method's Class Name + < + Method's Class Extender Name if exists*/

		String[] parts = argu.toString().split("<");
		String className = parts[0];
		String classExtendsName = null;
		if (parts.length == 2)
			classExtendsName = parts[1];

		String methodType = n.f1.accept(this, className).toString();
	    String methodName = n.f2.accept(this, className).toString();

		n.f4.accept(this, className);


		if (n.f7.present()){
		   int totalVars = n.f7.size();
		   for (int currVarPos = 0; currVarPos < totalVars; currVarPos++){
			   n.f7.elementAt(currVarPos).accept(this, className);
		   }
		}
		n.f8.accept(this, className + "." + methodName);
		//n.f10.accept(this, className + "." + methodName);
		return null;
	}


	/**
	 * f0 -> ArrayType()
	 *		 | BooleanType()
	 *		 | IntegerType()
	 *		 | Identifier()
	 */
	public Object visit(Type n, Object argu) {

		//System.out.println("Type (argu = " + argu + ")");

		String currType = n.f0.accept(this, argu).toString();
		if (!( currType.equals("boolean[]") || currType.equals("int[]") || currType.equals("boolean") || currType.equals("int"))){
			Hashtable<String, Class> stClasses = st.getClasses();
			if (stClasses.get(currType) == null){
				System.out.println("\tMissing Declaration: " + currType);
				System.exit(1);
			}
		}

		return currType;
	}

	/**
	 * f0 -> BooleanArrayType()
	 *		 | IntegerArrayType()
	 */
	public Object visit(ArrayType n, Object argu) {

		//System.out.println("ArrayType (argu = " + argu + ")");
		return n.f0.accept(this, argu).toString();
	}

	/**
	 * f0 -> "boolean"
	 * f1 -> "["
	 * f2 -> "]"
	 */
	public Object visit(BooleanArrayType n, Object argu) {

		//System.out.println("BooleanArrayType (argu = " + argu + ")");
		return n.f0.toString() + n.f1.toString() + n.f2.toString();
	}

	/**
	 * f0 -> "int"
	 * f1 -> "["
	 * f2 -> "]"
	 */
	public Object visit(IntegerArrayType n, Object argu) {

		//System.out.println("IntegerArrayType (argu = " + argu + ")");
		return n.f0.toString() + n.f1.toString() + n.f2.toString();
	}

	/**
	 * f0 -> "boolean"
	 */
	public Object visit(BooleanType n, Object argu) {

		//System.out.println("BooleanType (argu = " + argu + ")");
		return n.f0.toString();
	}

	/**
	 * f0 -> "int"
	 */
	public Object visit(IntegerType n, Object argu) {

		//System.out.println("IntegerType (argu = " + argu + ")");
		return n.f0.toString();
	}

	/**
	 * f0 -> Block()
	 *		 | AssignmentStatement()
	 *		 | ArrayAssignmentStatement()
	 *		 | IfStatement()
	 *		 | WhileStatement()
	 *		 | PrintStatement()
	 */
	public Object visit(Statement n, Object argu) {

		//System.out.println("Statement (argu = " + argu + ")");
		return n.f0.accept(this, argu);
	}

	/**
	 * f0 -> "{"
	 * f1 -> ( Statement() )*
	 * f2 -> "}"
	 */
	public Object visit(Block n, Object argu) {

		//System.out.println("Block (argu = " + argu + ")");
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return null;
	}

	/**
	 * f0 -> Identifier()
	 * f1 -> "="
	 * f2 -> Expression()
	 * f3 -> ";"
	 */
	public Object visit(AssignmentStatement n, Object argu) {

		//System.out.println("AssignmentStatement (argu = " + argu + ")");

		if (argu != null){ //remove this later
			String[] parts = argu.toString().split("\\.");
	  	   	String className = parts[0];
	 	   	String methodName = parts[1];

	 		String IdType = null;

			String IdName =	n.f0.accept(this, argu).toString();
			Hashtable<String, Method> stMethod = st.getMethods();

			Method currMethod = stMethod.get(argu);
			if (currMethod != null){
				if(currMethod.varNames!=null){
					for (int currVarPos = 0; currVarPos < currMethod.varNames.length; currVarPos++){
						if (IdName.equals(currMethod.varNames[currVarPos])){
							IdType = currMethod.varTypes[currVarPos];
						}
					}
				}
			}
			n.f2.accept(this, IdType);
		}
		else{
			n.f0.accept(this, argu);
			n.f2.accept(this, argu);
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

		//System.out.println("ArrayAssignmentStatement (argu = " + argu + ")");

		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		n.f3.accept(this, argu);
		n.f4.accept(this, argu);
		n.f5.accept(this, argu);
		n.f6.accept(this, argu);
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

		//System.out.println("IfStatement (argu = " + argu + ")");

		n.f2.accept(this, "boolean");
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
	public Object visit(WhileStatement n, Object argu) {

		System.out.println("WhileStatement (argu = " + argu + ")");

		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		n.f3.accept(this, argu);
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

		System.out.println("PrintStatement (argu = " + argu + ")");

		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		n.f3.accept(this, argu);
		n.f4.accept(this, argu);
		return null;
	}

	/**
	 * f0 -> AndExpression()
	 *		 | CompareExpression()
	 *		 | PlusExpression()
	 *		 | MinusExpression()
	 *		 | TimesExpression()
	 *		 | ArrayLookup()
	 *		 | ArrayLength()
	 *		 | MessageSend()
	 *		 | Clause()
	 */
	public Object visit(Expression n, Object argu) {

		System.out.println("Expression (argu = " + argu + ")");
		return n.f0.accept(this, argu);
	}

	/**
	 * f0 -> Clause()
	 * f1 -> "&&"
	 * f2 -> Clause()
	 */
	public Object visit(AndExpression n, Object argu) {

		//System.out.println("AndExpression (argu = " + argu + ")");

		if (argu != null){ //remove this later
			String IdType = argu.toString();
			System.out.println(IdType);
			if (!IdType.equals("boolean")){
				System.out.println("\tIncompatible Type in Assignment: " + IdType);
				System.exit(1);
			}
		}
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return null;
	}

	/**
	 * f0 -> PrimaryExpression()
	 * f1 -> "<"
	 * f2 -> PrimaryExpression()
	 */
	public Object visit(CompareExpression n, Object argu) {

		//System.out.println("CompareExpression (argu = " + argu + ")");

		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return null;
	}

	/**
	 * f0 -> PrimaryExpression()
	 * f1 -> "+"
	 * f2 -> PrimaryExpression()
	 */
	public Object visit(PlusExpression n, Object argu) {

		//System.out.println("PlusExpression (argu = " + argu + ")");

		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return null;
	}

	/**
	 * f0 -> PrimaryExpression()
	 * f1 -> "-"
	 * f2 -> PrimaryExpression()
	 */
	public Object visit(MinusExpression n, Object argu) {

		//System.out.println("MinusExpression (argu = " + argu + ")");

		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return null;
	}

	/**
	 * f0 -> PrimaryExpression()
	 * f1 -> "*"
	 * f2 -> PrimaryExpression()
	 */
	public Object visit(TimesExpression n, Object argu) {

		//System.out.println("TimesExpression (argu = " + argu + ")");

		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return null;
	}

	/**
	 * f0 -> PrimaryExpression()
	 * f1 -> "["
	 * f2 -> PrimaryExpression()
	 * f3 -> "]"
	 */
	public Object visit(ArrayLookup n, Object argu) {

		//System.out.println("ArrayLookup (argu = " + argu + ")");

		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		n.f3.accept(this, argu);
		return null;
	}

	/**
	 * f0 -> PrimaryExpression()
	 * f1 -> "."
	 * f2 -> "length"
	 */
	public Object visit(ArrayLength n, Object argu) {

		//System.out.println("ArrayLength (argu = " + argu + ")");

		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return null;
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

		//System.out.println("MessageSend (argu = " + argu + ")");

		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		n.f3.accept(this, argu);
		n.f4.accept(this, argu);
		n.f5.accept(this, argu);
		return null;
	}

	/**
	 * f0 -> Expression()
	 * f1 -> ExpressionTail()
	 */
	public Object visit(ExpressionList n, Object argu) {

		//System.out.println("ExpressionList (argu = " + argu + ")");

		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		return null;
	}

	/**
	 * f0 -> ( ExpressionTerm() )*
	 */
	public Object visit(ExpressionTail n, Object argu) {

		//System.out.println("ExpressionTail (argu = " + argu + ")");
		return n.f0.accept(this, argu);
	}

	/**
	 * f0 -> ","
	 * f1 -> Expression()
	 */
	public Object visit(ExpressionTerm n, Object argu) {

		//System.out.println("ExpressionTerm (argu = " + argu + ")");

		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		return null;
	}

	/**
	 * f0 -> NotExpression()
	 *		 | PrimaryExpression()
	 */
	public Object visit(Clause n, Object argu) {

		//System.out.println("Clause (argu = " + argu + ")");
		return n.f0.accept(this, argu);
	}

	/**
	 * f0 -> IntegerLiteral()
	 *		 | TrueLiteral()
	 *		 | FalseLiteral()
	 *		 | Identifier()
	 *		 | ThisExpression()
	 *		 | ArrayAllocationExpression()
	 *		 | AllocationExpression()
	 *		 | BracketExpression()
	 */
	public Object visit(PrimaryExpression n, Object argu) {

		//System.out.println("PrimaryExpression (argu = " + argu + ")");
		return n.f0.accept(this, argu);
	}

	/**
	 * f0 -> <INTEGER_LITERAL>
	 */
	public Object visit(IntegerLiteral n, Object argu) {

		//System.out.println("IntegerLiteral (argu = " + argu + ")");
		return n.f0.accept(this, argu);
	}

	/**
	 * f0 -> "true"
	 */
	public Object visit(TrueLiteral n, Object argu) {

		//System.out.println("TrueLiteral (argu = " + argu + ")");
		return n.f0.accept(this, argu);
	}

	/**
	 * f0 -> "false"
	 */
	public Object visit(FalseLiteral n, Object argu) {

		//System.out.println("FalseLiteral (argu = " + argu + ")");
		return n.f0.accept(this, argu);
	}

	/**
	 * f0 -> <IDENTIFIER>
	 */
	public Object visit(Identifier n, Object argu) {

		//System.out.println("Identifier (argu = " + argu + ")");
		return n.f0.toString();
	}

	/**
	 * f0 -> "this"
	 */
	public Object visit(ThisExpression n, Object argu) {

		//System.out.println("ThisExpression (argu = " + argu + ")");
		return n.f0.accept(this, argu);
	}

	/**
	 * f0 -> BooleanArrayAllocationExpression()
	 *		 | IntegerArrayAllocationExpression()
	 */
	public Object visit(ArrayAllocationExpression n, Object argu) {

		//System.out.println("ArrayAllocationExpression (argu = " + argu + ")");
		return n.f0.accept(this, argu);
	}

	/**
	 * f0 -> "new"
	 * f1 -> "boolean"
	 * f2 -> "["
	 * f3 -> Expression()
	 * f4 -> "]"
	 */
	public Object visit(BooleanArrayAllocationExpression n, Object argu) {

		//System.out.println("BooleanArrayAllocationExpression (argu = " + argu + ")");

		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		n.f3.accept(this, argu);
		n.f4.accept(this, argu);
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

		//System.out.println("AssignmentStatement (argu = " + argu + ")");

		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		n.f3.accept(this, argu);
		n.f4.accept(this, argu);
		return null;
	}

	/**
	 * f0 -> "new"
	 * f1 -> Identifier()
	 * f2 -> "("
	 * f3 -> ")"
	 */
	public Object visit(AllocationExpression n, Object argu) {

		//System.out.println("AssignmentStatement (argu = " + argu + ")");

		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		n.f3.accept(this, argu);
		return null;
	}

	/**
	 * f0 -> "!"
	 * f1 -> Clause()
	 */
	public Object visit(NotExpression n, Object argu) {

		//System.out.println("AssignmentStatement (argu = " + argu + ")");

		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		return null;
	}

	/**
	 * f0 -> "("
	 * f1 -> Expression()
	 * f2 -> ")"
	 */
	public Object visit(BracketExpression n, Object argu) {

		//System.out.println("BracketExpression: " + argu);

		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return null;
	}

}
