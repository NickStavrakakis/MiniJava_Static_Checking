class SiteExample{
    public static void main(String[] a){
	System.out.println(new A().foo());
    }
}

class A {
	int i ;
	boolean flag ;
	int j ;

    public int foo(){
	return 0 ;
    }

    public boolean fa(){
	return true ;
    }

}

class B extends A{
	A type ;
	int k ;

    public int foo(){
	return 0 ;
    }

    public boolean bla(){
	return true ;
    }

}
