package com.truelayer.java.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class RequestScopes {

    private final List<String> scopes;

}
