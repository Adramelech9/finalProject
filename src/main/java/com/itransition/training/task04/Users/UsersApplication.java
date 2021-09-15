package com.itransition.training.task04.Users;

import com.itransition.training.task04.Users.models.TableUsers;
import com.itransition.training.task04.Users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@SpringBootApplication
@RestController
public class UsersApplication extends WebSecurityConfigurerAdapter {
	private final UserRepository userRepository;

	public UsersApplication(UserRepository userRepository) {
		this.userRepository = userRepository;
	}



	@GetMapping("/user")
	public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal, Model model) {

		String id = principal.getName();
		TableUsers tableUsers = userRepository.findById(id).orElseGet(() -> {
			TableUsers newUser = new TableUsers();
			newUser.setSocialNetwork(id.length() > 20 ? "google" : "facebook");
			newUser.setId(id);
			newUser.setUserName(principal.getAttribute("name"));
			newUser.setActive(true);
			newUser.setFirstEntry(LocalDateTime.now());

			return newUser;
		});
		tableUsers.setLastEntry(LocalDateTime.now());
		userRepository.save(tableUsers);

		model.addAttribute("numIsFacebook" , userRepository.countBySocialNetwork("facebook"));
		return Collections.singletonMap("name", principal.getAttribute("name"));
	}

	@GetMapping("/error")
	@ResponseBody
	public String error(HttpServletRequest request) {
		String message = (String) request.getSession().getAttribute("error.message");
		request.getSession().removeAttribute("error.message");
		return message;
	}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			SimpleUrlAuthenticationFailureHandler handler = new SimpleUrlAuthenticationFailureHandler("/");

			http.antMatcher("/**")
					.authorizeRequests(a -> a
							.antMatchers("/", "/error", "/webjars/**").permitAll()
							.anyRequest().authenticated()
					)
					.exceptionHandling(e -> e
							.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
					)
					.csrf(c -> c
							.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
					)
					.logout(l -> l
							.logoutSuccessUrl("/").permitAll()
					)
					.oauth2Login(o -> o
							.failureHandler((request, response, exception) -> {
								request.getSession().setAttribute("error.message", exception.getMessage());
								handler.onAuthenticationFailure(request, response, exception);
							})
					);
	}

	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}
}
