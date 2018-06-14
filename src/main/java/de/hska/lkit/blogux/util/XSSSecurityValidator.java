package de.hska.lkit.blogux.util;

import org.springframework.web.util.HtmlUtils;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidator;

public class XSSSecurityValidator implements
  ConstraintValidator<XSSSecurityConstraint, String> {

    @Override
    public void initialize(XSSSecurityConstraint input) {
    }

    @Override
    public boolean isValid(String input,
      ConstraintValidatorContext cxt) {
        if (input.equals(HtmlUtils.htmlEscape(input)) || input == null)
          return true;
        else
          return false;
    }

}
