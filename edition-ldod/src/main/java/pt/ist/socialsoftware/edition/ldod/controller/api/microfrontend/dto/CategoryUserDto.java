package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Category;

public class CategoryUserDto {
	private UserDto user;
	private CategoryDto category;

	public CategoryUserDto(Category cat, UserDto user) {
		this.setUser(user);
		this.setCategory(new CategoryDto(cat));
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public CategoryDto getCategory() {
		return category;
	}

	public void setCategory(CategoryDto category) {
		this.category = category;
	}
}
