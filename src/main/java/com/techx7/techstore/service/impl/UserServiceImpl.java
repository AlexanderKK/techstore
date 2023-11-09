package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.dto.user.RegisterDTO;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final ModelMapper mapper;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(ModelMapper mapper,
                           UserRepository userRepository) {
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        User user = mapper.map(registerDTO, User.class);

        System.out.println();
//        userRepository.save(user);
    }

}
