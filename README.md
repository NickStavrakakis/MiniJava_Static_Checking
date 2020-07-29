# MiniJava Static Checking (Semantic Analysis)

## _Academic Year of 2019-2020_

This project is about building a compiler for MiniJava. MiniJava is a subset of Java that is designed so that its programs can be compiled by a full Java compiler like javac. The implementation is based on the use of two GJDepthFirst visitors and one Symbol Table. The Symbol Table is a hash table that supports the storage and the access of all the classes and the methods, that the input program has. The two visitors are being used to insert all the definitions into the Symbol Table and to check the whole program for possible syntax errors. After a successful compiling, the program is printing the variables' and methods' offsets.

### Supervisors

* Professor Yannis Smaragdakis <smaragd@di.uoa.gr>, University of Athens
