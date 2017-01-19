package tn.crns.ecos.converters;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import tn.crns.ecos.model.Utilisateur;

@FacesConverter("identityConverter")
public class IdentityConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		return context.getApplication().evaluateExpressionGet(context, "#{identity.utilisateur}", Utilisateur.class);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		
		return (String)value;
	}

}
