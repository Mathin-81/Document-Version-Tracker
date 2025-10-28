package com.dvt.version_tracker.controller;

import com.dvt.version_tracker.model.Document;
import com.dvt.version_tracker.model.User;
import com.dvt.version_tracker.model.Version;
import com.dvt.version_tracker.repository.UserRepository;
import com.dvt.version_tracker.service.DocumentService;
import com.dvt.version_tracker.service.VersionService;
import com.dvt.version_tracker.web.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private VersionService versionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionUser sessionUser;

    // ========================
    // 1️⃣ List all documents
    // ========================
    @GetMapping
    public String listDocuments(Model model) {
        if (!sessionUser.hasUser()) return "redirect:/?error=no-user";
        model.addAttribute("documents", documentService.getAllDocuments());
        model.addAttribute("activeUser", sessionUser.getUser());
        return "documents";
    }

    // ========================
    // 2️⃣ Show "create document" form
    // ========================
    @GetMapping("/new")
    public String newDocumentForm(Model model) {
        if (!sessionUser.hasUser()) return "redirect:/?error=no-user";
        model.addAttribute("activeUser", sessionUser.getUser());
        return "create-document";
    }

    // ========================
    // 3️⃣ Create new document
    // ========================
    @PostMapping("/create")
    public String createDocument(@RequestParam String title,
                                 @RequestParam String content,
                                 RedirectAttributes redirectAttributes) {
        if (!sessionUser.hasUser()) return "redirect:/?error=no-user";
        User user = sessionUser.getUser();
        Document doc = documentService.createDocument(title, content, user);
        redirectAttributes.addFlashAttribute("success", "Document created successfully.");
        return "redirect:/documents/" + doc.getId();
    }

    // ========================
    // 4️⃣ View document details
    // ========================
    @GetMapping("/{id}")
    public String viewDocument(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Document> dOpt = documentService.getDocumentById(id);
        if (dOpt.isEmpty()) {
            ra.addFlashAttribute("error", "Document not found.");
            return "redirect:/documents";
        }

        Document doc = dOpt.get();
        List<Version> versions = versionService.getVersionsByDocumentId(id);
        model.addAttribute("document", doc);
        model.addAttribute("versions", versions);
        model.addAttribute("activeUser", sessionUser.getUser());
        return "document-details";
    }

    // ========================
    // 5️⃣ Edit document form
    // ========================
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        if (!sessionUser.hasUser()) return "redirect:/?error=no-user";
        Optional<Document> docOpt = documentService.getDocumentById(id);
        if (docOpt.isEmpty()) {
            ra.addFlashAttribute("error", "Document not found.");
            return "redirect:/documents";
        }

        model.addAttribute("document", docOpt.get());
        model.addAttribute("activeUser", sessionUser.getUser());
        return "edit-document";
    }

    // ========================
    // 6️⃣ Submit document edit
    // ========================
    @PostMapping("/{id}/edit")
    public String submitEdit(@PathVariable Long id,
                             @RequestParam String content,
                             @RequestParam(required = false) String summary,
                             RedirectAttributes ra) {
        if (!sessionUser.hasUser()) return "redirect:/?error=no-user";

        User user = sessionUser.getUser();
        Document updated = documentService.editDocument(
                id,
                content,
                user,
                (summary == null || summary.isBlank()) ? "Updated content" : summary
        );

        if (updated == null) {
            ra.addFlashAttribute("error", "Document not found.");
            return "redirect:/documents";
        }

        ra.addFlashAttribute("success", "Document updated successfully.");
        return "redirect:/documents/" + id;
    }

    // ========================
    // 7️⃣ Rollback to previous version
    // ========================
    @PostMapping("/{id}/rollback")
    public String rollback(@PathVariable Long id,
                           @RequestParam int versionNumber,
                           RedirectAttributes ra) {
        if (!sessionUser.hasUser()) return "redirect:/?error=no-user";

        boolean ok = documentService.rollbackToVersion(id, versionNumber);
        if (ok) {
            ra.addFlashAttribute("success", "Rolled back to version " + versionNumber + " successfully.");
        } else {
            ra.addFlashAttribute("error", "Rollback failed — version not found.");
        }
        return "redirect:/documents/" + id;
    }
}
