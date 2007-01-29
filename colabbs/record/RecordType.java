package colabbs.record;

/**
 * This type was writen by yhwu.
 * �U�� record �� implementer �t�d implement �䤤�� methods
 * �غc�禡���T�ءA�@�جO�@�@�ǤJ�U�Ȧb�T�w���D�O���� Type �ɡA�t�@�جO�b�����D�O���� Handler �ɥΪ��A�u�ǤJ�@�� byte[]�A�̫�@�جO�S�Ѽƪ��C
 * ���禡���F���Ӫ�F�Y�� Record �~�A��� converter �ΡC
 */
public interface RecordType extends Cloneable
{

	Object clone();
	String deleteBody();

	public abstract byte [] getRecordBytes();

	public abstract String getRecordString();

	int getSize();

	boolean isDeleted();

	boolean isRangeDeletible();	//�O�_�i�H�Q�Ϭq�R��

	void setDeleted();

	void setRecord(byte data[]);
}