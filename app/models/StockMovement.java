package models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import play.data.validation.Required;
import play.db.jpa.Model;
 
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import controllers.OrderMovements;

@Entity
public class StockMovement extends Model {
	
	@Required
	public Date creationDate;
	
	@Required
    @ManyToOne
	public Item item;
	
	@Required
	public BigDecimal quantity;
	
	public String toString() {
	    return  item + " - " + quantity + " - " + creationDate;
	}
	
	public List<OrderMovement> getAllocatedOrders(){
		return OrderMovement.find("movement = ?", this).fetch();
	}
}
