package com.jdiaz.users.service.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="auth-server")
public interface AuthFeignClient {

}
