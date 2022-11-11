package com.polarbears.capstone.hmsnutritionist.service;

import com.polarbears.capstone.hmsnutritionist.domain.Epicrysis;
import com.polarbears.capstone.hmsnutritionist.repository.EpicrysisRepository;
import com.polarbears.capstone.hmsnutritionist.service.dto.EpicrysisDTO;
import com.polarbears.capstone.hmsnutritionist.service.mapper.EpicrysisMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Epicrysis}.
 */
@Service
@Transactional
public class EpicrysisService {

    private final Logger log = LoggerFactory.getLogger(EpicrysisService.class);

    private final EpicrysisRepository epicrysisRepository;

    private final EpicrysisMapper epicrysisMapper;

    public EpicrysisService(EpicrysisRepository epicrysisRepository, EpicrysisMapper epicrysisMapper) {
        this.epicrysisRepository = epicrysisRepository;
        this.epicrysisMapper = epicrysisMapper;
    }

    /**
     * Save a epicrysis.
     *
     * @param epicrysisDTO the entity to save.
     * @return the persisted entity.
     */
    public EpicrysisDTO save(EpicrysisDTO epicrysisDTO) {
        log.debug("Request to save Epicrysis : {}", epicrysisDTO);
        Epicrysis epicrysis = epicrysisMapper.toEntity(epicrysisDTO);
        epicrysis = epicrysisRepository.save(epicrysis);
        return epicrysisMapper.toDto(epicrysis);
    }

    /**
     * Update a epicrysis.
     *
     * @param epicrysisDTO the entity to save.
     * @return the persisted entity.
     */
    public EpicrysisDTO update(EpicrysisDTO epicrysisDTO) {
        log.debug("Request to update Epicrysis : {}", epicrysisDTO);
        Epicrysis epicrysis = epicrysisMapper.toEntity(epicrysisDTO);
        epicrysis = epicrysisRepository.save(epicrysis);
        return epicrysisMapper.toDto(epicrysis);
    }

    /**
     * Partially update a epicrysis.
     *
     * @param epicrysisDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EpicrysisDTO> partialUpdate(EpicrysisDTO epicrysisDTO) {
        log.debug("Request to partially update Epicrysis : {}", epicrysisDTO);

        return epicrysisRepository
            .findById(epicrysisDTO.getId())
            .map(existingEpicrysis -> {
                epicrysisMapper.partialUpdate(existingEpicrysis, epicrysisDTO);

                return existingEpicrysis;
            })
            .map(epicrysisRepository::save)
            .map(epicrysisMapper::toDto);
    }

    /**
     * Get all the epicryses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EpicrysisDTO> findAll() {
        log.debug("Request to get all Epicryses");
        return epicrysisRepository.findAll().stream().map(epicrysisMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one epicrysis by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EpicrysisDTO> findOne(Long id) {
        log.debug("Request to get Epicrysis : {}", id);
        return epicrysisRepository.findById(id).map(epicrysisMapper::toDto);
    }

    /**
     * Delete the epicrysis by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Epicrysis : {}", id);
        epicrysisRepository.deleteById(id);
    }
}
