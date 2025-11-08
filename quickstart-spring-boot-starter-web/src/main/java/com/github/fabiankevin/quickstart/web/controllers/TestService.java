package com.github.fabiankevin.quickstart.web.controllers;

import com.github.fabiankevin.quickstart.web.controllers.dtos.SimpleDto;

public interface TestService {
    void api();
    void accessDenied();
    void json(SimpleDto dto);
    void requireParam(int id);
    String methodOnly();
    void upload();
    void put();
    void patch();
    void delete();
}

