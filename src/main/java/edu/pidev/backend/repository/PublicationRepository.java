package edu.pidev.backend.repository;

import edu.pidev.backend.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PublicationRepository extends JpaRepository<Publication,Integer> {

}
