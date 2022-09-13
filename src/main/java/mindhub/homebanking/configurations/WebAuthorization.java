package mindhub.homebanking.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/loans").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/clients/current/**").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET, "/api/clients", "/api/accounts", "/api/clients/**", "/api/cards").hasAuthority("ADMIN")
                .antMatchers("/web/accounts.html", "/web/account.html", "/web/cards.html", "/web/create-account.html", "/web/create-cards.html", "/web/loan-application.html", "/web/transfers.html").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.PATCH, "/api/clients/current/**").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.DELETE, "/admin/", "/api/cards/delete").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/clients/current/**", "/api/transactions", "/api/clientloans").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/admin/**").hasAuthority("ADMIN")
                .antMatchers("/manager", "/rest/**", "/h2-console/**", "./admin/**").hasAuthority("ADMIN")
                .antMatchers("/index.html", "/web/styles.css", "/web/wickedcss.min.css", "/web/main.js", "/web/imagenes/**", "/api/login"  ).permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .antMatchers("/api/loans").permitAll();





        http.formLogin()

                .usernameParameter("email")

                .passwordParameter("password")

                .loginPage("/api/login");


        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF tokens

        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed
        http
                .headers()
                        .frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }
    }
}
