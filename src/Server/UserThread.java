package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import Beans.User;

public class UserThread extends Thread {
	private Socket socket;
	private List<User> userInfoList;
	private Map<String, UserThread> userThreadMap;

	public UserThread(Socket socket, List<User> userInfoList, Map<String, UserThread> userThreadMap) {
		this.socket = socket;
		this.userInfoList = userInfoList;
		this.userThreadMap = userThreadMap;
	}

	// 登陆时验证用户名和监听端口是否重复
	public int UserInofCheck(String username, int port) {
		for (User user : userInfoList) {
			if (user.getName().equals(username))
				return 1;
			if(user.getPort() == port)
				return 2;
		}
		return 0;
	}

	// 登录
	public void Login(String name, int port) {
		// 获取ip和登陆时间
		InetAddress addr = socket.getInetAddress();
		String ip = addr.getHostName();
		long nowTime = System.currentTimeMillis();

		// 新建用户实例加入用户列表
		User user = new User(name, ip, port, nowTime);
		userInfoList.add(user);

		// 将当前线程加入线程列表
		userThreadMap.put(name, this);
		System.out.println("用户 " + name + " 登录成功");
	}

	// 停止线程方法
	public void MyStop() {
		try {
			// 关闭socket
			socket.close();
			// 中断自身
			this.interrupt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String username = null;
		try {
			// 使用socket创建传输流
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(new DataOutputStream(socket.getOutputStream()));
			
			//进入循环，接收对应用户的消息
			while (!Thread.interrupted()) {
				// 接收请求码
				String request = reader.readLine();
				
				if(request!=null) {
					//处理登录请求
					if(request.equals("00")) {
						username = reader.readLine();
						String port = reader.readLine();
						int loginFlag = UserInofCheck(username, Integer.parseInt(port));
						//登录成功
						if(loginFlag == 0) {
							Login(username,Integer.parseInt(port));
						}
						
						//向客户端返回结果
						writer.println(loginFlag);
						writer.flush();
					}
				}
			}
		} catch (IOException e) {
			System.out.println("用户："+username+"异常断开连接，线程关闭");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
