package controllers;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.List;

import models.Order;
import models.OrderMovement;
import models.StockMovement;
import play.data.binding.Binder;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import controllers.CRUD.ObjectType;

public class StockMovements extends CRUD {
	public static void create(Object aa) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Model object = (Model) constructor.newInstance();
        Binder.bindBean(params.getRootParamNode(), "object", object);
        validation.valid(object);
        if (validation.hasErrors()) {
            renderArgs.put("error", play.i18n.Messages.get("crud.hasErrors"));
            try {
                render(request.controller.replace(".", "/") + "/blank.html", type, object);
            } catch (TemplateNotFoundException e) {
                render("CRUD/blank.html", type, object);
            }
        }
        object._save();
        addMovements((StockMovement) object);
        flash.success(play.i18n.Messages.get("crud.created", type.modelName));
        if (params.get("_save") != null) {
            redirect(request.controller + ".list");
        }
        if (params.get("_saveAndAddAnother") != null) {
            redirect(request.controller + ".blank");
        }
        redirect(request.controller + ".show", object._key());
    }

	private static void addMovements(StockMovement mov) {
		List<Object[]> orders = OrderMovements.getIncompleteOrders(mov.item);
		BigDecimal missingQty = mov.quantity;
		
		for(Object[] obj : orders){
			if(missingQty.intValue() <= 0){
				break;
			}
			Order order = (Order) obj[0];
			BigDecimal movAvailableQty = (BigDecimal) obj[1];
			
			BigDecimal qtyToAdd = missingQty.min(movAvailableQty);
			
			order.addMovement(mov, qtyToAdd);
			
			missingQty = missingQty.subtract(qtyToAdd);
		}
	}
}
