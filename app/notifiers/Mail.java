package notifiers;

import models.Order;
import models.User;
import play.mvc.Mailer;

public class Mail extends Mailer {
	private static String SENDER = "Daniel <daniel@example.com>";
	
	public static void completedOrder(User user, Order order){
		setSubject("Your Order is completed");
	    addRecipient(user.email);
	    setFrom(SENDER);
	    send(user, order);
	}
}
