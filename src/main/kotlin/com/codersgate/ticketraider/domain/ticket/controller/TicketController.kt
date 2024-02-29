package com.codersgate.ticketraider.domain.ticket.controller

import com.codersgate.ticketraider.domain.ticket.dto.CreateTicketRequest
import com.codersgate.ticketraider.domain.ticket.dto.TicketResponse
import com.codersgate.ticketraider.domain.ticket.service.TicketService
import com.codersgate.ticketraider.global.infra.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tickets")
class TicketController(
    val ticketService: TicketService
) {
    @Operation(summary = "티켓 생성")
    @PostMapping
    fun createTicket(
        @Valid @RequestBody request: CreateTicketRequest
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ticketService.createTicket(request))
    }

    @Operation(summary = "티켓 단건 조회")
    @GetMapping("/{ticketId}")
    fun getTicketById(
        @PathVariable ticketId: Long
    ): ResponseEntity<TicketResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ticketService.getTicketById(ticketId))
    }

    @Operation(summary = "멤버 티켓리스트 조회")
    @GetMapping("/get/{userId}")
    fun getTicketListByUserId(
        @PageableDefault(
            size = 15, sort = ["id"]
        ) pageable: Pageable,
        @PathVariable userId: Long
    ): ResponseEntity<Page<TicketResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ticketService.getTicketListByUserId(userId, pageable))
    }

    // 티켓 수정 : 과정 정리 필요
//    @Operation(summary = "티켓 수정")
//    @PutMapping
//    fun updateTicket(
//        @Valid @RequestBody request: CreateTicketRequest
//    ): ResponseEntity<Unit> {
//        return ResponseEntity
//            .status(HttpStatus.CREATED)
//            .body(ticketService.updateTicket(request))
//    }
    @Operation(summary = "티켓 삭제")
    @DeleteMapping("/delete/{ticketId}")
    fun deleteTicket(
        @PathVariable ticketId: Long,
        @AuthenticationPrincipal user: UserPrincipal
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ticketService.deleteTicket(ticketId, user))
    }


}