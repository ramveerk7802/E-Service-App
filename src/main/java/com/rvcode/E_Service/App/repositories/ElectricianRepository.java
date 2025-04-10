package com.rvcode.E_Service.App.repositories;

import com.rvcode.E_Service.App.entities.Electrician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectricianRepository extends JpaRepository<Electrician,Long> {
}
