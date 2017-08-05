package cn.orgid.message.webapp;

import com.alibaba.fastjson.JSON;

public class JsonCommonResult {

	private boolean success;

	private String errorMsg;
	
	private Object value;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}
	
	

	public Object getValue() {
		return value;
	}

	public void setValue(Object val) {
		this.value = val;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public static JsonCommonResult success() {
		JsonCommonResult r = new JsonCommonResult();
		r.success = true;
		return r;
	}
	
	public static JsonCommonResult success(Object val) {
		JsonCommonResult r = new JsonCommonResult();
		r.success = true;
		r.value=val;
		return r;
	}

	public static JsonCommonResult error(String errorMsg) {
		JsonCommonResult r = new JsonCommonResult();
		r.success = false;
		r.setErrorMsg(errorMsg);
		return r;
	}

	public String toString() {
		return JSON.toJSONString(this);
	}

	public static void main(String[] args) {
		JsonCommonResult r = new JsonCommonResult();
		//System.out.println(r);
	}

}
