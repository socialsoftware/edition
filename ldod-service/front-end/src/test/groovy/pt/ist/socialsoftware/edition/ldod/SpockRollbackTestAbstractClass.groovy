//package pt.ist.socialsoftware.edition.ldod
//
//import pt.ist.fenixframework.FenixFramework
//import pt.ist.fenixframework.core.WriteOnReadError
//import pt.ist.socialsoftware.edition.ldod.frontend.text.FeTextRequiresInterface
//import pt.ist.socialsoftware.edition.ldod.frontend.utils.Bootstrap
//import pt.ist.socialsoftware.edition.ldod.frontend.utils.LdoDLoadException
//import pt.ist.socialsoftware.edition.recommendation.domain.RecommendationModule
//import pt.ist.socialsoftware.edition.user.domain.UserModule
//import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface
//import pt.ist.socialsoftware.edition.virtual.domain.VirtualModule
//import pt.ist.socialsoftware.edition.virtual.feature.inout.VirtualEditionsTEICorpusImport
//import spock.lang.Specification
//
//import javax.transaction.NotSupportedException
//import javax.transaction.SystemException
//
//abstract class SpockRollbackTestAbstractClass extends Specification {
//
//    def setup() throws Exception {
//        try {
//            FenixFramework.getTransactionManager().begin(false)
//            setUpDatabaseWithCorpus()
//            populate4Test()
//        } catch (WriteOnReadError | NotSupportedException | SystemException e1) {
//            e1.printStackTrace()
//        }
//    }
//
//    def cleanup() {
//        try {
//
//            TextProvidesInterface.cleanFragmentMapCache()
//            TextProvidesInterface.cleanScholarInterMapCache()
//            VirtualProvidesInterface.cleanVirtualEditionInterMapByUrlIdCache()
//            VirtualProvidesInterface.cleanVirtualEditionInterMapByXmlIdCache()
//            VirtualProvidesInterface.cleanVirtualEditionMapCache()
//            FenixFramework.getTransactionManager().rollback()
//        } catch (IllegalStateException | SecurityException | SystemException e) {
//            e.printStackTrace()
//        }
//    }
//
//    abstract def populate4Test()
//
//    def setUpDatabaseWithCorpus() {
//        cleanDatabaseButCorpus();
//        Bootstrap.initializeSystem();
//        loadCorpus();
//    }
//
//    def loadCorpus() {
//        FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface()
//        if (feTextRequiresInterface.getSortedExpertEditionsDto().isEmpty()) {
//            String testFilesDirectory = PropertiesManager.getProperties().getProperty("test.files.dir")
//            File directory = new File(testFilesDirectory)
//            String filename = "corpus.xml"
//            File file = new File(directory, filename)
////            LoadTEICorpus corpusLoader1 = new LoadTEICorpus()
////            corpusLoader1.loadTEICorpus(new FileInputStream(file))
//            feTextRequiresInterface.getLoaderTEICorpus(new FileInputStream(file))
//            VirtualEditionsTEICorpusImport corpusLoader = new VirtualEditionsTEICorpusImport()
//            corpusLoader.loadTEICorpusVirtual(new FileInputStream(file))
//        }
//    }
//
//    def cleanDatabaseButCorpus() {
////        TextModule text = TextModule.getInstance()
//        FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface()
//        UserModule userModule = UserModule.getInstance()
//        VirtualModule ldoD = VirtualModule.getInstance()
//        RecommendationModule recommendationModule = RecommendationModule.getInstance()
//        if (ldoD != null) {
//            for (def user : userModule.getUsersSet()) {
//                if (!(user.getUsername().equals("ars") || user.getUsername().equals("Twitter"))) {
//                    user.remove()
//                }
//            }
//            for (def frag : feTextRequiresInterface.getFragmentDtoSet()) {
//                feTextRequiresInterface.removeFragmentByExternalId(frag.getExternalId())
////                frag.remove()
//            }
//            for (def cit : ldoD.getCitationSet()) {
//                cit.remove()
//            }
//            for (def uc : userModule.getUserConnectionSet()) {
//                uc.remove()
//            }
//            for (def t : userModule.getTokenSet()) {
//                t.remove()
//            }
//            for (def ve : ldoD.getVirtualEditionsSet()) {
//                if (!ve.getAcronym().equals(VirtualEdition.ARCHIVE_EDITION_ACRONYM)) {
//                    ve.remove()
//                }
//                for (def t : ldoD.getTweetSet()) {
//                    t.remove()
//                }
//            }
//            for (def r : recommendationModule.getRecommendationWeightsSet()) {
//                r.remove()
//            }
//        }
//        cleanRecommendationCache()
//    }
//
//    def loadFragments(def fragmentsToLoad) throws LdoDLoadException, FileNotFoundException {
//        def testFilesDirectory = PropertiesManager.getProperties().getProperty("test.files.dir");
//        def directory = new File(testFilesDirectory);
//
//        def fragmentFiles = fragmentsToLoad;
//
//        def file;
//        FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface()
//        for (def i = 0; i < fragmentFiles.length; i++) {
//            file = new File(directory, fragmentFiles[i]);
////            def fragmentLoader = new LoadTEIFragments();
////            fragmentLoader.loadFragmentsAtOnce(new FileInputStream(file));
//            feTextRequiresInterface.getLoadTEIFragmentsAtOnce(new FileInputStream(file))
//        }
//        feTextRequiresInterface.getFragmentCorpusGenerator()
//        //new VirtualEditionFragmentsTEIImport().LoadFragmentsCorpus()
//    }
//
//    def cleanRecommendationCache() {
//        StoredVectors.cleanCache()
//    }
//
//}
