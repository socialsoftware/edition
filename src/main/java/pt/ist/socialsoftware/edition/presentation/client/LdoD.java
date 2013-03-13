package pt.ist.socialsoftware.edition.presentation.client;

import pt.ist.socialsoftware.edition.presentation.client.view.ReadersView;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LdoD implements EntryPoint {

	private final LdoDServiceAsync rpcService = GWT.create(LdoDService.class);

	private final VerticalPanel mainPanel = new VerticalPanel();
	private final TabPanel servicesPanel = new TabPanel();

	@Override
	public void onModuleLoad() {
		GWT.log("presentation.client.LdoD::onModuleLoad() - begin");

		rpcService.initDatabase(new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				GWT.log("presentation.client.LdoD::onModuleLoad()::rpcService.initDatabase.sucess");

				loadReadersView();

			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("presentation.client.LdoD::onModuleLoad()::rpcService.initDatabase");
				GWT.log("-- Throwable: '" + caught.getClass().getName() + "'");
				Window.alert("Not able to init database: "
						+ caught.getMessage());

			}
		});

		GWT.log("presentation.client.LdoD::onModuleLoad() - done!");

	}

	private void loadReadersView() {
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {

				servicesPanel.add(new Label("LdoD Prototype"), "About");
				servicesPanel.add(new ReadersView(result), "Readers View");
				servicesPanel.selectTab(0);

				mainPanel.add(servicesPanel);

				RootPanel.get("LdoD").add(mainPanel);
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("presentation.client.LdoD::onModuleLoad()::rpcService.plainTransByInter");
				GWT.log("-- Throwable: '" + caught.getClass().getName() + "'");
				Window.alert("Not able to get transcription: "
						+ caught.getMessage());
			}
		};

		rpcService.plainTransByInter(callback);
	}
}