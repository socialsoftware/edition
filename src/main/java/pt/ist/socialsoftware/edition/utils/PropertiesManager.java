package pt.ist.socialsoftware.edition.utils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.artifact.FenixFrameworkArtifact;
import pt.ist.fenixframework.project.DmlFile;
import pt.ist.fenixframework.project.exception.FenixFrameworkProjectException;
import pt.ist.fenixframework.pstm.dml.FenixDomainModelWithOCC;
import pt.ist.socialsoftware.edition.domain.LdoD;

public class PropertiesManager {

	private static final Properties properties = new Properties();

	private static List<URL> urls = null;

	static {
		try {
			properties.load(PropertiesManager.class
					.getResourceAsStream("/configuration.properties"));
		} catch (IOException e) {
			throw new RuntimeException("Unable to load properties files.", e);
		}
	}

	public static List<URL> getUrls() {
		if (urls == null) {
			urls = new ArrayList<URL>();
			try {
				for (DmlFile dml : FenixFrameworkArtifact.fromName(
						properties.getProperty("app.name"))
						.getFullDmlSortedList()) {
					urls.add(dml.getUrl());
				}
			} catch (FenixFrameworkProjectException e) {
				throw new Error(e);
			} catch (IOException e) {
				throw new Error(e);
			}
		}
		if (urls.isEmpty()) {
			System.err.println("domain Model URLs were not loaded");
		}

		return urls;
	}

	public static Config getFenixFrameworkConfig() {
		Config config = new Config() {
			{
				domainModelClass = FenixDomainModelWithOCC.class;
				appName = properties.getProperty("app.name");
				dbAlias = properties.getProperty("db.alias");
				dbUsername = properties.getProperty("db.username");
				dbPassword = properties.getProperty("db.password");
				domainModelPaths = new String[0];
				rootClass = LdoD.class;
				errorfIfDeletingObjectNotDisconnected = true;
			}

			@Override
			public List<URL> getDomainModelURLs() {
				return getUrls();
			}
		};
		for (URL url : config.getDomainModelURLs()) {
			System.out.println("Domain Model URL: " + url.toExternalForm());
		}
		return config;
	}

}