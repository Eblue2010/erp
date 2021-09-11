package io.eliteblue.erp.core.lazy;

import io.eliteblue.erp.core.model.ErpClient;
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

public class LazyErpClientModel extends LazyDataModel<ErpClient> {

    private List<ErpClient> dataSource;

    public LazyErpClientModel(List<ErpClient> dataSource){ this.dataSource = dataSource; }

    @Override
    public ErpClient getRowData(String rowKey) {
        for (ErpClient erpClient : dataSource) {
            if (erpClient.getId() == Integer.parseInt(rowKey)) {
                return erpClient;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(ErpClient erpClient) {
        return String.valueOf(erpClient.getId());
    }

    @Override
    public List<ErpClient> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        long rowCount = dataSource.stream()
                .filter(o -> filter(FacesContext.getCurrentInstance(), filterBy.values(), o))
                .count();

        // apply offset & filters
        List<ErpClient> erpClients = dataSource.stream()
                .skip(first)
                .filter(o -> filter(FacesContext.getCurrentInstance(), filterBy.values(), o))
                .limit(pageSize)
                .collect(Collectors.toList());

        // sort
        if (!sortBy.isEmpty()) {
            List<Comparator<ErpClient>> comparators = sortBy.values().stream()
                    .map(o -> new ErpClientLazySorter(o.getField(), o.getOrder()))
                    .collect(Collectors.toList());
            Comparator<ErpClient> cp = ComparatorUtils.chainedComparator(comparators); // from apache
            erpClients.sort(cp);
        }

        setRowCount((int) rowCount);

        return erpClients;
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
