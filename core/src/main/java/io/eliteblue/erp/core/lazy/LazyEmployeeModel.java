package io.eliteblue.erp.core.lazy;

import io.eliteblue.erp.core.model.ErpEmployee;
import org.apache.commons.collections4.ComparatorUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.filter.FilterConstraint;
import org.primefaces.util.LocaleUtils;

import javax.faces.context.FacesContext;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LazyEmployeeModel extends LazyDataModel<ErpEmployee> {

    private List<ErpEmployee> dataSource;

    public LazyEmployeeModel(List<ErpEmployee> dataSource) { this.dataSource = dataSource; }

    @Override
    public ErpEmployee getRowData(String rowKey) {
        for (ErpEmployee erpEmployee : dataSource) {
            if (erpEmployee.getId() == Integer.parseInt(rowKey)) {
                return erpEmployee;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(ErpEmployee erpEmployee) {
        return String.valueOf(erpEmployee.getId());
    }

    @Override
    public List<ErpEmployee> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        long rowCount = dataSource.stream()
                .filter(o -> filter(FacesContext.getCurrentInstance(), filterBy.values(), o))
                .count();

        // apply offset & filters
        List<ErpEmployee> erpEmployees = dataSource.stream()
                .skip(first)
                .filter(o -> filter(FacesContext.getCurrentInstance(), filterBy.values(), o))
                .limit(pageSize)
                .collect(Collectors.toList());

        // sort
        if (!sortBy.isEmpty()) {
            List<Comparator<ErpEmployee>> comparators = sortBy.values().stream()
                    .map(o -> new EmployeeLazySorter(o.getField(), o.getOrder()))
                    .collect(Collectors.toList());
            Comparator<ErpEmployee> cp = ComparatorUtils.chainedComparator(comparators); // from apache
            erpEmployees.sort(cp);
        }

        setRowCount((int) rowCount);

        return erpEmployees;
    }

    private boolean filter(FacesContext context, Collection<FilterMeta> filterBy, Object o) {
        boolean matching = filterBy.size() <= 0;
        //System.out.println("FILTERING........ "+filterBy.size());

        for (FilterMeta filter : filterBy) {
            FilterConstraint constraint = filter.getConstraint();
            Object filterValue = filter.getFilterValue();
            //System.out.println("FILTERED VALUE: "+filterValue);
            //System.out.println("FILTERED FIELD: "+filter.getField());
            String filterText = (filterValue == null) ? null : filterValue.toString().trim().toLowerCase();

            ErpEmployee erpEmployee = (ErpEmployee) o;
            return erpEmployee.getFirstname().toLowerCase().contains(filterText)
                    || erpEmployee.getLastname().toLowerCase().contains(filterText)
                    || erpEmployee.getGender().name().toLowerCase().contains(filterText)
                    || erpEmployee.getEmployeeId().toLowerCase().contains(filterText)
                    || erpEmployee.getStatus().name().toLowerCase().contains(filterText)
                    || (erpEmployee.getFirstname().toLowerCase()+" "+erpEmployee.getLastname().toLowerCase()).contains(filterText);
        }

        return matching;
    }
}
