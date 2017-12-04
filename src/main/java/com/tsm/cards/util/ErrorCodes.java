package com.tsm.cards.util;

public interface ErrorCodes {

    // GENERAL
    public static final String FIELD_REQUIRED = "field.required";

    public static final String INVALID_STATUS = "invalidStatus";

    // CLIENT
    public static final String REQUIRED_CLIENT_NAME = "requiredClientName";

    public static final String INVALID_CLIENT_NAME_SIZE = "invalidClientNameSize";

    public static final String REQUIRED_CLIENT_EMAIL = "requiredClientEmail";

    public static final String INVALID_CLIENT_EMAIL = "invalidClientEmail";

    public static final String REQUIRED_CLIENT_TOKEN = "requiredClientToken";

    public static final String INVALID_CLIENT_TOKEN_SIZE = "invalidClientTokenSize";

    public static final String INVALID_CLIENT_STATUS = "invalidClientStatus";

    public static final String REQUIRED_CLIENT_HOSTS = "requiredClientHosts";

    public static final String REQUIRED_CLIENT_EMAIL_RECIPIENT = "requiredClientEmailRecipient";

    public static final String INVALID_CLIENT_EMAIL_RECIPIENT = "invalidClientEmailRecipient";

    //

    public static final String MISSING_HEADER = "missingHeader";

    public static final String ACCESS_NOT_ALLOWED = "accessNotAllowed";

    public static final String INVALID_NAME_SIZE = "invalidNameSize";

    public static final String REQUIRED_TOKEN = "requiredToken";

    public static final String INVALID_TOKEN_SIZE = "invalidTokenSize";

    public static final String DUPLICATED_TOKEN = "duplicatedToken";

    public static final String CLIENT_NOT_FOUND = "clientNotFound";

    // CARD

    public static final String REQUIRED_CARD_NAME = "requiredCardName";

    public static final String INVALID_CARD_NAME_SIZE = "invalidCardNameSize";

    public static final String REQUIRED_CARD_IMG_URL = "requiredImgUrl";

    public static final String INVALID_CARD_IMG = "invalidImgUrl";

    public static final String INVALID_CARD_IMG_URL_SIZE = "invalidImgUrlSize";

    public static final String INVALID_CARD_IMG_URL = "invalidCardImgUrl";

    public static final String REQUIRED_CARD_STATUS = "requiredCardStatus";

    public static final String INVALID_CARD_STATUS = "invalidCardStatus";

    public static final String REQUIRED_CARD_TYPE = "requiredCardType";

    public static final String INVALID_CARD_TYPE = "invalidCardType";

    public static final String DUPLICATED_CARD = "duplicatedCard";

    // CARD TYPE
    public static final String CARD_TYPE_NOT_FOUND = "cardTypeNotFound";

    public static final String CARD_NOT_FOUND = "cardNotFound";

    public static final String REQUIRED_CARD_TYPE_NAME = "requiredCardTypeName";

    public static final String INVALID_CARD_TYPE_NAME_SIZE = "invalidCardTypeNameSize";

    public static final String DUPLICATED_CARD_TYPE = "duplicatedCardType";


    // DEFINITIONS
    public static final String UNKNOWN_WORD_GIVEN = "unknownWordGiven";

    public static final String SYNONYMS_NOT_FOUND = "synonymsNotFound";


}
