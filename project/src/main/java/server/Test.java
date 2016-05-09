package server;

import java.time.format.DateTimeFormatter;

public class Test {

	
	public static void main(String[] args) {

		String testD = "2016-05-04T09:38:06.413+0000";
		testD = "2016-05-04T09:38:06.413+0000";
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		f.parse(testD);

	}

}
