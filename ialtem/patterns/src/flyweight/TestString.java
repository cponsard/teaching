package flyweight;

public class TestString {

	public static void main(String[] args) {
		String s1="ABCD";
		String s2="ABCD";
		System.out.println(s1.equals(s2));
		System.out.println(System.identityHashCode(s1));
		System.out.println(System.identityHashCode(s2));
		System.out.println(s1==s2);
		
		System.out.println("=================================");
		StringBuffer sb3=new StringBuffer("AB").append("CD");
		String s3=sb3.toString();
		System.out.println(s1.equals(s3));
		System.out.println(System.identityHashCode(s1));
		System.out.println(System.identityHashCode(s3));
		System.out.println(s1==s3);
	}
}
