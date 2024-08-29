package com.example.ecommerce.shopbase.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1001, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1002, "You do not have permission", HttpStatus.FORBIDDEN),
    BAD_CREDENTIAL(1003, "Bad credential", HttpStatus.UNAUTHORIZED),
    URL_NOT_FOUND(1004, "No handler found for {httpMethod} {url}", HttpStatus.NOT_FOUND),
    METHOD_NOT_SUPPORTED(1005, "Method {httpMethod} is not support for this request", HttpStatus.METHOD_NOT_ALLOWED),
    PARAMETER_MISSING(1006, "Parameter {parameter} is missing", HttpStatus.BAD_REQUEST),
    PARAMETER_TYPE_MISMATCH(1007, "{parameter} should be of type {type}", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1008, "Uncategorized error", HttpStatus.BAD_REQUEST),
    NOT_FOUND(1009, "Resource not found", HttpStatus.NOT_FOUND),
    EMAIL_EXITED(1010, "Email existed!", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1011, "User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1012, "User not existed", HttpStatus.NOT_FOUND),
    USER_BLOCKED(1013, "User is not active", HttpStatus.FORBIDDEN),
    USER_ACTIVATED(1014, "User is already active", HttpStatus.BAD_REQUEST),
    OTP_NOT_EXISTED(1015, "OTP not existed", HttpStatus.BAD_REQUEST),
    OTP_EXPIRED(1016, "OTP Expired", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_EXISTED(1017, "Token not existed", HttpStatus.BAD_REQUEST),
    LOCATION_NOT_EXISTED(1018, "Location not existed", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_EXISTED(1019, "Address not exist!", HttpStatus.BAD_REQUEST),
    OUT_OF_STOCK(1020,"No remain in inventory",HttpStatus.NOT_FOUND),
    ADDRESS_DEFAULT(1021, "You must have 1 address default", HttpStatus.BAD_REQUEST),
    METHOD_ARGUMENT_INVALID(1022, "Validation: Parameter is error!", HttpStatus.BAD_REQUEST),
    SELLER_EXISTED(1023, "Seller existed!", HttpStatus.BAD_REQUEST),
    SELLER_NOT_EXISTED(1024, "User not existed", HttpStatus.NOT_FOUND),
    INVALID_OTP(1025, "Invalid OTP", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1026, "Invalid Token", HttpStatus.BAD_REQUEST),
    INVALID_OLD_PASSWORD(1027, "Wrong old password", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTED(1028, "Product not existed", HttpStatus.NOT_FOUND),
    API_RESPONSE_NULL(1029,"API response null",HttpStatus.NOT_FOUND),
    VALUE_NOT_EXISTED(1030, "Value not existed", HttpStatus.NOT_FOUND),
    ORDER_NOT_EXISTED(1031, "Order not existed", HttpStatus.NOT_FOUND),
    API_CALL_FAILED(1032,"Can not call API", HttpStatus.BAD_REQUEST),
    SHIPPING_FEE_CALCULATION_FAILED(1033,"Shipping fee calculation failed: Response body is null",HttpStatus.NOT_FOUND),
    PRODUCT_NOT_ENOUGH(1034,"Product on inventory not enough",HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(1029, "Category not existed", HttpStatus.NOT_FOUND),
    ASSORTMENT_NOT_EXISTED(1031, "Assortment not existed", HttpStatus.NOT_FOUND),
    REDIS_CONNECTION_FAILURE(1028, "Connect to Redis Failure", HttpStatus.INTERNAL_SERVER_ERROR),
    QUANTITY_NOT_NEGATIVE(1035,"Quantity of Product must be positive",HttpStatus.BAD_REQUEST),
    VALUECOMBINATION_NOT_EXISTED(1032,"VALUECOMBINATION_NOT_EXISTED",HttpStatus.NOT_FOUND),
    CARTITEM_NOT_EXISTED(1033,"CARTITEM_NOT_EXISTED",HttpStatus.NOT_FOUND),
    ORDERITEM_NOT_EXISTED(1034,"ORDERITEM_NOT_EXISTED",HttpStatus.NOT_FOUND),
    LOILO(1032,"sas",HttpStatus.NOT_FOUND);
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
