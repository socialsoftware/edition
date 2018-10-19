package pt.ist.socialsoftware.edition.ldod.social.aware;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation;

public class LucenePerformance {

	private Logger logger = LoggerFactory.getLogger(LucenePerformance.class);

	@Atomic(mode = TxMode.WRITE)
	public void runLivro() throws IOException {
		logger.debug("STARTED BOOK PERFORMANCE ANALYSIS");

		String fileSuffix = "livroIDs.txt";
		File file = new File("C:\\Users\\dnf_o\\projetoTese\\ldod\\social\\teste\\" + fileSuffix);

		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

		computePrecisionAndRecall(bufferedReader);

		bufferedReader.close();

		logger.debug("FINISHED BOOK PERFORMANCE ANALYSIS");

	}

	@Atomic(mode = TxMode.WRITE)
	public void runBernardo() throws IOException {
		logger.debug("STARTED BERNARDO PERFORMANCE ANALYSIS");

		String fileSuffix = "bernardoIDs.txt";
		File file = new File("C:\\Users\\dnf_o\\projetoTese\\ldod\\social\\teste\\" + fileSuffix);

		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

		computePrecisionAndRecall(bufferedReader);

		bufferedReader.close();

		logger.debug("FINISHED BERNARDO PERFORMANCE ANALYSIS");
	}

	@Atomic(mode = TxMode.WRITE)
	public void runFP() throws IOException {
		logger.debug("STARTED FP PERFORMANCE ANALYSIS");

		String fileSuffix = "fpIDs.txt";
		File file = new File("C:\\Users\\dnf_o\\projetoTese\\ldod\\social\\teste\\" + fileSuffix);

		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

		computePrecisionAndRecall(bufferedReader);

		bufferedReader.close();

		logger.debug("FINISHED FP PERFORMANCE ANALYSIS");
	}

	@Atomic(mode = TxMode.WRITE)
	public void runVicente() throws IOException {
		logger.debug("STARTED VICENTE PERFORMANCE ANALYSIS");

		String fileSuffix = "vicenteIDs.txt";
		File file = new File("C:\\Users\\dnf_o\\projetoTese\\ldod\\social\\teste\\" + fileSuffix);

		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

		computePrecisionAndRecall(bufferedReader);

		bufferedReader.close();

		logger.debug("FINISHED VICENTE PERFORMANCE ANALYSIS");
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
			TwitterCitation tc = LdoD.getInstance().getTwitterCitationByTweetID(id);

			if (tc != null && isCitation) {
				TP++;
				if (!tc.getInfoRangeSet().isEmpty()) {
					jaroTP++;
				} else {
					// logger.debug("Tweet ID: " + id + " is a Jaro FN!!");
					jaroFN++;
				}
			} else if (tc != null && !isCitation) {
				FP++;
				logger.debug("Tweet ID: " + id + " is FP");
				if (!tc.getInfoRangeSet().isEmpty()) {
					jaroFP++;
					// logger.debug("Tweet ID: " + id + " is a Jaro FP!!");
				} else {
					jaroTN++;
				}
			} else if (tc == null && isCitation) {
				FN++;
				logger.debug("Tweet ID: " + id + " is FN");
			} else if (tc == null && !isCitation) {
				TN++;
			}

		}

		logger.debug("\nTP: " + TP + "\n" + "FP: " + FP + "\n" + "TN: " + TN + "\n" + "FN: " + FN);
		logger.debug("+++++++++++++++++++++++++++++++++");

		double precision = (double) TP / (TP + FP);
		double recall = (double) TP / (TP + FN);

		logger.debug("Precision = " + precision);
		logger.debug("Recall = " + recall + "\n");

		// for (TwitterCitation tc : LdoD.getInstance().getAllTwitterCitation()) {
		// logger.debug("Date: " + tc.getDate() + " Tweet ID: " + tc.getTweetID() + "\n"
		// + "Tweet text: "
		// + tc.getTweetText() + "\n");
		// }

		logger.debug("==========================================================================");
		logger.debug("==========================================================================\n");

		logger.debug("\njaroTP: " + jaroTP + "\n" + "jaroFP: " + jaroFP + "\n" + "jaroTN: " + jaroTN + "\n" + "jaroFN: "
				+ jaroFN);
		logger.debug("+++++++++++++++++++++++++++++++++");

		double jaroPrecision = (double) jaroTP / (jaroTP + jaroFP);
		double jaroRecall = (double) jaroTP / (jaroTP + jaroFN);

		logger.debug("JaroPrecision = " + jaroPrecision);
		logger.debug("JaroRecall = " + jaroRecall);
	}

}
