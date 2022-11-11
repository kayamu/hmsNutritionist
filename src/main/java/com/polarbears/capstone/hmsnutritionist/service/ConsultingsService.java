package com.polarbears.capstone.hmsnutritionist.service;

import com.polarbears.capstone.hmsnutritionist.domain.Consultings;
import com.polarbears.capstone.hmsnutritionist.repository.ConsultingsRepository;
import com.polarbears.capstone.hmsnutritionist.service.dto.ConsultingsDTO;
import com.polarbears.capstone.hmsnutritionist.service.mapper.ConsultingsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Consultings}.
 */
@Service
@Transactional
public class ConsultingsService {

    private final Logger log = LoggerFactory.getLogger(ConsultingsService.class);

    private final ConsultingsRepository consultingsRepository;

    private final ConsultingsMapper consultingsMapper;

    public ConsultingsService(ConsultingsRepository consultingsRepository, ConsultingsMapper consultingsMapper) {
        this.consultingsRepository = consultingsRepository;
        this.consultingsMapper = consultingsMapper;
    }

    /**
     * Save a consultings.
     *
     * @param consultingsDTO the entity to save.
     * @return the persisted entity.
     */
    public ConsultingsDTO save(ConsultingsDTO consultingsDTO) {
        log.debug("Request to save Consultings : {}", consultingsDTO);
        Consultings consultings = consultingsMapper.toEntity(consultingsDTO);
        consultings = consultingsRepository.save(consultings);
        return consultingsMapper.toDto(consultings);
    }

    /**
     * Update a consultings.
     *
     * @param consultingsDTO the entity to save.
     * @return the persisted entity.
     */
    public ConsultingsDTO update(ConsultingsDTO consultingsDTO) {
        log.debug("Request to update Consultings : {}", consultingsDTO);
        Consultings consultings = consultingsMapper.toEntity(consultingsDTO);
        consultings = consultingsRepository.save(consultings);
        return consultingsMapper.toDto(consultings);
    }

    /**
     * Partially update a consultings.
     *
     * @param consultingsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConsultingsDTO> partialUpdate(ConsultingsDTO consultingsDTO) {
        log.debug("Request to partially update Consultings : {}", consultingsDTO);

        return consultingsRepository
            .findById(consultingsDTO.getId())
            .map(existingConsultings -> {
                consultingsMapper.partialUpdate(existingConsultings, consultingsDTO);

                return existingConsultings;
            })
            .map(consultingsRepository::save)
            .map(consultingsMapper::toDto);
    }

    /**
     * Get all the consultings.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ConsultingsDTO> findAll() {
        log.debug("Request to get all Consultings");
        return consultingsRepository.findAll().stream().map(consultingsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the consultings with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ConsultingsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return consultingsRepository.findAllWithEagerRelationships(pageable).map(consultingsMapper::toDto);
    }

    /**
     * Get one consultings by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConsultingsDTO> findOne(Long id) {
        log.debug("Request to get Consultings : {}", id);
        return consultingsRepository.findOneWithEagerRelationships(id).map(consultingsMapper::toDto);
    }

    /**
     * Delete the consultings by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Consultings : {}", id);
        consultingsRepository.deleteById(id);
    }
}
