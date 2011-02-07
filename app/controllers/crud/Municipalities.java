package controllers.crud;

import controllers.Secure;
import controllers.Check;
import controllers.CRUD;
import models.Municipality;
import play.mvc.With;

/**
 *
 * @author inaki
 */
@Check("admin")
@With(Secure.class)
@CRUD.For(Municipality.class)
public class Municipalities extends CRUD {
}
