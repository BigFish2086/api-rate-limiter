package com.api_rate_limiter.api_rate_limiter.repository;

import com.api_rate_limiter.api_rate_limiter.entity.MembershipLookup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface MembershipLookupRepository extends JpaRepository<MembershipLookup, Integer> {
    @Query("SELECT m FROM MembershipLookup m WHERE m.membershipName = :name")
    MembershipLookup findByMembershipName(String name);

    @Query("SELECT m FROM MembershipLookup m")
    Iterable<MembershipLookup> findAllMemberships();
}
