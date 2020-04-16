package com.patroclos.controllers;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.patroclos.security.AuthenticationFacade;

@Controller
public class DefaultController {

	@Autowired
	private AuthenticationFacade authenticationFacade;

	@GetMapping("/")
	public String index(Model model) {
		return "welcome.html";
	}
	
	@GetMapping("/welcome")
	public String welcome(Model model) {
		return "welcome.html";
	}
	
	@GetMapping("/processlogin")
	public String login(Model model) {
		return "login.html";
	}
	
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> user() {		
		Authentication authentication = authenticationFacade.getAuthentication();

		if (authentication.getPrincipal() instanceof String)
			if (authentication.getPrincipal().equals("anonymousUser"))
				return Collections.singletonMap("name",Strings.EMPTY);	

		String username = authentication.getName();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		}else if (principal instanceof DefaultOAuth2User) {
			DefaultOAuth2User oauthUser = (DefaultOAuth2User)principal;
			username =oauthUser.getAttribute("name");
		}

		return Collections.singletonMap("name",username);	
	}	
	
	@RequestMapping(value = "/userphoto", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> userphoto() {		
		Authentication authentication = authenticationFacade.getAuthentication();

		if (authentication.getPrincipal() instanceof String)
			if (authentication.getPrincipal().equals("anonymousUser"))
				return Collections.singletonMap("src",Strings.EMPTY);	

		String photoSrc = Strings.EMPTY;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		
		if (authentication!=null && authentication instanceof OAuth2AuthenticationToken) {
			if (authentication.isAuthenticated()){
					OAuth2AuthenticationToken token = (OAuth2AuthenticationToken)authentication;
					String authorisationProvider = token.getAuthorizedClientRegistrationId();
					
					if (authorisationProvider.equals("facebook"))
						if (principal instanceof DefaultOAuth2User) {
							DefaultOAuth2User oauthUser = (DefaultOAuth2User)principal;
							LinkedHashMap<?, ?> pictureMap = (LinkedHashMap<?, ?>)oauthUser.getAttribute("picture");
							LinkedHashMap<?, ?> pictureData = (LinkedHashMap<?, ?>) pictureMap.get("data");
							photoSrc = (String) pictureData.get("url");
						}
					
					
					if (authorisationProvider.equals("google"))
						if (principal instanceof DefaultOAuth2User) {
							DefaultOAuth2User oauthUser = (DefaultOAuth2User)principal;
							photoSrc = oauthUser.getAttribute("picture");
						}
					
				}
				
	    }
		
		System.out.println(photoSrc);
		
		return Collections.singletonMap("src",photoSrc);	
	}

}
