package pt.ist.socialsoftware.edition.ldod.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.FenixFramework;

public class UserModule extends UserModule_Base {
    public static Logger logger = LoggerFactory.getLogger(UserModule.class);

    public static UserModule getInstance() {
        return FenixFramework.getDomainRoot().getUserModule();
    }

    public UserModule() {
        FenixFramework.getDomainRoot().setUserModule(this);
    }

}
