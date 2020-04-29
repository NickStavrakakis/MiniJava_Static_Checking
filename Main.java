import syntaxtree.*;
import visitor.*;
import java.io.*;
import visitors.*;
import types.SymbolTable;

class Main {
    public static void main (String [] args){

		SymbolTable st = new SymbolTable();

		if(args.length < 1){
			System.err.println("No File Given");
			System.exit(1);
		}

		FileInputStream fis_A = null;
		FileInputStream fis_B = null;

		for(int arg = 0; arg<args.length; arg++) {

			try{

				String currFileName = args[arg];

			    fis_A = new FileInputStream(currFileName);
			    MiniJavaParser parser_A = new MiniJavaParser(fis_A);
				System.err.println("\n" + currFileName + " parsed successfully.");

			    DefCollectVisitor visitor_A = new DefCollectVisitor();
			    Goal root_A = parser_A.Goal();
				root_A.accept(visitor_A, null);
				st =  visitor_A.st;
				System.out.println("\tVisitor A ended successfully");

				fis_B = new FileInputStream(currFileName);
				MiniJavaParser parser_B = new MiniJavaParser(fis_B);
				TypeCheckVisitor visitor_B = new TypeCheckVisitor(st);
				Goal root_B = parser_B.Goal();
				root_B.accept(visitor_B, null);
				System.out.println("\tVisitor B ended successfully");

			}
			catch(ParseException ex){
				System.out.println(ex.getMessage());
			}
			catch(FileNotFoundException ex){
				System.err.println(ex.getMessage());
			}
			catch(Exception ex){
				System.err.println("\u001B[31m" + ex.getMessage() + "\u001B[0m");
			}
			finally{
				try{
					if(fis_A != null) fis_A.close();
					if(fis_B != null) fis_B.close();
				}
				catch(IOException ex){
					System.err.println(ex.getMessage());
				}
			}
		}
	}
}
