class PiazzaExample{
    public static void main(String[] a){
	System.out.println(new A().foo());
    }
}

class A {
	int a ;
	int b ;

    public int foo(){
	return 1 ;
    }

}

class B extends A{
	int c ;
	boolean d ;

    public boolean bar(){
	return true ;
    }

}

class C extends B{
	int e ;
	int[] f ;

    public int search(){
	return 1 ;
    }

}

class D extends A{
	boolean g ;
	int[] h ;

    public int delete(){
	return 1 ;
    }

}
