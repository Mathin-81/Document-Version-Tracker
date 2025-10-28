package com.dvt.version_tracker.service;

import com.dvt.version_tracker.model.Document;
import com.dvt.version_tracker.model.User;
import com.dvt.version_tracker.model.Version;
import com.dvt.version_tracker.repository.DocumentRepository;
import com.dvt.version_tracker.repository.VersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private VersionRepository versionRepository;

    // ✅ Fetch all documents
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    // ✅ Fetch document by ID
    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    // ✅ Create a new document (and its first version)
    public Document createDocument(String title, String content, User createdBy) {
        Document document = new Document();
        document.setTitle(title);
        document.setCreatedBy(createdBy);
        document.setCreatedAt(LocalDateTime.now());
        document = documentRepository.save(document);

        Version version = new Version();
        version.setDocument(document);
        version.setVersionNumber("1");
        version.setContent(content);
        version.setEditedBy(createdBy);
        version.setSummary("Initial version");
        version.setCreatedAt(LocalDateTime.now());
        versionRepository.save(version);

        return document;
    }

    // ✅ Edit a document (creates new version)
    public Document editDocument(Long documentId, String newContent, User editedBy, String summary) {
        Optional<Document> docOpt = documentRepository.findById(documentId);
        if (docOpt.isEmpty()) return null;

        Document doc = docOpt.get();

        List<Version> existingVersions = versionRepository.findByDocument(doc);
        int newVersionNumber = existingVersions.size() + 1;

        Version version = new Version();
        version.setDocument(doc);
        version.setVersionNumber(String.valueOf(newVersionNumber));
        version.setContent(newContent);
        version.setEditedBy(editedBy);
        version.setSummary(summary);
        version.setCreatedAt(LocalDateTime.now());
        versionRepository.save(version);

        return doc;
    }

    // ✅ Rollback to previous version
    public boolean rollbackToVersion(Long documentId, int versionNumber) {
        Optional<Document> docOpt = documentRepository.findById(documentId);
        if (docOpt.isEmpty()) return false;

        Document document = docOpt.get();

        List<Version> versions = versionRepository.findByDocument(document);
        Optional<Version> versionOpt = versions.stream()
                .filter(v -> Integer.parseInt(v.getVersionNumber()) == versionNumber)
                .findFirst();

        if (versionOpt.isEmpty()) return false;

        Version targetVersion = versionOpt.get();

        Version rollbackVersion = new Version();
        rollbackVersion.setDocument(document);
        rollbackVersion.setVersionNumber(String.valueOf(versions.size() + 1));
        rollbackVersion.setContent(targetVersion.getContent());
        rollbackVersion.setEditedBy(targetVersion.getEditedBy());
        rollbackVersion.setSummary("Rolled back to version " + versionNumber);
        rollbackVersion.setCreatedAt(LocalDateTime.now());

        versionRepository.save(rollbackVersion);
        return true;
    }
}
