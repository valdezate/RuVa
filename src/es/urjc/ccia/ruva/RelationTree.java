package es.urjc.ccia.ruva;

import java.util.ArrayList;
import java.util.Collection;

public class RelationTree {
	public int idNode=0;
	public String nameNode="";
	public int rAND=0;
	public int rOR=0;
	public int rXOR=0;
	public int rOTHER=0;
	public int siblingsTotal=0;
	public Collection eNodes = new ArrayList<RelationNode>();   
	public int total() { return rAND+rOR+rXOR+rOTHER; }
}
