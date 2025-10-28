package com.dvt.version_tracker.service;

import com.dvt.version_tracker.model.Document;
import com.dvt.version_tracker.model.User;
import com.dvt.version_tracker.model.Version;
import com.dvt.version_tracker.repository.VersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VersionService {

    @Autowired
    private VersionRepository versionRepository;

    // Get all versions for a document
    public List<Version> getVersionsByDocumentId(Long documentId) {
        return versionRepository.findByDocumentId(documentId);
    }

    // Create and save a new version
    public Version createVersion(Document document, String content, User editedBy, int versionNumber, String summary) {
        Version version = new Version();
        version.setDocument(document);

        // Convert int -> String safely
        version.setVersionNumber(String.valueOf(versionNumber));

        version.setContent(content);
        version.setSummary(summary);
        version.setEditedBy(editedBy);
        version.setCreatedAt(LocalDateTime.now());
        return versionRepository.save(version);
    }
}
