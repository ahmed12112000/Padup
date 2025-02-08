package com.padelium.data.mappers

import com.padelium.data.dto.ProfileRequestDTO
import com.padelium.domain.dto.ProfileRequest
import javax.inject.Inject


class ProfileMapper @Inject constructor() {

    fun ProfileRequestToProfileRequestDto(profileRequest: ProfileRequest): ProfileRequestDTO {
        return ProfileRequestDTO(
            account = profileRequest.account.toString(),
            file = profileRequest.file

        )
    }
}