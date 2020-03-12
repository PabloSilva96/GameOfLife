import java.util.EventObject;
public class FormEvent extends EventObject {
	private String fps;
	private String insert;
	private String cellSize;
	
	public FormEvent(Object source) {
		super(source);
	}
	public FormEvent(Object source, String fps, String insert, String cellSize) {
		super(source);
		this.fps = fps;
		this.insert = insert;
		this.cellSize = cellSize;
	}
	public String getFps() {
		return fps;
	}
	public void setFps(String fps) {
		this.fps = fps;
	}
	public String getInsert() {
		return insert;
	}
	public void setInsert(String insert) {
		this.insert = insert;
	}
	public String getCellSize() {
		return cellSize;
	}
	public void setCellSize(String cellSize) {
		this.cellSize = cellSize;
	}

}
