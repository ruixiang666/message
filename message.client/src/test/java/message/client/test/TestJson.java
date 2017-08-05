package message.client.test;

import com.google.gson.Gson;

public class TestJson {
	public static void main(String[] args) {
		Gson g = new Gson();
		T1 t = new T1();
		t.setS("s");
		T tt =new T();
		tt.setT1(t);
		//System.out.println(g.toJson(tt).replace('\"', '\''));
	}
	
	public static class T{
		private T1 t1;

		public T1 getT1() {
			return t1;
		}

		public void setT1(T1 t1) {
			this.t1 = t1;
		}
		
		
		
		
	}
	
	public static class T1{
		private String s;

		public String getS() {
			return s;
		}

		public void setS(String s) {
			this.s = s;
		}
		
	}
}
