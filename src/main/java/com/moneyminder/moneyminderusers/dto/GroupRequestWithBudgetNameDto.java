package com.moneyminder.moneyminderusers.dto;

import com.moneyminder.moneyminderusers.models.GroupRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupRequestWithBudgetNameDto {

    @Schema(description = "ID de la solicitud", example = "db5d0fdd-142e-48c4-8684-0b0ee7e31eb8")
    private String id;

    @Schema(description = "Nombre del presupuesto asociado", example = "Presupuesto Anual")
    private String budgetName;

    @Schema(description = "Nombre de usuario del solicitante", example = "user1")
    private String requestingUser;

    @Schema(description = "Nombre de usuario del destinatario", example = "user2")
    private String requestedUser;

    @Schema(description = "Fecha de la solicitud", type = "string", format = "date", example = "2025-04-20")
    private LocalDate date;

    @Schema(description = "Indica si la solicitud fue aceptada", example = "false")
    private Boolean accepted;

    public GroupRequestWithBudgetNameDto groupToWithBudgetName(GroupRequest groupRequest) {
        this.id = groupRequest.getId();
        this.requestingUser = groupRequest.getRequestingUser();
        this.requestedUser = groupRequest.getRequestedUser();
        this.date = groupRequest.getDate();
        this.accepted = groupRequest.getAccepted();

        return this;
    }

    public GroupRequest withBudgetNameToGroupRequest() {
        final GroupRequest groupRequest = new GroupRequest();

        groupRequest.setId(this.id);
        groupRequest.setRequestingUser(this.requestingUser);
        groupRequest.setRequestedUser(this.requestedUser);
        groupRequest.setDate(this.date);
        groupRequest.setAccepted(this.accepted);

        return groupRequest;
    }

}
