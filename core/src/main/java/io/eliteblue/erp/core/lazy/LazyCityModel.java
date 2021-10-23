package io.eliteblue.erp.core.lazy;

import io.eliteblue.erp.core.model.ErpCity;
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

public class LazyCityModel extends LazyDataModel<ErpCity> {

    private List<ErpCity> dataSource;

    public LazyCityModel(List<ErpCity> dataSource) { this.dataSource = dataSource; }

    @Override
    public ErpCity getRowData(String rowKey) {
        for (ErpCity erpCity : dataSource) {
            if (erpCity.getId() == Integer.parseInt(rowKey)) {
                return erpCity;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(ErpCity erpCity) {
        return String.valueOf(erpCity.getId());
    }

    @Override
    public List<ErpCity> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        long rowCount = dataSource.stream()
                .filter(o -> filter(FacesContext.getCurrentInstance(), filterBy.values(), o))
                .count();

        // apply offset & filters
        List<ErpCity> erpCities = dataSource.stream()
                .skip(first)
                .filter(o -> filter(FacesContext.getCurrentInstance(), filterBy.values(), o))
                .limit(pageSize)
                .collect(Collectors.toList());

        // sort
        if (!sortBy.isEmpty()) {
            List<Comparator<ErpCity>> comparators = sortBy.values().stream()
                    .map(o -> new CityLazySorter(o.getField(), o.getOrder()))
                    .collect(Collectors.toList());
            Comparator<ErpCity> cp = ComparatorUtils.chainedComparator(comparators); // from apache
            erpCities.sort(cp);
        }

        setRowCount((int) rowCount);

        return erpCities;
    }

    private boolean filter(FacesContext context, Collection<FilterMeta> filterBy, Object o) {
        boolean matching = true;

        for (FilterMeta filter : filterBy) {
            FilterConstraint constraint = filter.getConstraint();
            Object filterValue = filter.getFilterValue();
            String filterText = (filterValue == null) ? null : filterValue.toString().trim().toLowerCase();

            ErpCity erpCity = (ErpCity) o;
            return erpCity.getName().toLowerCase().contains(filterText);
        }

        return matching;
    }
}
