package old.Utility;

import java.util.HashMap;

public class Parser {

	public Parser () {}
	
	public HashMap<String, String> parse (String s) {
		
		HashMap<String, String> retVal = new HashMap<String, String>();
		String[] lines = s.toString().split("&");
		
		for (int i = 0; i < lines.length; ++i) {
			retVal.put(lines[i].split("=")[0], lines[i].split("=")[1]);
			System.out.println("(" + lines[i].split("=")[0] + ", " + lines[i].split("=")[1] + ")");
		}
		
		return retVal;
	}
	
}
