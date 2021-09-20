package io.eliteblue.erp.core.lazy;

import io.eliteblue.erp.core.model.ErpEmployee;
import org.primefaces.model.SortOrder;

import java.util.Comparator;

public class EmployeeLazySorter implements Comparator<ErpEmployee> {

    private String sortField;
    private SortOrder sortOrder;

    public EmployeeLazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(ErpEmployee o1, ErpEmployee o2) {
        try {
            Object value1 = o1.getClass().getField(sortField).get(o1);
            Object value2 = o2.getClass().getField(sortField).get(o2);

            int value = ((Comparable)value1).compareTo(value2);

            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
