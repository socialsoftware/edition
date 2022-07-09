package pt.ist.fenixframework.backend;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;

@SuppressWarnings("all")
public class CurrentBackEndId extends BackEndId {
    
    public String getBackEndName() {
        return "jvstm-ojb";
    }
    
    public Class<? extends pt.ist.fenixframework.Config> getDefaultConfigClass() {
        try {
            return (Class<? extends pt.ist.fenixframework.Config>)Class.forName("pt.ist.fenixframework.backend.jvstmojb.JvstmOJBConfig");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }
    
    public Class<? extends pt.ist.fenixframework.core.AbstractDomainObject> getDomainClassRoot() {
        return pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.class;
    }
    
    public String getAppName() {
        return "microfrontend";
    }
    
}
