package pt.ist.socialsoftware.edition.virtual.feature.inout;

import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;


import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;
import pt.ist.socialsoftware.edition.notification.utils.LdoDLoadException;
import pt.ist.socialsoftware.edition.virtual.api.VirtualRequiresInterface;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualModule;
import pt.ist.socialsoftware.edition.virtual.feature.topicmodeling.CorpusGenerator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

public class GenerateTEIFragmentsCorpus {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GenerateTEIFragmentsCorpus.class);


    private VirtualModule virtualModule = null;

    public GenerateTEIFragmentsCorpus() { }


    public void LoadFragmentCorpus(Set<FragmentDto> fragments) {
        this.virtualModule = VirtualModule.getInstance();
        fragments.forEach(fragmentDto -> generateCorpus(fragmentDto));
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    private void generateCorpus(FragmentDto fragment) {

        // generate corpus in corpus.dir
        CorpusGenerator generator = new CorpusGenerator();
        try {
            generator.generate(fragment);
        } catch (FileNotFoundException e1) {
            throw new LdoDLoadException(
                    "LoadTEIFragments.loadFragment erro FileNotFoundException a gerar corpus do fragmento "
                            + fragment.getXmlId() + e1.getMessage());
        } catch (IOException e1) {
            throw new LdoDLoadException("LoadTEIFragments.loadFragment erro IOException a gerar corpus do fragmento "
                    + fragment.getXmlId());
        }

        // generate index in indexer.dir
          for (ScholarInterDto interDto: fragment.getEmbeddedScholarInterDtos()) {
            try {
//                Indexer indexer = Indexer.getIndexer();
//                indexer.addDocument(interDto);
//                textProvidesInterface.addDocumentToIndexer(interDto);
                VirtualRequiresInterface.getInstance().addDocumentToIndexer(interDto.getXmlId());

            } catch (FileNotFoundException e) {
                throw new LdoDLoadException(
                        "LoadTEIFragments.loadFragment erro FileNotFoundException a gerar index da interpretação "
                                + interDto.getXmlId());
            } catch (IOException e) {
                throw new LdoDLoadException(
                        "LoadTEIFragments.loadFragment erro IOException a gerar index da interpretação "
                                + interDto.getXmlId());
            }
        }

        VirtualEdition archiveEdition = this.virtualModule.getArchiveEdition();
        ScholarInterDto sourceInter = fragment.getSortedSourceInter().get(fragment.getSortedSourceInter().size() - 1);

        // if the representative fragment interpretation is not in
        // the archive edition we have to add it
        if (archiveEdition != null
                && !archiveEdition.getIntersSet().contains(sourceInter)) {
            logger.debug("loadFragment ldod-edition-size:{}", archiveEdition.getAllDepthVirtualEditionInters().size());
//            archiveEdition.createVirtualEditionInter(new ScholarInterDto(sourceInter.getXmlId()),
//                    archiveEdition.getMaxFragNumber() + 1);
            archiveEdition.createVirtualEditionInter(VirtualRequiresInterface.getInstance().getScholarInterByXmlId(sourceInter.getXmlId()),
                    archiveEdition.getMaxFragNumber() + 1);
        }


//         uncomment when a pretty print of the result of load is required in
//         stdout
//         Element rootElement = new Element(fragment.getXmlId(),
//         Namespace.getNamespace("http://www.tei-c.org/ns/1.0"));
//         JDomTEITextPortionWriter writer = new
//         JDomTEITextPortionWriter(rootElement,
//         fragment.getFragmentInterSet());
//         writer.visit((AppText) fragment.getTextPortion());
//         XMLOutputter xml = new XMLOutputter();
//         xml.setFormat(Format.getPrettyFormat());
//         System.out.println(xml.outputString(rootElement));

    }

}
