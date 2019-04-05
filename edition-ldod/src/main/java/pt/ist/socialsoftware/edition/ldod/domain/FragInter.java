package pt.ist.socialsoftware.edition.ldod.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public abstract class FragInter extends FragInter_Base {
    private static final Logger logger = LoggerFactory.getLogger(FragInter.class);

    public abstract boolean belongs2Edition(Edition edition);

    public abstract FragInter getLastUsed();

    public abstract String getReference();

    // solução a funcionar
    public abstract Set<HumanAnnotation> getAllDepthHumanAnnotations();

    // tentativa de suporte de ambas as anotações
    public abstract Set<Annotation> getAllDepthAnnotations();

    public abstract Set<Tag> getAllDepthTags();

    public abstract Set<Category> getAllDepthCategories();

    public abstract int getUsesDepth();

    public abstract Fragment getFragment();

}