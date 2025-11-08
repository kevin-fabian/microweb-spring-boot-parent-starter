package com.github.fabiankevin.quickstart.web.controllers;

import com.github.fabiankevin.quickstart.web.controllers.dtos.SimpleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Profile("autoconfig-test")
public class TestExceptionController {

    private final TestService service;

    @GetMapping("/api-ex")
    public void apiException() {
        service.api();
    }

    @GetMapping("/access-denied")
    public void accessDenied() {
        service.accessDenied();
    }

    @PostMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void json(@RequestBody SimpleDto dto) {
        service.json(dto);
    }

    @GetMapping("/param")
    public void requireParam(@RequestParam int id) {
        service.requireParam(id);
    }

    @GetMapping("/method-only")
    public String methodOnly() {
        return service.methodOnly();
    }

    @PostMapping("/upload")
    public void upload() {
        service.upload();
    }

    @PutMapping("/put")
    public void put() {
        service.put();
    }

    @PatchMapping("/patch")
    public void patch() {
        service.patch();
    }

    @DeleteMapping("/delete")
    public void delete() {
        service.delete();
    }
}
