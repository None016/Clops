package com.example.Clops.service.mapper;


import com.example.Clops.dto.TerritoryRequest;
import com.example.Clops.dto.TerritoryResponse;
import com.example.Clops.entity.Territory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TerritoryMapper {

    public Territory toEntity(TerritoryRequest territoryRequest) {
        if (territoryRequest == null) {
            return null;
        }

        Territory territory = new Territory();
        territory.setName(territoryRequest.getName());
        territory.setDescription(territoryRequest.getDescription());
        return territory;
    }

    public TerritoryResponse toResponse(Territory territory) {
        if (territory == null) {
            return null;
        }

        TerritoryResponse response = new TerritoryResponse();
        response.setId(territory.getId());
        response.setName(territory.getName());
        response.setDescription(territory.getDescription());
        return response;
    }

    public void updateEntity(TerritoryRequest territoryRequest, Territory territory) {
        if (territoryRequest == null || territory == null) {
            return;
        }

        territory.setName(territoryRequest.getName());
        territory.setDescription(territoryRequest.getDescription());
    }
}
