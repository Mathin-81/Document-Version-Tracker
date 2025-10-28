package com.dvt.version_tracker.repository;

import com.dvt.version_tracker.model.Document;
import com.dvt.version_tracker.model.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {
    List<Version> findByDocument(Document document);
    List<Version> findByDocumentId(Long documentId);
}
