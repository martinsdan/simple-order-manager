package models;

import java.math.BigDecimal;
import java.util.Date;

import play.data.validation.Required;
import play.db.jpa.Model;
 
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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
}
