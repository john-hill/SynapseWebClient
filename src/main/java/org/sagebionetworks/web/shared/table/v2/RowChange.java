package org.sagebionetworks.web.shared.table.v2;

import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Represent the changes to be made to a since row of a table entity.
 * 
 * @author John
 *
 */
public class RowChange implements IsSerializable {
	
	/**
	 * The ID of the row to change.
	 */
	Long rowId;
	/**
	 * The changes are a map of columnId to new value.
	 */
	Map<Long, String> changes;
	public Long getRowId() {
		return rowId;
	}
	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}
	public Map<Long, String> getChanges() {
		return changes;
	}
	public void setChanges(Map<Long, String> changes) {
		this.changes = changes;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((changes == null) ? 0 : changes.hashCode());
		result = prime * result + ((rowId == null) ? 0 : rowId.hashCode());
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
		RowChange other = (RowChange) obj;
		if (changes == null) {
			if (other.changes != null)
				return false;
		} else if (!changes.equals(other.changes))
			return false;
		if (rowId == null) {
			if (other.rowId != null)
				return false;
		} else if (!rowId.equals(other.rowId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "RowChange [rowId=" + rowId + ", changes=" + changes + "]";
	}

	
}
