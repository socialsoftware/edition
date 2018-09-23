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
		String fileSuffix = "livroIDs.txt";
		File file = new File("C:\\Users\\dnf_o\\projetoTese\\ldod\\social\\teste\\" + fileSuffix);

		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

		String line = null;
		int TP = 0;
		int FP = 0;
		int TN = 0;
		int FN = 0;
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
			} else if (tc != null && !isCitation) {
				FP++;
			} else if (tc == null && isCitation) {
				FN++;
			} else if (tc == null && !isCitation) {
				TN++;
			}

		}

		logger.debug("\nTP: " + TP + "\n" + "FP: " + FP + "\n" + "TN: " + TN + "\n" + "FN: " + FN);
		logger.debug("+++++++++++++++++++++++++++++++++");

		// for (TwitterCitation tc : LdoD.getInstance().getAllTwitterCitation()) {
		// logger.debug("Date: " + tc.getDate() + " Tweet ID: " + tc.getTweetID() + "\n"
		// + "Tweet text: "
		// + tc.getTweetText() + "\n");
		// }

		logger.debug("FINISHED PERFORMANCE ANALYSIS");

		bufferedReader.close();
	}

}
