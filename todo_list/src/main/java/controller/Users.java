package controller;



public class Users {
	
	public static boolean checklength(String content) {
//		System.out.println(content.length());
		if (content.length() <= 20 && content.length() >= 3) {
        	return true;
        } 
		return false;
		
	}
	

}

