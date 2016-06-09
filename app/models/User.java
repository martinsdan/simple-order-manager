package models;

import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Model;
 
import javax.persistence.Entity;

@Entity
public class User extends Model {
	@Required
	public String name;
	
	@Email
	@Required
	public String email;
	
	public String toString() {
	    return name;
	}
}
