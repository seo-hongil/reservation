package project.toy.reservation.store.service;

import project.toy.reservation.store.dto.StoreResponse;

public interface StoreProvider {
    StoreResponse getStores(String category, String keyword);
}
