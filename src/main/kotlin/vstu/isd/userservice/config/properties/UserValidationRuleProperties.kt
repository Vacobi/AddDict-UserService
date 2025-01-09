package vstu.isd.userservice.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.user.validation-rule")
data class UserValidationRuleProperties(
    val login: ValidationRule,
    val password: ValidationRule
) {
    data class ValidationRule(
        val minLength: Int,
        val maxLength: Int,
        val regex: String
    )
}
