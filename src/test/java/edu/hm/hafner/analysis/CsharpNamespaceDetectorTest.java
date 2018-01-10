package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import edu.hm.hafner.util.ResourceTest;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link CsharpNamespaceDetector}.
 */
class CsharpNamespaceDetectorTest extends ResourceTest {
    @ParameterizedTest
    @ValueSource(strings = {"ActionBinding.cs", "ActionBinding-Original-Formatting.cs"})
    void shouldExtractPackageNameFromCSharpSource(final String fileName) throws IOException {
        try (InputStream stream = asInputStream(fileName)) {
            assertThat(new CsharpNamespaceDetector().detectPackageName(stream))
                    .isEqualTo("Avaloq.SmartClient.Utilities");
        }
    }

    @Test
    void shouldFindNoPackageInPomFile() throws IOException {
        try (InputStream stream = asInputStream("pom.xml")) {
            assertThat(new CsharpNamespaceDetector().detectPackageName(stream))
                    .isEqualTo("-");
        }
    }

    @Test
    void shouldAcceptCorrectFileSuffix() {
        CsharpNamespaceDetector namespaceDetector = new CsharpNamespaceDetector();
        assertThat(namespaceDetector.accepts("ActionBinding.cs"))
                .as("Does not accept a C# file.").isTrue();
        assertThat(namespaceDetector.accepts("ActionBinding.cs.c"))
                .as("Accepts a non-C# file.").isFalse();
        assertThat(namespaceDetector.accepts("Action.java"))
                .as("Accepts a non-C# file.").isFalse();
        assertThat(namespaceDetector.accepts("pom.xml"))
                .as("Accepts a non-C# file.").isFalse();
    }
}

