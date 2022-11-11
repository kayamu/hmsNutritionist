package com.polarbears.capstone.hmsnutritionist.service;

import com.polarbears.capstone.hmsnutritionist.domain.MenuSuggestions;
import com.polarbears.capstone.hmsnutritionist.repository.MenuSuggestionsRepository;
import com.polarbears.capstone.hmsnutritionist.service.dto.MenuSuggestionsDTO;
import com.polarbears.capstone.hmsnutritionist.service.mapper.MenuSuggestionsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MenuSuggestions}.
 */
@Service
@Transactional
public class MenuSuggestionsService {

    private final Logger log = LoggerFactory.getLogger(MenuSuggestionsService.class);

    private final MenuSuggestionsRepository menuSuggestionsRepository;

    private final MenuSuggestionsMapper menuSuggestionsMapper;

    public MenuSuggestionsService(MenuSuggestionsRepository menuSuggestionsRepository, MenuSuggestionsMapper menuSuggestionsMapper) {
        this.menuSuggestionsRepository = menuSuggestionsRepository;
        this.menuSuggestionsMapper = menuSuggestionsMapper;
    }

    /**
     * Save a menuSuggestions.
     *
     * @param menuSuggestionsDTO the entity to save.
     * @return the persisted entity.
     */
    public MenuSuggestionsDTO save(MenuSuggestionsDTO menuSuggestionsDTO) {
        log.debug("Request to save MenuSuggestions : {}", menuSuggestionsDTO);
        MenuSuggestions menuSuggestions = menuSuggestionsMapper.toEntity(menuSuggestionsDTO);
        menuSuggestions = menuSuggestionsRepository.save(menuSuggestions);
        return menuSuggestionsMapper.toDto(menuSuggestions);
    }

    /**
     * Update a menuSuggestions.
     *
     * @param menuSuggestionsDTO the entity to save.
     * @return the persisted entity.
     */
    public MenuSuggestionsDTO update(MenuSuggestionsDTO menuSuggestionsDTO) {
        log.debug("Request to update MenuSuggestions : {}", menuSuggestionsDTO);
        MenuSuggestions menuSuggestions = menuSuggestionsMapper.toEntity(menuSuggestionsDTO);
        menuSuggestions = menuSuggestionsRepository.save(menuSuggestions);
        return menuSuggestionsMapper.toDto(menuSuggestions);
    }

    /**
     * Partially update a menuSuggestions.
     *
     * @param menuSuggestionsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MenuSuggestionsDTO> partialUpdate(MenuSuggestionsDTO menuSuggestionsDTO) {
        log.debug("Request to partially update MenuSuggestions : {}", menuSuggestionsDTO);

        return menuSuggestionsRepository
            .findById(menuSuggestionsDTO.getId())
            .map(existingMenuSuggestions -> {
                menuSuggestionsMapper.partialUpdate(existingMenuSuggestions, menuSuggestionsDTO);

                return existingMenuSuggestions;
            })
            .map(menuSuggestionsRepository::save)
            .map(menuSuggestionsMapper::toDto);
    }

    /**
     * Get all the menuSuggestions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MenuSuggestionsDTO> findAll() {
        log.debug("Request to get all MenuSuggestions");
        return menuSuggestionsRepository
            .findAll()
            .stream()
            .map(menuSuggestionsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one menuSuggestions by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MenuSuggestionsDTO> findOne(Long id) {
        log.debug("Request to get MenuSuggestions : {}", id);
        return menuSuggestionsRepository.findById(id).map(menuSuggestionsMapper::toDto);
    }

    /**
     * Delete the menuSuggestions by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MenuSuggestions : {}", id);
        menuSuggestionsRepository.deleteById(id);
    }
}
