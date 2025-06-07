package com.github.fabiankevin.microwebspringbootstarter;

import com.github.fabiankevin.microwebspringbootstarter.web.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class MicrowebAutoConfigurationTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(MicrowebAutoConfiguration.class));

    @Test
    void globalExceptionHandler_shouldHasSingleBean() {
        this.contextRunner.withUserConfiguration(MicrowebAutoConfiguration.class).run((context) -> {
            assertThat(context).hasSingleBean(GlobalExceptionHandler.class);
        });
    }

}
