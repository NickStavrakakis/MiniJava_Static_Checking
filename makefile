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

run_offset-examples:
	java Main ../TestFiles/offsets/offset-examples/BinaryTree.java \
	../TestFiles/offsets/offset-examples/BubbleSort.java \
	../TestFiles/offsets/offset-examples/Factorial.java \
	../TestFiles/offsets/offset-examples/LinearSearch.java \
	../TestFiles/offsets/offset-examples/LinkedList.java \
	../TestFiles/offsets/offset-examples/MoreThan4.java \
	../TestFiles/offsets/offset-examples/QuickSort.java \
	../TestFiles/offsets/offset-examples/TreeVisitor.java \
	../TestFiles/offsets/offset-examples/Extra/Add.java \
	../TestFiles/offsets/offset-examples/Extra/ArrayTest.java \
	../TestFiles/offsets/offset-examples/Extra/CallFromSuper.java \
	../TestFiles/offsets/offset-examples/Extra/Classes.java \
	../TestFiles/offsets/offset-examples/Extra/DerivedCall.java \
	../TestFiles/offsets/offset-examples/Extra/Example1.java \
	../TestFiles/offsets/offset-examples/Extra/FieldAndClassConflict.java \
	../TestFiles/offsets/offset-examples/Extra/Main.java \
	../TestFiles/offsets/offset-examples/Extra/ManyClasses.java \
	../TestFiles/offsets/offset-examples/Extra/OutOfBounds1.java \
	../TestFiles/offsets/offset-examples/Extra/Overload2.java \
	../TestFiles/offsets/offset-examples/Extra/ShadowBaseField.java \
	../TestFiles/offsets/offset-examples/Extra/ShadowField.java \
	../TestFiles/offsets/offset-examples/Extra/test06.java \
	../TestFiles/offsets/offset-examples/Extra/test07.java \
	../TestFiles/offsets/offset-examples/Extra/test15.java \
	../TestFiles/offsets/offset-examples/Extra/test17.java \
	../TestFiles/offsets/offset-examples/Extra/test20.java \
	../TestFiles/offsets/offset-examples/Extra/test62.java \
	../TestFiles/offsets/offset-examples/Extra/test73.java \
	../TestFiles/offsets/offset-examples/Extra/test82.java \
	../TestFiles/offsets/offset-examples/Extra/test93.java \
	../TestFiles/offsets/offset-examples/Extra/test99.java \
	../TestFiles/offsets/offset-examples/OtherExamples/PiazzaExample.java \
	../TestFiles/offsets/offset-examples/OtherExamples/SiteExample.java

build: jars compile

jars:
	java -jar lib/jtb132di.jar -te minijava/minijava.jj
	java -jar lib/javacc5.jar minijava/minijava-jtb.jj

compile:
	javac types/ClassInfo.java
	javac types/MethodInfo.java
	javac types/SymbolTable.java
	javac visitors/DefCollectVisitor.java
	javac visitors/TypeCheckVisitor.java
	javac Main.java

clean:
	find . -type f -iname \*.class -delete
	rm -f *~
	clear

commit:	clean delete
	git add -A
	git commit -m "Update"
	git push

delete:	clean
	rm TokenMgrError.* Token.* MiniJava* JavaCharStream.* ParseException.*
	rm -r visitor/ syntaxtree/
