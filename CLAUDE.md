# Claude Development Guide

This document provides essential information for using Claude to contribute to the TrueLayer Java SDK.

## Project Overview

@import README.md

## Build & Test Commands

Use `just` for all development tasks:

- `just build` - Build the project
- `just format` - Apply code formatting
- `just unit-test` - Run unit tests
- `just integration-test` - Run integration tests
- `just test` - Run all tests

Run `just -l` to see all available commands.


## Testing
- Unit tests in `src/test/java/com/truelayer/java/`
- Integration tests require mock responses in `src/test/resources/__files/`
- Acceptance tests require environment variables (see README)

## Pull Request Guidelines

When creating a PR, Claude will ask for a JIRA ticket reference if:
- The GitHub user is part of the Api Client Libraries team at TrueLayer
- The GitHub username has a `tl-` prefix
- When in doubt, an optional ACL ticket reference will be requested

Format: `[ACL-XXX]` in the PR title for JIRA ticket references.

### CI Optimization
When making changes that don't affect the Java artifact (CODEOWNERS, CI workflows, documentation, etc.), consider adding `[skip ci]` to the squash merge commit message to avoid unnecessary CI runs, following GitHub's best practices.


## Development Tips
- Check existing similar functionality before implementing new features
- Follow existing naming conventions
- Add unit tests for new functionality
- Use mock JSON responses for integration tests
- Ensure proper error handling and logging