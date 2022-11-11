package com.polarbears.capstone.hmsnutritionist.service;

import com.polarbears.capstone.hmsnutritionist.domain.ConsultingStatus;
import com.polarbears.capstone.hmsnutritionist.repository.ConsultingStatusRepository;
import com.polarbears.capstone.hmsnutritionist.service.dto.ConsultingStatusDTO;
import com.polarbears.capstone.hmsnutritionist.service.mapper.ConsultingStatusMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ConsultingStatus}.
 */
@Service
@Transactional
public class ConsultingStatusService {

    private final Logger log = LoggerFactory.getLogger(ConsultingStatusService.class);

    private final ConsultingStatusRepository consultingStatusRepository;

    private final ConsultingStatusMapper consultingStatusMapper;

    public ConsultingStatusService(ConsultingStatusRepository consultingStatusRepository, ConsultingStatusMapper consultingStatusMapper) {
        this.consultingStatusRepository = consultingStatusRepository;
        this.consultingStatusMapper = consultingStatusMapper;
    }

    /**
     * Save a consultingStatus.
     *
     * @param consultingStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public ConsultingStatusDTO save(ConsultingStatusDTO consultingStatusDTO) {
        log.debug("Request to save ConsultingStatus : {}", consultingStatusDTO);
        ConsultingStatus consultingStatus = consultingStatusMapper.toEntity(consultingStatusDTO);
        consultingStatus = consultingStatusRepository.save(consultingStatus);
        return consultingStatusMapper.toDto(consultingStatus);
    }

    /**
     * Update a consultingStatus.
     *
     * @param consultingStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public ConsultingStatusDTO update(ConsultingStatusDTO consultingStatusDTO) {
        log.debug("Request to update ConsultingStatus : {}", consultingStatusDTO);
        ConsultingStatus consultingStatus = consultingStatusMapper.toEntity(consultingStatusDTO);
        consultingStatus = consultingStatusRepository.save(consultingStatus);
        return consultingStatusMapper.toDto(consultingStatus);
    }

    /**
     * Partially update a consultingStatus.
     *
     * @param consultingStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConsultingStatusDTO> partialUpdate(ConsultingStatusDTO consultingStatusDTO) {
        log.debug("Request to partially update ConsultingStatus : {}", consultingStatusDTO);

        return consultingStatusRepository
            .findById(consultingStatusDTO.getId())
            .map(existingConsultingStatus -> {
                consultingStatusMapper.partialUpdate(existingConsultingStatus, consultingStatusDTO);

                return existingConsultingStatus;
            })
            .map(consultingStatusRepository::save)
            .map(consultingStatusMapper::toDto);
    }

    /**
     * Get all the consultingStatuses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ConsultingStatusDTO> findAll() {
        log.debug("Request to get all ConsultingStatuses");
        return consultingStatusRepository
            .findAll()
            .stream()
            .map(consultingStatusMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one consultingStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConsultingStatusDTO> findOne(Long id) {
        log.debug("Request to get ConsultingStatus : {}", id);
        return consultingStatusRepository.findById(id).map(consultingStatusMapper::toDto);
    }

    /**
     * Delete the consultingStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ConsultingStatus : {}", id);
        consultingStatusRepository.deleteById(id);
    }
}
