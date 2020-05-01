all: compile

compile:
	java -jar lib/jtb132di.jar -te minijava/minijava.jj
	java -jar lib/javacc5.jar minijava/minijava-jtb.jj
	javac types/ClassInfo.java
	javac types/MethodInfo.java
	javac types/SymbolTable.java
	javac visitors/DefCollectVisitor.java
	javac visitors/TypeCheckVisitor.java
	javac Main.java

clean:
	find . -type f -iname \*.class -delete
	rm -f *~
	rm -r visitor/ syntaxtree/
	rm TokenMgrError.* Token.* MiniJava* JavaCharStream.* ParseException.*
	clear
