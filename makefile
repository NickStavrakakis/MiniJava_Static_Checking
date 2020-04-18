# simplify that before sendind the project

all: compile

build: jars compile

jars:
	java -jar lib/jtb132di.jar minijava/minijava.jj
	java -jar lib/javacc5.jar minijava/minijava-jtb.jj

compile:
	javac Class.java
	javac Method.java
	javac SymbolTable.java
	javac LocateVisitor.java
	javac Main.java

clean:
	rm -f *.class *~
	clear

commit:#	clean delete
	git add -A
	$(commit_message)
	git push

delete:	clean
	rm TokenMgrError.* Token.* MiniJava* JavaCharStream.* ParseException.*
	rm -r visitor/ syntaxtree/

TEST = $(date +"Now time is %H:%M Update")

mytest:
	git commit -m "`date +'%H:%M'` Updated"
