package com.polarbears.capstone.hmsnutritionist.service.mapper;

import com.polarbears.capstone.hmsnutritionist.domain.Epicrysis;
import com.polarbears.capstone.hmsnutritionist.service.dto.EpicrysisDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Epicrysis} and its DTO {@link EpicrysisDTO}.
 */
@Mapper(componentModel = "spring")
public interface EpicrysisMapper extends EntityMapper<EpicrysisDTO, Epicrysis> {}
