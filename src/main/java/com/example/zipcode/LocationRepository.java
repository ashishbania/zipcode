package com.example.zipcode;

import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface LocationRepository extends JpaRepository<LocationDAO, Integer> {}
