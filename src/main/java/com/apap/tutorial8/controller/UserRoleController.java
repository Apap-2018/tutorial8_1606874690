package com.apap.tutorial8.controller;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.apap.tutorial8.model.PasswordModel;
import com.apap.tutorial8.model.UserRoleModel;
import com.apap.tutorial8.repository.UserRoleDb;
import com.apap.tutorial8.service.UserRoleService;

@Controller
@RequestMapping("/user")
public class UserRoleController {

	@Autowired
	private UserRoleService userService;

	@Autowired
	private UserRoleDb userDb;
	
	private boolean validatePassword(String password) {
        if (password.length() >= 8 && Pattern.compile("[0-9]").matcher(password).find() && Pattern.compile("[a-zA-Z]").matcher(password).find()) {
           return true;
        }
      
        return false;
    }
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	private ModelAndView addUserSubmit(@ModelAttribute UserRoleModel user, RedirectAttributes redirect) {
			
		String message = "";
	
		if (this.validatePassword(user.getPassword())) {
			userService.addUser(user);
			message = null;
			
		} else {
			message = "Password yang dimasukkan harus terdiri atas angka dan huruf serta minimal memiliki 8 karakter";
		}
		
		ModelAndView redirectView = new ModelAndView("redirect:/");
		redirect.addFlashAttribute("message", message);
		return redirectView;
	}
	
	@RequestMapping(value = "/updatePass", method = RequestMethod.POST)
	private ModelAndView updatePasswordSubmit(@ModelAttribute PasswordModel password, Model model, RedirectAttributes redirect) {
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		UserRoleModel user = userDb.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		String passwordMessage = "";
		
		if (password.getNewPassword().equals(password.getConfirmPassword())) {
			if (passwordEncoder.matches(password.getOldPassword(), user.getPassword())) {
				
				if (this.validatePassword(password.getNewPassword())) {
					String passwordBaru = password.getNewPassword();
					userService.updatePassword(user, passwordBaru);
					passwordMessage = "Password Anda berhasil di-update";
					
				} else {
					passwordMessage = "Password yang dimasukkan harus terdiri atas angka dan huruf serta minimal memiliki 8 karakter";
				}
				
				
			} else {
				passwordMessage = "Password yang dimasukkan tidak sesuai dengan password lama Anda";
			}
			
		} else {
			passwordMessage = "Password baru tidak sesuai dengan yang dikonfirmasi";
		}
		
		ModelAndView redirectView = new ModelAndView("redirect:/updatepassword");
		redirect.addFlashAttribute("passwordMessage", passwordMessage);
		return redirectView;
	}

	
	
}
