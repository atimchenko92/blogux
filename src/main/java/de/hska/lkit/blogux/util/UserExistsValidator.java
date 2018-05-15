package de.hska.lkit.blogux.util;

import de.hska.lkit.blogux.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidator;

public class UserExistsValidator implements
  ConstraintValidator<UserExistsConstraint, String> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UserExistsConstraint username) {
    }

    @Override
    public boolean isValid(String usernameField,
      ConstraintValidatorContext cxt) {
        if(usernameField == null){
          return false;
        }

        return !(userRepository.userAlreadyExists(usernameField));
    }

}
