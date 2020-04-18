import syntaxtree.*;
import visitor.*;
import java.io.*;

class Main {
    public static void main (String [] args){
		if(args.length < 1){
		    System.err.println("No File Given");
		    System.exit(1);
		}
		FileInputStream fis = null;

		for(int arg = 0; arg<args.length; arg++) {

			try{
			    fis = new FileInputStream(args[arg]);
			    MiniJavaParser parser = new MiniJavaParser(fis);
				System.err.println(args[arg] + " parsed successfully.");
			    LocateVisitor visitor = new LocateVisitor();
			    Goal root = parser.Goal();
				root.accept(visitor, null);
			    // System.out.println(root.accept(visitor, null));
			}
			catch(ParseException ex){
			    System.out.println(ex.getMessage());
			}
			catch(FileNotFoundException ex){
			    System.err.println(ex.getMessage());
			}
			finally{
			    try{
					if(fis != null) fis.close();
			    }
			    catch(IOException ex){
					System.err.println(ex.getMessage());
			    }
			}
    	}
	}
}
