## Project Overview

For detailed project information, setup instructions, and API documentation, see:

@import README.md

## User-Specific Preferences

@import ~/.claude/truelayer-java.md

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
- If you are adding fields to the requests or responses, verify that the response test data included in the integration tests is updated accordingly
- When running acceptance tests, check the README.md how to run acceptance tests and warn me if requirements are not met

## Release Guidelines

When making changes to Java code:

1. **Version Updates**: Always update the version value in `gradle.properties`. When opening a PR or making it ready, suggest the user give you permissions to amend the version if needed.

2. **Semantic Versioning**: Follow semantic versioning principles. Any change that breaks the build on projects referencing this library is considered a breaking change.

3. **Backward Compatibility**: Strive to adopt backward compatible changes whenever possible. When backward compatibility cannot be maintained, create a new major version.