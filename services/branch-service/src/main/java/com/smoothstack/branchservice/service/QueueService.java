package com.smoothstack.branchservice.service;

import com.smoothstack.branchservice.dto.QueueDTO;
import com.smoothstack.branchservice.mapper.QueueMapper;
import com.smoothstack.branchservice.model.Branch;
import com.smoothstack.branchservice.model.Queue;
import com.smoothstack.branchservice.dao.BranchRepository;
import com.smoothstack.branchservice.dao.QueueRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class QueueService {
    private final QueueRepository queueRepository;
    private final QueueMapper queueMapper;
    private final BranchRepository branchRepository;

    /**
     * Retrieves a list of QueueDTO objects based on the specified parameters.
     *
     * <p>This method allows fetching queues based on various criteria such as queueId, branchId, or userId.
     * If all parameters are null, it returns a list of all queues.
     * If queueId is provided, it retrieves the specific queue with that ID.
     * If branchId is provided, it retrieves all queues associated with the specified branch.
     * If userId is provided, it retrieves all queues associated with the specified user.
     *
     * @param queueId  The ID of the specific queue to retrieve. If provided, branchId and userId are ignored.
     * @param branchId The ID of the branch for which to retrieve queues. Ignored if queueId or userId is specified.
     * @param userId   The ID of the user for which to retrieve queues. Ignored if queueId or branchId is specified.
     * @return A list of QueueDTO objects representing queues based on the provided parameters.
     */
    public List<QueueDTO> getQueuesByParams(Integer queueId, Integer branchId, Integer userId) {
        if (queueId != null) {
            return getQueueById(queueId);
        }
        else if (branchId != null) {
            return getQueuesByBranchId(branchId);
        }
        else if (userId != null) {
            return getQueuesByUserId(userId);
        }
        else {
            return getAllQueues();
        }
    }

    public List<QueueDTO> getAllQueues() {
        return queueRepository.findAll()
                .stream()
                .map(queueMapper::queueToQueueDTO)
                .collect(Collectors.toList());
    }

    public List<QueueDTO> getQueueById(Integer queueId) {
        return queueRepository.findByQueueId(queueId)
                .stream()
                .map(queueMapper::queueToQueueDTO)
                .collect(Collectors.toList());
    }

    public List<QueueDTO> getQueuesByBranchId(Integer branchId) {
        List<Queue> queues = queueRepository.findQueuesByBranch_BranchId(branchId);
        return queueMapper.queueListToQueueDTOList(queues);
    }

    public List<QueueDTO> getQueuesByUserId(Integer userId) {
        List<Queue> queues = queueRepository.findQueuesByUserId(userId);
        return queueMapper.queueListToQueueDTOList(queues);
    }

    @Transactional
    public QueueDTO createQueue(QueueDTO queueDTO) {
        Queue queue = queueMapper.queueDTOToQueue(queueDTO);

        if (queueDTO.getBranchId() != null) {
            Branch existingBranch = branchRepository.findById(queueDTO.getBranchId())
                    .orElseThrow(() -> new EntityNotFoundException("Queue not found with id: " + queueDTO.getBranchId()));
            queue.setBranch(existingBranch);
        }

            Queue savedQueue = queueRepository.save(queue);
            return queueMapper.queueToQueueDTO(savedQueue);
    }

    @Transactional
    public QueueDTO updateQueue(Integer queueId, QueueDTO updatedQueueDTO) {
        Queue existingQueue = queueRepository.findByQueueId(queueId)
                .orElseThrow(() -> new EntityNotFoundException("Queue not found with ID: " + queueId));

        Queue updatedQueue = queueMapper.queueDTOToQueue(updatedQueueDTO);
        updateExistingQueue(existingQueue, updatedQueue);

        Queue savedQueue = queueRepository.save(existingQueue);
        return queueMapper.queueToQueueDTO(savedQueue);
    }

    private void updateExistingQueue(Queue existingQueue, Queue updatedQueue) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        modelMapper.map(updatedQueue, existingQueue);
    }

    @Transactional
    public void deleteQueueById(Integer queueId) {
        queueRepository.deleteById(queueId);
    }
}
