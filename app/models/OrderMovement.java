package models;

import java.math.BigDecimal;

import play.data.validation.Required;
import play.db.jpa.Model;
 
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class OrderMovement extends Model {
	
	@Required
	@ManyToOne
	public Order order;
	
	@Required
	@ManyToOne
	public StockMovement movement;
	
	@Required
	public BigDecimal quantity;
}
