package controllers;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Query;

import models.Item;
import models.StockMovement;

import play.db.jpa.JPA;
import play.mvc.Controller;

public class OrderMovements extends CRUD {
	
	/**
	 * @param item The item in the Movement
	 * @return a list of [StockMovement, BigDecimal] where the Big Decimal is the not allocated quantity
	 */
	public static List<Object[]> getIncompleteMovements(Item item){
		String queryStr = "SELECT sm, sm.quantity - sum(COALESCE(om.quantity, 0)) FROM " +
		"OrderMovement as om RIGHT JOIN om.movement as sm " +
		"WHERE sm.item = " + item.id +
		" GROUP BY sm.id " +
		"HAVING sm.quantity > sum(COALESCE(om.quantity, 0)) " ;
		
		Query query = JPA.em().createQuery(queryStr);
		
		List<Object[]> movements = query.getResultList();
		return movements;
	}
}
