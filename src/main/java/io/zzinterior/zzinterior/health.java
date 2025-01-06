package io.zzinterior.zzinterior;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
@Slf4j
@RequestMapping("/api")
public class health {
    @GetMapping("/health")
    @ResponseStatus(HttpStatus.OK)
    public void getHealth(){
        log.info("ok");
    }
}
