package com.moneyminder.moneyminderusers.dto;

import com.moneyminder.moneyminderusers.models.GroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GroupRequestWithBudgetNameDtoTest {

    @Test
    @DisplayName("Getters test")
    void testGettersWithBuilder() {
        GroupRequestWithBudgetNameDto dto = GroupRequestWithBudgetNameDto.builder()
                .id("1")
                .budgetName("Project Alpha")
                .requestingUser("userA")
                .requestedUser("userB")
                .date(LocalDate.now())
                .accepted(true)
                .build();

        assertEquals("1", dto.getId());
        assertEquals("Project Alpha", dto.getBudgetName());
        assertEquals("userA", dto.getRequestingUser());
        assertEquals("userB", dto.getRequestedUser());
        assertNotNull(dto.getDate());
        assertTrue(dto.getAccepted());
    }

    @Test
    @DisplayName("Setters test")
    void testSettersWithNoArgsConstructor() {
        GroupRequestWithBudgetNameDto dto = new GroupRequestWithBudgetNameDto();
        dto.setId("2");
        dto.setBudgetName("Project Beta");
        dto.setRequestingUser("userC");
        dto.setRequestedUser("userD");
        dto.setDate(LocalDate.of(2025, 4, 26));
        dto.setAccepted(false);

        assertEquals("2", dto.getId());
        assertEquals("Project Beta", dto.getBudgetName());
        assertEquals("userC", dto.getRequestingUser());
        assertEquals("userD", dto.getRequestedUser());
        assertEquals(LocalDate.of(2025, 4, 26), dto.getDate());
        assertFalse(dto.getAccepted());
    }

    @Test
    @DisplayName("GroupToWithBudgetName conversion from group test")
    void testGroupToWithBudgetName() {
        GroupRequest groupRequest = GroupRequest.builder()
                .id("3")
                .requestingUser("userIng")
                .requestedUser("userEd")
                .date(LocalDate.now())
                .accepted(true)
                .build();

        GroupRequestWithBudgetNameDto dto = new GroupRequestWithBudgetNameDto();
        dto.groupToWithBudgetName(groupRequest);

        assertEquals("3", dto.getId());
        assertEquals("userIng", dto.getRequestingUser());
        assertEquals("userEd", dto.getRequestedUser());
        assertNotNull(dto.getDate());
        assertTrue(dto.getAccepted());
        assertNull(dto.getBudgetName());
    }

    @Test
    @DisplayName("WithBudgetNameToGroupRequest conversion to group test")
    void testWithBudgetNameToGroupRequest() {
        GroupRequestWithBudgetNameDto dto = GroupRequestWithBudgetNameDto.builder()
                .id("4")
                .budgetName("Project Gamma")
                .requestingUser("userIng")
                .requestedUser("userEd")
                .date(LocalDate.now())
                .accepted(false)
                .build();

        GroupRequest groupRequest = dto.withBudgetNameToGroupRequest();

        assertEquals("4", groupRequest.getId());
        assertEquals("userIng", groupRequest.getRequestingUser());
        assertEquals("userEd", groupRequest.getRequestedUser());
        assertNotNull(groupRequest.getDate());
        assertFalse(groupRequest.getAccepted());
        assertNull(groupRequest.getGroup());
    }

}