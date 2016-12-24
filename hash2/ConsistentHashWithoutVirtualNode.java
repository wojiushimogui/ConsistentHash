package hash2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.CRC32;

public class ConsistentHashWithoutVirtualNode {
	//用来存储服务器节点对象
	List<ServerNode> serverNodes= new ArrayList<ServerNode>();
	
	//添加服务器节点
	public void addServerNode(String serverNodeName){
		if(serverNodeName==null){
			return;
		}
		//利用Hash算法，求出服务器节点的Hash值
		long serverNodeHash = getHash(serverNodeName);
		ServerNode serverNode = new ServerNode(serverNodeName,serverNodeHash);
		serverNodes.add(serverNode);
		
		//将serverNodes进行排序
		Collections.sort(serverNodes,new Comparator<ServerNode>() {

			@Override
			public int compare(ServerNode node1, ServerNode node2) {
				if(node1.getServerNodeHash()<node2.getServerNodeHash()){
					return -1;
				}
				return 1;
			}
			
		});
	}

	public long getHash(String serverNodeName) {
		CRC32 crc32 = new CRC32();
		crc32.update(serverNodeName.getBytes());
		return crc32.getValue();
	}
	//删除服务器节点
	public void deleteServerNode(String serverName){
		//这里假设所有服务器名字不一样，则直接遍历名字是否相同即可
		int serverNum=serverNodes.size();
		for(int i=0;i<serverNum;i++){
			ServerNode node = serverNodes.get(i);
			if(node.getServerNodeName().equals(serverName)){
				serverNodes.remove(node);
				return;
			}
		}
	}
	//得到应当路由到的结点
	public ServerNode getServerNode(String key){
		//得到key的hash值
		long hash = getHash(key);
		//在serverNodes中找到大于hash且离其最近的的那个ServerNode
		//由于serverNodes是升序排列的，因此，找到的第一个大于hash的就是目标节点
		for(ServerNode node:serverNodes){
			if(node.getServerNodeHash()>hash){
				return node;
			}
		}
		//如果没有找到，则说明此key的hash值比所有服务器节点的hash值都大，因此返回最小hash值的那个Server节点
		return serverNodes.get(0);
		
	}
	
	public void printServerNodes(){
		System.out.println("所有的服务器节点信息如下：");
		for(ServerNode node:serverNodes){
			System.out.println(node.getServerNodeName()+":"+node.getServerNodeHash());
		}
	}
	
	public static void main(String[] args){
		ConsistentHashWithoutVirtualNode ch = new ConsistentHashWithoutVirtualNode();
		//添加一系列的服务器节点
		String[] servers = {"192.168.0.0:111", "192.168.0.1:111", "192.168.0.2:111",
				             "192.168.0.3:111", "192.168.0.4:111"};
		for(String server:servers){
			ch.addServerNode(server);
		}
		//打印输出一下服务器节点
		ch.printServerNodes();
		
		//看看下面的客户端节点会被路由到哪个服务器节点
		String[] nodes = {"127.0.0.1:1111", "221.226.0.1:2222", "10.211.0.1:3333"};
		System.out.println("此时，各个客户端的路由情况如下：");
		for(String node:nodes){
			ServerNode serverNode = ch.getServerNode(node);
			System.out.println(node+","+ ch.getHash(node)+"------->"+
						serverNode.getServerNodeName()+","+serverNode.getServerNodeHash());
		}
		
		//如果由一个服务器节点宕机，即需要将这个节点从服务器集群中移除
		String deleteNodeName="192.168.0.0:111";
		ch.deleteServerNode(deleteNodeName);
		
		System.out.println("删除节点"+deleteNodeName+"后，再看看同样的客户端的路由情况，如下：");
		for(String node:nodes){
			ServerNode serverNode = ch.getServerNode(node);
			System.out.println(node+","+ ch.getHash(node)+"------->"+
						serverNode.getServerNodeName()+","+serverNode.getServerNodeHash());
		}
		
		
	}
}
