package com.dvt.version_tracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "versions")
public class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship to Document
    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    // Relationship to User who edited/created this version
    @ManyToOne
    @JoinColumn(name = "edited_by")
    private User editedBy;

    // Version number (as String for flexibility, e.g. “v1.0”)
    private String versionNumber;

    // The content of the document at this version
    @Column(columnDefinition = "TEXT")
    private String content;

    // Optional summary of what changed
    private String summary;

    // Timestamp of when this version was created
    private LocalDateTime createdAt;

    // ---------- Getters and Setters ----------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public User getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(User editedBy) {
        this.editedBy = editedBy;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
