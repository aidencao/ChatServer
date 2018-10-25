package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import Beans.User;

public class ServerStart {
	public static void main(String[] args) {
		//创建用户信息储存表
		List<User> userInofList = new CopyOnWriteArrayList<User>();
		
		//创建用户线程列表
		Map<String, UserThread> userThreadMap= new ConcurrentHashMap<String, UserThread>();
		
		try {
			ServerSocket ss = new ServerSocket(4001);
			
			//启动心跳监听线程
			HeartBeatHandlerThread heartBeatHandlerThread = new HeartBeatHandlerThread(userInofList, userThreadMap);
			heartBeatHandlerThread.start();
			while (true) {
				System.out.println("服务器正在监听端口4001。。。");
				//监听端口4001
				Socket socket = ss.accept();
				UserThread userThread = new UserThread(socket, userInofList, userThreadMap);
				userThread.start();
				System.out.println("监听到一个请求，建立用户线程");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
