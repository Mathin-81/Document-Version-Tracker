package com.dvt.version_tracker.repository;

import com.dvt.version_tracker.model.Document;
import com.dvt.version_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByCreatedBy(User user);
}
