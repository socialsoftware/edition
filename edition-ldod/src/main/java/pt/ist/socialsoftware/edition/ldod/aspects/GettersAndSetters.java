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

	/**
	 * This is a pointcut accepting all calls and executions of setter methods (methods which return void and start with {@code set}).
	 */
	@Pointcut("execution(public void pt.ist.socialsoftware.edition.ldod.domain.*.set*(..)) || call(public void pt.ist.socialsoftware.edition.ldod.domain.*.set*(..))")
	public void publicSetter() {} // NOPMD (Aspect)

	@Pointcut("execution(* set*(pt.ist.fenixframework..*, ..)) || call(* set*(pt.ist.fenixframework..*, ..))")
	public void setterWithFenixFrameworkObjectArgument() {} // NOPMD (Aspect)

	/**
	 * This is a pointcut accepting all calls and executions of getter methods (methods which return something and start with {@code get}).
	 */
	@Pointcut("execution(public * pt.ist.socialsoftware.edition.ldod.domain.*.get*(..)) || call(public * pt.ist.socialsoftware.edition.ldod.domain.*.get*(..))")
	public void publicGetter() {} // NOPMD (Aspect)

	@Pointcut("execution(pt.ist.fenixframework..* get*(..)) || call(pt.ist.fenixframework..* get*(..))")
	public void getterReturningFenixFrameworkObject() {} // NOPMD (Aspect)

	@Pointcut("publicGetter() || publicSetter()")
	public void publicGetterAndSetter() {} // NOPMD (Aspect)

	@Pointcut("!getterReturningFenixFrameworkObject() && !setterWithFenixFrameworkObjectArgument()")
	public void noGetterOrSetterWithFenixFramework() {} // NOPMD (Aspect)

	@Pointcut("(execution(* *(..)) && publicGetterAndSetter() && !setterWithFenixFrameworkObjectArgument())") 
	public void monitoredOperation() { // noSetterWithFenixFramework and the only one getter for fenix getDomainObject()
		// Aspect Declaration (MUST be empty)
	}
}