# simplify that before sendind the project

all: compile

run_all:
	java Main ../TestFiles/BinaryTree.java \
	../TestFiles/BubbleSort.java ../TestFiles/BubbleSort-error.java \
	../TestFiles/Factorial.java ../TestFiles/Factorial-error.java \
	../TestFiles/LinearSearch.java ../TestFiles/LinearSearch-error.java  \
	../TestFiles/LinkedList.java ../TestFiles/LinkedList-error.java \
	../TestFiles/MoreThan4.java ../TestFiles/MoreThan4-error.java \
	../TestFiles/QuickSort.java ../TestFiles/QuickSort-error.java \
	../TestFiles/TreeVisitor.java ../TestFiles/TreeVisitor-error.java

run_minijava-extra:
	java Main ../TestFiles/minijava-extra/Add.java \
	 ../TestFiles/minijava-extra/ArrayTest.java \
	 ../TestFiles/minijava-extra/CallFromSuper.java \
	 ../TestFiles/minijava-extra/Classes.java \
	 ../TestFiles/minijava-extra/DerivedCall.java \
	 ../TestFiles/minijava-extra/Example1.java \
	 ../TestFiles/minijava-extra/FieldAndClassConflict.java \
	 ../TestFiles/minijava-extra/Main.java \
	 ../TestFiles/minijava-extra/ManyClasses.java \
	 ../TestFiles/minijava-extra/OutOfBounds1.java \
	 ../TestFiles/minijava-extra/Overload2.java \
	 ../TestFiles/minijava-extra/ShadowBaseField.java \
	 ../TestFiles/minijava-extra/ShadowField.java \
	 ../TestFiles/minijava-extra/test06.java \
	 ../TestFiles/minijava-extra/test07.java \
	 ../TestFiles/minijava-extra/test15.java \
	 ../TestFiles/minijava-extra/test17.java \
	 ../TestFiles/minijava-extra/test20.java \
	 ../TestFiles/minijava-extra/test62.java \
	 ../TestFiles/minijava-extra/test73.java \
	 ../TestFiles/minijava-extra/test82.java \
	 ../TestFiles/minijava-extra/test93.java \
	 ../TestFiles/minijava-extra/test99.java

run_minijava-error-extra:
	java Main ../TestFiles/minijava-error-extra/BadAssign2.java \
	../TestFiles/minijava-error-extra/BadAssign.java \
	../TestFiles/minijava-error-extra/Classes-error.java \
	../TestFiles/minijava-error-extra/DoubleDeclaration1.java \
	../TestFiles/minijava-error-extra/DoubleDeclaration4.java \
	../TestFiles/minijava-error-extra/DoubleDeclaration6.java \
	../TestFiles/minijava-error-extra/NoMatchingMethod.java \
	../TestFiles/minijava-error-extra/NoMethod.java \
	../TestFiles/minijava-error-extra/Overload1.java \
	../TestFiles/minijava-error-extra/test18.java \
	../TestFiles/minijava-error-extra/test21.java \
	../TestFiles/minijava-error-extra/test35.java \
	../TestFiles/minijava-error-extra/test52.java \
	../TestFiles/minijava-error-extra/test68.java \
	../TestFiles/minijava-error-extra/UseArgs.java

build: jars compile

jars:
	java -jar lib/jtb132di.jar -te minijava/minijava.jj
	java -jar lib/javacc5.jar minijava/minijava-jtb.jj

compile:
	javac types/Class.java
	javac types/Method.java
	javac types/SymbolTable.java
	javac visitors/DefCollectVisitor.java
	javac visitors/TypeCheckVisitor.java
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
