package vstu.isd.userservice.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import vstu.isd.userservice.dto.FindUserRequestDto
import vstu.isd.userservice.dto.SubscribeDto
import vstu.isd.userservice.dto.SubscribeUserRequestDto
import vstu.isd.userservice.entity.Subscribe
import vstu.isd.userservice.exception.SubscribeNotUniqueException
import vstu.isd.userservice.exception.UserNotFoundException
import vstu.isd.userservice.mapper.toDto
import vstu.isd.userservice.mapper.toEntity
import vstu.isd.userservice.repository.SubscribeRepository
import vstu.isd.userservice.repository.UserRepository
import vstu.isd.userservice.validator.SubscribeValidator

@Service
class SubscribeService (
    private val subscribeValidator: SubscribeValidator,
    private val subscribeRepository: SubscribeRepository,
    private val userRepository: UserRepository,
){
    @Transactional
    fun subscribe(requestDto: SubscribeUserRequestDto): SubscribeDto {
        subscribeValidator.validateSubscribeUserRequestDto(requestDto).ifPresent { throw it }

        val subscribe: Subscribe = requestDto.toEntity()

        if (userRepository.findById(requestDto.subscriberId).isEmpty) {
            throw UserNotFoundException(FindUserRequestDto(requestDto.subscriberId, null))
        }

        if (userRepository.findById(requestDto.authorId).isEmpty) {
            throw UserNotFoundException(FindUserRequestDto(requestDto.authorId, null))
        }

        if (subscribeRepository.findByAuthorAndSubscriber(requestDto.authorId, requestDto.subscriberId).isPresent) {
            throw SubscribeNotUniqueException(requestDto.subscriberId, requestDto.authorId)
        }

        return subscribeRepository.save(subscribe).toDto()
    }
}