package pt.ist.socialsoftware.edition.presentation.client.view;

import pt.ist.socialsoftware.edition.presentation.client.controler.ReadersControler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReadersView extends VerticalPanel {
	VerticalPanel verticalPanel = new VerticalPanel();

	String transcription = null;

	HTMLPanel htmlPanel = null;

	RichTextArea textArea = null;

	public ReadersView(String transcription) {
		GWT.log("presentation.client.view.ReadersView::constructor()");

		this.transcription = transcription;

		textArea = new RichTextArea();
		textArea.setHTML(this.transcription);

		htmlPanel = new HTMLPanel(transcription);

		DecoratorPanel decoratorPanel = new DecoratorPanel();

		// decoratorPanel.add(htmlPanel);
		decoratorPanel.add(textArea);

		verticalPanel.add(decoratorPanel);
		add(verticalPanel);

		final ReadersControler readersControler = new ReadersControler(this);

		System.out.println(htmlPanel.getElement());

		htmlPanel.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				readersControler.handleEvent(event);
			}
		}, ClickEvent.getType());

		// htmlPanel.addDomHandler(new SelectionHandler<String>(){
		// @Override
		// public void onSelection(SelectionEvent<String> event){
		// readersControler.handleEvent(event);
		// },SelectionEvent<String>);

	}
}
