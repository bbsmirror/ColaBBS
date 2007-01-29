package colabbs.record;

/**
 * This type was writen by yhwu.
 * 各種 record 的 implementer 負責 implement 其中的 methods
 * 建構函式有三種，一種是一一傳入各值在確定知道是那個 Type 時，另一種是在不知道是那種 Handler 時用的，只傳入一個 byte[]，最後一種是沒參數的。
 * 此函式除了拿來表達某種 Record 外，亦當 converter 用。
 */
public interface RecordType extends Cloneable
{

	Object clone();
	String deleteBody();

	public abstract byte [] getRecordBytes();

	public abstract String getRecordString();

	int getSize();

	boolean isDeleted();

	boolean isRangeDeletible();	//是否可以被區段刪除

	void setDeleted();

	void setRecord(byte data[]);
}