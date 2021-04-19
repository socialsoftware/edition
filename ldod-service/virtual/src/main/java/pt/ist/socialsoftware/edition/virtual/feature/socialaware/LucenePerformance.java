package pt.ist.socialsoftware.edition.virtual.feature.socialaware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.virtual.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualModule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LucenePerformance {

    private final Logger logger = LoggerFactory.getLogger(LucenePerformance.class);

    @Atomic(mode = TxMode.WRITE)
    public void runLivro() throws IOException {
        this.logger.debug("STARTED BOOK PERFORMANCE ANALYSIS");

        String fileSuffix = "livroIDs.txt";
        File file = new File("C:\\Users\\dnf_o\\projetoTese\\ldod\\social\\teste\\" + fileSuffix);

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        computePrecisionAndRecall(bufferedReader);

        bufferedReader.close();

        this.logger.debug("FINISHED BOOK PERFORMANCE ANALYSIS");

    }

    @Atomic(mode = TxMode.WRITE)
    public void runBernardo() throws IOException {
        this.logger.debug("STARTED BERNARDO PERFORMANCE ANALYSIS");

        String fileSuffix = "bernardoIDs.txt";
        File file = new File("C:\\Users\\dnf_o\\projetoTese\\ldod\\social\\teste\\" + fileSuffix);

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        computePrecisionAndRecall(bufferedReader);

        bufferedReader.close();

        this.logger.debug("FINISHED BERNARDO PERFORMANCE ANALYSIS");
    }

    @Atomic(mode = TxMode.WRITE)
    public void runFP() throws IOException {
        this.logger.debug("STARTED FP PERFORMANCE ANALYSIS");

        String fileSuffix = "fpIDs.txt";
        File file = new File("C:\\Users\\dnf_o\\projetoTese\\ldod\\social\\teste\\" + fileSuffix);

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        computePrecisionAndRecall(bufferedReader);

        bufferedReader.close();

        this.logger.debug("FINISHED FP PERFORMANCE ANALYSIS");
    }

    @Atomic(mode = TxMode.WRITE)
    public void runVicente() throws IOException {
        this.logger.debug("STARTED VICENTE PERFORMANCE ANALYSIS");

        String fileSuffix = "vicenteIDs.txt";
        File file = new File("C:\\Users\\dnf_o\\projetoTese\\ldod\\social\\teste\\" + fileSuffix);

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        computePrecisionAndRecall(bufferedReader);

        bufferedReader.close();

        this.logger.debug("FINISHED VICENTE PERFORMANCE ANALYSIS");
    }

    private void computePrecisionAndRecall(BufferedReader bufferedReader) throws IOException {
        String line = null;
        int TP = 0;
        int FP = 0;
        int TN = 0;
        int FN = 0;

        // só faz sentido alterar estas variáveis
        // quando a tc != null, senão nem há info ranges
        int jaroTP = 0;
        int jaroFP = 0;
        int jaroTN = 0;
        int jaroFN = 0;

        while ((line = bufferedReader.readLine()) != null) {
            String[] split = line.split(";");
            long id = Long.parseLong(split[0]);
            boolean isCitation = false;
            if (split[1].contains("yes")) {
                isCitation = true;
            }
            TwitterCitation tc = VirtualModule.getInstance().getTwitterCitationByTweetID(id);

            if (tc != null && isCitation) {
                TP++;
                if (!tc.getInfoRangeDtoSet().isEmpty()) {
                    jaroTP++;
                } else {
                    // logger.debug("Tweet ID: " + id + " is a Jaro FN!!");
                    jaroFN++;
                }
            } else if (tc != null && !isCitation) {
                FP++;
                this.logger.debug("Tweet ID: " + id + " is FP");
                if (!tc.getInfoRangeDtoSet().isEmpty()) {
                    jaroFP++;
                    // logger.debug("Tweet ID: " + id + " is a Jaro FP!!");
                } else {
                    jaroTN++;
                }
            } else if (tc == null && isCitation) {
                FN++;
                this.logger.debug("Tweet ID: " + id + " is FN");
            } else if (tc == null && !isCitation) {
                TN++;
            }

        }

        this.logger.debug("\nTP: " + TP + "\n" + "FP: " + FP + "\n" + "TN: " + TN + "\n" + "FN: " + FN);
        this.logger.debug("+++++++++++++++++++++++++++++++++");

        double precision = (double) TP / (TP + FP);
        double recall = (double) TP / (TP + FN);

        this.logger.debug("Precision = " + precision);
        this.logger.debug("Recall = " + recall + "\n");

        // for (TwitterCitation tc : VirtualModule.getInstance().getAllTwitterCitation()) {
        // logger.debug("Date: " + tc.getDate() + " Tweet ID: " + tc.getTweetID() + "\n"
        // + "Tweet text: "
        // + tc.getTweetText() + "\n");
        // }

        this.logger.debug("==========================================================================");
        this.logger.debug("==========================================================================\n");

        this.logger.debug("\njaroTP: " + jaroTP + "\n" + "jaroFP: " + jaroFP + "\n" + "jaroTN: " + jaroTN + "\n" + "jaroFN: "
                + jaroFN);
        this.logger.debug("+++++++++++++++++++++++++++++++++");

        double jaroPrecision = (double) jaroTP / (jaroTP + jaroFP);
        double jaroRecall = (double) jaroTP / (jaroTP + jaroFN);

        this.logger.debug("JaroPrecision = " + jaroPrecision);
        this.logger.debug("JaroRecall = " + jaroRecall);
    }

}
