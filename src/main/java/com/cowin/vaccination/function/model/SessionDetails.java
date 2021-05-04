package com.cowin.vaccination.function.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionDetails {
	private Integer minAgeLimit;
	private Integer availableCapacity;
	private String vaccine;
	private List<String> availableSlots;
}
