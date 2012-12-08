package org.scbit.lsbi.protein;

public class HeaderVO {
	
	private String header;
	private Integer counter;
	public HeaderVO(String header, Integer counter) {
		super();
		this.header = header;
		this.counter = counter;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public Integer getCounter() {
		return counter;
	}
	public void setCounter(Integer counter) {
		this.counter = counter;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((counter == null) ? 0 : counter.hashCode());
		result = prime * result + ((header == null) ? 0 : header.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HeaderVO other = (HeaderVO) obj;
		if (counter == null) {
			if (other.counter != null)
				return false;
		} else if (!counter.equals(other.counter))
			return false;
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (!header.equals(other.header))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "HeaderVO [header=" + header + ", counter=" + counter + "]";
	}
	
	public HeaderVO() {
		// TODO Auto-generated constructor stub
	}

}
