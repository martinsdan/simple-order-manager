package models;

import play.data.validation.Required;
import play.db.jpa.Model;
 
import javax.persistence.Entity;

@Entity
public class Item extends Model {
	
	@Required
	public String name;
	
	public String toString() {
	    return name;
	}
}
