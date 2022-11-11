package com.polarbears.capstone.hmsnutritionist.service.mapper;

import com.polarbears.capstone.hmsnutritionist.domain.ConsultingStatus;
import com.polarbears.capstone.hmsnutritionist.service.dto.ConsultingStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConsultingStatus} and its DTO {@link ConsultingStatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConsultingStatusMapper extends EntityMapper<ConsultingStatusDTO, ConsultingStatus> {}
