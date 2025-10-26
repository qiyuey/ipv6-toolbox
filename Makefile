# IPv6 Toolbox - Simple Makefile

.PHONY: help desktop android ios build test clean

# Default target
help:
	@echo "IPv6 Toolbox - Build Commands"
	@echo ""
	@echo "  make desktop          - Run desktop app"
	@echo "  make android          - Build Android APK"
	@echo "  make ios              - Build iOS app (macOS only)"
	@echo "  make build            - Build all platforms"
	@echo "  make test             - Run tests"
	@echo "  make clean            - Clean build files"
	@echo ""

# Run desktop app
desktop:
	./gradlew :composeApp:run

# Build Android APK
android:
	./gradlew :composeApp:assembleDebug
	@echo "APK: composeApp/build/outputs/apk/debug/"

# Build iOS app (macOS only)
ios:
	@if [ "$$(uname)" = "Darwin" ]; then \
		open iosApp/iosApp.xcodeproj; \
	else \
		echo "iOS build requires macOS"; \
		exit 1; \
	fi

# Build all
build:
	./gradlew build

# Run tests
test:
	./gradlew test

# Clean
clean:
	./gradlew clean
