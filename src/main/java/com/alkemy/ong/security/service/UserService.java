package com.alkemy.ong.security.service;

import javassist.NotFoundException;

public interface UserService {
    void delete(String id) throws NotFoundException;
}
