# simplify that before sendind the project

all: compile

runall:
	java Main ../TestFiles/BinaryTree.java \
	../TestFiles/BubbleSort.java ../TestFiles/BubbleSort-error.java \
	../TestFiles/Factorial.java ../TestFiles/Factorial-error.java \
	../TestFiles/LinearSearch.java ../TestFiles/LinearSearch-error.java  \
	../TestFiles/LinkedList.java ../TestFiles/LinkedList-error.java \
	../TestFiles/MoreThan4.java ../TestFiles/MoreThan4-error.java \
	../TestFiles/QuickSort.java ../TestFiles/QuickSort-error.java \
	../TestFiles/TreeVisitor.java ../TestFiles/TreeVisitor-error.java

build: jars compile

jars:
	java -jar lib/jtb132di.jar -te minijava/minijava.jj
	java -jar lib/javacc5.jar minijava/minijava-jtb.jj

compile:
	javac Class.java
	javac Method.java
	javac SymbolTable.java
	javac LocateVisitor.java
	javac Visitor2.java
	javac Main.java

clean:
	rm -f *.class *~
	clear

commit:	clean delete
	git add -A
	git commit -m "Update"
	git push

delete:	clean
	rm TokenMgrError.* Token.* MiniJava* JavaCharStream.* ParseException.*
	rm -r visitor/ syntaxtree/
