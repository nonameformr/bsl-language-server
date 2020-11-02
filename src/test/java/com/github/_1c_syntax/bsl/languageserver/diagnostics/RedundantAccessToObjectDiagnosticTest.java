/*
 * This file is a part of BSL Language Server.
 *
 * Copyright © 2018-2020
 * Alexey Sosnoviy <labotamy@gmail.com>, Nikita Gryzlov <nixel2007@gmail.com> and contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * BSL Language Server is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * BSL Language Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with BSL Language Server.
 */
package com.github._1c_syntax.bsl.languageserver.diagnostics;

import com.github._1c_syntax.bsl.languageserver.context.DocumentContext;
import com.github._1c_syntax.bsl.languageserver.util.TestUtils;
import com.github._1c_syntax.utils.Absolute;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.eclipse.lsp4j.Diagnostic;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.github._1c_syntax.bsl.languageserver.util.Assertions.assertThat;
import static org.mockito.Mockito.spy;

class RedundantAccessToObjectDiagnosticTest extends AbstractDiagnosticTest<RedundantAccessToObjectDiagnostic> {
  RedundantAccessToObjectDiagnosticTest() {
    super(RedundantAccessToObjectDiagnostic.class);
  }

  @Test
  void test() {

    List<Diagnostic> diagnostics = getDiagnostics();

    assertThat(diagnostics).hasSize(10);
    assertThat(diagnostics, true)
      .hasRange(2, 4, 2, 14)
      .hasRange(3, 4, 3, 14)
      .hasRange(6, 18, 6, 28)
      .hasRange(7, 18, 7, 28)
      .hasRange(8, 18, 8, 28)
      .hasRange(10, 4, 10, 14)
      .hasRange(16, 4, 16, 14)
      .hasRange(17, 4, 17, 14)
      .hasRange(20, 15, 20, 25)
      .hasRange(22, 4, 22, 14);

  }

  @Test
  void testCommonModule() {
    var documentContext = createDocumentContextFromFile(
      "src/test/resources/metadata/CommonModules/ПервыйОбщийМодуль/Ext/Module.bsl"
    );
    List<Diagnostic> diagnostics = diagnosticInstance.getDiagnostics(documentContext);
    assertThat(diagnostics).hasSize(1);
    assertThat(diagnostics, true)
      .hasRange(75, 4, 75, 21);
  }

  @SneakyThrows
  DocumentContext createDocumentContextFromFile(String pathToFile) {
    Path path = Absolute.path("src/test/resources/metadata");
    Path testFile = Paths.get(pathToFile).toAbsolutePath();

    initServerContext(path);
    return spy(TestUtils.getDocumentContext(
      testFile.toUri(),
      FileUtils.readFileToString(testFile.toFile(), StandardCharsets.UTF_8),
      context
    ));
  }

  @Test
  void testCatalogs() {
    var documentContext = createDocumentContextFromFile(
      "src/test/resources/metadata/Catalogs/Справочник1/Ext/ManagerModule.bsl"
    );
    List<Diagnostic> diagnostics = diagnosticInstance.getDiagnostics(documentContext);
    assertThat(diagnostics).hasSize(1);
    assertThat(diagnostics, true)
      .hasRange(19, 4, 19, 15);
  }

  @Test
  void testInformationRegisters() {
    var documentContext = createDocumentContextFromFile(
      "src/test/resources/metadata/InformationRegisters/РегистрСведений1/Ext/ManagerModule.bsl"
    );
    List<Diagnostic> diagnostics = diagnosticInstance.getDiagnostics(documentContext);
    assertThat(diagnostics).hasSize(1);
    assertThat(diagnostics, true)
      .hasRange(18, 4, 18, 20);
  }
}
