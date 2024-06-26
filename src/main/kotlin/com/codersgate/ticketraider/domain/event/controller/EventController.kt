package com.codersgate.ticketraider.domain.event.controller


import com.codersgate.ticketraider.domain.event.dto.EventRequest
import com.codersgate.ticketraider.domain.event.dto.EventResponse
import com.codersgate.ticketraider.domain.event.dto.price.PriceResponse
import com.codersgate.ticketraider.domain.event.service.EventService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/events")
class EventController(
    private val eventService: EventService
) {
    @Operation(summary = "가격 조회")
    @GetMapping("/price/{eventId}")
    fun getPrice(
        @PathVariable eventId: Long
    ): ResponseEntity<PriceResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(eventService.getPrice(eventId))
    }

    @Operation(summary = " 이벤트 생성")
    @PostMapping
    fun createEvent(
        @Valid @RequestBody eventRequest: EventRequest,
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(eventService.createEvent(eventRequest))
    }

    @Operation(summary = "이미지 업로드")
    @PostMapping("/imgUpload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadImage(
        @RequestPart(value = "file", required = true) file: MultipartFile
    ): ResponseEntity<String> {
        println("controller")
        println("uploadImage")
        println(file)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(eventService.uploadImage(file))
    }

    @Operation(summary = " 이벤트 수정")
    @PutMapping("/{eventId}")
    fun updateEvent(
        @PathVariable eventId: Long,
        @Valid @RequestBody eventRequest: EventRequest,
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(eventService.updateEvent(eventId, eventRequest))
    }

    @Operation(summary = "이벤트 삭제")
    @DeleteMapping("/{eventId}")
    fun deleteEvent(@PathVariable eventId: Long): ResponseEntity<Unit> {
        eventService.deleteEvent(eventId)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(eventService.deleteEvent(eventId))
    }

    @Operation(summary = "이벤트 목록 조회")
    @GetMapping
    fun getEventList(
        @PageableDefault(size = 5, sort = ["id"]) pageable: Pageable,
        @RequestParam(required = false, defaultValue = "rating") sortStatus: String,
        @RequestParam(required = false) searchStatus: String?,
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false) keyword: String?,
    ): ResponseEntity<Page<EventResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                eventService.getPaginatedEventList(
                    pageable,
                    sortStatus,
                    searchStatus,
                    category,
                    keyword
                )
            )
    }

    @Operation(summary = "이벤트 조회")
    @GetMapping("/{eventId}")
    fun getEvent(
        @PathVariable eventId: Long
    ): ResponseEntity<EventResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(eventService.getEvent(eventId))
    }
}




