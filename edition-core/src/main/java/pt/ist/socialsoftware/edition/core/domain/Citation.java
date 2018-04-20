package pt.ist.socialsoftware.edition.core.domain;

public abstract class Citation extends Citation_Base {
    
    public Citation() {
        super();
    }
    
    //protected, maybe
  	public void init(LdoD ldoD, Fragment fragment, String sourceLink, String date, String fragText) {
  		setLdoD(ldoD);
  		setFragment(fragment);
  		setSourceLink(sourceLink);
  		setDate(date);
  		setFragText(fragText);
  	}
    
}
