package models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import play.data.validation.Required;
import play.db.jpa.Model;
 
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	
	public String toString() {
	    return  item + " - " + quantity + " - " + creationDate;
	}
}