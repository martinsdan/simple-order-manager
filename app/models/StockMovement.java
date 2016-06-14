package models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import play.data.validation.Required;
import play.db.jpa.Model;
 
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import controllers.OrderMovements;
import controllers.CRUD.Hidden;

@Entity
public class StockMovement extends Model {
	
	@Required
	public Date creationDate;
	
	@Required
    @ManyToOne
	public Item item;
	
	@Required
	public BigDecimal quantity;
	
	@OneToMany(mappedBy="movement", cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.EAGER)
    public List<OrderMovement> orderMovements;
	
	public String toString() {
	    return  item + " - " + quantity + " - " + creationDate;
	}

	public void clearMovements() {
		if(orderMovements!=null){
			orderMovements.clear();
		}
	}
}
