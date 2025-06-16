# Claude Development Guide

This document provides essential information for using Claude to contribute to the TrueLayer Java SDK.

## Project Overview

This is the official TrueLayer Java client library that provides convenient access to TrueLayer APIs. It supports:
- Payments processing
- Mandates management
- Merchant accounts
- Payouts
- Payment providers
- Signup Plus functionality

## Build & Test Commands

Use `just` for all development tasks:

- `just build` - Build the project
- `just format` - Apply code formatting
- `just unit-test` - Run unit tests
- `just integration-test` - Run integration tests
- `just test` - Run all tests

Run `just -l` to see all available commands.

## Code Style
- Uses Palantir Java Format via Spotless
- **Always run `just format` before committing**
- Formatting is enforced in CI

## Project Structure

### Core Packages
- `com.truelayer.java.auth` - Authentication handling
- `com.truelayer.java.payments` - Payment operations
- `com.truelayer.java.mandates` - Mandate operations
- `com.truelayer.java.payouts` - Payout operations
- `com.truelayer.java.merchantaccounts` - Merchant account operations
- `com.truelayer.java.paymentsproviders` - Payment provider operations
- `com.truelayer.java.http` - HTTP client and interceptors
- `com.truelayer.java.entities` - Data models and DTOs

### Key Files
- `TrueLayerClient.java` - Main client entry point
- `TrueLayerClientBuilder.java` - Client configuration
- `Environment.java` - Environment configuration (sandbox/production)

## Testing
- Unit tests in `src/test/java/com/truelayer/java/`
- Integration tests require mock responses in `src/test/resources/__files/`
- Acceptance tests require environment variables (see README)

## Common Patterns
- Use builder pattern for request objects
- All API calls return `CompletableFuture<ApiResponse<T>>`
- Error handling via `ApiResponse` wrapper
- Retrofit for HTTP client
- OkHttp for connection management

## Pull Request Guidelines

When creating a PR, Claude will ask for a JIRA ticket reference if:
- The GitHub user is part of the Api Client Libraries team at TrueLayer
- The GitHub username has a `tl-` prefix
- When in doubt, an optional ACL ticket reference will be requested

Format: `[ACL-XXX]` in the PR title for JIRA ticket references.

## API Development Guidelines

When developing new features or modifying HTTP endpoint integrations:

1. **Check TrueLayer's API Specifications**
   - Visit https://docs.truelayer.com/reference/welcome-api-reference
   - Download the relevant OpenAPI (OAS) specification files for:
     - Authentication server
     - Payments API v3
     - Data API v1
     - Client Tracking API
     - Signup+
     - Verification API
   - Use these specs to understand endpoint details, parameters, and data models

2. **API Integration Best Practices**
   - Match request/response models exactly to the OpenAPI spec
   - Follow existing patterns in the codebase for similar endpoints
   - Ensure proper error handling for all documented error responses
   - Add comprehensive integration tests with mock responses

## Development Tips
- Check existing similar functionality before implementing new features
- Follow existing naming conventions
- Add unit tests for new functionality
- Use mock JSON responses for integration tests
- Ensure proper error handling and logging