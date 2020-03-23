package pt.ist.socialsoftware.edition.ldod.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import kieker.monitoring.probe.aspectj.operationExecution.AbstractOperationExecutionAspect;

/**
 * @author Bernardo Andrade
 */
@Aspect
public class GettersAndSetters extends AbstractOperationExecutionAspect {

	/**
	 * Default constructor.
	 */
	
	public GettersAndSetters() {
		// empty default constructor
	}

	// GETTERS AND SETTERS

	/**
	 * This is a pointcut accepting all calls and executions of getters methods (methods which return something and start with {@code get}).
	 */
	@Pointcut("execution(public * pt.ist.socialsoftware.edition.ldod.domain.*.get*(..)) || call(public * pt.ist.socialsoftware.edition.ldod.domain.*.get*(..))")
	public void publicGetters() {}

	@Pointcut("execution(pt.ist.fenixframework..* get*(..)) || call(pt.ist.fenixframework..* get*(..))")
	public void gettersReturningFenixFrameworkObject() {}
	
	/**
	 * This is a pointcut accepting all calls and executions of setter methods (methods which return void and start with {@code set}).
	 */
	@Pointcut("execution(public void pt.ist.socialsoftware.edition.ldod.domain.*.set*(..)) || call(public void pt.ist.socialsoftware.edition.ldod.domain.*.set*(..))")
	public void publicSetters() {}
	
	@Pointcut("execution(* set*(pt.ist.fenixframework..*, ..)) || call(* set*(pt.ist.fenixframework..*, ..))")
	public void settersWithFenixFrameworkObjectArgument() {}
	
	@Pointcut("!gettersReturningFenixFrameworkObject() && !settersWithFenixFrameworkObjectArgument()")
	public void noGettersOrSettersWithFenixFramework() {}
	
	@Pointcut("publicGetters() || publicSetters()")
	public void publicGettersAndSetters() {}
	
	// ADDS AND REMOVES

	@Pointcut("execution(public void pt.ist.socialsoftware.edition.ldod.domain.*.add*(..)) || call(public void pt.ist.socialsoftware.edition.ldod.domain.*.add*(..))")
	public void publicAdds() {}

	@Pointcut("execution(public void pt.ist.socialsoftware.edition.ldod.domain.*.remove*(..)) || call(public void pt.ist.socialsoftware.edition.ldod.domain.*.remove*(..))")
	public void publicRemoves() {}

	@Pointcut("publicAdds() || publicRemoves()")
	public void publicAddsAndRemoves() {}

	// FENIX FRAMEWORK

	@Pointcut("execution(public * pt.ist.fenixframework.FenixFramework.getDomainObject(..)) || call(public * pt.ist.fenixframework.FenixFramework.getDomainObject(..))")
	public void fenixFrameworkGetDomainObject() {} // This one isn't used on the domain classes

	@Pointcut("execution(public * pt.ist.fenixframework.FenixFramework.getDomainModel(..)) || call(public * pt.ist.fenixframework.FenixFramework.getDomainModel(..))")
	public void fenixFrameworkGetDomainModel() {}

	@Pointcut("execution(public * pt.ist.fenixframework.FenixFramework.getDomainRoot(..)) || call(public * pt.ist.fenixframework.FenixFramework.getDomainRoot(..))")
	public void fenixFrameworkGetDomainRoot() {}

	@Pointcut("fenixFrameworkGetDomainObject() || fenixFrameworkGetDomainModel() || fenixFrameworkGetDomainRoot()")
	public void fenixFrameworkGetters() {}

	// OTHER CLASSES (AND METHODS) THAT MAY BE IMPORTANT TO CATCH

	@Pointcut("execution(public * pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject.getExternalId(..)) || call(public * pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject.getExternalId(..))")
	public void fenixFrameworkAbstractDomainObjectGetExternalId() {}

	@Pointcut("execution(public * pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject.deleteDomainMetaObject(..)) || call(public * pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject.deleteDomainMetaObject(..))")
	public void fenixFrameworkAbstractDomainObjectDeleteDomainMetaObject() {} // This one isn't used on the domain classes

	@Pointcut("execution(public * pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject.deleteDomainObject(..)) || call(public * pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject.deleteDomainObject(..))")
	public void fenixFrameworkAbstractDomainObjectDeleteDomainObject() {}

	@Pointcut("fenixFrameworkAbstractDomainObjectGetExternalId() || fenixFrameworkAbstractDomainObjectDeleteDomainMetaObject() || fenixFrameworkAbstractDomainObjectDeleteDomainObject()")
	public void otherMethodsThatMayBeImportant() {}

	// KIEKER METHOD

	@Pointcut("(execution(* *(..)) && publicGettersAndSetters() && noGettersOrSettersWithFenixFramework() || publicAddsAndRemoves() || fenixFrameworkGetters() || otherMethodsThatMayBeImportant())") 
	public void monitoredOperation() {
		// Aspect Declaration (MUST be empty)
	}

	// Possible conflict between methods of fenix framework classes. Execute a deeper functional testing on the website to verify logs
	// Last filter to do: Weave only controllers with @RequestMapping
}