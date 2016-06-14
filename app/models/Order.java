package models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import play.data.validation.Required;
import play.db.jpa.Model;
 
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import notifiers.Mail;

import controllers.CRUD.Hidden;
import controllers.OrderMovements;
import controllers.CRUD.ObjectType;

@Entity
@Table(name="OrderTable")
public class Order extends Model {
	
	@Required
	public Date creationDate;
	
	@Required
    @ManyToOne
	public Item item;
	
	@Required
	public BigDecimal quantity;
	
	@Required
    @ManyToOne
	public User createdBy;
	
	@Hidden
	@OneToMany(mappedBy="order", cascade=CascadeType.ALL,orphanRemoval=true)
    public List<OrderMovement> orderMovements;
	
	public String toString() {
	    return item + " - " + quantity + " - " + creationDate;
	}
	
	public BigDecimal allocatedQuantity(){
		return OrderMovements.getOrderAllocatedQuantity(this);
	}

	public boolean isComplete() {
		BigDecimal allocated = allocatedQuantity();
		return allocated.compareTo(quantity) >= 0;
	}
	
	public void addMovement(StockMovement movement, BigDecimal quantity){
		OrderMovement orderMov = new OrderMovement(this, movement, quantity);
		orderMov.save();
		
		if(isComplete()){
			Mail.completedOrder(createdBy, this);
		}
	}
}