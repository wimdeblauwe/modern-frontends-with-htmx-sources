package com.modernfrontendshtmx.contactsapp.infrastructure.repository;

import java.util.List;

public record Page<T>(List<T> values,
                      int number,
                      int size,
                      int totalElements) {
}
