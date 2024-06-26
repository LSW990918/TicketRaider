package com.codersgate.ticketraider.domain.review.service

import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.member.repository.MemberRepository
import com.codersgate.ticketraider.domain.review.dto.CreateReviewRequest
import com.codersgate.ticketraider.domain.review.dto.ReviewResponse
import com.codersgate.ticketraider.domain.review.dto.UpdateReviewRequest
import com.codersgate.ticketraider.domain.review.model.Review
import com.codersgate.ticketraider.domain.review.repository.ReviewRepository
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
    private val memberRepository: MemberRepository,
    private val eventRepository: EventRepository,
) : ReviewService {

    @Transactional
    override fun createReview(memberId: Long, request: CreateReviewRequest) {

        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw NotFoundException()

        // 티켓 내역 확인
        val ticket = member.tickets.find {
            it.event.id == request.eventId
//                    && it.ticketStatus == TicketStatus.EXPIRED
        } ?: throw IllegalStateException("Expired ticket not found for event id: ${request.eventId}")

        val review = reviewRepository.save(
            Review(
                request.title,
                request.content,
                request.rating,
                member,
                ticket.event,
            )
        )
        review.event.addRating(request.rating)
    }

    override fun getReviewList_V2(pageable: Pageable, memberId: Long?, eventId: Long?): Page<ReviewResponse> {
        return reviewRepository.getReviewList_V2(pageable, memberId, eventId).map {
            val event = eventRepository.findByIdOrNull(it.event.id)
                ?: throw ModelNotFoundException("review", it.event.id)
            val member = memberRepository.findByIdOrNull(it.member.id)
                ?: throw ModelNotFoundException("review", it.member.id)
            ReviewResponse.from(it, event, member)
        }
    }

    override fun getReviewList(pageable: Pageable): Page<ReviewResponse> {
        return reviewRepository.getReviewList(pageable).map {
            val event = eventRepository.findByIdOrNull(it.event.id)
                ?: throw ModelNotFoundException("review", it.event.id)
            val member = memberRepository.findByIdOrNull(it.member.id)
                ?: throw ModelNotFoundException("review", it.member.id)
            ReviewResponse.from(it, event, member)
        }
    }

    override fun getReviewListByEvent(pageable: Pageable, eventId: Long): Page<ReviewResponse> {
        return reviewRepository.getReviewListByEventId(pageable, eventId).map {
            val event = eventRepository.findByIdOrNull(it.event.id)
                ?: throw ModelNotFoundException("review", it.event.id)
            val member = memberRepository.findByIdOrNull(it.member.id)
                ?: throw ModelNotFoundException("review", it.member.id)
            ReviewResponse.from(it, event, member)
        }
    }

    override fun getReviewListByUser(pageable: Pageable, memberId: Long): Page<ReviewResponse> {
        return reviewRepository.getReviewListByUserId(pageable, memberId).map {
            val event = eventRepository.findByIdOrNull(it.event.id)
                ?: throw ModelNotFoundException("review", it.event.id)
            val member = memberRepository.findByIdOrNull(it.member.id)
                ?: throw ModelNotFoundException("review", it.member.id)
            ReviewResponse.from(it, event, member)
        }
    }

    override fun getReview(reviewId: Long): ReviewResponse {
        val review = reviewRepository.findByIdOrNull(reviewId)
            ?: throw ModelNotFoundException("review", reviewId)
        val event = eventRepository.findByIdOrNull(review.event.id)
            ?: throw ModelNotFoundException("review", review.event.id)
        val member = memberRepository.findByIdOrNull(review.member.id)
            ?: throw ModelNotFoundException("review", review.member.id)

        return ReviewResponse.from(review, event, member)
    }

    @Transactional
    override fun updateReview(reviewId: Long, request: UpdateReviewRequest) {
        val review = reviewRepository.findByIdOrNull(reviewId)
            ?: throw NotFoundException()
        review.event.deleteRating(review.rating)
        review.event.addRating(request.rating)

        review.title = request.title
        review.content = request.content
        review.rating = request.rating

    }

    override fun deleteReview(reviewId: Long) {
        val review = reviewRepository.findByIdOrNull(reviewId)
            ?: throw NotFoundException()
        review.event.deleteRating(review.rating)
        reviewRepository.delete(review)
    }
}