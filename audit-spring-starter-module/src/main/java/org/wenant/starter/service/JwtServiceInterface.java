package org.wenant.starter.service;

public interface JwtServiceInterface {
    String getUsernameFromAuthorizationHeader(String authorizationHeader);
}
