package pt.ist.socialsoftware.edition.presentation.client.controler;

import pt.ist.socialsoftware.edition.presentation.client.view.ReadersView;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.Window;

public class ReadersControler {

	private ReadersView view = null;

	public ReadersControler(ReadersView view) {
		this.view = view;
	}

	public void handleEvent(SelectionEvent<String> event) {

		Window.alert("Selection: OK");

		// AsyncCallback<Void> callback = new AsyncCallback<Void>() {
		// @Override
		// public void onSuccess(Void result) {
		// GWT.log("presentationserver.client.AnaCom::onModuleLoad()::rpcService.createMobile.sucess");
		// Window.alert("Selection: OK");
		// }
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// GWT.log("");
		// GWT.log("-- Throwable: '" + caught.getClass().getName() + "'");
		//
		// Window.alert("Selection: OK");
		//
		// }
		// };
		//
	}

	public void handleEvent(ClickEvent event) {

		Window.alert("Click: OK" + event.toString() + event.getX());

		// AsyncCallback<Void> callback = new AsyncCallback<Void>() {
		// @Override
		// public void onSuccess(Void result) {
		// GWT.log("presentationserver.client.AnaCom::onModuleLoad()::rpcService.createMobile.sucess");
		// Window.alert("Selection: OK");
		// }
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// GWT.log("");
		// GWT.log("-- Throwable: '" + caught.getClass().getName() + "'");
		//
		// Window.alert("Selection: OK");
		//
		// }
		// };
		//
	}

}
