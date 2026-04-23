package com.backend.housing.application.commands.payments;

import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import lombok.Getter;

import java.util.Objects;

@Getter
public class InitiatePaymentCommand {

    private final ContractId contractId;
    private final Long tenantId;

    private InitiatePaymentCommand(Builder builder) {
        this.contractId = Objects.requireNonNull(builder.contractId, "ContractId is required");
        this.tenantId = Objects.requireNonNull(builder.tenantId, "TenantId is required");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ContractId contractId;
        private Long tenantId;

        public Builder contractId(ContractId contractId) {
            this.contractId = contractId;
            return this;
        }

        public Builder tenantId(Long tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public InitiatePaymentCommand build() {
            return new InitiatePaymentCommand(this);
        }
    }
}