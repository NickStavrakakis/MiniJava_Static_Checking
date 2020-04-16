all: compile

compile:
	java -jar lib/jtb132di.jar minijava/minijava.jj
	java -jar lib/javacc5.jar minijava/minijava-jtb.jj

run:
	javac Main.java
	javac LocateVisitor.java
	java Main ../TestFiles/BinaryTree.java

clean:
	rm -f *.class *~
