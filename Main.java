import syntaxtree.*;
import visitor.*;
import java.io.*;

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
				System.err.println(currFileName + " parsed successfully.");
			    LocateVisitor visitor_A = new LocateVisitor();
			    Goal root_A = parser_A.Goal();
				root_A.accept(visitor_A, null);
				st =  visitor_A.st;
				System.out.println("Visitor A ended successfully");

				fis_B = new FileInputStream(currFileName);
				MiniJavaParser parser_B = new MiniJavaParser(fis_B);
				System.err.println(currFileName + " parsed successfully.");
				Visitor2 visitor_B = new Visitor2(st);
				Goal root_B = parser_B.Goal();
				root_B.accept(visitor_B, null);
				System.out.println("Visitor B ended successfully");

			}
			catch(ParseException ex){
			    System.out.println(ex.getMessage());
			}
			catch(FileNotFoundException ex){
			    System.err.println(ex.getMessage());
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
