package com.jdiaz.users.service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="auth-server")
public interface AuthFeignClient {

	@GetMapping("/encrypt-password")
	public String encryptPassword(@RequestParam String password);

}
