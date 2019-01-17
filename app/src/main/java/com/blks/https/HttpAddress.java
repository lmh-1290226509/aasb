package com.blks.https;

public class HttpAddress {

	// 测试服务器（公司）
	public static final String BASE_URL = "http://121.196.238.132:8071/HostPublic/";
	// 服务器（陈）
	public static final String IDS_URL = "http://ids.frtauto.com/";
	// 登录
	public static final String LOGIN = IDS_URL + "Usr/Login";

	 // 用户心跳
	 public static final String HEARTBEAT = BASE_URL +
	 "IDS_Host.svc/HeartBeat";
	 // 修改密码
	 public static final String CONFIRM_PASSWORD = BASE_URL
	 + "IDS_Host.svc/ResetPassword";
	 // 修改密码
	 public static final String CESHI =
	 "http://192.168.1.127:8089/api/Values";
}
