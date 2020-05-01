# simplify that before sendind the project

all: compile

run_all:
	java Main test_files/BinaryTree.java \
	test_files/BubbleSort.java test_files/BubbleSort-error.java \
	test_files/Factorial.java test_files/Factorial-error.java \
	test_files/LinearSearch.java test_files/LinearSearch-error.java  \
	test_files/LinkedList.java test_files/LinkedList-error.java \
	test_files/MoreThan4.java test_files/MoreThan4-error.java \
	test_files/QuickSort.java test_files/QuickSort-error.java \
	test_files/TreeVisitor.java test_files/TreeVisitor-error.java

run_minijava-extra:
	java Main test_files/minijava-extra/Add.java \
	 test_files/minijava-extra/ArrayTest.java \
	 test_files/minijava-extra/CallFromSuper.java \
	 test_files/minijava-extra/Classes.java \
	 test_files/minijava-extra/DerivedCall.java \
	 test_files/minijava-extra/Example1.java \
	 test_files/minijava-extra/FieldAndClassConflict.java \
	 test_files/minijava-extra/Main.java \
	 test_files/minijava-extra/ManyClasses.java \
	 test_files/minijava-extra/OutOfBounds1.java \
	 test_files/minijava-extra/Overload2.java \
	 test_files/minijava-extra/ShadowBaseField.java \
	 test_files/minijava-extra/ShadowField.java \
	 test_files/minijava-extra/test06.java \
	 test_files/minijava-extra/test07.java \
	 test_files/minijava-extra/test15.java \
	 test_files/minijava-extra/test17.java \
	 test_files/minijava-extra/test20.java \
	 test_files/minijava-extra/test62.java \
	 test_files/minijava-extra/test73.java \
	 test_files/minijava-extra/test82.java \
	 test_files/minijava-extra/test93.java \
	 test_files/minijava-extra/test99.java

run_minijava-error-extra:
	java Main test_files/minijava-error-extra/BadAssign2.java \
	test_files/minijava-error-extra/BadAssign.java \
	test_files/minijava-error-extra/Classes-error.java \
	test_files/minijava-error-extra/DoubleDeclaration1.java \
	test_files/minijava-error-extra/DoubleDeclaration4.java \
	test_files/minijava-error-extra/DoubleDeclaration6.java \
	test_files/minijava-error-extra/NoMatchingMethod.java \
	test_files/minijava-error-extra/NoMethod.java \
	test_files/minijava-error-extra/Overload1.java \
	test_files/minijava-error-extra/test18.java \
	test_files/minijava-error-extra/test21.java \
	test_files/minijava-error-extra/test35.java \
	test_files/minijava-error-extra/test52.java \
	test_files/minijava-error-extra/test68.java \
	test_files/minijava-error-extra/UseArgs.java

run_offset-examples:
	java Main test_files/offsets/offset-examples/BinaryTree.java \
	test_files/offsets/offset-examples/BubbleSort.java \
	test_files/offsets/offset-examples/Factorial.java \
	test_files/offsets/offset-examples/LinearSearch.java \
	test_files/offsets/offset-examples/LinkedList.java \
	test_files/offsets/offset-examples/MoreThan4.java \
	test_files/offsets/offset-examples/QuickSort.java \
	test_files/offsets/offset-examples/TreeVisitor.java \
	test_files/offsets/offset-examples/Extra/Add.java \
	test_files/offsets/offset-examples/Extra/ArrayTest.java \
	test_files/offsets/offset-examples/Extra/CallFromSuper.java \
	test_files/offsets/offset-examples/Extra/Classes.java \
	test_files/offsets/offset-examples/Extra/DerivedCall.java \
	test_files/offsets/offset-examples/Extra/Example1.java \
	test_files/offsets/offset-examples/Extra/FieldAndClassConflict.java \
	test_files/offsets/offset-examples/Extra/Main.java \
	test_files/offsets/offset-examples/Extra/ManyClasses.java \
	test_files/offsets/offset-examples/Extra/OutOfBounds1.java \
	test_files/offsets/offset-examples/Extra/Overload2.java \
	test_files/offsets/offset-examples/Extra/ShadowBaseField.java \
	test_files/offsets/offset-examples/Extra/ShadowField.java \
	test_files/offsets/offset-examples/Extra/test06.java \
	test_files/offsets/offset-examples/Extra/test07.java \
	test_files/offsets/offset-examples/Extra/test15.java \
	test_files/offsets/offset-examples/Extra/test17.java \
	test_files/offsets/offset-examples/Extra/test20.java \
	test_files/offsets/offset-examples/Extra/test62.java \
	test_files/offsets/offset-examples/Extra/test73.java \
	test_files/offsets/offset-examples/Extra/test82.java \
	test_files/offsets/offset-examples/Extra/test93.java \
	test_files/offsets/offset-examples/Extra/test99.java \
	test_files/offsets/offset-examples/OtherExamples/PiazzaExample.java \
	test_files/offsets/offset-examples/OtherExamples/SiteExample.java

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
