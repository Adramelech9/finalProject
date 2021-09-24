package com.itransition.training.finalTask.Math;

import com.itransition.training.finalTask.Math.models.Role;
import com.itransition.training.finalTask.Math.models.User;
import com.itransition.training.finalTask.Math.repository.UserRepository;
import com.itransition.training.finalTask.Math.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@SpringBootApplication
@RestController
public class UsersApplication extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserRepository userRepository;
	public static String id;
	@GetMapping("/user")
	public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {

		this.id = principal.getName();
		userRepository.findById(id).orElseGet(() -> {
			User newUser = new User();
			newUser.setId(id);
			newUser.setUsername(principal.getAttribute("name"));
			newUser.setRoles(Collections.singleton(Role.USER));
			userRepository.save(newUser);
			return newUser;
		});
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
			SimpleUrlAuthenticationFailureHandler handler =
					new SimpleUrlAuthenticationFailureHandler("/");

			http.antMatcher("/**")
					.authorizeRequests(a -> a
							.antMatchers("/", "/error", "/webjars/**", "/exercises/*").permitAll()
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
	@Autowired
	private UserService userService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService);
	}

	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}
}
