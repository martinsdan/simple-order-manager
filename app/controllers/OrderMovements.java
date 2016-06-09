package controllers;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Query;

import controllers.CRUD.ObjectType;

import notifiers.Mail;

import models.Item;
import models.Order;
import models.OrderMovement;
import models.StockMovement;

import play.db.jpa.JPA;
import play.mvc.Controller;

public class OrderMovements extends CRUD {
	
	public static BigDecimal getOrderAllocatedQuantity(Order order){
		String queryStr = "SELECT COALESCE(sum(quantity), 0) FROM "+
				"OrderMovement "+
				"WHERE order = " + order.id;
		Query query = JPA.em().createQuery(queryStr);
		
		List<BigDecimal> qty = query.getResultList();
		return qty.get(0);
	}
	
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
	
	/**
	 * @param item The item in the Order
	 * @return a list of [Order, BigDecimal] where the Big Decimal is the not allocated quantity
	 */
	public static List<Object[]> getIncompleteOrders(Item item){
		String queryStr = "SELECT o, o.quantity - sum(COALESCE(om.quantity, 0)) FROM " +
		"OrderMovement as om RIGHT JOIN om.order as o " +
		"WHERE o.item = " + item.id +
		" GROUP BY o.id " +
		"HAVING o.quantity > sum(COALESCE(om.quantity, 0)) " ;
		
		Query query = JPA.em().createQuery(queryStr);
		
		List<Object[]> movements = query.getResultList();
		return movements;
	}
}
