package com.smoothstack.branchservice.mapper;

import com.smoothstack.branchservice.dto.BankerDTO;
import com.smoothstack.branchservice.model.Banker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankerMapper {

    @Mapping(target = "bankerId", ignore = true)
    Banker bankerDTOToBanker(BankerDTO bankerDTO);

    @Mapping(source = "bankerId", target = "bankerId")
    BankerDTO bankerToBankerDTO(Banker banker);

    List<BankerDTO> bankerListToBankerDTOList(List<Banker> bankers);

}
