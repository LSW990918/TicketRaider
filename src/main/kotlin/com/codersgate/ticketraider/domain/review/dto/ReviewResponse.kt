package com.codersgate.ticketraider.domain.review.dto

import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.member.entity.Member
import com.codersgate.ticketraider.domain.review.model.Review
import java.time.format.DateTimeFormatter

data class ReviewResponse(
    val id: Long,

    val modifiedAt: String,

    val title: String,

    val content: String,

    val nickname: String,

    val rating: Int,

    val memberId: Long,  //userId 보단 memberId로 바꾸는 게 좋을것 같음

    val eventId: Long,

    val eventTitle: String,

    ) {
    companion object {
        fun from(review: Review, event: Event, member: Member): ReviewResponse {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val FormattedDate = review.updatedAt!!.format(formatter)

            return ReviewResponse(
                id = review.id!!,
                modifiedAt = FormattedDate.toString(),
                title = review.title,
                content = review.content,
                nickname = member.nickname,
                rating = review.rating,
                memberId = member.id!!,
                eventId = event.id!!,
                eventTitle = event.title
            )
        }
    }
}