package project.toy.reservation.store.repository;

import project.toy.reservation.store.entity.Store;

import java.util.List;

public interface StoreRepositoryCustom {
    List<Store> searchStores(Long categoryId, String keyword);
}
