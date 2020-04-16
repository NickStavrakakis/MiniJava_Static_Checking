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

		String idName = n.f11.accept(this, argu).toString();
		if (n.f14 != null){

			int totalVars = n.f14.size();
			String[] varName = new String[totalVars];
			String[] varType = new String[totalVars];

			for (int currVarPos = 0; currVarPos < totalVars; currVarPos++){
				String[] currVar = (n.f14.elementAt(currVarPos).accept(this, argu).toString()).split(" ");
				varName[currVarPos] = currVar[0];
				varType[currVarPos] = currVar[1];
			}
		}

		Classes currClass = new Classes();
		currClass.addClass(className, "main");

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
       n.f2.accept(this, argu);
       n.f3.accept(this, argu);
       n.f4.accept(this, argu);
       n.f5.accept(this, argu);
       return _ret;
    }

}
