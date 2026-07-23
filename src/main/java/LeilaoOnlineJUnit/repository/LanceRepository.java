package LeilaoOnlineJUnit.repository;

import LeilaoOnlineJUnit.entity.Lance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanceRepository extends JpaRepository<Lance,Long> {
}
