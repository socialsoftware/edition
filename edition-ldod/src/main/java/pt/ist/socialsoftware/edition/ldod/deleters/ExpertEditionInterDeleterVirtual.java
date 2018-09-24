package pt.ist.socialsoftware.edition.ldod.deleters;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.search.Indexer;
import pt.ist.socialsoftware.edition.ldod.topicmodeling.TopicModeler;
import pt.ist.socialsoftware.edition.text.deleters.ExpertEditionInterDeleter;
import pt.ist.socialsoftware.edition.text.domain.ExpertEditionInter;

import java.util.ArrayList;
import java.util.List;

public class ExpertEditionInterDeleterVirtual extends ExpertEditionInterDeleter {

    @Override
    public void remove(ExpertEditionInter expertEditionInter) {

        for (VirtualEditionInter inter: expertEditionInter.getIsUsedBySet()){
            // it is necessary to remove all interpretations that use the expert
            // interpretation
            for (VirtualEditionInter inter1: DeleterVirtual.allUsesVirtualEdition(inter)) {
                inter1.remove();
            }
            inter.remove();
        }


//        remove from Lucene
        String externalId = expertEditionInter.getExternalId();
        List<String> externalIds = new ArrayList<>();
        externalIds.add(externalId);
        Indexer indexer = Indexer.getIndexer();
        indexer.cleanMissingHits(externalIds);

        // remove from mallet directory
        TopicModeler topicModeler = new TopicModeler();
        topicModeler.deleteFile(externalId);

        super.remove(expertEditionInter);
    }
}
