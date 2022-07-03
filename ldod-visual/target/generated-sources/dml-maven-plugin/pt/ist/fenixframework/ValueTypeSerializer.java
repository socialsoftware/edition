package pt.ist.fenixframework;

import com.google.gson.JsonObject;
import pt.ist.fenixframework.util.JsonConverter;

@SuppressWarnings("all")
public final class ValueTypeSerializer {
    
    // VT: BackingArrays serializes as java.io.Serializable
    public static final java.io.Serializable serialize$BackingArrays(pt.ist.fenixframework.adt.bplustree.DoubleArray obj) {
        return (obj == null) ? null : (java.io.Serializable)pt.ist.fenixframework.adt.bplustree.AbstractNodeArray.externalizeArrays(obj);
    }
    public static final pt.ist.fenixframework.adt.bplustree.DoubleArray deSerialize$BackingArrays(java.io.Serializable obj) {
        return (obj == null) ? null : (pt.ist.fenixframework.adt.bplustree.DoubleArray)pt.ist.fenixframework.adt.bplustree.AbstractNodeArray.internalizeArrays(obj);
    }
    
    // VT: DomainObjectMap serializes as java.lang.String
    public static final java.lang.String serialize$DomainObjectMap(java.util.TreeMap obj) {
        return (obj == null) ? null : (java.lang.String)pt.ist.fenixframework.adt.bplustree.DomainLeafNode.externalizeDomainObjectMap(obj);
    }
    public static final java.util.TreeMap deSerialize$DomainObjectMap(java.lang.String obj) {
        return (obj == null) ? null : (java.util.TreeMap)pt.ist.fenixframework.adt.bplustree.DomainLeafNode.internalizeDomainObjectMap(obj);
    }
    
    // VT: PredicateMethod serializes as java.lang.String
    public static final java.lang.String serialize$PredicateMethod(java.lang.reflect.Method obj) {
        return (obj == null) ? null : (java.lang.String)obj.toString();
    }
    public static final java.lang.reflect.Method deSerialize$PredicateMethod(java.lang.String obj) {
        return (obj == null) ? null : (java.lang.reflect.Method)pt.ist.fenixframework.consistencyPredicates.Externalization.internalizePredicateMethod(obj);
    }
    
    // VT: GenericTreeMap serializes as java.io.Serializable
    public static final java.io.Serializable serialize$GenericTreeMap(java.util.TreeMap obj) {
        return (obj == null) ? null : (java.io.Serializable)pt.ist.fenixframework.adt.bplustree.AbstractNode.externalizeTreeMap(obj);
    }
    public static final java.util.TreeMap deSerialize$GenericTreeMap(java.io.Serializable obj) {
        return (obj == null) ? null : (java.util.TreeMap)pt.ist.fenixframework.adt.bplustree.AbstractNode.internalizeTreeMap(obj);
    }
    
    // VT: ModuleData serializes as com.google.gson.JsonElement
    public static final com.google.gson.JsonElement serialize$ModuleData(pt.ist.fenixframework.data.ModuleData obj) {
        return (obj == null) ? null : (com.google.gson.JsonElement)obj.json();
    }
    public static final pt.ist.fenixframework.data.ModuleData deSerialize$ModuleData(com.google.gson.JsonElement obj) {
        return (obj == null) ? null : (pt.ist.fenixframework.data.ModuleData)new pt.ist.fenixframework.data.ModuleData(obj);
    }
    
    // VT: OidIndexedMap serializes as java.lang.String
    public static final java.lang.String serialize$OidIndexedMap(java.util.TreeMap obj) {
        return (obj == null) ? null : (java.lang.String)pt.ist.fenixframework.adt.bplustree.DomainInnerNode.externalizeOidIndexedMap(obj);
    }
    public static final java.util.TreeMap deSerialize$OidIndexedMap(java.lang.String obj) {
        return (obj == null) ? null : (java.util.TreeMap)pt.ist.fenixframework.adt.bplustree.DomainInnerNode.internalizeOidIndexedMap(obj);
    }
    
}
