package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.*;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
//import static org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder.webAppContextSetup;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

/**
 * Created by Vernon on 2/23/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MethodLevelSecurityTests {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Before
    public void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void rejectsMethodInvocationsForNoAuth() {

        try {
            bookRepository.findAll();
            fail("Expected a security error");
        } catch (AuthenticationCredentialsNotFoundException e) {
            // expected
        }

        try {
            bookRepository.findByTitleContains("Spring");
            fail("Expected a security error");
        } catch (AuthenticationCredentialsNotFoundException e) {
            // expected
        }

        try {
            bookRepository.save(new Book("MacBook Pro", "...", LocalDate.of(2016, 06, 28), new Money(new BigDecimal(45.83)),
                    authorRepository.save(new Author("Blow", "Smith"))));
            fail("Expected a security error");
        } catch (AuthenticationCredentialsNotFoundException e) {
            // expected
        }
    }

    @Test
    public void rejectsMethodInvocationsForAuthWithInsufficientPermissions() {

        SecurityUtils.runAs("system", "system", "ROLE_USER");

        bookRepository.findAll();
        bookRepository.findByTitleContains("Spring");

        try {
            bookRepository.save(new Book("MacBook Pro", "...", LocalDate.of(2016, 06, 28), new Money(new BigDecimal(45.83)),
                    authorRepository.save(new Author("Blow", "Smith"))));
            fail("Expected a security error");
        } catch (AccessDeniedException e) {
            // expected
        }

        try {
            bookRepository.delete(1L);
            fail("Expected a security error");
        } catch (AccessDeniedException e) {
            // expected
        }
    }

    @Test
    public void allowsMethodInvocationsForAuthWithSufficientPermissions() {

        SecurityUtils.runAs("system", "system", "ROLE_USER", "ROLE_ADMIN");

        bookRepository.findAll();
        bookRepository.findByTitleContains("Spring");
        bookRepository.save(new Book("MacBook Pro", "...", LocalDate.of(2016, 06, 28), new Money(new BigDecimal(45.83)),
                authorRepository.save(new Author("Blow", "Smith"))));
        bookRepository.delete(1L);
    }
}
