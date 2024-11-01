package com.estsoft.estsoft2ndproject.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.estsoft.estsoft2ndproject.custonException.AdditionalInformationRequireException;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class OAuth2ExceptionHandler {
	@ExceptionHandler(AdditionalInformationRequireException.class)
	public String handleAdditionalInfoException(AdditionalInformationRequireException e, RedirectAttributes attributes, HttpSession session) {
		log.error("AdditionalInformationRequireException: {}", e.getMessage());

		attributes.addFlashAttribute("email", session.getAttribute("email"));
		attributes.addFlashAttribute("nickname", session.getAttribute("nickname"));
		attributes.addFlashAttribute("profileImageUrl", session.getAttribute("profileImageUrl"));

		return "redirect:/member/register";
	}
}
