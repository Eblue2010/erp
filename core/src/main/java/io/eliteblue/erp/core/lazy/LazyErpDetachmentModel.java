package io.eliteblue.erp.core.lazy;

import io.eliteblue.erp.core.model.ErpDetachment;
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

public class LazyErpDetachmentModel extends LazyDataModel<ErpDetachment> {

    private List<ErpDetachment> dataSource;

    public LazyErpDetachmentModel(List<ErpDetachment> dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ErpDetachment getRowData(String rowKey) {
        for (ErpDetachment erpDetachment : dataSource) {
            if (erpDetachment.getId() == Integer.parseInt(rowKey)) {
                return erpDetachment;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(ErpDetachment erpDetachment) { return String.valueOf(erpDetachment.getId()); }

    @Override
    public List<ErpDetachment> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        long rowCount = dataSource.stream()
                .filter(o -> filter(FacesContext.getCurrentInstance(), filterBy.values(), o))
                .count();

        // apply offset & filters
        List<ErpDetachment> erpDetachments = dataSource.stream()
                .skip(first)
                .filter(o -> filter(FacesContext.getCurrentInstance(), filterBy.values(), o))
                .limit(pageSize)
                .collect(Collectors.toList());

        // sort
        if (!sortBy.isEmpty()) {
            List<Comparator<ErpDetachment>> comparators = sortBy.values().stream()
                    .map(o -> new ErpDetachmentLazySorter(o.getField(), o.getOrder()))
                    .collect(Collectors.toList());
            Comparator<ErpDetachment> cp = ComparatorUtils.chainedComparator(comparators); // from apache
            erpDetachments.sort(cp);
        }

        setRowCount((int) rowCount);

        return erpDetachments;
    }

    private boolean filter(FacesContext context, Collection<FilterMeta> filterBy, Object o) {
        boolean matching = true;

        for (FilterMeta filter : filterBy) {
            FilterConstraint constraint = filter.getConstraint();
            Object filterValue = filter.getFilterValue();

            try {
                Object columnValue = String.valueOf(o.getClass().getField(filter.getField()).get(o));
                matching = constraint.isMatching(context, columnValue, filterValue, LocaleUtils.getCurrentLocale());
            } catch (ReflectiveOperationException e) {
                matching = false;
            }

            if (!matching) {
                break;
            }
        }

        return matching;
    }
}
