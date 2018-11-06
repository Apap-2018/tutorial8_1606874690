package com.apap.tutorial8.service;

import com.apap.tutorial8.model.UserRoleModel;

public interface UserRoleService {
	UserRoleModel addUser(UserRoleModel user);
	void updatePassword(UserRoleModel user, String passwordBaru);
	public String encrypt(String password);
}
