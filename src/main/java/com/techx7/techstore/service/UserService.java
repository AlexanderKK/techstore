package com.techx7.techstore.service;

import com.techx7.techstore.model.dto.user.RegisterDTO;
import com.techx7.techstore.model.entity.User;

public interface UserService {

    void register(RegisterDTO registerDTO);

}
