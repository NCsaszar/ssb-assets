package com.smoothstack.branchservice.mapper;

import com.smoothstack.branchservice.dto.QueueDTO;
import com.smoothstack.branchservice.model.Queue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QueueMapper {

    @Mapping(target = "queueId", ignore = true)
    Queue queueDTOToQueue(QueueDTO queueDTO);

    @Mapping(source = "queueId", target = "queueId")
    QueueDTO queueToQueueDTO(Queue queue);

    List<QueueDTO> queueListToQueueDTOList(List<Queue> queues);

}
