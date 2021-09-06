package io.eliteblue.erp.core.converters;

import io.eliteblue.erp.core.model.OperationsArea;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.io.Serializable;

public class OperationsAreaConverter implements Converter<OperationsArea>, Serializable {

    public OperationsArea getAsObject(FacesContext context, UIComponent component, String value) {
        OperationsArea retVal = new OperationsArea();
        retVal.setLocation(value);
        return retVal;
    }

    public String getAsString(FacesContext context, UIComponent component, OperationsArea value) {
        return value.getLocation();
    }
}
