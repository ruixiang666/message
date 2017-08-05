package message.client.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Test {
	
	public static void main(String[] args) throws IOException {
		
		
		try {
			FileInputStream fis = 
					new FileInputStream("./test_file/20106635319267.jpg");
			byte[] bs = new byte[fis.available()];
			fis.read(bs);
			String s = Base64.getEncoder().encodeToString(bs);
			System.out.println(s);
			byte[] bs2 = Base64.getDecoder().decode(s);
			FileOutputStream fos = new FileOutputStream("./test_file/1.jpg");
			fos.write(bs2);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
