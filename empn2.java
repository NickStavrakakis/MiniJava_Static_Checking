/**
* f0 -> PrimaryExpression()
* f1 -> "."
* f2 -> Identifier()
* f3 -> "("
* f4 -> ( ExpressionList() )?
* f5 -> ")"
*/
public String visit(MessageSend n, String[] argu) throws Exception {

	String primExprType = n.f0.accept(this, argu);

	String idName = n.f2.accept(this, argu);

	String argumentTypeList,className;

	if( n.f4.present() ){
		argumentTypeList = n.f4.accept(this, argu);
	}
	else{
		argumentTypeList = null;
	}

	if( SymbolTable.isBasicType(primExprType) ){
		throw new TypeCheckingException("Error! At method " + argu[1] + " the type of PrimaryExpression in MessageSend left to . is "
		+ primExprType + " . It should be this or className , so this MessageSend is invalid!");
	}

	if( SymbolTable.isClassName(primExprType) ){
		className = primExprType;
	}
	else{
		throw new TypeCheckingException("Error! At method " + argu[1] + " the type of PrimaryExpression in MessageSend left to . is "
		+ primExprType + " . It should be this or className , so this MessageSend is invalid!");
	}

	TypeInfo tempMethod;

	tempMethod = new MethodInfo(null,idName);

	if( argumentTypeList != null ){

		FieldInfo tempParam;
		String difName;
		if( argumentTypeList.contains(",") ){

			String[] argumentTypes = argumentTypeList.split(",");

			for( int i=0; i<argumentTypes.length; i++ ){
				difName = "a"+i;		/* We need to give different name as we have hashing */
				tempParam = new FieldInfo(argumentTypes[i],difName);
				tempMethod.newParameter(tempParam);
			}

		}
		else{ /* We have only one argument */
			tempParam = new FieldInfo(argumentTypeList,"a");
			tempMethod.newParameter(tempParam);
		}
	}

	String returnType = SymbolTable.classes.get(className).isMethodCallIn(tempMethod);

	if( returnType == null ){
		throw new TypeCheckingException("Error! At method " + argu[1] + " in MessageSend right to . method " + idName +" has wrong arguments or it is undefined."
		+ " So this MessageSend is invalid!");
	}

	return returnType;

}

/**
* f0 -> "System.out.println"
* f1 -> "("
* f2 -> Expression()
* f3 -> ")"
* f4 -> ";"
*/
public String visit(PrintStatement n, String[] argu) throws Exception {

	String _ret=null;

	String exprType = n.f2.accept(this, argu);

	if( !exprType.equals("int") ){
		throw new TypeCheckingException("Error! At method " + argu[1] + " the type of Expression in PrintStatement is "
		+ exprType + " . It should be int , so this PrintStatement is invalid!");
	}

	return _ret;

}
