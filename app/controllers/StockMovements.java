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
	public static void create() throws Exception {
        StockMovement object = new StockMovement();
        Binder.bindBean(params.getRootParamNode(), "object", object);
        validation.valid(object);
        if (validation.hasErrors()) {
            renderArgs.put("error", play.i18n.Messages.get("crud.hasErrors"));
            try {
                render(request.controller.replace(".", "/") + "/blank.html", object);
            } catch (TemplateNotFoundException e) {
                render("CRUD/blank.html", object);
            }
        }
        object._save();
        addMovements((StockMovement) object);
        flash.success(play.i18n.Messages.get("crud.created", "StockMovement"));
        if (params.get("_save") != null) {
            redirect(request.controller + ".list");
        }
        if (params.get("_saveAndAddAnother") != null) {
            redirect(request.controller + ".blank");
        }
        redirect(request.controller + ".show", object._key());
    }
	
    public static void save(String id) throws Exception {
        StockMovement object = StockMovement.findById(Long.valueOf(id));
        Binder.bindBean(params.getRootParamNode(), "object", object);
        validation.valid(object);
        if (validation.hasErrors()) {
            renderArgs.put("error", play.i18n.Messages.get("crud.hasErrors"));
            try {
                render(request.controller.replace(".", "/") + "/show.html", object);
            } catch (TemplateNotFoundException e) {
                render("CRUD/show.html", object);
            }
        }
        object.clearMovements();
        object._save();
        addMovements(object);
        flash.success(play.i18n.Messages.get("crud.saved", "StockMovement"));
        if (params.get("_save") != null) {
            redirect(request.controller + ".list");
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
