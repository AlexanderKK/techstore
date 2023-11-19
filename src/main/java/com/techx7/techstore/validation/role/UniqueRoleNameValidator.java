package com.techx7.techstore.validation.role;

import com.techx7.techstore.repository.RoleRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueRoleNameValidator implements ConstraintValidator<UniqueRoleName, String> {

    private final RoleRepository roleRepository;

    @Autowired
    public UniqueRoleNameValidator(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return true;
        }

        return roleRepository.findByName(value).isEmpty();
    }

}
