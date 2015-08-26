package org.sessionization.fields.ncsa;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;
import org.sessionization.fields.Protocol;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProcessID implements Field {

	@Column(name = "process_id")
	private int pId;

	public ProcessID() {
		pId = 0;
	}

	public ProcessID(String niz) {
		pId = Integer.valueOf(niz);
	}

	public int getpId() {
		return pId;
	}

	public void setpId(int pId) {
		this.pId = pId;
	}

	@Override
	public String izpis() {
		return pId + "";
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.ProcesID;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof ProcessID && pId == ((ProcessID) o).getpId();
	}

	@Override
	public String toString() {
		return pId + "";
	}
}
