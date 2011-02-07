package controllers.crud;

import controllers.Secure;
import controllers.Check;
import controllers.CRUD;
import models.election.Election;
import play.mvc.With;

/**
 *
 * @author inaki
 */
@Check("admin")
@With(Secure.class)
@CRUD.For(Election.class)
public class Elections extends CRUD {
}
