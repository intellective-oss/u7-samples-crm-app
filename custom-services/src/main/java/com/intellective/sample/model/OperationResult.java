package com.intellective.sample.model;

import java.util.Collections;
import java.util.Map;

public class OperationResult {

	public static OperationResult success(Map<String, Object> data) {
		return new OperationResult().withPayload(data);
	}

	private OperationResult withPayload(Map<String, Object> data) {
		this.payload = Collections.unmodifiableMap(data);
		return this;
	}

	public static OperationResult error(String errorMessage) {
		return new OperationResult(errorMessage);
	}
	
	private boolean success = true;
	private String errorMessage;
	private Map<String, Object> payload;

	public OperationResult() {
	}

	public OperationResult(String errorMessage) {
		this.success = false;
		this.errorMessage = errorMessage;
	}

	public boolean isSuccess() {
		return success;
	}

	public Map<String, Object> getPayload() {
		return payload;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
