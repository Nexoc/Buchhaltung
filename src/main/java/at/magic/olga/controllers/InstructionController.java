package at.magic.olga.controllers;

import at.magic.olga.dto.InstructionDto;
import at.magic.olga.mappers.InstructionMapper;
import at.magic.olga.models.Instruction;
import at.magic.olga.service.FileService;
import at.magic.olga.service.InstructionService;

import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * REST controller for managing product instructions.
 */
@Validated
@RestController
@RequestMapping("/api/instructions")
public class InstructionController {

    private final InstructionService instructionService;
    private final InstructionMapper instructionMapper;
    private final FileService fileService;

    public InstructionController(InstructionService instructionService,
                                 InstructionMapper instructionMapper,
                                 FileService fileService) {
        this.instructionService = instructionService;
        this.instructionMapper = instructionMapper;
        this.fileService = fileService;
    }

    @GetMapping
    public List<InstructionDto> listAll() {
        List<Instruction> list = instructionService.findAll();
        return instructionMapper.toDtoList(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstructionDto> getOne(@PathVariable Integer id) {
        Instruction entity = instructionService.findById(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(instructionMapper.toDto(entity));
    }

    @PostMapping
    public ResponseEntity<InstructionDto> create(@RequestBody @Valid InstructionDto dto) {
        Instruction saved = instructionService.create(instructionMapper.toEntity(dto));
        InstructionDto out = instructionMapper.toDto(saved);
        URI location = URI.create("/api/instructions/" + out.getId());
        return ResponseEntity.created(location).body(out);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstructionDto> update(@PathVariable Integer id,
                                                 @RequestBody @Valid InstructionDto dto) {
        Instruction updated = instructionService.update(id, instructionMapper.toEntity(dto));
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(instructionMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        instructionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/file")
    public ResponseEntity<String> uploadInstruction(
            @PathVariable Integer id,
            @RequestParam("file") MultipartFile file) throws IOException {

        Instruction ins = instructionService.findById(id);
        if (ins == null) {
            return ResponseEntity.notFound().build();
        }

        String relativePath = fileService.store(file, "instructions");
        ins.setFilename(relativePath);
        instructionService.update(id, ins);
        return ResponseEntity.ok(relativePath);
    }

    @GetMapping("/file/{filename:.+}")
    public ResponseEntity<Resource> serveInstruction(@PathVariable String filename) throws IOException {
        Resource resource = fileService.load("instructions/" + filename);

        if (resource == null || !resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        // Try to detect content type (e.g. image/jpeg, application/pdf, etc.)
        String contentType = Files.probeContentType(Path.of(resource.getFile().getAbsolutePath()));
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // fallback
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
