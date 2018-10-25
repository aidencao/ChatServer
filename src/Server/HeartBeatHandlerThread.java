package Server;

import java.util.List;
import java.util.Map;

import Beans.User;

//用于循环验证用户心跳状态
public class HeartBeatHandlerThread extends Thread {
	private List<User> userInfoList;
	private Map<String, UserThread> userThreadMap;
	
	public HeartBeatHandlerThread(List<User> userInfoList, Map<String, UserThread> userThreadMap) {
		this.userInfoList = userInfoList;
		this.userThreadMap = userThreadMap;
	}
	
	//停止线程方法
	public void MyStop() {
		//中断自身
		this.interrupt();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			System.out.println("开始监听用户心跳");
			while(!Thread.interrupted()) {
				for (User user : userInfoList) {
					//判断心跳是否超时, 超时后通知其停止
					long nowTime = System.currentTimeMillis();
					if(nowTime - user.getHeartbeatTime() > 30000) {
						String username = user.getName();
						System.out.println("用户"+username+"超时");
						UserThread userThread = userThreadMap.get(username);
						userThread.MyStop();
						
						//从服务器删除该用户信息
						userThreadMap.remove(username);
						userInfoList.remove(user);
					}
				}
				sleep(1000);//睡眠1秒
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
