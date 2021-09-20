package io.eliteblue.erp.client.config;

import io.eliteblue.erp.client.handler.OAuth2LoginSuccessHandler;
import io.eliteblue.erp.core.service.ErpOAuthUserService;
import io.eliteblue.erp.core.service.ErpUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by aLeXcBa1990 on 24/11/2018.
 * 
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomAuthenticationProvider authProvider;

	@Autowired
	private ErpUserDetailsService erpUserDetailsService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private ErpOAuthUserService erpOAuthUserService;

	@Autowired
	private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		protected void configure(HttpSecurity http) throws Exception {
			// rest Login
			http.antMatcher("/admin/**").authorizeRequests().anyRequest().hasRole("SYS_ADMIN").and().httpBasic().and().csrf()
					.disable();
		}
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// form login
		http.authorizeRequests()
				.antMatchers("/", "/login.xhtml", "/javax.faces.resource/**").permitAll()
				.anyRequest()
				.fullyAuthenticated().and()
				.formLogin()
					.loginPage("/login.xhtml")
					.defaultSuccessUrl("/index.xhtml")
					.failureUrl("/login.xhtml?authfailed=true")
					.permitAll()
				.and()
				.oauth2Login()
					.loginPage("/login.xhtml")
					.userInfoEndpoint()
					.userService(erpOAuthUserService)
				.and()
				.successHandler(oAuth2LoginSuccessHandler)
				.and()
				.logout().logoutSuccessUrl("/login.xhtml")
				.logoutUrl("/j_spring_security_logout").and().csrf().disable();

		// allow to use ressource links like pdf
		http.headers().frameOptions().sameOrigin();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(bCryptPasswordEncoder);
		provider.setUserDetailsService(erpUserDetailsService);
		return provider;
	}

}
