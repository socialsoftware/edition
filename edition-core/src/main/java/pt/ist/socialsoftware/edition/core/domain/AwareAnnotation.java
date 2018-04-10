package pt.ist.socialsoftware.edition.core.domain;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class AwareAnnotation extends AwareAnnotation_Base {
    
	//setText() //guardar a meta informação
	
	public AwareAnnotation(VirtualEditionInter inter, String quote, String text) {
		super.init(inter, quote, text);
	}
	
	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		super.remove();
	}
}
