package pt.ist.socialsoftware.edition.domain;

import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.Config.RepositoryType;
import pt.ist.fenixframework.FenixFramework;

public class DatabaseBootstrap {

	private static boolean notInitialized = true;

	public static void initDatabase() {
		if (notInitialized) {
			FenixFramework.initialize(new Config() {
				{
					dbAlias = "test-db";
					domainModelPath = "src/main/dml/edition.dml";
					repositoryType = RepositoryType.BERKELEYDB;
					rootClass = LdoD.class;
				}
			});
		}
		notInitialized = false;
	}

}
