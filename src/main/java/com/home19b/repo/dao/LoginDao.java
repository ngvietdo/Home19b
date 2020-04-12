package com.home19b.repo.dao;

import com.home19b.common.CollectionMongoUtils;
import com.home19b.domain.dto.User;
import com.home19b.domain.request.LoginRequest;
import com.mongodb.client.model.Filters;
import org.springframework.stereotype.Repository;

@Repository
public class LoginDao extends BaseDao {

    public User findUserLogin(LoginRequest request) {
        User user = getCollection(CollectionMongoUtils.CLT_USER, User.class)
                .find(Filters.and(
                        Filters.eq("sdt", request.getSdt()),
                        Filters.eq("password", request.getPassword())
                )).first();
        return user;
    }

    public User findUserViaSdt(String sdt) {
        User user = getCollection(CollectionMongoUtils.CLT_USER, User.class)
                .find(
                        Filters.eq("sdt", sdt)
                ).first();
        return user;
    }
}
