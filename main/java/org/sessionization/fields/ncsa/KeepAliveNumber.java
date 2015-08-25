package org.sessionization.fields.ncsa;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class KeepAliveNumber implements Field {

	@Column(name = "keep_alive_number")
	 private int number;

	public KeepAliveNumber() {
		number = 0;
	}

    public KeepAliveNumber(String niz) {
        number = Integer.valueOf(niz);
    }

	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumber() {
        return number;
    }

    @Override
    public String izpis() {
        return number + "";
    }

    @Override
    public FieldType getFieldType() {
        return FieldType.KeepAliveNumber;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof KeepAliveNumber && number == ((KeepAliveNumber) o).getNumber();
    }

    @Override
    public String toString() {
        return number + "";
    }
}
