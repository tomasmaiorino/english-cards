package com.tsm.cards.resources;

import static com.tsm.cards.util.ErrorCodes.INVALID_CLIENT_EMAIL;
import static com.tsm.cards.util.ErrorCodes.INVALID_CLIENT_EMAIL_RECIPIENT;
import static com.tsm.cards.util.ErrorCodes.INVALID_CLIENT_NAME_SIZE;
import static com.tsm.cards.util.ErrorCodes.INVALID_CLIENT_STATUS;
import static com.tsm.cards.util.ErrorCodes.INVALID_CLIENT_TOKEN_SIZE;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CLIENT_EMAIL;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CLIENT_EMAIL_RECIPIENT;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CLIENT_HOSTS;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CLIENT_NAME;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CLIENT_TOKEN;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

public class ClientResource extends BaseResource {

    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    @NotNull(message = REQUIRED_CLIENT_NAME)
    @Size(min = 2, max = 30, message = INVALID_CLIENT_NAME_SIZE)
    private String name;

    @Getter
    @Setter
    @NotEmpty(message = REQUIRED_CLIENT_EMAIL)
    @Email(message = INVALID_CLIENT_EMAIL)
    private String email;

    @Getter
    @Setter
    @NotNull(message = REQUIRED_CLIENT_TOKEN)
    @Size(min = 2, max = 50, message = INVALID_CLIENT_TOKEN_SIZE)
    private String token;

    @Getter
    @Setter
    @Pattern(regexp = "\\b(ACTIVE|INACTIVE)\\b", message = INVALID_CLIENT_STATUS)
    private String status;

    @Getter
    @Setter
    @NotEmpty(message = REQUIRED_CLIENT_HOSTS)
    private Set<String> hosts;

    @Getter
    @Setter
    @NotEmpty(message = REQUIRED_CLIENT_EMAIL_RECIPIENT)
    @Email(message = INVALID_CLIENT_EMAIL_RECIPIENT)
    private String emailRecipient;

}
