package com.bruno.loja.controller;

import static org.springframework.http.ResponseEntity.ok;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bruno.loja.model.AutorizacaoDto;
import com.bruno.loja.repository.UsuarioRepository;
import com.bruno.loja.secutiry.jwt.JwtTokenProvider;


@RestController
@RequestMapping("/auth")
public class AutorizacaoController {
	
	
		@Autowired
		private AuthenticationManager authenticationManager;
		
		@Autowired
		private JwtTokenProvider tokenProvider;
		
		@Autowired
		private UsuarioRepository userRepository;
		
		@SuppressWarnings("rawtypes")
		@PostMapping("/login")
		public ResponseEntity login(@RequestBody AutorizacaoDto data){
			try {
				var username = data.getUsername();
				var pasword = data.getPassword();
				
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, pasword));
				
				var user = userRepository.findByUsername(username);
				
				var token = "";
				
				if (user != null) {
					token = tokenProvider.createToken(username, user.getRoles());
				} else {
					throw new UsernameNotFoundException("Usuario nao encontrado!");
				}
				Map<Object, Object> model = new HashMap<>();
				model.put("username", username);
				model.put("token", token);
				return ok(model);
			}catch (AuthenticationException e) {
				throw new BadCredentialsException("Usuario ou senha invalida!");	
			}
		}
		
	}
