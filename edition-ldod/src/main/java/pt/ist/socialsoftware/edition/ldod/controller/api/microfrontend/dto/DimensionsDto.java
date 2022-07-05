package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Dimensions;

public class DimensionsDto {
	
	private float height;
	private float width;

	public DimensionsDto(Dimensions dimension) {
		this.setHeight(dimension.getHeight());
		this.setWidth(dimension.getWidth());
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}
}
