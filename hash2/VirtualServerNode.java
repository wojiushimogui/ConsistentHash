package hash2;

public class VirtualServerNode {
	private String serverNodeName;//这个名字指的是其对应的真实的物理服务器节点的名字
	private long virtualServerNodeHash;
	
	public VirtualServerNode(String serverNodeName, long virtualServerNodeHash) {
		super();
		this.serverNodeName = serverNodeName;
		this.virtualServerNodeHash = virtualServerNodeHash;
	}
	public String getServerNodeName() {
		return serverNodeName;
	}
	public void setServerNodeName(String serverNodeName) {
		this.serverNodeName = serverNodeName;
	}
	public long getVirtualServerNodeHash() {
		return virtualServerNodeHash;
	}
	public void setVirtualServerNodeHash(long virtualServerNodeHash) {
		this.virtualServerNodeHash = virtualServerNodeHash;
	}
	
}
