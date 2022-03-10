package es.urjc.ccia.ruva;

public class TreeNode {
    int idNode=0;           //node id
    int idParentNode=0;     //parent node id
    String nameNode="";      //name node (generally equal to feature except when aux node)
    String alias="";		  //feature name or alias
    String nameFeature="";   //feature name
    String parentName="";    //Name of parent (Feature)
    String relType="";          //relation with parent - category . Values: ("O" Optional, "M" Mandatory)
    String relSiblings="";      //relation with siblings (AND, SET (OR/XOR/OTHER) )
    String type=""; 		 //Type of node - Values: ("" or "BASIC", normal type, "SET" Set Type)
    int minCardinality=0;   //min times-to-be-present
    int maxCardinality=0;   //max times-to-be-present
    int totChildren=0;      //total of children below the node
    int totSiblings=0;      //total of children below the node
    int level=0;            //depth level
    Feature feature;      //Feature associated with node
    int match=0;            //Amount of supertypes that are compatible (by default, 0) when checking to add new feature
    int idFeature=0;        //idFeature if take from master repository - only when available
    int tAND=0;				//total of AND relations
    int tOR=0;				//total of OR relations
    int tXOR=0;				//total of XOR relations
}
