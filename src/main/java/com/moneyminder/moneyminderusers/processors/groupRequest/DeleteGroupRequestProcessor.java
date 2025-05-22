package com.moneyminder.moneyminderusers.processors.groupRequest;

import com.moneyminder.moneyminderusers.mappers.GroupRequestMapper;
import com.moneyminder.moneyminderusers.persistence.repositories.GroupRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteGroupRequestProcessor {
    private final GroupRequestRepository groupRequestRepository;
    private final GroupRequestMapper groupRequestMapper;

    public void deleteGroupRequest(String id) {
        Assert.isTrue(this.groupRequestRepository.existsById(id), "GroupRequest not found");
        this.groupRequestRepository.deleteById(id);
    }

    public void deleteGroupRequestList(List<String> ids) {
        ids.forEach(this::deleteGroupRequest);
    }
}
