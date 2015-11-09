public class Composite{

	private String s;
	private BoundedBuffer<string> bb;

	public Composite(BoundedBuffer<String> bb, String s){
		this.bb = bb;
		this.s = s;
	}

	public BoundedBuffer<String> getBB(){
		return this.bb
	}

	public String getString(){
		return this.s;
	}
}